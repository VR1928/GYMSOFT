package ij.plugin;
import java.io.*;
import java.util.*;
import java.net.URL;
import ij.*;
import ij.io.*;
import ij.process.*;
import ij.util.Tools;
import ij.measure.Calibration;

/** This plugin decodes DICOM files. If 'arg' is empty, it
	displays a file open dialog and opens and displays the 
	image selected by the user. If 'arg' is a path, it opens the 
	specified image and the calling routine can display it using
	"((ImagePlus)IJ.runPlugIn("ij.plugin.DICOM", path)).show()".
	*/

/* RAK (Richard Kirk, rak@cre.canon.co.uk) changes 14/7/99

   InputStream.skip() looped to check the actual number of
   bytes is read.

   Big/little-endian options on element length.

   Explicit check for each known VR to make mistaken identifications
   of explicit VR's less likely.

   Variables b1..b4 renamed as b0..b3.

   Increment of 4 to offset on (7FE0,0010) tag removed.

   Queries on some other unrecognized tags.
   Anyone want to claim them?

   RAK changes 15/7/99

   Bug fix on magic values for explicit VRs with 32-bit lengths.

   Various bits of tidying up, including...
   'location' incremented on read using getByte() or getString().
   simpler debug mode message generation (values no longer reported).

   Added z pixel aspect ratio support for multi-slice DICOM volumes.
   Michael Abramoff, 31-10-2000

   Added DICOM tags to the dictionary (now contains about 2700 tags).
   implemented getDouble() for VR = FD (Floating Double) and getFloat()
   for VR = FL (Floating Single).
   Extended case statement in getHeaderInfo to retrieve FD and FL values.
   Johannes Hermen, Christian Moll, 25-04-2008

   */

public class DICOM extends ImagePlus implements PlugIn {
	private boolean showErrors = true;
	private boolean gettingInfo;
	private BufferedInputStream inputStream;
	private String info;
	
	/** Default constructor. */
	public DICOM() {
	}

	/** Constructs a DICOM reader that using an InputStream. Here 
		is an example that shows how to open and display a DICOM:
		<pre>
		DICOM dcm = new DICOM(is);
		dcm.run("Name");
		dcm.show();
		<pre>
	*/
	public DICOM(InputStream is) {
		this(new BufferedInputStream(is));
	}

	/** Constructs a DICOM reader that using an BufferredInputStream. */
	public DICOM(BufferedInputStream bis) {
		inputStream = bis;
	}

	public void run(String arg) {
		OpenDialog od = new OpenDialog("Open Dicom...", arg);
		String directory = od.getDirectory();
		String fileName = od.getFileName();
		if (fileName==null)
			return;
		//IJ.showStatus("Opening: " + directory + fileName);
		DicomDecoder dd = new DicomDecoder(directory, fileName);
		dd.inputStream = inputStream;
		FileInfo fi = null;
		try {
			fi = dd.getFileInfo();
		} catch (IOException e) {
			String msg = e.getMessage();
			IJ.showStatus("");
			if (msg.indexOf("EOF")<0&&showErrors) {
				IJ.error("DICOM Reader", e.getClass().getName()+"\n \n"+msg);
				return;
			} else if (!dd.dicmFound()&&showErrors) {
				msg = "This does not appear to be a valid\n"
				+ "DICOM file. It does not have the\n"
				+ "characters 'DICM' at offset 128.";
				IJ.error("DICOM Reader", msg);
				return;
			}
		}
		if (gettingInfo) {
			info = dd.getDicomInfo();
			return;
		}
		if (fi!=null && fi.width>0 && fi.height>0 && fi.offset>0) {
			FileOpener fo = new FileOpener(fi);
			ImagePlus imp = fo.openImage();
			ImageProcessor ip = imp.getProcessor();
			if (Prefs.openDicomsAsFloat) {
				ip = ip.convertToFloat();
				if (dd.rescaleSlope!=1.0)
					ip.multiply(dd.rescaleSlope);
				if (dd.rescaleIntercept!=0.0)
					ip.add(dd.rescaleIntercept);
				imp.setProcessor(ip);
			} else if (fi.fileType==FileInfo.GRAY16_SIGNED) {
				if (dd.rescaleIntercept!=0.0 && dd.rescaleSlope==1.0)
					ip.add(dd.rescaleIntercept);
			} else if (dd.rescaleIntercept!=0.0 && (dd.rescaleSlope==1.0||fi.fileType==FileInfo.GRAY8)) {
				double[] coeff = new double[2];
				coeff[0] = dd.rescaleIntercept;
				coeff[1] = dd.rescaleSlope;
				imp.getCalibration().setFunction(Calibration.STRAIGHT_LINE, coeff, "Gray Value");
			}
			if (dd.windowWidth>0.0) {
				double min = dd.windowCenter-dd.windowWidth/2;
				double max = dd.windowCenter+dd.windowWidth/2;
				if (Prefs.openDicomsAsFloat) {
					min -= dd.rescaleIntercept;
					max -= dd.rescaleIntercept;
				} else {
					Calibration cal = imp.getCalibration();
					min = cal.getRawValue(min);
					max = cal.getRawValue(max);
				}
				ip.setMinAndMax(min, max);
				if (IJ.debugMode) IJ.log("window: "+min+"-"+max);
			}
			if (imp.getStackSize()>1)
				setStack(fileName, imp.getStack());
			else
				setProcessor(fileName, imp.getProcessor());
			setCalibration(imp.getCalibration());
			setProperty("Info", dd.getDicomInfo());
			setFileInfo(fi); // needed for revert
			if (arg.equals("")) show();
		} else if (showErrors)
			IJ.error("DICOM Reader","Unable to decode DICOM header.");
		IJ.showStatus("");
	}

	/** Opens the specified file as a DICOM. Does not 
		display a message if there is an error.
		Here is an example:
		<pre>
		DICOM dcm = new DICOM();
		dcm.open(path);
		if (dcm.getWidth()==0)
			IJ.log("Error opening '"+path+"'");
		else
			dcm.show();
		</pre>
	*/
	public void open(String path) {
		showErrors = false;
		run(path);
	}
	
	/** Returns the DICOM tags of the specified file as a string. */ 
	public String getInfo(String path) {
		showErrors = false;
		gettingInfo = true;
		run(path);
		return info;
	}

	/** Convert 16-bit signed to unsigned if all pixels>=0. */
	void convertToUnsigned(ImagePlus imp, FileInfo fi) {
		ImageProcessor ip = imp.getProcessor();
		short[] pixels = (short[])ip.getPixels();
		int min = Integer.MAX_VALUE;
		int value;
		for (int i=0; i<pixels.length; i++) {
			value = pixels[i]&0xffff;
			if (value<min)
				min = value;
		}
		if (IJ.debugMode) IJ.log("min: "+(min-32768));
		if (min>=32768) {
			for (int i=0; i<pixels.length; i++)
				pixels[i] = (short)(pixels[i]-32768);
			ip.resetMinAndMax();
			Calibration cal = imp.getCalibration();
			cal.setFunction(Calibration.NONE, null, "Gray Value");
			fi.fileType = FileInfo.GRAY16_UNSIGNED;
		}
	}

}




