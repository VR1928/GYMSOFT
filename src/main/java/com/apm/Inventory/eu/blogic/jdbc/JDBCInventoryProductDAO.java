package com.apm.Inventory.eu.blogic.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.apm.Accounts.eu.entity.Accounts;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.DiaryManagement.eu.entity.Priscription;
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.HouseKeeping.eu.bi.LaundryDAO;
import com.apm.HouseKeeping.eu.bi.blogic.jdbc.JDBCLaundryDAO;
import com.apm.Inventory.eu.bi.IndentDAO;
import com.apm.Inventory.eu.bi.InventoryCatalogueDAO;
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.bi.InventoryVendorDAO;
import com.apm.Inventory.eu.bi.PoPaymenytDAO;
import com.apm.Inventory.eu.bi.ProcurementDAO;
import com.apm.Inventory.eu.entity.Procurement;
import com.apm.Inventory.eu.entity.Product;
import com.apm.Inventory.eu.entity.Vendor;
import com.apm.Master.eu.entity.Master;
import com.apm.Master.eu.entity.MedicineType;
import com.apm.Payroll.web.form.AllowanceForm;
import com.apm.Pharmacy.eu.bi.PharmacyDAO;
import com.apm.Pharmacy.eu.blogic.jdbc.JDBCPharmacyDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.common.helper.LoginInfo;


public class JDBCInventoryProductDAO implements InventoryProductDAO {

	Connection connection;

	public JDBCInventoryProductDAO(Connection connection) {

		this.connection = connection;
	}

	public ArrayList<Product> getAllCategories(Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();

		String sql = "";
		try {
			if (pagination == null) {
				sql = "select id, name, description from inventory_category";
			} else {
				sql = "select id, name, description from inventory_category";
				sql = pagination.getSQLQuery(sql);
			}
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));
				product.setDescription(rs.getString(3));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int countAllCategories() {

		int result = 0;

		try {
			String sql = "select count(*) from inventory_category";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int addCategory(Product product) {

		int result = 0;

		try {
			String sql = "insert into inventory_category (name,description,warehouse) values (?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setString(3, product.getWarehouse_id());

			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public Product getCategory(String id) {

		Product product = new Product();

		try {

			String sql = "select name,description from inventory_category where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				product.setName(rs.getString(1));
				product.setDescription(rs.getString(2));
				product.setId(Integer.parseInt(id));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return product;
	}

	public int updateCategory(Product product) {

		int result = 0;

		try {

			String sql = "update inventory_category  set  name=?,description=?,warehouse=? where id=" + product.getId()
					+ "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setString(3, product.getWarehouse_id());
			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public int deleteCategory(String id) {

		int result = 0;

		try {

			String sql = "delete from inventory_category where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getAllSubCategoryList(Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();

		String sql = "";
		try {
			if (pagination == null) {

				sql = "select id, category_id, name, description from inventory_subcategory";
			} else {
				sql = "select id, category_id, name, description from inventory_subcategory";
				sql = pagination.getSQLQuery(sql);
			}
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				Product product2 = getCategory(rs.getString(2));
				product.setCategory(product2.getName());
				product.setCategory_id(rs.getString(2));
				product.setName(rs.getString(3));
				product.setDescription(rs.getString(4));
				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public Product getSubCategory(String id) {

		Product product = new Product();

		try {

			String sql = "select category_id, name, description,isstrip from inventory_subcategory where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				product.setCategory_id(rs.getString(1));
				product.setName(rs.getString(2));
				product.setDescription(rs.getString(3));
				Product product2 = getCategory(product.getCategory_id());
				product.setCategory(product2.getName());
				product.setId(Integer.parseInt(id));
				String CheckStrip = rs.getString(4);
				if (CheckStrip.equals("0")) {
					product.setChecked(false);
				} else {
					product.setChecked(true);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return product;
	}

	public int updateSubCategory(Product product) {

		int result = 0;

		try {

			String sql = "update inventory_subcategory set category_id=?,name=?,description=?,isstrip=? where id="
					+ product.getId() + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getCategory_id());
			ps.setString(2, product.getName());
			ps.setString(3, product.getDescription());
			ps.setString(4, product.getStripcheck());

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int updateSubCategoryOnly(Product product) {

		int result = 0;

		try {

			String sql = "update inventory_subcategory set name=?,description=? where id=" + product.getId() + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int deleteSubCategory(String id) {

		int result = 0;

		try {
			String sql = "delete from inventory_subcategory where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int getTotalSubCategoryCount() {
		int result = 0;

		try {
			String sql = "select count(*) from inventory_subcategory";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int addSubCategory(Product product) {

		int result = 0;

		try {

			String sql = "insert into inventory_subcategory (category_id, name, description) values (?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getCategory_id());
			ps.setString(2, product.getName());
			ps.setString(3, product.getDescription());

			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getAllBrandList(Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();

		String sql = "";
		try {

			if (pagination == null) {

				sql = "select id, category_id, subcategory_id, name, description from inventory_brandname order by id desc";
			} else {
				sql = "select id, category_id, subcategory_id, name, description from inventory_brandname order by id desc";
				sql = pagination.getSQLQuery(sql);
			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setCategory_id(rs.getString(2));
				product.setSubcategory_id(rs.getString(3));

				Product category = getCategory(product.getCategory_id());
				Product subcategory = getSubCategory(product.getSubcategory_id());

				product.setCategory(category.getName());
				product.setSubcategory(subcategory.getName());
				product.setName(rs.getString(4));
				product.setDescription(rs.getString(5));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int getTotalBrandCount() {
		int result = 0;

		try {
			String sql = "select count(*) from inventory_brandname";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int addBrandname(Product product) {

		int result = 0;

		try {

			String sql = "insert into inventory_brandname (category_id, name, description,vendorid) values (?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getCategory_id());
			ps.setString(2, product.getName());
			ps.setString(3, product.getDescription());
			ps.setString(4, product.getVendor());

			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public Product getBrandName(String id) {

		Product product = new Product();

		try {
			String sql = "select category_id, subcategory_id, name, description,vendorid from inventory_brandname where id="
					+ id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				product.setCategory_id(rs.getString(1));
				product.setSubcategory_id(rs.getString(2));

				Product category = getCategory(product.getCategory_id());
				Product subcategory = getSubCategory(product.getSubcategory_id());

				product.setCategory(category.getName());
				product.setSubcategory(subcategory.getName());
				product.setName(rs.getString(3));
				product.setDescription(rs.getString(4));
				product.setVendor(rs.getString(5));

				product.setId(Integer.parseInt(id));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return product;
	}

	public int updateBrandName(Product product) {

		int result = 0;

		try {

			String sql = "update inventory_brandname set category_id=?,subcategory_id=?,name=?,description=? where id="
					+ product.getId() + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getCategory_id());
			ps.setString(2, product.getSubcategory_id());
			ps.setString(3, product.getName());
			ps.setString(4, product.getDescription());

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int deleteBrandname(String id) {

		int result = 0;

		try {
			String sql = "delete from inventory_brandname where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getSubCategoryList(String category_id) {
		ArrayList<Product> list = new ArrayList<Product>();

		try {

			String sql = "select id, category_id, name, description from inventory_subcategory where category_id='"
					+ category_id + "'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				Product product2 = getCategory(rs.getString(2));
				product.setCategory(product2.getName());
				product.setCategory_id(rs.getString(2));
				product.setName(rs.getString(3));
				product.setDescription(rs.getString(4));
				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Product> getAllCateoryandSub() {

		ArrayList<Product> list = new ArrayList<Product>();

		try {

			String sql = "select id,name from inventory_category";

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));

				ArrayList<Product> subcategoryList = getSubCategoryList(String.valueOf(product.getId()));

				product.setSubcategoryList(subcategoryList);
				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int addProduct(Product product) {

		int result = 0;

		try {
			
			//Akash 03 May 2018
			String added_date="";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			added_date = dateFormat.format(cal.getTime());
			added_date = DateTimeUtils.getCommencingDate1(added_date);
			
			StringBuffer sql = new StringBuffer(
					"insert into inventory_product (prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,expiry_date,tax,userid,lastmodified,mdicinenameid,added_date)");
			sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, product.getSubcategory_id());
			ps.setString(2, product.getVendor_id());
			ps.setString(3, product.getBrand_id());
			ps.setString(4, product.getProduct_code());
			ps.setString(5, product.getProduct_name());
			ps.setString(6, product.getMrp());
			ps.setString(7, product.getPurchase_price());
			ps.setString(8, product.getSale_price());
			ps.setString(9, product.getPurchase_discount());
			ps.setString(10, product.getSale_discount());
			ps.setString(11, product.getWeight());
			ps.setString(12, product.getUnit());
			ps.setString(13, product.getDescription());
			ps.setString(14, product.getCategory_id());
			ps.setString(15, product.getExpiry_date());
			ps.setString(16, product.getTax());
			ps.setString(17, product.getUserid());
			ps.setString(18, product.getLastmodified());
			ps.setString(19, product.getMedicinenameid());
			ps.setString(20, added_date);
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getAllProducts(Pagination pagination, int i, String commencing, String categoryid,
			String searchtext, String medicine_shedule, String location, LoginInfo loginInfo,String report_filter,String withstock_filter,String orderby, String product_type) {

		ArrayList<Product> list = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		String sql = "";
		if (categoryid == null) {
			categoryid = "2";
		}

		Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(loginInfo.getUserId());

		try {

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("select inventory_product.id,inventory_product.catalogueid,inventory_product.mrp, inventory_product.purchaseprice, inventory_product.saleprice,stock,");
			stringBuilder.append("inventory_product.expiry_date,inventory_product.batch_no,inventory_product.location,inventory_product.prodtypeid,");
			stringBuilder.append("inventory_catalogue.product_name,inventory_catalogue.genericname,inventory_subcategory.name,inventory_product.cell,inventory_product.pack,inventory_product.vat,inventory_catalogue.shedule,inventory_catalogue.product_code, ");
			stringBuilder.append("inventory_catalogue.minorder,inventory_catalogue.maxorder from inventory_product ");
			stringBuilder.append("inner join inventory_catalogue on inventory_catalogue.id=inventory_product.catalogueid ");
			stringBuilder.append("left join inventory_subcategory on inventory_subcategory.id=inventory_catalogue.subcategoryid ");
			stringBuilder.append("where inventory_product.catalogueid!=0 and inventory_product.procurement=0  "); //and inventory_product.stock>0
			if (!location.equals("0")) {
				stringBuilder.append("and inventory_product.location=" + location + " ");
			}
			if (searchtext != null) {
				stringBuilder.append("and (inventory_product.prodname like ('%" + searchtext + "%') or inventory_catalogue.product_name like ('%" + searchtext
						+ "%')  or inventory_product.genericname like ('%" + searchtext + "%') or inventory_catalogue.genericname like ('%" + searchtext + "%') or inventory_subcategory.name like ('" + searchtext + "%') or inventory_catalogue.product_code like ('%" + searchtext + "%')  ) ");
			}
			
			if(!categoryid.equals("0")){
				stringBuilder.append("and inventory_product.categoryid="+categoryid+"  ");
			}
			if(!product_type.equals("0")){
				stringBuilder.append("and inventory_catalogue.shedule='"+product_type+"' ");
			}
			
			if(withstock_filter.equals("2")){
				stringBuilder.append("and stock>0 ");
			}else if(withstock_filter.equals("3")){
				stringBuilder.append("and stock=0 ");
			}else{
				stringBuilder.append("and stock>=0 ");
			}
			stringBuilder.append(" order by inventory_product.id desc ");
			
			//lokesh
			
			/*if ("2".equals(withstock_filter)) {
				stringBuilder.append("and stock>=0 ");
			}else{
				stringBuilder.append("and stock>0 ");
			}*/
		
			

			/*if("1".equals(report_filter)){
				stringBuilder.append("group by inventory_product.catalogueid ");
			}else{
				stringBuilder.append("group by inventory_product.catalogueid,batch_no ");
			}*/
			/*if("1".equals(orderby)){
				stringBuilder.append("order by inventory_catalogue.product_name asc ");
			}else if("2".equals(orderby)){
				stringBuilder.append("order by sum(stock) desc ");
			}else if("3".equals(orderby)){
				stringBuilder.append("order by (inventory_product.mrp+0) desc ");
			}*/
			

			/*stringBuilder.append("order by id desc ");*/

			if (pagination != null) {
				sql = pagination.getSQLQuery(stringBuilder.toString());
			} else {
				sql = stringBuilder.toString();
			}

			
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			String currdate=dateFormat.format(calendar.getTime());
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setCatalogueid(rs.getString(2));
				Product master = getProductCatalogueDetails(product.getCatalogueid());
				String name = master.getProduct_name() + " (" + master.getGenericname() + ")";
				/*String name = "";
				if(rs.getString(18)!=null){
					name = master.getProduct_name() + " (" + master.getGenericname() + ")" + "-" +rs.getString(18);
				}else{
					name = master.getProduct_name() + " (" + master.getGenericname() + ")";
				}*/
				product.setProduct_name(name);
				product.setMrp(DateTimeUtils.changeFormat(rs.getDouble(3)));
				product.setPurchase_price(DateTimeUtils.changeFormat(rs.getDouble(4)));
				product.setSale_price(DateTimeUtils.changeFormat(rs.getDouble(5)));
				product.setStock(rs.getString(6));
				product.setMfg(master.getMfg());
				String expirydate = DateTimeUtils.getCommencingDate1(rs.getString(7));
				product.setExpiry_date(expirydate);
				product.setColor("");
				if(rs.getString(17)!=null){
					if(rs.getString(17).equals("Narcotics")){
						product.setShedule("Narcotics");
						product.setColor("#e05d6f");
					}else if(rs.getString(17).equals("H1")){
							product.setShedule("H1");
							product.setColor("#e69522");
					}else if(rs.getString(17).equals("High Risk Medicine")){
						product.setShedule("High Risk Medicine");
						product.setColor("#9381cc");
					}else if(rs.getString(17).equals("Vaccination")){
						product.setShedule("Vaccination");
						product.setColor("#e0acdafc");
					}else{
						product.setShedule("");
					}
				}else{
						product.setShedule("");
				}
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar expireCalendar = Calendar.getInstance();
				
				expirydate = rs.getString(7);
				Date date2=format.parse(currdate);
				if (expirydate != null) {

					if (!expirydate.equals("")) {
						Date date = format.parse(expirydate);
						expireCalendar.setTime(date);
						long diff=date.getTime()-date2.getTime();
						diff = (diff / (1000*60*60*24));
						if(diff<45){
							product.setColor("#f20404");
						}/*else if(product.getShedule().equals("Narcotics")){
							product.setColor("#1c15ea");
						}else{
							product.setColor("");
						}*/
					}/*else{
					 	product.setColor("");
					}*/
				}/*else{
					product.setColor("");
				}*/
				
				/*if(product.getColor().equals("#f20404")){
					
				}else if(product.getShedule().equals("Narcotics")){
					product.setColor("#1c15ea");
				}
				else{
					product.setColor("");
				}*/
				product.setBatch_no(rs.getString(8));
				String locid = rs.getString(9);
				product.setLocation(locid);
				product.setLocationName(pharmacyDAO.getLocationName(locid));
				product.setEdit_catalogue(priscription.getEdit_catalogue());
				Product subcategory = getSubCategory(master.getSubcategory_id());
				product.setSubcategory(subcategory.getName());
				String shelf ="";
				if(rs.getString(14)==null){
					shelf = "";
				}else{
					shelf = rs.getString(14);
				}
				product.setShelf(shelf);
				product.setPack(rs.getString(15));
				product.setVat(rs.getString(16));
				String pack ="1";
				if(rs.getString(15)!=null){
					if(!rs.getString(15).equals("")){
						pack = rs.getString(15);
					}
				}
				double purpriceqty = (rs.getDouble(4)/Double.parseDouble(pack))*rs.getInt(6);
				purpriceqty= Math.round(purpriceqty * 100.0) / 100.0;
				/*product.setPurpriceqty(""+(rs.getDouble(4)/Double.parseDouble(pack))*rs.getInt(6));*/
				product.setPurpriceqty(""+purpriceqty);
				product.setSalepriceqty(""+(rs.getDouble(5)/Double.parseDouble(pack))*rs.getInt(6));
				
				//Akash 06 sep 2018
				product.setPro_code(rs.getString(18));
				product.setMinorder(rs.getString(19));
				product.setMaxorder(rs.getString(20));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}
	//@ruchi product list for download product eport in xls
	public ArrayList<Product> getAllProductsXLS( int i, String commencing, String categoryid,
			String searchtext, String medicine_shedule, String location, LoginInfo loginInfo) {

		ArrayList<Product> list = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		String sql = "";
		if (categoryid == null) {
			categoryid = "2";
		}

		Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(loginInfo.getUserId());

		try {

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
					"select id,catalogueid,mrp, purchaseprice, saleprice,stock,expiry_date,batch_no,location,prodtypeid from inventory_product ");
			stringBuilder.append("where catalogueid!=0 and procurement=0 ");
			if (!location.equals("0")) {
				stringBuilder.append("and inventory_product.location=" + location + " ");
			}
			if (searchtext != null) {
				stringBuilder.append("and (prodname like ('" + searchtext + "%') or prodname like ('%" + searchtext
						+ "')  ) or genericname like ('" + searchtext + "%') ");
			}

			stringBuilder.append("order by id desc ");

			PreparedStatement ps = connection.prepareStatement(stringBuilder.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setCatalogueid(rs.getString(2));
				Product master = getProductCatalogueDetails(product.getCatalogueid());
				String name = master.getProduct_name() + " (" + master.getGenericname() + ")";
				product.setProduct_name(name);
				product.setMrp(DateTimeUtils.changeFormat(rs.getDouble(3)));
				product.setPurchase_price(DateTimeUtils.changeFormat(rs.getDouble(4)));
				product.setSale_price(DateTimeUtils.changeFormat(rs.getDouble(5)));
				product.setStock(rs.getString(6));
				product.setMfg(master.getMfg());
				String expirydate = DateTimeUtils.getCommencingDate1(rs.getString(7));
				product.setExpiry_date(expirydate);
				product.setBatch_no(rs.getString(8));
				String locid = rs.getString(9);
				product.setLocation(locid);
				product.setLocationName(pharmacyDAO.getLocationName(locid));
				product.setEdit_catalogue(priscription.getEdit_catalogue());
				Product subcategory = getSubCategory(rs.getString(10));
				product.setSubcategory(subcategory.getName());
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}


	public ArrayList<Product> getAllProducts(String id) {

		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		try {

			String sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified from inventory_product where categoryid="
					+ id + " order by id desc ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));
				product.setLastmodified(rs.getString(21));

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Product> getAllProducts(String id, String product_id, Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		String sql = "";
		if (id == null) {
			id = "";
		}
		try {

			LaundryDAO laundryDAO = new JDBCLaundryDAO(connection);
			sql = "SELECT id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, categoryid, stock from inventory_product where categoryid=1 order by id desc";

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setCategory_id(rs.getString(9));
				product.setStock(rs.getString(10));

				Product linenproduct = laundryDAO.getLinenProduct(rs.getString(1));

				if (linenproduct == null) {

					product.setProcessing("0");
					product.setRemains(product.getStock());
				} else {
					product.setProcessing(linenproduct.getProcessing());
					product.setRemains(linenproduct.getRemains());
				}

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());
				product.setTinno(vendor.getTinno());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public Product getProduct(String prodid) {

		Product product = new Product();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		try {

			String sql = "select prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,mdicinenameid,expiry_date,tax,lastmodified,batch_no,vat,genericname,cell,pack,medicine_type,mfg,medicine_shedule,freeqty,userid,hsnno,location,catalogueid,global_prodid,barcode from inventory_product where id="
					+ prodid + "";

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				product.setSubcategory_id(rs.getString(1));
				product.setVendor_id(rs.getString(2));
				product.setBrand_id(rs.getString(3));
				product.setProduct_code(rs.getString(4));
				product.setProduct_name(rs.getString(5));
				product.setMrp(DateTimeUtils.changeFormat(rs.getDouble(6)));
				product.setPurchase_price(DateTimeUtils.changeFormat(rs.getDouble(7)));
				product.setSale_price(DateTimeUtils.changeFormat(rs.getDouble(8)));
				product.setPurchase_discount(DateTimeUtils.changeFormat(rs.getDouble(9)));
				product.setSale_discount(rs.getString(10));
				product.setWeight(rs.getString(11));
				product.setUnit(rs.getString(12));
				product.setDescription(rs.getString(13));
				product.setCategory_id(rs.getString(14));
				product.setStock(""+rs.getInt(15));
				product.setMedicinenameid(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setLastmodified(rs.getString(19));
				product.setBatch_no(rs.getString(20));
				product.setVat(String.valueOf(rs.getInt(21)));

				if (product.getVat() != null) {
					if (product.getVat().equals("")) {
						product.setVat("0");
					}
				} else {
					product.setVat("0");
				}

				product.setGenericname(rs.getString(22));
				product.setShelf(rs.getString(23));
				product.setPack(rs.getString(24));
				product.setMedicine_type(rs.getString(25));
				product.setMfg(rs.getString(26));
				product.setMedicine_shedule(rs.getString(27));
				product.setFreeqty(""+rs.getInt(28));
				if (rs.getString(29) != null) {

					product.setUserid(rs.getString(29));
				} else {
					product.setUserid("Admin");
				}
				product.setHsnno(rs.getString(30));
				product.setLocation(rs.getString(31));
				product.setCatalogueid(rs.getString(32));
				product.setGlobal_prodid(rs.getInt(33));
				product.setBarcode(rs.getString(34));
				if (product.getMedicine_shedule() == null) {
					product.setShedule("Regular");
					product.setMedicine_shedule("0");
				} else if (product.getMedicine_shedule().equals("0")) {
					product.setShedule("Regular");
				} else if (product.getMedicine_shedule().equals("1")) {
					product.setShedule("Narcotics");
				} else if (product.getMedicine_shedule().equals("2")) {
					product.setShedule("H1");
				} else {
					product.setShedule("Regular");
					product.setMedicine_shedule("0");
				}

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());
				product.setTinno(vendor.getTinno());
				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				product.setId(Integer.parseInt(prodid));
				product.setIndividual_pur_price(getIndividualPurchasePrice(product.getPurchase_price(), product.getPack()));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return product;
	}

	public int updateProduct(Product product) {

		int result = 0;
		try {

			String sql = "update inventory_product set prodcode=?,prodname=?,mrp=?,purchaseprice=?,saleprice=?,purdiscount=?,salediscount=?,weight=?,unit=?,description=?,prodtypeid=?,supplierid=?,brandid=?,categoryid=? where id="
					+ product.getId() + "";
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, product.getProduct_code());
			ps.setString(2, product.getProduct_name());
			ps.setString(3, product.getMrp());
			ps.setString(4, product.getPurchase_price());
			ps.setString(5, product.getSale_price());
			ps.setString(6, product.getPurchase_discount());
			ps.setString(7, product.getSale_discount());
			ps.setString(8, product.getWeight());
			ps.setString(9, product.getUnit());
			ps.setString(10, product.getDescription());
			ps.setString(11, product.getSubcategory());
			ps.setString(12, product.getVendor());
			ps.setString(13, product.getBrand());
			ps.setString(14, product.getCategory());

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int deleteProduct(String id) {

		int result = 0;

		try {

			String sql = "update inventory_product set deleted=1 where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int deleteCategoryandSubcategory(String categoryid) {

		int result = 0;

		try {

			String sql = "delete from inventory_category where id=" + categoryid + "";
			PreparedStatement ps = connection.prepareStatement(sql);

			result = ps.executeUpdate();

			String sql2 = "delete from inventory_subcategory where category_id='" + categoryid + "'";
			PreparedStatement ps2 = connection.prepareStatement(sql2);

			result = ps2.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getAssetProducts(String categoryid) {

		ArrayList<Product> list = new ArrayList<Product>();

		try {

			String sql = "select id,supplierid,brandid,prodcode,prodname,mrp,purchaseprice,saleprice,stock from inventory_product where categoryid ="
					+ categoryid + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setVendor_id(rs.getString(2));
				product.setBrand_id(rs.getString(3));
				product.setProduct_code(rs.getString(4));
				product.setProduct_name(rs.getString(5));
				product.setMrp(rs.getString(6));
				product.setPurchase_price(rs.getString(7));
				product.setSale_price(rs.getString(8));
				product.setStock(rs.getString(9));
				product.setCategory(categoryid);
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int getAssetsCount(String product_id) {

		int result = 0;
		String sql = "";
		try {
			if (product_id != null) {

				sql = "select count(*) from inventory_product inner join his_laundry on his_laundry.product_id=inventory_product.id where categoryid=1 and his_laundry.product_id="
						+ product_id + "";
			} else {
				sql = "select count(*) from inventory_product inner join his_laundry on his_laundry.product_id=inventory_product.id where categoryid=1";
			}
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int geTotalProductCount(String date, String categoryid, String searchtext, String medicine_shedule,
			String location, String withstock_filter, String product_type) {

		int result = 0;
		
		String sql = "";
		try {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("select count(*) from inventory_product  ");
			stringBuilder.append("inner join inventory_catalogue on inventory_catalogue.id=inventory_product.catalogueid  ");
			stringBuilder.append("where inventory_product.catalogueid!=0  and inventory_product.procurement=0 ");

			if (searchtext != null) {
				/*stringBuilder.append(
						"and ((prodname like ('" + searchtext + "%') or prodname like ('%" + searchtext + "') ) ");
				stringBuilder.append(" or genericname like ('" + searchtext + "%')) ");*/
				stringBuilder.append("and (inventory_product.prodname like ('%" + searchtext + "%') or inventory_catalogue.product_name like ('%" + searchtext+ "%')  or inventory_product.genericname like ('%" + searchtext + "%') or inventory_catalogue.genericname like ('%" + searchtext + "%') or inventory_catalogue.product_code like ('%" + searchtext + "%')  ) ");
			}

			
			if (!location.equals("0")) {
					stringBuilder.append("and inventory_product.location='" + location + "'  ");
			}
			if(!categoryid.equals("0")){
				stringBuilder.append("and inventory_product.categoryid='" + categoryid + "'  ");
			}
			if(!product_type.equals("0")){
				stringBuilder.append("and inventory_catalogue.shedule='"+product_type+"' ");
			}
			if(withstock_filter.equals("2")){
				stringBuilder.append("and stock>0 ");
			}else if(withstock_filter.equals("3")){
				stringBuilder.append("and stock=0 ");
			}else{
				stringBuilder.append("and stock>=0 ");
			}
			sql = stringBuilder.toString();
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getGodownList() {

		ArrayList<Product> list = new ArrayList<Product>();

		try {

			String sql = "SELECT id,name,description from apm_inventory_godown";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));
				product.setDescription(rs.getString(3));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Product> getProductByVendor(String vendorid) {

		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		try {
			String sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,batch_no,vat,genericname,cell from inventory_product where supplierid="
					+ vendorid + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));
				product.setLastmodified(rs.getString(21));
				product.setBatch_no(rs.getString(22));
				product.setVat(rs.getString(23));
				product.setGenericname(rs.getString(24));
				product.setShelf(rs.getString(25));

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				product.setProduct_name(rs.getString(6) + " (" + product.getBrand() + " - " + vendor.getName() + ")");

				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int addSaleProduct(Product product) {

		int result = 0;

		try {

			String sql = "insert into inventory_product_sale (product_id, gowdownid, quantity, total, notes, date, lastmodified) values (?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getProduct_id());
			ps.setString(2, product.getGowdown());
			ps.setString(3, product.getQuantity());
			Product product2 = getProduct(product.getProduct_id());

			Product masterProduct = getProduct(product.getProduct_id());

			int total = Integer.parseInt(product.getQuantity()) * Integer.parseInt(product2.getSale_price());
			ps.setInt(4, total);
			ps.setString(5, product.getNotes());
			ps.setString(6, product.getSale_date());
			ps.setString(7, product.getLastmodified());

			result = ps.executeUpdate();

			int nowstock = Integer.parseInt(masterProduct.getStock()) - Integer.parseInt(product.getQuantity());
			updateProductQty(product.getProduct_id(), String.valueOf(nowstock));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int getSaleProductCount(String gowdown, String fromdate, String todate) {

		int result = 0;
		String sql = "";
		try {

			if (gowdown != null && fromdate != null && todate != null) {

				sql = "SELECT count(*) from inventory_product_sale where gowdownid=" + gowdown + " and date between '"
						+ fromdate + "' and '" + todate + "' ";

			} else if (fromdate != null && todate != null) {

				sql = "SELECT count(*) from inventory_product_sale where date between '" + fromdate + "' and '" + todate
						+ "'";
			} else if (gowdown != null) {
				sql = "SELECT count(*) from inventory_product_sale where gowdownid=" + gowdown + "";
			} else {

				sql = "SELECT count(*) from inventory_product_sale";
			}
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				result = rs.getInt(1);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.apm.Inventory.eu.bi.InventoryProductDAO#getSoldProduct(com.apm.common
	 * .utils.Pagination)
	 */
	public ArrayList<Product> getSoldProduct(Pagination pagination, String gowdown, String fromdate, String todate) {

		ArrayList<Product> list = new ArrayList<Product>();
		StringBuffer buffer = new StringBuffer();
		String sql = "";
		try {

			buffer.append(
					"select id, product_id, gowdownid, quantity, total, notes, date, lastmodified from inventory_product_sale ");
			if (gowdown != null && fromdate != null && todate != null) {

				buffer.append(
						"where gowdownid=" + gowdown + " and date between '" + fromdate + "' and '" + todate + "' ");

			} else if (fromdate != null && todate != null) {

				buffer.append("where date between '" + fromdate + "' and '" + todate + "' ");
			} else if (gowdown != null) {
				buffer.append(sql = "where gowdownid=" + gowdown + " ");
			}

			if (pagination != null) {
				sql = pagination.getSQLQuery(buffer.toString());
			}

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_id(rs.getString(2));
				product.setGodownid(rs.getString(3));
				product.setQuantity(rs.getString(4));

				Product masterProd = getProduct(product.getProduct_id());
				product.setProduct_code(masterProd.getProduct_code());
				product.setProduct_name(masterProd.getProduct_name());
				product.setSubcategory(masterProd.getSubcategory());
				product.setCategory(masterProd.getCategory());
				product.setVendor(masterProd.getVendor());
				product.setSale_date(rs.getString(7));
				product.setExpiry_date(masterProd.getExpiry_date());
				product.setSale_price(masterProd.getSale_price());
				product.setStock(masterProd.getStock());
				product.setGowdown(getGodownName(product.getGodownid()));

				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public String getGodownName(String id) {

		String result = "";
		try {
			String sql = "select name from apm_inventory_godown where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public Product getSaleProducDetails(String id) {

		Product product = new Product();
		try {

			String sql = "SELECT product_id, gowdownid, quantity, total, notes, date, lastmodified from inventory_product_sale where id="
					+ id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				product.setProduct_id(rs.getString(1));
				product.setGodownid(rs.getString(2));
				product.setQuantity(rs.getString(3));
				product.setSale_date(rs.getString(6));
				product.setLastmodified(rs.getString(7));
				String gowdown = getGodownName(product.getGodownid());
				product.setGowdown(gowdown);
				Product masterProduct = getProduct(product.getProduct_id());

				product.setCategory(masterProduct.getCategory());
				product.setSubcategory(masterProduct.getSubcategory());
				product.setVendor(masterProduct.getVendor());
				product.setProduct_name(masterProduct.getProduct_name());

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return product;
	}

	public int returnsaleQty(String id, String returnqty, String lastmodified) {

		int result = 0;
		try {

			Product product = getSaleProducDetails(id);

			Product masterProduct = getProduct(product.getProduct_id());

			int nowqty = Integer.parseInt(product.getQuantity()) - Integer.parseInt(returnqty);

			if (nowqty < 0) {

				nowqty = 0;
			}

			String sql = "update inventory_product_sale set quantity=" + nowqty + ",lastmodified='" + lastmodified
					+ "' where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();

			int nowStock = Integer.parseInt(masterProduct.getStock()) + Integer.parseInt(returnqty);

			updateProductQty(product.getProduct_id(), String.valueOf(nowStock));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int updateProductQty(String id, String nowstock) {

		int result = 0;
		try {

			String sql = "update inventory_product set stock=" + nowstock + " where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getListandHistoryProduct(Pagination pagination, String fromdate, String todate,
			String brandid, String vendorid) {

		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		StringBuffer buffer = new StringBuffer();
		String sql = "";
		try {
			buffer.append(
					"select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified from inventory_product where stock!=0 ");
			if (fromdate != null && todate != null && brandid != null && vendorid != null) {

				buffer.append("and brandid=" + brandid + " and supplierid=" + vendorid + " and lastmodified between '"
						+ fromdate + "' and '" + todate + "' ");

			} else if (fromdate != null && todate != null && brandid != null) {
				buffer.append("and brandid=" + brandid + " and lastmodified between '" + fromdate + "' and '" + todate
						+ "' ");
			} else if (fromdate != null && todate != null && vendorid != null) {
				buffer.append("and supplierid=" + vendorid + " and lastmodified between '" + fromdate + "' and '"
						+ todate + "' ");
			} else if (fromdate == null && brandid != null && vendorid != null) {
				buffer.append("and brandid=" + brandid + " and supplierid='" + vendorid + "' ");
			} else if (fromdate != null && todate != null) {
				buffer.append("and lastmodified between '" + fromdate + "' and '" + todate + "' ");
			} else if (brandid != null) {
				buffer.append("and brandid=" + brandid + " ");
			} else if (vendorid != null) {
				buffer.append("and supplierid=" + vendorid + " ");
			}
			buffer.append("order by id desc");
			if (pagination != null) {
				sql = pagination.getSQLQuery(buffer.toString());
			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));

				int total = Integer.parseInt(product.getStock()) * Integer.parseInt(product.getPurchase_price());
				product.setTotal(String.valueOf(total));

				String lastmodified = DateTimeUtils.getCommencingDate1(rs.getString(21));
				product.setLastmodified(lastmodified);

				ArrayList<Product> historyvendorProduct = getLastHistorybyVendor(product.getVendor_id(),
						product.getId());
				product.setHistoryvendorProduct(historyvendorProduct);

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Product> getLastHistorybyVendor(String vendorid, int productid) {

		ArrayList<Product> list = new ArrayList<Product>();

		try {
			String sql = "SELECT prodcode,lastmodified,purchaseprice,stock from inventory_product where supplierid="
					+ vendorid + " and id=" + productid + " order by id desc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int i = 0;
			while (rs.next()) {
				if (i == 4) {
					break;
				}

				Product product = new Product();
				product.setProduct_code(rs.getString(1));
				String purchasedate = DateTimeUtils.getCommencingDate1(rs.getString(2));
				product.setLastmodified(purchasedate);
				product.setPurchase_price(rs.getString(3));
				product.setStock(rs.getString(4));

				int total = Integer.parseInt(product.getStock()) * Integer.parseInt(product.getPurchase_price());
				product.setTotal(String.valueOf(total));
				list.add(product);

				i++;
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;
	}

	public int geTotalProductCountByPurchased(String fromdate, String todate, String brand, String vendor) {

		int result = 0;

		String sql = "";
		try {

			if (fromdate != null && todate != null && brand != null && vendor != null) {

				sql = "select count(*) from inventory_product where stock!=0 and brandid=" + brand + " and supplierid="
						+ vendor + " and lastmodified between '" + fromdate + "' and '" + todate + "'";

			} else if (fromdate != null && todate != null && brand != null) {
				sql = "select count(*) from inventory_product where stock!=0 and brandid=" + brand
						+ " and lastmodified between '" + fromdate + "' and '" + todate + "'";
			} else if (fromdate != null && todate != null && vendor != null) {
				sql = "select count(*) from inventory_product where stock!=0 and supplierid=" + vendor
						+ " and lastmodified between '" + fromdate + "' and '" + todate + "'";
			} else if (fromdate == null && brand != null && vendor != null) {
				sql = "select count(*) from inventory_product where stock!=0 and brandid=" + brand + " and supplierid='"
						+ vendor + "'";
			} else if (fromdate != null && todate != null) {
				sql = "select count(*) from inventory_product where lastmodified between '" + fromdate + "' and '"
						+ todate + "'";
			} else if (brand != null) {
				sql = "select count(*) from inventory_product where brandid=" + brand + "";
			} else if (vendor != null) {
				sql = "select count(*) from inventory_product where supplierid=" + vendor + "";
			} else {
				sql = "select count(*) from inventory_product where stock!=0";
			}
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;

	}

	private static long daysBetween(Date one, Date two) {
		long difference = (one.getTime() - two.getTime()) / 86400000;
		return Math.abs(difference);
	}

	private static long getExpiryDaysBetween(Date one, Date two) {

		return one.compareTo(two);
	}

	public ArrayList<Product> getAllWillExpireList() {

		ArrayList<Product> list = new ArrayList<Product>();
		try {

			String sql = "SELECT id,prodname,expiry_date from inventory_product";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setExpiry_date(rs.getString(3));
				Calendar expireCalendar = Calendar.getInstance();
				String expirydate = product.getExpiry_date();
				Date date = format.parse(expirydate);
				expireCalendar.setTime(date);

				long diffdays = daysBetween(expireCalendar.getTime(), calendar.getTime());

				if (diffdays <= 10) {

					product.setStatus("expired");
					list.add(product);

				} else {
					product.setStatus("notexpired");
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public Product getMedicineProductDetails(String mdicinenameid) {

		Product product = new Product();
		try {
			InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
			String sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid, stock, expiry_date, tax, location_id, qty, lastmodified, userid,batch_no,vat,genericname,cell from inventory_product where mdicinenameid="
					+ mdicinenameid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));
				product.setLastmodified(rs.getString(21));
				product.setUserid(rs.getString(22));
				product.setBatch_no(rs.getString(23));
				product.setVat(rs.getString(24));
				product.setGenericname(rs.getString(25));
				product.setShelf(rs.getString(26));

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return product;
	}

	public int updateMedicineQty(int saleqty, String productid, int plusminus) {

		int result = 0;
		int stock = 0;
		try {

			String sql = "select stock from inventory_product where id=" + productid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				stock = rs.getInt(1);
			}

			if (plusminus == 0) {
				stock = stock - saleqty;
			} else {
				stock = stock + saleqty;
			}

			String updatesql = "update inventory_product set stock=" + stock + " where id=" + productid + " ";
			PreparedStatement preparedStatement = connection.prepareStatement(updatesql);

			result = preparedStatement.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int getCountProduct(String id) {

		int result = 0;
		try {

			String sql = "select count(*) from inventory_product where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getAllProductList(String categoryid) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {

			String sql = "select id,name,vendorid from inventory_brandname where category_id=" + categoryid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public int savenewProduct(Product product) {
		
		int result = 0;
		try {
			//Akash 03 May 2018
			String added_date="";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			added_date = dateFormat.format(cal.getTime());
			added_date = DateTimeUtils.getCommencingDate1(added_date);
			
			String sql = "insert into inventory_product (supplierid,brandid,prodname,mrp,purchaseprice,saleprice,vat,categoryid,userid,lastmodified,mdicinenameid,genericname,procurement,location,stock,added_date) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getVendor());
			ps.setString(2, product.getBrand());
			ps.setString(3, product.getProduct_name());
			ps.setString(4, product.getMrp());
			ps.setString(5, product.getPurchase_price());
			ps.setString(6, product.getSale_price());
			ps.setString(7, product.getVat());
			ps.setString(8, product.getCategory_id());
			ps.setString(9, product.getUserid());
			ps.setString(10, product.getLastmodified());
			ps.setString(11, product.getMedicinenameid());
			ps.setString(12, product.getGenericname());
			ps.setInt(13, product.getProcurement());
			ps.setString(14, product.getLocation());
			ps.setString(15, product.getStock());
			ps.setString(16, added_date);
			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getAllBrandListByCategory(String categoryid) {

		ArrayList<Product> list = new ArrayList<Product>();

		try {
			String sql = "SELECT id,name from inventory_brandname where category_id=" + categoryid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Product> geProductList(String categoryid, String location) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String sql = "";
			StringBuffer buffer = new StringBuffer();
			if (!location.equals("0")) {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and location='"
						+ location + "' and pack>0 order by prodname asc   ";
			} else {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and pack>0 order by prodname asc ";

			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));
				product.setLastmodified(rs.getString(21));

				String genericname = rs.getString(22);
				if (genericname == null) {
					genericname = "GEN";
				}

				String medicinenameid = rs.getString(23);
				product.setBatch_no(rs.getString(24));
				product.setHsnno(rs.getString(25));
				if (product.getHsnno() == null) {
					product.setHsnno("");
				}
				product.setMedicinenameid(medicinenameid);
				// Product medicineData=
				// getMedicineProductDetails(medicinenameid);
				
				String pro_code = getCatlogueProductCode(rs.getString(26));
				
				String expiry= product.getExpiry_date();
				if(expiry!=null){
					if(!expiry.equals("")){
						try {
							String temp[] = expiry.split("-");
							expiry= temp[1]+"/"+temp[0];
						} catch (Exception e) {
							e.printStackTrace();

						}
					}
					
				} else {
					expiry= "";
				}
				
				String data ="";
				if(pro_code!=null){
					if(pro_code.equals("")){
						data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
								+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
								+ ") (" + product.getStock() + ")  ";
					}else{
						data = product.getProduct_name() + "- " + genericname + "- " + pro_code + " - (" + product.getBatch_no() + "/"
								+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
								+ ") (" + product.getStock() + ")  ";
					}
				}else{
					data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}
				 
				product.setGenericname(data);

				/*
				 * Product category=getCategory(product.getCategory_id());
				 * product.setCategory(category.getName());
				 * 
				 * Product
				 * subcategory=getSubCategory(product.getSubcategory_id());
				 * product.setSubcategory(subcategory.getName());
				 * 
				 * Vendor
				 * vendor=inventoryVendorDAO.getVendor(product.getVendor_id());
				 * 
				 * product.setMin_delivery_time(vendor.getMin_delivery_time());
				 * product.setVendor(vendor.getName());
				 * 
				 * Product brand=getBrandName(product.getBrand_id());
				 * product.setBrand(brand.getName());
				 */
				boolean flag = false;
				Calendar expireCalendar = Calendar.getInstance();
				String expirydate = rs.getString(17);
				if (expirydate != null) {

					if (!expirydate.equals("")) {
						try {
							Date date = format.parse(expirydate);
							expireCalendar.setTime(date);
							long res = getExpiryDaysBetween(expireCalendar.getTime(), calendar.getTime());
							if (res > 0) {
								flag = true;
							}

						} catch (Exception e) {
							e.printStackTrace();

						}
					}
				}
				if (flag) {
					list.add(product);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	private String getCatlogueProductCode(String string) {
		String product_code ="";
		try {
			String sql="select product_code from inventory_catalogue where id='"+string+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				product_code = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product_code;
	}

	public ArrayList<Product> getPoProductList(String categoryid) {
		PreparedStatement preparedStatement = null;
		ArrayList<Product> list = new ArrayList<Product>();
		String sql = "SELECT id, prodname,brandid,supplierid,genericname FROM inventory_product where categoryid = "
				+ categoryid + " and procurement=1 order by id desc";

		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));

				/*
				 * InventoryProductDAO inventoryProductDAO = new
				 * JDBCInventoryProductDAO(connection); Product brand =
				 * inventoryProductDAO.getBrandName(rs.getString(3)); String
				 * brandname = brand.getName();
				 */

				InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
				Vendor vendor = inventoryVendorDAO.getVendor(rs.getString(4));
				String vendorname = vendor.getName();
				String genericname = rs.getString(5);
				if (genericname == null) {
					genericname = "GEN";
				}

				product.setProduct_name(rs.getString(2) + " (" + genericname + ") - " + vendorname + ")");

				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int updateProductByProcurement(Product product, int i) {

		int result = 0;
		try {
			String expiry ="";
			try {
				expiry = DateTimeUtils.getCommencingDate1(product.getExpiry_date());
			} catch (Exception e) {
				// TODO: handle exception
			}
			/*String sql = "update inventory_product set mrp=?,purchaseprice=?,saleprice=?,stock=?,expiry_date=?,batch_no=?,vat=?,cell=?,mfg=?,pack=?,medicine_shedule=?,freeqty=?,hsnno=?,procurement=? where id="
					+ product.getProduct_id() + " ";*/
			//Akash 02 Aug 2018
			String sql="";
			if(i==1){
				sql = "update inventory_product set mrp=?,purchaseprice=?,saleprice=?,expiry_date=?,batch_no=?,vat=?,cell=?,mfg=?,pack=?,medicine_shedule=?,freeqty=?,hsnno=?,procurement=?,barcode=? where id="
						+ product.getProduct_id() + " ";
			}else{
				sql = "update inventory_product set mrp=?,purchaseprice=?,saleprice=?,expiry_date=?,batch_no=?,vat=?,cell=?,mfg=?,pack=?,medicine_shedule=?,freeqty=?,hsnno=?,procurement=?,stock=?,supplierid=?,barcode=? where id="
						+ product.getProduct_id() + " ";
			}
			
			
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getMrp());
			ps.setString(2, product.getPurchase_price());
			ps.setString(3, product.getSale_price());
			ps.setString(4, expiry);
			ps.setString(5, product.getBatch_no());
			ps.setString(6, product.getVat());
			ps.setString(7, product.getShelf());
			ps.setString(8, product.getMfg());
			ps.setString(9, product.getPack());
			ps.setString(10, product.getMedicine_shedule());
			ps.setString(11, product.getFreeqty());
			ps.setString(12, product.getHsnno());
			ps.setString(13, "0");
			if(i==1){
				ps.setString(14, product.getBarcode());
			}else{
				ps.setString(14, product.getStock());
				ps.setString(15, product.getVendor_id());
				ps.setString(16, product.getBarcode());
			}
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Master> getcellList(String department) {
		PreparedStatement preparedStatement = null;
		ArrayList<Master> list = new ArrayList<Master>();
		StringBuilder builder = new StringBuilder();
		//String sql = "select id,name from apm_medicine_cell ";
		builder.append("select id,name from apm_medicine_cell ");
		if(department!=null){
			builder.append("where departmentid in ("+department+") ");
		}

		try {
			preparedStatement = connection.prepareStatement(builder.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				list.add(master);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int updatePackandMedicineType(String pack, String medicine_type, String pid) {

		int result = 0;
		try {

			String sql = "update inventory_product set pack=?,medicine_type=? where id=" + pid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, pack);
			ps.setString(2, medicine_type);

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getSupplierBillProcurementandDates(String vendorid, String fromdate, String todate) {

		ArrayList<Product> list = new ArrayList<Product>();

		try {

			StringBuffer buffer = new StringBuffer();
			buffer.append(
					"SELECT inventory_procurement.id,commencing,vendorid,paytype,inventory_paymentpo.voucherno,inventory_procurement.procurementid ");
			buffer.append(
					"from  inventory_paymentpo inner join inventory_procurement where inventory_procurement.vendorid="
							+ vendorid + " ");
			buffer.append("and commencing between '" + fromdate + "' and '" + todate
					+ "' group by inventory_paymentpo.voucherno ");

			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setDate(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setPayType(rs.getString(4));
				product.setVoucherno(rs.getString(5));
				product.setProcurementid(rs.getString(6));
				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Product> getSupplierBillList(String vendorid, String commencing, String voucherno,
			String procurementid) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT id,prodid,purprice,qty,total,date,procurementid ");
			buffer.append("from  inventory_procurement where vendorid=" + vendorid + " ");
			buffer.append(
					"and procurementid=" + procurementid + " and voucherno='" + voucherno + "' order by id desc ");

			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();

			double subTotal = 0;
			double allTotal = 0;
			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_id(rs.getString(2));
				product.setPurchase_price(rs.getString(3));
				product.setQty(rs.getString(4));
				product.setTotal(rs.getString(5));
				product.setDate(rs.getString(6));
				product.setProcurementid(rs.getString(7));

				Product master = getProduct(product.getProduct_id());
				product.setProduct_name(master.getProduct_name());
				product.setMrp(master.getMrp());
				product.setMfg(master.getMfg());
				product.setBatch_no(master.getBatch_no());
				product.setPack(master.getPack());
				product.setVoucherno(master.getVoucherno());

				subTotal = subTotal + Double.parseDouble(product.getTotal());

				product.setSubTotal(DateTimeUtils.changeFormat(subTotal));
				product.setAllTotal(DateTimeUtils.changeFormat(allTotal));

				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public Product getPriscMedicineByAvailablility(String mdicinenametxt, int required,String genericname) {

		Product product = new Product();
		int m=0;
		String result=mdicinenametxt;
		
		for(String str:mdicinenametxt.split(" ")){
			 
			   int l=str.length();
			   if(m<l){
				   m=l;
				   result= str;
			   }
		}
		
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_product.id,inventory_product.supplierid,inventory_catalogue.product_name, ");
			buffer.append("inventory_product.mrp,inventory_product.purchaseprice,inventory_product.saleprice,inventory_product.stock, ");
			buffer.append("inventory_product.mdicinenameid,inventory_product.expiry_date,inventory_product.batch_no,inventory_product.vat, ");
			buffer.append("inventory_catalogue.genericname,inventory_product.cell,inventory_product.pack,inventory_product.medicine_type,inventory_product.mfg ");
			buffer.append("from inventory_product inner join inventory_catalogue on inventory_product.catalogueid=inventory_catalogue.id ");
			buffer.append("where inventory_catalogue.product_name like ('%"+result+"%') ");
			//buffer.append("where ( inventory_catalogue.product_name like ('%"+result+"%') or inventory_catalogue.genericname like ('"+genericname+"') ) ");
			buffer.append("and inventory_product.saleprice is not null and inventory_product.mrp>0 and stock>0  ");
			buffer.append("order by inventory_product.expiry_date asc ");

			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				product.setId(rs.getInt(1));
				product.setVendor_id(rs.getString(2));
				product.setProduct_name(rs.getString(3));
				product.setMrp(rs.getString(4));
				product.setPurchase_price(rs.getString(5));
				product.setSale_price(rs.getString(6));
				product.setStock(rs.getString(7));
				product.setMedicinenameid(rs.getString(8));
				product.setExpiry_date(rs.getString(9));
				product.setBatch_no(rs.getString(10));
				product.setVat(rs.getString(11));
				product.setGenericname(rs.getString(12));
				product.setShelf(rs.getString(13));
				product.setPack(rs.getString(14));
				product.setMedicine_type(rs.getString(15));
				product.setMfg(rs.getString(16));
				break;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return product;
	}

	public ArrayList<Product> getExpiryMedicineReport(String fromdate, String todate, String days, String location,
			String report, Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			String nowdate = dateFormat.format(calendar.getTime());
			StringBuffer buffer = new StringBuffer();
			buffer.append(
					"select inventory_product.id,inventory_product.prodname,inventory_product.mrp,inventory_product.purchaseprice,inventory_product.saleprice,inventory_product.stock,inventory_product.expiry_date,inventory_product.supplierid,inventory_product.genericname,inventory_procurement.voucherno,inventory_product.location from inventory_product ");
			buffer.append("inner join inventory_procurement on inventory_product.id=inventory_procurement.prodid  ");
			buffer.append("where inventory_product.lastmodified between '" + fromdate + "' and '" + todate + "' ");
			if (!location.equals("0") && !location.equals("32")) {

				buffer.append("and inventory_product.location='" + location + "' ");
			}
			buffer.append("group by inventory_product.id order by expiry_date ");
			String sql =pagination.getSQLQuery(buffer.toString());
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setMrp(rs.getString(3));
				product.setPurchase_price(rs.getString(4));
				product.setSale_price(rs.getString(5));

				product.setStock(rs.getString(6));
				String expiry = rs.getString(7);
				if (product.getPurchase_price() == null) {
					product.setPurchase_price("0");
				}
				if (product.getStock() == null) {
					product.setStock("0");
				}

				double total = Double.parseDouble(product.getPurchase_price()) * Double.parseDouble(product.getStock());
				if (expiry == null) {
					expiry = nowdate;
				}
				product.setExpiry_date(DateTimeUtils.getCommencingDate1(expiry));

				product.setTotal(DateTimeUtils.changeFormat(total));

				Vendor vendor = inventoryVendorDAO.getVendor(rs.getString(8));
				product.setVendor(vendor.getName());
				product.setContact(vendor.getMobile_pri());
				product.setGenericname(rs.getString(9));
				product.setVoucherno(rs.getString(10));

				String locationame = pharmacyDAO.getPharmacyLocation(rs.getString(11));
				product.setLocationName(locationame);
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar expireCalendar = Calendar.getInstance();
				long res = 0;
				boolean flag = false;
				String expirydate = rs.getString(7);

				if (expirydate != null) {

					if (!expirydate.equals("")) {
						Date date = format.parse(expirydate);
						expireCalendar.setTime(date);
						res = getExpiryDaysBetween(expireCalendar.getTime(), calendar.getTime());
						flag = true;
					}
				}

				if (flag) {

					if (res <= 0) {
						// exired
					} else {
						int d = Integer.parseInt(days);
						if (res < d) {
							// expiring
						} else {
							// after d days
						}

					}
				} else {

					// not given exiry date
				}

				if (report.equals("0")) {
					long diffdays = DateTimeUtils.getDifferenceOfTwoDateDBFormat(nowdate, expiry);
					product.setDays("" + diffdays + " Days");
					String daysbetween[] = getDatesforUsedorNot(days);
					boolean status = isProductUsedOrNot(daysbetween[0], daysbetween[1], product.getId());
					if (status) {
						product.setStatus("1");
						list.add(product);
					}
				} else if (report.equals("1")) {
					long diffdays = DateTimeUtils.getDifferenceOfTwoDateDBFormat(nowdate, expiry);
					product.setDays("" + diffdays + " Days");
					String daysbetween[] = getDatesforUsedorNot(days);
					boolean status = isProductUsedOrNot(daysbetween[0], daysbetween[1], product.getId());
					if (!status) {
						product.setStatus("0");
						list.add(product);
					}
				} else if (report.equals("2")) {

					if (flag) {

						if (res <= 0) {
							// exired
							if (res <= Integer.parseInt(days)) {
								long diffdays = DateTimeUtils.getDifferenceOfTwoDateDBFormat(nowdate, expiry);
								product.setDays("" + diffdays + " Days");
								product.setStatus("2");
								list.add(product);
							}
						} else {
							int d = Integer.parseInt(days);
							if (res < d) {
								// expiring
							} else {
								// after d days
							}

						}
					}

				} else if (report.equals("3")) {
					if (res <= 0) {
						// exired

					} else {
						int d = Integer.parseInt(days);
						if (res < d) {
							// expiring
							long diffdays = DateTimeUtils.getDifferenceOfTwoDateDBFormat(nowdate, expiry);
							product.setDays("" + diffdays + " Days");
							product.setStatus("3");
							list.add(product);
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private String[] getDatesforUsedorNot(String days) {

		String dates[] = null;
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar fromcal = Calendar.getInstance();
			String fromdate = "", todate = "";

			if (days.equals("30")) {
				fromcal.add(Calendar.DATE, -30);
			} else if (days.equals("60")) {
				fromcal.add(Calendar.DATE, -60);
			} else if (days.equals("90")) {
				fromcal.add(Calendar.DATE, -90);
			}

			fromdate = dateFormat.format(fromcal.getTime());
			Calendar calendar = Calendar.getInstance();
			todate = dateFormat.format(calendar.getTime());

			dates = new String[] { fromdate, todate };

		} catch (Exception e) {

			e.printStackTrace();
		}

		return dates;
	}

	private boolean isProductUsedOrNot(String fromdate, String toDate, int pid) {

		try {
			String sql = "select id from apm_medicine_charges where product_id=" + pid + " and commencing between '"
					+ fromdate + "' and '" + toDate + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				int id = rs.getInt(1);
				if (id > 0) {
					return true;
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}

	public double[] getGraphPerUseandDead(String fromdate, String todate, String days) {

		double dbls[] = null;
		double useper = 0;
		double deadper = 0;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select id,prodname from inventory_product ");
			buffer.append("where lastmodified between '" + fromdate + "' and '" + todate + "' order by expiry_date ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());

			ResultSet rs = ps.executeQuery();
			int use = 0;
			int dead = 0;
			int total = 0;
			while (rs.next()) {

				int pid = rs.getInt(1);
				String daysbetween[] = getDatesforUsedorNot(days);
				boolean status = isProductUsedOrNot(daysbetween[0], daysbetween[1], pid);
				if (status) {
					use++;
				} else {
					dead++;
				}

				total++;
			}

			useper = use * 100 / total;
			deadper = dead * 100 / total;

			dbls = new double[] { useper, deadper };

		} catch (Exception e) {

			e.printStackTrace();
		}

		return dbls;
	}

	public ArrayList<Product> getVatReportList(String fromdate, String todate, String location) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			String sql = "";
			if (!location.equals("0") && !location.equals("1")) {
				sql = "SELECT id,prodid,vendorid from inventory_procurement where date between '" + fromdate + "' and '"
						+ todate + "' and location=" + location + " group by vendorid ";
			} else {
				sql = "SELECT id,prodid,vendorid from inventory_procurement where date between '" + fromdate + "' and '"
						+ todate + "' group by vendorid ";
			}
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			double allsixtaxTot = 0;
			double allthirttaxTot = 0;
			double allsixVatPer = 0;
			double allthirVatPer = 0;
			double alltableValtot = 0;
			double alltotvatTotal = 0;
			double alltotalGross = 0;
			double alltotalNet = 0;

			double allfivetatTot = 0;
			double alltwelltaxTot = 0;
			double alleighteentaxTot = 0;
			double alltwentyeighttaxTot = 0;

			double allfivetatPer = 0;
			double alltwelltaxPer = 0;
			double alleighteentaxPer = 0;
			double alltwentyeighttaxPer = 0;

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));

				ArrayList<Product> listVat = getVatReportVoucherandVendorWise(product.getVendor_id(), fromdate, todate);
				product.setListVat(listVat);

				double fivetaxTot = 0;
				double fivetaxPer = 0;
				double twelltaxTot = 0;
				double twelltaxPer = 0;
				double eighteentaxTot = 0;
				double eighteentacPer = 0;
				double twentyeighttaxTot = 0;
				double twentyeighttaxPer = 0;
				double sixtaxTot = 0;
				double thirttaxTot = 0;
				double sixVatPer = 0;
				double thirVatPer = 0;
				double tableValtot = 0;
				double totvatTotal = 0;
				double totalGross = 0;
				double totalNet = 0;

				for (Product master : listVat) {

					fivetaxTot = fivetaxTot + Double.parseDouble(master.getFiveTaxable());
					twelltaxTot = twelltaxTot + Double.parseDouble(master.getTwellTaxable());
					eighteentaxTot = eighteentaxTot + Double.parseDouble(master.getEighteenTaxable());
					twentyeighttaxTot = twentyeighttaxTot + Double.parseDouble(master.getTwentyeightTaxable());

					fivetaxPer = fivetaxPer + Double.parseDouble(master.getFivetaxPer());
					twelltaxPer = twelltaxPer + Double.parseDouble(master.getTwelltaxPer());
					eighteentacPer = eighteentacPer + Double.parseDouble(master.getEighteentaxPer());
					twentyeighttaxPer = twentyeighttaxPer + Double.parseDouble(master.getTwentyeighttaxPer());

					sixtaxTot = sixtaxTot + Double.parseDouble(master.getSixtaxable());
					thirttaxTot = thirttaxTot + Double.parseDouble(master.getThirttentax());
					sixVatPer = sixVatPer + Double.parseDouble(master.getSixTaxper());
					thirVatPer = thirVatPer + Double.parseDouble(master.getThirteenTaxper());
					tableValtot = tableValtot + Double.parseDouble(master.getTableVal());
					totvatTotal = totvatTotal + Double.parseDouble(master.getTotalVat());
					totalGross = totalGross + Double.parseDouble(master.getGrossVat());
					totalNet = totalNet + Double.parseDouble(master.getNetVal());

				}
				product.setFiveTaxable(DateTimeUtils.changeFormat(fivetaxTot));
				product.setTwellTaxable(DateTimeUtils.changeFormat(twelltaxTot));
				product.setEighteenTaxable(DateTimeUtils.changeFormat(eighteentaxTot));
				product.setTwentyeightTaxable(DateTimeUtils.changeFormat(twentyeighttaxTot));

				product.setFivetaxPer(DateTimeUtils.changeFormat(fivetaxPer));
				product.setTwelltaxPer(DateTimeUtils.changeFormat(twelltaxPer));
				product.setEighteentaxPer(DateTimeUtils.changeFormat(eighteentacPer));
				product.setTwentyeighttaxPer(DateTimeUtils.changeFormat(twentyeighttaxPer));

				product.setSixtaxable(DateTimeUtils.changeFormat(sixtaxTot));
				product.setThirttentax(DateTimeUtils.changeFormat(thirttaxTot));
				product.setSixTaxper(DateTimeUtils.changeFormat(sixVatPer));
				product.setThirteenTaxper(DateTimeUtils.changeFormat(thirVatPer));
				product.setTableVal(DateTimeUtils.changeFormat(tableValtot));
				product.setTotalVat(DateTimeUtils.changeFormat(totvatTotal));
				product.setGrossVat(DateTimeUtils.changeFormat(totalGross));
				product.setNetVal(DateTimeUtils.changeFormat(totalNet));

				allfivetatTot = allfivetatTot + fivetaxTot;
				alltwelltaxTot = alltwelltaxTot + twelltaxTot;
				alleighteentaxTot = alleighteentaxTot + eighteentaxTot;
				alltwentyeighttaxTot = alltwentyeighttaxTot + twentyeighttaxTot;

				allfivetatPer = allfivetatPer + fivetaxPer;
				alltwelltaxPer = alltwelltaxPer + twelltaxPer;
				alleighteentaxPer = alleighteentaxPer + eighteentacPer;
				alltwentyeighttaxPer = alltwentyeighttaxPer + twentyeighttaxPer;

				allsixtaxTot = allsixtaxTot + sixtaxTot;
				allthirttaxTot = allthirttaxTot + thirttaxTot;
				allsixVatPer = allsixVatPer + sixVatPer;
				allthirVatPer = allthirVatPer + thirVatPer;
				alltableValtot = alltableValtot + tableValtot;
				alltotvatTotal = alltotvatTotal + totvatTotal;
				alltotalGross = alltotalGross + totalGross;
				alltotalNet = alltotalNet + totalNet;

				product.setAllfivetatTot(DateTimeUtils.changeFormat(allfivetatTot));
				product.setAlltwelltaxTot(DateTimeUtils.changeFormat(alltwelltaxTot));
				product.setAlleighteentaxTot(DateTimeUtils.changeFormat(alleighteentaxTot));
				product.setAlltwentyeighttaxTot(DateTimeUtils.changeFormat(alltwentyeighttaxTot));

				product.setAllfivetatPer(DateTimeUtils.changeFormat(allfivetatPer));
				product.setAlltwelltaxPer(DateTimeUtils.changeFormat(alltwelltaxPer));
				product.setAlleighteentaxPer(DateTimeUtils.changeFormat(alleighteentaxPer));
				product.setAlltwentyeighttaxPer(DateTimeUtils.changeFormat(alltwentyeighttaxPer));

				product.setAllsixtaxTot(DateTimeUtils.changeFormat(allsixtaxTot));
				product.setAllthirttaxTot(DateTimeUtils.changeFormat(allthirttaxTot));
				product.setAllsixVatPer(DateTimeUtils.changeFormat(allsixVatPer));
				product.setAllthirVatPer(DateTimeUtils.changeFormat(allthirVatPer));
				product.setAlltableValtot(DateTimeUtils.changeFormat(alltableValtot));
				product.setAlltotvatTotal(DateTimeUtils.changeFormat(alltotvatTotal));
				product.setAlltotalGross(DateTimeUtils.changeFormat(alltotalGross));
				product.setAlltotalNet(DateTimeUtils.changeFormat(alltotalNet));

				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	private ArrayList<Product> getVatReportVoucherandVendorWise(String vendor_id, String fromdate, String todate) {

		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		try {

			String sql = "Select id,prodid,voucherno,date from inventory_procurement where vendorid=" + vendor_id
					+ " and date between '" + fromdate + "' and '" + todate
					+ "' and voucherno is not null group by voucherno order by id desc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_id(rs.getString(2));
				product.setVoucherno(rs.getString(3));
				Product vatProduct = getTaxableAMtOnVoucher(product.getVoucherno());
				Vendor vendor = inventoryVendorDAO.getVendor(vendor_id);
				vatProduct.setId(product.getId());
				vatProduct.setVoucherno(product.getVoucherno());
				vatProduct.setVendor(vendor.getName());
				vatProduct.setTinno(vendor.getTinno());
				vatProduct.setDate(DateTimeUtils.getCommencingDate1(rs.getString(4)));

				list.add(vatProduct);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	private Product getTaxableAMtOnVoucher(String voucherno) {

		Product product = new Product();
		double six = 0;
		double thirteen = 0;
		double five = 0;
		double twelhe = 0;
		double eighteen = 0;
		double twentyeight = 0;
		try {
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			String sql = "select prodid,purprice,qty from inventory_procurement where voucherno='" + voucherno + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product master = inventoryProductDAO.getProduct(rs.getString(1));
				double rate = rs.getDouble(2);
				int qty = rs.getInt(3);

				double vatper = Double.parseDouble(master.getVat());
				if (vatper == 6) {

					double total = rate * qty;
					double temp = total * vatper / 100;
					six = six + temp;
				}
				if (vatper == 5) {
					double total = rate * qty;
					double temp = total * vatper / 100;
					five = five + temp;
				}
				if (vatper == 12) {
					double total = rate * qty;
					double temp = total * vatper / 100;
					twelhe = twelhe + temp;
				}
				if (vatper == 18) {
					double total = rate * qty;
					double temp = total * vatper / 100;
					eighteen = eighteen + temp;
				}
				if (vatper == 28) {
					double total = rate * qty;
					double temp = total * vatper / 100;
					twentyeight = twentyeight + temp;
				}

				if (vatper == 13.5) {
					double total = rate * qty;
					double temp = total * vatper / 100;
					thirteen = thirteen + temp;
				}

			}

			double sixVat = six * 6 / 100;
			double thirteenVat = thirteen * 13.5 / 100;
			double fiveVat = five * 5 / 100;
			double twellVat = twelhe * 12 / 100;
			double eighteenVat = eighteen * 18 / 100;
			double twentyeightVat = twentyeight * 28 / 100;

			double tableVal = six + thirteen + five + twelhe + eighteen + twentyeight;
			double totalVat = thirteenVat + sixVat + fiveVat + twellVat + eighteenVat + twentyeightVat;
			double netWat = totalVat + tableVal;

			product.setFiveTaxable(DateTimeUtils.changeFormat(five));
			product.setTwellTaxable(DateTimeUtils.changeFormat(twelhe));
			product.setEighteenTaxable(DateTimeUtils.changeFormat(eighteen));
			product.setTwentyeightTaxable(DateTimeUtils.changeFormat(twentyeight));

			product.setFivetaxPer(DateTimeUtils.changeFormat(fiveVat));
			product.setTwelltaxPer(DateTimeUtils.changeFormat(twellVat));
			product.setEighteentaxPer(DateTimeUtils.changeFormat(eighteenVat));
			product.setTwentyeighttaxPer(DateTimeUtils.changeFormat(twentyeightVat));

			product.setSixtaxable(DateTimeUtils.changeFormat(six));
			product.setThirttentax(DateTimeUtils.changeFormat(thirteen));
			product.setSixTaxper(DateTimeUtils.changeFormat(sixVat));
			product.setThirteenTaxper(DateTimeUtils.changeFormat(thirteenVat));
			product.setTableVal(DateTimeUtils.changeFormat(tableVal));
			product.setTotalVat(DateTimeUtils.changeFormat(totalVat));
			product.setGrossVat(DateTimeUtils.changeFormat(tableVal));
			product.setNetVal(DateTimeUtils.changeFormat(netWat));

		} catch (Exception e) {

			e.printStackTrace();
		}
		return product;
	}

	public String getProdPack(String prodid) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "select pack from inventory_product where id = " + prodid + " ";

		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public int savemdbarcodedata(String imgname, Product product) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_mdbarcode(img, product, bno,gprodid) values(?,?,?,?) ";

		try {
			/*if(product.getProduct_name()!=null){
				if(product.getProduct_name().length()>16){
					product.setProduct_name(product.getProd_name().substring(0,15));
				}
			}*/
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, imgname);
			preparedStatement.setString(2, product.getProduct_name());
			preparedStatement.setString(3, product.getBatch_no());
			preparedStatement.setString(4, ""+product.getGlobal_prodid());
			result = preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getmdBarcodeCount() {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "SELECT count(*) FROM apm_mdbarcode ";

		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public int deletemdbarcode() {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "truncate apm_mdbarcode ";

		try {
			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public int updateProductAjax(Product product) {

		int result = 0;
		try {
			String sql = "update inventory_product set prodname=?,mrp=?,purchaseprice=?,saleprice=?, genericname=?,stock=?,expiry_date=?,batch_no=?,mfg=?,cell=?,medicine_shedule=?,hsnno=?,location=? where id="
					+ product.getId() + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getProduct_name());
			ps.setString(2, product.getMrp());
			ps.setString(3, product.getPurchase_price());
			ps.setString(4, product.getSale_price());
			ps.setString(5, product.getGenericname());
			ps.setString(6, product.getStock());
			ps.setString(7, product.getExpiry_date());
			ps.setString(8, product.getBatch_no());
			ps.setString(9, product.getMfg());
			ps.setString(10, product.getShelf());
			ps.setString(11, product.getMedicine_shedule());
			ps.setString(12, product.getHsnno());
			ps.setString(13, product.getLocation());

			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int updateProductCatalogue(Product product) {

		int result = 0;
		try {
			String sql = "update inventory_catalogue set product_name=?,mrp=?,purchase_price=?,sale_price=?, genericname=?,mfg=?,hsnno=?,categoryid=?,subcategoryid=?,gst=?,pack=?,shedule=?,description=?,minorder=?,maxorder=?,product_code=? where id="
					+ product.getId() + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getProduct_name());
			ps.setString(2, product.getMrp());
			ps.setString(3, product.getPurchase_price());
			ps.setString(4, product.getSale_price());
			ps.setString(5, product.getGenericname());
			ps.setString(6, product.getMfg());
			ps.setString(7, product.getHsnno());
			ps.setString(8, product.getCategory_id());
			ps.setString(9, product.getSubcategory_id());
			ps.setString(10, product.getVat());
			ps.setString(11, product.getPack());
			ps.setString(12, product.getMedicine_shedule());
			ps.setString(13, product.getDescription());
			ps.setString(14, product.getMinorder());
			ps.setString(15, product.getMaxorder());
			ps.setString(16, product.getPro_code());
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getpatientlist() {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			// id, fullname, address, reference, age, gender, date, billno,
			// deliverstatus, mobile, pmbalance, location
			String sql = "select id,fullname,address,mobile from apm_pharmacy_client";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setFullname(rs.getString(2));
				String fullinfo = rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
				product.setAllinfo(fullinfo);
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Product> getallsupplierlist() {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select id,name from inventory_vendor";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Product> getMedicineListforVendor(String location) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			String sql = "";
			if (!location.equals("0")) {
				sql = "select id,product_name,genericname from inventory_catalogue where location=" + location
						+ " order by id desc ";
			} else {
				sql = "select id,product_name,genericname from inventory_catalogue order by id desc ";
			}
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				String name = rs.getString(2) + "(" + rs.getString(3) + ")";
				product.setProduct_name(name);
				product.setProd_name(rs.getString(2));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Product> getAllProdfrCatalogue(Pagination pagination, int i, String commencing, String categoryid,
			String searchtext, String location, String subcategoryid, LoginInfo loginInfo) {

		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(loginInfo.getUserId());
		String sql = "";
		try {

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
					"select id, categoryid, subcategoryid, product_name,genericname,pack, mfg, mrp, purchase_price, sale_price, gst, datetime, lastmodified, location, minorder,maxorder,product_code,shedule from inventory_catalogue ");
			// stringBuilder.append("where deleted=0 and stock>0 ");
			stringBuilder.append("where deleted=0 ");
			if (commencing != null) {
				stringBuilder.append("and lastmodified='" + commencing + "' ");
			}
			if (searchtext != null) {
				if (searchtext == "" || searchtext.equals("")) {
				} else
					stringBuilder.append(" and (product_name like ('%" + searchtext + "%') or genericname like ('"
							+ searchtext + "%') or product_code like ('" + searchtext + "%') ) ");
			}

			if (!location.equals("0")) {
				stringBuilder.append(" and location=" + location + " ");
			}
			if (!categoryid.equals("0")) {
				stringBuilder.append(" and categoryid=" + categoryid + " ");
			}

			if (!subcategoryid.equals("0")) {
				stringBuilder.append(" and subcategoryid=" + subcategoryid + " ");
			}

			// stringBuilder.append(" order by product_name ");
			stringBuilder.append(" order by id desc ");
			if (pagination != null) {
				sql = pagination.getSQLQuery(stringBuilder.toString());
			} else {
				sql = stringBuilder.toString();
			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setCategory_id(rs.getString(2));
				product.setSubcategory_id(rs.getString(3));
				product.setProduct_name(rs.getString(4));
				product.setGenericname(rs.getString(5));
				product.setPack(rs.getString(6));
				product.setMfg(rs.getString(7));
				product.setMrp(DateTimeUtils.changeStringFormat(rs.getString(8)));
				product.setPurchase_price(DateTimeUtils.changeStringFormat(rs.getString(9)));
				product.setSale_price(DateTimeUtils.changeStringFormat(rs.getString(10)));
				product.setVat(rs.getString(11));
				product.setDateTime(rs.getString(12));
				product.setLastmodified(rs.getString(13));
				product.setLocation(rs.getString(14));
				product.setMinorder(rs.getString(15));
				product.setMaxorder(rs.getString(16));
				product.setEdit_catalogue(priscription.getEdit_catalogue());

				if (product.getCategory_id().equals("2")) {

					String data = product.getProduct_name() + "(" + product.getGenericname() + ")";
					product.setProduct_name(data);
				}
				product.setLocationName(pharmacyDAO.getPharmacyLocation(product.getLocation()));
				if (product.getLastmodified() != null) {

					String purchase_date = DateTimeUtils.getCommencingDate1(product.getLastmodified());
					product.setLastmodified(purchase_date);
				}

				if (product.getPurchase_price() == null) {
					product.setPurchase_price("0");
				}
				if (product.getPurchase_price().equals("")) {
					product.setPurchase_price("0");
				}
				if (product.getVat() == null) {
					product.setVat("0");
				}
				if (product.getVat().equals("")) {
					product.setVat("0");
				}

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				if (brand.getName() == null) {
					product.setBrand(product.getMfg());
				} else if (brand.getName().equals("")) {
					product.setBrand(product.getMfg());
				}
				product.setPro_code(rs.getString(17));
				product.setShedule(DateTimeUtils.isNull(rs.getString(18)));
				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public String getMedicineTypeNameFromId(String string) {
		String med_name = "";
		try {
			String query = "select name from apm_medicine_type where id=" + string + "";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				med_name = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return med_name;
	}

	public int geTotalProductCountfrCatalogue(String date, String categoryid, String searchtext, String location,
			String subcategoryid) {

		int result = 0;
		String sql = "";
		try {
			StringBuilder stringBuilder = new StringBuilder();
			// stringBuilder.append("select count(*) from inventory_product
			// where stock>0 and deleted=0 ");
			stringBuilder.append("select count(*) from inventory_catalogue where deleted=0 ");

			if (date != null) {
				stringBuilder.append("and lastmodified='" + date + "' ");
			}

			if (searchtext != null) {
				if (searchtext == "" || searchtext.equals("")) {

				} else
					stringBuilder.append(" and (product_name like ('%" + searchtext + "%') or genericname like ('"
							+ searchtext + "%') or product_code like ('" + searchtext + "%') ) ");
			}

			if (!location.equals("0")) {
				stringBuilder.append(" and location='" + location + "'");
			}

			if (!categoryid.equals("0")) {
				stringBuilder.append(" and categoryid='" + categoryid + "'");
			}

			if (!subcategoryid.equals("0")) {
				stringBuilder.append(" and subcategoryid='" + subcategoryid + "'");
			}

			sql = stringBuilder.toString();
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public double getOpeningStock(String fromdate, String todate) {

		double res = 0;
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(fromdate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			fromdate = dateFormat.format(calendar.getTime());

			calendar = Calendar.getInstance();
			date = dateFormat.parse(todate);
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			todate = dateFormat.format(calendar.getTime());

			String sql = "select sum(payment) from apm_medicine_payment where date between '" + fromdate + "' and '"
					+ todate + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				res = rs.getDouble(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public double getPurchaseStock(String fromdate, String todate) {

		double result = 0;
		try {

			String sql = "select sum(total) from inventory_procurement where date between '" + fromdate + "' and '"
					+ todate + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				result = rs.getDouble(1);
				double discount = getTotalDiscount(fromdate, todate);
				double vat = getTotalVat(fromdate, todate);

				double t = result - discount;
				result = t + vat;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public double getSoldMedicineAmt(String fromdate, String todate) {

		double res = 0;
		try {

			String sql = "select sum(payment) from apm_medicine_payment where date between '" + fromdate + "' and '"
					+ todate + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				res = rs.getDouble(1);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return res;
	}

	public double getTotalVat(String fromdate, String todate) {

		double res = 0;
		try {

			String sql = "select sum(vat) from inventory_vendor_procurement where date between '" + fromdate + "' and '"
					+ todate + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				res = rs.getDouble(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return res;
	}

	public double getTotalDiscount(String fromdate, String todate) {

		double res = 0;
		try {

			String sql = "select sum(discount) from inventory_vendor_procurement where date between '" + fromdate
					+ "' and '" + todate + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				res = rs.getDouble(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return res;
	}

	public int updateStock(int nowstock, String product_id) {

		int result = 0;
		try {

			String sql = "update inventory_product set stock='" + nowstock + "' where id='" + product_id + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);

			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public int getMedicineDetails() {
		int result = 0;
		try {
			String sql = "select genericname,prodname,medicine_shedule from  inventory_product group by prodname";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setGeneric_name(rs.getString(1));
				product.setProd_name(rs.getString(2));
				product.setMedicine_shedule(rs.getString(3));
				result = setmedicinedetails(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int setmedicinedetails(Product product) {
		int result = 0;
		try {
			String sql = "insert into apm_medicine_details (drug,genericname,medicine_shedule) values (?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getProd_name());
			ps.setString(2, product.getGeneric_name());
			ps.setString(3, product.getMedicine_shedule());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int addTOMedicineMaster(Product product) {

		int result = 0;
		try {

			String sql = "select id from apm_medicine_details where drug='" + product.getProduct_name()
					+ "' and genericname='" + product.getGenericname() + "' ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int id = 0;
			while (rs.next()) {

				id = rs.getInt(1);

			}

			if (id == 0) {

				String sql2 = "insert into apm_medicine_details(drug,genericname,medicine_shedule) values (?,?,?) ";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ps2.setString(1, product.getProduct_name());
				ps2.setString(2, product.getGenericname());
				ps2.setString(3, product.getMedicine_shedule());
				result = ps2.executeUpdate();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getItemWiseReportList(String fromdate, String todate, String product_id,
			String location) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
			Product product = getProduct(product_id);
			int stock = getTotalStock(product.getProduct_name());
			StringBuffer buffer = new StringBuffer();
			buffer.append("select id,supplierid from inventory_product  ");
			if (!product_id.equals("0")) {
				buffer.append("where prodname like ('%" + product.getProduct_name() + "%') ");
			}
			buffer.append("group by supplierid order by id desc ");

			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				qty = stock;
				String sql1 = "";
				String pid = rs.getString(1);
				product = getProduct(pid);
				stock = getTotalStock(product.getProduct_name());
				String vendorid = rs.getString(2);
				if (!location.equals("0") && !location.equals("1")) {
					sql1 = "select  id,stock from inventory_product where supplierid ='" + vendorid
							+ "' and prodname like ('%" + product.getProduct_name() + "%') and location=" + location
							+ " ;";
				} else {
					sql1 = "select  id,stock from inventory_product where supplierid ='" + vendorid
							+ "' and prodname like ('%" + product.getProduct_name() + "%')  ;";
				}

				PreparedStatement ps1 = connection.prepareStatement(sql1);
				ResultSet rs1 = ps1.executeQuery();
				Product master = new Product();
				ArrayList<Product> innerProductList = new ArrayList<Product>();
				while (rs1.next()) {

					pid = rs1.getString(1);
					master.setId(rs1.getInt(1));

					ArrayList<Product> tempList = getProductItemReport(pid, fromdate, todate, stock,location);
					innerProductList.addAll(tempList);

				}
				if (innerProductList.size() > 0) {
					master.setInnerProductList(innerProductList);
				}
				Vendor vendor = inventoryVendorDAO.getVendor(vendorid);
				master.setVendor(vendor.getName());
				master.setProduct_name(product.getProduct_name());
				master.setGenericname(product.getGenericname());
				master.setHsnno(product.getHsnno());

				master.setStock(String.valueOf(stock));
				if (innerProductList.size() > 0) {
					list.add(master);
				}
				qty = 0;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	int qty = 0;

	private ArrayList<Product> getProductItemReport(String pid, String fromdate, String todate, int stock, String location) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select apm_medicine_bill.date,isreturn, ");
			buffer.append("apm_medicine_charges.practitionerName,apm_medicine_charges.user,apm_medicine_charges.invoiceid, ");
			buffer.append("apm_medicine_charges.quantity,apm_medicine_bill.cgst,apm_medicine_bill.sgst,apm_medicine_bill.id, ");
			buffer.append("apm_condition.name,apm_medicine_charges.charge,apm_medicine_charges.gstper,apm_medicine_charges.cgst,   ");
			buffer.append("apm_medicine_charges.sgst,apm_medicine_charges.discount_share from apm_medicine_bill ");
			buffer.append("left join apm_condition on apm_condition.id=apm_medicine_bill.location ");
			buffer.append("inner join apm_medicine_charges on apm_medicine_bill.id= apm_medicine_charges.invoiceid ");
			buffer.append("where apm_medicine_charges.product_id=" + pid + " and apm_medicine_bill.date between '"
					+ fromdate + "' and '" + todate + "' and apm_medicine_bill.deleted=0 ");
			
			if(!location.equals("0")){
				buffer.append("and apm_medicine_bill.location='"+location+"' ");
			}
			
			buffer.append(" order by apm_medicine_bill.date desc ");

			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			PharmacyDAO pharmacyDAO=new JDBCPharmacyDAO(connection);
			int qtycount=0;
			int retrunqtycount=0;
			while (rs.next()) {

				Product product = new Product();
				product = getProduct(pid);
				product.setDate(DateTimeUtils.getCommencingDate1(rs.getString(1)));
				int ret = rs.getInt(2);
				String doctor = rs.getString(3);
				String clientname = rs.getString(4);
				String billno = rs.getString(5);
				int qt = 0;
				if(ret>0){
					product.setQty("0");
					retrunqtycount = retrunqtycount+rs.getInt(6);
				}else{
					product.setQty(rs.getString(6));
					qtycount=rs.getInt(6)+qtycount;
					qt = rs.getInt(6);
				}
				product.setRetrunqtycount(retrunqtycount);
				product.setTotalqty(qtycount);
				int totalsumqty = qtycount - retrunqtycount;
				product.setTotalsumqty(totalsumqty);
				qty = qty - qt;
				product.setStock(String.valueOf(qty));
				product.setDoctor(doctor);
				product.setClientname(clientname);
				product.setUserid(getUserIdOfBill(billno));
				Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(product.getUserid());
				product.setFullname(priscription.getFullname());
				if (ret > 0) {
					product.setReturnQty(rs.getString(6));
				} else {
					product.setReturnQty("0");
				}
				product.setCgst(rs.getString(7));
				product.setSgst(rs.getString(8));
				double cal_gst = rs.getDouble(6) * Double.parseDouble(product.getPurchase_price());
				if (product.getVat() == null) {
					product.setVat("0");
				} else if (product.getVat().equals("")) {
					product.setVat("0");
				}
				
				product.setBillno(rs.getString(9));
				product.setLocationName(rs.getString(10));
				product.setNew_mrp(product.getMrp());
				product.setMrp(rs.getString(11));
				//12,13,14,15
				product.setVat(rs.getString(12));
				product.setCgst(rs.getString(13));
				product.setSgst(rs.getString(14));
				product.setDiscount(rs.getString(15));
				product.setTotalAmt(Math.round((rs.getInt(6)*rs.getDouble(11)) * 100.0)/100.0);
				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public int getTotalStock(String prodname) {

		int stock = 0;
		try {

			String sql = "select sum(stock) from inventory_product where prodname like ('%" + prodname + "%');";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				stock = rs.getInt(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return stock;
	}

	public String getUserIdOfBill(String billno) {

		String res = "";
		try {
			String sql = "select userid from apm_medicine_payment where billno=" + billno + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				res = rs.getString(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return res;
	}

	public ArrayList<Product> getProductWiseReportList(String fromdate, String todate, String vendorid,
			   String location,String purchaseinvoice, Pagination pagination) {
			  ArrayList<Product> list = new ArrayList<Product>();
			  try {
			   ProcurementDAO procurementDAO = new JDBCProcurementDAO(connection);
			   PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
			   PoPaymenytDAO poPaymenytDAO = new JDBCPoPaymengtDAO(connection);
			   InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
			   StringBuffer buffer = new StringBuffer();
			   if(purchaseinvoice!=null)
			   {
			    if(purchaseinvoice.equals("Invoice Date"))
			    {
			        buffer.append("select count(*),procurementid,inventory_procurement.voucherno,inventory_procurement.voucherdate,location,inventory_procurement.date,inventory_procurement.vendorid,sum(total),inventory_procurement.id from inventory_procurement ");
				    buffer.append("inner join inventory_parent_procurement on inventory_parent_procurement.id = inventory_procurement.procurementid ");
			    	buffer.append("where inventory_procurement.confirm=1 and inventory_procurement.gudreceipt=1 and inventory_procurement.deleted=0 and isdelivermemo=0 ");
				    if (vendorid != null) {
				     buffer.append("and inventory_procurement.vendorid=" + vendorid + " ");
				    }
				    if (!location.equals("0") && !location.equals("1")) {
				     buffer.append("and location=" + location + " ");
				    }
				    buffer.append("and inventory_procurement.voucherdate between '" + fromdate + "' and '" + todate + "' ");
	
				    buffer.append("group by procurementid order by inventory_procurement.voucherdate desc");
			    }
			    else
			    {
			     
			     
			     buffer.append(
			       "select count(*),procurementid,inventory_procurement.voucherno,inventory_procurement.voucherdate,location,date,inventory_procurement.vendorid,sum(total),inventory_procurement.id from inventory_procurement ");
			     buffer.append("inner join inventory_parent_procurement on inventory_parent_procurement.id = inventory_procurement.procurementid ");
			     buffer.append("where inventory_procurement.confirm=1 and inventory_procurement.gudreceipt=1 and inventory_procurement.deleted=0 and isdelivermemo=0 ");
			     if (vendorid != null) {
			      buffer.append("and inventory_procurement.vendorid=" + vendorid + " ");
			     }
			     if (!location.equals("0") && !location.equals("1")) {
			      buffer.append("and location=" + location + " ");
			     }
			     buffer.append("and inventory_procurement.date between '" + fromdate + "' and '" + todate + "' ");

			     buffer.append("group by procurementid order by date desc");
			    }
			   }
			   else
			   {
			    buffer.append(
			    "select count(*),procurementid,inventory_procurement.voucherno,inventory_procurement.voucherdate,location,inventory_procurement.date,inventory_procurement.vendorid,sum(total),inventory_procurement.id from inventory_procurement ");
			    buffer.append("inner join inventory_parent_procurement on inventory_parent_procurement.id = inventory_procurement.procurementid ");
		    	
			    buffer.append("where inventory_procurement.confirm=1 and inventory_procurement.gudreceipt=1 and inventory_procurement.deleted=0 and isdelivermemo=0 ");
			  if (vendorid != null) {
			   buffer.append("and inventory_procurement.vendorid=" + vendorid + " ");
			  }
			  if (!location.equals("0") && !location.equals("1")) {
			   buffer.append("and location=" + location + " ");
			  }
			  buffer.append("and inventory_procurement.date between '" + fromdate + "' and '" + todate + "' ");

			  buffer.append("group by procurementid order by date desc ");
			   }
			   
			   String sql=buffer.toString();
			   if(pagination!=null){
				   sql= pagination.getSQLQuery(sql);
			   }
			   PreparedStatement ps = connection.prepareStatement(sql);
			   ResultSet rs = ps.executeQuery();
			   double nettotal = 0;
			   double allttal = 0;
			   double totalsubtotal=0;
			   double totalvat =0;
			   while (rs.next()) {
			    Product product = new Product();
			    product.setProcurementid(rs.getString(2));
			    ArrayList<Product> innerProductList= procurementDAO.getProcurementProductList(product.getProcurementid(),0);
			    product.setInnerProductList(innerProductList);
			    product.setVoucherno(rs.getString(3));
			    product.setVoucherdate(DateTimeUtils.getCommencingDate1(rs.getString(4)));
			    product.setLocation(pharmacyDAO.getLocationName(rs.getString(5)));
			    product.setDate(DateTimeUtils.getCommencingDate1(rs.getString(6)));
			    product.setVendor_id(rs.getString(7));
			    Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());
			    // ArrayList<Product> voucherlistData=
			    // procurementDAO.getProcurementVoucherProductList(product.getVoucherno());
			    double tot = 0;
			    int i = 0;
			    /*
			     * double cgst=0; double sgst=0;
			     */
			    /*
			     * for(Product master: voucherlistData) {
			     * 
			     * tot= tot+Double.parseDouble(master.getTotal()); i++; }
			     */
			    product.setId(i);
			    product.setSubTotal(DateTimeUtils.changeFormat(rs.getDouble(8)));
			    Product prodDi = procurementDAO.getProcAccountDetails(product.getProcurementid());
			    double vat = Double.parseDouble(prodDi.getVat());
			    double disc = Double.parseDouble(prodDi.getDiscvat());

			    double d = tot + vat;
			    double net = d - disc;
			    product.setDiscvat(DateTimeUtils.changeFormat(disc));
			    product.setVat(DateTimeUtils.changeFormat(vat));
			    product.setTotalamt(DateTimeUtils.changeFormat(net));
			    nettotal = nettotal + net;

			    /*
			     * for(Product master: voucherlistData){
			     * 
			     * master.setProcurementid(product.getProcurementid());
			     * master.setVoucherno(product.getVoucherno());
			     * master.setVoucherdate(product.getVoucherdate());
			     * master.setLocation(product.getLocation());
			     * master.setDate(product.getDate());
			     * master.setVendor_id(product.getVendor_id());
			     * master.setId(product.getId());
			     * master.setCgst(prodDi.getCgst());
			     * master.setSgst(prodDi.getSgst());
			     * master.setIgst(prodDi.getIgst());
			     * master.setSubTotal(product.getSubTotal());
			     * master.setDiscvat(product.getDiscvat());
			     * master.setVat(product.getVat());
			     * master.setVendor(vendor.getName());
			     * master.setTotalamt(DateTimeUtils.changeFormat(Math.round(
			      Double.parseDouble(product.getTotalamt())  100.0) / 100.0));
			      master.setTotal_amount(Math.round(nettotal  100.0) / 100.0);
			     * list.add(master); }
			     */
			    int dmcmplted = procurementDAO.checkIsComplatedDm(product.getProcurementid());
			    double tamt = poPaymenytDAO.getTotalVoucherAmount(product.getVoucherno(), product.getProcurementid(),dmcmplted);
			    allttal = allttal + tamt;
			    product.setId(product.getId());
			    product.setCgst(prodDi.getCgst());
			    product.setSgst(prodDi.getSgst());
			    product.setIgst(prodDi.getIgst());
			    product.setSubTotal(product.getSubTotal());
			    product.setDiscvat(product.getDiscvat());
			    product.setVat(product.getVat());
			    product.setVendor(vendor.getName());
			    product.setTotalamt(DateTimeUtils.changeFormat(Math.round(tamt * 100.0) / 100.0));
			    product.setTotal_amount(Math.round(allttal * 100.0) / 100.0);
			    product.setId(rs.getInt(9));
			    totalsubtotal = totalsubtotal + rs.getDouble(8);
			    totalvat = totalvat + vat;
			    product.setTotalvat(totalvat);
			    product.setTotalsubtotal(totalsubtotal);
			    list.add(product);

			   }

			  } catch (Exception e) {

			   e.printStackTrace();
			  }

			  return list;
			 }

	public ArrayList<Master> getMedicineTypeList() {

		ArrayList<Master> list = new ArrayList<Master>();
		String sql = "select id, name,isstrip from apm_medicine_type ";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				master.setStrip(rs.getInt(3));
				list.add(master);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	/*public ArrayList<Product> getSaleReturnReport(String fromdate, String todate, String location, String salereturun,
			String billtype) {

		ArrayList<Product> list = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		try {
			StringBuffer buffer = new StringBuffer();

			buffer.append(
					"select apm_medicine_bill.id,apm_medicine_bill.date,debit,apm_medicine_bill.clientid,apm_medicine_bill.pclientid,apm_medicine_bill.location,cgst,sgst,time,isreturn,priscid,payment,paymode from apm_medicine_bill   ");
			buffer.append("inner join apm_medicine_payment on apm_medicine_payment.billno = apm_medicine_bill.id ");
			buffer.append("where apm_medicine_bill.date between '" + fromdate + "' and '" + todate + "' ");

			
			 * if(!billtype.equals("0")){ buffer= new StringBuffer(); buffer.
			 * append("select apm_medicine_bill.id,apm_medicine_bill.date,debit,apm_medicine_bill.clientid,apm_medicine_bill.pclientid,apm_medicine_bill.location,cgst,sgst,time,isreturn,priscid,paymode  "
			 * ); buffer.
			 * append(" from apm_medicine_bill inner join apm_medicine_payment on "
			 * ); buffer.
			 * append(" apm_medicine_bill.id = apm_medicine_payment.billno where "
			 * ); buffer.append("apm_medicine_payment.date between '"
			 * +fromdate+"' and '"+todate+"' and paymode='"+billtype+"' "); }
			 

			if (!billtype.equals("0")) {
				buffer.append("and paymode='" + billtype + "' ");
			}

			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}

			if (salereturun.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}

			if (salereturun.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}

			buffer.append("and apm_medicine_bill.tpid='0' ");
			buffer.append("order by apm_medicine_bill.id desc ");

			PreparedStatement ps = connection.prepareStatement(buffer.toString());

			ResultSet rs = ps.executeQuery();
			double tot = 0;
			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setDate(rs.getString(2));
				product.setDebit(rs.getString(3));
				product.setClientid(rs.getString(4));
				product.setPclientid(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setCgst(rs.getString(7));
				product.setSgst(rs.getString(8));
				product.setTime(rs.getString(9));
				product.setIsreturn(rs.getInt(10));
				product.setPriscid(rs.getString(11));
				// double payAmt= getTotalReceived(product.getId());
				double payAmt = rs.getDouble(12);
				tot = tot + payAmt;
				product.setPayment(tot);
				product.setPayAmount(DateTimeUtils.changeFormat(payAmt));
				// if(!billtype.equals("0")){
				product.setBilltype(rs.getString(13));
				// }

				String date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				product.setDateTime(date + " " + product.getTime());

				double temp = Double.parseDouble(product.getSgst()) * 2;
				double withoutGST = rs.getDouble(3) - temp;
				product.setTotal(DateTimeUtils.changeFormat(withoutGST));
				if (product.getIsreturn() > 0) {
					product.setType("Sales Return");
				} else {
					product.setType("Sales");
				}
				if (!product.getPclientid().equals("0")) {

					Priscription priscription = pharmacyDAO.getPharmacyPatient(product.getPclientid());
					product.setClienttype("-");
					product.setTpname("NO");
					product.setDoctor(priscription.getPractitionername());
					product.setClientname(priscription.getFullname());
				} else {

					String isipdid = pharmacyDAO.getIpdIdFromClientID(rs.getInt(4));
					if (isipdid.equals("0")) {
						product.setClienttype("OPD");
					} else {
						product.setClienttype("IPD");
					}
					ClientDAO clientDAO = new JDBCClientDAO(connection);
					Client client = clientDAO.getClientDetails(product.getClientid());
					if (client.getTypeName() != null) {
						if (!client.getTypeName().equals("0")) {
							product.setTpname("YES");
						} else {
							product.setTpname("NO");
						}
					} else {
						product.setTpname("NO");
					}
					product.setClientname(client.getTitle() + " " + client.getFirstName() + " " + client.getLastName());
					if (!product.getPriscid().equals("0")) {
						Priscription priscription = emrDAO.getEditPriscription(product.getPriscid());
						UserProfile userProfile = userProfileDAO
								.getUserprofileDetails(Integer.parseInt(priscription.getPrectionerid()));
						product.setDoctor(userProfile.getFullname());
					} else {
						product.setDoctor(" ");
					}

				}
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}*/

	public boolean isStrip(String mtypeid) {

		int res = 0;
		try {
			String sql = "select isstrip from inventory_subcategory where id=" + mtypeid + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				res = rs.getInt(1);
			}

			if (res == 1) {
				return true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}

	public int transferProductPTP(Product product, String location, String qty, int prodid, int parentid,
			String comment,String trans_ret) {
		int result = 0;
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		try {
			
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar=Calendar.getInstance();
			String date=dateFormat.format(calendar.getTime());  
			
			// minus stock
			int oldstock = Integer.parseInt(product.getStock());
			int tqty = oldstock - Integer.parseInt(qty);
			int previousstock = oldstock;
			String query = "update inventory_product set stock=" + tqty + " where id=" + prodid + "";
			PreparedStatement statement = connection.prepareStatement(query);
			int res = statement.executeUpdate();
			
			//stock log
			String datetime = product.getDateTime();
			int qtyinout=1;
			String source ="Return To Store Out";
			if(trans_ret.equals("1")){
				source ="Direct Transfer Out";
			}
			
			int currentstock=tqty;
			int changeqty=Integer.parseInt(qty);
			int reslog = insertIntoProductLog(product.getUserid(),datetime,product.getLocation(),qtyinout,""+prodid,product.getCatalogueid(),source,currentstock,previousstock,changeqty,"0","0",0,0,parentid,"0");
			
			boolean checkopningstockexist = pharmacyDAO.checkopeningstockexist(""+prodid,date);
			if(!checkopningstockexist){
				int r = pharmacyDAO.saveOpeningStock(""+prodid,date,previousstock,"0");
			}
			
			// check medicine already present or not
			String query2 = "";
			// search by catalogue id instead of prodname 
			query2 = "select id,stock from inventory_product where expiry_date='" + product.getExpiry_date()
					+ "' and batch_no='" + product.getBatch_no() + "' and catalogueid='"+product.getCatalogueid()+"' and location=" + location + " and procurement=0";
			
			/*query2 = "select id,stock from inventory_product where global_prodid='"+product.getGlobal_prodid()+"' and location=" + location + " and procurement=0";*/
			
			PreparedStatement ps1 = connection.prepareStatement(query2);
			ResultSet resultSet = ps1.executeQuery();
			int id = 0;
			String stock = "0";
			if (resultSet.next()) {
				id = resultSet.getInt(1);
				stock = resultSet.getString(2);
			}

			if (id > 0) {
				// if present then update stock means plus stock
				int newqty = Integer.parseInt(stock) + Integer.parseInt(qty);
				String query3 = "update inventory_product set stock=" + newqty + " where id=" + id + "";
				PreparedStatement statement2 = connection.prepareStatement(query3);
				int res1 = statement2.executeUpdate();
				
				//stock log
				qtyinout=0;
				source ="Return To Store IN";
				if(trans_ret.equals("1")){
					source ="Direct Transfer IN";
				}
				
				currentstock=newqty;
				changeqty=Integer.parseInt(qty);
				previousstock = Integer.parseInt(stock);
				reslog = insertIntoProductLog(product.getUserid(),datetime,location,qtyinout,""+id,product.getCatalogueid(),source,currentstock,previousstock,changeqty,"0","0",0,0,parentid,"0");
				
				boolean checkopningstockexist2 = pharmacyDAO.checkopeningstockexist(""+id,date);
				if(!checkopningstockexist2){
					int r = pharmacyDAO.saveOpeningStock(""+id,date,previousstock,"0");
				}
				
				//Akash 28 Nov 2018 for opeing closing report
				boolean checkopningstockexist1 = pharmacyDAO.checkopeningstockexist(""+id,date);
				if(checkopningstockexist1){
					Product openingproduct = pharmacyDAO.checkopeningstockexistData(""+id,date);
					int indentqty =  Integer.parseInt(qty)+openingproduct.getIndentqty();
					int ress = pharmacyDAO.updateIndentQtyInOpening(openingproduct,indentqty);
				}
			} else {
				// else add new
				
				//Akash 03 May 2018
				String added_date=date;
				/*DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				added_date = dateFormat.format(cal.getTime());
				added_date = DateTimeUtils.getCommencingDate1(added_date);*/
				
				StringBuffer sql = new StringBuffer(
						"insert into inventory_product (prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,mdicinenameid,expiry_date,tax,lastmodified,batch_no,vat,genericname,cell,pack,medicine_type,mfg,medicine_shedule,freeqty,userid,hsnno,location,catalogueid,added_date,barcode)");
				sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				PreparedStatement ps = connection.prepareStatement(sql.toString());
				ps.setString(1, product.getSubcategory_id());
				ps.setString(2, product.getVendor_id());
				ps.setString(3, product.getBrand_id());
				ps.setString(4, product.getProduct_code());
				ps.setString(5, product.getProduct_name());
				ps.setString(6, product.getMrp());
				ps.setString(7, product.getPurchase_price());
				ps.setString(8, product.getSale_price());
				ps.setString(9, product.getPurchase_discount());
				ps.setString(10, product.getSale_discount());
				ps.setString(11, product.getWeight());
				ps.setString(12, product.getUnit());
				ps.setString(13, product.getDescription());
				ps.setString(14, product.getCategory_id());
				ps.setString(15, qty);
				ps.setString(16, product.getMedicinenameid());
				ps.setString(17, product.getExpiry_date());
				ps.setString(18, product.getTax());
				ps.setString(19, product.getLastmodified());
				ps.setString(20, product.getBatch_no());
				ps.setString(21, product.getVat());
				ps.setString(22, product.getGenericname());
				ps.setString(23, "");
				ps.setString(24, product.getPack());
				ps.setString(25, product.getMedicine_type());
				ps.setString(26, product.getMfg());
				ps.setString(27, product.getMedicine_shedule());
				ps.setString(28, product.getFreeqty());
				ps.setString(29, null);
				ps.setString(30, product.getHsnno());
				ps.setString(31, location);
				ps.setString(32, product.getCatalogueid());
				ps.setString(33, added_date);
				ps.setString(34, product.getBarcode());
				result = ps.executeUpdate();
				if(result>0)
				{
					ResultSet resultSet2=ps.getGeneratedKeys();
					while(resultSet2.next()){
					     id= resultSet2.getInt(1);	
					}
				}
				
				int globleproductid = updateGlobalProductId(id, product.getGlobal_prodid());
				
				//stock log
				qtyinout=0;
				source ="Return To Store IN";
				if(trans_ret.equals("1")){
					source ="Direct Transfer IN";
				}
				
				currentstock=Integer.parseInt(qty);
				changeqty=Integer.parseInt(qty);
				previousstock = 0;
				reslog = insertIntoProductLog(product.getUserid(),datetime,location,qtyinout,""+id,product.getCatalogueid(),source,currentstock,previousstock,changeqty,"0","0",0,0,parentid,"0");
				
				boolean checkopningstockexist2 = pharmacyDAO.checkopeningstockexist(""+id,date);
				if(!checkopningstockexist2){
					int r = pharmacyDAO.saveOpeningStock(""+id,date,previousstock,"0");
				}
				
				//Akash 28 Nov 2018 for opeing closing report
				boolean checkopningstockexist1 = pharmacyDAO.checkopeningstockexist(""+id,date);
				if(checkopningstockexist1){
					Product openingproduct = pharmacyDAO.checkopeningstockexistData(""+id,date);
					int indentqty =  Integer.parseInt(qty)+openingproduct.getIndentqty();
					int ress = pharmacyDAO.updateIndentQtyInOpening(openingproduct,indentqty);
				}
				
			}
			int res1 = saveChildProductTransfer(prodid, id, qty, parentid, comment,product.getCatalogueid(),trans_ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int truncatetemporaytable(String loginsesseionid) {
		int result = 0;
		try {
			String sql = "delete from inventory_transfer_temporary where sessionlogid='"+loginsesseionid+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private int saveChildProductTransfer(int prodid, int id, String qty, int parentid, String comment,String catid,String trans_ret) {
		int result = 0;
		try {
			String sql = "insert into inventory_transfer_log (parentid, old_prodid, new_prodid, qty,comment,req_trans_ret, catlogueid) values (?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "" + parentid);
			preparedStatement.setString(2, "" + prodid);
			preparedStatement.setString(3, "" + id);
			preparedStatement.setString(4, qty);
			preparedStatement.setString(5, comment);
			preparedStatement.setString(6, trans_ret);
			preparedStatement.setString(7, catid);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int transferProductTemporary(int prodid, String firstlocation, String location, String qty, String comment, String loginsessionid) {
		int result = 0;
		try {

			String sql = "insert into inventory_transfer_temporary (from_location, to_location, prodid, qty,comment,sessionlogid) values (?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, firstlocation);
			preparedStatement.setString(2, location);
			preparedStatement.setString(3, "" + prodid);
			preparedStatement.setString(4, qty);
			preparedStatement.setString(5, comment);
			preparedStatement.setString(6, loginsessionid);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<String> getToLocationList(String loginsessionid) {
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			String sql = "select to_location from inventory_transfer_temporary where sessionlogid=? group by to_location";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, loginsessionid);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String loc = rs.getString(1);
				arrayList.add(loc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Product> getTempTransferData(String string, String loginsessionid) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select from_location, to_location, prodid, qty,comment from inventory_transfer_temporary where to_location="
					+ string + " and sessionlogid=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, loginsessionid);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setLocation(rs.getString(1));
				product.setTlocation(rs.getString(2));
				product.setId(Integer.parseInt(rs.getString(3)));
				product.setStock(rs.getString(4));
				product.setComment(rs.getString(5));
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int saveParentProductTransfer(String firstlocation, String tolacation, String todate, String time,
			String comment1, String userid, int indentcount) {
		int result = 0;
		try {
			String sql = "insert into inventory_parent_transfer_log (request_date, issued_date, from_location, to_location,time,req_or_trans,remark,userid,trans_indent_no,r_status,warehouse_id) values (?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, todate);
			preparedStatement.setString(2, todate);
			preparedStatement.setString(3, firstlocation);
			preparedStatement.setString(4, tolacation);
			preparedStatement.setString(5, time);
			preparedStatement.setString(6, "1");
			preparedStatement.setString(7, comment1);
			preparedStatement.setString(8, userid);
			preparedStatement.setString(9, "" + indentcount);
			preparedStatement.setString(10, "8");
			preparedStatement.setString(11, "0");
			result = preparedStatement.executeUpdate();
			if (result > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();

				while (resultSet.next()) {
					result = resultSet.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int getCountTransferDashboard(String searchtext, String fromdate, String todate, String location,
			String filter_status, boolean flag, String indentapprove, String filter_bydate,int showindentreq){
		int result = 0;
		try {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("select count(*) from inventory_parent_transfer_log where deleted=0 ");
			//buffer.append("and request_date between '" + fromdate + "' and '" + todate + "'  ");
			
			if(filter_bydate.equals("0")){
				buffer.append("and request_date between '" + fromdate + "' and '" + todate + "' ");
			}else if(filter_bydate.equals("1")){
				buffer.append("and admin_aprove_date between '" + fromdate + "' and '" + todate + "' ");
			}else if(filter_bydate.equals("2")){
				buffer.append("and (deliver_Date between '" + fromdate + "' and '" + todate + "' or issued_date between '" + fromdate + "' and '" + todate + "') ");
			}
			
			if (searchtext != null) {
				buffer.append("and id ='" + searchtext + "'  ");
			}
			
			if (!filter_status.equals("10")) {
				if (filter_status.equals("8")) {
					buffer.append("and req_or_trans=1 ");
				} else {
					buffer.append("and r_status=" + filter_status + " ");
				}
			}
			if(showindentreq>0){
				buffer.append("and (from_location in ("+location+") or to_location in ("+location+")) ");
			}else if (!location.equals("0")) {
				buffer.append("and (warehouse_id in (" + location + ") or from_location in ("+location+") or to_location in ("+location+")) ");
			}
			
			if(showindentreq>0){
				buffer.append("and r_status!='1'  ");
			}else if(flag){
				buffer.append("and r_status!='0' and r_status!='1'  ");
			}
			
			if(indentapprove.equals("1")){
				buffer.append("and r_status!='0'  ");
			}
			String sql = buffer.toString();
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public ArrayList<Product> getParentProductTransferData(String searchText, String fromdate, String todate,
			String location, String filter_status, Pagination pagination, boolean flag, String indentapprove, String filter_bydate,int showindentreq) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		IndentDAO indentDAO = new JDBCIndentDAO(connection);
		try {

			StringBuffer buffer = new StringBuffer();
			todate = todate+" "+"59:59:59";
			buffer.append("select id, request_date, issued_date, from_location, to_location,time,r_status,req_or_trans, admin_notes, head_notes, check_availability_date, admin_aprove_date, head_aprove_date,expected_date,userid,admin_approve_userid,indentreq,warehouse_id from inventory_parent_transfer_log ");
			buffer.append("where deleted=0   ");
			
			if(filter_bydate.equals("0")){
				buffer.append("and request_date between '" + fromdate + "' and '" + todate + "' ");
			}else if(filter_bydate.equals("1")){
				buffer.append("and admin_aprove_date between '" + fromdate + "' and '" + todate + "' ");
			}else if(filter_bydate.equals("2")){
				buffer.append("and (deliver_Date between '" + fromdate + "' and '" + todate + "' or issued_date between '" + fromdate + "' and '" + todate + "') ");
			}
			
			if (searchText != null) {
				
				buffer.append("and id ='" + searchText + "'  ");
			}
			
			if (!filter_status.equals("10")) {
				if (filter_status.equals("8")) {
					buffer.append("and req_or_trans=1 ");
				} else {
					buffer.append("and r_status=" + filter_status + " ");
				}
			}
			
			if(showindentreq>0){
				buffer.append("and (from_location in ("+location+") or to_location in ("+location+")) ");
			}else
			if (!location.equals("0")) {
				buffer.append("and (warehouse_id in (" + location + ") or from_location in ("+location+") or to_location in ("+location+")) ");
			}
			if(showindentreq>0){
				buffer.append("and r_status!='1'  ");
			}else if(flag){
				buffer.append("and r_status!='0' and r_status!='1'  ");
			}
			
			if(indentapprove.equals("1")){
				buffer.append("and r_status!='0'  ");
			}
			
			//buffer.append("order by request_date,time desc");
			buffer.append("order by FIELD(r_status, '0', '1','5', '3','7', '6','4', '2','8','9') asc,request_date desc,time desc ");
			/*buffer.append("order by FIELD(r_status, '9', '8','2', '4','6', '7','3', '5','1','0'),request_date,time desc ");*/
			
			
			String sql1 = pagination.getSQLQuery(buffer.toString());
			PreparedStatement preparedStatement = connection.prepareStatement(sql1);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setParentid("" + rs.getInt(1));
				String request_date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				String issued_date = DateTimeUtils.getCommencingDate1(rs.getString(3));
				String time = rs.getString(6);
				product.setRequest_date(request_date + " " + time);
				product.setIssued_date(issued_date + " " + time);
				// product.setRequest_date(rs.getString(2));
				// product.setIssued_date(rs.getString(3));
				String fromlocation = pharmacyLocationNameByID(rs.getString(4));
				String tolocation = pharmacyLocationNameByID(rs.getString(5));
				product.setStatus(rs.getString(7));
				product.setFrom_location(fromlocation);
				product.setTo_location(tolocation);
				product.setReq_or_transfer(rs.getString(8));
				String adminnotes = "", headnotes = "", check_availabity_date = "", admin_aprove_date = "",
						head_aprove_date = "";
				if (rs.getString(9) != null) {
					adminnotes = rs.getString(9);
				}
				if (rs.getString(10) != null) {
					headnotes = rs.getString(10);
				}
				if (rs.getString(11) != null) {
					check_availabity_date = rs.getString(11);
				}
				if (rs.getString(12) != null) {
					admin_aprove_date = rs.getString(12);
					String[] data = admin_aprove_date.split(" ");
					String date = DateTimeUtils.getCommencingDate1(data[0]);
					admin_aprove_date = date + " " + data[1];
				}
				if (rs.getString(13) != null) {
					head_aprove_date = rs.getString(13);
					String[] data = head_aprove_date.split(" ");
					String date = DateTimeUtils.getCommencingDate1(data[0]);
					head_aprove_date = date + " " + data[1];
				}
				product.setAdmin_notes(adminnotes);
				product.setHead_notes(headnotes);
				product.setCheck_availability_date(check_availabity_date);
				product.setAdmin_aprove_date(admin_aprove_date);
				product.setHead_aprove_date(head_aprove_date);
				product.setExpectedDate(rs.getString(14));

				Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(rs.getString(15));

				if (priscription.getFullname() != null) {
					product.setUserid(priscription.getFullname());
				} else {
					product.setUserid(rs.getString(15));
				}

				product.setAdmin_approve_userid(DateTimeUtils.isNull(rs.getString(16)));
				product.setIndent(rs.getInt(17));
				
				//10Jan 2018 Akash set requested department
				String name = indentDAO.getWarehouseNameFromId(rs.getString(18));
				product.setWarehouse_id(rs.getString(18));
				product.setWarehouse_name(name);
				
				boolean flag2 = false;
				if(!location.equals("0")){
					flag2 = indentDAO.checkLocationInWarehouseid(location);
				}
				int deilverproduct = 0;
				if(flag2==true || location.equals("0")){
					deilverproduct = 1;
				}
				product.setDeilverproduct(deilverproduct);
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public String pharmacyLocationNameByID(String id) {
		String name = "";
		try {
			String query = "select name from apm_condition where id=" + id;
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				name = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	public int savetempProduct(Product product) {

		int result = 0;
		try {
			//Akash 03 May 2018
			String added_date="";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			added_date = dateFormat.format(cal.getTime());
			added_date = DateTimeUtils.getCommencingDate1(added_date);
			String sql = "insert into inventory_product (supplierid,prodname,categoryid,location,procurement,mrp,purchaseprice,catalogueid,added_date,is_po_prod,pack) values (?,?,?,?,?,?,?,?,?,?,?) ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getVendor());
			ps.setString(2, product.getProduct_name());
			ps.setString(3, product.getCategory_id());
			ps.setString(4, product.getLocation());
			ps.setInt(5, 1);
			ps.setString(6, product.getMrp());
			ps.setString(7, product.getPurchase_price());
			ps.setString(8, product.getCatalogueid());
			ps.setString(9, added_date);
			ps.setString(10, ""+product.getIs_po_prod());
			ps.setString(11, product.getPack());
			result = ps.executeUpdate();

			if (result > 0) {

				ResultSet rs = ps.getGeneratedKeys();
				while (rs.next()) {

					result = rs.getInt(1);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int geReqTranTotalProductCountfrCatalogue(ArrayList<String> arrayList2, String fromlocation) {
		int result = 0;
		String sql = "";
		try {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("select count(*) from inventory_product where location !=" + fromlocation + " and (");
			int i = 0;
			int j = arrayList2.size();
			for (String string : arrayList2) {
				if (j - 1 == i) {
					stringBuilder.append("prodname like ('" + string + "%')) ");
				} else {
					stringBuilder.append("prodname like ('" + string + "%') or ");
				}
				i++;
			}
			sql = stringBuilder.toString();
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public int updateParentProductStatus(String parentid) {
		int result = 0;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String currentdate = dateFormat.format(calendar.getTime());
			String sql = "update inventory_parent_transfer_log set r_status='1',check_availability_date='" + currentdate
					+ "' where id=" + parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getChildRequestedTempData(String parentid) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			/*String sql = "select id, parentid, old_prodid, new_prodid, qty,location,transfer_date from inventory_request_temp_log where parentid="
					+ parentid + "";*/
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_request_temp_log.id, parentid, old_prodid, new_prodid, qty,inventory_request_temp_log.location, ");
			buffer.append("transfer_date,product_name from inventory_request_temp_log ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_request_temp_log.catlogueid ");
			buffer.append("where parentid='"+parentid+"' ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total_amount = 0;
			double totolmrp = 0;

			while (rs.next()) {
				Product product = new Product();
				product.setChildid("" + rs.getInt(1));
				product.setParentid(rs.getString(2));
				product.setProduct_id(rs.getString(3));
				Product product2 = getProduct(rs.getString(3));
				product.setHsnno(product2.getHsnno());
				product.setProduct_name(product2.getProduct_name());
				product.setBatch_no(product2.getBatch_no());
				String expirydate="";
				if(product2.getExpiry_date()!=null){
					if(!product2.getExpiry_date().equals("")){
						DateTimeUtils.getCommencingDate1(product2.getExpiry_date());
					}
				}
				product.setExpiry_date(expirydate);
				product.setStock(rs.getString(5));
				/*
				 * product.setSale_price(product2.getSale_price());
				 * product.setMrp(product2.getMrp()); double amountno =
				 * Integer.parseInt(rs.getString(5)) *
				 * Double.parseDouble(product.getSale_price()); double amountmrp
				 * = Integer.parseInt(rs.getString(5)) *
				 * Double.parseDouble(product.getMrp());
				 */
				if (product2.getPack() == null) {
					product2.setPack("1");
				}
				if (product2.getPack().equals("")) {
					product2.setPack("1");
				}
				double amt = Double.parseDouble(product2.getPurchase_price()) / Integer.parseInt(product2.getPack());
				double mrpamt = Double.parseDouble(product2.getMrp()) / Integer.parseInt(product2.getPack());
				double unit1 = Math.round(amt * 100.0) / 100.0;
				double unit2 = Math.round(mrpamt * 100.0) / 100.0;
				product.setSale_price("" + unit1);
				product.setMrp("" + unit2);
				product.setUnit("" + unit1);
				double amountno = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getSale_price());
				double amountmrp = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getMrp());
				total_amount = total_amount + amountno;
				totolmrp = totolmrp + amountmrp;
				product.setTotal_amount(Math.round(total_amount * 100.0) / 100.0);
				product.setTotolmrp(Math.round(totolmrp * 100.0) / 100.0);
				product.setAmountno(Math.round(amountno * 100.0) / 100.0);
				product.setAmountmrp(Math.round(amountmrp * 100.0) / 100.0);
				product.setOld_location(rs.getString(6));
				product.setFrom_location(pharmacyLocationNameByID(product2.getLocation()));

				String transfer_date = rs.getString(7);
				if (transfer_date != null) {
					if (!transfer_date.equals("")) {
						String[] data = transfer_date.split(" ");
						transfer_date = DateTimeUtils.getCommencingDate1(data[0]);
						transfer_date = transfer_date + " " + data[1];
					}
				} else {
					transfer_date = "";
				}
				product.setTransfer_date(transfer_date);
				product.setProduct_name(rs.getString(8));
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public Product getChildRequestTempCal(String parentid) {
		Product product = new Product();
		try {
			String sql = "select id, parentid, old_prodid, new_prodid, qty from inventory_request_temp_log where parentid="
					+ parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			double total_amount = 0;
			double totolmrp = 0;

			while (rs.next()) {
				product.setChildid("" + rs.getInt(1));
				product.setParentid(rs.getString(2));
				Product product2 = getProduct(rs.getString(3));
				product.setHsnno(product2.getHsnno());
				product.setProduct_name(product2.getProduct_name());
				product.setBatch_no(product2.getBatch_no());
				product.setExpiry_date(product2.getExpiry_date());
				product.setStock(rs.getString(5));
				product.setSale_price(product2.getSale_price());
				product.setMrp(product2.getMrp());
				double amountno = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getSale_price());
				double amountmrp = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getMrp());
				total_amount = total_amount + amountno;
				totolmrp = totolmrp + amountmrp;
				product.setTotal_amount(Math.round(total_amount * 100.0) / 100.0);
				product.setTotolmrp(Math.round(totolmrp * 100.0) / 100.0);
				product.setAmountno(Math.round(amountno * 100.0) / 100.0);
				product.setAmountmrp(Math.round(amountmrp * 100.0) / 100.0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public int addToProductDeletedLog(String userid, String date, String location, int i, String productid) {

		int result = 0;
		try {
			String sql = "insert into inventory_product_log(productid, userid, location, notes, date, deleted) values "
					+ "(?,?,?,?,?,?) ";

			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, productid);
			ps.setString(2, userid);
			ps.setString(3, location);
			ps.setString(4, "");
			ps.setString(5, date);
			ps.setInt(6, 1);

			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int addToProductUpdatedLog(String userid, String date, String location, int i, String productid,
			String comment, String catalogueid, String previousqty, String currentqty, Product product2, Product product, String datetime) {

		int result = 0;
		try {
			/*String sql = "insert into inventory_product_log(productid, userid, location, notes, date, updated,comment,catalogueid,previousqty,currentqty) values "
					+ "(?,?,?,?,?,?,?,?,?,?) ";*/
			StringBuffer buffer = new StringBuffer();
			buffer.append("insert into inventory_product_log(productid, userid, location, notes, date, updated,comment ");
			buffer.append(",catalogueid,previousqty,currentqty,currentpack,previouspack,currentgst,previousgst, ");
			buffer.append("currentmrp,previousmrp,currentsaleprice,previoussaleprice,currentexpdate,previousexpdate,currentpurprice,previouspurprice,datetime,previousbarcode,currentbarcode) values ");
			buffer.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ps.setString(1, productid);
			ps.setString(2, userid);
			ps.setString(3, location);
			ps.setString(4, "");
			ps.setString(5, date);
			ps.setInt(6, 1);
			ps.setString(7, comment);
			ps.setString(8, catalogueid);
			ps.setString(9, previousqty);
			ps.setString(10, currentqty);
			
			ps.setString(11, product.getPack());
			ps.setString(12, product2.getPack());
			ps.setString(13, product.getVat());
			ps.setString(14, product2.getVat());
			ps.setString(15, product.getMrp());
			ps.setString(16, product2.getMrp());
			ps.setString(17, product.getSale_price());
			ps.setString(18, product2.getSale_price());
			ps.setString(19, product.getExpiry_date());
			ps.setString(20, product2.getExpiry_date());
			
			ps.setString(21, product.getPurchase_price());
			ps.setString(22, product2.getPurchase_price());
			ps.setString(23, datetime);
			
			ps.setString(24, product2.getBarcode());
			ps.setString(25, product.getBarcode());
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getDelUpdateReportList(String fromdate, String todate, String location, String filter, Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);

			StringBuffer buffer = new StringBuffer();
			buffer.append(
					"select id, productid, userid, location, notes, date, deleted, updated,comment,pharmacy_billno,catalogueid,currentqty, previousqty from inventory_product_log where ");
			buffer.append("date between '" + fromdate + "' and '" + todate + "' ");
			if (!location.equals("0") && !location.equals("1")) {
				buffer.append("and location='" + location + "' ");
			}
			buffer.append("and isindent=" + filter + " ");

			buffer.append("order by id desc ");
			String sql = pagination.getSQLQuery(buffer.toString());
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_id(rs.getString(2));
				String catalogueid = rs.getString(11);
				Product master = null;
				if (!catalogueid.equals("0")) {
					master = getProductCatalogueDetails(catalogueid);
				} else {
					master = getProduct(rs.getString(2));
				}

				product.setUserid(rs.getString(3));
				product.setLocation(pharmacyDAO.getLocationName(rs.getString(4)));
				product.setNotes(rs.getString(5));
				product.setDate(DateTimeUtils.getCommencingDate2(rs.getString(6)));
				product.setDeleted(rs.getInt(7));
				product.setProduct_name(master.getProduct_name());
				product.setUpdated(rs.getInt(8));
				product.setComment(rs.getString(9));
				String billno = "";
				if (rs.getString(10) == null) {
					billno = "0";
				} else {
					billno = rs.getString(10);
				}
				product.setBillno(billno);
				
				if(filter.equals("2")){
					
					CompleteAppointment appointment= pharmacyDAO.getBillDetails(Integer.parseInt(product.getBillno()));
					if(!appointment.getPclientid().equals("0")){
						
						Priscription priscription= pharmacyDAO.getPharmacyPatient(appointment.getPclientid());
						product.setFullname(priscription.getFullname());
						
					} else {
						ClientDAO clientDAO= new JDBCClientDAO(connection);
						Client client=clientDAO.getClientDetails(appointment.getClientId());
						String fullname =client.getTitle()+" "+client.getFirstName()+" "+client.getLastName();
						product.setFullname(fullname);
					}
					product.setTotal(DateTimeUtils.changeFormat(appointment.getTotal()));
				}
				//12,13
				product.setQty(""+rs.getInt(12));
				product.setQuantity(""+rs.getInt(13));
				list.add(product);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	// new methdod for request and transfer @Akash

	public int requestProductTemporarySave(int prodid, String location, String qty, String parentid, String prod_name,
			String transfer_date, int count5) {
		int result = 0;
		try {
			String sql = "insert into inventory_request_temp_log (parentid, old_prodid, new_prodid, qty,location,prod_name,transfer_date,trans_count) values (?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, parentid);
			preparedStatement.setString(2, "" + prodid);
			preparedStatement.setString(3, "0");
			preparedStatement.setString(4, qty);
			preparedStatement.setString(5, location);
			preparedStatement.setString(6, prod_name);
			preparedStatement.setString(7, transfer_date);
			preparedStatement.setString(8, "" + count5);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int requestProductTemporarySave(int prodid, String location, String qty, String parentid) {
		int result = 0;
		try {
			String sql = "insert into inventory_request_temp_log (parentid, old_prodid, new_prodid, qty,location,prod_name) values (?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, parentid);
			preparedStatement.setString(2, "" + prodid);
			preparedStatement.setString(3, "0");
			preparedStatement.setString(4, qty);
			preparedStatement.setString(5, location);

			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<String> getChildProductName(String parentid) {
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			String sql = "select prod_name from inventory_transfer_log where parentid='" + parentid
					+ "' and status='0'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String prod_name = rs.getString(1);
				arrayList.add(prod_name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int getReqQtyFrmTemp(String prod_name, String parentid) {
		int qty = 0;
		try {
			// String sql = "select sum(qty) from inventory_request_temp_log
			// where parentid="+parentid+" and prod_name='"+prod_name+"' group
			// by prod_name;";
			String sql = "select sum(qty) from inventory_request_temp_log where parentid=" + parentid
					+ " and prod_name like('" + prod_name + "') group by prod_name;";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				qty = Integer.parseInt(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return qty;
	}

	public Product getReqQtyFrmAct(String parentid, String prod_name) {
		Product product = new Product();
		try {
			// String sql ="select id,qty from inventory_transfer_log where
			// prod_name='"+prod_name+"' and parentid="+parentid+"";
			String sql = "select id,qty from inventory_transfer_log where prod_name like ('" + prod_name
					+ "') and parentid=" + parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				product.setId(rs.getInt(1));
				product.setQty(rs.getString(2));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public int updateChildTransferStatus(int id, int status) {
		int result = 0;
		try {
			String transfer_date = "";
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			transfer_date = dateFormat.format(cal.getTime());

			String sql = "";
			if (status != 0) {
				sql = "update inventory_transfer_log set status='" + status + "',transfer_date='" + transfer_date
						+ "' where id=" + id + "";
			} else {
				sql = "update inventory_transfer_log set status='" + status + "' where id=" + id + "";
			}

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getCountTransferProd(String parentid) {
		int result = 0;
		try {
			String sql = "select count(*) from inventory_transfer_log where parentid='" + parentid + "' and cancel_req='0'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getCountTransferedProd(String parentid) {
		int result = 0;
		try {
			String sql = "select count(*) from inventory_transfer_log where parentid='" + parentid + "' and status!=0 and cancel_req='0'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateChildTempTransferStatus(String prod_name, String parentid, int status) {
		int result = 0;
		try {
			// String sql = "update inventory_request_temp_log set
			// status="+status+" where parentid='"+parentid+"' and
			// prod_name='"+prod_name+"'";
			String sql = "update inventory_request_temp_log set status=" + status + " where parentid='" + parentid
					+ "' and prod_name like('" + prod_name + "')";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// old method replace @Akash

	public Product getParentTransferData(String parentid) {
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		Product product = new Product();
		try {
			// id, request_date, issued_date, from_location, to_location, time,
			// r_status, req_or_trans, admin_status, admin_notes, head_notes,
			// check_availability_date, admin_aprove_date, head_aprove_date,
			// userid, admin_approve_userid
			String sql = "select id, request_date, issued_date, from_location, to_location,time,userid,admin_notes,admin_aprove_date,admin_approve_userid,indentreq,expected_date,check_avail_note,check_availability_date,check_avail_userid,remark,handover_to,trans_indent_no,warehouse_id,procurmentid,req_or_trans from inventory_parent_transfer_log where id="
					+ parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				product.setParentid("" + rs.getInt(1));
				String request_date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				String issued_date = "";
				if (rs.getString(3) != null) {
					issued_date = DateTimeUtils.getCommencingDate1(rs.getString(3));
				}
				String time = rs.getString(6);
				product.setRequest_date(request_date + " " + time);
				product.setIssued_date(issued_date);
				String fromlocation = pharmacyLocationNameByID(rs.getString(4));
				String tolocation = pharmacyLocationNameByID(rs.getString(5));
				product.setLocation(rs.getString(4));
				product.setFrom_location(fromlocation);
				product.setTo_location(tolocation);

				product.setAdmin_notes(rs.getString(8));
				product.setAdmin_aprove_date(rs.getString(9));

				product.setIndent(rs.getInt(11));
				product.setExpectedDate(rs.getString(12));
				product.setCheck_availability_note(rs.getString(13));
				product.setCheck_availability_date(rs.getString(14));

				String handover = "";
				if (rs.getString(17) == null) {
					handover = "0";
				} else if (rs.getString(17).equals("")) {
					handover = "0";
				} else {
					handover = rs.getString(17);
				}
				product.setHandover_to(handover);
				product.setComment(rs.getString(16));
				product.setTime(time);
				Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(rs.getString(7));
				Priscription priscription1 = pharmacyDAO.getPharacyUsrLInfo(rs.getString(10));
				Priscription priscription2 = pharmacyDAO.getPharacyUsrLInfo(rs.getString(15));
				if (priscription.getFullname() != null) {
					product.setUserid(priscription.getFullname());
				} else {
					product.setUserid(rs.getString(7));
				}

				if (priscription1.getFullname() != null) {
					product.setAdmin_approve_userid(priscription1.getFullname());
				} else {
					product.setAdmin_approve_userid(rs.getString(10));
				}

				if (priscription2.getFullname() != null) {
					product.setCheck_avail_userid(priscription2.getFullname());
				} else {
					product.setCheck_avail_userid(rs.getString(15));
				}
				product.setTransfer_indentno(rs.getString(18));
				product.setWarehouse_id(rs.getString(19));
				product.setReq_userid(rs.getString(7));
				product.setProcurementid(rs.getString(20));
				product.setReq_or_transfer(""+rs.getInt(21));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public ArrayList<Product> getChildTranfserData(String parentid) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			/*String sql = "select id, parentid, old_prodid, new_prodid, qty,comment,avail_qty,status,transfer_date,catlogueid,cancel_req, cancel_req_note from inventory_transfer_log where parentid="
					+ parentid + "";*/
			String sql = "select id, parentid, old_prodid, new_prodid, qty,comment,avail_qty,status,transfer_date,catlogueid,cancel_req, cancel_req_note,requested_qty from inventory_transfer_log where parentid="
				     + parentid + "";
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_transfer_log.id, parentid, old_prodid, new_prodid, qty,comment,avail_qty,status, ");
			buffer.append("transfer_date,catlogueid,cancel_req, cancel_req_note,requested_qty,product_name ");
			buffer.append("from inventory_transfer_log ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_transfer_log.catlogueid ");
			buffer.append("where parentid='"+parentid+"' ");
			//PreparedStatement preparedStatement = connection.prepareStatement(sql);
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total_amount = 0;
			double totolmrp = 0;

			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setChildid("" + rs.getInt(1));
				product.setParentid(rs.getString(2));
				Product product2 = getProduct(rs.getString(3));
				product.setHsnno(product2.getHsnno());
				product.setProduct_name(product2.getProduct_name());
				product.setBatch_no(product2.getBatch_no());
				String expirydate="";
				if(product2.getExpiry_date()!=null){
					if(!product2.getExpiry_date().equals("")){
						expirydate = DateTimeUtils.getCommencingDate1(product2.getExpiry_date());
					}
				}
				product.setExpiry_date(expirydate);
				product.setStock(rs.getString(5));
				String comment = "";
				if (rs.getString(6) != null) {
					comment = rs.getString(6);
				}
				product.setComment(comment);
				product.setSale_price(product2.getSale_price());
				product.setMrp(product2.getMrp());
				if (product2.getPack() == null) {
					product2.setPack("1");
				}
				if (product2.getPack().equals("")) {
					product2.setPack("1");
				}
				double amt = Double.parseDouble(product2.getPurchase_price()) / Integer.parseInt(product2.getPack());

				double amountno = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getSale_price());
				double amountmrp = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getMrp());
				total_amount = total_amount + amountno;
				totolmrp = totolmrp + amountmrp;
				product.setTotal_amount(total_amount);
				product.setTotolmrp(Math.round(totolmrp * 100.0) / 100.0);
				product.setAmountno(Math.round(amountno * 100.0) / 100.0);
				product.setAmountmrp(Math.round(amountmrp * 100.0) / 100.0);
				product.setPurchase_price(DateTimeUtils.changeFormat(amt));
				product.setProduct_id(rs.getString(3));
				double unit1 = Math.round(amt * 100.0) / 100.0;
				product.setUnit("" + unit1);
				Product product3 = getParentTransferData(parentid);
				product.setIssued_date(product3.getIssued_date()+" "+product3.getTime());
				product.setFrom_location(product3.getFrom_location());
				product.setExpectedDate(product3.getExpectedDate());
				int avail_stock = getAllAvailableStock(product2.getProduct_name(), product3.getLocation());
				product.setAvail_stock(avail_stock);
				int a_qty = 0;
				if (rs.getString(7) != null) {
					if (!rs.getString(7).equals("")) {
						a_qty = Integer.parseInt(rs.getString(7));
					}
				}
				product.setAvail_qty(a_qty);

				int total_recived_qty = getReqQtyFrmTemp(product2.getProduct_name(), parentid);

				if (total_recived_qty == Integer.parseInt(rs.getString(5))) {
					product.setStatus("1");
				} else if (total_recived_qty > Integer.parseInt(rs.getString(5))) {
					product.setStatus("1");
				} else if (total_recived_qty > 0) {
					product.setStatus("1");
				} else {
					product.setStatus("0");
				}
				String transfer_date = rs.getString(9);
				if (transfer_date != null) {
					if (!transfer_date.equals("")) {
						String[] data = transfer_date.split(" ");
						transfer_date = DateTimeUtils.getCommencingDate1(data[0]);
						transfer_date = transfer_date + " " + data[1];
					}else{
						transfer_date = product.getIssued_date();
					}
				} else {
					transfer_date = product.getIssued_date();
				}
				product.setTransfer_date(transfer_date);
				String catlogueid = rs.getString(10);
				String totaltransferqt = getIndentTransferQuantity(parentid,catlogueid);
				product.setTotaltransferqt(totaltransferqt);
				product.setCancel_req(rs.getString(11));
				product.setCancel_req_note(rs.getString(12));
				product.setRequested_qty(rs.getString(13));
				product.setProduct_name(rs.getString(14));
				product.setCatalogueid(catlogueid);
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public String getIndentTransferQuantity(String parentid, String catlogueid) {
		String total ="";
		try {
			String sql ="select sum(qty) from inventory_request_temp_log where parentid='"+parentid+"' and catlogueid='"+catlogueid+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				total = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}

	public Product getChildTransferCal(String parentid) {
		Product product = new Product();
		try {
			String sql = "select id, parentid, old_prodid, new_prodid, qty from inventory_transfer_log where parentid="
					+ parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			double total_amount = 0;
			double totolmrp = 0;

			while (rs.next()) {
				product.setChildid("" + rs.getInt(1));
				product.setParentid(rs.getString(2));
				Product product2 = getProduct(rs.getString(3));
				product.setHsnno(product2.getHsnno());
				product.setProduct_name(product2.getProduct_name());
				product.setBatch_no(product2.getBatch_no());
				product.setExpiry_date(product2.getExpiry_date());
				product.setStock(rs.getString(5));
				product.setSale_price(product2.getSale_price());
				product.setMrp(product2.getMrp());
				double amountno = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getSale_price());
				double amountmrp = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getMrp());
				total_amount = total_amount + amountno;
				totolmrp = totolmrp + amountmrp;
				product.setTotal_amount(Math.round(total_amount * 100.0) / 100.0);
				product.setTotolmrp(Math.round(totolmrp * 100.0) / 100.0);
				product.setAmountno(Math.round(amountno * 100.0) / 100.0);
				product.setAmountmrp(Math.round(amountmrp * 100.0) / 100.0);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public ArrayList<Product> getReqTransAllProdfrCatalogue(Pagination pagination, ArrayList<String> arrayList2,
			String fromlocation) {
		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		String sql = "";

		try {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder
					.append("select inventory_product.id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,batch_no,vat,genericname,cell,pack,mfg,hsnno,location from inventory_product where location !="
							+ fromlocation + " and (");

			int i = 0;
			int j = arrayList2.size();
			for (String string : arrayList2) {
				if (j - 1 == i) {
					stringBuilder.append("prodname like ('" + string + "%'))");
				} else {
					stringBuilder.append("prodname like ('" + string + "%') or ");
				}
				i++;
			}
			if (pagination != null) {
				// sql=pagination.getSQLQuery(sql);
				sql = pagination.getSQLQuery(stringBuilder.toString());
			} else {
				sql = stringBuilder.toString();
			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(DateTimeUtils.changeFormat(rs.getDouble(7)));
				product.setPurchase_price(DateTimeUtils.changeFormat(rs.getDouble(8)));
				product.setSale_price(DateTimeUtils.changeFormat(rs.getDouble(9)));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(DateTimeUtils.getCommencingDate1(rs.getString(17)));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));
				product.setLastmodified(rs.getString(21));
				product.setBatch_no(rs.getString(22));
				product.setVat(rs.getString(23));
				product.setGenericname(rs.getString(24));
				product.setShelf(rs.getString(25));
				String pack = rs.getString(26);
				product.setPack(pack);
				product.setMfg(rs.getString(27));
				product.setHsnno(rs.getString(28));
				product.setLocation(pharmacyDAO.getPharmacyLocation(rs.getString(29)));

				if (product.getLastmodified() != null) {

					String purchase_date = DateTimeUtils.getCommencingDate1(product.getLastmodified());
					product.setLastmodified(purchase_date);
				}

				if (product.getPurchase_price() == null) {
					product.setPurchase_price("0");
				}
				if (product.getPurchase_price().equals("")) {
					product.setPurchase_price("0");
				}
				if (product.getVat() == null) {
					product.setVat("0");
				}
				if (product.getVat().equals("")) {
					product.setVat("0");
				}
				/*
				 * double vatrate= Double.parseDouble(product.getVat()); double
				 * amt= Double.parseDouble(product.getPurchase_price()) *
				 * vatrate/100;
				 * 
				 * double total= Double.parseDouble(product.getPurchase_price())
				 * + amt;
				 * product.setPurchase_price(DateTimeUtils.changeFormat(total));
				 */
				Calendar expireCalendar = Calendar.getInstance();
				String expirydate = rs.getString(17);
				if (expirydate != null) {

					if (!expirydate.equals("")) {
						Date date = format.parse(expirydate);
						expireCalendar.setTime(date);

						long diffdays = daysBetween(expireCalendar.getTime(), calendar.getTime());

						if (diffdays <= 10) {

							product.setStatus("expired");
						} else {
							product.setStatus("notexpired");
						}
					}
				}

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				if (brand.getName() == null) {
					product.setBrand(product.getMfg());
				} else if (brand.getName().equals("")) {
					product.setBrand(product.getMfg());
				}

				list.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int updateParentProductStatus(String parentid, String userid, int all_med_status, int r_status) {
		int result = 0;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String currentdate = dateFormat.format(calendar.getTime());
			String sql = "update inventory_parent_transfer_log set r_status='" + r_status
					+ "',check_availability_date='" + currentdate + "', check_avail_userid='" + userid
					+ "', all_med_status=" + all_med_status + " where id=" + parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getMedinceListofType(String subcategory, String category) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select id,product_name from inventory_catalogue where categoryid='" + category + "' ");
			buffer.append("and subcategoryid='" + subcategory + "' ");
			buffer.append("and product_name is NOT NULL group by product_name asc ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int saveRequestIndent(Product product) {

		int result = 0;
		try {
			String sql = "insert into inventory_parent_transfer_log (request_date,from_location,expected_date,userid,time,indentreq,warehouse_id) values (?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getDate());
			ps.setString(2, product.getLocation());
			ps.setString(3, product.getExpectedDate());
			ps.setString(4, product.getUserid());
			ps.setString(5, product.getTime());
			ps.setInt(6, product.getIndent());
			ps.setString(7, product.getWarehouse_id());
			result = ps.executeUpdate();
			if (result > 0) {

				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					result = rs.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int saveChildRequestIndent(Product master, int parentid) {

		int result = 0;
		try {
			String sql = "insert into inventory_transfer_log (parentid,old_prodid,qty,comment,avail_qty,prod_name,catlogueid,requested_qty) values (?,?,?,?,?,?,?,?)";
			//String sql = "insert into inventory_request_log (parentid,old_prodid,qty,comment,avail_qty,prod_name,catlogueid) values (?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			//Product product = getProduct(master.getProduct_id());
			Product product=getProductCatalogueDetails(master.getCatalogueid());
			ps.setInt(1, parentid);
			ps.setString(2, master.getProduct_id());
			ps.setString(3, master.getQty());
			ps.setString(4, master.getComment());
			ps.setString(5, "" + master.getAvail_qty());
			ps.setString(6, product.getProduct_name());
			ps.setString(7, master.getCatalogueid());
			ps.setString(8, master.getQty());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateRequestIndentChild(Product product) {

		int result = 0;
		try {

			String sql = "update inventory_transfer_log set qty=?,comment=? where id=" + product.getId() + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getQty());
			ps.setString(2, product.getComment());
			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int deleteIndentRequest(String id) {

		int result = 0;
		try {
			String sql = "delete from inventory_transfer_log where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int getTotalIdentCount() {

		int result = 0;
		try {
			String sql = "select count(*) from inventory_parent_transfer_log where indentreq>0";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public int deleteIndentParent(String id) {

		int result = 0;
		try {
			String sql = "delete from inventory_parent_transfer_log where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();

			String sql1 = "delete from inventory_transfer_log where parentid=" + id + "";
			PreparedStatement ps1 = connection.prepareStatement(sql1);
			result = ps1.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int getAvailableStock(String product_id, String product_name) {
		int qty = 0;
		try {
			String sql = "select sum(stock) from inventory_product where stock>0 and location=32 and prodname like ('"
					+ product_name + "')";
			// String sql ="select sum(stock) from inventory_product where
			// stock>0 and location=1 and prodname ='"+product_name+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				qty = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qty;
	}

	public ArrayList<Product> getBatchWiseProductList(int req_stock, Product prod) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select id, prodname, stock, expiry_date, batch_no, location, hsnno from inventory_product where location=32 and stock>0 and prodname like ('"
					+ prod.getProduct_name() + "')";
			// String sql ="select id, prodname, stock, expiry_date, batch_no,
			// location, hsnno from inventory_product where location=1 and
			// stock>0 and prodname ='"+prod.getProduct_name()+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int avail_stock = Integer.parseInt(rs.getString(3));
				Product product = new Product();
				product.setAvail_stock(avail_stock);
				product.setProduct_id("" + rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setStock(rs.getString(3));
				product.setExpiry_date(rs.getString(4));
				product.setBatch_no(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setHsnno(rs.getString(7));
				String fromlocation = pharmacyLocationNameByID(rs.getString(6));
				product.setFromlocation(fromlocation);
				product.setReqqty("" + req_stock);
				if (avail_stock >= req_stock) {
					arrayList.add(product);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Product> getWithoutBatchWiseProductList(Product prod) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select id, prodname, stock, expiry_date, batch_no, location, hsnno from inventory_product where location=32 and stock>0 and prodname like ('"
					+ prod.getProduct_name() + "')";
			// String sql ="select id, prodname, stock, expiry_date, batch_no,
			// location, hsnno from inventory_product where location=1 and
			// stock>0 and prodname ='"+prod.getProduct_name()+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int avail_stock = Integer.parseInt(rs.getString(1));
				Product product = new Product();
				product.setAvail_stock(avail_stock);
				product.setProduct_id("" + rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setStock(rs.getString(3));
				product.setExpiry_date(rs.getString(4));
				product.setBatch_no(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setHsnno(rs.getString(7));
				product.setReqqty(prod.getStock());
				String fromlocation = pharmacyLocationNameByID(rs.getString(6));
				product.setFromlocation(fromlocation);
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int updateTempPoStatus(int id) {
		int result = 0;
		try {
			String sql = "update temp_po set status='1' where id=" + id + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateCheckAvailabilityNotes(String parentid, String comment, String transfer_date) {
		int result = 0;
		try {
			if (comment == null)
				comment = "";
			String sql = "update inventory_parent_transfer_log set check_avail_note='" + comment + "',deliver_Date='"+transfer_date+"' where id="
					+ parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Product getChildTransferTempCal(String parentid) {
		Product product = new Product();
		try {
			String sql = "select id, parentid, old_prodid, new_prodid, qty from inventory_request_temp_log where parentid="
					+ parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			double total_amount = 0;
			double totolmrp = 0;

			while (rs.next()) {
				product.setChildid("" + rs.getInt(1));
				product.setParentid(rs.getString(2));
				Product product2 = getProduct(rs.getString(3));
				product.setHsnno(product2.getHsnno());
				product.setProduct_name(product2.getProduct_name());
				product.setBatch_no(product2.getBatch_no());
				product.setExpiry_date(product2.getExpiry_date());
				product.setStock(rs.getString(5));
				// product.setSale_price(product2.getSale_price());
				// product.setMrp(product2.getMrp());
				if (product2.getPack() == null) {
					product2.setPack("1");
				}
				if (product2.getPack().equals("")) {
					product2.setPack("1");
				}
				double amt = Double.parseDouble(product2.getPurchase_price()) / Integer.parseInt(product2.getPack());
				double mrpamt = Double.parseDouble(product2.getMrp()) / Integer.parseInt(product2.getPack());
				double unit1 = Math.round(amt * 100.0) / 100.0;
				double unit2 = Math.round(mrpamt * 100.0) / 100.0;
				product.setSale_price("" + unit1);
				product.setMrp("" + unit2);
				product.setUnit("" + unit1);
				double amountno = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getSale_price());
				double amountmrp = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getMrp());
				total_amount = total_amount + amountno;
				totolmrp = totolmrp + amountmrp;
				product.setTotal_amount(Math.round(total_amount * 100.0) / 100.0);
				product.setTotolmrp(Math.round(totolmrp * 100.0) / 100.0);
				product.setAmountno(amountno);
				product.setAmountmrp(amountmrp);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public ArrayList<Product> getOpeningStockList(String fromDate, String toDate, Pagination pagination, int min,
			int max, String ismonthlyreport, String searchbyprodname,String location_filter, String category_id) {
		PreparedStatement preparedStatement = null;
		ArrayList<Product> list = new ArrayList<Product>();
		StringBuffer buffer = new StringBuffer();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		/*buffer.append(" SELECT distinct prodid, medicine_opening_stock.stock,inventory_product.prodname"
				+ " FROM medicine_opening_stock inner join inventory_product on medicine_opening_stock.prodid=inventory_product.id "
				+ " where commencing between '" + fromDate + "' and '" + toDate + "' and isconsume='0' ");*/
		buffer.append("SELECT prodid, medicine_opening_stock.stock,inventory_product.prodname,medicine_opening_stock.commencing,  ");
		buffer.append("inventory_product.location,inventory_product.categoryid,indentqty ");//7
		buffer.append("FROM medicine_opening_stock ");
		buffer.append(" inner join inventory_product on medicine_opening_stock.prodid=inventory_product.id ");
		buffer.append("where commencing between '" + fromDate + "' and '" + toDate + "' and isconsume='0' ");
		if (searchbyprodname != null) {
			buffer.append("and (prodname like ('%" + searchbyprodname + "%') or batch_no like ('%" + searchbyprodname + "%') )  ");
		}
		if(!location_filter.equals("0")){
			buffer.append("and inventory_product.location='"+location_filter+"' ");
		}
		if(!category_id.equals("0")){
			buffer.append("and inventory_product.categoryid='"+category_id+"' ");
		}
		buffer.append("group by medicine_opening_stock.prodid ");
		/*
		 * if(ismonthlyreport.equals("1")){ buffer.append("group by prodid "); }
		 */
		if (max > 0) {
			buffer.append(" limit " + min + " , " + max + " ");
		} else {
			buffer.append(" limit 0,1000");
		}
		/*
		 * String sql =
		 * "SELECT prodid, stock FROM medicine_opening_stock where commencing between '"
		 * + fromDate + "' and '" + toDate + "' ";
		 */
		double tot = 0;
		int totalqty = 0;
		int opentotalcount = 0;
		int totalopeningval = 0;
		try {

			preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				Product product2 = getProduct(rs.getString(1));
				Product pmaster= getProductCatalogueDetails(product2.getCatalogueid());
				int productid = rs.getInt(1);
				String productname = rs.getString(3);
				product.setCategory(product2.getCategory());
				product.setProduct_name(productname);
				product.setOpeningstock(rs.getString(2));
				/*int sale = getClosingstock(productid, fromDate, toDate);
				int returnstock = getRetuenstock(productid, fromDate, toDate);*/
				/*int saleout = getClosingstock(productid, rs.getString(4), rs.getString(4));*/
				int saleout = getClosingstock(productid, fromDate, toDate);
				int returntosupplierout = getRetuenToSuplierstock(productid, fromDate, toDate);
				int cousmeqtyout = getconsumeqty(productid, fromDate, toDate);
				int adjustout = getadjustmentstockdata(productid,fromDate,toDate);
				int indentout = getIndentOutForNormalOpening(productid,fromDate,toDate,1);
				
				int indentin = getIndentOutForNormalOpening(productid,fromDate,toDate,0);
				int returnstockin = getRetuenstock(productid, fromDate, toDate);
				int adjustin = getadjustmentstockdataIN(productid,fromDate,toDate);
				int purchasein = getPurcahseInStock(productid,fromDate,toDate);
				
				int sale = saleout+returntosupplierout + cousmeqtyout +adjustout+indentout;
				int in = indentin+returnstockin+adjustin+purchasein;
				int closingstock = (rs.getInt(2)+in) - sale;
				product.setSale(Integer.toString(sale));
				product.setReturnstock(Integer.toString(returnstockin));
				product.setClosingstock(Integer.toString(closingstock));
				product.setPurchaseqty(in);
				product.setIndentqty(rs.getInt(7));
				String locationName = pharmacyDAO.getLocationName(rs.getString(5));
				product.setLocationName(locationName);
				product.setPro_code(pmaster.getPro_code());
				if(product2.getSale_price()!=null){
					if(product2.getSale_price().equals("")){
						product2.setSale_price("0");
					}
				}else{
					product2.setSale_price("0");
				}
				double salevalue = sale*Double.parseDouble(product2.getSale_price());
				salevalue =   Math.round(salevalue * 100.0) / 100.0;
				if (product2.getPack() != null) {
					if (product2.equals("0") || product2.equals("")) {
						product2.setPack("1");
					}
				} else {
					product2.setPack("1");
				}
				product.setSalevalue(salevalue);
				double unitprice = Double.parseDouble(product2.getPurchase_price())
						/ Integer.parseInt(product2.getPack());
				unitprice = Math.round(unitprice * 100.0) / 100.0;
				double amt1 = Math.round(closingstock * 100.0) / 100.0  * unitprice ;
				amt1 = Math.round(amt1 * 100.0) / 100.0;
				tot = tot + amt1;
				product.setUnit("" + Math.round(amt1 * 100.0) / 100.0);
				product.setTotal_amount(tot);
				totalqty = totalqty + closingstock;
				product.setTotalqty(totalqty);
				opentotalcount = rs.getInt(2) + opentotalcount;
				product.setOpeningstockvalue(Math.round((rs.getDouble(2) * unitprice) * 100.0) / 100.0 );
				product.setOpeningstockvalue(Math.round(product.getOpeningstockvalue() * 100.0) / 100.0);
				product.setTotalopeningval(product.getOpeningstockvalue() + totalopeningval);
				totalopeningval = (int) product.getTotalopeningval();
				product.setOpentotalcount(String.valueOf(opentotalcount));

				// lokesh
				product.setMfg(product2.getMfg());
				product.setExpiry_date(product2.getExpiry_date());
				product.setBatch_no(product2.getBatch_no());
				product.setMrp(product2.getMrp());
				product.setPurchase_price(product2.getPurchase_price());
				product.setSale_price(product2.getSale_price());

				product.setVendor(product2.getVendor());

				product.setPurchase_stock_qty(getPurchaseQty(rs.getString(1)));
				product.setUnitprices(""+unitprice);
				product.setSv(DateTimeUtils.changeFormat(Math.round(amt1 * 100.0) / 100.0));
				product.setMinorder(pmaster.getMinorder());
				product.setMaxorder(pmaster.getMaxorder());
				list.add(product);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	private int getPurcahseInStock(int productid, String string, String string2) {
		int res =0;
		try {
			string2 = string2 +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("select sum(qty) from inventory_procurement ");
			buffer.append("where inventory_procurement.date between '"+string+"' and '"+string2+"' and inventory_procurement.deleted=0 and confirm=1 and gudreceipt=1 ");
			buffer.append("and  inventory_procurement.iscancel=0 and prodid='"+productid+"' ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				res=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private int getIndentOutForNormalOpening(int productid, String string, String string2, int i) {
		int res =0;
		try {
			string2= string2 +" "+"59:59:59";
			String sql ="";
			if(i==0){
				sql ="select sum(change_qty) from product_stock_log where qty_in_out=0 and productid='"+productid+"' and datetime between '"+string+"' and '"+string2+"' and (source='Indent IN' or source='Direct Transfer IN' or source='Return To Store IN')";
			}else{
				sql ="select sum(change_qty) from product_stock_log where qty_in_out=1 and productid='"+productid+"' and datetime between '"+string+"' and '"+string2+"' and (source='Indent Out' or source='Direct Transfer Out' or source='Return To Store Out')";
			}
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private int getadjustmentstockdataIN(int productid, String fromDate, String toDate) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		toDate = toDate +" "+"59:59:59";
		
		String sql1= "select sum(change_qty) from adjustment_data where datetime between '" + fromDate
				+ "' and '" + toDate + "' " + "  and adjustment_type=2 and product_id='"+productid+"'  ";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private int getadjustmentstockdata(int productid, String fromDate, String toDate) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		toDate = toDate +" "+"59:59:59";
		
		String sql1= "select sum(change_qty) from adjustment_data where datetime between '" + fromDate
				+ "' and '" + toDate + "' " + "  and adjustment_type!=2 and product_id='"+productid+"'  ";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private int getconsumeqty(int productid, String fromDate, String toDate) {
			PreparedStatement preparedStatement = null;
			int result = 0;
			toDate = toDate +" "+"59:59:59";
			
			String sql1= "select sum(qty) from indent_patient_transfer_log where datetime between '" + fromDate+ "' and '" + toDate + "' and prodid='"+productid+"'  ";
			try {
				preparedStatement = connection.prepareStatement(sql1);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					result = rs.getInt(1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
	}

	public ArrayList<Product> getOpeningStockListByCatalogueWise(String fromDate, String toDate, Pagination pagination,
			int min, int max, String searchbyprodname, String category_id, String location_filter) {
		PreparedStatement preparedStatement = null;
		ArrayList<Product> list = new ArrayList<Product>();
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("select distinct inventory_catalogue.id,inventory_product.prodname,product_code ");
			buffer.append("from inventory_catalogue ");
			buffer.append("inner join inventory_product on inventory_product.catalogueid = inventory_catalogue.id  ");
			buffer.append("where procurement=0 and product_name is not null ");
			
			if (searchbyprodname != null) {
				buffer.append("and (inventory_product.prodname like ('%" + searchbyprodname + "%') or inventory_product.batch_no like ('%" + searchbyprodname + "%') or inventory_product.mfg like ('%" + searchbyprodname + "%') )  ");
			}
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			if(!category_id.equals("0")){
				buffer.append("and inventory_product.categoryid='"+category_id+"' ");
			}
			
			buffer.append("order by inventory_product.prodname ");
			
			String sql =buffer.toString();
			if(pagination!=null){
				sql= pagination.getSQLQuery(sql);
			}
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int totalopeningstock =0;
			double totalopeingstockvalue =0;
			int totalqtyin =0;
			double totalqtyinvalue=0;
			int totalqtyout=0;
			double totalstockvalue=0;
			double totalsaleprice=0;
			int totalclosingstock=0;
			double totalclosingvalue=0;
			
			while (rs.next()) {
				Product product = new Product();
				product.setProduct_name(rs.getString(2));
				product.setPro_code(rs.getString(3));
				String catalogueid = rs.getString(1);
				Product product2 = getProductDataByCatalogueid(catalogueid);
				product.setCategory(product2.getCategory());
				product.setMfg(product2.getMfg());
				//Opening- Purchase qty + return from patient + return to store - Patient sale - Return to supplier
				//- Consume -Direct transfer -Request transfer =before fromdate
				//Purchase qty =before fromdate this plus
				Product purbeforeproduct = getPuchaseProductData(catalogueid,fromDate,location_filter);  
				
				//Return from patient before fromdate this plus
				Product retpatientbeforeproduct = getReturnPatientProductData(catalogueid,fromDate,location_filter);
				
				//Patient Sale Before Fromdate this minus
				Product salebefore = getPatientSaleBeforeDate(catalogueid,fromDate,location_filter);
				
				//Return to supplier before fromdate this minus
				Product returntosuppplierbefore = getReturnToSupplierBeforeDate(catalogueid,fromDate,location_filter); 
				
				//Consume to user or patient this minus
				Product consumebefore = getConsumeBeforeDate(catalogueid,fromDate,location_filter);
				
				int directtransferout =0; 
				int requesttransferout =0;
				int retruntransferin =0;
				double returntransfervaluein =0;
				double directtransfervalueout =0;
				double requesttransfervalueout =0;
				
				int directtransferin =0;
				int requesttransferin =0;
				int returntransferout =0;
				double directtransfervaluein=0;
				double requesttransfervaluein=0;
				double returntransfervalueout=0;
				if(!location_filter.equals("0")){
					//Direct transfer out before fromdate
					Product directtransferproductout = getDirectTransferBefore(catalogueid,fromDate,location_filter);
					
					//Request Transfer out before fromdate
					Product requesttransferproductout = getRequestTransferBefore(catalogueid,fromDate,location_filter);
					
					//Return transfer in before fromdate
					Product returntransferproductin = getReturnTransferBefore(catalogueid,fromDate,location_filter);
					
					directtransferout = directtransferproductout.getTotalqty();
					requesttransferout = requesttransferproductout.getTotalqty();
					retruntransferin = returntransferproductin.getTotalqty();
					returntransfervaluein = returntransferproductin.getTotal_amount();
					directtransfervalueout = directtransferproductout.getTotal_amount();
					requesttransfervalueout = requesttransferproductout.getTotal_amount();
					//Direct transfer in before  fromdate
					Product directtransferproductin = getDirectTransferBeforeIn(catalogueid,fromDate,location_filter);
					
					//Request Transfer in before  fromdate
					Product requesttransferproductin = getRequestTransferBeforeIn(catalogueid,fromDate,location_filter);
					
					//Return transfer out before  fromdate
					Product returntransferproductout = getReturnTransferBeforeOut(catalogueid,fromDate,location_filter);
				
					directtransferin =directtransferproductin.getTotalqty();
					requesttransferin =requesttransferproductin.getTotalqty();
					returntransferout =returntransferproductout.getTotalqty();
					directtransfervaluein=directtransferproductin.getTotal_amount();
					requesttransfervaluein=requesttransferproductin.getTotal_amount();
					returntransfervalueout  = returntransferproductout.getTotal_amount();
				}
				
				Product adjustmentbeforout = getAdjustmentDataBeforeOut(catalogueid,fromDate,location_filter);
				
				Product adjustmentbeforin = getAdjustmentDataBeforeIn(catalogueid,fromDate,location_filter);
				
				//opening stock calculation
				//Opening= Purchase qty + return from patient + return to store - Patient sale - Return to supplier
				//- Consume -Direct transfer -Request transfer =before fromdate
				int openingstock = purbeforeproduct.getTotalqty() + retpatientbeforeproduct.getTotalqty() 
						-salebefore.getTotalqty() - returntosuppplierbefore.getTotalqty() - consumebefore.getTotalqty() 
						- directtransferout - requesttransferout + retruntransferin 
						+ directtransferin + requesttransferin- returntransferout  - adjustmentbeforout.getTotalqty() + adjustmentbeforin.getTotalqty();
				
				if(openingstock<0){
					openingstock=0;
				}
				
				//Opening Stock Value - purchase qty pur value + return from patient sale price = Before Fromdate;
				double opeingstockvalue = purbeforeproduct.getTotal_amount() + retpatientbeforeproduct.getTotal_amount() 
										- salebefore.getTotal_amount() - returntosuppplierbefore.getTotal_amount() - consumebefore.getTotal_amount()
										- directtransfervalueout - requesttransfervalueout + returntransfervaluein
										+ directtransfervaluein +requesttransfervaluein - returntransfervalueout - adjustmentbeforout.getTotal_amount() + adjustmentbeforin.getTotal_amount();
				
				if(opeingstockvalue<0){
					opeingstockvalue=0;
				}
				
				//Qty in-  Purchase qty +  return from patient + retruntransfer  = in fromdate and todate
				
				//Purchase qty =  in fromdate and todate
				Product purproduct = getPuchaseProductDataBetween(catalogueid,fromDate,toDate,location_filter);  
				
				//return from patient =  in fromdate and todate
				Product returnpatientqty = getReturnPatientProductDataBetween(catalogueid,fromDate,toDate,location_filter);
				
				// retruntransfer = in fromdate and todate 
				int retruntransferqtyin =0;
				double retruntransferqtyinvalue=0;
				int directtransferqtyin=0;
				int requesttransferqtyin =0;
				double directtransferqtyinvalue=0;
				double requesttransferqtyinvalue=0;
				if(!location_filter.equals("0")){
					//Return transfer between date 
					Product returntransferproductin = getReturnTransferBetweenDate(catalogueid,fromDate,location_filter,toDate);
					retruntransferqtyin = returntransferproductin.getTotalqty();
					retruntransferqtyinvalue = returntransferproductin.getTotal_amount();
					
					//Direct transfer between 
					Product directtransferproductin = getDirectTransferBetweenDateIn(catalogueid,fromDate,location_filter,toDate);
					
					//Request Transfer between
					Product requesttransferproductin = getRequestTransferBetweenDateIn(catalogueid,fromDate,location_filter,toDate);
					
					directtransferqtyin = directtransferproductin.getTotalqty();
					requesttransferqtyin = requesttransferproductin.getTotalqty();
					directtransferqtyinvalue = directtransferproductin.getTotal_amount();
					requesttransferqtyinvalue = requesttransferproductin.getTotal_amount();
				}
				
				Product adjustmentbetweenin = getAdjustmentDataBetweenIn(catalogueid,fromDate,location_filter,toDate);
				
				int qtyin = purproduct.getTotalqty() + returnpatientqty.getTotalqty() 
							+retruntransferqtyin + directtransferqtyin +requesttransferqtyin + adjustmentbetweenin.getTotalqty() ;
				
				double qtyinvalue = purproduct.getTotal_amount() + returnpatientqty.getTotal_amount() 
							+retruntransferqtyinvalue + directtransferqtyinvalue +requesttransferqtyinvalue + adjustmentbetweenin.getTotal_amount() ;
				
				//Qty out - Patient sale + Return to Supplier + Consume + direct transfer + request return  = in Fromdate and Todate
				
				// retruntransfer = in fromdate and todate 
				int directtransferqtyout =0;
				double directtransferqtyoutvalue=0;
				int requesttransferqtyout =0;
				double requesttransferqtyoutvalue=0;
				
				int returntransferqtyout =0;
				double returntransferqtyoutvalue=0;
				
				double directsaleprice=0;
				double returnsaleprice=0;
				double requestsaleprice=0;
				
				if(!location_filter.equals("0")){
					//Direct transfer between 
					Product directtransferproductout = getDirectTransferBetweenDate(catalogueid,fromDate,location_filter,toDate);
					
					//Request Transfer between
					Product requesttransferproductout = getRequestTransferBetweenDate(catalogueid,fromDate,location_filter,toDate);
					
					directtransferqtyout = directtransferproductout.getTotalqty();
					requesttransferqtyout = requesttransferproductout.getTotalqty();
					directtransferqtyoutvalue = directtransferproductout.getTotal_amount();
					requesttransferqtyoutvalue = requesttransferproductout.getTotal_amount();
					
					//Return to store between
					Product returntransferproductin = getReturnTransferBetweenDateOut(catalogueid,fromDate,location_filter,toDate);
					returntransferqtyout = returntransferproductin.getTotalqty();
					returntransferqtyoutvalue = returntransferproductin.getTotal_amount();
					
					directsaleprice = directtransferproductout.getSalepricetotal();
					returnsaleprice = returntransferproductin.getSalepricetotal();
					requestsaleprice = requesttransferproductout.getSalepricetotal();
				}
				
				//Patient Sale between Fromdate and Todate
				Product salepatient = getPatientSaleBetweenDate(catalogueid,fromDate,toDate,location_filter);
				
				//Return to supplier in between fromdate and todate
				Product returntosuppplier = getReturnToSupplierBetwwenDate(catalogueid,fromDate,toDate,location_filter); 
				
				//Consume to user or patient between Fromdate and Todate
				Product consume = getConsumeBetweenDate(catalogueid,fromDate,toDate,location_filter);
				
				Product adjustmentbetweenOut = getAdjustmentDatabetweenOut(catalogueid,fromDate,location_filter,toDate);
				
				int qtyout = salepatient.getTotalqty()+returntosuppplier.getTotalqty()
							+consume.getTotalqty() + directtransferqtyout +requesttransferqtyout 
							+returntransferqtyout + adjustmentbetweenOut.getTotalqty();
				
				//Stock value =  Patient sale unit price + Return to Supplier purchase price + Consume purchase price
				double stockvalue =  salepatient.getTotal_amount()+ returntosuppplier.getTotal_amount()
										+consume.getTotal_amount() +directtransferqtyoutvalue +requesttransferqtyoutvalue 
										+returntransferqtyoutvalue + adjustmentbetweenOut.getTotal_amount();
				
				//sale price total 
				double saleprice = salepatient.getSalepricetotal()+ returntosuppplier.getSalepricetotal()
										+consume.getSalepricetotal() +directsaleprice +returnsaleprice 
										+requestsaleprice + adjustmentbetweenOut.getSalepricetotal();
				
				
				
				//Closing - opening + qtyin - qtyout
				int closingstock = openingstock +qtyin - qtyout;
				
				//double closingvalue = unitprice * closingstock;
				double closingvalue = opeingstockvalue + qtyinvalue - stockvalue;
				int unknownqty = 0;
				if((openingstock +qtyin)<qtyout){
					closingstock =0;
					closingvalue =0;
					unknownqty = qtyout - (openingstock +qtyin);
				}
				
				//double unitprice = getUnitPriceFromCatalogueid(catalogueid);
				
				product.setUnknownqty(unknownqty);
				product.setOpeningstock(""+openingstock);
				product.setOpeningstockvalue(Math.round(opeingstockvalue * 100.0)/100.0);
				product.setPurchaseqty(qtyin);
				product.setSale(""+qtyout);
				product.setSalevalue(Math.round(stockvalue * 100.0)/100.0);
				product.setClosingstock(""+closingstock);
				product.setSv(DateTimeUtils.changeFormat(Math.round(closingvalue * 100.0)/100.0));
				product.setQtyinvalue(DateTimeUtils.changeFormat(qtyinvalue));
				product.setSale_price(DateTimeUtils.changeFormat(Math.round(saleprice * 100.0)/100.0));
				if(openingstock==0 && qtyin==0 && qtyout==0){
					
				}else{
					totalopeningstock =totalopeningstock + openingstock;
					totalopeingstockvalue =totalopeingstockvalue + opeingstockvalue;
					totalqtyin =totalqtyin + qtyin;
					totalqtyinvalue=qtyinvalue + totalqtyinvalue;
					totalqtyout = totalqtyout+qtyout;
					totalstockvalue = totalstockvalue+stockvalue;
					totalsaleprice = totalsaleprice + saleprice;
					totalclosingstock = totalclosingstock + closingstock;
					totalclosingvalue = totalclosingvalue + closingvalue;
					product.setTotalopeningstock(totalopeningstock);
					product.setTotalopeingstockvalue(totalopeingstockvalue);
					product.setTotalqtyin(totalqtyin);
					product.setTotalqtyinvalue(totalqtyinvalue);
					product.setTotalqtyout(totalqtyout);
					product.setTotalstockvalue(totalstockvalue);
					product.setTotalssaleprice(totalsaleprice);
					product.setTotalclosingstock(totalclosingstock);
					product.setTotalclosingvalue(totalclosingvalue);
					list.add(product);
				}
				//list.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Product getAdjustmentDatabetweenOut(String catalogueid, String fromDate, String location_filter,
			String toDate) {
		Product product = new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(adjustment_data.change_qty),sum(purchaseprice)/sum(pack),sum(adjustment_data.change_qty*(purchaseprice/pack)),sum(adjustment_data.change_qty*saleprice)   ");
			buffer.append("from adjustment_data ");
			buffer.append("inner join inventory_product on inventory_product.id = adjustment_data.product_id ");
			buffer.append("where adjustment_type!=2 and inventory_product.catalogueid='"+catalogueid+"' and adjustment_data.datetime between '"+fromDate+"' and '"+toDate+"' ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0.0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(total));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalepricetotal(0);
			while (rs.next()) {
				product.setSalepricetotal(rs.getDouble(4));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public Product getAdjustmentDataBetweenIn(String catalogueid, String fromDate, String location_filter, String toDate) {
		Product product = new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(adjustment_data.change_qty),sum(purchaseprice)/sum(pack),sum(adjustment_data.change_qty*(purchaseprice/pack))   ");
			buffer.append("from adjustment_data ");
			buffer.append("inner join inventory_product on inventory_product.id = adjustment_data.product_id ");
			buffer.append("where adjustment_type=2  and adjustment_data.datetime between '"+fromDate+"' and '"+toDate+"' ");
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			}
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0.0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(total));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public Product getAdjustmentDataBeforeIn(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			fromDate = fromDate +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(adjustment_data.change_qty),sum(purchaseprice)/sum(pack),sum(adjustment_data.change_qty*(purchaseprice/pack))   ");
			buffer.append("from adjustment_data ");
			buffer.append("inner join inventory_product on inventory_product.id = adjustment_data.product_id ");
			buffer.append("where adjustment_type=2 and inventory_product.catalogueid='"+catalogueid+"' and adjustment_data.datetime<'"+fromDate+"' ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0.0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(total));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public Product getAdjustmentDataBeforeOut(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			fromDate = fromDate +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(adjustment_data.change_qty),sum(purchaseprice)/sum(pack),sum(adjustment_data.change_qty*(purchaseprice/pack))   ");
			buffer.append("from adjustment_data ");
			buffer.append("inner join inventory_product on inventory_product.id = adjustment_data.product_id ");
			buffer.append("where adjustment_type!=2 and inventory_product.catalogueid='"+catalogueid+"' and adjustment_data.datetime<'"+fromDate+"' ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0.0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(total));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public double getUnitPriceFromCatalogueid(String catalogueid) {
		double res = 0;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select purchaseprice/pack from inventory_procurement ");
			buffer.append("inner join inventory_product on inventory_product.id = inventory_procurement.prodid ");
			buffer.append("where inventory_product.catalogueid='"+catalogueid+"' and inventory_product.procurement=0 ");
			buffer.append("order by inventory_product.id desc limit 1 ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				res = rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public Product getReturnTransferBetweenDateOut(String catalogueid, String fromDate, String location_filter,
			String toDate) {
		Product product = new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)),sum(inventory_transfer_log.qty*saleprice) from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
			buffer.append("where inventory_parent_transfer_log.req_or_trans=2 and inventory_parent_transfer_log.issued_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			if (!location_filter.equals("0")) {
				buffer.append("and from_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalepricetotal(0);
			while(rs.next()){
				product.setSalepricetotal(rs.getDouble(4));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getRequestTransferBetweenDateIn(String catalogueid, String fromDate, String location_filter,
			String toDate) {
		Product product = new Product();
		try {
			StringBuffer buffer=new StringBuffer();
			buffer.append("SELECT sum(inventory_request_temp_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_request_temp_log.qty*(purchaseprice/pack))  ");
			buffer.append("FROM inventory_request_temp_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_request_temp_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_request_temp_log.old_prodid ");
			buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			}
			if (!location_filter.equals("0")) {
				buffer.append("and from_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getDirectTransferBetweenDateIn(String catalogueid, String fromDate, String location_filter,
			String toDate) {
		Product product = new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1
			buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)) from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
			buffer.append("where inventory_parent_transfer_log.req_or_trans=1 and inventory_parent_transfer_log.issued_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			}
			
			if (!location_filter.equals("0")) {
				buffer.append("and to_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getReturnTransferBeforeOut(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			//out
			buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)) from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
			buffer.append("where inventory_parent_transfer_log.req_or_trans=2 and inventory_parent_transfer_log.issued_date<'"+fromDate+"' ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' and inventory_parent_transfer_log.deleted=0 ");
			if (!location_filter.equals("0")) {
				buffer.append("and from_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getRequestTransferBeforeIn(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			StringBuffer buffer=new StringBuffer();
			//in
			buffer.append("SELECT sum(inventory_request_temp_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_request_temp_log.qty*(purchaseprice/pack))  ");
			buffer.append("FROM inventory_request_temp_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_request_temp_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_request_temp_log.old_prodid ");
			buffer.append("where transfer_date<'"+fromDate+"' ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' and inventory_parent_transfer_log.deleted=0 ");
			if (!location_filter.equals("0")) {
				buffer.append("and from_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getDirectTransferBeforeIn(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1 // in
			buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)) from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
			buffer.append("where inventory_parent_transfer_log.req_or_trans=1 and inventory_parent_transfer_log.issued_date<'"+fromDate+"' ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' and inventory_parent_transfer_log.deleted=0 ");
			if (!location_filter.equals("0")) {
				buffer.append("and to_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getProductDataByCatalogueid(String catalogueid) {
		Product product = new Product();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select categoryid,mfg,catalogueid ");
			buffer.append("from inventory_product where procurement=0 and catalogueid='"+catalogueid+"' order by id desc ");
			PreparedStatement preparedStatement= connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product category = getCategory(rs.getString(1));
				product.setCategory(category.getName());
				product.setMfg(rs.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public Product getRequestTransferBetweenDate(String catalogueid, String fromDate, String location_filter,
			String toDate) {
		Product product = new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer=new StringBuffer();
			buffer.append("SELECT sum(inventory_request_temp_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_request_temp_log.qty*(purchaseprice/pack)),sum(inventory_request_temp_log.qty*saleprice)   ");
			buffer.append("FROM inventory_request_temp_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_request_temp_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_request_temp_log.old_prodid ");
			buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			if (!location_filter.equals("0")) {
				buffer.append("and inventory_request_temp_log.location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalepricetotal(0);
			while(rs.next()){
				product.setSalepricetotal(rs.getDouble(4));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getDirectTransferBetweenDate(String catalogueid, String fromDate, String location_filter,
			String toDate) {
		Product product = new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1
			buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)),sum(inventory_transfer_log.qty*saleprice) from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
			buffer.append("where inventory_parent_transfer_log.req_or_trans=1 and inventory_parent_transfer_log.issued_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			if (!location_filter.equals("0")) {
				buffer.append("and from_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalepricetotal(0);
			while(rs.next()){
				product.setSalepricetotal(rs.getDouble(4));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getReturnTransferBetweenDate(String catalogueid, String fromDate, String location_filter,
			String toDate) {
		Product product = new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer= new StringBuffer();
			buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)) from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
			buffer.append("where inventory_parent_transfer_log.req_or_trans=2 and inventory_parent_transfer_log.issued_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
			
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			}
			
			if (!location_filter.equals("0")) {
				buffer.append("and to_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getReturnTransferBefore(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			//from location out
			buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)) from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
			buffer.append("where inventory_parent_transfer_log.req_or_trans=2 and inventory_parent_transfer_log.issued_date<'"+fromDate+"' ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' and inventory_parent_transfer_log.deleted=0 ");
			if (!location_filter.equals("0")) {
				buffer.append("and to_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getRequestTransferBefore(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			StringBuffer buffer=new StringBuffer();
			//from location out
			buffer.append("SELECT sum(inventory_request_temp_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_request_temp_log.qty*(purchaseprice/pack))  ");
			buffer.append("FROM inventory_request_temp_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_request_temp_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_request_temp_log.old_prodid ");
			buffer.append("where transfer_date<'"+fromDate+"' and inventory_parent_transfer_log.deleted=0 ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			if (!location_filter.equals("0")) {
				buffer.append("and inventory_request_temp_log.location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getDirectTransferBefore(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1 out
			buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)) from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
			buffer.append("where inventory_parent_transfer_log.req_or_trans=1 and inventory_parent_transfer_log.issued_date<'"+fromDate+"' and inventory_parent_transfer_log.deleted=0 ");
			buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			if (!location_filter.equals("0")) {
				buffer.append("and from_location ='" + location_filter + "' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while(rs.next()){
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getConsumeBetweenDate(String catalogueid, String fromDate, String toDate, String location_filter) {
		Product product = new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer= new StringBuffer();
			buffer.append("select sum(indent_patient_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(indent_patient_transfer_log.qty*(purchaseprice/pack)),sum(indent_patient_transfer_log.qty*saleprice) ");
			buffer.append("from indent_patient_transfer_log ");
			buffer.append("inner join inventory_product on  inventory_product.id = indent_patient_transfer_log.prodid ");
			buffer.append("where inventory_product.catalogueid='"+catalogueid+"' and indent_patient_transfer_log.datetime between '"+fromDate+"' and '"+toDate+"' ");
			if(!location_filter.equals("0")){
				buffer.append("and indent_patient_transfer_log.fromlocation='"+location_filter+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalepricetotal(0);
			while(rs.next()){
				product.setSalepricetotal(rs.getDouble(4));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getReturnToSupplierBetwwenDate(String catalogueid, String fromDate, String toDate, String location_filter) {
		Product product = new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer= new StringBuffer();
			buffer.append("SELECT sum(inventory_product_return_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_product_return_log.qty*(purchaseprice/pack)),sum(saleprice*inventory_product_return_log.qty) ");
			buffer.append("from inventory_product_return_log ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_product_return_log.productid ");
			buffer.append("where status!=0 and inventory_product.catalogueid='"+catalogueid+"' and datetime between '"+fromDate+"' and '"+toDate+"' and inventory_product_return_log.iscancel=0 ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalepricetotal(0);
			while(rs.next()){
				product.setSalepricetotal(rs.getDouble(4));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getPatientSaleBetweenDate(String catalogueid, String fromDate, String toDate, String location_filter) {
		Product product= new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer= new StringBuffer();
			buffer.append("select sum(quantity),sum(quantity*charge),sum(quantity*(purchaseprice/pack))  ");
			buffer.append("from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_charges.invoiceid = apm_medicine_bill.id ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("where apm_medicine_bill.isreturn =0 and inventory_product.catalogueid='"+catalogueid+"' and date between '"+fromDate+"' and '"+toDate+"' and apm_medicine_bill.deleted=0  ");
			if(!location_filter.equals("0")){
				buffer.append("and apm_medicine_bill.location='"+location_filter+"' ");
			}
			PreparedStatement preparedStatement=connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalepricetotal(0);
			while (rs.next()) {
				product.setSalepricetotal(rs.getDouble(2));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}
	

	public Product getReturnPatientProductDataBetween(String catalogueid, String fromDate, String toDate, String location_filter) {
		Product product= new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer= new StringBuffer();
			buffer.append("select sum(quantity),sum(quantity*charge),sum(quantity*(purchaseprice/pack))  ");
			buffer.append("from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_charges.invoiceid = apm_medicine_bill.id ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("where apm_medicine_bill.isreturn =1  and date between '"+fromDate+"' and '"+toDate+"' and apm_medicine_bill.deleted=0  ");
			if(!location_filter.equals("0")){
				buffer.append("and apm_medicine_bill.location='"+location_filter+"' ");
			}
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			}
			PreparedStatement preparedStatement=connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getPuchaseProductDataBetween(String catalogueid, String fromDate, String toDate, String location_filter) {
		Product product= new Product();
		try {
			toDate = toDate +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			/*buffer.append("select inventory_procurement.qty,inventory_product.purchaseprice,inventory_product.pack from inventory_procurement  ");
			buffer.append("inner join inventory_product on inventory_product.id = inventory_procurement.prodid ");
			buffer.append("where inventory_product.catalogueid="+catalogueid+" and inventory_procurement.date between '"+fromDate+"' and '"+toDate+"' and  procurement='0'  and inventory_procurement.deleted=0 ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}*/
			buffer.append("select sum((inventory_procurement.qty+0) + (inventory_product.freeqty+0)),sum(purchaseprice)/sum(pack),sum(inventory_procurement.qty*(purchaseprice/pack))+sum(inventory_product.freeqty * (purchaseprice/pack)),   ");
			buffer.append("sum(inventory_product.freeqty),sum(inventory_procurement.qty),sum(inventory_procurement.qty*(purchaseprice/pack)),sum(inventory_product.freeqty * (purchaseprice/pack)) ");
			buffer.append("from inventory_procurement ");
			buffer.append("inner join inventory_product on inventory_product.id = inventory_procurement.prodid ");
			buffer.append("where  inventory_procurement.date between '"+fromDate+"' and '"+toDate+"' and  procurement='0'  and inventory_procurement.deleted=0 and confirm=1 and gudreceipt=1 ");
			buffer.append("and  inventory_procurement.iscancel=0 ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid="+catalogueid+" ");
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setGrnqtyamtttl(0);
			product.setGrnfreeqtyamtttl(0);
			while (rs.next()) {
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
				product.setGrnqtyamtttl(rs.getDouble(6));
				product.setGrnfreeqtyamtttl(rs.getDouble(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public Product getConsumeBeforeDate(String catalogueid, String fromDate, String location_filter) {
		Product product= new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			buffer.append("select sum(indent_patient_transfer_log.qty),sum(indent_patient_transfer_log.qty*(purchaseprice/pack)) ");
			buffer.append("from indent_patient_transfer_log ");
			buffer.append("inner join inventory_product on  inventory_product.id = indent_patient_transfer_log.prodid ");
			buffer.append("where inventory_product.catalogueid='"+catalogueid+"' and indent_patient_transfer_log.datetime<'"+fromDate+"' ");
			if(!location_filter.equals("0")){
				buffer.append("and indent_patient_transfer_log.fromlocation='"+location_filter+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(2) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getReturnToSupplierBeforeDate(String catalogueid, String fromDate, String location_filter) {
		Product product= new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			buffer.append("SELECT sum(inventory_product_return_log.qty),sum(inventory_product_return_log.qty*(purchaseprice/pack)) ");
			buffer.append("from inventory_product_return_log ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_product_return_log.productid ");
			buffer.append("where status!=0 and inventory_product.catalogueid='"+catalogueid+"' and datetime<'"+fromDate+"' and inventory_product_return_log.iscancel=0 ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(2) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(2));
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getPatientSaleBeforeDate(String catalogueid, String fromDate, String location_filter) {
		Product product= new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			buffer.append("select sum(quantity),sum(quantity*(purchaseprice/pack))  ");
			buffer.append("from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_charges.invoiceid = apm_medicine_bill.id ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("where apm_medicine_bill.isreturn =0 and inventory_product.catalogueid='"+catalogueid+"' and date<'"+fromDate+"' and apm_medicine_bill.deleted=0  ");
			if(!location_filter.equals("0")){
				buffer.append("and apm_medicine_bill.location='"+location_filter+"' ");
			}
			PreparedStatement preparedStatement=connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(2) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return product;
	}

	public Product getReturnPatientProductData(String catalogueid, String fromDate, String location_filter) {
		Product product= new Product();
		try {
			StringBuffer buffer= new StringBuffer();
			/*buffer.append("select quantity,inventory_product.purchaseprice,inventory_product.pack  ");
			buffer.append("from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_charges.invoiceid = apm_medicine_bill.id ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("where apm_medicine_bill.isreturn =1 and inventory_product.catalogueid='"+catalogueid+"' and date<'"+fromDate+"'  ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}*/
			buffer.append("select sum(quantity),sum(quantity*charge),sum(quantity*(purchaseprice/pack))  ");
			buffer.append("from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_charges.invoiceid = apm_medicine_bill.id ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("where apm_medicine_bill.isreturn =1 and inventory_product.catalogueid='"+catalogueid+"' and date<'"+fromDate+"' and apm_medicine_bill.deleted=0  ");
			if(!location_filter.equals("0")){
				buffer.append("and apm_medicine_bill.location='"+location_filter+"' ");
			}
			PreparedStatement preparedStatement=connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));
				product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
				product.setTotal_amount(total);
				product.setTotalqty(totalqty);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}

	public Product getPuchaseProductData(String catalogueid, String fromDate, String location_filter) {
		Product product = new Product();
		try {
			StringBuffer buffer = new StringBuffer();
		/*	buffer.append("select inventory_procurement.qty,purchaseprice,pack from inventory_procurement  ");
			buffer.append("inner join inventory_product on inventory_product.id = inventory_procurement.prodid ");
			buffer.append("where inventory_product.catalogueid="+catalogueid+" and inventory_procurement.date<'"+fromDate+"' and  procurement='0'  and inventory_procurement.deleted=0 ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}*/
			buffer.append("select sum(inventory_procurement.qty),sum(purchaseprice)/sum(pack),sum(inventory_procurement.qty*(purchaseprice/pack))+sum(inventory_product.freeqty * (purchaseprice/pack)),sum(inventory_product.freeqty),sum(inventory_procurement.qty)   ");
			buffer.append("from inventory_procurement ");
			buffer.append("inner join inventory_product on inventory_product.id = inventory_procurement.prodid ");
			buffer.append("where inventory_product.catalogueid="+catalogueid+" and inventory_procurement.date<'"+fromDate+"' and  procurement='0'  and inventory_procurement.deleted=0 and confirm=1 and gudreceipt=1 ");
			buffer.append("and inventory_procurement.iscancel=0 ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0.0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(total));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				/*totalqty = totalqty + rs.getInt(1);
				double pack =rs.getDouble(3);
				if(pack==0){
					pack=1.0;
				}
				double unitprice = rs.getDouble(2)/pack;
				total = total +  (unitprice*rs.getInt(1));*/
				/*totalqty = rs.getInt(1);
				total = rs.getDouble(3);*/
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}
	public Product getYesterdayClosingData(String previousdate, String catalogueid, String location_filter) {
		Product product = new Product();
		try {
			String sql ="select closing_stock, closing_value,closing from inventory_catalogue_log where date='"+previousdate+"' and location='"+location_filter+"' and catalogueid='"+catalogueid+"' ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			product.setTotalclosingstock(0);
			product.setTotalclosingvalue(0);
			product.setClosing(0);
			if (rs.next()) {
				product.setTotalclosingstock(rs.getInt(1));
				product.setTotalclosingvalue(rs.getDouble(2));
				product.setClosing(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public boolean checkPreviousDataCatAvailable(String previousdate, String catalogueid, String location_filter) {
		boolean flag = false;
		try {
			String sql ="select * from inventory_catalogue_log where date='"+previousdate+"' and location='"+location_filter+"' and catalogueid='"+catalogueid+"' ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public int getRetuenToSuplierstock(int productid, String fromDate, String toDate) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		toDate = toDate +" "+"59:59:59";
		
		String sql1= "select sum(qty) from inventory_product_return_log where datetime '" + fromDate
				+ "' and '" + toDate + "' " + " and status=1 and productid='"+productid+"' and iscancel=0 ";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getRetuenstock(int productid, String fromDate, String toDate) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		toDate = toDate +" "+"59:59:59";
		String sql = "SELECT sum(quantity) FROM apm_medicine_bill inner join apm_medicine_charges on "
				+ " apm_medicine_charges.invoiceid = apm_medicine_bill.id " + " where commencing between '" + fromDate
				+ "' and '" + toDate + "' " + " and product_id = " + productid + " and isreturn = 1 and apm_medicine_bill.deleted=0 ";

		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getClosingstock(int productid, String fromDate, String toDate) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		toDate = toDate +" "+"59:59:59";
		/*String sql = "SELECT sum(quantity) FROM apm_medicine_charges where product_id = " + productid + " "
				+ " and commencing between '" + fromDate + "' and '" + toDate + "' and returnbillno=0 ";*/
		String sql = "SELECT sum(quantity) FROM apm_medicine_bill inner join apm_medicine_charges on "
				+ " apm_medicine_charges.invoiceid = apm_medicine_bill.id " + " where commencing between '" + fromDate
				+ "' and '" + toDate + "' " + " and product_id = " + productid + " and isreturn = 0 and apm_medicine_bill.deleted=0 ";
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getproductName(int productid) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT prodname FROM inventory_product where id = " + productid + " ";

		try {

			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getConsumptionReport(String fromdate, String todate, String location, String salereturun,
			String billtype, Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		try {
			StringBuffer buffer = new StringBuffer();

			buffer.append(
					"select id,date,debit,clientid,pclientid,location,cgst,sgst,time,isreturn,priscid from apm_medicine_bill where  ");
			buffer.append("date between '" + fromdate + "' and '" + todate + "' ");

			if (!billtype.equals("0")) {
				buffer = new StringBuffer();
				buffer.append(
						"select apm_medicine_bill.id,apm_medicine_bill.date,debit,apm_medicine_bill.clientid,apm_medicine_bill.pclientid,apm_medicine_bill.location,cgst,sgst,time,isreturn,priscid,paymode  ");
				buffer.append(" from apm_medicine_bill inner join apm_medicine_payment on ");
				buffer.append(" apm_medicine_bill.id = apm_medicine_payment.billno where ");
				buffer.append("apm_medicine_bill.date between '" + fromdate + "' and '" + todate + "' and paymode='"
						+ billtype + "' ");
			}

			if (!location.equals("0") && !location.equals("1")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}

			if (salereturun.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}

			if (salereturun.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}

			buffer.append("and apm_medicine_bill.tpid!='0' ");
			buffer.append("order by apm_medicine_bill.id desc ");
			String sql = pagination.getSQLQuery(buffer.toString());
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setDate(rs.getString(2));
				product.setDebit(rs.getString(3));
				product.setClientid(rs.getString(4));
				product.setPclientid(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setCgst(rs.getString(7));
				product.setSgst(rs.getString(8));
				product.setTime(rs.getString(9));
				product.setIsreturn(rs.getInt(10));
				product.setPriscid(rs.getString(11));

				if (!billtype.equals("0")) {
					product.setBilltype(rs.getString(12));
				}

				String date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				product.setDateTime(date + " " + product.getTime());

				double temp = Double.parseDouble(product.getSgst()) * 2;
				double withoutGST = rs.getDouble(3) - temp;
				product.setTotal(DateTimeUtils.changeFormat(withoutGST));
				if (product.getIsreturn() > 0) {
					product.setType("Sales Return");
				} else {
					product.setType("Sales");
				}
				if (!product.getPclientid().equals("0")) {

					Priscription priscription = pharmacyDAO.getPharmacyPatient(product.getPclientid());
					product.setClienttype("-");
					product.setTpname("NO");
					product.setDoctor(priscription.getPractitionername());
					product.setClientname(priscription.getFullname());
				} else {

					String isipdid = pharmacyDAO.getIpdIdFromClientID(rs.getInt(4));
					if (isipdid.equals("0")) {
						product.setClienttype("OPD");
					} else {
						product.setClienttype("IPD");
					}
					ClientDAO clientDAO = new JDBCClientDAO(connection);
					Client client = clientDAO.getClientDetails(product.getClientid());
					/*
					 * if(client.getTypeName()!=null){
					 * if(!client.getTypeName().equals("0")){
					 * product.setTpname("YES"); } else {
					 * product.setTpname("NO"); } } else {
					 * product.setTpname("NO"); }
					 */

					product.setClientname(client.getTitle() + " " + client.getFirstName() + " " + client.getLastName());
					if (!product.getPriscid().equals("0")) {
						Priscription priscription = emrDAO.getEditPriscription(product.getPriscid());
						UserProfile userProfile = userProfileDAO
								.getUserprofileDetails(Integer.parseInt(priscription.getPrectionerid()));
						product.setDoctor(userProfile.getFullname());
					} else {
						product.setDoctor(" ");
					}

				}
				product.setTpname("YES");

				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Master> getWareHouseList() {

		ArrayList<Master> list = new ArrayList<Master>();
		try {
			String sql = "select id,name from inventory_warehouse ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				list.add(master);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Master> getCategoryFromWareHouse(String id) {

		ArrayList<Master> list = new ArrayList<Master>();
		try {
			String sql = "select id,name from inventory_category where warehouse like('%" + id + "%')  ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				list.add(master);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Product> getProductListFromOtherLocation(Product prod, String location) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select id, prodname, stock, expiry_date, batch_no, location, hsnno from inventory_product where location="
					+ location + " and stock>0 and prodname like ('" + prod.getProduct_name() + "')";
			// String sql ="select id, prodname, stock, expiry_date, batch_no,
			// location, hsnno from inventory_product where
			// location="+location+" and stock>0 and prodname
			// ='"+prod.getProduct_name()+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int avail_stock = Integer.parseInt(rs.getString(3));
				Product product = new Product();
				product.setAvail_stock(avail_stock);
				product.setProduct_id("" + rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setStock(rs.getString(3));
				product.setExpiry_date(rs.getString(4));
				product.setBatch_no(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setHsnno(rs.getString(7));
				product.setReqqty(prod.getStock());
				String fromlocation = pharmacyLocationNameByID(rs.getString(6));
				product.setFromlocation(fromlocation);
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int addTempPoRequest(String product_id, String parentid, int req_stock, String prod_name, String todate,
			String warehouse_id, int indentno, String catid) {
		int result = 0;
		boolean flag = false;
		try {
			String sql = "select prod_name from temp_po where status=0 and prod_name like ('" + prod_name
					+ "') and parentid=" + parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				flag = true;
			}

			if (flag == false) {
				String query = "insert into temp_po (prod_id,status,parentid,qty,prod_name,date,warehouseid,indent_no,catalogueid) values (?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedStatement2 = connection.prepareStatement(query);
				preparedStatement2.setString(1, product_id);
				preparedStatement2.setString(2, "0");
				preparedStatement2.setString(3, parentid);
				preparedStatement2.setString(4, "" + req_stock);
				preparedStatement2.setString(5, prod_name);
				preparedStatement2.setString(6, todate);
				preparedStatement2.setString(7, warehouse_id);
				preparedStatement2.setString(8, "" + indentno);
				preparedStatement2.setString(9, catid);
				result = preparedStatement2.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getTempPoList(String parentid) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
		try {
			String sql = "select prod_id,id,qty,catalogueid from temp_po where status=0 and parentid=" + parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product2 = new Product();
				Product product = new Product();
				if(rs.getString(1)==null){
					product = inventoryProductDAO.getProductCatalogueDetails(rs.getString(4));
				}else if(rs.getString(1).equals("0")){
					product = inventoryProductDAO.getProductCatalogueDetails(rs.getString(4));
				}else{
					product = inventoryProductDAO.getProduct(rs.getString(1));
				}
				product2.setId(rs.getInt(2));
				product2.setProd_name(product.getProduct_name());
				product2.setQty(rs.getString(3));
				arrayList.add(product2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Product> getLocationWiseStock(Product prod, Product product) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select sum(stock),location from inventory_product where stock>0 and location!=32 and prodname like ('"
					+ prod.getProduct_name() + "') and location!='"+product.getLocation()+"' group by location";
			// String sql ="select sum(stock),location from inventory_product
			// where stock>0 and location!=1 and
			// prodname='"+prod.getProduct_name()+"' and
			// location!="+product.getLocation()+" group by location";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int qty = rs.getInt(1);
				Product product1 = new Product();
				product1.setAvail_stock(qty);
				String location = rs.getString(2);
				product1.setFromlocation(rs.getString(2));
				product1.setLocation(rs.getString(2));
				arrayList.add(product1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Product> getWithoutBatchWiseProductList(int req_stock, Product prod) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			// String sql ="select id, prodname, stock, expiry_date, batch_no,
			// location, hsnno from inventory_product where location=1 and
			// stock>0 and prodname like ('"+prod.getProduct_name()+"')";
			String sql = "select id, prodname, stock, expiry_date, batch_no, location, hsnno from inventory_product where location=32 and stock>0 and prodname='"
					+ prod.getProduct_name() + "'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int avail_stock = Integer.parseInt(rs.getString(3));
				Product product = new Product();
				product.setAvail_stock(avail_stock);
				product.setProduct_id("" + rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setStock(rs.getString(3));
				product.setExpiry_date(rs.getString(4));
				product.setBatch_no(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setHsnno(rs.getString(7));
				product.setReqqty(prod.getStock());
				String fromlocation = pharmacyLocationNameByID(rs.getString(6));
				product.setFromlocation(fromlocation);
				product.setReq_qty(req_stock);
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int updateTempPoStatus(int id, String qty) {
		int result = 0;
		try {
			String sql = "update temp_po set qty=" + qty + " where id=" + id + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int saveNewMedicineRequestParent(Product product, String userid) {
		int result = 0;
		try {
			String sql = "insert into inventory_parent_transfer_log (request_date, from_location, time,r_status,req_or_trans,userid,expected_date) values (?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			String data = product.getRequest_date();
			String[] datetime = data.split(" ");
			preparedStatement.setString(1, datetime[0]);
			preparedStatement.setString(2, product.getLocation());
			preparedStatement.setString(3, datetime[1]);
			preparedStatement.setString(4, "0");
			preparedStatement.setString(5, "0");
			preparedStatement.setString(6, userid);
			preparedStatement.setString(7, product.getExpectedDate());
			result = preparedStatement.executeUpdate();
			if (result > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				while (resultSet.next()) {
					result = resultSet.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getAllAvailableStock(String product_name, String location) {
		int qty = 0;
		try {
			String sql = "select sum(stock) from inventory_product where stock>0 and location=" + location
					+ " and prodname like ('" + product_name + "')";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				qty = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qty;
	}

	

	public int updateAproveTransferLog(String parentid, String status, String aprove, String userid) {
		int result = 0;

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String currentdate = dateFormat.format(calendar.getTime());

			String sql = "";
			if (aprove.equals("1")) {
				sql = "update inventory_parent_transfer_log set r_status=" + status
						+ ", admin_status=1, admin_aprove_date='" + currentdate + "',admin_approve_userid='" + userid
						+ "' where id=" + parentid + " ";
			} else {
				sql = "update inventory_parent_transfer_log set r_status=" + status + ",head_aprove_date='"
						+ currentdate + "' where id=" + parentid + " ";
			}
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public int createPoOnTransferStatus(String parentid, String status, String transfer_date, String userid) {
		int result = 0;
		try {
			String sql = "";
			sql = "update inventory_parent_transfer_log set r_status=" + status + ",deliver_Date='"+transfer_date+"',pocreateduserid='"+userid+"' where id=" + parentid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getVenodrMedinceListofType(String medicinetype, String category, String vendorid) {

		ArrayList<Product> list = new ArrayList<Product>();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select id,prodname from inventory_product where categoryid='" + category + "' ");
			if (category.equals("2")) {
				buffer.append("and medicine_type='" + medicinetype + "'  ");
			} else {
				buffer.append("and prodtypeid='" + medicinetype + "' ");
			}
			buffer.append("and prodname is NOT NULL group by prodname asc ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int updateHandover_To(String parentid, String handover_to) {
		int result = 0;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String currentdate = dateFormat.format(calendar.getTime());
			String handover = handover_to + "[" + currentdate + "]";
			String sql = "";
			sql = "update inventory_parent_transfer_log set handover_to='" + handover + "' where id=" + parentid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getTotalTransferIdentCount() {
		int result = 0;
		try {
			String sql = "select count(*) from inventory_parent_transfer_log where trans_indent_no>0 and req_or_trans=1";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int saveUpDeleteIndent(String parentid, String delete_reason, String userid, String loc) {
		int result = 0;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			String date = dateFormat.format(cal.getTime());

			String sql = "insert into inventory_product_log (productid, userid, location,  date, deleted, comment, isindent, indent_parentid) values (?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "0");
			preparedStatement.setString(2, userid);
			preparedStatement.setString(3, loc);
			preparedStatement.setString(4, date);
			preparedStatement.setString(5, "1");
			preparedStatement.setString(6, delete_reason);
			preparedStatement.setString(7, "1");
			preparedStatement.setString(8, parentid);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int saveUpDeletePharmacyBill(String userid, String loc, String delete_reason, String bill) {
		int result = 0;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			String date = dateFormat.format(cal.getTime());

			String sql = "insert into inventory_product_log (productid, userid, location,  date, deleted, comment, isindent, pharmacy_billno) values (?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "0");
			preparedStatement.setString(2, userid);
			preparedStatement.setString(3, loc);
			preparedStatement.setString(4, date);
			preparedStatement.setString(5, "1");
			preparedStatement.setString(6, delete_reason);
			preparedStatement.setString(7, "2");
			preparedStatement.setString(8, bill);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public double getTotalReceived(int billno) {

		double tot = 0;
		try {

			String sql = "select sum(payment) from apm_medicine_payment where billno=" + billno + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				tot = rs.getDouble(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return tot;
	}

	public ArrayList<Product> getChildTranfserDataAfterPO(String parentid) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select id, parentid, old_prodid, new_prodid, qty,comment,avail_qty,status,catlogueid from inventory_transfer_log where status=0 and parentid="
					+ parentid + " and cancel_req='0'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			double total_amount = 0;
			double totolmrp = 0;

			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setChildid("" + rs.getInt(1));
				product.setParentid(rs.getString(2));
				Product product2 = getProduct(rs.getString(3));
				product.setHsnno(product2.getHsnno());
				product.setProduct_name(product2.getProduct_name());
				product.setBatch_no(product2.getBatch_no());
				String expirydate ="";
				if(product2.getExpiry_date()!=null){
					if(!product2.getExpiry_date().equals("")){
						expirydate = DateTimeUtils.getCommencingDate1(product2.getExpiry_date());
					}
				}
				
				product.setExpiry_date(expirydate);
				product.setStock(rs.getString(5));
				String comment = "";
				if (rs.getString(6) != null) {
					comment = rs.getString(6);
				}
				product.setComment(comment);
				product.setSale_price(product2.getSale_price());
				product.setMrp(product2.getMrp());
				if (product2.getPack() == null) {
					product2.setPack("1");
				}
				if (product2.getPack().equals("")) {
					product2.setPack("1");
				}
				double amt = Double.parseDouble(product2.getPurchase_price()) / Integer.parseInt(product2.getPack());

				double amountno = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getSale_price());
				double amountmrp = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getMrp());
				total_amount = total_amount + amountno;
				totolmrp = totolmrp + amountmrp;
				product.setTotal_amount(total_amount);
				product.setTotolmrp(Math.round(totolmrp * 100.0) / 100.0);
				product.setAmountno(Math.round(amountno * 100.0) / 100.0);
				product.setAmountmrp(Math.round(amountmrp * 100.0) / 100.0);
				product.setPurchase_price(DateTimeUtils.changeFormat(amt));
				product.setProduct_id(rs.getString(3));
				double unit1 = Math.round(amt * 100.0) / 100.0;
				product.setUnit("" + unit1);
				Product product3 = getParentTransferData(parentid);
				product.setExpectedDate(product3.getExpectedDate());
				int avail_stock = getAllAvailableStock(product2.getProduct_name(), product3.getLocation());
				product.setAvail_stock(avail_stock);
				int a_qty = 0;
				if (rs.getString(7) != null) {
					if (!rs.getString(7).equals("")) {
						a_qty = Integer.parseInt(rs.getString(7));
					}
				}
				product.setAvail_qty(a_qty);

				int total_recived_qty = getReqQtyFrmTemp(product2.getProduct_name(), parentid);

				if (total_recived_qty == Integer.parseInt(rs.getString(5))) {
					product.setStatus("1");
				} else if (total_recived_qty > Integer.parseInt(rs.getString(5))) {
					product.setStatus("1");
				} else if (total_recived_qty > 0) {
					product.setStatus("1");
				} else {
					product.setStatus("0");
				}
				product.setCatalogueid(rs.getString(9));
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int updateTransferNotes(String parentid, String comment, String transfer_date) {
		int result = 0;
		try {
			String sql1 = "select transfer_note from inventory_parent_transfer_log where id=" + parentid + "";
			PreparedStatement statement = connection.prepareStatement(sql1);
			ResultSet rs = statement.executeQuery();
			String comment1 = "";
			while (rs.next()) {
				comment1 = rs.getString(1);
			}

			if (comment == null)
				comment = "";

			if (comment1 != null) {
				if (comment1.equals("")) {
					comment1 = comment;
				} else {
					comment1 = comment1 + "," + comment;
				}
			} else {
				comment1 = comment;
			}

			String query = "select check_avail_note from inventory_parent_transfer_log where id=" + parentid + "";
			PreparedStatement statement2 = connection.prepareStatement(query);
			ResultSet rs5 = statement2.executeQuery();
			String msg = "";
			while (rs5.next()) {
				msg = rs5.getString(1);
			}

			if (msg != null) {
				if (msg.equals("")) {
					msg = comment;
				} else {
					msg = msg + "||" + comment;
				}
			} else {
				msg = comment;
			}

			String sql = "update inventory_parent_transfer_log set transfer_note='" + comment1 + "', check_avail_note='"
					+ msg + "',deliver_date='"+transfer_date+"' where id=" + parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getChildTempReqData(String parentid) {
		InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			// String sql ="select id, parentid, old_prodid, new_prodid, qty,
			// location,status from inventory_request_temp_log where
			// parentid="+parentid+" and status=1";
			String sql = "select id, parentid, old_prodid, new_prodid, qty, location,status from inventory_request_temp_log where parentid="
					+ parentid + " and transfer_status='0' ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_id(rs.getString(3));
				Product product2 = inventoryProductDAO.getProduct(rs.getString(3));
				product.setProduct_name(product2.getProduct_name());
				product.setLocation(rs.getString(6));
				product.setReqqty(rs.getString(5));
				product.setStock(product2.getStock());
				product.setHsnno(product2.getHsnno());
				product.setExpiry_date(product2.getExpiry_date());
				product.setBatch_no(product2.getBatch_no());
				product.setLocation(product2.getLocation());
				Product product3 = getParentTransferData(parentid);
				product.setExpectedDate(product3.getExpectedDate());
				product.setReq_location(product3.getLocation());
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int updateParentProductStatus(String parentid, int all_med_status, int r_status) {
		int result = 0;
		try {
			String sql = "update inventory_parent_transfer_log set r_status='" + r_status + "', all_med_status="
					+ all_med_status + " where id=" + parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getTotalReqTransferTemp(String parentid) {
		int count = 0;
		try {
			String sql = "select count(*) from inventory_request_temp_log where parentid=" + parentid
					+ " and trans_count>0 group by trans_count";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				count = resultSet.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public int updatePendingTransferLog(String parentid, String userid) {
		int result = 0;

		try {
			String po = "";
			String sql1 = "select po_data from inventory_parent_transfer_log where id=" + parentid + "";
			PreparedStatement preparedStatement = connection.prepareStatement(sql1);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				po = resultSet.getString(1);
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String currentdate = dateFormat.format(calendar.getTime());
			String po_data = currentdate + " " + userid;

			if (po != null) {
				if (!po.equals("")) {
					po_data = po + "," + po_data;
				}
			}

			String sql = "";
			sql = "update inventory_parent_transfer_log set po_data='" + po_data + "' where id=" + parentid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}

	public int updateHandover_ToNew(String parentid, String handover_to) {
		int result = 0;
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String currentdate = dateFormat.format(calendar.getTime());

			String query = "select handover_to from inventory_parent_transfer_log where id=" + parentid + "";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			String handover = "";
			while (resultSet.next()) {
				handover = resultSet.getString(1);
			}

			handover = handover + "||" + handover_to + "[" + currentdate + "]";

			String sql = "update inventory_parent_transfer_log set handover_to='" + handover + "' where id=" + parentid
					+ " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getPaymentReceiveReport(String fromdate, String todate, String location,
			String salereturun, String billtype, String balance1,Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		try {
			StringBuffer buffer = new StringBuffer();

			buffer.append(
					"select apm_medicine_bill.id,apm_medicine_bill.date,debit,apm_medicine_bill.clientid,apm_medicine_bill.pclientid,apm_medicine_bill.location,cgst,sgst,time,isreturn,priscid,payment,paymode,apm_medicine_bill.balance from apm_medicine_bill   ");
			buffer.append("inner join apm_medicine_payment on apm_medicine_payment.billno = apm_medicine_bill.id ");
			buffer.append("where apm_medicine_payment.date between '" + fromdate + "' and '" + todate + "' ");

			if (!billtype.equals("0")) {
				buffer.append("and paymode='" + billtype + "' ");
			}
			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}
			if (salereturun.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}
			if (salereturun.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}
			if (!balance1.equals("0")) {
				if (balance1.equals("1")) {
					buffer.append("and apm_medicine_bill.balance < 100 and apm_medicine_bill.balance > 0 ");
				} else if (balance1.equals("2")) {
					buffer.append("and apm_medicine_bill.balance < 1000 and apm_medicine_bill.balance > 0 ");
				} else if (balance1.equals("3")) {
					buffer.append("and apm_medicine_bill.balance < 5000 and apm_medicine_bill.balance > 0 ");
				} else if (balance1.equals("4")) {
					buffer.append("and apm_medicine_bill.balance > 5000 and apm_medicine_bill.balance > 0 ");
				}
			}
			buffer.append("and apm_medicine_bill.tpid='0' ");
			buffer.append("order by apm_medicine_payment.id desc ");
			String sql1 = pagination.getSQLQuery(buffer.toString());
			PreparedStatement ps = connection.prepareStatement(sql1);

			ResultSet rs = ps.executeQuery();
			double tot = 0;
			double bal = 0;
			double total =0;
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setDate(rs.getString(2));
				product.setDebit(rs.getString(3));
				product.setClientid(rs.getString(4));
				product.setPclientid(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setCgst(rs.getString(7));
				product.setSgst(rs.getString(8));
				product.setTime(rs.getString(9));
				product.setIsreturn(rs.getInt(10));
				product.setPriscid(rs.getString(11));
				// double payAmt= getTotalReceived(product.getId());
				double payAmt = rs.getDouble(12);
				total = total + rs.getDouble(3);
				product.setTotalAmt(total);
				tot = tot + payAmt;
				product.setPayment(tot);
				product.setPayAmount(DateTimeUtils.changeFormat(payAmt));
				// if(!billtype.equals("0")){
				product.setBilltype(rs.getString(13));
				// }

				String date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				product.setDateTime(date + " " + product.getTime());

				double temp = Double.parseDouble(product.getSgst()) * 2;
				double withoutGST = rs.getDouble(3) - temp;
				product.setTotal(DateTimeUtils.changeFormat(withoutGST));
				if (product.getIsreturn() > 0) {
					product.setType("Sales Return");
				} else {
					product.setType("Sales");
				}
				if (!product.getPclientid().equals("0")) {

					Priscription priscription = pharmacyDAO.getPharmacyPatient(product.getPclientid());
					product.setClienttype("-");
					product.setTpname("NO");
					product.setDoctor(priscription.getPractitionername());
					product.setClientname(priscription.getFullname());
					product.setMobile(priscription.getMobile());
				} else {

					String isipdid = pharmacyDAO.getIpdIdFromClientID(rs.getInt(4));
					if (isipdid.equals("0")) {
						product.setClienttype("OPD");
					} else {
						product.setClienttype("IPD");
					}
					ClientDAO clientDAO = new JDBCClientDAO(connection);
					Client client = clientDAO.getClientDetails(product.getClientid());
					if (client.getTypeName() != null) {
						if (!client.getTypeName().equals("0")) {
							product.setTpname("YES");
						} else {
							product.setTpname("NO");
						}
					} else {
						product.setTpname("NO");
					}
					product.setClientname(client.getTitle() + " " + client.getFirstName() + " " + client.getLastName());
					product.setMobile(client.getMobNo());
					if (!product.getPriscid().equals("0")) {
						Priscription priscription = emrDAO.getEditPriscription(product.getPriscid());
						UserProfile userProfile = userProfileDAO
								.getUserprofileDetails(Integer.parseInt(priscription.getPrectionerid()));
						product.setDoctor(userProfile.getFullname());
					} else {
						product.setDoctor(" ");
					}

				}
				double balance = rs.getDouble(14);
				bal = bal + balance;
				product.setBalance(rs.getString(14));
				product.setTotalbalance(bal);
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Product> getWarehouse() {
		ArrayList<Product> list = new ArrayList<Product>();
		try {
			String sql = "select id,name from inventory_warehouse";
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setWarehouse_id(rs.getString(1));
				product.setWarehouse_name(rs.getString(2));
				list.add(product);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Product getCategoryWarehouseById(String selectedid) {
		Product productmanagement = new Product();
		try {
			String sql = "select id,name,description,warehouse from inventory_category where id=" + selectedid + "";
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				productmanagement.setId(rs.getInt(1));
				productmanagement.setName(rs.getString(2));
				productmanagement.setDescription(rs.getString(3));
				productmanagement.setWarehouse_id(rs.getString(4));

			}

		} catch (Exception e) {

		}
		return productmanagement;
	}

	public Product getProductCatalogueDetails(String catalogueid) {

		Product product = new Product();

		try {
			String sql = "SELECT id, categoryid, subcategoryid, product_name, mrp, purchase_price, sale_price, gst, datetime, location, lastmodified, genericname,pack,hsnno,mfg,shedule,description,minorder,maxorder,product_code from inventory_catalogue where id="
					+ catalogueid + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				product.setId(rs.getInt(1));
				product.setCategory_id(rs.getString(2));
				product.setSubcategory_id(rs.getString(3));
				product.setProduct_name(rs.getString(4));
				product.setMrp(rs.getString(5));
				product.setPurchase_price(DateTimeUtils.changeFormat(rs.getDouble(6)));
				product.setSale_price(rs.getString(7));
				product.setVat(""+rs.getInt(8));
				product.setDateTime(rs.getString(9));
				product.setLocation(rs.getString(10));
				product.setLastmodified(rs.getString(11));
				product.setGenericname(rs.getString(12));
				product.setPack(DateTimeUtils.numberCheck1(rs.getString(13)));
				product.setHsnno(rs.getString(14));
				product.setMfg(rs.getString(15));
				product.setMedicine_shedule(rs.getString(16));
				product.setDescription(rs.getString(17));
				product.setMinorder(DateTimeUtils.numberCheck(rs.getString(18)));
				product.setMaxorder(DateTimeUtils.numberCheck(rs.getString(19)));
				product.setCatalogueid(catalogueid);
				product.setPro_code(rs.getString(20));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return product;
	}

	public ArrayList<Product> getAllTermsAndCondition() {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select id,term from terms_and_condition";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Product product = new Product();
				product.setId(resultSet.getInt(1));
				product.setName(resultSet.getString(2));
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Product> getItemWiseStockReportList(String fromdate, String todate, String product_id,
			String location) {

		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			
				InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
				StringBuffer buffer = new StringBuffer();
				
						buffer.append("select inventory_procurement.id, prodid, purprice, inventory_procurement.qty, total,  " );
						buffer.append("inventory_procurement.mrp, date, inventory_procurement.vendorid, procurementid, voucherno, voucherdate, ");
						buffer.append("inventory_procurement.location, discount,p_time, p_userid, grnno,inventory_product.id,inventory_procurement.vendorid, ");
						buffer.append("batch_no,prodname,inventory_product.purchaseprice,inventory_product.vat,inventory_product.expiry_date ");
						buffer.append("from inventory_procurement "); 
						buffer.append("inner join inventory_parent_procurement on inventory_parent_procurement.id = inventory_procurement.procurementid ");
						buffer.append("inner join inventory_product on inventory_product.id=inventory_procurement.prodid ");
						buffer.append("where inventory_procurement.date between '" + fromdate + "' and '" + todate+"' and inventory_procurement.qty>0 and confirm=1  and gudreceipt=1 and isdelivermemo=0 and prodid is not null and inventory_procurement.voucherno is NOT NULL ");
						if (!product_id.equals("")) {
							buffer.append("and prodname like ('%" + product_id + "%') ");
						}
						PreparedStatement preparedStatement1 = connection.prepareStatement(buffer.toString());
						ResultSet rs1 = preparedStatement1.executeQuery();
						while (rs1.next()) {
							Product product2 = new Product();
							//Product product3 = getProduct(rs1.getString(17));
							product2.setId(rs1.getInt(1));
							product2.setProduct_id(rs1.getString(2));
							product2.setPurchase_price(rs1.getString(3));
							product2.setQty(rs1.getString(4));
							product2.setTotal(rs1.getString(5));
							product2.setMrp(rs1.getString(6));
							product2.setDate(rs1.getString(7));
							product2.setVendor_id(rs1.getString(8));
							product2.setProcurementid(rs1.getString(9));
							product2.setVoucherno(rs1.getString(10));
							product2.setVoucherdate(rs1.getString(11));
							product2.setLocation(rs1.getString(12));
							product2.setDiscount(rs1.getString(13));
							product2.setTime(rs1.getString(14));
							product2.setUserid(rs1.getString(15));
							product2.setGrnno(rs1.getInt(16));
							product2.setBatch_no(rs1.getString(19));
							product2.setProd_name(rs1.getString(20));
							Vendor vendor = inventoryVendorDAO.getVendor(rs1.getString(18));
							product2.setVendor(vendor.getName());
							double vat = rs1.getDouble(21)* (rs1.getDouble(22) / 100.0);
							// product2.setVat(product3.getVat());
							double netvat = rs1.getDouble(21) + vat;
							product2.setNetVal("" + Math.round(netvat * 100.0) / 100.0);
							product2.setExpiry_date(DateTimeUtils.getCommencingDate1(rs1.getString(23)));
							product2.setVat("" + Math.round(vat * 100.0) / 100.0);
							product2.setGrossVat(rs1.getString(22));
							arrayList.add(product2);
				
							
						}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	/*
	 * private ArrayList<Product> getProcurmentDataByProdId(String id, String
	 * supplierid, String batchno) { ArrayList<Product> arrayList = new
	 * ArrayList<Product>(); try { String sql =
	 * "select id, prodid, purprice, qty, total, mrp, date, vendorid, procurementid, voucherno, voucherdate, location, discount,  p_time, p_userid, grnno from inventory_procurement where prodid='"
	 * +id+"' and vendorid='"+supplierid+"' "; PreparedStatement
	 * preparedStatement = connection.prepareStatement(sql); ResultSet rs =
	 * preparedStatement.executeQuery(); while (rs.next()) { Product product =
	 * new Product(); product.setProcurementid(rs.getString(1));
	 * 
	 * } } catch (Exception e) { e.printStackTrace(); } return arrayList; }
	 */
	public int getTotalStockBatchWise(String prodname, String batchno) {
		int stock = 0;
		try {

			String sql = "select sum(stock) from inventory_product where prodname like ('%" + prodname
					+ "%') and batch_no = '" + batchno + "';";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				stock = rs.getInt(1);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return stock;
	}

	public ArrayList<Product> getProductListForItemWiseStockReport(String categoryid, String location) {

		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String sql = "";

			/*
			 * if(!location.equals("0")){
			 * sql="select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno from inventory_product where categoryid='"
			 * +categoryid+"' and stock>=0 and location='"
			 * +location+"' group by prodname order by prodname asc "; } else {
			 * sql="select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno from inventory_product where categoryid='"
			 * +categoryid+"' and stock>=0 group by prodname order by prodname asc "
			 * ;
			 * 
			 * }
			 */

			if (!location.equals("0")) {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno from inventory_product where location='"
						+ location + "' group by prodname order by prodname asc ";
			} else {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno from inventory_product  group by prodname order by prodname asc ";

			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));
				product.setLastmodified(rs.getString(21));

				String genericname = rs.getString(22);
				if (genericname == null) {
					genericname = "GEN";
				}

				String medicinenameid = rs.getString(23);
				product.setBatch_no(rs.getString(24));
				product.setHsnno(rs.getString(25));
				if (product.getHsnno() == null) {
					product.setHsnno("");
				}
				product.setMedicinenameid(medicinenameid);
				// Product medicineData=
				// getMedicineProductDetails(medicinenameid);

				String data = product.getProduct_name() + "-" + genericname + "- (" + product.getBatch_no() + "/"
						+ product.getHsnno() + ") (" + product.getExpiry_date() + ") (Rs." + product.getSale_price()
						+ ") (" + product.getStock() + ")  ";
				product.setGenericname(data);

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				boolean flag = false;
				Calendar expireCalendar = Calendar.getInstance();
				String expirydate = rs.getString(17);
				if (expirydate != null) {

					if (!expirydate.equals("")) {
						Date date = format.parse(expirydate);
						expireCalendar.setTime(date);
						long res = getExpiryDaysBetween(expireCalendar.getTime(), calendar.getTime());
						if (res > 0) {
							flag = true;
						}
					}
				}
				if (flag) {
					list.add(product);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public int addNewProduct(Product product) {
		int result = 0;
		try {
			if(product.getMaxorder()!=null){
				if(product.getMaxorder().equals("")){
					product.setMaxorder("0");
				}
			}else{
				product.setMaxorder("0");
			}
			if(product.getMinorder()!=null){
				if(product.getMinorder().equals("")){
					product.setMinorder("0");
				}
			}else{
				product.setMinorder("0");
			}
			String fromdate = "";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			fromdate = dateFormat.format(cal.getTime());
			fromdate = DateTimeUtils.getCommencingDate1(fromdate);
			String sql = "insert into inventory_catalogue (categoryid, subcategoryid, product_name, mrp, purchase_price, sale_price, gst, datetime, location, lastmodified, genericname, pack, mfg,shedule,description,hsnno,minorder,maxorder,product_code) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getCategory_id());
			ps.setString(2, product.getSubcategory_id());
			ps.setString(3, product.getProduct_name());
			ps.setString(4, product.getMrp());
			ps.setString(5, product.getPurchase_price());
			ps.setString(6, product.getSale_price());
			ps.setString(7, product.getVat());
			ps.setString(8, product.getDate());
			ps.setString(9, product.getLocation());
			ps.setString(10, fromdate);
			ps.setString(11, product.getGeneric_name());
			ps.setString(12, product.getPack());
			ps.setString(13, product.getMfg());
			ps.setString(14, product.getProdtype());
			ps.setString(15, product.getDescription());
			ps.setString(16, product.getHsnno());
			ps.setString(17, product.getMinorder());
			ps.setString(18, product.getMaxorder());
			ps.setString(19, product.getPro_code());
			result = ps.executeUpdate();

			if (result > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				while (rs.next()) {
					result = rs.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getProductListForItemWiseSaleReport(String categoryid, String location) {
		ArrayList<Product> list = new ArrayList<Product>();
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String sql = "";

			if (!location.equals("0")) {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno from inventory_product where categoryid='"
						+ categoryid + "' and stock>=0 and location='" + location + "' order by prodname asc ";
			} else {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno from inventory_product where categoryid='"
						+ categoryid + "' and stock>=0 order by prodname asc ";

			}
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));
				product.setLastmodified(rs.getString(21));

				String genericname = rs.getString(22);
				if (genericname == null) {
					genericname = "GEN";
				}

				String medicinenameid = rs.getString(23);
				product.setBatch_no(rs.getString(24));
				product.setHsnno(rs.getString(25));
				if (product.getHsnno() == null) {
					product.setHsnno("");
				}
				product.setMedicinenameid(medicinenameid);
				// Product medicineData=
				// getMedicineProductDetails(medicinenameid);

				String data = product.getProduct_name() + "-" + genericname + "- (" + product.getBatch_no() + "/"
						+ product.getHsnno() + ") (" + product.getExpiry_date() + ") (Rs." + product.getSale_price()
						+ ") (" + product.getStock() + ")  ";
				product.setGenericname(data);

				Product category = getCategory(product.getCategory_id());
				product.setCategory(category.getName());

				Product subcategory = getSubCategory(product.getSubcategory_id());
				product.setSubcategory(subcategory.getName());

				Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());

				product.setMin_delivery_time(vendor.getMin_delivery_time());
				product.setVendor(vendor.getName());

				Product brand = getBrandName(product.getBrand_id());
				product.setBrand(brand.getName());

				boolean flag = false;
				Calendar expireCalendar = Calendar.getInstance();
				String expirydate = rs.getString(17);
				if (expirydate != null) {

					if (!expirydate.equals("")) {
						Date date = format.parse(expirydate);
						expireCalendar.setTime(date);
						long res = getExpiryDaysBetween(expireCalendar.getTime(), calendar.getTime());
						if (res > 0) {
							flag = true;
						}
					}
				}
				if (flag) {
					list.add(product);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public int updateProductStockStatus(Product product) {

		int result = 0;
		try {

			String sql = "update inventory_product set stock=?,expiry_date=?,batch_no=?,cell=?,mrp=?,saleprice=?,pack=?,purchaseprice=?,vat=?,barcode=? where id=" + product.getId()
					+ "";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getStock());
			ps.setString(2, product.getExpiry_date());
			ps.setString(3, product.getBatch_no());
			ps.setString(4, product.getShelf());
			ps.setString(5, product.getMrp());
			ps.setString(6, product.getSale_price());
			ps.setString(7, product.getPack());
			ps.setString(8, product.getPurchase_price());
			ps.setString(9, product.getVat());
			ps.setString(10, product.getBarcode());
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int checkForProcurmentItemReturn(String searchText, String fromdate, String todate, String location,
			String filter_status, Pagination pagination) {
		
		try {

			StringBuffer buffer = new StringBuffer();
			boolean flag = false;
			
			buffer.append("select id,indentreq,req_or_trans from inventory_parent_transfer_log ");
			if (searchText != null) {
				flag = true;
				buffer.append("where id ='" + searchText + "'  ");
			}
			if (flag) {
				buffer.append("and  request_date between '" + fromdate + "' and '" + todate + "' ");
			} else {
				buffer.append("where request_date between '" + fromdate + "' and '" + todate + "'  ");
			}

			if (location.equals("32")) {

			} else if (!location.equals("0")) {
				buffer.append("and from_location=" + location + " ");
			}

			if (!filter_status.equals("10")) {
				if (filter_status.equals("8")) {
					buffer.append("and req_or_trans=1 ");
				} else {
					buffer.append("and r_status=" + filter_status + " ");
				}
			}
			buffer.append("order by request_date,time desc");

			/*
			 * String sql1=pagination.getSQLQuery(buffer.toString());
			 * PreparedStatement preparedStatement =
			 * connection.prepareStatement(sql1);
			 */
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String parentid = rs.getString(1);
				String indentreq = rs.getString(2);
				String req_or_trans = rs.getString(3);
				if (req_or_trans == null) {
					req_or_trans = "1";
				}
				if (req_or_trans.equals("0")) {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(
							"select inventory_parent_transfer_log.id,inventory_parent_transfer_log.indentreq,inventory_transfer_log.old_prodid from inventory_parent_transfer_log ");
					stringBuilder.append(
							"inner join inventory_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
					stringBuilder.append("where inventory_transfer_log.status = 0 and inventory_transfer_log.parentid ="
							+ parentid + " ");
					PreparedStatement preparedStatement2 = connection.prepareStatement(stringBuilder.toString());
					ResultSet rs1 = preparedStatement2.executeQuery();
					while (rs1.next()) {
						int res = checkInTempPO(rs1.getString(1), rs1.getString(2), rs1.getString(3));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int checkInTempPO(String parentid, String indentreq, String prodid) {
		int res = 0;
		try {
			Product product = getProduct(prodid);
			String sql = "select id from temp_po where prod_name like ('" + product.getProduct_name()
					+ "') and parentid='" + parentid + "' and status=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0) {
					int result = checkProcurmentStatus(parentid, indentreq, prodid, rs.getString(1),
							product.getProduct_name());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int checkProcurmentStatus(String parentid, String indentreq, String prodid, String temp, String prodname) {
		int res = 0;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(
					"select inventory_procurement.id,inventory_procurement.prodid from inventory_procurement inner join temp_po ");
			builder.append(
					"on inventory_procurement.longpo=temp_po.id where inventory_procurement.gudreceipt=1 and temp_po.indent_no='"
							+ indentreq + "'");
			PreparedStatement preparedStatement = connection.prepareStatement(builder.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int procurmentid = rs.getInt(1);
				String newprodid = rs.getString(2);
				Product product = getProduct(newprodid);
				if (product.getProduct_name().equals(prodname)) {
					String query = "select r_status from inventory_parent_transfer_log where id='" + parentid + "'";
					PreparedStatement statement2 = connection.prepareStatement(query);
					ResultSet resultSet = statement2.executeQuery();
					String status = "";
					while (resultSet.next()) {
						if (resultSet.getString(1) == null) {

						} else if (!resultSet.getString(1).equals("")) {
							status = resultSet.getString(1);
						}
					}
					if (!status.equals("3")) {
						String sql = "update inventory_parent_transfer_log set r_status=7,procurmentid='" + procurmentid
								+ "' where id=" + parentid + "";
						PreparedStatement statement = connection.prepareStatement(sql);
						res = statement.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int addNewProductToVendor(String location, int prodid) {
		int result = 0;
		try {
			String sql = "";
			if (!location.equals("0")) {
				sql = "select id,products from inventory_vendor where location='" + location + "'  ";
			} else {
				sql = "select id,products from inventory_vendor";
			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String temp = rs.getString(2) + "," + prodid;
				String sql2 = "update inventory_vendor set products='" + temp + "' where id=" + rs.getInt(1) + "";
				PreparedStatement statement = connection.prepareStatement(sql2);
				result = statement.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Master> getAllLocation(String searchText) {
		ArrayList<Master> users = new ArrayList<Master>();
		try {
			String sql = "";
			if (searchText != null) {
				sql = "select id,name from apm_condition where pharmacy=1 and name like ('" + searchText + "%') ";
			} else {
				sql = "select id,name from apm_condition where pharmacy=1";
			}

			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Master locationMaster = new Master();
				locationMaster.setId(rs.getInt(1));
				locationMaster.setName(rs.getString(2));
				users.add(locationMaster);
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	public int saveParentProductReturn(String firstlocation, String tolacation, String todate, String time,
			String comment1, String userid, int indentcount) {
		int result = 0;
		try {
			String sql = "insert into inventory_parent_transfer_log (request_date, issued_date, from_location, to_location,time,req_or_trans,remark,userid,trans_indent_no,r_status) values (?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, todate);
			preparedStatement.setString(2, todate);
			preparedStatement.setString(3, firstlocation);
			preparedStatement.setString(4, tolacation);
			preparedStatement.setString(5, time);
			preparedStatement.setString(6, "2");
			preparedStatement.setString(7, comment1);
			preparedStatement.setString(8, userid);
			preparedStatement.setString(9, "" + indentcount);
			preparedStatement.setString(10, "9");
			result = preparedStatement.executeUpdate();
			if (result > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();

				while (resultSet.next()) {
					result = resultSet.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getProdidFromCatagoryid(String cataloguid) {
		String id = "0";
		try {
			String sql = "select id from inventory_product where catalogueid='" + cataloguid
					+ "' order by id limit 0,1;";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				id = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public ArrayList<Product> getIndentTransferLog(String fromdate, String todate, String location,
			String filter_status, String location_filter, String searhText, Pagination pagination) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append(
					"select id, request_date, issued_date, from_location, to_location,time,r_status,req_or_trans, admin_notes, head_notes, check_availability_date, admin_aprove_date, head_aprove_date,expected_date,userid,admin_approve_userid,indentreq,trans_indent_no from inventory_parent_transfer_log ");
			buffer.append("where request_date between '" + fromdate + "' and '" + todate + "' and deleted=0 ");

			if (!filter_status.equals("10")) {
				buffer.append("and req_or_trans ='" + filter_status + "' ");
			}
			if (searhText != null) {
				buffer.append("and id ='" + searhText + "' ");
			}

			if (!location_filter.equals("0")) {
				if (!filter_status.equals("10")) {
					if (filter_status.equals("0")) {
						buffer.append("and from_location ='" + location_filter + "' ");
					} else {
						buffer.append("and to_location ='" + location_filter + "' ");
					}
				} else {
					buffer.append("and to_location ='" + location_filter + "' ");
				}

			}

			buffer.append(" order by request_date,time asc ");
			/*
			 * if(!location_filter.equals("10")){
			 * if(location_filter.equals("0")){
			 * 
			 * }else if(location_filter.equals("1")){
			 * buffer.append("and warehouseid ='"+filter_status+"' "); }else
			 * if(location_filter.equals("2")){
			 * buffer.append("and warehouseid='"+filter_status+"' "); } }
			 */
			String sql = pagination.getSQLQuery(buffer.toString());
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setParentid("" + rs.getInt(1));
				String request_date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				String issued_date = DateTimeUtils.getCommencingDate1(rs.getString(3));
				String time = rs.getString(6);
				product.setRequest_date(request_date + " " + time);
				product.setIssued_date(issued_date + " " + time);
				// product.setRequest_date(rs.getString(2));
				// product.setIssued_date(rs.getString(3));
				String fromlocation = pharmacyLocationNameByID(rs.getString(4));
				String tolocation = pharmacyLocationNameByID(rs.getString(5));
				product.setStatus(rs.getString(7));
				product.setFrom_location(fromlocation);
				product.setTo_location(tolocation);
				product.setReq_or_transfer(rs.getString(8));
				String adminnotes = "", headnotes = "", check_availabity_date = "", admin_aprove_date = "",
						head_aprove_date = "";
				if (rs.getString(9) != null) {
					adminnotes = rs.getString(9);
				}
				if (rs.getString(10) != null) {
					headnotes = rs.getString(10);
				}
				if (rs.getString(11) != null) {
					check_availabity_date = rs.getString(11);
				}
				if (rs.getString(12) != null) {
					admin_aprove_date = rs.getString(12);
					String[] data = admin_aprove_date.split(" ");
					String date = DateTimeUtils.getCommencingDate1(data[0]);
					admin_aprove_date = date + " " + data[1];
				}
				if (rs.getString(13) != null) {
					head_aprove_date = rs.getString(13);
					String[] data = head_aprove_date.split(" ");
					String date = DateTimeUtils.getCommencingDate1(data[0]);
					head_aprove_date = date + " " + data[1];
				}
				product.setAdmin_notes(adminnotes);
				product.setHead_notes(headnotes);
				product.setCheck_availability_date(check_availabity_date);
				product.setAdmin_aprove_date(admin_aprove_date);
				product.setHead_aprove_date(head_aprove_date);
				product.setExpectedDate(rs.getString(14));

				Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(rs.getString(15));

				if (priscription.getFullname() != null) {
					product.setUserid(priscription.getFullname());
				} else {
					product.setUserid(rs.getString(15));
				}

				product.setAdmin_approve_userid(rs.getString(16));
				product.setIndent(rs.getInt(17));
				product.setTransfer_indentno(rs.getString(18));
				if (filter_status.equals("10")) {
					if (rs.getInt(8) != 0) {
						arrayList.add(product);
					} else {
						ArrayList<Product> arrayList2 = getRequestTranferIndent(rs.getString(1));
						if (arrayList2.size() > 0) {
							product.setTransferproductlist(arrayList2);
							arrayList.add(product);
						}
					}
				} else if (filter_status.equals("1")) {
					// ArrayList<Product> arrayList2 =
					// getChildTranfserData(rs.getString(1));
					// product.setTransferproductlist(arrayList2);
					arrayList.add(product);
				} else if (filter_status.equals("2")) {
					// ArrayList<Product> arrayList2 =
					// getChildTranfserData(rs.getString(1));
					// product.setTransferproductlist(arrayList2);
					arrayList.add(product);
				} else {
					ArrayList<Product> arrayList2 = getRequestTranferIndent(rs.getString(1));
					if (arrayList2.size() > 0) {
						product.setTransferproductlist(arrayList2);
						arrayList.add(product);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;

	}

	public ArrayList<Product> getRequestTranferIndent(String parentid) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select id, parentid, old_prodid, new_prodid, qty,location,transfer_date from inventory_request_temp_log where parentid="
					+ parentid + " and status!=0";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			double total_amount = 0;
			double totolmrp = 0;

			while (rs.next()) {
				Product product = new Product();
				product.setChildid("" + rs.getInt(1));
				product.setParentid(rs.getString(2));
				product.setProduct_id(rs.getString(3));
				Product product2 = getProduct(rs.getString(3));
				product.setHsnno(product2.getHsnno());
				product.setProduct_name(product2.getProduct_name());
				product.setBatch_no(product2.getBatch_no());
				String expirydate = DateTimeUtils.getCommencingDate1(product2.getExpiry_date());
				product.setExpiry_date(expirydate);
				product.setStock(rs.getString(5));

				if (product2.getPack() == null) {
					product2.setPack("1");
				}
				if (product2.getPack().equals("")) {
					product2.setPack("1");
				}
				double amt = Double.parseDouble(product2.getPurchase_price()) / Integer.parseInt(product2.getPack());
				double mrpamt = Double.parseDouble(product2.getMrp()) / Integer.parseInt(product2.getPack());
				double unit1 = Math.round(amt * 100.0) / 100.0;
				double unit2 = Math.round(mrpamt * 100.0) / 100.0;
				product.setSale_price("" + unit1);
				product.setMrp("" + unit2);
				product.setUnit("" + unit1);
				double amountno = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getSale_price());
				double amountmrp = Integer.parseInt(rs.getString(5)) * Double.parseDouble(product.getMrp());
				total_amount = total_amount + amountno;
				totolmrp = totolmrp + amountmrp;
				product.setTotal_amount(Math.round(total_amount * 100.0) / 100.0);
				product.setTotolmrp(Math.round(totolmrp * 100.0) / 100.0);
				product.setAmountno(Math.round(amountno * 100.0) / 100.0);
				product.setAmountmrp(Math.round(amountmrp * 100.0) / 100.0);
				product.setOld_location(rs.getString(6));
				product.setFrom_location(pharmacyLocationNameByID(product2.getLocation()));

				String transfer_date = rs.getString(7);
				if (transfer_date != null) {
					if (!transfer_date.equals("")) {
						String[] data = transfer_date.split(" ");
						transfer_date = DateTimeUtils.getCommencingDate1(data[0]);
						transfer_date = transfer_date + " " + data[1];
					}
				} else {
					transfer_date = "";
				}
				product.setTransfer_date(transfer_date);

				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int updateProcurmentStatus(String procurementid, String status) {
		int res = 0;
		try {
			String sql2 = "update inventory_procurement set istransfer='" + status + "' where id=" + procurementid + "";
			PreparedStatement statement = connection.prepareStatement(sql2);
			res = statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int deleteProductStock(String id) {

		int res = 0;
		try {

			String sql = "delete from inventory_product where id=" + id + "";
			PreparedStatement ps = connection.prepareStatement(sql);
			res = ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public int updateTOZeroStock(String product_id) {

		int res = 0;
		try {

			String sql = "update inventory_product set stock=0 where id=" + product_id + " ";
			PreparedStatement ps = connection.prepareStatement(sql);
			res = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return res;
	}

	public int cancelIndent(String id) {
		int result = 0;
		try {
			String sql = "update inventory_parent_transfer_log set deleted=1 where id="+id+"";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Product> getAscStockWiseProductList(int req_stock, Product prod) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			/*String sql = "select id, prodname, stock, expiry_date, batch_no, location, hsnno from inventory_product where location=32 and stock>0 and prodname like ('"
					+ prod.getProduct_name() + "') order by (stock+0) asc";*/
			String sql = "select id, prodname, stock, expiry_date, batch_no, location, hsnno from inventory_product where location=32 and stock>0 and prodname like ('"
					+ prod.getProduct_name() + "') order by (stock+0) asc";
			// String sql ="select id, prodname, stock, expiry_date, batch_no,
			// location, hsnno from inventory_product where location=1 and
			// stock>0 and prodname ='"+prod.getProduct_name()+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int  total = 0;
			int reqqty = req_stock;
			int temp=0;
			while (rs.next()) {
				int avail_stock = Integer.parseInt(rs.getString(3));
				Product product = new Product();
				product.setAvail_stock(avail_stock);
				product.setProduct_id("" + rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setStock(rs.getString(3));
				product.setExpiry_date(rs.getString(4));
				product.setBatch_no(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setHsnno(rs.getString(7));
				String fromlocation = pharmacyLocationNameByID(rs.getString(6));
				product.setFromlocation(fromlocation);
				product.setReqqty("" + req_stock);
				/*if (avail_stock >= req_stock) {
					arrayList.add(product);
				}*/
				total = total + avail_stock;
				product.setTotalbalance(total);
				if(total>=req_stock){
					temp = reqqty-avail_stock;
					if(temp>=0){
						product.setStockqty(avail_stock);
						reqqty = temp;
					}else{
						product.setStockqty(reqqty);
					}
					arrayList.add(product);
					break;
				}else{
					temp = reqqty-avail_stock;
					if(temp>=0){
						product.setStockqty(avail_stock);
						reqqty = temp;
					}else{
						product.setStockqty(reqqty);
					}
					arrayList.add(product);
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public ArrayList<Product> getProductLimitedFromOtherLocation(Product prod, String location) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			String sql = "select id, prodname, stock, expiry_date, batch_no, location, hsnno from inventory_product where location="
					+ location + " and stock>0 and prodname like ('" + prod.getProduct_name() + "')";
			// String sql ="select id, prodname, stock, expiry_date, batch_no,
			// location, hsnno from inventory_product where
			// location="+location+" and stock>0 and prodname
			// ='"+prod.getProduct_name()+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int  total = 0;
			int reqqty = Integer.parseInt(prod.getStock()); 
			int temp=0;
			while (rs.next()) {
				int avail_stock = Integer.parseInt(rs.getString(3));
				Product product = new Product();
				product.setAvail_stock(avail_stock);
				product.setProduct_id("" + rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setStock(rs.getString(3));
				product.setExpiry_date(rs.getString(4));
				product.setBatch_no(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setHsnno(rs.getString(7));
				product.setReqqty(prod.getStock());
				String fromlocation = pharmacyLocationNameByID(rs.getString(6));
				product.setFromlocation(fromlocation);
				total = total + avail_stock;
				product.setTotalbalance(total);
				if(total>=Integer.parseInt(prod.getStock())){
					temp = reqqty-avail_stock;
					if(temp>=0){
						product.setStockqty(avail_stock);
						reqqty = temp;
					}else{
						product.setStockqty(reqqty);
					}
					arrayList.add(product);
					break;
				}else{
					temp = reqqty-avail_stock;
					if(temp>=0){
						product.setStockqty(avail_stock);
						reqqty = temp;
					}else{
						product.setStockqty(reqqty);
					}
					arrayList.add(product);
				}
				
				
				//arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	
	public ArrayList<Product> getSaleReturnReport(String fromdate, String todate, String location, String salereturun,
			String billtype, Pagination pagination) {

		ArrayList<Product> list = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		try {
			StringBuffer buffer = new StringBuffer();

			buffer.append(
					"select apm_medicine_bill.id,apm_medicine_bill.date,debit,apm_medicine_bill.clientid,apm_medicine_bill.pclientid,apm_medicine_bill.location,cgst,sgst,time,isreturn,priscid,discount,initial_paymode,oldparentid,refundamt from apm_medicine_bill   ");
			//comment by Akash to show 1 invoice at time 
			//buffer.append("inner join apm_medicine_payment on apm_medicine_payment.billno = apm_medicine_bill.id ");
			buffer.append("where apm_medicine_bill.date between '" + fromdate + "' and '" + todate + "' ");

			/*
			 * if(!billtype.equals("0")){ buffer= new StringBuffer(); buffer.
			 * append("select apm_medicine_bill.id,apm_medicine_bill.date,debit,apm_medicine_bill.clientid,apm_medicine_bill.pclientid,apm_medicine_bill.location,cgst,sgst,time,isreturn,priscid,paymode  "
			 * ); buffer.
			 * append(" from apm_medicine_bill inner join apm_medicine_payment on "
			 * ); buffer.
			 * append(" apm_medicine_bill.id = apm_medicine_payment.billno where "
			 * ); buffer.append("apm_medicine_payment.date between '"
			 * +fromdate+"' and '"+todate+"' and paymode='"+billtype+"' "); }
			 */
			
			//this may be not working so commented for some time @Akash
			/*if (!billtype.equals("0")) {
				buffer.append("and paymode='" + billtype + "' ");
			}*/
			if(billtype==null){
				billtype="0";
			}else if(billtype.equals("")){
				billtype="0";
			}

			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}

			if (salereturun.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}

			if (salereturun.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}
			
			if(!billtype.equals("0")){
				buffer.append(" and  apm_medicine_bill.initial_paymode='"+billtype+"' ");
			}
			buffer.append("and apm_medicine_bill.tpid='0' ");
			buffer.append(" and apm_medicine_bill.deleted='0' ");
			buffer.append("order by apm_medicine_bill.id desc ");
			String sql1 = pagination.getSQLQuery(buffer.toString());
			PreparedStatement ps = connection.prepareStatement(sql1);
			int totaldebit=0;
			ResultSet rs = ps.executeQuery();
			double tot = 0;
			double gst=0;
			double finaldeciamount=0;
			double ttldisc=0;
			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setDate(rs.getString(2));
				product.setDebit(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3)*100.00)/100.00));
				product.setClientid(rs.getString(4));
				product.setPclientid(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setCgst(DateTimeUtils.changeFormat(Math.round(rs.getDouble(7)*100.00)/100.00));
				product.setSgst(DateTimeUtils.changeFormat(Math.round(rs.getDouble(8)*100.00)/100.00));
				product.setGstamount(String.valueOf(Double.parseDouble(product.getCgst())+gst));
				gst=Double.parseDouble(product.getGstamount());
				product.setTime(rs.getString(9));
				product.setIsreturn(rs.getInt(10));
				product.setPriscid(rs.getString(11));
				if(product.getIsreturn()==1&&(rs.getInt(3)==0)){
					product.setDebit(DateTimeUtils.changeFormat(Math.round(rs.getDouble("refundamt")*100.00)/100.00));	
				}
				double amt=DateTimeUtils.convertToDouble(product.getDebit());
				int xyzamt=(int)amt;
				// double payAmt= getTotalReceived(product.getId());
				product.setTotaldebit(xyzamt+totaldebit);
				totaldebit= product.getTotaldebit();
				//@Akash get payment by other function
				//double payAmt = rs.getDouble(12);
				//tot = tot + payAmt;
				//product.setPayment(tot);
				//product.setPayAmount(DateTimeUtils.changeFormat(payAmt));
				
				// if(!billtype.equals("0")){
					// this comment
					//product.setBilltype(rs.getString(13));
				// }

				double decibillamount=0;/*pharmacyDAO.getDecimalBillAmount(rs.getString(1));*/
				product.setDecimalbillamount(Math.round(decibillamount*100.00)/100.00);
				product.setTotaldecimalbillamount(decibillamount+finaldeciamount);
				finaldeciamount= product.getTotaldecimalbillamount();
				double payment = getPaymentInfo(rs.getInt(1));
				String billtype1 = getPaymentModeInfo(rs.getInt(1));
				tot = tot + payment;
				product.setPayment(tot);
				product.setPayAmount(DateTimeUtils.changeFormat(Math.round(payment*100.00)/100.00));
				product.setBilltype(rs.getString(13));
				
				String date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				product.setDateTime(date + " " + product.getTime());

				double temp = Double.parseDouble(product.getSgst()) * 2;
				double withoutGST = DateTimeUtils.convertToDouble(product.getDebit()) - temp;
				product.setTotal(DateTimeUtils.changeFormat(Math.round(withoutGST*100.00)/100.00));
				if (product.getIsreturn() > 0) {
					product.setType("Sales Return");
				} else {
					product.setType("Sales");
				}
				
				
				if (!product.getPclientid().equals("0")) {

					Priscription priscription = pharmacyDAO.getPharmacyPatient(product.getPclientid());
					product.setClienttype("-");
					product.setTpname("NO");
					product.setDoctor(priscription.getPractitionername());
					product.setClientname(priscription.getFullname());
				} else {
					if(rs.getInt(14)>0){
						boolean flag = pharmacyDAO.checkIpdPatientStatus(rs.getInt(14));
						if(flag){
							product.setClienttype("IPD");
						}else{
							product.setClienttype("OPD");
						}
					}else{
						product.setClienttype("OPD");
					}
					/*String isipdid = pharmacyDAO.getIpdIdFromClientID(rs.getInt(4));
					if (isipdid.equals("0")) {
						product.setClienttype("OPD");
					} else {
						product.setClienttype("IPD");
					}*/
					
					Client client = clientDAO.getClientDetails(product.getClientid());
					if (client.getTypeName() != null) {
						if (!client.getTypeName().equals("0")) {
							product.setTpname("YES");
						} else {
							product.setTpname("NO");
						}
					} else {
						product.setTpname("NO");
					}
					product.setClientname(client.getTitle() + " " + client.getFirstName() + " " + client.getLastName());
					if (!product.getPriscid().equals("0")) {
						Priscription priscription = emrDAO.getEditPriscription(product.getPriscid());
						UserProfile userProfile = userProfileDAO
								.getUserprofileDetails(Integer.parseInt(priscription.getPrectionerid()));
						product.setDoctor(userProfile.getFullname());
					} else {
						product.setDoctor(" ");
					}

				}
				
				product.setDiscount(DateTimeUtils.changeFormat(Math.round(rs.getDouble(12)*100.00)/100.00));
				product.setTotaldiscount(Math.round((Double.parseDouble(product.getDiscount())+ttldisc)*100.0)/100.0);
				ttldisc=product.getTotaldiscount();
				if(billtype1==null){
					billtype1="";
				}
//				if(!paymode.equals("")){
//					if(paymode.equals("Cash")&&billtype1.equals("Cash")){
//						list.add(product);
//					}else if(paymode.equals("Card")&&billtype1.equals("Card")){
//						list.add(product);
//					}else if(paymode.equals("Credit")){
//						list.add(product);
//					}
//				}else{
					list.add(product);	
//				}
				
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}
	public double getPaymentInfo(int id){
		double payment =0.0;
		try {
			String sql ="select sum(payment) from apm_medicine_payment where billno='"+id+"'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				payment = rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payment;
	}
	public String getPaymentModeInfo(int id){
		String name = "";
		try {
			String sql ="select paymode from apm_medicine_payment where billno='"+id+"'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				name = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	public boolean isPharamcistOrNot(String userid) {
		boolean flag = false;
		try {
			String sql ="select id from apm_pharmacy_user where userid='"+userid+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public String getHISUserLocation(String userid) {
		String location="";
		try {
			String sql ="select discription from apm_user where userid='"+userid+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if(rs.getString(1)==null){
					location="0";
				}else if(rs.getString(1).equals("")){
					location="0";
				}else{
					String sql1 ="select id from apm_condition where speciality='"+rs.getString(1)+"'";
					PreparedStatement preparedStatement2 = connection.prepareStatement(sql1);
					ResultSet rs1 = preparedStatement2.executeQuery();
					while (rs1.next()) {
						location = rs1.getString(1);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}
	static int balanceqty=0;
	public ArrayList<Product> getBinCardReport(String location, String catalogueid, String fromdate, String todate,String abbrivation,String userwise, String location_filter) {
       
		ArrayList<Product> list= new ArrayList<Product>();
		try {
			todate = todate+" "+"59:59:59";
			balanceqty=0;
			InventoryVendorDAO inventoryVendorDAO=new JDBCInventoryVendorDAO(connection);
			//InventoryCatalogueDAO catalogueDAO= new JDBCInventoryCatalogueDAO(connection);
			ProcurementDAO procurementDAO = new  JDBCProcurementDAO(connection);
			//Product pmaster= catalogueDAO.getProductDetails(catalogueid);
			StringBuffer buffer= new StringBuffer();
			buffer.append("select prodid,date,voucherno,vendorid,qty,procurementid,location from inventory_procurement where catalogueid="+catalogueid+" and date between '"+fromdate+"' and '"+todate+"' and confirm=1 and deleted=0 and gudreceipt=1 and iscancel=0 and (inventory_procurement.prodid!=0 or inventory_procurement.prodid is not null)  order by id ");
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
					int printid = 0;
					String link="";
				    Product product=new Product();
					String prodid= rs.getString(1);
					String date= rs.getString(2);
					String voucherno= rs.getString(3);
					String vendorid= rs.getString(4);
					String qty= rs.getString(5);
					product.setProduct_id(prodid);
					product.setDate(date);
					product.setVoucherno(voucherno);
					product.setDocno(voucherno);
					product.setVendor_id(vendorid);
					product.setIssueqty("-");
					product.setQty(qty);
					
					Vendor vendor= inventoryVendorDAO.getVendor(vendorid);
					String name= vendor.getName();
					product.setTranstype("GRN");
					product.setVendor(name);
					
					printid = procurementDAO.getChildprocId(rs.getString(6));
					link ="grnprintProcurement?id="+printid+"";
					product.setPrintId(link);
					if(location_filter.equals("0")){
						balanceqty = balanceqty+ rs.getInt(5);
						product.setBalqty(balanceqty);
						list.add(product);
					}else if(location_filter.equals(rs.getString(7))){
						balanceqty = balanceqty+ rs.getInt(5);
						product.setBalqty(balanceqty);
						list.add(product);
					}
					
					
					
					String freeqty= getFreeqtyofProduct(prodid);
					if(freeqty!=null){
						if(!freeqty.equals("0")){
							Product freeproduct= new Product();
							freeproduct.setFreeqty(freeqty);
							freeproduct.setQty(freeqty);
							freeproduct.setIssueqty("-");
							freeproduct.setTranstype("Free GRN");
							freeproduct.setVendor(name);
							freeproduct.setDate(date);
							freeproduct.setVoucherno(voucherno);
							freeproduct.setDocno(voucherno);
							freeproduct.setPrintId(link);
							if(location_filter.equals("0")){
								balanceqty =balanceqty +Integer.parseInt(freeqty);
								freeproduct.setBalqty(balanceqty);
								list.add(freeproduct);
							}else if(location_filter.equals(rs.getString(7))){
								balanceqty =balanceqty +Integer.parseInt(freeqty);
								freeproduct.setBalqty(balanceqty);
								list.add(freeproduct);
							}
						}
					}
					
//					ArrayList<Product> listTransfer=getifTransferedThisProduct(catalogueid, pmaster.getProduct_name(), abbrivation);
//					list.addAll(listTransfer);
					
					//Akash 06-03-2018 bin card according to prodid
					
					//Return from pharmacy in
					ArrayList<Product> returnpatientlist = getReturnToPatientList(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(returnpatientlist);
					
					//return to store in 
					ArrayList<Product> returnindenttransferlist = getReturnIndentTransferList(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(returnindenttransferlist);
					
					//direct transfer in 
					ArrayList<Product> directindenttransferlistin = getDirectIndentTransferListIn(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(directindenttransferlistin);
					
					//request indent transfer in
					ArrayList<Product> requestindenttransferlistin = getRequestIndentTransferListIn(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(requestindenttransferlistin);
					
					//adjustment in
					ArrayList<Product> adjustmentin = binCardAdjustmentIn(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(adjustmentin);
					
					//return to store out 
					ArrayList<Product> returnindenttransferlistout = getReturnIndentTransferListOut(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(returnindenttransferlistout);
					
					
					//request indent transfer out
					ArrayList<Product> requestindenttransferlist = getRequestIndentTransferList(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(requestindenttransferlist);
					
					//direct transfer out 
					ArrayList<Product> directindenttransferlist = getDirectIndentTransferList(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(directindenttransferlist);
					
					//Return to supplier out
					ArrayList<Product> returnsupplierlist = getReturnToSupplierList(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(returnsupplierlist);
					
					//Consume by patient or user out
					ArrayList<Product> consumelist = getProductConsumeList(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(consumelist);
					
					//sale to patient out
					ArrayList<Product> salepatientlist = getSaleToPatientList(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(salepatientlist);
					
					
					///adjustment out
					ArrayList<Product> adjustmentout = binCardAdjustmentOut(prodid,catalogueid,abbrivation,userwise,location_filter,fromdate,todate);
					list.addAll(adjustmentout);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}
	
	private ArrayList<Product> binCardAdjustmentOut(String prodid, String catalogueid, String abbrivation,
			String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> list= new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO =new JDBCPharmacyDAO(connection);
			StringBuffer buffer=new StringBuffer();
			buffer.append("select adjustment_data.id,adjustment_data.change_qty,inventory_product.location,adjustment_data.datetime,adjustment_data.userid ");
			buffer.append("from adjustment_data ");
			buffer.append("inner join inventory_product on inventory_product.id = adjustment_data.product_id ");
			buffer.append("where adjustment_type!=2 and adjustment_data.product_id='"+prodid+"'  ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and adjustment_data.datetime between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				    Product product= new Product();
			        String qty= rs.getString(2);
			        String location= rs.getString(3);
			        String date= rs.getString(4);
			        if(date!=null){
			        	String[] datetime = date.split(" ");
			        	date = datetime[0];
			        }
			        balanceqty =balanceqty - rs.getInt(2);
			        String vendor= pharmacyDAO.getPharmacyLocation(location);
			      /*  product.setTranstype("DEP-ISS");*/
			        product.setTranstype("Adjustment Out");
			        product.setVendor(vendor);
			        product.setQty("-");
			        product.setIssueqty(""+qty);
			        product.setBalqty(balanceqty);
			       /* product.setDocno(abbrivation+""+rs.getString(5));*/
			        product.setDocno(rs.getString(1));
			        product.setDate(date);
			        String handoverto = rs.getString(5);
			        product.setHandover_to(handoverto);
					String link="";
					product.setPrintId(link);
			        list.add(product);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}

	private ArrayList<Product> binCardAdjustmentIn(String prodid, String catalogueid, String abbrivation,
			String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> list= new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO =new JDBCPharmacyDAO(connection);
			StringBuffer buffer=new StringBuffer();
			
			buffer.append("select adjustment_data.id,adjustment_data.change_qty,inventory_product.location,adjustment_data.datetime,adjustment_data.userid ");
			buffer.append("from adjustment_data ");
			buffer.append("inner join inventory_product on inventory_product.id = adjustment_data.product_id ");
			buffer.append("where adjustment_type=2 and adjustment_data.product_id='"+prodid+"'  ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and adjustment_data.datetime between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				    Product product= new Product();
			        String qty= rs.getString(2);
			        String location= rs.getString(3);
			        String date= rs.getString(4);
			        if(date!=null){
			        	String[] datetime = date.split(" ");
			        	date = datetime[0];
			        }
			        balanceqty =balanceqty + rs.getInt(2);
			        String vendor= pharmacyDAO.getPharmacyLocation(location);
			      /*  product.setTranstype("DEP-ISS");*/
			        product.setTranstype("Adjustment In");
			        product.setVendor(vendor);
			        product.setQty(""+qty);
			        product.setIssueqty("-");
			        product.setBalqty(balanceqty);
			       /* product.setDocno(abbrivation+""+rs.getString(5));*/
			        product.setDocno(rs.getString(1));
			        product.setDate(date);
			        String handoverto = rs.getString(5);
			        product.setHandover_to(handoverto);
			        
			        String link="";
					product.setPrintId(link);
			        list.add(product);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}

	private ArrayList<Product> getReturnIndentTransferListOut(String prodid, String catalogueid, String abbrivation,
			String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1
			buffer.append("select inventory_parent_transfer_log.issued_date,inventory_transfer_log.qty,inventory_parent_transfer_log.to_location,inventory_parent_transfer_log.id,inventory_parent_transfer_log.handover_to from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("where (inventory_transfer_log.old_prodid='"+prodid+"' or inventory_transfer_log.new_prodid='"+prodid+"') and inventory_parent_transfer_log.req_or_trans=2 and inventory_parent_transfer_log.deleted=0 ");
			if(!userwise.equals("0")){
				buffer.append("and inventory_parent_transfer_log.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
					buffer.append("and from_location ='" + location_filter + "' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and inventory_parent_transfer_log.issued_date between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				Product product= new Product();
				product.setDate(rs.getString(1));
				product.setQty("-");
				balanceqty = balanceqty -rs.getInt(2);
				product.setBalqty(balanceqty);
				product.setIssueqty(""+rs.getInt(2));
				String location= rs.getString(3);
				int parentid= rs.getInt(4);
			    String vendor = pharmacyDAO.getLocationName(location);	
				/*product.setTranstype("DEP-ISS");*/
			    product.setTranstype("Return to Store Out");
				product.setVendor(vendor);
				/*product.setDocno(abbrivation+""+parentid);*/
				product.setDocno(""+parentid);
				String handoverto = rs.getString(5);
		        if(handoverto!=null){
		        	String[] data = handoverto.split("]");
		        	handoverto = data[0];
		        }else{
		        	handoverto="";
		        }
		        product.setHandover_to(handoverto);
		        int printid=0;
		        String link="";
		        printid= rs.getInt(4);
				link="deliverPrintDirectProduct?id="+printid+"";
				product.setPrintId(link);
				arrayList.add(product);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return arrayList;
	}

	private ArrayList<Product> getRequestIndentTransferListIn(String prodid, String catalogueid, String abbrivation,
			String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> list= new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO =new JDBCPharmacyDAO(connection);
			StringBuffer buffer=new StringBuffer();
			buffer.append("SELECT inventory_request_temp_log.id,inventory_request_temp_log.qty,inventory_request_temp_log.location,inventory_request_temp_log.transfer_date,inventory_parent_transfer_log.id,inventory_parent_transfer_log.handover_to  FROM inventory_request_temp_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_request_temp_log.parentid ");
			buffer.append("where (inventory_request_temp_log.new_prodid='"+prodid+"' or inventory_request_temp_log.old_prodid='"+prodid+"') and inventory_parent_transfer_log.deleted=0 ");
			if(!userwise.equals("0")){
				buffer.append("and inventory_parent_transfer_log.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
				buffer.append("and from_location ='" + location_filter + "' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and inventory_request_temp_log.transfer_date between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				 
			        Product product= new Product();
			        String qty= rs.getString(2);
			        String location= rs.getString(3);
			        String date= rs.getString(4);
			        if(date!=null){
			        	String[] datetime = date.split(" ");
			        	date = datetime[0];
			        }
			        balanceqty =balanceqty + rs.getInt(2);
			        String vendor= pharmacyDAO.getPharmacyLocation(location);
			      /*  product.setTranstype("DEP-ISS");*/
			        product.setTranstype("Indent In");
			        product.setVendor(vendor);
			        product.setQty(""+qty);
			        product.setIssueqty("-");
			        product.setBalqty(balanceqty);
			       /* product.setDocno(abbrivation+""+rs.getString(5));*/
			        product.setDocno(rs.getString(5));
			        product.setDate(date);
			        String handoverto = rs.getString(6);
			        if(handoverto!=null){
			        	String[] data = handoverto.split("]");
			        	handoverto = data[0];
			        }else{
			        	handoverto="";
			        }
			        product.setHandover_to(handoverto);
			        int printid=0;
			        String link="";
			        printid= rs.getInt(5);
					link="deliverPrintDirectProduct?id="+printid+"";
					product.setPrintId(link);
			        list.add(product);
			        
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}

	private ArrayList<Product> getDirectIndentTransferListIn(String prodid, String catalogueid, String abbrivation,
			String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1
			buffer.append("select inventory_parent_transfer_log.issued_date,inventory_transfer_log.qty,inventory_parent_transfer_log.from_location,inventory_parent_transfer_log.id,inventory_parent_transfer_log.handover_to from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("where (inventory_transfer_log.new_prodid='"+prodid+"' or inventory_transfer_log.old_prodid='"+prodid+"') and inventory_parent_transfer_log.req_or_trans=1 and inventory_parent_transfer_log.deleted=0 ");
			if(!userwise.equals("0")){
				buffer.append("and inventory_parent_transfer_log.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
					buffer.append("and to_location ='" + location_filter + "' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and inventory_parent_transfer_log.issued_date between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				Product product= new Product();
				product.setDate(rs.getString(1));
				product.setQty(rs.getString(2));
				balanceqty = balanceqty +rs.getInt(2);
				product.setBalqty(balanceqty);
				product.setIssueqty("-");
				String location= rs.getString(3);
				int parentid= rs.getInt(4);
			    String vendor = pharmacyDAO.getLocationName(location);	
				/*product.setTranstype("DEP-ISS");*/
			    product.setTranstype("Indent In");
				product.setVendor(vendor);
				/*product.setDocno(abbrivation+""+parentid);*/
				product.setDocno(""+parentid);
				String handoverto = rs.getString(5);
		        if(handoverto!=null){
		        	String[] data = handoverto.split("]");
		        	handoverto = data[0];
		        }else{
		        	handoverto="";
		        }
		        product.setHandover_to(handoverto);
		        int printid=0;
		        String link="";
		        printid= rs.getInt(4);
				link="deliverPrintDirectProduct?id="+printid+"";
				product.setPrintId(link);
				arrayList.add(product);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return arrayList;
	}

	private ArrayList<Product> getProductConsumeList(String prodid, String catalogueid, String abbrivation,
			String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			
			buffer.append("select indent_patient_transfer_log.id, fromlocation, datetime,userid, prodid, qty ");
			buffer.append("from indent_patient_transfer_log ");
			buffer.append("inner join indent_parent_patient_transfer_log on indent_parent_patient_transfer_log.id = indent_patient_transfer_log.parentid ");
			buffer.append("inner join apm_patient on apm_patient.id = indent_patient_transfer_log.clientid ");
			buffer.append("where indent_patient_transfer_log.prodid='"+prodid+"' ");
			if(!userwise.equals("0")){
				buffer.append("and indent_patient_transfer_log.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
					buffer.append("and inventory_product.fromlocation ='" + location_filter + "' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and indent_patient_transfer_log.datetime between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				Product product= new Product();
				String date="";
				if(rs.getString(3)!=null){
					String[] datetime = rs.getString(3).split(" ");
					date = datetime[0];
				}
				product.setDate(date);
				product.setQty("-");
				balanceqty = balanceqty -rs.getInt(6);
				product.setBalqty(balanceqty);
				product.setIssueqty(rs.getString(6));
				String location= rs.getString(2);
				int parentid= rs.getInt(1);
			    String vendor = pharmacyDAO.getLocationName(location);	
				product.setTranstype("Consume");
				product.setVendor(vendor);
				product.setDocno(""+parentid);
		        String link="";
				product.setPrintId(link);
				arrayList.add(product);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return arrayList;
	}

	private ArrayList<Product> getReturnIndentTransferList(String prodid, String catalogueid, String abbrivation,String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1
			buffer.append("select inventory_parent_transfer_log.issued_date,inventory_transfer_log.qty,inventory_parent_transfer_log.from_location,inventory_parent_transfer_log.id,inventory_parent_transfer_log.handover_to from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("where inventory_transfer_log.new_prodid="+prodid+" and inventory_parent_transfer_log.req_or_trans=2 and inventory_parent_transfer_log.deleted=0 ");
			if(!userwise.equals("0")){
				buffer.append("and inventory_parent_transfer_log.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
					buffer.append("and to_location ='" + location_filter + "' ");
			}

			if(fromdate!=null && todate!=null){
				buffer.append("and inventory_parent_transfer_log.issued_date between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				Product product= new Product();
				product.setDate(rs.getString(1));
				product.setQty(""+rs.getInt(2));
				balanceqty = balanceqty +rs.getInt(2);
				product.setBalqty(balanceqty);
				product.setIssueqty("-");
				String location= rs.getString(3);
				int parentid= rs.getInt(4);
			    String vendor = pharmacyDAO.getLocationName(location);	
				/*product.setTranstype("DEP-ISS");*/
			    product.setTranstype("Return to Store In");
				product.setVendor(vendor);
				/*product.setDocno(abbrivation+""+parentid);*/
				product.setDocno(""+parentid);
				String handoverto = rs.getString(5);
		        if(handoverto!=null){
		        	String[] data = handoverto.split("]");
		        	handoverto = data[0];
		        }else{
		        	handoverto="";
		        }
		        product.setHandover_to(handoverto);
		        int printid=0;
		        String link="";
		        printid= rs.getInt(4);
				link="deliverPrintProduct?id="+printid+"&status=2";
				product.setPrintId(link);
				arrayList.add(product);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return arrayList;
	}

	
	private ArrayList<Product> getReturnToSupplierList(String prodid, String catalogueid, String abbrivation,
			String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			
			buffer.append("SELECT inventory_product_return_log.id, inventory_product_return_log.qty, ");
			buffer.append("datetime, inventory_product_return_log.userid,inventory_product.location ");
			buffer.append("from inventory_product_return_log ");
			buffer.append("inner join inventory_product on  inventory_product.id = inventory_product_return_log.productid ");
			buffer.append("where status!=0 and inventory_product_return_log.productid='"+prodid+"' and inventory_product_return_log.iscancel=0 ");
			if(!userwise.equals("0")){
				buffer.append("and inventory_product_return_log.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
					buffer.append("and inventory_product.location ='" + location_filter + "' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and inventory_product_return_log.datetime between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				Product product= new Product();
				String date="";
				if(rs.getString(3)!=null){
					String[] datetime = rs.getString(3).split(" ");
					date = datetime[0];
				}
				product.setDate(date);
				product.setQty("-");
				balanceqty = balanceqty -rs.getInt(2);
				product.setBalqty(balanceqty);
				product.setIssueqty(rs.getString(2));
				String location= rs.getString(5);
				int parentid= rs.getInt(1);
			    String vendor = pharmacyDAO.getLocationName(location);	
				product.setTranstype("Supplier-Return");
				product.setVendor(vendor);
				product.setDocno(""+parentid);
				int printid=0;
		        String link="";
		        printid= rs.getInt(1);
				link="printreturngrnProduct?grnreturnid="+printid+"&status=1";
				product.setPrintId(link);
				arrayList.add(product);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return arrayList;
	}

	private ArrayList<Product> getReturnToPatientList(String prodid, String catalogueid, String abbrivation,
			String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1
			//not done code of bsale bill. when required do it code query and codes
			/*buffer.append("select inventory_parent_transfer_log.issued_date,inventory_transfer_log.qty,inventory_parent_transfer_log.to_location,inventory_parent_transfer_log.id from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("where inventory_transfer_log.old_prodid="+prodid+" and inventory_parent_transfer_log.req_or_trans=1 ");
			*/
			buffer.append("select date, apm_medicine_bill.clientid, apm_medicine_bill.location, apm_medicine_bill.pclientid, ");
			buffer.append("refundid,quantity,apm_medicine_bill.id,product_id ");
			buffer.append("from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_charges.invoiceid = apm_medicine_bill.id ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("where apm_medicine_bill.isreturn =1 and product_id='"+prodid+"' and apm_medicine_bill.deleted=0  ");
			if(!userwise.equals("0")){
				buffer.append("and apm_medicine_bill.userid='"+userwise+"' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and apm_medicine_bill.date between '"+fromdate+"' and '"+todate+"' ");
			}
			if (!location_filter.equals("0")) {
					buffer.append("and apm_medicine_bill.location ='" + location_filter + "' ");
			}
			
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				int printid=0;
				String link="";
				Product product= new Product();
				product.setDate(rs.getString(1));
				product.setQty(rs.getString(6));
				balanceqty = balanceqty +rs.getInt(6);
				product.setBalqty(balanceqty);
				product.setIssueqty("-");
				String location= rs.getString(3);
				int parentid= rs.getInt(5);
			    String vendor = pharmacyDAO.getLocationName(location);	
				product.setTranstype("Pharmacy-Return");
				product.setVendor(vendor);
				product.setDocno(""+parentid);
				printid= rs.getInt(7);
				link="viewbillPharmacy?selectedid=0&billno="+printid+"";
				product.setPrintId(link);
				arrayList.add(product);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return arrayList;
	}

	private ArrayList<Product> getSaleToPatientList(String prodid, String catalogueid, String abbrivation, String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1
			//not done code of bsale bill. when required do it code query and codes
			/*buffer.append("select inventory_parent_transfer_log.issued_date,inventory_transfer_log.qty,inventory_parent_transfer_log.to_location,inventory_parent_transfer_log.id from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("where inventory_transfer_log.old_prodid="+prodid+" and inventory_parent_transfer_log.req_or_trans=1 ");
			*/
			buffer.append("select date, apm_medicine_bill.clientid, apm_medicine_bill.location, apm_medicine_bill.pclientid, ");
			buffer.append("refundid,quantity,apm_medicine_bill.id,product_id ");
			buffer.append("from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_charges.invoiceid = apm_medicine_bill.id ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("where apm_medicine_bill.isreturn =0 and product_id='"+prodid+"' and apm_medicine_bill.deleted=0  ");
			if(!userwise.equals("0")){
				buffer.append("and apm_medicine_bill.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
					buffer.append("and apm_medicine_bill.location ='" + location_filter + "' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and apm_medicine_bill.date between '"+fromdate+"' and '"+todate+"' ");
			}
			
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				Product product= new Product();
				product.setDate(rs.getString(1));
				product.setQty("-");
				balanceqty = balanceqty -rs.getInt(6);
				product.setBalqty(balanceqty);
				product.setIssueqty(rs.getString(6));
				String location= rs.getString(3);
				int parentid= rs.getInt(7);
			    String vendor = pharmacyDAO.getLocationName(location);	
				product.setTranstype("Pharmacy-Sale");
				product.setVendor(vendor);
				product.setDocno(""+parentid);
				
				int printid= rs.getInt(7);
				String link="viewbillPharmacy?selectedid=0&billno="+printid+"";
				product.setPrintId(link);
				arrayList.add(product);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return arrayList;
	}

	private ArrayList<Product> getDirectIndentTransferList(String prodid, String catalogueid, String abbrivation,String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			//req_or_transfer==1
			buffer.append("select inventory_parent_transfer_log.issued_date,inventory_transfer_log.qty,inventory_parent_transfer_log.to_location,inventory_parent_transfer_log.id,inventory_parent_transfer_log.handover_to from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("where inventory_transfer_log.old_prodid="+prodid+" and inventory_parent_transfer_log.req_or_trans=1 and inventory_parent_transfer_log.deleted=0 ");
			if(!userwise.equals("0")){
				buffer.append("and inventory_parent_transfer_log.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
					/*buffer.append("and to_location ='" + location_filter + "' ");*/
					buffer.append("and from_location ='" + location_filter + "' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and inventory_parent_transfer_log.issued_date between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				Product product= new Product();
				product.setDate(rs.getString(1));
				product.setQty("-");
				balanceqty = balanceqty -rs.getInt(2);
				product.setBalqty(balanceqty);
				product.setIssueqty(rs.getString(2));
				String location= rs.getString(3);
				int parentid= rs.getInt(4);
			    String vendor = pharmacyDAO.getLocationName(location);	
				/*product.setTranstype("DEP-ISS");*/
			    product.setTranstype("Indent Out");
				product.setVendor(vendor);
				/*product.setDocno(abbrivation+""+parentid);*/
				product.setDocno(""+parentid);
				String handoverto = rs.getString(5);
		        if(handoverto!=null){
		        	String[] data = handoverto.split("]");
		        	handoverto = data[0];
		        }else{
		        	handoverto="";
		        }
		        product.setHandover_to(handoverto);
		        int printid=0;
		        String link="";
		        printid= rs.getInt(4);
				link="deliverPrintDirectProduct?id="+printid+"";
				product.setPrintId(link);
				arrayList.add(product);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return arrayList;
	}

	private ArrayList<Product> getRequestIndentTransferList(String prodid, String catalogueid, String abbrivation,String userwise, String location_filter, String fromdate, String todate) {
		ArrayList<Product> list= new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO =new JDBCPharmacyDAO(connection);
			StringBuffer buffer=new StringBuffer();
			/*buffer.append("SELECT inventory_request_temp_log.id,inventory_request_temp_log.qty,inventory_parent_transfer_log.from_location,inventory_parent_transfer_log.issued_date  FROM inventory_request_temp_log ");*/
			buffer.append("SELECT inventory_request_temp_log.id,inventory_request_temp_log.qty,inventory_parent_transfer_log.from_location,inventory_request_temp_log.transfer_date,inventory_parent_transfer_log.id,inventory_parent_transfer_log.handover_to  FROM inventory_request_temp_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_request_temp_log.parentid ");
			buffer.append("where inventory_request_temp_log.old_prodid="+prodid+" and inventory_parent_transfer_log.deleted=0 ");
			if(!userwise.equals("0")){
				buffer.append("and inventory_parent_transfer_log.userid='"+userwise+"' ");
			}
			if (!location_filter.equals("0")) {
				/*buffer.append("and from_location ='" + location_filter + "' ");*/
				buffer.append("and inventory_request_temp_log.location='" + location_filter + "' ");
			}
			if(fromdate!=null && todate!=null){
				buffer.append("and inventory_request_temp_log.transfer_date between '"+fromdate+"' and '"+todate+"' ");
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				 
			        Product product= new Product();
			        String qty= rs.getString(2);
			        String location= rs.getString(3);
			        String date= rs.getString(4);
			        if(date!=null){
			        	String[] datetime = date.split(" ");
			        	date = datetime[0];
			        }
			        balanceqty =balanceqty - rs.getInt(2);
			        String vendor= pharmacyDAO.getPharmacyLocation(location);
			      /*  product.setTranstype("DEP-ISS");*/
			        product.setTranstype("Indent Out");
			        product.setVendor(vendor);
			        product.setQty("-");
			        product.setIssueqty(qty);
			        product.setBalqty(balanceqty);
			       /* product.setDocno(abbrivation+""+rs.getString(5));*/
			        product.setDocno(rs.getString(5));
			        product.setDate(date);
			        String handoverto = rs.getString(6);
			        if(handoverto!=null){
			        	String[] data = handoverto.split("]");
			        	handoverto = data[0];
			        }else{
			        	handoverto="";
			        }
			        product.setHandover_to(handoverto);
			        int printid=0;
			        String link="";
			        printid= rs.getInt(5);
					link="deliverPrintProduct?id="+printid+"&status=2";
					product.setPrintId(link);
			        list.add(product);
			        
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}

	public String getFreeqtyofProduct(String product_id){
		
		String res="0";
		try {
			String sql="select freeqty from inventory_product where id="+product_id+" ";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				 res= rs.getString(1);
			}
		} catch (Exception e) {

			e.printStackTrace();
		} 
		
		return res;
	}
	
	
	public ArrayList<Product> getifTransferedThisProduct(String catalogueid, String prodname, String abbrivation) {
		
		ArrayList<Product> list= new ArrayList<Product>();
		try {
			
			StringBuffer buffer= new StringBuffer();
			buffer.append("select req_trans_ret,id,parentid from inventory_transfer_log where catlogueid="+catalogueid+" order by id asc ");
			PreparedStatement preparedStatement=connection.prepareStatement(buffer.toString());
			ResultSet rs =preparedStatement.executeQuery();
			while(rs.next()){
				
				 int res= rs.getInt(1);
				 int id= rs.getInt(2);
				 int parentid= rs.getInt(3);
				 if(res==1){
					 Product product= getDirectTransferforBinCard(id,abbrivation);
					 list.add(product);
				 } else if(res==0) {
					 ArrayList<Product> alllist= getReqTransferforBinCard(parentid,balanceqty,abbrivation,prodname);
					 list.addAll(alllist);
				 }
				 
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}
	
	private ArrayList<Product> getReqTransferforBinCard(int parentid, int bal, String abbrivation, String prodname) {
		
		ArrayList<Product> list= new ArrayList<Product>();
		try {
			PharmacyDAO pharmacyDAO =new JDBCPharmacyDAO(connection);
			StringBuffer buffer=new StringBuffer();
			buffer.append("SELECT inventory_request_temp_log.id,inventory_request_temp_log.qty,inventory_parent_transfer_log.from_location,inventory_parent_transfer_log.issued_date  FROM inventory_request_temp_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_request_temp_log.parentid ");
			buffer.append("where inventory_request_temp_log.parentid="+parentid+" and inventory_request_temp_log.prod_name='"+prodname+"' ;");
			
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				 
			        Product product= new Product();
			        String qty= rs.getString(2);
			        String location= rs.getString(3);
			        String date= rs.getString(4);
			        balanceqty =balanceqty - rs.getInt(2);
			        String vendor= pharmacyDAO.getPharmacyLocation(location);
			        product.setTranstype("DEP-ISS");
			        product.setVendor(vendor);
			        product.setQty("-");
			        product.setIssueqty(qty);
			        product.setBalqty(balanceqty);
			        product.setDocno(abbrivation+""+parentid);
			        product.setDate(date);
			        list.add(product);
			        
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}

	public Product getDirectTransferforBinCard(int id,String abbrivation) {
		
		Product product= new Product();
		try {
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			StringBuffer buffer= new StringBuffer();
			buffer.append("select inventory_parent_transfer_log.issued_date,inventory_transfer_log.qty,inventory_parent_transfer_log.to_location,inventory_parent_transfer_log.id from inventory_transfer_log ");
			buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
			buffer.append("where inventory_transfer_log.id="+id+" ");
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				
				product.setDate(rs.getString(1));
				product.setQty("-");
				balanceqty = balanceqty -rs.getInt(2);
				product.setBalqty(balanceqty);
				product.setIssueqty(rs.getString(2));
				String location= rs.getString(3);
				int parentid= rs.getInt(4);
			    String vendor = pharmacyDAO.getLocationName(location);	
				product.setTranstype("DEP-ISS");
				product.setVendor(vendor);
				product.setDocno(abbrivation+""+parentid);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return product;
	}
	
	
	public String getCatIdProdId(String product_id) {
		String catid="0";
		try {
			String sql="select catalogueid from inventory_product where id='"+product_id+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				catid=rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return catid;
	}

	public int getAllProdAvailableStock(String catid, String warehouse_id) {
		int qty = 0;
		try {
			String sql = "select sum(stock) from inventory_product where stock>0 and location='"+warehouse_id+"' and catalogueid='"+catid+"'";
			// String sql ="select sum(stock) from inventory_product where
			// stock>0 and location=1 and prodname ='"+product_name+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				qty = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qty;
	}

	public int getTotalGrnReportCount(String fromdate, String todate, String vendorid, String location, String purchaseinvoice) {

		int result=0;
			try {
				
				StringBuffer buffer= new StringBuffer();
				/*buffer.append("select count(*) from inventory_procurement ");
				buffer.append("where voucherno is NOT NULL ");
				    if (vendorid != null) {
				      buffer.append("and vendorid=" + vendorid + " ");
				    }
				    if (!location.equals("0") && !location.equals("1")) {
				       buffer.append("and location=" + location + " ");
				    }
				    buffer.append("and date between '" + fromdate + "' and '" + todate + "' ");*/
				
				  if(purchaseinvoice!=null){
				    if(purchaseinvoice.equals("Invoice Date")){
				    	buffer.append("select count(*) from inventory_procurement ");
				    	buffer.append("inner join inventory_parent_procurement on inventory_parent_procurement.id = inventory_procurement.procurementid ");
				    	buffer.append("where inventory_procurement.voucherno is NOT NULL and inventory_procurement.deleted=0 and isdelivermemo=0 ");
				    	if (vendorid != null) {
				    		buffer.append("and inventory_procurement.vendorid=" + vendorid + " ");
				    	}
				    	if (!location.equals("0") && !location.equals("1")) {
				    		buffer.append("and inventory_procurement.location=" + location + " ");
				    	}
				    	buffer.append("and inventory_procurement.voucherdate between '" + fromdate + "' and '" + todate + "' ");
				    	buffer.append("group by procurementid order by inventory_procurement.voucherdate desc");
				    }else{
				    	buffer.append("select count(*) from inventory_procurement ");
				    	buffer.append("inner join inventory_parent_procurement on inventory_parent_procurement.id = inventory_procurement.procurementid ");
				    	buffer.append("where inventory_procurement.voucherno is NOT NULL and inventory_procurement.deleted=0 and isdelivermemo=0 ");
				    	if (vendorid != null) {
				    		buffer.append("and inventory_procurement.vendorid=" + vendorid + " ");
				    	}
				    	if (!location.equals("0") && !location.equals("1")) {
				    		buffer.append("and inventory_procurement.location=" + location + " ");
				    	}
				    	buffer.append("and inventory_procurement.date between '" + fromdate + "' and '" + todate + "' ");
				    	buffer.append("group by inventory_procurement.procurementid order by inventory_procurement.date desc");
				    }
				  }else{
					  buffer.append("select count(*) from inventory_procurement ");
					  buffer.append("inner join inventory_parent_procurement on inventory_parent_procurement.id = inventory_procurement.procurementid ");
				      buffer.append("where inventory_procurement.voucherno is NOT NULL and inventory_procurement.deleted=0 and isdelivermemo=0 ");
					  if (vendorid != null) {
						  buffer.append("and inventory_procurement.vendorid=" + vendorid + " ");
					  }
					  if (!location.equals("0") && !location.equals("1")) {
						  buffer.append("and inventory_procurement.location=" + location + " ");
					  }
					  buffer.append("and inventory_procurement.date between '" + fromdate + "' and '" + todate + "' ");
					  buffer.append("group by procurementid order by inventory_procurement.date desc ");
				  }
				 PreparedStatement ps=connection.prepareStatement(buffer.toString());   
				 ResultSet rs= ps.executeQuery();
				 while(rs.next()){
					  result =result+1;
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		return result;
	}

	public int getCountSaleReturnReport(String fromdate, String todate, String location, String salereturn,
			String billtype) {
		int result=0;
		try {
			//Akash for get total count for pagination
			StringBuffer buffer = new StringBuffer();

			buffer.append("select count(*) from apm_medicine_bill   ");
			buffer.append("where apm_medicine_bill.date between '" + fromdate + "' and '" + todate + "' ");

		
			if(billtype==null){
				billtype="0";
			}else if(billtype.equals("")){
				billtype="0";
			}

			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}

			if (salereturn.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}

			if (salereturn.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}
			
			if(!billtype.equals("0")){
				buffer.append(" and  apm_medicine_bill.initial_paymode='"+billtype+"' ");
			}
			buffer.append("and apm_medicine_bill.tpid='0' ");
			buffer.append(" and apm_medicine_bill.deleted='0' ");
			buffer.append("order by apm_medicine_bill.id desc ");
			
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result= rs.getInt(1);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int getCountPaymentReceiveReport(String fromdate, String todate, String location, String salereturn,
			String billtype, String balance) {
		int result=0;
		try {
			StringBuffer buffer = new StringBuffer();
			//Akash for get total count for pagination
			buffer.append("select apm_medicine_bill.id from apm_medicine_bill   ");
			buffer.append("inner join apm_medicine_payment on apm_medicine_payment.billno = apm_medicine_bill.id ");
			buffer.append("where apm_medicine_payment.date between '" + fromdate + "' and '" + todate + "' ");
			if (!billtype.equals("0")) {
				buffer.append("and paymode='" + billtype + "' ");
			}
			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}
			if (salereturn.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}
			if (salereturn.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}
			if (!balance.equals("0")) {
				if (balance.equals("1")) {
					buffer.append("and apm_medicine_bill.balance < 100 and apm_medicine_bill.balance > 0 ");
				} else if (balance.equals("2")) {
					buffer.append("and apm_medicine_bill.balance < 1000 and apm_medicine_bill.balance > 0 ");
				} else if (balance.equals("3")) {
					buffer.append("and apm_medicine_bill.balance < 5000 and apm_medicine_bill.balance > 0 ");
				} else if (balance.equals("4")) {
					buffer.append("and apm_medicine_bill.balance > 5000 and apm_medicine_bill.balance > 0 ");
				}
			}
			buffer.append("and apm_medicine_bill.tpid='0' ");
			buffer.append("order by apm_medicine_payment.id desc ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getTotalBillByBillDate(String fromdate, String todate, String location, String salereturn,
			String billtype) {
		int tot =0;
		try {
			//Akash for get total sum after pagination
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(payment) from apm_medicine_bill   ");
			buffer.append("inner join apm_medicine_payment on apm_medicine_bill.id =  apm_medicine_payment.billno ");
			buffer.append("where apm_medicine_bill.date between '" + fromdate + "' and '" + todate + "' ");
			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}
			if (salereturn.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}
			if (salereturn.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}
			buffer.append("and apm_medicine_bill.tpid='0' ");
			buffer.append("order by apm_medicine_bill.id desc ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				tot = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tot;
	}

	public int getCountOpeningStockList(String fromDate, String toDate,String searchbyprodname, String location_filter, String category_id) {
		PreparedStatement preparedStatement = null;
		//shubham 17/11/2018
		StringBuffer buffer= new StringBuffer();
		/*buffer.append(" SELECT count(*)"
				+ " FROM medicine_opening_stock inner join inventory_product on medicine_opening_stock.prodid=inventory_product.id "
				+ " where commencing between '" + fromDate+ "' and '" + toDate + "' and isconsume='0' ");
		if(searchbyprodname!=null){
			buffer.append("and prodname like ('%" + searchbyprodname + "%') ");
		}*/
		buffer.append("SELECT count(*)  ");
		buffer.append("FROM medicine_opening_stock ");
		buffer.append(" inner join inventory_product on medicine_opening_stock.prodid=inventory_product.id ");
		buffer.append("where commencing between '" + fromDate + "' and '" + toDate + "' and isconsume='0' ");
		if (searchbyprodname != null) {
			buffer.append("and (prodname like ('%" + searchbyprodname + "%') or batch_no like ('%" + searchbyprodname + "%') )  ");
		}
		if(!location_filter.equals("0")){
			buffer.append("and inventory_product.location='"+location_filter+"' ");
		}
		if(!category_id.equals("0")){
			buffer.append("and inventory_product.categoryid='"+category_id+"' ");
		}
		buffer.append("group by medicine_opening_stock.prodid ");
		int result =0;
		//Akash for get total count for pagination
		try {
			preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				result = result + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Product getToalBillByPaymentDate(String fromdate, String todate, String location, String salereturn,
			String billtype, String balance) {
		Product product = new Product();
		int result=0;
		try {
			StringBuffer buffer = new StringBuffer();
			//Akash for get total count for pagination
			buffer.append("select sum(payment),sum(apm_medicine_bill.balance) from apm_medicine_bill   ");
			buffer.append("inner join apm_medicine_payment on apm_medicine_payment.billno = apm_medicine_bill.id ");
			buffer.append("where apm_medicine_payment.date between '" + fromdate + "' and '" + todate + "' ");
			if (!billtype.equals("0")) {
				buffer.append("and paymode='" + billtype + "' ");
			}
			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}
			if (salereturn.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}
			if (salereturn.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}
			if (!balance.equals("0")) {
				if (balance.equals("1")) {
					buffer.append("and apm_medicine_bill.balance < 100 and apm_medicine_bill.balance > 0 ");
				} else if (balance.equals("2")) {
					buffer.append("and apm_medicine_bill.balance < 1000 and apm_medicine_bill.balance > 0 ");
				} else if (balance.equals("3")) {
					buffer.append("and apm_medicine_bill.balance < 5000 and apm_medicine_bill.balance > 0 ");
				} else if (balance.equals("4")) {
					buffer.append("and apm_medicine_bill.balance > 5000 and apm_medicine_bill.balance > 0 ");
				}
			}
			buffer.append("and apm_medicine_bill.tpid='0' ");
			buffer.append("order by apm_medicine_payment.id desc ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				product.setTotal(""+rs.getInt(1));
				product.setBalance(""+rs.getInt(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public int getCountConsumptionReport(String fromdate, String todate, String location, String salereturn,
			String billtype) {
		int result=0;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append(
					"select id from apm_medicine_bill where  ");
			buffer.append("date between '" + fromdate + "' and '" + todate + "' ");

			if (!billtype.equals("0")) {
				buffer = new StringBuffer();
				buffer.append(
						"select apm_medicine_bill.id,apm_medicine_bill.date,debit,apm_medicine_bill.clientid,apm_medicine_bill.pclientid,apm_medicine_bill.location,cgst,sgst,time,isreturn,priscid,paymode  ");
				buffer.append(" from apm_medicine_bill inner join apm_medicine_payment on ");
				buffer.append(" apm_medicine_bill.id = apm_medicine_payment.billno where ");
				buffer.append("apm_medicine_bill.date between '" + fromdate + "' and '" + todate + "' and paymode='"
						+ billtype + "' ");
			}

			if (!location.equals("0") && !location.equals("1")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}

			if (salereturn.equals("1")) {
				buffer.append(" and isreturn=0 ");
			}

			if (salereturn.equals("2")) {
				buffer.append(" and isreturn=1 ");
			}

			buffer.append("and apm_medicine_bill.tpid!='0' ");
			buffer.append("order by apm_medicine_bill.id desc ");

			PreparedStatement ps = connection.prepareStatement(buffer.toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result++;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public int getCountExpiryMedicineReport(String fromdate, String todate, String days, String location,
			String report) {
		int result=0;
		try {
			//Akash 28 sep 2017 For Pafination
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			String nowdate = dateFormat.format(calendar.getTime());
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_product.id from inventory_product ");
			buffer.append("inner join inventory_procurement on inventory_product.id=inventory_procurement.prodid  ");
			buffer.append("where inventory_product.lastmodified between '" + fromdate + "' and '" + todate + "' ");
			if (!location.equals("0") && !location.equals("32")) {

				buffer.append("and inventory_product.location='" + location + "' ");
			}
			buffer.append("group by inventory_product.id order by expiry_date ");

			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getCountIndentTransferLog(String fromdate, String todate, String location1, String filter_status,
			String location_filter, String searhText) {
		//Akash 28 sep 2017 pagination code
		int result =0;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append(
					"select id,req_or_trans from inventory_parent_transfer_log ");
			buffer.append("where request_date between '" + fromdate + "' and '" + todate + "' and deleted=0 ");

			if (!filter_status.equals("10")) {
				buffer.append("and req_or_trans ='" + filter_status + "' ");
			}
			if (searhText != null) {
				buffer.append("and id ='" + searhText + "' ");
			}

			if (!location_filter.equals("0")) {
				if (!filter_status.equals("10")) {
					if (filter_status.equals("0")) {
						buffer.append("and from_location ='" + location_filter + "' ");
					} else {
						buffer.append("and to_location ='" + location_filter + "' ");
					}
				} else {
					buffer.append("and to_location ='" + location_filter + "' ");
				}

			}

			buffer.append(" order by request_date,time asc ");
			
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				if (filter_status.equals("10")) {
					if (rs.getInt(2) != 0) {
						result++;
					} else {
						ArrayList<Product> arrayList2 = getRequestTranferIndent(rs.getString(1));
						if (arrayList2.size() > 0) {
							result++;
						}
					}
				} else if (filter_status.equals("1")) {
					result++;
				} else if (filter_status.equals("2")) {
					result++;
				} else {
					ArrayList<Product> arrayList2 = getRequestTranferIndent(rs.getString(1));
					if (arrayList2.size() > 0) {
						result++;
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getCountDelUpdateReportList(String fromdate, String todate, String location, String filter) {
		int result =0;
		try {
			PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);

			StringBuffer buffer = new StringBuffer();
			buffer.append(
					"select id from inventory_product_log where ");
			buffer.append("date between '" + fromdate + "' and '" + todate + "' ");
			if (!location.equals("0") && !location.equals("1")) {
				buffer.append("and location='" + location + "' ");
			}
			buffer.append("and isindent=" + filter + " ");

			buffer.append("order by id desc ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				result++;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}
	//@ jitu
		public int addToRroductReturntoSupplier(String product_id,String date,String location) {

			int res=0;
			try {
				
				 String sql="insert into inventory_product_temp_return (product_id,date,location,status) values (?,?,?,?)  ";
				 PreparedStatement ps=connection.prepareStatement(sql);
				 ps.setString(1, product_id);
				 ps.setString(2, date);
				 ps.setString(3, location);
				 ps.setString(4, "0");
				 
				 res= ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

		public ArrayList<Product> getTempReturnQueList(String location) {

			ArrayList<Product> list= new ArrayList<Product>();
			try {
				ProcurementDAO procurementDAO= new JDBCProcurementDAO(connection);
				InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
				StringBuffer buffer= new StringBuffer();
				/*buffer.append("select id,product_id,date,location from inventory_product_temp_return where status=0 ");
				if(!location.equals("0")){
					 
					buffer.append("and location="+location+" ");
				}
				buffer.append("group by product_id ");
				buffer.append("order by date desc ");*/
				buffer.append("select inventory_product_temp_return.id,product_id,inventory_product_temp_return.date, ");
				buffer.append("inventory_product_temp_return.location,  ");
				buffer.append("inventory_product.location,prodname,purchaseprice,stock,pack,supplierid,batch_no,expiry_date ");
				buffer.append("from inventory_product_temp_return ");
				buffer.append("inner join inventory_product on inventory_product.id = inventory_product_temp_return.product_id ");
				buffer.append("where status=0 and stock>0 ");
				if(!location.equals("0")){
					buffer.append("and inventory_product_temp_return.location="+location+" ");
				}
				buffer.append("group by product_id ");
				buffer.append("order by date desc ");
				PreparedStatement ps=connection.prepareStatement(buffer.toString());
				ResultSet rs =ps.executeQuery();
				while(rs.next()){
					  Product product= new Product();
					  product.setId(rs.getInt(1));
					  product.setProduct_id(rs.getString(2));
					  product.setDateTime(rs.getString(3));
					  product.setLocation(rs.getString(4));
					  //Product master =getProduct(product.getProduct_id());
					  product.setLocationName(pharmacyLocationNameByID(rs.getString(5)));
					  product.setProduct_name(rs.getString(6));
					  product.setPurchase_price(rs.getString(7));
					  Procurement procurement=procurementDAO.getProcurementDetails(product.getProduct_id()); 
					  product.setVoucherno(procurement.getVoucherno());
					  product.setQty(procurement.getQuantity());
					  product.setStock(rs.getString(8));
					  product.setPack(rs.getString(9));
					  product.setVendor_id(rs.getString(10));
					  product.setBatch_no(rs.getString(11));
					  product.setExpiry_date(rs.getString(12));
					  if(procurement.getProcurementid()!=null){
						  if(procurement.getProcurementid().equals("")){
							  product.setProcurementid("0");
						  }else{
							  product.setProcurementid(procurement.getProcurementid());
						  }
					  }else{
						  product.setProcurementid("0");
					  }
					  Vendor vendor = inventoryVendorDAO.getVendor(product.getVendor_id());
					  product.setVendor(vendor.getName());
					  int stock = rs.getInt(8);
					  if(stock>0){
						  list.add(product);
					  }
					 /* list.add(product);*/
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return list;
		}

		public int addToReturnDashBoard(Product product) {

			int res=0;
			try {
				
				String sql="insert into inventory_product_return_log (productid, vendorid, qty, datetime, location, userid, status,procurementid,grnreturnid,newprodid,newprocid) values (?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
				PreparedStatement ps=connection.prepareStatement(sql);
				ps.setString(1, product.getProduct_id());
				ps.setString(2, product.getVendor_id());
				ps.setString(3, product.getQty());
				ps.setString(4, product.getDateTime());
				ps.setString(5, product.getLocation());
				ps.setString(6, product.getUserid());
				ps.setString(7, "0");
				ps.setString(8, product.getProcurementid());
				ps.setString(9, product.getGrnreturnid());
				ps.setString(10, ""+product.getProdid());
				ps.setString(11, ""+product.getProcid());
				res =ps.executeUpdate();
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			return res;
		}

		public ArrayList<Product> getProductReturnList(String fromdate, String todate, String location, String searchtext,Pagination pagination) {

			ArrayList<Product> list= new ArrayList<Product>(); 
			InventoryVendorDAO inventoryVendorDAO= new JDBCInventoryVendorDAO(connection);
			ProcurementDAO procurementDAO =new JDBCProcurementDAO(connection);
			todate=todate+" 23:59:59";
			try {
				StringBuffer buffer= new StringBuffer();
				buffer.append("SELECT inventory_product_return_log.id, inventory_product_return_log.productid, inventory_product_return_log.vendorid, qty, datetime,inventory_product_return_log.location, inventory_product_return_log.userid, inventory_product_return_log.status, procurementid, grnreturnid,iscancel from inventory_product_return_log ");
				if(searchtext!=null){
					buffer.append("inner join inventory_vendor on inventory_vendor.id=inventory_product_return_log.vendorid ");
				}
				buffer.append("where inventory_product_return_log.datetime between '"+fromdate+"' and '"+todate+"' ");
				if(!location.equals("0")){
					buffer.append("and inventory_product_return_log.location="+location+" ");
				}
				
				if(searchtext!=null){
					buffer.append("and (inventory_product_return_log.grnreturnid='"+searchtext+"' or inventory_vendor.name like '%"+searchtext+"' or inventory_vendor.name like '"+searchtext+"%' ) ");
				}
				buffer.append("group by grnreturnid desc ");
				String sql=buffer.toString();
				
				if(pagination!=null){
					sql= pagination.getSQLQuery(sql);
				}
				
				PreparedStatement ps=connection.prepareStatement(sql);
				ResultSet rs =ps.executeQuery();
				while(rs.next()){
					 Product product= new Product();
					 product.setId(rs.getInt(1));
					 product.setProduct_id(rs.getString(2));
					 product.setVendor_id(rs.getString(3));
					 product.setQty(rs.getString(4));
					 product.setDateTime(DateTimeUtils.getDBDate(rs.getString(5)));
					 product.setLocation(rs.getString(6));
					 product.setLocationName(pharmacyLocationNameByID(product.getLocation()));
					 product.setUserid(rs.getString(7));
					 product.setStatus(rs.getString(8));
					 product.setProcurementid(rs.getString(9));
					 product.setGrnreturnid(rs.getString(10));
					 product.setDeleted(rs.getInt(11));
					 Product master= getProduct(product.getProduct_id());
					 product.setProduct_name(master.getProduct_name());
					 String voucherno= procurementDAO.getVouchernoByProcurementid(product.getProcurementid());
					 product.setVoucherno(voucherno);
					 product.setPurchase_price(master.getPurchase_price());
					 product.setBatch_no(master.getBatch_no());
					 double total= getTotalAmountOfReturn(product.getGrnreturnid());
					 product.setTotal(DateTimeUtils.changeFormat(total));
					 product.setExpiry_date(master.getExpiry_date());
					 Vendor vendor= inventoryVendorDAO.getVendor(product.getVendor_id()); 
					 product.setVendor(vendor.getName());
					 int sttaus = procurementDAO.getStatusRefundAmount(rs.getString(10));
					 product.setAmtreturnstatus(sttaus);
					 Product products = procurementDAO.getParentReturnProductData(rs.getString(10));
					 product.setNetammt(products.getNetammt());
					 product.setAprovedamt(products.getAprovedamt());
					 list.add(product);
					  
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
			
			return list;
		}
		
		
		private double getTotalAmountOfReturn(String grnreturnid) {
			
			double res=0;
			
			try {
				String sql="SELECT productid,qty from inventory_product_return_log where grnreturnid="+grnreturnid+" ";
				PreparedStatement ps=connection.prepareStatement(sql);
				ResultSet rs =ps.executeQuery();
				while(rs.next()){
					
					  String productid= rs.getString(1);
					  Product master= getProduct(productid);
					  int qty= rs.getInt(2);
					  double total = qty* (Double.parseDouble(master.getPurchase_price())/Double.parseDouble(master.getPack()));
					  res= res+ total;
				}
					
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			
			return res;
		}

		public int updateStockToReturn(String product_id,int remainstock) {

			int res=0;
			try {
				
				String sql="update inventory_product set isreturn=1, stock="+remainstock+" where id="+product_id+"";
				PreparedStatement ps=connection.prepareStatement(sql);
				res= ps.executeUpdate();
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			return res;
		}
		
		public Product getPriscAvailablilityByMolecules(String molecules,String genericname, int required) {

			Product product = new Product();

			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append("select inventory_product.id,inventory_product.supplierid,inventory_product.prodname,inventory_product.mrp, ");
				buffer.append("inventory_product.purchaseprice,inventory_product.saleprice,inventory_product.stock, ");
				buffer.append("inventory_product.mdicinenameid, inventory_product.expiry_date,inventory_product.batch_no, ");
				buffer.append("inventory_product.vat,inventory_product.genericname,inventory_product.cell,inventory_product.pack, ");
				buffer.append("inventory_product.medicine_type,inventory_product.mfg from inventory_product ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id=inventory_product.catalogueid where ( inventory_catalogue.genericname='"+genericname+"' ");
				buffer.append("or inventory_catalogue.molecules like ('%"+molecules+"%') ) and inventory_product.saleprice is not null and inventory_product.mrp>0 ");
				buffer.append("order by inventory_product.expiry_date asc ");
				PreparedStatement ps = connection.prepareStatement(buffer.toString());
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {

					product.setId(rs.getInt(1));
					product.setVendor_id(rs.getString(2));
					product.setProduct_name(rs.getString(3));
					product.setMrp(rs.getString(4));
					product.setPurchase_price(rs.getString(5));
					product.setSale_price(rs.getString(6));
					product.setStock(rs.getString(7));
					product.setMedicinenameid(rs.getString(8));
					product.setExpiry_date(rs.getString(9));
					product.setBatch_no(rs.getString(10));
					product.setVat(rs.getString(11));
					product.setGenericname(rs.getString(12));
					product.setShelf(rs.getString(13));
					product.setPack(rs.getString(14));
					product.setMedicine_type(rs.getString(15));
					product.setMfg(rs.getString(16));
					break;
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
			return product;
		}

		public int getProductReturnCount(String fromdate, String todate, String location, String searchtext) {

			int res=0;
			todate = todate + " 23:59:59";
			try {
				StringBuffer buffer= new StringBuffer();
				buffer.append("SELECT count(*) from inventory_product_return_log ");
				if(searchtext!=null){
					buffer.append("inner join inventory_vendor on inventory_vendor.id=inventory_product_return_log.vendorid ");
				}
				buffer.append("where inventory_product_return_log.datetime between '"+fromdate+"' and '"+todate+"' ");
				if(!location.equals("0")){
					buffer.append("and inventory_product_return_log.location="+location+" ");
				}
				
				if(searchtext!=null){
					buffer.append("and (inventory_product_return_log.grnreturnid='"+searchtext+"' or inventory_vendor.name like '%"+searchtext+"' or inventory_vendor.name like '"+searchtext+"%' ) ");
				}
				buffer.append("group by grnreturnid ");
				
				PreparedStatement ps=connection.prepareStatement(buffer.toString());
				ResultSet rs =ps.executeQuery();
				while(rs.next()){
					 res++;
				}
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			return res;
		}

		public int getTodayReturnCount() {

			int res=0;
			
			try {
				Calendar calendar=Calendar.getInstance();
				SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
				String date=dateFormat.format(calendar.getTime());
				String todate=date+" 23:59:59";
				StringBuffer buffer= new StringBuffer();
				buffer.append("SELECT count(*) from inventory_return_grn_parent where datetime between '"+date+"' and '"+todate+"' ");
				PreparedStatement ps=connection.prepareStatement(buffer.toString());
				ResultSet rs= ps.executeQuery();
				while(rs.next()){
					res= rs.getInt(1);
				}
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			return res;
		}

		public String getMedicineShedule(String catalogueid) {
		
			String res="";
			try {
				 String sql="SELECT shedule from inventory_catalogue where id="+catalogueid+" ";
				 PreparedStatement ps=connection.prepareStatement(sql);
				 ResultSet rs =ps.executeQuery();
				 while(rs.next()){
					  res =rs.getString(1);
					  if(res==null){
						  res="Regular";
					  }
					  if(res.equals("")){
						  res="Regular";
					  }
					  
				 }
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			
			return res;
		}

		public int updateCatalogueMRP(Product product) {

			int res=0;
			try {
				  
				  String sql="update inventory_catalogue set mrp=?,purchase_price=?,sale_price=?,hsnno=?, pack=?, mfg=?, gst=? where id="+product.getCatalogueid()+" ";
				  PreparedStatement ps=connection.prepareStatement(sql);
				  ps.setString(1, product.getMrp());
				  ps.setString(2, product.getPurchase_price());
				  ps.setString(3, product.getSale_price());
				  ps.setString(4, product.getHsnno());
				  ps.setString(5, product.getPack());
				  ps.setString(6, product.getMfg());
				  ps.setString(7, product.getVat());
				  res= ps.executeUpdate();
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			return res;
		}

		public boolean checkCatalogueExist(String name, String location) {

			try {
				String sql="select id from inventory_catalogue where product_name='"+name+"' and location='"+location+"'  ";
				PreparedStatement ps=connection.prepareStatement(sql);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
					 return true;
				}
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			return false;
		}
		
		public int getStockFromProduct(String catalogueid) {

			int res=0;
			try {
				
				StringBuffer buffer= new StringBuffer();
				buffer.append("select sum(inventory_product.stock) from inventory_product ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id=inventory_product.catalogueid ");
				buffer.append("where inventory_catalogue.id="+catalogueid+" ");
				
				PreparedStatement ps=connection.prepareStatement(buffer.toString());
				ResultSet rs =ps.executeQuery();
				
				while(rs.next()){
					 res =rs.getInt(1);
				}
				
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			
			return res;
			
		}

		public int addProductToVendor(int prodid, String vendorid) {
			int result = 0;
			try {
				String sql = "select id,products from inventory_vendor where id="+vendorid+" ";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					String temp = rs.getString(2) + "," + prodid;
					String sql2 = "update inventory_vendor set products='" + temp + "' where id=" + rs.getInt(1) + "";
					PreparedStatement statement = connection.prepareStatement(sql2);
					result = statement.executeUpdate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public int getStockAvailable(String catalogueid) {

			int res=0;
			try {
				
				StringBuffer buffer= new StringBuffer();
				buffer.append("select sum(inventory_product.stock) from inventory_product inner join ");
				buffer.append("inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
				buffer.append("where inventory_product.catalogueid="+catalogueid+" and inventory_product.mrp!=0  ");
				PreparedStatement ps=connection.prepareStatement(buffer.toString());
				
				ResultSet rs =ps.executeQuery();
				while(rs.next()){
					 res= rs.getInt(1);
				}
				
			} catch (Exception e) {

				e.printStackTrace();
			} 
			return res;
		}

		public int getTotalStockProduct(String catalogueid) {

			int res=0;
			try {
				String sql="select sum(stock) from inventory_product where catalogueid="+catalogueid+" ";
				PreparedStatement ps=connection.prepareStatement(sql);
				ResultSet rs=ps.executeQuery();
				while(rs.next()){
				     res =rs.getInt(1);	
				}
				
			} catch (Exception e) {

				e.printStackTrace();
			} 
			return res;
		}

		public ArrayList<Product> getProfitandLostReport() {

			ArrayList<Product> list= new ArrayList<Product>();
			try {
				
				 String sql="select id,debit,clientid,pclientid,discount,cgst from apm_medicine_bill where apm_medicine_bill.isreturn=0 and deleted=0 order by id asc ";
				 PreparedStatement ps=connection.prepareStatement(sql);
				 ResultSet rs= ps.executeQuery();
				 while(rs.next()){
					  
					   Product product =new Product();
					   int billno= rs.getInt(1);
					   double total =rs.getDouble(2);
					   double discount= rs.getDouble(5);
					   double vat= rs.getDouble(6)*2;
					   product.setId(billno);
					   product.setName("-");
					   product.setBillno(rs.getString(1));
					   product.setDiscount(DateTimeUtils.changeFormat(discount));
					   double totalpurchasePrice = getTotalPurchasePriceofBill(String.valueOf(billno));
					   double profit=total- (totalpurchasePrice+discount +vat) ;
					 
					   product.setProfit(DateTimeUtils.changeFormat(profit));
					   product.setPurchase_price(DateTimeUtils.changeFormat(totalpurchasePrice));
					   product.setVat(DateTimeUtils.changeFormat(vat));
					   product.setTotalamt(DateTimeUtils.changeFormat(total));
					   
					   list.add(product);
				 }
				 
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			return list;
		}
		
		private double getTotalPurchasePriceofBill(String billno ){
			
			StringBuffer buffer= new StringBuffer();
			double res=0;
			try {
				
				buffer.append("select purchaseprice, pack,apm_medicine_charges.quantity from apm_medicine_charges inner join inventory_product ");
				buffer.append("on  apm_medicine_charges.product_id= inventory_product.id where apm_medicine_charges.invoiceid="+billno+" ");
				
				PreparedStatement ps=connection.prepareStatement(buffer.toString());
				ResultSet rs= ps.executeQuery();
				while(rs.next()){

					   double pur= rs.getDouble(1);
					   int pack= rs.getInt(2);
					   
					   double temp= pur/pack;
					   temp=temp*rs.getInt(3);
					   res= res+temp;
				}
				
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			return res;
			
		}

		public String getPharakmacyUserLocation(String userid) {
			String res = "";
			try {
				String sql ="select location from apm_pharmacy_user where userid='"+userid+"'";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					res = rs.getString(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

		public ArrayList<Product> getApproveParentProductTransferData(String searchtext, String locatioon) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
			IndentDAO indentDAO = new JDBCIndentDAO(connection);
			try {

				StringBuffer buffer = new StringBuffer();
				
				buffer.append("select id, request_date, issued_date, from_location, to_location,time,r_status,req_or_trans, admin_notes, head_notes, check_availability_date, admin_aprove_date, head_aprove_date,expected_date,userid,admin_approve_userid,indentreq,warehouse_id from inventory_parent_transfer_log ");
				buffer.append("where deleted=0 and r_status='1'  ");
				if(searchtext!=null){
					buffer.append("and id='"+searchtext+"' ");
				}
				if(!locatioon.equals("0")){
					buffer.append("and warehouse_id='"+locatioon+"' ");
				}
				buffer.append("order by id desc ");
				
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					Product product = new Product();
					product.setParentid("" + rs.getInt(1));
					String request_date = DateTimeUtils.getCommencingDate1(rs.getString(2));
					String issued_date = DateTimeUtils.getCommencingDate1(rs.getString(3));
					String time = rs.getString(6);
					product.setRequest_date(request_date + " " + time);
					product.setIssued_date(issued_date + " " + time);
					// product.setRequest_date(rs.getString(2));
					// product.setIssued_date(rs.getString(3));
					String fromlocation = pharmacyLocationNameByID(rs.getString(4));
					String tolocation = pharmacyLocationNameByID(rs.getString(5));
					product.setStatus(rs.getString(7));
					product.setFrom_location(fromlocation);
					product.setTo_location(tolocation);
					product.setReq_or_transfer(rs.getString(8));
					String adminnotes = "", headnotes = "", check_availabity_date = "", admin_aprove_date = "",
							head_aprove_date = "";
					if (rs.getString(9) != null) {
						adminnotes = rs.getString(9);
					}
					if (rs.getString(10) != null) {
						headnotes = rs.getString(10);
					}
					if (rs.getString(11) != null) {
						check_availabity_date = rs.getString(11);
					}
					if (rs.getString(12) != null) {
						admin_aprove_date = rs.getString(12);
						String[] data = admin_aprove_date.split(" ");
						String date = DateTimeUtils.getCommencingDate1(data[0]);
						admin_aprove_date = date + " " + data[1];
					}
					if (rs.getString(13) != null) {
						head_aprove_date = rs.getString(13);
						String[] data = head_aprove_date.split(" ");
						String date = DateTimeUtils.getCommencingDate1(data[0]);
						head_aprove_date = date + " " + data[1];
					}
					product.setAdmin_notes(adminnotes);
					product.setHead_notes(headnotes);
					product.setCheck_availability_date(check_availabity_date);
					product.setAdmin_aprove_date(admin_aprove_date);
					product.setHead_aprove_date(head_aprove_date);
					product.setExpectedDate(rs.getString(14));

					Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(rs.getString(15));

					if (priscription.getFullname() != null) {
						product.setUserid(priscription.getFullname());
					} else {
						product.setUserid(rs.getString(15));
					}

					product.setAdmin_approve_userid(DateTimeUtils.isNull(rs.getString(16)));
					product.setIndent(rs.getInt(17));
					
					//10Jan 2018 Akash set requested department
					String name = indentDAO.getWarehouseNameFromId(rs.getString(18));
					product.setWarehouse_id(rs.getString(18));
					product.setWarehouse_name(name);
					boolean flag2 = false;
					if(!locatioon.equals("0")){
						flag2 = indentDAO.checkLocationInWarehouseid(locatioon);
					}
					int deilverproduct = 0;
					if(flag2==true || locatioon.equals("0")){
						deilverproduct = 1;
					}
					product.setDeilverproduct(deilverproduct);
					arrayList.add(product);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		public ArrayList<Product> getRequestParentProductTransferData(String searchtext, String locatioon,String fromdate,String todate) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
			IndentDAO indentDAO = new JDBCIndentDAO(connection);
			try {

				StringBuffer buffer = new StringBuffer();
				
				buffer.append("select id, request_date, issued_date, from_location, to_location,time,r_status,req_or_trans, admin_notes, head_notes, check_availability_date, admin_aprove_date, head_aprove_date,expected_date,userid,admin_approve_userid,indentreq,warehouse_id from inventory_parent_transfer_log ");
				buffer.append("where deleted=0 and r_status='0'  ");
				if(searchtext!=null){
					buffer.append("and id='"+searchtext+"' ");
				}
				if(fromdate!=null&&todate!=null){
					buffer.append(" and request_date between '"+fromdate+"' and '"+todate+" "+"23.59.59'  ");
				}
				buffer.append("order by id desc ");
				
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					Product product = new Product();
					product.setParentid("" + rs.getInt(1));
					String request_date = DateTimeUtils.getCommencingDate1(rs.getString(2));
					String issued_date = DateTimeUtils.getCommencingDate1(rs.getString(3));
					String time = rs.getString(6);
					product.setRequest_date(request_date + " " + time);
					product.setIssued_date(issued_date + " " + time);
					// product.setRequest_date(rs.getString(2));
					// product.setIssued_date(rs.getString(3));
					String fromlocation = pharmacyLocationNameByID(rs.getString(4));
					String tolocation = pharmacyLocationNameByID(rs.getString(5));
					product.setStatus(rs.getString(7));
					product.setFrom_location(fromlocation);
					product.setTo_location(tolocation);
					product.setReq_or_transfer(rs.getString(8));
					String adminnotes = "", headnotes = "", check_availabity_date = "", admin_aprove_date = "",
							head_aprove_date = "";
					if (rs.getString(9) != null) {
						adminnotes = rs.getString(9);
					}
					if (rs.getString(10) != null) {
						headnotes = rs.getString(10);
					}
					if (rs.getString(11) != null) {
						check_availabity_date = rs.getString(11);
					}
					if (rs.getString(12) != null) {
						admin_aprove_date = rs.getString(12);
						String[] data = admin_aprove_date.split(" ");
						String date = DateTimeUtils.getCommencingDate1(data[0]);
						admin_aprove_date = date + " " + data[1];
					}
					if (rs.getString(13) != null) {
						head_aprove_date = rs.getString(13);
						String[] data = head_aprove_date.split(" ");
						String date = DateTimeUtils.getCommencingDate1(data[0]);
						head_aprove_date = date + " " + data[1];
					}
					product.setAdmin_notes(adminnotes);
					product.setHead_notes(headnotes);
					product.setCheck_availability_date(check_availabity_date);
					product.setAdmin_aprove_date(admin_aprove_date);
					product.setHead_aprove_date(head_aprove_date);
					product.setExpectedDate(rs.getString(14));

					Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(rs.getString(15));

					if (priscription.getFullname() != null) {
						product.setUserid(priscription.getFullname());
					} else {
						product.setUserid(rs.getString(15));
					}

					product.setAdmin_approve_userid(DateTimeUtils.isNull(rs.getString(16)));
					product.setIndent(rs.getInt(17));
					
					//10Jan 2018 Akash set requested department
					String name = indentDAO.getWarehouseNameFromId(rs.getString(18));
					product.setWarehouse_id(rs.getString(18));
					product.setWarehouse_name(name);
					boolean flag2 = false;
					if(!locatioon.equals("0")){
						flag2 = indentDAO.checkLocationInWarehouseid(locatioon);
					}
					int deilverproduct = 0;
					if(flag2==true || locatioon.equals("0")){
						deilverproduct = 1;
					}
					product.setDeilverproduct(deilverproduct);
					arrayList.add(product);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		public ArrayList<Product> getstockValuationReport(String location, String filteroforder) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			try {
				StringBuilder builder = new StringBuilder();
				builder.append("select inventory_catalogue.id,product_name,sum(stock),(sum(purchaseprice)*sum(stock)),((sum(purchaseprice)*sum(stock))/sum(stock)) from inventory_product ");
				builder.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
				builder.append("where inventory_catalogue.location='"+location+"' and product_name is not null ");
				builder.append("group by catalogueid  ");
				if(filteroforder.equals("0")){
					builder.append("order by product_name asc ");
				}else if(filteroforder.equals("1")){
					builder.append("order by sum(stock) desc ");
				}
				PreparedStatement preparedStatement = connection.prepareStatement(builder.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					Product product = new Product();
					product.setCatalogueid(rs.getString(1));
					product.setProd_name(rs.getString(2));
					product.setTotalstock(rs.getInt(3));
					if(rs.getString(4)==null){
						product.setTotalpurchaseprice(0);
					}else{
						product.setTotalpurchaseprice(Math.round(Double.parseDouble(rs.getString(4)) * 100.0) / 100.0);
					}
					if(rs.getString(5)==null){
						product.setAvgcost(0);
					}else{
						product.setAvgcost(Math.round(Double.parseDouble(rs.getString(5)) * 100.0) / 100.0);
					}
					String lastgrndate = getLastGRN(rs.getString(1));
					product.setLastgrndate(lastgrndate);
					arrayList.add(product);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		private String getLastGRN(String string) {
			String lastdate ="";
			try {
				String sql ="SELECT date FROM inventory_procurement where catalogueid='"+string+"'";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					lastdate = DateTimeUtils.getCommencingDate1(rs.getString(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return lastdate;
		}

		public ArrayList<Product> getDeptMaterialIssueList(String fromDate, String toDate,String product1, String department, String warehouseid ) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			try {
				toDate = toDate+" "+"59:59:59";
				StringBuffer buffer = new StringBuffer();
				buffer.append("select from_location,name from inventory_parent_transfer_log ");
				buffer.append("inner join inventory_request_temp_log on inventory_parent_transfer_log.id = inventory_request_temp_log.parentid ");
				buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.from_location ");
				buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and from_location!='' ");
				if(!department.equals("")){
					buffer.append(" and inventory_parent_transfer_log.from_location='"+department+"' ");
				}
				if(!product1.equals("")){
					buffer.append(" and inventory_request_temp_log.catlogueid='"+product1+"' ");
				}
				/*if(!warehouseid.equals("")){
					buffer.append(" and inventory_parent_transfer_log.warehouse_id='"+product1+"' ");
				}*/
				buffer.append("group by from_location ");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				int i=0;
				while (rs.next()) {
					Product product = new Product();
					product.setFrom_location(rs.getString(1));
					product.setLocationName(rs.getString(2));
					ArrayList<Product> materiallist = getDeptMaterialproduct(fromDate,toDate,rs.getInt(1),product1);
					product.setMateriallist(materiallist);
					int size = materiallist.size();
					if (size > 0) {
						String new_invistigation = materiallist.get(size - 1).getTotal();
						String new_collected = materiallist.get(size - 1).getTotalamt();
						product.setTotal(new_invistigation);
						product.setTotalamt(new_collected);
					} else {
						product.setTotal("0");
						product.setTotalamt("0");
					}
					/*product.setQuantitycount(i);*/
					arrayList.add(product);
				}
				System.out.println(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		private ArrayList<Product> getDeptMaterialproduct(String fromDate, String toDate, int int1, String product1) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			try {
				/*toDate = toDate+" "+"59:59:59";*/
				StringBuffer buffer = new StringBuffer();
				buffer.append("select catlogueid,product_name,sum(qty) from inventory_parent_transfer_log ");
				buffer.append("inner join inventory_request_temp_log on inventory_parent_transfer_log.id = inventory_request_temp_log.parentid ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_request_temp_log.catlogueid ");
				buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and from_location='"+int1+"' ");
				if(!product1.equals("")){
					buffer.append(" and inventory_request_temp_log.catlogueid='"+product1+"' ");
				}
				buffer.append("group by catlogueid ");
				buffer.append("order by sum(qty) desc");
				
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				int total=0;
				double amount =0;
				while (rs.next()) {
					Product product = new Product();
					product.setCatalogueid(rs.getString(1));
					product.setProd_name(rs.getString(2));
					product.setQty(rs.getString(3));
					String purchaseprice = getMaterialPurPrice(rs.getString(1),int1);
					Double price = Double.parseDouble(purchaseprice)*rs.getDouble(3);
					price = Math.round(price * 100.0) / 100.0;
					product.setPurchase_price(""+price);
					amount = amount+price;
					total = total+ rs.getInt(3);
					product.setTotal(""+total);
					product.setTotalamt(String.valueOf(Math.round(amount * 100.0) / 100.0));
					arrayList.add(product);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		private String getMaterialPurPrice(String catalogueid, int int1) {
			String res = "0";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select (sum(purchaseprice)/sum(pack)) from inventory_product ");
			buffer.append("where location='"+int1+"' and catalogueid='"+catalogueid+"' ");
			buffer.append("group by catalogueid ");
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					res = rs.getString(1);
					if(res==null){
						res="0";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return res;
		}
		
		public ArrayList<Product> getDeptMaterialIssueListNew(String fromDate, String toDate, String productname,
				String department, String warehouseid, String category) {
			ArrayList<Product> materialissuereport = new ArrayList<Product>();
			try {
				ArrayList<Product> directdepartnamelist = getDirectMaterialIssueDept(fromDate,toDate,productname,department,warehouseid,category);
				ArrayList<Product> requestdepartnamelist = getRequestMaterialIssueDept(fromDate,toDate,productname,department,warehouseid,category);
				ArrayList<Product> arrayList = new ArrayList<Product>();
				
				arrayList.addAll(directdepartnamelist);
				arrayList.addAll(requestdepartnamelist);
				
				
				ArrayList<Product> arrayList2 = new ArrayList<Product>();
				int i=0;
				for (Product product : arrayList) {
					
					/*if(arrayList.size()==2){
						if(i==0){
							arrayList2.add(product);
						}
						if(i!=0){
							boolean flag = false;
							for (Product product1 : arrayList2) {
								if(product1.getName().equals(product.getName())){
									flag= true;
									break;
								}
							}
							if(!flag){
								arrayList2.add(product);
							}
						}
					}else{*/
						if(i==0){
							arrayList2.add(product);
						}
						if(i!=0){
							boolean flag = false;
							for (Product product1 : arrayList2) {
								if(product1.getName().equals(product.getName())){
									flag= true;
									break;
								}
							}
							if(!flag){
								arrayList2.add(product);
							}
						}
					/*}*/
					
					
					i++;
				}
				
				
				for (Product string : arrayList2) {
					Product product = new Product();
					product.setLocationName(string.getName());
					/*ArrayList<Product> materiallist = getDeptMaterialproduct(fromDate,toDate,rs.getInt(1),product1);*/
					ArrayList<Product> directmateriallist = getDirectDeptMaterialproduct(fromDate, toDate, productname, string.getTo_location(),string.getWarehouse_id(),category,string.getName());
					ArrayList<Product> requestmateriallist = getRequestDeptMaterialproduct(fromDate, toDate, productname, string.getTo_location(),string.getWarehouse_id(),category,string.getName());
					ArrayList<Product> materiallist = new ArrayList<Product>();
					materiallist.addAll(requestmateriallist);
					materiallist.addAll(directmateriallist);
					product.setMateriallist(materiallist);
					int size = directmateriallist.size();
					if (size > 0) {
						String new_invistigation = directmateriallist.get(size - 1).getTotal();
						String new_collected = directmateriallist.get(size - 1).getTotalamt();
						product.setTotal(new_invistigation);
						product.setTotalamt(new_collected);
					} else {
						product.setTotal("0");
						product.setTotalamt("0");
					}
					
					int size1 = requestmateriallist.size();
					if (size1 > 0) {
						String new_invistigation = requestmateriallist.get(size1 - 1).getTotal();
						String new_collected = requestmateriallist.get(size1 - 1).getTotalamt();
						String data = String.valueOf(Double.parseDouble(new_collected) +  Double.parseDouble(product.getTotalamt()));
						String qty = String.valueOf(Integer.parseInt(new_invistigation) + Integer.parseInt(product.getTotal()));
						product.setTotal(qty);
						product.setTotalamt(data);
					} else {
						String data = String.valueOf(0 +  Double.parseDouble(product.getTotalamt()));
						String qty = String.valueOf(0 + Integer.parseInt(product.getTotal()));
						product.setTotal(qty);
						product.setTotalamt(data);
					}
					
					
					materialissuereport.add(product);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return materialissuereport;
		}

		private ArrayList<Product> getRequestDeptMaterialproduct(String fromDate, String toDate, String productname,
				String department, String from_location, String category, String locationName) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			try {
				toDate = toDate+" "+"59:59:59";
				StringBuffer buffer = new StringBuffer();
				buffer.append("select catlogueid,product_name,sum(qty),inventory_catalogue.categoryid,inventory_catalogue.subcategoryid from inventory_parent_transfer_log ");
				buffer.append("inner join inventory_request_temp_log on inventory_request_temp_log.parentid = inventory_parent_transfer_log.id ");
				buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.from_location ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_request_temp_log.catlogueid ");
				buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='0' and warehouse_id='"+from_location+"' and name is not null ");
				if(!productname.equals("")){
					buffer.append(" and inventory_catalogue.product_name like '"+productname+"%' ");
				}
				if(!category.equals("0")){
					buffer.append("and inventory_catalogue.categoryid='"+category+"' ");
				}
				buffer.append(" and inventory_parent_transfer_log.from_location='"+department+"' ");
				buffer.append("group by catlogueid ");
				buffer.append("order by sum(qty) desc");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				int total=0;
				double amount =0;
				while (rs.next()) {
					Product product = new Product();
					product.setCatalogueid(rs.getString(1));
					product.setProd_name(rs.getString(2));
					product.setQty(rs.getString(3));
					//String purchaseprice = getMaterialPurPrice(rs.getString(1),Integer.parseInt(from_location));
					Product mainpurchaseprice = getMaterialPurchasePrice(fromDate,toDate,productname,department,from_location,rs.getString(1),category); 
					//Double price = Double.parseDouble(purchaseprice)*rs.getDouble(3);
					//Double price = purchaseprice*rs.getDouble(3);
					Double price =mainpurchaseprice.getTotalpurchaseprice();
					price = Math.round(price * 100.0) / 100.0;
					product.setPurchase_price(""+price);
					amount = amount+price;
					total = total+ rs.getInt(3);
					product.setTotal(""+total);
					product.setTotalamt(String.valueOf(Math.round(amount * 100.0) / 100.0));
					product.setFromwhere("Request Indent");
					Product category1 = getCategory(rs.getString(4));
					product.setCategory(category1.getName());
					Product subcategory = getSubCategory(rs.getString(5));
					product.setSubcategory(subcategory.getName());
					product.setNewpurprice(Math.round(mainpurchaseprice.getNewpurprice() *100.0)/100.0);
					product.setVat(mainpurchaseprice.getVat());
					product.setLocationName(locationName);
					String fromdate1= fromDate.split("-")[0] +"-"+fromDate.split("-")[1];
					String todate1= toDate.split("-")[0] +"-"+toDate.split("-")[1];
					String frommonth="";
					String tomonth="";
					if(fromdate1.equals(todate1)){
						frommonth = DateTimeUtils.getMonthName(Integer.parseInt(fromDate.split("-")[1]))+"-"+fromDate.split("-")[0];
					}else{
						frommonth = DateTimeUtils.getMonthName(Integer.parseInt(fromDate.split("-")[1]))+"-"+fromDate.split("-")[0];
						tomonth = DateTimeUtils.getMonthName(Integer.parseInt(todate1.split("-")[1]))+"-"+todate1.split("-")[0];
					}
					product.setFrommonth(frommonth);
					product.setTomonth(tomonth);
					arrayList.add(product);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		private Product getMaterialPurchasePrice(String fromDate, String toDate, String productname, String department,
				String from_location,String catlogueid, String category) {
			Product product1 = new  Product();
			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append("select catlogueid,product_name,qty,old_prodid from inventory_parent_transfer_log ");
				buffer.append("inner join inventory_request_temp_log on inventory_request_temp_log.parentid = inventory_parent_transfer_log.id ");
				buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.from_location ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_request_temp_log.catlogueid ");
				buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='0' and warehouse_id='"+from_location+"' and name is not null ");
				if(!productname.equals("")){
					buffer.append(" and inventory_catalogue.product_name like '"+productname+"%' ");
				}
				if(!category.equals("0")){
					buffer.append("and inventory_catalogue.categoryid='"+category+"' ");
				}
				buffer.append(" and inventory_parent_transfer_log.from_location='"+department+"' ");
				buffer.append("and catlogueid='"+catlogueid+"' ");
				buffer.append("order by qty desc");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				double purchaseprice=0;
				double newpurprice =0;
				String vat ="0";
				while (rs.next()) {
					Product product = getProduct(rs.getString(4));
					if(product.getPack()!=null){
						if(product.getPack().equals("") || product.getPack().equals("0")){
							product.setPack("1");
						}
					}else{
						product.setPack("1");
					}
					double purprice = Double.parseDouble(product.getPurchase_price())/Integer.parseInt(product.getPack());
					newpurprice = purprice;
					purprice = purprice*rs.getInt(3);
					purchaseprice = purchaseprice+purprice;
					vat =product.getVat();
				}
				product1.setTotalpurchaseprice(purchaseprice);
				product1.setNewpurprice(newpurprice);
				product1.setVat(vat);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return product1;
		}

		private ArrayList<Product> getDirectDeptMaterialproduct(String fromDate, String toDate, String productname,
				String department, String to_location, String category, String locationname) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			try {
				toDate = toDate+" "+"59:59:59";
				StringBuffer buffer = new StringBuffer();
				buffer.append("select catlogueid,product_name,sum(qty),inventory_catalogue.categoryid,inventory_catalogue.subcategoryid from inventory_parent_transfer_log ");
				buffer.append("inner join inventory_transfer_log on inventory_transfer_log.parentid = inventory_parent_transfer_log.id ");
				buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.to_location ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_transfer_log.catlogueid ");
				buffer.append("where request_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='1' and from_location='"+to_location+"' and name is not null ");
				if(!productname.equals("")){
					buffer.append(" and inventory_catalogue.product_name like '"+productname+"%' ");
				}
				if(!category.equals("0")){
					buffer.append("and inventory_catalogue.categoryid='"+category+"' ");
				}
				buffer.append(" and inventory_parent_transfer_log.to_location='"+department+"' ");
				buffer.append("group by catlogueid ");
				buffer.append("order by sum(qty) desc");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				int total=0;
				double amount =0;
				while (rs.next()) {
					Product product = new Product();
					product.setCatalogueid(rs.getString(1));
					product.setProd_name(rs.getString(2));
					product.setQty(rs.getString(3));
					//String purchaseprice = getMaterialPurPrice(rs.getString(1),Integer.parseInt(to_location));
					//Double price = Double.parseDouble(purchaseprice)*rs.getDouble(3);
					Product mainpurchaseprice = getMaterialDirectPurchasePrice(fromDate,toDate,productname,department,to_location,rs.getString(1),category); 
					Double price =mainpurchaseprice.getTotalpurchaseprice();
					price = Math.round(price * 100.0) / 100.0;
					product.setPurchase_price(""+price);
					amount = amount+price;
					total = total+ rs.getInt(3);
					product.setTotal(""+total);
					product.setTotalamt(String.valueOf(Math.round(amount * 100.0) / 100.0));
					product.setFromwhere("Direct Indent");
					
					Product category1 = getCategory(rs.getString(4));
					product.setCategory(category1.getName());
					Product subcategory = getSubCategory(rs.getString(5));
					product.setSubcategory(subcategory.getName());
					product.setNewpurprice(Math.round(mainpurchaseprice.getNewpurprice() *100.0)/100.0);
					product.setVat(mainpurchaseprice.getVat());
					product.setLocationName(locationname);
					
					String fromdate1= fromDate.split("-")[0] +"-"+fromDate.split("-")[1];
					String todate1= toDate.split("-")[0] +"-"+toDate.split("-")[1];
					String frommonth="";
					String tomonth="";
					if(fromdate1.equals(todate1)){
						frommonth = DateTimeUtils.getMonthName(Integer.parseInt(fromDate.split("-")[1]))+"-"+fromDate.split("-")[0];
					}else{
						frommonth = DateTimeUtils.getMonthName(Integer.parseInt(fromDate.split("-")[1]))+"-"+fromDate.split("-")[0];
						tomonth = DateTimeUtils.getMonthName(Integer.parseInt(todate1.split("-")[1]))+"-"+todate1.split("-")[0];
					}
					product.setFrommonth(frommonth);
					product.setTomonth(tomonth);
					
					arrayList.add(product);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		private Product getMaterialDirectPurchasePrice(String fromDate, String toDate, String productname,
				String department, String to_location,String catlogueid, String category) {
			Product product1 = new  Product();
			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append("select catlogueid,product_name,qty,old_prodid from inventory_parent_transfer_log ");
				buffer.append("inner join inventory_transfer_log on inventory_transfer_log.parentid = inventory_parent_transfer_log.id ");
				buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.to_location ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_transfer_log.catlogueid ");
				buffer.append("where request_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='1' and from_location='"+to_location+"' and name is not null ");
				if(!productname.equals("")){
					buffer.append(" and inventory_catalogue.product_name like '"+productname+"%' ");
				}
				if(!category.equals("0")){
					buffer.append("and inventory_catalogue.categoryid='"+category+"' ");
				}
				buffer.append(" and inventory_parent_transfer_log.to_location='"+department+"' ");
				buffer.append("and catlogueid='"+catlogueid+"' ");
				buffer.append("order by qty desc");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				double purchaseprice=0;
				double newpurprice =0;
				String vat ="0";
				while (rs.next()) {
					Product product = getProduct(rs.getString(4));
					if(product.getPack()!=null){
						if(product.getPack().equals("")  || product.getPack().equals("0")){
							product.setPack("1");
						}
					}else{
						product.setPack("1");
					}
					double purprice = Double.parseDouble(product.getPurchase_price())/Integer.parseInt(product.getPack());
					newpurprice = purprice;
					vat =product.getVat();
					purprice = purprice*rs.getInt(3);
					purchaseprice = purchaseprice+purprice;
					
				}

				product1.setTotalpurchaseprice(purchaseprice);
				product1.setNewpurprice(newpurprice);
				product1.setVat(vat);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return product1;
		}

		private ArrayList<Product> getRequestMaterialIssueDept(String fromDate, String toDate, String productname,
				String department, String warehouseid,String category) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			try {
				toDate = toDate+" "+"59:59:59";
				StringBuffer buffer = new StringBuffer();
				buffer.append("select warehouse_id,from_location,name from inventory_parent_transfer_log ");
				buffer.append("inner join inventory_request_temp_log on inventory_request_temp_log.parentid = inventory_parent_transfer_log.id ");
				buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.from_location ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_request_temp_log.catlogueid ");
				buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='0' and warehouse_id='"+warehouseid+"' and name is not null ");
				if(!productname.equals("")){
					buffer.append(" and inventory_catalogue.product_name like '"+productname+"%' ");
				}
				if(!category.equals("0")){
					buffer.append("and inventory_catalogue.categoryid='"+category+"' ");
				}
				if(!department.equals("")){
					buffer.append(" and inventory_parent_transfer_log.from_location='"+department+"' ");
				}
				buffer.append("group by from_location ");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					Product product = new Product();
					product.setWarehouse_id(rs.getString(1));
					product.setTo_location(rs.getString(2));
					product.setName(rs.getString(3));
					arrayList.add(product);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		private ArrayList<Product> getDirectMaterialIssueDept(String fromDate, String toDate, String productname,
				String department, String warehouseid, String category) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			try {
				toDate = toDate+" "+"59:59:59";
				StringBuffer buffer = new StringBuffer();
				buffer.append("select from_location,to_location,name from inventory_parent_transfer_log ");
				buffer.append("inner join inventory_transfer_log on inventory_transfer_log.parentid = inventory_parent_transfer_log.id ");
				buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.to_location ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_transfer_log.catlogueid ");
				buffer.append("where request_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='1' and from_location='"+warehouseid+"' and name is not null ");
				if(!productname.equals("")){
					buffer.append(" and inventory_catalogue.product_name like '"+productname+"%' ");
				}
				if(!category.equals("0")){
					buffer.append("and inventory_catalogue.categoryid='"+category+"' ");
				}
				if(!department.equals("")){
					buffer.append(" and inventory_parent_transfer_log.to_location='"+department+"' ");
				}
				buffer.append("group by to_location ");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					Product product = new Product();
					product.setWarehouse_id(rs.getString(1));
					product.setTo_location(rs.getString(2));
					product.setName(rs.getString(3));
					arrayList.add(product);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		public ArrayList<Product> getNonMovingItemList(String department, String toDate,String filteroforder) {
			ArrayList<Product> arrayList = new ArrayList<Product>();
			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append("select added_date,catalogueid,prodname,sum(stock) from inventory_product ");
				buffer.append("where location='"+department+"' and added_date is not null ");
				buffer.append("group by catalogueid having sum(stock)>0 ");
				buffer.append("order by added_date desc ");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				String status="";
				while (rs.next()) {
					if(rs.getInt(2)==5004){
						System.out.println("");
					}
					Product product = new Product();
					String productids = getProdidfromCatId(department,rs.getString(2));
					product.setStock(rs.getString(4));
					String indentdatediff = getIndentNotMovingItemDate(department,rs.getString(1),rs.getString(2));
					String directtransferdiff = getDirectTransferDiffDate(department,rs.getString(1),rs.getString(2));
					String saledatediff = getMeanSaleDiffernce(productids,rs.getString(1));
					boolean nowhere=false;
					String test="";
					if(indentdatediff.equals("") && directtransferdiff.equals("") && saledatediff.equals("")){
						String date1[] = rs.getString(1).split("-");
						String date= date1[2]+"/"+date1[1]+"/"+date1[0];
						
						Date d1=new SimpleDateFormat("dd/MM/yyyy").parse(date);
						Date d2= new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
						long diff=d2.getTime()- d1.getTime();
						long difference= (Math.abs((diff / (1000*60*60*24))));
						test = ""+difference;
						nowhere=true;
					}else if(!indentdatediff.equals("") || (!directtransferdiff.equals("")) || (!saledatediff.equals(""))){
						if(!indentdatediff.equals("")){
							test = indentdatediff;
						}
						if(!directtransferdiff.equals("")){
							if(test.equals("")){
								test=directtransferdiff;
							}else if(Integer.parseInt(test)>Integer.parseInt(directtransferdiff)){
								test = directtransferdiff;
							}
						}
						if(!saledatediff.equals("")){
							if(test.equals("")){
								test=saledatediff;
							}else if(Integer.parseInt(test)>Integer.parseInt(saledatediff)){
								test = saledatediff;
							}
						}
					}
					product.setProd_name(rs.getString(3));
					try {
						if(filteroforder.equals("0")){
							if(Integer.parseInt(test)>180||nowhere){
								status="1";
								product.setStatus(status);
								arrayList.add(product);
							}else if(Integer.parseInt(test)<180 && Integer.parseInt(test)>60){
								status="2";
								product.setStatus(status);
								arrayList.add(product);
							}else if(Integer.parseInt(test)<60 && Integer.parseInt(test)>=0){
								status="3";
								product.setStatus(status);
								arrayList.add(product);
							}
							
						}else{
							if(filteroforder.equals("1")){
								if(Integer.parseInt(test)>180||nowhere){
									status="1";
									product.setStatus(status);
									arrayList.add(product);
								}
							}else if(filteroforder.equals("2")){
								if(Integer.parseInt(test)<180 && Integer.parseInt(test)>60&&(!nowhere)){
									status="2";
									product.setStatus(status);
									arrayList.add(product);
								}
							}else if(filteroforder.equals("3")){
								if(Integer.parseInt(test)<60 && Integer.parseInt(test)>=0&&(!nowhere)){
									status="3";
									product.setStatus(status);
									arrayList.add(product);
								}
							}
						}
						
					} catch (Exception e) {
						
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arrayList;
		}

		private String getSaleDiffernce(String productids, String added_date) {
			String res="";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select commencing from apm_medicine_charges ");
			buffer.append("where returnbillno='0' and product_id in ("+productids+") ");
			buffer.append("order by commencing  desc ");
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					String date1[] = added_date.split("-");
					String date= date1[2]+"/"+date1[1]+"/"+date1[0];
					
					Date d1=new SimpleDateFormat("dd/MM/yyyy").parse(date);
					Date d2= new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(1));
					long diff=d2.getTime()- d1.getTime();
					long difference= (Math.abs((diff / (1000*60*60*24))));
					res = ""+difference;
					if(difference>0){
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
				
			return res;
		}
		
		
		
		private String getMeanSaleDiffernce(String productids, String added_date) {
			String res="";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select commencing from apm_medicine_charges ");
			buffer.append("where returnbillno='0' and product_id in ("+productids+") ");
			buffer.append("order by commencing  asc ");
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				String date1[] = added_date.split("-");
				String date= date1[2]+"/"+date1[1]+"/"+date1[0];
				
				int count=0;
				String cdate=added_date;
				String datecont=added_date;
				long finaldiff=0;
				while (rs.next()) {
					if(rs.getString(1)!=null){
				
					if((count!=0)&&(datecont!=null)){
						cdate=datecont;
					}
					datecont=rs.getString(1);
					
					
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				    Calendar cal = Calendar.getInstance();
				   
				    String finaltoDate = dateFormat.format(cal.getTime());
					
					
					Date d1=new SimpleDateFormat("yyyy-MM-dd").parse(finaltoDate);
					Date d2= new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(1));
					long diff=d2.getTime()- d1.getTime();
					long difference= (Math.abs((diff / (1000*60*60*24))))+1;
					finaldiff=difference+finaldiff;
					count=count+1;
					res = ""+difference;
					/*if(difference>0){
						break;
					}*/
					}
				}
				if(finaldiff>0){
					res=""+(finaldiff/count);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
				
			return res;
		}

		private String getProdidfromCatId(String department, String string) {
			String res="0";
			try {
				String sql ="select id from inventory_product where catalogueid='"+string+"' and location='"+department+"'";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					res=res+","+rs.getInt(1);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

		private String getDirectTransferDiffDate(String department, String added_date, String catid) {
			String res = "";
			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append("select request_date from inventory_transfer_log ");
				buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id = inventory_transfer_log.parentid ");
				buffer.append("where req_trans_ret!='0' and catlogueid='"+catid+"' and from_location='"+department+"' ");
				buffer.append("order by request_date  desc");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					String date1[] = added_date.split("-");
					String date= date1[2]+"/"+date1[1]+"/"+date1[0];
					
					/*String df[]=rs.getString(1).split(" ");
					String todays=df[0];*/
					
					Date d1=new SimpleDateFormat("dd/MM/yyyy").parse(date);
					Date d2= new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(1));
					long diff=d2.getTime()- d1.getTime();
					long difference= (Math.abs((diff / (1000*60*60*24))));
					res = ""+difference;
					if(difference>0){
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

		private String getIndentNotMovingItemDate(String department, String added_date, String catid) {
			String res = "";
			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append("select transfer_date from inventory_request_temp_log ");
				buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id = inventory_request_temp_log.parentid ");
				buffer.append("where catlogueid='"+catid+"' and location='"+department+"' ");
				buffer.append("order by transfer_date  desc");
				PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					String date1[] = added_date.split("-");
					String date= date1[2]+"/"+date1[1]+"/"+date1[0];
					
					String df[]=rs.getString(1).split(" ");
					String todays=df[0];
					
					Date d1=new SimpleDateFormat("dd/MM/yyyy").parse(date);
					Date d2= new SimpleDateFormat("yyyy-MM-dd").parse(todays);
					long diff=d2.getTime()- d1.getTime();
					long difference= (Math.abs((diff / (1000*60*60*24))));
					res = ""+difference;
					if(difference>0){
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

		public ArrayList<Product> getALLStockReport(String location, String orderby, String report_filter, String withstock_filter, String cat_filter,String order_filter,String fromdate, int isprevious, String prductlist, String searchtext) {
			ArrayList<Product> list = new ArrayList<Product>();
			try {
				String toDate="";
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				toDate = dateFormat.format(cal.getTime());
				toDate = DateTimeUtils.getCommencingDate1(toDate);
				InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
				StringBuilder stringBuilder = new StringBuilder();
				String prductlistnew="0";
				if(isprevious>0){
					stringBuilder.append("select inventory_product.id,inventory_product.catalogueid,inventory_product.mrp, inventory_product.purchaseprice, ");
					if(report_filter.equals("3")){
						stringBuilder.append("inventory_product.saleprice,medicine_opening_stock.stock,inventory_product.expiry_date,inventory_product.batch_no,inventory_product.location, ");
						stringBuilder.append("inventory_product.prodtypeid,inventory_catalogue.product_name,inventory_catalogue.genericname,inventory_subcategory.name, ");
						stringBuilder.append("inventory_product.cell,inventory_product.pack,apm_condition.name,inventory_catalogue.genericname,inventory_product.mfg, ");
						stringBuilder.append("inventory_catalogue.categoryid,inventory_category.name,inventory_catalogue.minorder,inventory_catalogue.maxorder, ");
						stringBuilder.append("inventory_product.supplierid,inventory_catalogue.product_code,inventory_product.vat,  ");
						stringBuilder.append("purchaseprice/inventory_product.pack, (purchaseprice/inventory_product.pack) * (medicine_opening_stock.stock),inventory_product.mfg,inventory_catalogue.genericname,sum(medicine_opening_stock.stock * inventory_product.saleprice) ");
					}else{
						stringBuilder.append("inventory_product.saleprice,sum(medicine_opening_stock.stock),inventory_product.expiry_date,inventory_product.batch_no,inventory_product.location, ");
						stringBuilder.append("inventory_product.prodtypeid,inventory_catalogue.product_name,inventory_catalogue.genericname,inventory_subcategory.name, ");
						stringBuilder.append("inventory_product.cell,inventory_product.pack,apm_condition.name,inventory_catalogue.genericname,inventory_product.mfg, ");
						stringBuilder.append("inventory_catalogue.categoryid,inventory_category.name,inventory_catalogue.minorder,inventory_catalogue.maxorder, ");
						stringBuilder.append("inventory_product.supplierid,inventory_catalogue.product_code,inventory_product.vat,  ");
						stringBuilder.append("avg(purchaseprice/inventory_product.pack), avg(purchaseprice/inventory_product.pack) * sum(medicine_opening_stock.stock),inventory_product.mfg,inventory_catalogue.genericname,sum(medicine_opening_stock.stock * inventory_product.saleprice) ");
					}
					stringBuilder.append("from inventory_product ");
					stringBuilder.append("inner join medicine_opening_stock on medicine_opening_stock.prodid = inventory_product.id ");
					stringBuilder.append("inner join inventory_catalogue on inventory_catalogue.id=inventory_product.catalogueid  ");
					stringBuilder.append("left join inventory_subcategory on inventory_subcategory.id=inventory_catalogue.subcategoryid ");
					stringBuilder.append("inner join apm_condition on apm_condition.id = inventory_product.location ");
					stringBuilder.append("left join inventory_category on inventory_category.id = inventory_catalogue.categoryid ");
					stringBuilder.append("where inventory_product.catalogueid!=0 and inventory_product.procurement=0 and medicine_opening_stock.commencing='"+fromdate+"'  ");
					if (!location.equals("0")) {
						stringBuilder.append("and inventory_product.location='" + location + "' ");
					}
					
					if (withstock_filter.equals("2")) {
						stringBuilder.append("and medicine_opening_stock.stock>=0 ");
					}else if(withstock_filter.equals("3")){
						stringBuilder.append("and medicine_opening_stock.stock=0 ");
					}else{
						stringBuilder.append("and medicine_opening_stock.stock>0 ");
					}
					
					
					if (!cat_filter.equals("0")) {
						stringBuilder.append("and inventory_catalogue.categoryid='" + cat_filter + "' ");
					}
					
					if (searchtext != null) {
						stringBuilder.append("and (inventory_product.prodname like ('%" + searchtext + "%') or inventory_catalogue.product_name like ('%" + searchtext
								+ "%')  or inventory_product.genericname like ('%" + searchtext + "%') or inventory_catalogue.genericname like ('%" + searchtext + "%') or inventory_subcategory.name like ('" + searchtext + "%') or inventory_catalogue.product_code like ('%" + searchtext + "%')  ) ");
					}
					
					if(report_filter.equals("3")){
						
					}else if(report_filter.equals("1")){
						stringBuilder.append("group by catalogueid ");
					}else{
						stringBuilder.append("group by catalogueid,batch_no ");
					}
					
					if(orderby.equals("1")){
						stringBuilder.append("order by inventory_catalogue.product_name  ");
					}else if(orderby.equals("2")){
						if(report_filter.equals("3")){
							stringBuilder.append("order by (medicine_opening_stock.stock+0)  ");
						}else{
							stringBuilder.append("order by sum(medicine_opening_stock.stock)  ");
						}
					}else if(orderby.equals("3")){
						stringBuilder.append("order by (inventory_product.mrp+0)  ");
					}
					if (order_filter.equals("desc")) {
						stringBuilder.append("desc");
					} else {
						stringBuilder.append("asc");
					}
				}else{
					stringBuilder.append("select inventory_product.id,inventory_product.catalogueid,inventory_product.mrp, inventory_product.purchaseprice, ");
					if(report_filter.equals("3")){
						stringBuilder.append("inventory_product.saleprice,stock,inventory_product.expiry_date,inventory_product.batch_no,inventory_product.location, ");
						stringBuilder.append("inventory_product.prodtypeid,inventory_catalogue.product_name,inventory_catalogue.genericname,inventory_subcategory.name, ");
						stringBuilder.append("inventory_product.cell,inventory_product.pack,apm_condition.name,inventory_catalogue.genericname,inventory_product.mfg, ");
						stringBuilder.append("inventory_catalogue.categoryid,inventory_category.name,inventory_catalogue.minorder,inventory_catalogue.maxorder, ");
						stringBuilder.append("inventory_product.supplierid,inventory_catalogue.product_code,inventory_product.vat,  ");
						stringBuilder.append("purchaseprice/inventory_product.pack, (purchaseprice/inventory_product.pack) * (inventory_product.stock),inventory_product.mfg,inventory_catalogue.genericname,(inventory_product.stock * inventory_product.saleprice) ");
					}else{
						stringBuilder.append("inventory_product.saleprice,sum(stock),inventory_product.expiry_date,inventory_product.batch_no,inventory_product.location, ");
						stringBuilder.append("inventory_product.prodtypeid,inventory_catalogue.product_name,inventory_catalogue.genericname,inventory_subcategory.name, ");
						stringBuilder.append("inventory_product.cell,inventory_product.pack,apm_condition.name,inventory_catalogue.genericname,inventory_product.mfg, ");
						stringBuilder.append("inventory_catalogue.categoryid,inventory_category.name,inventory_catalogue.minorder,inventory_catalogue.maxorder, ");
						stringBuilder.append("inventory_product.supplierid,inventory_catalogue.product_code,inventory_product.vat,  ");
						stringBuilder.append("avg(purchaseprice/inventory_product.pack), avg(purchaseprice/inventory_product.pack) * sum(inventory_product.stock),inventory_product.mfg,inventory_catalogue.genericname,sum(inventory_product.stock * inventory_product.saleprice) ");
					}
					stringBuilder.append("from inventory_product ");
					stringBuilder.append("inner join inventory_catalogue on inventory_catalogue.id=inventory_product.catalogueid  ");
					stringBuilder.append("left join inventory_subcategory on inventory_subcategory.id=inventory_catalogue.subcategoryid ");
					stringBuilder.append("inner join apm_condition on apm_condition.id = inventory_product.location ");
					stringBuilder.append("inner join inventory_category on inventory_category.id = inventory_catalogue.categoryid ");
					stringBuilder.append("where inventory_product.catalogueid!=0 and inventory_product.procurement=0 and  inventory_product.id not in ("+prductlist+") ");
					if (!location.equals("0")) {
						stringBuilder.append("and inventory_product.location='" + location + "' ");
					}
					
					if (withstock_filter.equals("2")) {
						stringBuilder.append("and stock>=0 ");
					}else if(withstock_filter.equals("3")){
						stringBuilder.append("and stock=0 ");
					}else{
						stringBuilder.append("and stock>0 ");
					}
					
					if (!cat_filter.equals("0")) {
						stringBuilder.append("and inventory_catalogue.categoryid='" + cat_filter + "' ");
					}
					if (searchtext != null) {
						stringBuilder.append("and (inventory_product.prodname like ('%" + searchtext + "%') or inventory_catalogue.product_name like ('%" + searchtext
								+ "%')  or inventory_product.genericname like ('%" + searchtext + "%') or inventory_catalogue.genericname like ('%" + searchtext + "%') or inventory_subcategory.name like ('" + searchtext + "%') or inventory_catalogue.product_code like ('%" + searchtext + "%')  ) ");
					}
					
					if(report_filter.equals("3")){
						
					}else if(report_filter.equals("1")){
						stringBuilder.append("group by catalogueid ");
					}else{
						stringBuilder.append("group by catalogueid,batch_no ");
					}
					
					if(orderby.equals("1")){
						stringBuilder.append("order by inventory_catalogue.product_name  ");
					}else if(orderby.equals("2")){
						if(report_filter.equals("3")){
							stringBuilder.append("order by (stock+0)  ");
						}else{
							stringBuilder.append("order by sum(stock)  ");
						}
					}else if(orderby.equals("3")){
						stringBuilder.append("order by (inventory_product.mrp+0)  ");
					}
					if (order_filter.equals("desc")) {
						stringBuilder.append("desc");
					} else {
						stringBuilder.append("asc");
					}
				}
				
				
				PreparedStatement ps = connection.prepareStatement(stringBuilder.toString());
				ResultSet rs = ps.executeQuery();
				double totalmrp =0;
				double totalpurchaseprice=0;
				double totalsaleprice=0;
				double totalamt=0;
				int totalqty=0;
				double totalsalevaluation=0;
				double packquantity=0;
				SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				String currdate=dateFormat1.format(calendar.getTime());
				while (rs.next()) {
					if(isprevious>0){
						prductlistnew = prductlistnew + ","+rs.getInt(1);
					}
					Product product = new Product();
					product.setPrductlistnew(prductlistnew);
					product.setId(rs.getInt(1));
					product.setCatalogueid(rs.getString(2));
					String name = rs.getString(11) + " (" + rs.getString(17) + ")";
					int stock = rs.getInt(6);
					String locid = rs.getString(9);
					/*if(!toDate.equals(fromdate)){
						boolean flag = checkChangeINProduct(fromdate,product.getId(),locid);
						if(flag){
							int openingstock =getChangeINProduct(fromdate,product.getId(),locid);
							int qtyin = getQtyInStockLog(fromdate,product.getId(),locid);
							int qtyout = getQtyOutStockLog(fromdate,product.getId(),locid);
							stock = openingstock+qtyin-qtyout;
							stock = openingstock;
							if(stock<0){
								stock=0;
							}
						}
					}*/
					product.setProduct_name(name);
					product.setMrp(DateTimeUtils.changeFormat(rs.getDouble(3)));
					product.setPurchase_price(DateTimeUtils.changeFormat(rs.getDouble(4)));
					product.setSale_price(DateTimeUtils.changeFormat(rs.getDouble(5)));
					product.setStock(""+stock);
					product.setMfg(rs.getString(18));
					String expirydate = DateTimeUtils.getCommencingDate1(rs.getString(7));
					product.setExpiry_date(expirydate);
					product.setBatch_no(rs.getString(8));
					
					
					product.setColor("");

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Calendar expireCalendar = Calendar.getInstance();
									
					expirydate = rs.getString(7);
					Date date2=format.parse(currdate);
					if (expirydate != null) {

						if (!expirydate.equals("")) {
							Date date = format.parse(expirydate);
							expireCalendar.setTime(date);
							long diff=date.getTime()-date2.getTime();
							diff = (diff / (1000*60*60*24));
							if(diff<45){
								product.setColor("#f20404");
							}
						}
					}
									
					
					product.setLocation(locid);
					product.setLocationName(rs.getString(16));
					product.setSubcategory(rs.getString(13));
					String shelf ="";
					if(rs.getString(14)==null){
						shelf = "";
					}else{
						shelf = rs.getString(14);
					}
					product.setShelf(shelf);
					product.setPack(rs.getString(15));
					String pack ="1";
					/*if(rs.getString(15)!=null){
						if(!rs.getString(15).equals("") || !rs.getString(15).equals("0")){
							pack = rs.getString(15);
						}
					}*/
					
					if(rs.getInt(15)==0){
						pack ="1";
					}else{
						pack = rs.getString(15);
					}
					product.setPurpriceqty(""+(rs.getDouble(4)/Double.parseDouble(pack))*stock);
					product.setSalepriceqty(""+(rs.getDouble(5)/Double.parseDouble(pack))*stock);
					product.setCategory(rs.getString(20));
					totalmrp = totalmrp + rs.getDouble(3);
					totalpurchaseprice = totalpurchaseprice+ rs.getDouble(4);
					totalsaleprice = totalsaleprice + rs.getDouble(5);
					totalqty = totalqty +stock;
					
					product.setTotalmrp(String.valueOf(Math.round(totalmrp * 100.0) / 100.0));
					product.setTotalpurchaseprice(totalpurchaseprice);
					product.setTotalsaleprice(String.valueOf(Math.round(totalsaleprice * 100.0) / 100.0));
					product.setTotalqty(totalqty);
					
					//lokesh
					
					product.setMinorder(rs.getString(21));
					product.setMaxorder(rs.getString(22));
					String vendorid = rs.getString(23);
					if(vendorid!=null){
						Vendor vendor = inventoryVendorDAO.getVendor(vendorid);
						product.setVendor(vendor.getName());
					}
					product.setPro_code(rs.getString(24));
					product.setVat(rs.getString(25));
					//shubham
					
					double purchaseprice1 = 0;
					double unitprice =0;
					 if(report_filter.equals("3")){
						 double purchaseprice=rs.getDouble(4);
						 double unitprice1 = purchaseprice/Double.parseDouble(pack);
						 unitprice=Double.parseDouble(DateTimeUtils.changeFormat(unitprice1));
						 purchaseprice = unitprice*stock;
						 purchaseprice1=Double.parseDouble(DateTimeUtils.changeFormat(purchaseprice));
					 }else{
						 double purchaseprice=rs.getDouble(4);
						 /*double unitprice1 = purchaseprice/Double.parseDouble(pack);*/
						double unitprice1 = rs.getDouble(26);
						unitprice=Double.parseDouble(DateTimeUtils.changeFormat(unitprice1));
						/* purchaseprice = unitprice*rs.getInt(6);*/
						 purchaseprice = rs.getDouble(27);
						purchaseprice1=Double.parseDouble(DateTimeUtils.changeFormat(purchaseprice));
					 }
				 	product.setMfg(rs.getString(28));
					product.setGenericname(rs.getString(29));
					product.setValuation(purchaseprice1);
					product.setUnitprice(unitprice);
					packquantity=purchaseprice1+packquantity;
					product.setTotalcountpurprice(DateTimeUtils.changeFormat(packquantity));
					product.setTotalAmt(packquantity);
					double vatamt = (rs.getDouble(25) *rs.getDouble(4))/100;
					vatamt = vatamt * (stock/Double.parseDouble(pack));
					/*double salevaluation = Math.round(rs.getDouble(30)*100.0)/100.0;*/
					vatamt = Math.round(vatamt*100.0)/100.0;
					product.setVatamt(vatamt);
					double salevaluation = vatamt + purchaseprice1;
					totalsalevaluation = totalsalevaluation+salevaluation;
					product.setSalevaluation(salevaluation);
					product.setTotalsalevaluation(totalsalevaluation);
					list.add(product);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}

private int getQtyOutStockLog(String fromdate, int id, String locid) {
	int res = 0;
	try {
		String todate =fromdate +" "+"59:59:59";
		String sql ="select sum(change_qty+0) from product_stock_log where datetime between '"+fromdate+"' and '"+todate+"' and productid='"+id+"' and qty_in_out=1";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			res = resultSet.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

private int getQtyInStockLog(String fromdate, int id, String locid) {
	int res = 0;
	try {
		String todate =fromdate +" "+"59:59:59";
		String sql ="select sum(change_qty+0) from product_stock_log where datetime between '"+fromdate+"' and '"+todate+"' and productid='"+id+"' and qty_in_out=0";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			res = resultSet.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

private int getChangeINProduct(String fromdate, int id, String locid) {
	int res = 0;
	try {
		String sql ="select stock from medicine_opening_stock where commencing='"+fromdate+"' and prodid='"+id+"'";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			res = resultSet.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

private boolean checkChangeINProduct(String fromdate, int id, String locid) {
		boolean flag = false;
		try {
			String sql ="select * from medicine_opening_stock where commencing='"+fromdate+"' and prodid='"+id+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
}

public ArrayList<Product> getUserMaterialIssueList(String fromDate, String toDate, String product,
		String department , String fromwherefilter,String warehousefilter1) {
	ArrayList<Product> arrayList = new ArrayList<Product>();
	UserProfileDAO profileDAO = new JDBCUserProfileDAO(connection);
	try {
		if(fromwherefilter.equals("0")){
			//Request
			toDate =toDate+"59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_parent_transfer_log.id,inventory_parent_transfer_log.userid,catlogueid,inventory_catalogue.product_name, ");
			buffer.append("sum(inventory_request_temp_log.qty),concat(initial,' ',firstname,' ',lastname) ");
			buffer.append("from inventory_parent_transfer_log ");
			buffer.append("inner join inventory_request_temp_log on inventory_request_temp_log.parentid=inventory_parent_transfer_log.id ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id =inventory_request_temp_log.catlogueid ");
			buffer.append("inner join apm_user on apm_user.userid = inventory_parent_transfer_log.userid and warehouse_id='"+warehousefilter1+"' ");
			buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='0' ");
			if(!department.equals("0")){
					buffer.append("and inventory_parent_transfer_log.userid='"+department+"' ");
			}
			if(!product.equals("")){
				buffer.append("and inventory_catalogue.id='"+product+"' ");
			}
			buffer.append("group by catlogueid ");
			
			buffer.append("order by inventory_parent_transfer_log.userid asc, inventory_catalogue.product_name asc ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product2 = new Product();
				product2.setId(rs.getInt(1));
				product2.setUserid(rs.getString(2));
				product2.setCatalogueid(rs.getString(3));
				product2.setProd_name(rs.getString(4));
				product2.setQty(rs.getString(5));
				product2.setName(rs.getString(6));
				product2.setFromwhere("Request Indent");
				if(rs.getString(6)==null){
					String name = profileDAO.getPharmacyFullname(rs.getString(2));
					product2.setName(name);
				}

				Double mainpurchaseprice=getUserMaterialIssueListOldPack( fromDate,  toDate,  product,
						 department ,  fromwherefilter, warehousefilter1, rs.getString(3));
				Double price =mainpurchaseprice;
				price = Math.round(price * 100.0) / 100.0;
				product2.setPurchase_price(""+price);
				arrayList.add(product2);
			}
			
			//Direct
			ArrayList<Product> arrayList2 = getDirectTransferUserWise(fromDate,toDate,product,department,warehousefilter1);
			arrayList.addAll(arrayList2);
		}else if(fromwherefilter.equals("1")){
			//Direct
			ArrayList<Product> arrayList2 = getDirectTransferUserWise(fromDate,toDate,product,department,warehousefilter1);
			arrayList.addAll(arrayList2);
		}else if(fromwherefilter.equals("2")){
			//Request
			toDate =toDate+"59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_parent_transfer_log.id,inventory_parent_transfer_log.userid,catlogueid,inventory_catalogue.product_name, ");
			buffer.append("sum(inventory_request_temp_log.qty),concat(initial,' ',firstname,' ',lastname) ");
			buffer.append("from inventory_parent_transfer_log ");
			buffer.append("inner join inventory_request_temp_log on inventory_request_temp_log.parentid=inventory_parent_transfer_log.id ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id =inventory_request_temp_log.catlogueid ");
			buffer.append("inner join apm_user on apm_user.userid = inventory_parent_transfer_log.userid  ");
			buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='0' and warehouse_id='"+warehousefilter1+"' ");
			if(!department.equals("0")){
				buffer.append("and inventory_parent_transfer_log.userid='"+department+"' ");
			}
			if(!product.equals("")){
				buffer.append("and inventory_catalogue.id='"+product+"' ");
			}
			buffer.append("group by catlogueid ");
			buffer.append("order by inventory_parent_transfer_log.userid asc, inventory_catalogue.product_name asc ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product2 = new Product();
				product2.setId(rs.getInt(1));
				product2.setUserid(rs.getString(2));
				product2.setCatalogueid(rs.getString(3));
				product2.setProd_name(rs.getString(4));
				product2.setQty(rs.getString(5));
				product2.setName(rs.getString(6));
				product2.setFromwhere("Request Indent");
				if(rs.getString(6)==null){
					String name = profileDAO.getPharmacyFullname(rs.getString(2));
					product2.setName(name);
				}
					Double mainpurchaseprice=getDirectTransferUserWiseOldprodid(fromDate, toDate, product, department, warehousefilter1,rs.getString(3));
					Double price =mainpurchaseprice;
					price = Math.round(price * 100.0) / 100.0;
					product2.setPurchase_price(""+price);
				arrayList.add(product2);
			}
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return arrayList;
}

private ArrayList<Product> getDirectTransferUserWise(String fromDate, String toDate, String product,
		String department,String warehousefilter1 ) {
	ArrayList<Product> arrayList = new ArrayList<Product>();
	UserProfileDAO profileDAO = new JDBCUserProfileDAO(connection);
	try {
		//Direct
		toDate =toDate+"59:59:59";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select inventory_parent_transfer_log.id,inventory_parent_transfer_log.userid,catlogueid,inventory_catalogue.product_name, ");
		buffer.append("sum(inventory_transfer_log.qty),concat(initial,' ',firstname,' ',lastname) from inventory_parent_transfer_log ");
		buffer.append("inner join inventory_transfer_log on inventory_transfer_log.parentid=inventory_parent_transfer_log.id ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id =inventory_transfer_log.catlogueid ");
		buffer.append("inner join apm_user on apm_user.userid = inventory_parent_transfer_log.userid  ");
		buffer.append("where request_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='1' and from_location='"+warehousefilter1+"' ");
		if(!department.equals("0")){
			buffer.append("and inventory_parent_transfer_log.userid='"+department+"' ");
		}
		if(!product.equals("")){
			buffer.append("and inventory_catalogue.id='"+product+"' ");
		}
		buffer.append("group by catlogueid ");
		buffer.append("order by inventory_parent_transfer_log.userid asc, inventory_catalogue.product_name asc ");
		PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Product product2 = new Product();
			product2.setId(rs.getInt(1));
			product2.setUserid(rs.getString(2));
			product2.setCatalogueid(rs.getString(3));
			product2.setProd_name(rs.getString(4));
			product2.setQty(rs.getString(5));
			product2.setName(rs.getString(6));
			product2.setFromwhere("Direct Transfer");
			if(rs.getString(6)==null){
				String name = profileDAO.getPharmacyFullname(rs.getString(2));
				product2.setName(name);
			}
			Double mainpurchaseprice=getDirectTransferUserWiseOldprodid(fromDate, toDate, product, department, warehousefilter1, rs.getString(3));
			Double price =mainpurchaseprice;
			price = Math.round(price * 100.0) / 100.0;
			product2.setPurchase_price(""+price);
			arrayList.add(product2);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return arrayList;
}

public ArrayList<Master> getCatalogueList() {
	PreparedStatement preparedStatement = null;
	ArrayList<Master>list = new ArrayList<Master>();
	String sql = "select id, product_name from inventory_catalogue where product_name is not null and product_name !=''  order by product_name limit 0,50 ";
	
	try{
		
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			Master master = new Master();
			master.setId(rs.getInt(1));
			master.setName(rs.getString(2));
			
			list.add(master);
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return list;
}

public ArrayList<Master> getSecStoreNameList() {
	PreparedStatement preparedStatement = null;
	ArrayList<Master>list = new ArrayList<Master>();
	String sql = "select id,name from inventory_secondory_store order by name ";
	
	try{
		
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			Master master = new Master();
			master.setId(rs.getInt(1));
			master.setName(rs.getString(2));
			
			list.add(master);
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return list;
}

public ArrayList<Product> getPriscMedicineByAvailablilityList(String mdicinenametxt, int int1, String genericname, int default_location_new) {
	
	int m=0;
	String result=mdicinenametxt;
	
	/*for(String str:mdicinenametxt.split(" ")){
		 
		   int l=str.length();
		   if(m<l){
			   m=l;
			   result= str;
		   }
	}*/
	ArrayList<Product> arrayList = new ArrayList<Product>();
	try {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select inventory_product.id,inventory_product.supplierid,inventory_catalogue.product_name, ");
		buffer.append("inventory_product.mrp,inventory_product.purchaseprice,inventory_product.saleprice,inventory_product.stock, ");
		buffer.append("inventory_product.mdicinenameid,inventory_product.expiry_date,inventory_product.batch_no,inventory_product.vat, ");
		buffer.append("inventory_catalogue.genericname,inventory_product.cell,inventory_product.pack,inventory_product.medicine_type,inventory_product.mfg ");
		buffer.append("from inventory_product inner join inventory_catalogue on inventory_product.catalogueid=inventory_catalogue.id ");
		/*buffer.append("where inventory_catalogue.product_name like ('%"+result+"%') ");*/
		buffer.append("where inventory_catalogue.product_name='"+result+"' ");
		//buffer.append("where ( inventory_catalogue.product_name like ('%"+result+"%') or inventory_catalogue.genericname like ('"+genericname+"') ) ");
		buffer.append("and inventory_product.saleprice is not null and inventory_product.mrp>0 and stock>0 and inventory_product.pack>0  ");
		buffer.append("and inventory_product.location='"+default_location_new+"' ");
		buffer.append("order by inventory_product.expiry_date asc ");

		PreparedStatement ps = connection.prepareStatement(buffer.toString());
		ResultSet rs = ps.executeQuery();
		int count =0;
		while (rs.next()) {
			Product product = new Product();
			boolean flag = false;
			count = count +rs.getInt(7);
			if(count>=int1){
				/*product.setSaleqty(rs.getInt(7)-(count-rs.getInt(7)));*/
				product.setSaleqty(int1-(count-rs.getInt(7)));
				flag= true;
			}else{
				product.setSaleqty(rs.getInt(7));
			}
			product.setId(rs.getInt(1));
			product.setVendor_id(rs.getString(2));
			product.setProduct_name(rs.getString(3));
			product.setMrp(rs.getString(4));
			product.setPurchase_price(rs.getString(5));
			product.setSale_price(DateTimeUtils.changeFormat(rs.getDouble(6)));
			product.setStock(rs.getString(7));
			product.setMedicinenameid(rs.getString(8));
			product.setExpiry_date(rs.getString(9));
			product.setBatch_no(rs.getString(10));
			product.setVat(rs.getString(11));
			product.setGenericname(rs.getString(12));
			product.setShelf(rs.getString(13));
			product.setPack(rs.getString(14));
			product.setMedicine_type(rs.getString(15));
			product.setMfg(rs.getString(16));
			arrayList.add(product);
			if(flag){
				break;
			}
			
		}
	} catch (Exception e) {

		e.printStackTrace();
	}
	return arrayList;
}

public int getPreviousShelfId(String catalogueid) {
	int res =0;
	try {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select apm_medicine_cell.id from inventory_procurement ");
		buffer.append("inner join inventory_product on inventory_product.id =inventory_procurement.prodid ");
		buffer.append("inner join apm_medicine_cell on apm_medicine_cell.name = inventory_product.cell ");
		buffer.append("where inventory_procurement.catalogueid='"+catalogueid+"' ");
		buffer.append("order by inventory_procurement.id desc limit 1 ");
		PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int getEditGRNShelfId(String shelf) {
	int res =0;
	try {
		String sql ="select id from apm_medicine_cell where name='"+shelf+"'";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res= rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int saveInventoryStockLog(String todaydate) {
	int res =0;
	try {
		String sql ="select id,stock,purchaseprice from inventory_product";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			boolean  flag = isProductInLog(rs.getInt(1),todaydate);
			if(!flag){
				 res = insertInventoryStockLog(rs.getInt(1),rs.getString(2),rs.getString(3),todaydate);
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

private int insertInventoryStockLog(int prodid, String stock, String purchaseprice, String todaydate) {
	int result = 0;

	try {
		String sql = "insert into inventory_stock_log (productid,qty,purchase_price,date) values (?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, ""+prodid);
		ps.setString(2, stock);
		ps.setString(3, purchaseprice);
		ps.setString(4, todaydate);
		result = ps.executeUpdate();
	} catch (Exception e) {

		e.printStackTrace();
	}

	return result;
}

private boolean isProductInLog(int int1, String todaydate) {
	boolean flag= false;
	try {
		String sql ="select id from inventory_stock_log where date='"+todaydate+"' and productid='"+int1+"'";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			flag = true;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return flag;
}

public int checkProductCodeExist(String val) {
	int res =0;
	try {
		String sql="select id from inventory_catalogue where product_code=?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, val);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = 1;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public ArrayList<Product> getchildCathData(String val, String location1) {
	ArrayList<Product> arrayList = new ArrayList<Product>();
	try {
		//String sql="select prodid,catlogueid,qty from cathlab_template_child where parentid='"+val+"'";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select inventory_product.id,catlogueid,cathlab_template_child.qty,stock from cathlab_template_child ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id = cathlab_template_child.catlogueid ");
		buffer.append("inner join inventory_product on inventory_catalogue.id = inventory_product.catalogueid ");
		buffer.append("where parentid='"+val+"' and stock>0  ");
		buffer.append("and inventory_product.location='"+location1+"' ");
		buffer.append("group by inventory_product.id ");
		PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setProduct_id(rs.getString(1));
			product.setCatalogueid(rs.getString(2));
			product.setQty(rs.getString(3));
			arrayList.add(product);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return arrayList;
}

public String getCathlabTemplateProcid(String cathtempid) {
	String res ="0";
	try {
		String sql="select procedure_id from cathlab_template where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, cathtempid);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = rs.getString(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int geTotalCathLabCount(String fromdate, String todate) {
	int result = 0;

	try {
		todate = todate+" "+"59:59:59";
		String sql = "select count(*) from indent_parent_patient_transfer_log where given_date between '"+fromdate+"' and '"+todate+"' and iscathlab='1' ";
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			result = rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public ArrayList<Product> geTotalCathLabList(Pagination pagination, String fromdate, String todate) {
	ArrayList<Product> arrayList = new ArrayList<Product>();
	try {
		todate = todate+" "+"59:59:59";
		String sql="";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select given_userid,given_date,clientid,procedureid,templateid,indent_parent_patient_transfer_log.status,concat(title,' ',firstname,' ',middlename,' ',surname),indent_parent_patient_transfer_log.id,indent_parent_patient_transfer_log.remarks from indent_parent_patient_transfer_log ");
		buffer.append("inner join indent_patient_transfer_log on indent_parent_patient_transfer_log.id = indent_patient_transfer_log.parentid "); 
		buffer.append("left join apm_patient on indent_patient_transfer_log.clientid = apm_patient.id ");
		buffer.append("where indent_parent_patient_transfer_log.given_date between '"+fromdate+"' and '"+todate+"' and iscathlab=1 group by indent_parent_patient_transfer_log.id order by indent_parent_patient_transfer_log.id desc ");
		if (pagination != null) {
			sql = pagination.getSQLQuery(buffer.toString());
		} else {
			sql = buffer.toString();
		}
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setUserid(rs.getString(1));
			product.setDate(rs.getString(2));
			product.setClientid(rs.getString(3));
			String tempname = getCathlabTemplateName(rs.getString(5));
			product.setTempname(tempname);
			product.setStatus(rs.getString(6));
			product.setClientname(rs.getString(7));
			product.setId(rs.getInt(8));
			product.setRemark(rs.getString(9));
			arrayList.add(product);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return arrayList;
}

public String getCathlabTemplateName(String cathtempid) {
	String res ="";
	try {
		/*String sql="select procedure_id from cathlab_template where id=?";*/
		StringBuffer buffer = new StringBuffer();
		buffer.append("select template_name,name from cathlab_template ");
		buffer.append("inner join apm_newchargetype on apm_newchargetype.id = cathlab_template.procedure_id ");
		buffer.append("where cathlab_template.id='"+cathtempid+"' ");
		PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = rs.getString(1)+" - "+rs.getString(2);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int getProductStockByCatlogue(String catalogueid, String location) {
	int res =0;
	try {
		String sql ="select sum(stock) from inventory_product  where catalogueid='"+catalogueid+"' and location='"+location+"' ";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return res;
}

public ArrayList<Product> getCathDataChildList(String id, String string) {
	ArrayList<Product> arrayList = new ArrayList<Product>();
	try {
		/*String sql="select prodid,qty,id from indent_patient_transfer_log where parentid='"+id+"'";*/
		StringBuffer buffer =new StringBuffer();
		buffer.append("select prodid,qty,id from indent_patient_transfer_log where parentid='"+id+"' ");
		if(string.equals("1")){
			buffer.append("and isused='1' ");
		}
		PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setProduct_id(rs.getString(1));
			Product product2 = getProduct(rs.getString(1));
			product.setMrp(product2.getMrp());
			product.setProduct_name(product2.getProduct_name());
			product.setQty(rs.getString(2));
			product.setConsumeid(rs.getString(3));
			arrayList.add(product);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return arrayList;
}

public Product getCathParentData(String id) {
	Product product = new Product();
	try {
		String sql="";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select given_userid,given_date,clientid,procedureid,templateid,indent_parent_patient_transfer_log.status,concat(title,' ',firstname,' ',middlename,' ',surname),indent_parent_patient_transfer_log.id,indent_parent_patient_transfer_log.remarks from indent_parent_patient_transfer_log ");
		buffer.append("inner join indent_patient_transfer_log on indent_parent_patient_transfer_log.id = indent_patient_transfer_log.parentid "); 
		buffer.append("left join apm_patient on indent_patient_transfer_log.clientid = apm_patient.id ");
		buffer.append("where indent_parent_patient_transfer_log.id='"+id+"' ");
		sql = buffer.toString();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			product.setUserid(rs.getString(1));
			product.setDate(rs.getString(2));
			String tempname = getCathlabTemplateName(rs.getString(5));
			product.setTempname(tempname);
			product.setStatus(rs.getString(6));
			product.setClientname(rs.getString(7));
			product.setId(rs.getInt(8));
			product.setTempid(rs.getString(5));
			product.setClientid(rs.getString(3));
			product.setComment(rs.getString(9));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return product;
}

public int updateCathStatus(int parentid,String status) {
	int result = 0;
	try {
		String sql = "update indent_parent_patient_transfer_log  set status='"+status+"' where id=" + parentid
				+ "";
		PreparedStatement ps = connection.prepareStatement(sql);
		result = ps.executeUpdate();
	} catch (Exception e) {

		e.printStackTrace();
	}
	return result;
}

public ArrayList<Product> getProductData(String location) {
	ArrayList<Product> list = new ArrayList<Product>();
	try {
		String sql = "";
		if (!location.equals("0")) {
			sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and location='"
					+ location + "' order by prodname ";
		} else {
			sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 order by prodname ";

		}
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setId(rs.getInt(1));
			product.setSubcategory_id(rs.getString(2));
			product.setVendor_id(rs.getString(3));
			product.setBrand_id(rs.getString(4));
			product.setProduct_code(rs.getString(5));
			product.setProduct_name(rs.getString(6));
			product.setMrp(rs.getString(7));
			product.setPurchase_price(rs.getString(8));
			product.setSale_price(rs.getString(9));
			product.setPurchase_discount(rs.getString(10));
			product.setSale_discount(rs.getString(11));
			product.setWeight(rs.getString(12));
			product.setUnit(rs.getString(13));
			product.setDescription(rs.getString(14));
			product.setCategory_id(rs.getString(15));
			product.setStock(rs.getString(16));
			product.setExpiry_date(rs.getString(17));
			product.setTax(rs.getString(18));
			product.setUserid(rs.getString(19));
			product.setQty(rs.getString(20));
			product.setLastmodified(rs.getString(21));
			String genericname = rs.getString(22);
			if (genericname == null) {
				genericname = "GEN";
			}

			String medicinenameid = rs.getString(23);
			product.setBatch_no(rs.getString(24));
			product.setHsnno(rs.getString(25));
			if (product.getHsnno() == null) {
				product.setHsnno("");
			}
			product.setMedicinenameid(medicinenameid);
			String pro_code = getCatlogueProductCode(rs.getString(26));
			
			String expiry= product.getExpiry_date();
			if(expiry!=null){
				if(!expiry.equals("")){
					String temp[] = expiry.split("-");
					expiry= temp[1]+"/"+temp[0];
				}
				
			} else {
				expiry= "";
			}
			
			String data ="";
			if(pro_code!=null){
				if(pro_code.equals("")){
					data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}else{
					data = product.getProduct_name() + "- " + genericname + "- " + pro_code + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}
			}else{
				data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
						+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
						+ ") (" + product.getStock() + ")  ";
			}
			product.setGenericname(data);
			list.add(product);
		}

	} catch (Exception e) {

		e.printStackTrace();
	}
	return list;
}

public int getTemplateCathLabCount() {
	int result = 0;

	try {
		
		String sql = "select count(*) from cathlab_template where cathlab_template.isdeleted='0' ";
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			result = rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public ArrayList<Product> getTemplateCathLabList(Pagination pagination) {
	ArrayList<Product> arrayList = new ArrayList<Product>();
	try {
		
		String sql="";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select cathlab_template.id,template_name,userid,datetime,name from cathlab_template ");
		buffer.append("inner join apm_newchargetype on apm_newchargetype.id = cathlab_template.procedure_id ");
		buffer.append("where cathlab_template.isdeleted='0' ");
		buffer.append(" order  by cathlab_template.id desc");
		if (pagination != null) {
			sql = pagination.getSQLQuery(buffer.toString());
		} else {
			sql = buffer.toString();
		}
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setId(rs.getInt(1));
			product.setTempname(rs.getString(2));
			product.setUserid(rs.getString(3));
			product.setDate(rs.getString(4));
			product.setProcedurename(rs.getString(5));
			arrayList.add(product);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return arrayList;
}

public ArrayList<Product> getChildCathTempData(String id) {
	ArrayList<Product> arrayList = new ArrayList<Product>();
	try {
		String sql ="select id, qty, prodid, catlogueid from cathlab_template_child where parentid='"+id+"' ";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setId(rs.getInt(1));
			product.setQty(rs.getString(2));
			product.setProduct_id(rs.getString(3));
			product.setCatalogueid(rs.getString(4));
			arrayList.add(product);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return arrayList;
}

public Product getCathTemplateData(String id) {
	Product product = new Product();
	try {
		String sql="select id, template_name, procedure_id, userid,remark from cathlab_template where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			product.setId(rs.getInt(1));
			product.setTempname(rs.getString(2));
			product.setProcedureid(rs.getString(3));
			product.setRemark(rs.getString(5));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return product;
}

public ArrayList<Product> getCathlabProductData(String location) {
	ArrayList<Product> list = new ArrayList<Product>();
	try {
		String sql = "";
		if (!location.equals("0")) {
			sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where location='"
					+ location + "' group by catalogueid order by prodname,max(stock) ";
		} else {
			sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product group by catalogueid order by prodname,max(stock) ";

		}
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setId(rs.getInt(1));
			product.setSubcategory_id(rs.getString(2));
			product.setVendor_id(rs.getString(3));
			product.setBrand_id(rs.getString(4));
			product.setProduct_code(rs.getString(5));
			product.setProduct_name(rs.getString(6));
			product.setMrp(rs.getString(7));
			product.setPurchase_price(rs.getString(8));
			product.setSale_price(rs.getString(9));
			product.setPurchase_discount(rs.getString(10));
			product.setSale_discount(rs.getString(11));
			product.setWeight(rs.getString(12));
			product.setUnit(rs.getString(13));
			product.setDescription(rs.getString(14));
			product.setCategory_id(rs.getString(15));
			product.setStock(rs.getString(16));
			product.setExpiry_date(rs.getString(17));
			product.setTax(rs.getString(18));
			product.setUserid(rs.getString(19));
			product.setQty(rs.getString(20));
			product.setLastmodified(rs.getString(21));
			String genericname = rs.getString(22);
			if (genericname == null) {
				genericname = "GEN";
			}

			String medicinenameid = rs.getString(23);
			product.setBatch_no(rs.getString(24));
			product.setHsnno(rs.getString(25));
			if (product.getHsnno() == null) {
				product.setHsnno("");
			}
			product.setMedicinenameid(medicinenameid);
			String pro_code = getCatlogueProductCode(rs.getString(26));
			
			String expiry= product.getExpiry_date();
			if(expiry!=null){
				if(!expiry.equals("")){
					String temp[] = expiry.split("-");
					expiry= temp[1]+"/"+temp[0];
				}
				
			} else {
				expiry= "";
			}
			
			String data ="";
			if(pro_code!=null){
				if(pro_code.equals("")){
					data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}else{
					data = product.getProduct_name() + "- " + genericname + "- " + pro_code + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}
			}else{
				data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
						+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
						+ ") (" + product.getStock() + ")  ";
			}
			product.setGenericname(data);
			list.add(product);
		}

	} catch (Exception e) {

		e.printStackTrace();
	}
	return list;
}

public int getCountCathOpeningStockList(String fromDate, String toDate) {
	PreparedStatement preparedStatement = null;
	String sql = "SELECT count(*) FROM medicine_opening_stock where commencing between '" + fromDate
			+ "' and '" + toDate + "' and isconsume='1' ";
	int result =0;
	//Akash for get total count for pagination
	try {
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			result = rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public ArrayList<Product> getCathOpeningStockList(String fromDate, String toDate, Pagination pagination, int min,
		int max, String ismonthlyreport) {
	PreparedStatement preparedStatement = null;
	InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
	ArrayList<Product> list = new ArrayList<Product>();
	StringBuffer buffer= new StringBuffer();
	buffer.append(" SELECT prodid, stock,commencing FROM medicine_opening_stock where commencing between '" + fromDate+ "' and '" + toDate + "' and isconsume='1' ");
	if(ismonthlyreport.equals("1")){
		buffer.append("group by prodid ");
	}
	if(max>0){
		buffer.append(" limit "+min+" , "+max+" ");
	}else{
		buffer.append(" limit 0,1000");
	}
	/*String sql = "SELECT prodid, stock FROM medicine_opening_stock where commencing between '" + fromDate
			+ "' and '" + toDate + "' ";*/
	double tot = 0;
	int openstockcount=0;
	try {
		
		preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			int productid = rs.getInt(1);
			String productname = getproductName(productid);
			product.setProduct_name(productname);
			product.setOpeningstock(rs.getString(2));
			product.setDate(DateTimeUtils.getCommencingDate1(rs.getString(3)));
			int sale = getCathClosingstock(productid, fromDate, toDate);
			product.setSale(Integer.toString(sale));

			int closingstock = rs.getInt(2) - sale;
			product.setClosingstock(Integer.toString(closingstock));

			Product product2 = getProduct(rs.getString(1));
			
			double amt1 = closingstock * Double.parseDouble(product2.getPurchase_price());
			tot = tot + amt1;
			product.setUnit("" + Math.round(amt1 * 100.0) / 100.0);
			product.setTotal_amount(tot);
			Product master = getProductCatalogueDetails(product2.getCatalogueid());
			Product productcat = getCategory(master.getCategory_id());
			Product productsubcat =getSubCategory(master.getSubcategory_id());
			String categoryname = productcat.getName();
			String subcategoryname =productsubcat.getName();
			product.setPro_code(master.getPro_code());
			product.setCategory(categoryname);
			product.setSubcategory(subcategoryname);
			product.setMfg(product2.getMfg());
			product.setExpiry_date(DateTimeUtils.getCommencingDate1(product2.getExpiry_date()));
			product.setBatch_no(product2.getBatch_no());
			product.setMinorder(master.getMinorder());
			product.setMaxorder(master.getMaxorder());
			 product.setMrp(product2.getMrp());
			 product.setSale_price(product2.getSale_price());
			 product.setVat(product2.getVat());
			String vendorid = product2.getVendor_id();
			Vendor vendor = inventoryVendorDAO.getVendor(vendorid);
			product.setVendor(vendor.getName());
			//shubham 19/10
			openstockcount=rs.getInt(2)+openstockcount;
			product.setOpentotalcount(String.valueOf(openstockcount));
			list.add(product);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	return list;
}

private int getCathClosingstock(int productid, String fromDate, String toDate) {
	PreparedStatement preparedStatement = null;
	int result = 0;
	toDate = toDate +" "+"59:59:59";
	String sql = "SELECT sum(qty) FROM indent_patient_transfer_log where prodid = '" + productid + "' "
			+ " and datetime between '" + fromDate + "' and '" + toDate + "' ";

	try {
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			result = rs.getInt(1);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public int updateCathComment(int parentid, String comment1) {
	int result = 0;
	try {
		String sql = "update indent_parent_patient_transfer_log  set remarks=? where id=" + parentid
				+ "";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, comment1);
		result = ps.executeUpdate();
	} catch (Exception e) {

		e.printStackTrace();
	}
	return result;

}

public int deleteCathlabTemplate(String id, String datetime, String userid) {
	int result = 0;
	try {
		String sql = "update cathlab_template  set isdeleted=?,deleteduserid=?,deleteddate=? where id=" + id
				+ "";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, "1");
		ps.setString(2, userid);
		ps.setString(3, datetime);
		result = ps.executeUpdate();
	} catch (Exception e) {

		e.printStackTrace();
	}
	return result;
}

public ArrayList<Procurement> getSupplierWisePaymentList(String fromdate, String todate, String vendorid,
		Pagination pagination) {
	PreparedStatement preparedStatement = null;
	ArrayList<Procurement>list = new ArrayList<Procurement>();
	InventoryVendorDAO vendorDAO=new JDBCInventoryVendorDAO(connection);
	ProcurementDAO procurementDAO = new JDBCProcurementDAO(connection);
	PoPaymenytDAO poPaymenytDAO = new JDBCPoPaymengtDAO(connection);
	StringBuffer buffer= new StringBuffer();
	buffer.append("SELECT handoverto,paytype,chequeno,chequetype,payamount,commencing,voucherno,cheq_receiver,inventory_parent_procurement.id,bankname,inventory_paymentpo.id,parentid FROM inventory_paymentpo ");
	buffer.append("inner join inventory_parent_procurement on inventory_paymentpo.procurementid= inventory_parent_procurement.id ");
	buffer.append("where inventory_paymentpo.commencing between '"+fromdate+"' and '"+todate+"' ");
	if(!vendorid.equals("0")){
		buffer.append("and vendorid="+vendorid+" ");
	}
	
	try{
		preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			Procurement procurement = new Procurement();
			Procurement procurement2 = procurementDAO.getProcurementNew(rs.getString(9),0);
			
			procurement.setHandoverTo(rs.getString(1));
			procurement.setPaymentType(rs.getString(2));
			procurement.setCheqNo(rs.getString(3));
			procurement.setCheqType(rs.getString(4));
			procurement.setPaymentAmount(rs.getString(5));
			procurement.setPaymentDate(DateTimeUtils.getCommencingDate1(rs.getString(6)));
			procurement.setVoucherno(rs.getString(7));
			procurement.setCheq_receiver(rs.getString(8));
			Vendor vendor=vendorDAO.getVendor(procurement2.getVendor_id());
			procurement.setVendor(vendor.getName());
			int res=procurementDAO.getProcurmentSeqNo(rs.getString(9));
			String grnno="";
			if(res>0){
			   grnno=String.valueOf(res);
			}else{
			    grnno=rs.getString(9);
			}
			procurement.setProSeqNo(grnno);
			procurement.setBankName(rs.getString(10));
			procurement.setVoucherno(procurement2.getVoucherno());
			procurement.setVoucherdate(procurement2.getVoucherdate());
			
			double total= poPaymenytDAO.getTotalVoucherAmount(procurement.getVoucherno(),rs.getString(9),procurement2.getVendor_id());
			double paidAmt= poPaymenytDAO.getPaidAmtofVoucher(procurement.getVoucherno(), rs.getString(9));
			double balance= total - paidAmt ;
			procurement.setBalance(DateTimeUtils.changeFormat(balance));
			
			String locationame = pharmacyLocationNameByID(procurement2.getLocation());
			procurement.setLocationname(locationame);
			procurement.setPaymentNo(rs.getInt(11));
			procurement.setParentid(rs.getString(12));
			list.add(procurement);
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

public int getTotalSupplierPaymentReportCount(String fromdate, String todate, String vendorid) {
	PreparedStatement preparedStatement = null;
	int res =0;
	StringBuffer buffer= new StringBuffer();
	buffer.append("SELECT count(*) FROM inventory_paymentpo ");
	buffer.append("inner join inventory_parent_procurement on inventory_paymentpo.procurementid= inventory_parent_procurement.id ");
	buffer.append("where inventory_paymentpo.commencing between '"+fromdate+"' and '"+todate+"' ");
	if(!vendorid.equals("0")){
		buffer.append("and vendorid="+vendorid+" ");
	}
	
	try{
		preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			res = rs.getInt(1);
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}


public Double getUserMaterialIssueListOldPack(String fromDate, String toDate, String product,
		String department , String fromwherefilter,String warehousefilter1,String catlog) {
	ArrayList<Product> arrayList = new ArrayList<Product>();
	double purchaseprice=0;
	UserProfileDAO profileDAO = new JDBCUserProfileDAO(connection);
	try {
		if(fromwherefilter.equals("0")){
			//Request
			toDate =toDate+"59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_parent_transfer_log.id,inventory_parent_transfer_log.userid,catlogueid,inventory_catalogue.product_name, ");
			buffer.append("inventory_request_temp_log.qty,concat(initial,' ',firstname,' ',lastname),old_prodid ");
			buffer.append("from inventory_parent_transfer_log ");
			buffer.append("inner join inventory_request_temp_log on inventory_request_temp_log.parentid=inventory_parent_transfer_log.id ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id =inventory_request_temp_log.catlogueid ");
			buffer.append("inner join apm_user on apm_user.userid = inventory_parent_transfer_log.userid and warehouse_id='"+warehousefilter1+"' ");
			buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='0' ");
			if(!department.equals("0")){
					buffer.append("and inventory_parent_transfer_log.userid='"+department+"' ");
			}
			buffer.append("and catlogueid='"+catlog+"' ");
			buffer.append("order by inventory_parent_transfer_log.userid asc, inventory_catalogue.product_name asc ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				
				Product product3 = getProduct(rs.getString(7));
				if(product3.getPack()!=null){
					if(product3.getPack().equals("")  || product3.getPack().equals("0")){
						product3.setPack("1");
					}
				}else{
					product3.setPack("1");
				}
				double purprice = Double.parseDouble(product3.getPurchase_price())/Integer.parseInt(product3.getPack());
				purprice = purprice*rs.getInt(5);
				purchaseprice = purchaseprice+purprice;
			}
			}
			}catch (Exception e) {
e.printStackTrace();
}
	return purchaseprice;
}
public Double getDirectTransferUserWiseOldprodid(String fromDate, String toDate, String product,
		String department,String warehousefilter1,String catlogueid ){
	
	try {
		//Direct
		toDate =toDate+"59:59:59";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select inventory_parent_transfer_log.id,inventory_parent_transfer_log.userid,catlogueid,inventory_catalogue.product_name, ");
		buffer.append("inventory_transfer_log.qty,concat(initial,' ',firstname,' ',lastname),old_prodid from inventory_parent_transfer_log ");
		buffer.append("inner join inventory_transfer_log on inventory_transfer_log.parentid=inventory_parent_transfer_log.id ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id =inventory_transfer_log.catlogueid ");
		buffer.append("inner join apm_user on apm_user.userid = inventory_parent_transfer_log.userid  ");
		buffer.append("where request_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='1' and from_locaton='"+warehousefilter1+"' ");
		if(!department.equals("0")){
			buffer.append("and inventory_parent_transfer_log.userid='"+department+"' ");
		}
		buffer.append("and catlogueid='"+catlogueid+"' ");
		buffer.append("order by inventory_parent_transfer_log.userid asc, inventory_catalogue.product_name asc ");
		PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		double purchaseprice = 0;
		while (rs.next()) {
			Product product2 = new Product();
			product2.setOldprodid(rs.getString(7));
			}
		Product product3 = getProduct(rs.getString(7));
		if(product3.getPack()!=null){
			if(product3.getPack().equals("")  || product3.getPack().equals("0")){
				product3.setPack("1");
			}
		}else{
			product3.setPack("1");
		}
		double purprice = Double.parseDouble(product3.getPurchase_price())/Integer.parseInt(product3.getPack());
		purprice = purprice*rs.getInt(5);
		purchaseprice = purchaseprice+purprice;
}catch (Exception e) {
e.printStackTrace();
}
	return null;
}


private double getIndividualPurchasePrice(String purprice,String packqty){
	double res=0.0,pur=0.0,qty=1.0;
	try {
		if(purprice!=null){
			if(!purprice.equals("")){
				pur=Double.parseDouble(purprice);
			}
		}
		if(packqty!=null){
			if(!packqty.equals("") || !packqty.equals("0")){
				qty=Double.parseDouble(packqty);
			}
		}
		res=pur/qty;
	} catch (Exception e) {
		e.printStackTrace();
	} 
	return res;
}



public ArrayList<Product> getProductListForItemWise(String string, String location) {
	ArrayList<Product> list = new ArrayList<Product>();
	try {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "";
		if (!location.equals("0")) {
			sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno from inventory_product where location='"
					+ location + "'  order by prodname asc ";
		} else {
			sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno from inventory_product  order by prodname asc ";
		}
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setId(rs.getInt(1));
			product.setProduct_name(rs.getString(6));
			String genericname = rs.getString(22);
			if (genericname == null) {
				genericname = "GEN";
			}
			String data = product.getProduct_name() + "-" + genericname + "- (" + rs.getString(24) + "/"
					+rs.getString(25) + ") (" + rs.getString(17) + ") ";
			product.setGenericname(data);
			boolean flag = false;
			Calendar expireCalendar = Calendar.getInstance();
			String expirydate = rs.getString(17);
			if (expirydate != null) {

				if (!expirydate.equals("")) {
					Date date = format.parse(expirydate);
					expireCalendar.setTime(date);
					long res = getExpiryDaysBetween(expireCalendar.getTime(), calendar.getTime());
					if (res > 0) {
						flag = true;
					}
				}
			}
			if (flag) {
				list.add(product);
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

public ArrayList<Product> getItemWiseReportListNew(String fromdate, String todate, String product_id, String location) {
	ArrayList<Product> list = new ArrayList<Product>();
	try {
		InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
		Product product = getProduct(product_id);
		int stock = getTotalStock(product.getProduct_name());
		StringBuffer buffer = new StringBuffer();
		buffer.append("select product_id from apm_medicine_charges ");
		buffer.append("where commencing between '"+fromdate+"' and '"+todate+"' ");
		if (!product_id.equals("0")) {
			buffer.append("and product_id='"+product_id+"' ");
		}
		buffer.append("group by product_id ");
		
		PreparedStatement ps = connection.prepareStatement(buffer.toString());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String pid = rs.getString(1);
			product = getProduct(pid);
			String vendorid = product.getVendor_id();
			Product master = new Product();
			ArrayList<Product> innerProductList = new ArrayList<Product>();
				ArrayList<Product> tempList = getProductItemReport(pid, fromdate, todate, 0,location);
				innerProductList.addAll(tempList);
				Product product2=tempList.get(tempList.size()-1);
				master.setTotalqty(product2.getTotalqty());
			if (innerProductList.size() > 0) {
				master.setInnerProductList(innerProductList);
			}
			Vendor vendor = inventoryVendorDAO.getVendor(vendorid);
			master.setVendor(vendor.getName());
			master.setProduct_name(product.getProduct_name());
			master.setGenericname(product.getGenericname());
			master.setHsnno(product.getHsnno());

			master.setStock(String.valueOf(stock));
			if (innerProductList.size() > 0) {
				list.add(master);
			}
			qty = 0;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

public ArrayList<Master> getWareHouseList(String location) {

	ArrayList<Master> list = new ArrayList<Master>();
	try {
		/*String sql = "select id,name from inventory_warehouse ";*/
		StringBuffer buffer = new StringBuffer();
		buffer.append("select id,name from inventory_warehouse ");
		if(!location.equals("0")){
			buffer.append("where id='"+location+"' ");
		}
		PreparedStatement ps = connection.prepareStatement(buffer.toString());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			Master master = new Master();
			master.setId(rs.getInt(1));
			master.setName(rs.getString(2));
			list.add(master);

		}

	} catch (Exception e) {

		e.printStackTrace();
	}

	return list;
}


public ArrayList<Product> getAllItemWiseReportList(String fromdate, String todate, String product_id, String location, Pagination pagination, String product_type, int isfromoepningclosing, String catalogueid) {
	ArrayList<Product> list = new ArrayList<Product>();
	try {
		/*if (!product_id.equals("")) {
			ArrayList<Product> arrayList = getProductItemSaleReportData(product_id,fromdate,todate,location,1,pagination);
			list.addAll(arrayList);
		}else{*/
			StringBuffer buffer = new StringBuffer();
			buffer.append("select distinct catalogueid from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_bill.id = apm_medicine_charges.invoiceid ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
			buffer.append("where apm_medicine_bill.date between '"+fromdate+"' and '"+todate+"' ");
			if(isfromoepningclosing==1){
				buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
			}else{
				if (!product_id.equals("")) {
					buffer.append("and inventory_product.prodname like '"+product_id+"%' ");
				}
			}
			
			if(!location.equals("0")){
				buffer.append("and apm_medicine_bill.location='"+location+"' ");
			}
			if (!product_type.equals("0")) {
				buffer.append("and inventory_catalogue.shedule='"+product_type+"' "); 
			}
			String sql1=pagination.getSQLQuery(buffer.toString());
			PreparedStatement ps = connection.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList<Product> arrayList = getProductItemSaleReportData(rs.getString(1),fromdate,todate,location,0,pagination);
				list.addAll(arrayList);
			}
		/*}*/
		
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

private ArrayList<Product> getProductItemSaleReportData(String catalogueid, String fromdate, String todate, String location, int i, Pagination pagination) {
	ArrayList<Product> list = new ArrayList<Product>();
	try {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct inventory_product.id from apm_medicine_charges ");
		buffer.append("inner join apm_medicine_bill on apm_medicine_bill.id = apm_medicine_charges.invoiceid ");
		buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
		buffer.append("where apm_medicine_bill.date between '"+fromdate+"' and '"+todate+"' ");
		buffer.append("and catalogueid='"+catalogueid+"' ");
		if(!location.equals("0")){
			buffer.append("and apm_medicine_bill.location='"+location+"' ");
		}
		String sql1="";
		if(i>0){
			sql1=pagination.getSQLQuery(buffer.toString());
		}else{
			sql1=buffer.toString();
		}
		PreparedStatement ps = connection.prepareStatement(sql1);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String pid = rs.getString(1);
			Product product = getProduct(pid);
			
			Product master = new Product();
			ArrayList<Product> innerProductList = new ArrayList<Product>();
			ArrayList<Product> tempList = getProductItemReport(pid, fromdate, todate, 0,location);
			innerProductList.addAll(tempList);
			Product product2=tempList.get(tempList.size()-1);
			master.setTotalqty(product2.getTotalqty());
			master.setRetrunqtycount(product2.getRetrunqtycount());
			master.setTotalsumqty(product2.getTotalsumqty());
			if (innerProductList.size() > 0) {
				master.setInnerProductList(innerProductList);
			}
			master.setVendor(product.getVendor());
			master.setProduct_name(product.getProduct_name());
			master.setGenericname(product.getGenericname());
			master.setHsnno(product.getHsnno());

			/*master.setStock(String.valueOf(stock));*/
			if (innerProductList.size() > 0) {
				list.add(master);
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

public int getAllItemWiseReportListCount(String fromdate, String todate, String product_id, String location, String product_type, int isfromoepningclosing, String catalogueid) {
	int res=0;
	try {
		if (!product_id.equals("0")) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("select count(distinct inventory_product.id) from apm_medicine_charges ");
				buffer.append("inner join apm_medicine_bill on apm_medicine_bill.id = apm_medicine_charges.invoiceid ");
				buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
				buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
				buffer.append("where apm_medicine_bill.date between '"+fromdate+"' and '"+todate+"' ");
				//buffer.append("and catalogueid='"+product_id+"' ");
				if(isfromoepningclosing==1){
					buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
				}else{
					if (!product_id.equals("")) {
						buffer.append("and inventory_product.prodname like '"+product_id+"%' ");
					}
				}
				/*if (!product_id.equals("")) {
					buffer.append("and inventory_product.prodname like '"+product_id+"%' ");
				}*/
				
				if(!location.equals("0")){
					buffer.append("and apm_medicine_bill.location='"+location+"' ");
				}
				if (!product_type.equals("0")) {
					buffer.append("and inventory_catalogue.shedule='"+product_type+"' "); 
				}
				PreparedStatement ps = connection.prepareStatement(buffer.toString());
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
						res = rs.getInt(1);
				}
		}else{
			StringBuffer buffer = new StringBuffer();
			buffer.append("select count(distinct catalogueid) from apm_medicine_charges ");
			buffer.append("inner join apm_medicine_bill on apm_medicine_bill.id = apm_medicine_charges.invoiceid ");
			buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
			buffer.append("where apm_medicine_bill.date between '"+fromdate+"' and '"+todate+"' ");
			if (!product_id.equals("0")) {
				buffer.append("and catalogueid='"+product_id+"' ");
			}
			if(!location.equals("0")){
				buffer.append("and apm_medicine_bill.location='"+location+"' ");
			}
			if (!product_type.equals("0")) {
				buffer.append("and inventory_catalogue.shedule='"+product_type+"' "); 
			}
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				res = rs.getInt(1);
			}
		}
		
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}



private int getPurchaseQty(String prodid){
	int res=0;
	PreparedStatement ps= null;
	try {
		String sql=" select qty from inventory_procurement where prodid='"+prodid+"' and inventory_procurement.deleted=0 and confirm=1 and gudreceipt=1 and  inventory_procurement.iscancel=0";
		ps= connection.prepareStatement(sql);
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			res=rs.getInt(1);
		}
		/*if(res==0){
			int transfprod=getOldProdidFromTransferLog(prodid);
			int reqtransfprod=getOldProdidFromRequestTransferLog(prodid);
			if(transfprod!=0){
				res=getPurchaseQty(String.valueOf(transfprod));
			}else{
				res=getPurchaseQty(String.valueOf(reqtransfprod));
			}
		}*/
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}
private int getOldProdidFromTransferLog(String prodid){
	int res=0;
	PreparedStatement ps= null;
	try {
		StringBuffer buffer= new StringBuffer();
		buffer.append("  select qty,old_prodid,req_trans_ret  from  inventory_transfer_log   ");
		buffer.append("  inner join inventory_parent_transfer_log on  inventory_parent_transfer_log.id =inventory_transfer_log.parentid where new_prodid='"+prodid+"' and req_trans_ret='1' ");
		ps= connection.prepareStatement(buffer.toString());
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			res=rs.getInt(2);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}
private int getOldProdidFromRequestTransferLog(String prodid){
	int res=0;
	PreparedStatement ps= null;
	try {
		StringBuffer buffer= new StringBuffer();
		buffer.append("  select old_prodid from  inventory_request_temp_log   ");
		buffer.append("  inner join inventory_parent_transfer_log on  inventory_parent_transfer_log.id =inventory_request_temp_log.parentid where new_prodid='"+prodid+"'  ");
		ps= connection.prepareStatement(buffer.toString());
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			res=rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}
public int changeChargesName(String chargeid,String val )
{
	int res=0;
	PreparedStatement ps=null;
	try {
		String sql="update apm_invoice_assesments set apmtType='"+val+"' where id="+chargeid+"";
		ps=connection.prepareStatement(sql);
		res=ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
	
}
public int changeUnitCost(String chargeid,String val )
{
	int res=0;
	PreparedStatement ps=null;
	try {
		String sql="update apm_invoice_assesments set charge='"+val+"' where id="+chargeid+"";
		ps=connection.prepareStatement(sql);
		res=ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
	
}

public int saveDeletedProcurement(String procchildid, String userid, String qty,String prodid, String charge,String procid ){
	int res=0;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    String date = dateFormat.format(cal.getTime());  
	
	try {
		String sql="insert into procurement_deleted_producr(prodid,proc_id,qty,charge,userid,datetime,childid) values(?,?,?,?,?,?,?)  ";
		PreparedStatement ps=connection.prepareStatement(sql);
		ps.setString(1, prodid);
		ps.setString(2, procid);
		ps.setString(3, qty);
		ps.setString(4, charge);
		ps.setString(5, userid);
		ps.setString(6, date);
		ps.setString(7, procchildid);
		res= ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public boolean checkProdIDInReturnList(String str) {
	boolean flag = false;
	try {
		String sql ="select id from inventory_product_temp_return where product_id='"+str+"' and status=0";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			flag = true;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return flag;
}

public int updateReturnProductData(int id, String vat, String discper, String discount, String gstamount, String total, String voucherno, String returnqty, String returnfreeqty) {
	int res=0;
	PreparedStatement ps=null;
	try {
		String sql="update inventory_product_return_log set newvat='"+vat+"',newdiscper='"+discper+"',newdiscount='"+discount+"',newgstamt='"+gstamount+"',newtotal='"+total+"',invoicenoo='"+voucherno+"',returnqty_ac='"+returnqty+"',returnfreeqty_ac='"+returnfreeqty+"' where id="+id+"";
		ps=connection.prepareStatement(sql);
		res=ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int updateParentReturnProductData(String grnreturnid, double total_amount, String sale_discount, String cgst,
		String sgst, String igst, String alltotvatTotal, String total_amt, String notes) {
	int res=0;
	PreparedStatement ps=null;
	try {
		String sql="update inventory_return_grn_parent set nettotal='"+total_amt+"',netvat='"+alltotvatTotal+"',netdiscount='"+sale_discount+"',netcgst='"+cgst+"',netsgst='"+sgst+"',netigst='"+igst+"',netsubtotal='"+total_amount+"',mainnotes=? where id='"+grnreturnid+"'";
		ps=connection.prepareStatement(sql);
		ps.setString(1, notes);
		res=ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public boolean checkVendorPresentInSystem(String str) {
	boolean flag = false;
	try {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select inventory_product.id from inventory_product ");
		buffer.append("inner join inventory_vendor on inventory_vendor.id = inventory_product.supplierid ");
		buffer.append("where inventory_product.id='"+str+"' ");
		PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			flag = true;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return flag;
}

public String getProductNameFromID(String prodid) {
	String res ="0";
	try {
		String sql ="select prodname from inventory_product where id='"+prodid+"'";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = rs.getString(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public Product getProductCatalogueDetailsforsale(String catalogueid, String location) {
	Product product = new Product();
	StringBuffer buffer=new StringBuffer();
	String sql="";
	try {
		buffer.append("SELECT id, categoryid, subcategoryid, product_name, mrp, purchase_price, sale_price, gst, datetime, location, lastmodified, ");
		buffer.append("genericname,pack,hsnno,mfg,shedule,description,minorder,maxorder,product_code from inventory_catalogue where id="+catalogueid+" ");
	
		
			if(!location.equals("0")){
				buffer.append("and location="+location+" ");
			
		}
		sql=buffer.toString();
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {

			product.setId(rs.getInt(1));
			product.setCategory_id(rs.getString(2));
			product.setSubcategory_id(rs.getString(3));
			product.setProduct_name(rs.getString(4));
			product.setMrp(rs.getString(5));
			product.setPurchase_price(rs.getString(6));
			product.setSale_price(rs.getString(7));
			product.setVat(rs.getString(8));
			product.setDateTime(rs.getString(9));
			product.setLocation(rs.getString(10));
			product.setLastmodified(rs.getString(11));
			product.setGenericname(rs.getString(12));
			product.setPack(rs.getString(13));
			product.setHsnno(rs.getString(14));
			product.setMfg(rs.getString(15));
			product.setMedicine_shedule(rs.getString(16));
			product.setDescription(rs.getString(17));
			product.setMinorder(rs.getString(18));
			product.setMaxorder(rs.getString(19));
			product.setCatalogueid(catalogueid);
			product.setPro_code(rs.getString(20));
		}

	} catch (Exception e) {

		e.printStackTrace();
	}
	return product;
}

public int getTotalStockProductforsale(String catalogueid, String location) {
	int res=0;
	try {
		String sql="select sum(stock) from inventory_product where catalogueid="+catalogueid+""; 
			if(!location.equals("0")){
				sql=sql+""+" and location="+location+"";
			}
		PreparedStatement ps=connection.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while(rs.next()){
		     res =rs.getInt(1);	
		}
		
	} catch (Exception e) {

		e.printStackTrace();
	} 
	return res;
}

public int getMedLimit(String userId) {
	int result=0;
	try {
		String sql="select medlimit from apm_pharmacy_user where userid='"+userId+"'";
		PreparedStatement ps=connection.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while(rs.next()){
		     result =rs.getInt(1);	
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public String getOpeningStockStringNew(String fromDate, String toDate, String ismonthlyreport, String searchbyprodname,
		String location_filter, String category_id) {
	PreparedStatement preparedStatement = null;
	StringBuffer buffer = new StringBuffer();
	buffer.append("SELECT prodid  ");
	buffer.append("FROM medicine_opening_stock ");
	buffer.append(" inner join inventory_product on medicine_opening_stock.prodid=inventory_product.id ");
	buffer.append("where commencing between '" + fromDate + "' and '" + toDate + "' and isconsume='0' ");
	if (searchbyprodname != null) {
		buffer.append("and (prodname like ('%" + searchbyprodname + "%') or batch_no like ('%" + searchbyprodname + "%') )  ");
	}
	if(!location_filter.equals("0")){
		buffer.append("and inventory_product.location='"+location_filter+"' ");
	}
	if(!category_id.equals("0")){
		buffer.append("and inventory_product.categoryid='"+category_id+"' ");
	}
	String prodids="0";
	try {

		preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			prodids = prodids+","+rs.getInt(1);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	return prodids;
}

public ArrayList<Product> getOpeningStockListNew(String fromDate, String toDate, Pagination pagination, int min,
		int max, String ismonthlyreport, String searchbyprodname, String location_filter, String category_id,
		String alreadypresentids) {
	PreparedStatement preparedStatement = null;
	ArrayList<Product> list = new ArrayList<Product>();
	StringBuffer buffer = new StringBuffer();
	PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
	buffer.append("SELECT id,stock  ");
	buffer.append("FROM inventory_product ");
	buffer.append("where id not in ("+alreadypresentids+") and stock>0 ");
	if (searchbyprodname != null) {
		buffer.append("and (prodname like ('%" + searchbyprodname + "%') or batch_no like ('%" + searchbyprodname + "%') )  ");
	}
	if(!location_filter.equals("0")){
		buffer.append("and inventory_product.location='"+location_filter+"' ");
	}
	if(!category_id.equals("0")){
		buffer.append("and inventory_product.categoryid='"+category_id+"' ");
	}
	buffer.append("order by id desc ");
	if (max > 0) {
		buffer.append(" limit " + min + " , " + max + " ");
	} else {
		buffer.append(" limit 0,1000");
	}
	double tot = 0;
	int totalqty = 0;
	int opentotalcount = 0;
	int totalopeningval = 0;
	try {

		preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			Product product2 = getProduct(rs.getString(1));
			Product pmaster= getProductCatalogueDetails(product2.getCatalogueid());
			String productname = product2.getProduct_name();
			product.setCategory(product2.getCategory());
			product.setProduct_name(productname);
			product.setOpeningstock(rs.getString(2));
			/*int sale = getClosingstock(productid, rs.getString(4), rs.getString(4));
			int returnstock = getRetuenstock(productid, rs.getString(4), rs.getString(4));
			int returntosupplier = getRetuenToSuplierstock(productid, rs.getString(4), rs.getString(4));
			sale = sale+returntosupplier;
			int closingstock = (rs.getInt(2)+rs.getInt(7)) - (sale - returnstock);*/
			int sale =0;
			int returnstock=0;
			int closingstock =rs.getInt(2);
			product.setSale(Integer.toString(sale));
			product.setReturnstock(Integer.toString(returnstock));
			product.setClosingstock(Integer.toString(closingstock));
			product.setPurchaseqty(returnstock);
			product.setIndentqty(0);
			String locationName = pharmacyDAO.getLocationName(product2.getLocation());
			product.setLocationName(locationName);
			product.setPro_code(pmaster.getPro_code());
			if(product2.getSale_price()!=null){
				if(product2.getSale_price().equals("")){
					product2.setSale_price("0");
				}
			}else{
				product2.setSale_price("0");
			}
			double salevalue = sale*Double.parseDouble(product2.getSale_price());
			if (product2.getPack() != null) {
				if (product2.equals("0") || product2.equals("")) {
					product2.setPack("1");
				}
			} else {
				product2.setPack("1");
			}
			product.setSalevalue(salevalue);
			double unitprice = Double.parseDouble(product2.getPurchase_price())
					/ Integer.parseInt(product2.getPack());
			double amt1 = closingstock * unitprice;
			tot = tot + amt1;
			product.setUnit("" + Math.round(amt1 * 100.0) / 100.0);
			product.setTotal_amount(tot);
			totalqty = totalqty + closingstock;
			product.setTotalqty(totalqty);
			opentotalcount = rs.getInt(2) + opentotalcount;
			product.setOpeningstockvalue(rs.getDouble(2) * unitprice);
			product.setOpeningstockvalue(Math.round(product.getOpeningstockvalue() * 100.0) / 100.0);
			product.setTotalopeningval(product.getOpeningstockvalue() + totalopeningval);
			totalopeningval = (int) product.getTotalopeningval();
			product.setOpentotalcount(String.valueOf(opentotalcount));

			// lokesh
			product.setMfg(product2.getMfg());
			product.setExpiry_date(product2.getExpiry_date());
			product.setBatch_no(product2.getBatch_no());
			product.setMrp(product2.getMrp());
			product.setPurchase_price(product2.getPurchase_price());
			product.setSale_price(product2.getSale_price());

			product.setVendor(product2.getVendor());

			product.setPurchase_stock_qty(getPurchaseQty(rs.getString(1)));
			product.setUnitprices(""+unitprice);
			product.setSv(DateTimeUtils.changeFormat(Math.round(amt1 * 100.0) / 100.0));
			product.setMinorder(pmaster.getMinorder());
			product.setMaxorder(pmaster.getMaxorder());
			list.add(product);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	return list;
}

public int getCountOpeningStockListNew(String fromDate, String toDate, String searchbyprodname, String location_filter,
		String category_id, String alreadypresentids) {
	PreparedStatement preparedStatement = null;
	StringBuffer buffer = new StringBuffer();
	buffer.append("SELECT count(*)  ");
	buffer.append("FROM inventory_product ");
	buffer.append("where id not in ("+alreadypresentids+") and stock>0 ");
	if (searchbyprodname != null) {
		buffer.append("and (prodname like ('%" + searchbyprodname + "%') or batch_no like ('%" + searchbyprodname + "%') )  ");
	}
	if(!location_filter.equals("0")){
		buffer.append("and inventory_product.location='"+location_filter+"' ");
	}
	if(!category_id.equals("0")){
		buffer.append("and inventory_product.categoryid='"+category_id+"' ");
	}
	buffer.append("order by id desc ");
	int total=0;
	try {

		preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			total = rs.getInt(1);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	return total;
}

public int cancelReturnToSupplierReq(String parentid, String delete_reason, String userid, String datetime) {
	int res =0;
	try {
		String sql ="update inventory_product_return_log set iscancel=?, cancel_notes=?, cancel_date=?, cancel_userid=? where grnreturnid=?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, "1");
		preparedStatement.setString(2, delete_reason);
		preparedStatement.setString(3, datetime);
		preparedStatement.setString(4, userid);
		preparedStatement.setString(5, parentid);
		res = preparedStatement.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int getCountOpeningStockListByCatalogueWise(String fromDate, String toDate, Pagination pagination, int minlimit,
		int maxlimit, String searchbyprodname, String category_id, String location_filter) {
	PreparedStatement preparedStatement = null;
	int res =0;
	StringBuffer buffer = new StringBuffer();
	try {
		buffer.append("select count(distinct inventory_catalogue.id) ");
		buffer.append("from inventory_catalogue ");
		buffer.append("inner join inventory_product on inventory_product.catalogueid = inventory_catalogue.id  ");
		buffer.append("where procurement=0 and product_name is not null ");
		if (searchbyprodname != null) {
			buffer.append("and (inventory_product.prodname like ('%" + searchbyprodname + "%') or inventory_product.batch_no like ('%" + searchbyprodname + "%') or inventory_product.mfg like ('%" + searchbyprodname + "%') )  ");
		}
		if(!location_filter.equals("0")){
			buffer.append("and inventory_product.location='"+location_filter+"' ");
		}
		if(!category_id.equals("0")){
			buffer.append("and inventory_product.categoryid='"+category_id+"' ");
		}
		buffer.append("order by product_name ");
		preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int updateRemainFreeQty(String proc_childid, int totalfreereturn) {
	int res =0;
	try {
		String sql ="update inventory_procurement set remainfreeqty=? where id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, ""+totalfreereturn);
		preparedStatement.setString(2, proc_childid);
		res = preparedStatement.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int updateProductStocks(String id, int qty) {
	int res =0;
	try {
		String updatesql = "update inventory_product set stock='" + qty + "' where id=" + id + " ";
		PreparedStatement preparedStatement = connection.prepareStatement(updatesql);
		res = preparedStatement.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int insertAdjustmentProductData(String id, String adj_type, int stock, int qty, int changerqty, String comment,
		String userid, String date) {
	int result = 0;

	try {
		String sql = "insert into adjustment_data (adjustment_type,product_id,pre_stock,current_stock,change_qty,userid,datetime,remark) values (?,?,?,?,?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, adj_type);
		ps.setString(2, id);
		ps.setString(3, ""+stock);
		ps.setString(4, ""+qty);
		ps.setString(5, ""+changerqty);
		ps.setString(6, userid);
		ps.setString(7, date);
		ps.setString(8, comment);
		result = ps.executeUpdate();
		if(result>0){
			ResultSet rs=ps.getGeneratedKeys();
			while(rs.next()){
				 result=rs.getInt(1);
			}
		}
	} catch (Exception e) {

		e.printStackTrace();
	}

	return result;
}

public ArrayList<Product> getProductWiseReportListNew(String fromdate, String todate, String product_id, String location,
		Pagination pagination, String product_type) {
	ArrayList<Product> list = new ArrayList<Product>();
	try {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select apm_medicine_bill.date,apm_medicine_bill.isreturn, ");
		buffer.append("apm_medicine_charges.practitionerName,apm_medicine_charges.user,apm_medicine_charges.invoiceid, ");
		buffer.append("apm_medicine_charges.quantity,apm_medicine_bill.cgst,apm_medicine_bill.sgst,apm_medicine_bill.id, ");
		buffer.append("apm_condition.name,apm_medicine_charges.charge,inventory_product.id,apm_medicine_charges.gstper,apm_medicine_charges.cgst,  ");
		buffer.append("apm_medicine_charges.sgst,apm_medicine_charges.discount_share from apm_medicine_bill ");
		buffer.append("left join apm_condition on apm_condition.id=apm_medicine_bill.location ");
		buffer.append("inner join apm_medicine_charges on apm_medicine_bill.id= apm_medicine_charges.invoiceid ");
		buffer.append("inner join inventory_product on apm_medicine_charges.product_id = inventory_product.id ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
		buffer.append("where apm_medicine_bill.date between '"+ fromdate + "' and '" + todate + "' and apm_medicine_bill.deleted=0 ");
		if(!location.equals("0")){
			buffer.append("and apm_medicine_bill.location='"+location+"' ");
		}
		if(!product_id.equals("")){
			buffer.append("and inventory_product.prodname like '%"+product_id+"%' ");
		}
		if(!product_type.equals("0")){
			buffer.append("and inventory_catalogue.shedule='"+product_type+"' ");
		}
		
		buffer.append("order by apm_medicine_bill.date desc ");
		String sql = pagination.getSQLQuery(buffer.toString());
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		PharmacyDAO pharmacyDAO=new JDBCPharmacyDAO(connection);
		int qtycount=0;
		int retrunqtycount=0;
		while (rs.next()) {
			Product product = new Product();
			product = getProduct(rs.getString(12));
			product.setDate(DateTimeUtils.getCommencingDate1(rs.getString(1)));
			int ret = rs.getInt(2);
			String doctor = rs.getString(3);
			String clientname = rs.getString(4);
			String billno = rs.getString(5);
			if(ret>0){
				product.setQty("0");
				retrunqtycount = retrunqtycount+rs.getInt(6);
			}else{
				product.setQty(rs.getString(6));
				qtycount=rs.getInt(6)+qtycount;
			}
			product.setRetrunqtycount(retrunqtycount);
			product.setTotalqty(qtycount);
			int totalsumqty = qtycount - retrunqtycount;
			product.setTotalsumqty(totalsumqty);
			product.setDoctor(doctor);
			product.setClientname(clientname);
			product.setUserid(getUserIdOfBill(billno));
			Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(product.getUserid());
			product.setFullname(priscription.getFullname());
			if (ret > 0) {
				product.setReturnQty(rs.getString(6));
			} else {
				product.setReturnQty("0");
			}
			product.setCgst(rs.getString(7));
			product.setSgst(rs.getString(8));
			if (product.getVat() == null) {
				product.setVat("0");
			} else if (product.getVat().equals("")) {
				product.setVat("0");
			}
			
			product.setBillno(rs.getString(9));
			product.setLocationName(rs.getString(10));
			product.setNew_mrp(product.getMrp());
			product.setMrp(rs.getString(11));
			
			product.setVat(rs.getString(13));
			product.setCgst(rs.getString(14));
			product.setSgst(rs.getString(15));
			product.setDiscount(rs.getString(16));
			product.setTotalAmt(Math.round((rs.getInt(6)*rs.getDouble(11)) * 100.0)/100.0);
			list.add(product);

		}

	} catch (Exception e) {

		e.printStackTrace();
	}
	return list;
}

public int getProductWiseReportListCount(String fromdate, String todate, String product_id, String location, String product_type) {
	int res =0;
	try {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from apm_medicine_bill ");
		buffer.append("left join apm_condition on apm_condition.id=apm_medicine_bill.location ");
		buffer.append("inner join apm_medicine_charges on apm_medicine_bill.id= apm_medicine_charges.invoiceid ");
		buffer.append("inner join inventory_product on apm_medicine_charges.product_id = inventory_product.id ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
		buffer.append("where apm_medicine_bill.date between '"+ fromdate + "' and '" + todate + "' ");
		if(!location.equals("0")){
			buffer.append("and apm_medicine_bill.location='"+location+"' ");
		}
		if(!product_id.equals("")){
			buffer.append("and inventory_product.prodname like '%"+product_id+"%' ");
		}
		if(!product_type.equals("0")){
			buffer.append("and inventory_catalogue.shedule='"+product_type+"' ");
		}
		
		PreparedStatement ps = connection.prepareStatement(buffer.toString());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			res = rs.getInt(1);
		}

	} catch (Exception e) {

		e.printStackTrace();
	}
	return res;
}

public int changeApmtcode(String chargeid, String val) {
	{
		int res=0;
		PreparedStatement ps=null;
		try {
			String sql="update apm_invoice_assesments set pkgcode='"+val+"' where id="+chargeid+"";
			ps=connection.prepareStatement(sql);
			res=ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
		
	}
}

public int changeApmtqty(String chargeid, String val) {
	int res=0;
	PreparedStatement ps=null;
	try {
		String sql="update apm_invoice_assesments set quantity='"+val+"' where id="+chargeid+"";
		ps=connection.prepareStatement(sql);
		res=ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
	
}

public ArrayList<Product> getOpeningStockListByCatalogueWiseNew(String fromDate, String toDate, Pagination pagination,
		int minlimit, int maxlimit, String searchbyprodname, String category_id, String location_filter) {
	PreparedStatement preparedStatement = null;
	ArrayList<Product> list = new ArrayList<Product>();
	StringBuffer buffer = new StringBuffer();
	try {
		/*buffer.append("select inventory_catalogue.id,inventory_catalogue.product_name,product_code, inventory_catalogue_log.date, inventory_catalogue_log.location, opening_stock, opeing_value, sum(qty_in+0), sum(qty_in_value+0), sum(qty_out+0), sum(qty_out_value+0), sum(stock_value+0), sum(Unknown_qty+0) ");
		buffer.append("from inventory_catalogue_log ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_catalogue_log.catalogueid  ");
		buffer.append("where inventory_catalogue_log.date between '"+fromDate+"' and '"+toDate+"' ");
		
		if (searchbyprodname != null) {
			buffer.append("and (inventory_catalogue.product_name like ('%" + searchbyprodname + "%')  )  ");
		}
		if(!location_filter.equals("0")){
				buffer.append("and inventory_catalogue_log.location='"+location_filter+"' ");
			
		}
		if(!category_id.equals("0")){
				buffer.append("and inventory_product.categoryid='"+category_id+"' ");
		}
		buffer.append("group by inventory_catalogue.id ");
		buffer.append("order by inventory_catalogue.product_name ");*/
		
		buffer.append("select inventory_catalogue.id,inventory_catalogue.product_name,product_code, cataloguelogtemp.date, ");
		buffer.append("cataloguelogtemp.location, opening_stock, opeing_value, sum(qty_in+0), sum(qty_in_value+0), sum(qty_out+0), ");
		buffer.append("sum(qty_out_value+0), sum(stock_value+0), sum(Unknown_qty+0), sum(unknown_value+0) ");
		buffer.append("from (SELECT * FROM inventory_catalogue_log ORDER BY inventory_catalogue_log.date asc ) AS cataloguelogtemp ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id = cataloguelogtemp.catalogueid ");
		buffer.append("where cataloguelogtemp.date between '"+fromDate+"' and '"+toDate+"' ");
		if(!location_filter.equals("0")){
			buffer.append("and cataloguelogtemp.location='"+location_filter+"' ");
		}
		if (searchbyprodname != null) {
			buffer.append("and (inventory_catalogue.product_name like ('%" + searchbyprodname + "%')  )  ");
		}
		buffer.append("group by inventory_catalogue.id ");
		buffer.append("having ((opening_stock+0) + sum(qty_in+0) +  sum(qty_out+0))>0 ");
		buffer.append("order by inventory_catalogue.product_name ");
		String sql =buffer.toString();
		if(pagination!=null){
			sql= pagination.getSQLQuery(sql);
		}
		
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		int totalopeningstock =0;
		double totalopeingstockvalue =0;
		int totalqtyin =0;
		double totalqtyinvalue=0;
		int totalqtyout=0;
		double totalstockvalue=0;
		double totalsaleprice=0;
		int totalclosingstock=0;
		double totalclosingvalue=0;
		int totalunknownqty=0;
		double totalunknownvalue=0;
		while (rs.next()) {
			Product product = new Product();
			product.setProduct_name(rs.getString(2));
			product.setPro_code(rs.getString(3));
			String catalogueid = rs.getString(1);
			Product product2 = getProductDataByCatalogueid(catalogueid);
			product.setCategory(product2.getCategory());
			product.setMfg(product2.getMfg());
			
			/*Product product3 = getNextOpeningData(fromDate,catalogueid,location_filter);
			int openingstock = product3.getTotalclosingstock();
			double opeingstockvalue = product3.getTotalclosingvalue();*/
			int openingstock = rs.getInt(6);
			double opeingstockvalue=rs.getDouble(7);
			/*if(openingstock<0){
				openingstock=0;
			}
			if(opeingstockvalue<0){
				opeingstockvalue=0;
			}*/
			int unknownqty = rs.getInt(13);
			double unknownvalue =rs.getDouble(14);
			int qtyin = rs.getInt(8);
			double qtyinvalue =rs.getDouble(9);
			int qtyout = rs.getInt(10);
			//Stock value =  Patient sale unit price + Return to Supplier purchase price + Consume purchase price
			double stockvalue = rs.getDouble(11);
			//sale price total 
			double saleprice = rs.getDouble(12);
			//Closing - opening + qtyin - qtyout
			int closingstock = openingstock + qtyin - qtyout+unknownqty;
			//double closingvalue = unitprice * closingstock;
			double closingvalue = opeingstockvalue + qtyinvalue - stockvalue;
			double tempclsoingvalue= closingvalue;
			
			if(closingvalue<0){
				closingvalue =0;
				tempclsoingvalue=0;
				/*unknownvalue = Math.abs(closingvalue);
				tempclsoingvalue = unknownvalue;*/
			}
			/*if((openingstock +qtyin)<qtyout){
				closingstock =0;
				closingvalue =0;
				unknownqty = qtyout - (openingstock +qtyin);
			}*/
			//double unitprice = getUnitPriceFromCatalogueid(catalogueid);
			product.setUnknownqty(unknownqty);
			product.setUnknownvalue(Math.round(unknownvalue * 100.0)/100.0);
			product.setOpeningstock(""+openingstock);
			product.setOpeningstockvalue(Math.round(opeingstockvalue * 100.0)/100.0);
			product.setPurchaseqty(qtyin);
			product.setSale(""+qtyout);
			product.setSalevalue(Math.round(stockvalue * 100.0)/100.0);
			product.setClosingstock(""+closingstock);
			product.setSv(DateTimeUtils.changeFormat(Math.round(closingvalue * 100.0)/100.0));
			product.setQtyinvalue(DateTimeUtils.changeFormat(qtyinvalue));
			product.setSale_price(DateTimeUtils.changeFormat(Math.round(saleprice * 100.0)/100.0));
			totalunknownqty = totalunknownqty + unknownqty;
			totalopeningstock =totalopeningstock + openingstock;
			totalopeingstockvalue =totalopeingstockvalue + opeingstockvalue;
			totalqtyin =totalqtyin + qtyin;
			totalqtyinvalue=qtyinvalue + totalqtyinvalue;
			totalqtyout = totalqtyout+qtyout;
			totalstockvalue = totalstockvalue+stockvalue;
			totalsaleprice = totalsaleprice + saleprice;
			totalclosingstock = totalclosingstock + closingstock;
			totalclosingvalue = totalclosingvalue + tempclsoingvalue;
			totalunknownvalue = totalunknownvalue + unknownvalue;
			product.setTotalopeningstock(totalopeningstock);
			product.setTotalopeingstockvalue(totalopeingstockvalue);
			product.setTotalqtyin(totalqtyin);
			product.setTotalqtyinvalue(totalqtyinvalue);
			product.setTotalqtyout(totalqtyout);
			product.setTotalstockvalue(totalstockvalue);
			product.setTotalssaleprice(totalsaleprice);
			product.setTotalclosingstock(totalclosingstock);
			product.setTotalclosingvalue(totalclosingvalue);
			product.setTotalunknownqty(totalunknownqty);
			product.setTotalunknownvalue(totalunknownvalue);
			product.setCatalogueid(catalogueid);
			product.setFromdate(fromDate);
			product.setTodate(toDate);
			list.add(product);
			//list.add(product);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}
private Product getNextOpeningData(String nextdate, String catalogueid, String location_filter) {
	Product product = new Product();
	try {
		String sql ="select opening_stock, opeing_value from inventory_catalogue_log where date='"+nextdate+"' and location='"+location_filter+"' and catalogueid='"+catalogueid+"' ";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		product.setTotalclosingstock(0);
		product.setTotalclosingvalue(0);
		if (rs.next()) {
			product.setTotalclosingstock(rs.getInt(1));
			product.setTotalclosingvalue(rs.getDouble(2));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return product;
}
public int getCountOpeningStockListByCatalogueWiseNew(String fromDate, String toDate, Pagination pagination,
		int minlimit, int maxlimit, String searchbyprodname, String category_id, String location_filter) {
	PreparedStatement preparedStatement = null;
	int res =0;
	StringBuffer buffer = new StringBuffer();
	try {
		/*buffer.append("select count(*) ");
		buffer.append("from inventory_catalogue_log ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_catalogue_log.catalogueid  ");
		buffer.append("where inventory_catalogue_log.date between '"+fromDate+"' and '"+toDate+"' ");
		
		if (searchbyprodname != null) {
			buffer.append("and (inventory_catalogue.product_name like ('%" + searchbyprodname + "%')  )  ");
		}
		if(!location_filter.equals("0")){
				buffer.append("and inventory_catalogue_log.location='"+location_filter+"' ");
			
		}
		if(!category_id.equals("0")){
				buffer.append("and inventory_product.categoryid='"+category_id+"' ");
		}
		buffer.append("group by inventory_catalogue.id ");
		buffer.append("order by inventory_catalogue.product_name ");*/
		buffer.append("select opening_stock ");
		buffer.append("from (SELECT * FROM inventory_catalogue_log ORDER BY inventory_catalogue_log.date asc ) AS cataloguelogtemp ");
		buffer.append("inner join inventory_catalogue on inventory_catalogue.id = cataloguelogtemp.catalogueid ");
		buffer.append("where cataloguelogtemp.date between '"+fromDate+"' and '"+toDate+"' ");
		if(!location_filter.equals("0")){
			buffer.append("and cataloguelogtemp.location='"+location_filter+"' ");
		}
		if (searchbyprodname != null) {
			buffer.append("and (inventory_catalogue.product_name like ('%" + searchbyprodname + "%')  )  ");
		}
		buffer.append("group by inventory_catalogue.id ");
		buffer.append("having ((opening_stock+0) + sum(qty_in+0) +  sum(qty_out+0))>0 ");
		
		preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = res+1;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public Product getNextOpeingData(String nextdate, String catalogueid, String location_filter) {
	Product product = new Product();
	try {
		String sql ="select opening_stock, opeing_value from inventory_catalogue_log where date='"+nextdate+"' and location='"+location_filter+"' and catalogueid='"+catalogueid+"' ";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		product.setTotalclosingstock(0);
		product.setTotalclosingvalue(0);
		if (rs.next()) {
			product.setTotalclosingstock(rs.getInt(1));
			product.setTotalclosingvalue(rs.getDouble(2));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return product;
}

public boolean checkInventoryCatalogueStatus(String fromDate, String catalogueid, String location_filter) {
	boolean flag = false;
	try {
		String sql ="select * from inventory_catalogue_log where date='"+fromDate+"' and catalogueid='"+catalogueid+"' and location='"+location_filter+"'";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			flag = true;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return flag;
}

public int insertIntoProductLog(String userid, String datetime, String location, int qtyinout, String product_id,
		String catalogueid, String source, int currentstock, int previousstock, int changeqty,String procurementid, String grnreturnid, int billno, int adjustmentid, int indentid, String consumeid) {
	int result=0;
	try {
		String sql="insert into product_stock_log (source, location, userid, qty_in_out, productid, catalogueid, current_qty, previous_qty, change_qty,procurementid,returnsupplierid,pharmacysaleid,adjustmentid,indentid,consumeid,datetime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps=connection.prepareStatement(sql);
		ps.setString(1, source);
		ps.setString(2, location);
		ps.setString(3, userid);
		ps.setString(4, ""+qtyinout);
		ps.setString(5, product_id);
		ps.setString(6, catalogueid);
		ps.setString(7, ""+currentstock);
		ps.setString(8, ""+previousstock);
		ps.setString(9, ""+changeqty);
		ps.setString(10, procurementid);
		ps.setString(11, grnreturnid);
		ps.setString(12, ""+billno);
		ps.setString(13, ""+adjustmentid);
		ps.setString(14, ""+indentid);
		ps.setString(15, ""+consumeid);
		ps.setString(16, datetime);
		result=ps.executeUpdate();
		if(result>0){
			ResultSet rs=ps.getGeneratedKeys();
			while(rs.next()){
				 result=rs.getInt(1);
			}
		}
	} catch (Exception e) {

		e.printStackTrace();
	}
	
	return result;
}

public int getPreviousStock(String product_id) {
	int res =0;
	try {
		String sql="select stock from inventory_product where id='"+product_id+"'";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			res = rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int getCountExpiryMedicineReportNew(String fromdate, String todate, String days, String location,
		String report, String vendor_id) {
	int result=0;
	try {
		//Akash 28 sep 2017 For Pafination
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from inventory_product ");
		buffer.append("where inventory_product.expiry_date between '" + fromdate + "' and '" + todate + "' and inventory_product.stock>0 ");
		if (!location.equals("0")) {
			buffer.append("and inventory_product.location='" + location + "' ");
		}
		if (!vendor_id.equals("0")) {
			buffer.append("and inventory_product.supplierid='" + vendor_id + "' ");
		}
		//buffer.append("group by inventory_product.id order by expiry_date ");
		PreparedStatement ps = connection.prepareStatement(buffer.toString());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			result = rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public ArrayList<Product> getExpiryMedicineReportNew(String fromdate, String todate, String days, String location,
		String report, Pagination pagination, String vendor_id) {
	ArrayList<Product> list = new ArrayList<Product>();
	InventoryVendorDAO inventoryVendorDAO = new JDBCInventoryVendorDAO(connection);
	PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
	try {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String nowdate = dateFormat.format(calendar.getTime());
		StringBuffer buffer = new StringBuffer();
		buffer.append("select inventory_product.id,inventory_product.prodname,inventory_product.mrp,inventory_product.purchaseprice, ");
		buffer.append("inventory_product.saleprice,inventory_product.stock,inventory_product.expiry_date,inventory_product.supplierid, ");
		buffer.append("inventory_product.genericname,inventory_product.pack,inventory_product.location from inventory_product ");
		buffer.append("where inventory_product.expiry_date between '" + fromdate + "' and '" + todate + "' and inventory_product.stock>0 ");
		if (!location.equals("0")) {
			buffer.append("and inventory_product.location='" + location + "' ");
		}
		if (!vendor_id.equals("0")) {
			buffer.append("and inventory_product.supplierid='" + vendor_id + "' ");
		}
		buffer.append("order by inventory_product.expiry_date asc ");
		String sql =pagination.getSQLQuery(buffer.toString());
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Product product = new Product();
			product.setId(rs.getInt(1));
			product.setProduct_name(rs.getString(2));
			product.setMrp(rs.getString(3));
			product.setPurchase_price(rs.getString(4));
			product.setSale_price(rs.getString(5));
			product.setStock(rs.getString(6));
			String expiry = rs.getString(7);
			if (product.getPurchase_price() == null) {
				product.setPurchase_price("0");
			}
			if (product.getStock() == null) {
				product.setStock("0");
			}
			int pack=1;
			if(rs.getInt(10)!=0){
				pack = rs.getInt(10);
			}
			double total = (rs.getDouble(4)/pack) * rs.getDouble(6);
			if (expiry == null) {
				expiry = nowdate;
			}
			product.setExpiry_date(DateTimeUtils.getCommencingDate1(expiry));
			product.setTotal(DateTimeUtils.changeFormat(total));

			Vendor vendor = inventoryVendorDAO.getVendor(rs.getString(8));
			product.setVendor(vendor.getName());
			product.setContact(vendor.getMobile_pri());
			product.setGenericname(rs.getString(9));
			//product.setVoucherno(rs.getString(10));

			long diffdays = DateTimeUtils.getDifferenceOfTwoDateDBFormat(nowdate, expiry);
			product.setDays("" + Math.abs(diffdays) + " Days");
			String locationame = pharmacyDAO.getPharmacyLocation(rs.getString(11));
			product.setLocationName(locationame);
			if(report.equals("2")){
				if(diffdays>0){
					product.setStatus("3");
				}else{
					product.setStatus("2");
				}
			}else if(report.equals("3")){
				if(diffdays>0){
					product.setStatus("3");
				}else{
					product.setStatus("2");
				}
			}
			list.add(product);			

		}

	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

public Product getOpeingClosingBiferSale(String fromdate, String todate, String searchbyprodname,
		String location_filter) {
	Product product= new Product();
	try {
		todate = todate +" "+ "59:59:59";
		StringBuffer buffer= new StringBuffer();
		buffer.append("select sum(quantity),sum(quantity*charge),sum(quantity*(purchaseprice/pack))  ");
		buffer.append("from apm_medicine_charges ");
		buffer.append("inner join apm_medicine_bill on apm_medicine_charges.invoiceid = apm_medicine_bill.id ");
		buffer.append("inner join inventory_product on inventory_product.id = apm_medicine_charges.product_id ");
		buffer.append("where apm_medicine_bill.isreturn =0 and  date between '"+fromdate+"' and '"+todate+"' and apm_medicine_bill.deleted=0  ");
		if(!location_filter.equals("0")){
			buffer.append("and apm_medicine_bill.location='"+location_filter+"' ");
		}
		PreparedStatement preparedStatement=connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		double total=0;
		int totalqty=0;
		product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
		product.setTotalqty(totalqty);
		product.setTotal_amount(total);
		product.setSalepricetotal(0);
		while (rs.next()) {
			product.setSalepricetotal(rs.getDouble(2));
			product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
			product.setTotalqty(rs.getInt(1));
			product.setTotal_amount(rs.getDouble(3));
		}
		
	} catch (Exception e) {

		e.printStackTrace();
	}
	
	return product;
}

public Product getOpeingClosingBiferDirectTransfer(String fromDate, String toDate, String searchbyprodname,
		String location_filter) {
	Product product = new Product();
	try {
		toDate = toDate +" "+ "59:59:59";
		StringBuffer buffer= new StringBuffer();
		//req_or_transfer==1
		buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)),sum(inventory_transfer_log.qty*saleprice) from inventory_transfer_log ");
		buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
		buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
		buffer.append("where inventory_parent_transfer_log.req_or_trans=1 and inventory_parent_transfer_log.issued_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
		if (!location_filter.equals("0")) {
			buffer.append("and from_location ='" + location_filter + "' ");
		}
		PreparedStatement ps=connection.prepareStatement(buffer.toString());
		ResultSet rs =ps.executeQuery();
		double total=0;
		int totalqty=0;
		product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
		product.setTotalqty(totalqty);
		product.setTotal_amount(total);
		product.setSalepricetotal(0);
		while(rs.next()){
			product.setSalepricetotal(rs.getDouble(4));
			product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
			product.setTotalqty(rs.getInt(1));
			product.setTotal_amount(rs.getDouble(3));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return product;
}

public Product getOpeingClosingBiferRequestTransfer(String fromDate, String toDate, String searchbyprodname,
		String location_filter) {
	Product product = new Product();
	try {
		toDate = toDate +" "+ "59:59:59";
		StringBuffer buffer=new StringBuffer();
		buffer.append("SELECT sum(inventory_request_temp_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_request_temp_log.qty*(purchaseprice/pack)),sum(inventory_request_temp_log.qty*saleprice)   ");
		buffer.append("FROM inventory_request_temp_log ");
		buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_request_temp_log.parentid ");
		buffer.append("inner join inventory_product on  inventory_product.id = inventory_request_temp_log.old_prodid ");
		buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
		//buffer.append("and inventory_product.catalogueid='"+catalogueid+"' ");
		if (!location_filter.equals("0")) {
			buffer.append("and inventory_request_temp_log.location ='" + location_filter + "' ");
		}
		PreparedStatement ps=connection.prepareStatement(buffer.toString());
		ResultSet rs =ps.executeQuery();
		double total=0;
		int totalqty=0;
		product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
		product.setTotalqty(totalqty);
		product.setTotal_amount(total);
		product.setSalepricetotal(0);
		while(rs.next()){
			product.setSalepricetotal(rs.getDouble(4));
			product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
			product.setTotalqty(rs.getInt(1));
			product.setTotal_amount(rs.getDouble(3));
		}
		
		
	} catch (Exception e) {

		e.printStackTrace();
	}
	
	return product;
}

public Product getOpeingClosingBiferReturnTransfer(String fromDate, String toDate, String searchbyprodname,
		String location_filter) {
	Product product = new Product();
	try {
		StringBuffer buffer= new StringBuffer();
		buffer.append("select sum(inventory_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_transfer_log.qty*(purchaseprice/pack)),sum(inventory_transfer_log.qty*saleprice) from inventory_transfer_log ");
		buffer.append("inner join inventory_parent_transfer_log on inventory_parent_transfer_log.id=inventory_transfer_log.parentid ");
		buffer.append("inner join inventory_product on  inventory_product.id = inventory_transfer_log.old_prodid ");
		buffer.append("where inventory_parent_transfer_log.req_or_trans=2 and inventory_parent_transfer_log.issued_date between '"+fromDate+"' and '"+toDate+"' and inventory_parent_transfer_log.deleted=0 ");
		if (!location_filter.equals("0")) {
			buffer.append("and from_location ='" + location_filter + "' ");
		}
		PreparedStatement ps=connection.prepareStatement(buffer.toString());
		ResultSet rs =ps.executeQuery();
		double total=0;
		int totalqty=0;
		product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
		product.setTotalqty(totalqty);
		product.setTotal_amount(total);
		product.setSalepricetotal(0);
		while(rs.next()){
			product.setSalepricetotal(rs.getDouble(4));
			product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
			product.setTotalqty(rs.getInt(1));
			product.setTotal_amount(rs.getDouble(3));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return product;
}

public Product getOpeingClosingBiferReturnSupplier(String fromDate, String toDate, String searchbyprodname,
		String location_filter) {
	Product product = new Product();
	try {
		toDate = toDate +" "+ "59:59:59";
		StringBuffer buffer= new StringBuffer();
		buffer.append("SELECT sum(inventory_product_return_log.qty),sum(purchaseprice)/sum(pack),sum(inventory_product_return_log.qty*(purchaseprice/pack)),sum(saleprice*inventory_product_return_log.qty) ");
		buffer.append("from inventory_product_return_log ");
		buffer.append("inner join inventory_product on  inventory_product.id = inventory_product_return_log.productid ");
		buffer.append("where status!=0  and datetime between '"+fromDate+"' and '"+toDate+"' and inventory_product_return_log.iscancel=0 ");
		if(!location_filter.equals("0")){
			buffer.append("and inventory_product.location='"+location_filter+"' ");
		}
		PreparedStatement ps=connection.prepareStatement(buffer.toString());
		ResultSet rs =ps.executeQuery();
		double total=0;
		int totalqty=0;
		product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
		product.setTotalqty(totalqty);
		product.setTotal_amount(total);
		product.setSalepricetotal(0);
		while(rs.next()){
			product.setSalepricetotal(rs.getDouble(4));
			product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
			product.setTotalqty(rs.getInt(1));
			product.setTotal_amount(rs.getDouble(3));
		}
		
	} catch (Exception e) {

		e.printStackTrace();
	}
	
	return product;
}

public Product getOpeingClosingBiferConsume(String fromDate, String toDate, String searchbyprodname,
		String location_filter) {
	Product product = new Product();
	try {
		toDate = toDate +" "+ "59:59:59";
		StringBuffer buffer= new StringBuffer();
		buffer.append("select sum(indent_patient_transfer_log.qty),sum(purchaseprice)/sum(pack),sum(indent_patient_transfer_log.qty*(purchaseprice/pack)),sum(indent_patient_transfer_log.qty*saleprice) ");
		buffer.append("from indent_patient_transfer_log ");
		buffer.append("inner join inventory_product on  inventory_product.id = indent_patient_transfer_log.prodid ");
		buffer.append("where  indent_patient_transfer_log.datetime between '"+fromDate+"' and '"+toDate+"' ");
		if(!location_filter.equals("0")){
			buffer.append("and indent_patient_transfer_log.fromlocation='"+location_filter+"' ");
		}
		PreparedStatement ps=connection.prepareStatement(buffer.toString());
		ResultSet rs =ps.executeQuery();
		double total=0;
		int totalqty=0;
		product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
		product.setTotalqty(totalqty);
		product.setTotal_amount(total);
		product.setSalepricetotal(0);
		while(rs.next()){
			product.setSalepricetotal(rs.getDouble(4));
			product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
			product.setTotalqty(rs.getInt(1));
			product.setTotal_amount(rs.getDouble(3));
		}
		
	} catch (Exception e) {

		e.printStackTrace();
	}
	
	return product;
}

public Product getOpeingClosingBiferAdjust(String fromdate, String todate, String searchbyprodname,
		String location_filter) {
	Product product = new Product();
	try {
		todate = todate +" "+ "59:59:59";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select sum(adjustment_data.change_qty),sum(purchaseprice)/sum(pack),sum(adjustment_data.change_qty*(purchaseprice/pack)),sum(adjustment_data.change_qty*saleprice)   ");
		buffer.append("from adjustment_data ");
		buffer.append("inner join inventory_product on inventory_product.id = adjustment_data.product_id ");
		buffer.append("where adjustment_type!=2  and adjustment_data.datetime between '"+fromdate+"' and '"+todate+"' ");
		if(!location_filter.equals("0")){
			buffer.append("and inventory_product.location='"+location_filter+"' ");
		}
		PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
		ResultSet rs = preparedStatement.executeQuery();
		double total=0.0;
		int totalqty=0;
		product.setTotal(DateTimeUtils.changeFormat(total));
		product.setTotalqty(totalqty);
		product.setTotal_amount(total);
		product.setSalepricetotal(0);
		while (rs.next()) {
			product.setSalepricetotal(rs.getDouble(4));
			product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
			product.setTotalqty(rs.getInt(1));
			product.setTotal_amount(rs.getDouble(3));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return product;
}

public ArrayList<Product> getProductListByCatalogueId(String catalogueid, String location, String qty) {
	ArrayList<Product> list = new ArrayList<Product>();
	try {
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Calendar cal = Calendar.getInstance();
		 String todate = dateFormat.format(cal.getTime());   
		String sql = "";
		/*if (!location.equals("0")) {
			sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and location='"
					+ location + "' and catalogueid='"+catalogueid+"' and expiry_date>='"+todate+"' order by prodname asc  ";
		} else {
			sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and catalogueid='"+catalogueid+"' and expiry_date>='"+todate+"' order by prodname asc ";

		}*/
		StringBuffer buffer = new StringBuffer();
		buffer.append("select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, ");
		buffer.append("weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid ");
		buffer.append("from inventory_product ");
		buffer.append("where catalogueid='"+catalogueid+"' and expiry_date>='"+todate+"' ");
		if (!location.equals("0")) {
			buffer.append("and location='"+location+"' ");
		}
		if(qty.equals("0")){
			buffer.append("and stock>0 ");
		}/*else{
			buffer.append("and stock>='"+qty+"'  ");
		}*/
		buffer.append("order by prodname asc ");

		PreparedStatement ps = connection.prepareStatement(buffer.toString());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Product product = new Product();
			product.setId(rs.getInt(1));
			product.setSubcategory_id(rs.getString(2));
			product.setVendor_id(rs.getString(3));
			product.setBrand_id(rs.getString(4));
			product.setProduct_code(rs.getString(5));
			product.setProduct_name(rs.getString(6));
			product.setMrp(rs.getString(7));
			product.setPurchase_price(rs.getString(8));
			product.setSale_price(rs.getString(9));
			product.setPurchase_discount(rs.getString(10));
			product.setSale_discount(rs.getString(11));
			product.setWeight(rs.getString(12));
			product.setUnit(rs.getString(13));
			product.setDescription(rs.getString(14));
			product.setCategory_id(rs.getString(15));
			product.setStock(rs.getString(16));
			product.setExpiry_date(rs.getString(17));
			product.setTax(rs.getString(18));
			product.setUserid(rs.getString(19));
			product.setQty(rs.getString(20));
			product.setLastmodified(rs.getString(21));
			String genericname = rs.getString(22);
			if (genericname == null) {
				genericname = "GEN";
			}
			String medicinenameid = rs.getString(23);
			product.setBatch_no(rs.getString(24));
			product.setHsnno(rs.getString(25));
			if (product.getHsnno() == null) {
				product.setHsnno("");
			}
			product.setMedicinenameid(medicinenameid);
			String pro_code = getCatlogueProductCode(rs.getString(26));
			String expiry= product.getExpiry_date();
			String data ="";
			if(pro_code!=null){
				if(pro_code.equals("")){
					data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}else{
					data = product.getProduct_name() + "- " + genericname + "- " + pro_code + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (Stock: " + product.getStock() + ")  ";
				}
			}else{
				data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
						+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
						+ ") (Stock: " + product.getStock() + ")  ";
			}
			product.setGenericname(data);
			list.add(product);
		
		}

	} catch (Exception e) {

		e.printStackTrace();
	}
	return list;
}

public ArrayList<Product> getadjusmentlist(String fromDate, String toDate,Pagination pagination) {
	ArrayList<Product> arrayList=new ArrayList<Product>();
	try {
		fromDate = fromDate + " 00:59:59";
		toDate = toDate + " 23:59:59";
		
		StringBuilder builder = new StringBuilder();
		builder.append("select adjustment_type, pre_stock, current_stock, change_qty, adjustment_data.userid, datetime, remark,prodname,inventory_product.pack,mrp, purchaseprice, saleprice,inventory_product.catalogueid ");
		builder.append("from adjustment_data ");
		builder.append("inner join inventory_product on inventory_product.id=adjustment_data.product_id ");
		builder.append("where datetime between '"+fromDate+"' and '"+toDate+"' order by  datetime desc");
		String sql=builder.toString();
		if(pagination!=null){
			sql= pagination.getSQLQuery(sql);
		}
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		double totalprice=0.0;
		while (rs.next()) {
			Product product=new Product();
			product.setAdjustment_type(rs.getInt(1));
			product.setPre_stock(rs.getInt(2));
			product.setCurrent_stock(rs.getInt(3));
			product.setChange_qty(rs.getString(4));
			product.setUserid(rs.getString(5));
			String datetime = rs.getString(6);
			String[] data= datetime.split(" ");
			String datetime1 = DateTimeUtils.getCommencingDate1(data[0]);
			String finaldatetime = datetime1+" "+data[1];
			product.setDateTime(finaldatetime);
			product.setRemark(rs.getString(7));
			product.setProd_name(rs.getString(8));
			product.setPack(""+rs.getInt(9));
			product.setMrp(""+rs.getDouble(10));
			product.setPurchase_price(""+rs.getDouble(11));
			product.setSale_price(""+rs.getDouble(12));
			double purpriceqty = (rs.getDouble(11)/rs.getDouble(9))*rs.getInt(4);
			purpriceqty= Math.round(purpriceqty * 100.0) / 100.0;
			product.setPurpriceqty(DateTimeUtils.changeFormat(purpriceqty));
			totalprice = totalprice + purpriceqty;
			product.setTotalpurchaseprice(totalprice);
			Product master = getProductCatalogueDetails(rs.getString(13));
			String name = master.getProduct_name() + " (" + master.getGenericname() + ")";
			product.setProd_name(name);
			arrayList.add(product);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return arrayList;
}

	public int getadjusmentlistcount(String fromDate, String toDate) {
		fromDate = fromDate + " 00:00:00";
		toDate = toDate + " 23:59:59";
		int res = 0;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("select count(*) ");
			builder.append("from adjustment_data ");
			builder.append("inner join inventory_product on inventory_product.id=adjustment_data.product_id ");
			builder.append("where datetime between '" + fromDate + "' and '" + toDate + "' ");
			PreparedStatement preparedStatement = connection.prepareStatement(builder.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				res = rs.getInt(1);

							}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
}

	public ArrayList<Product> getInventoryGSTReport(String fromdate, String todate, String location) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		ProcurementDAO procurementDAO = new JDBCProcurementDAO(connection);
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_procurement.procurementid,inventory_procurement.date,inventory_procurement.vendorid, ");
			buffer.append("inventory_procurement.voucherno,inventory_procurement.voucherdate,name,tinno,inventory_procurement.id ");
			buffer.append("from inventory_parent_procurement ");
			buffer.append("inner join inventory_procurement on inventory_procurement.procurementid = inventory_parent_procurement.id ");
			buffer.append("inner join inventory_vendor on inventory_vendor.id = inventory_procurement.vendorid ");
			buffer.append("inner join inventory_product on inventory_procurement.prodid = inventory_product.id ");
			buffer.append("where inventory_procurement.date between '"+fromdate+"' and '"+todate+"' ");
			buffer.append("and inventory_procurement.deleted=0 and confirm=1 and gudreceipt=1 and  inventory_procurement.iscancel=0 and isdelivermemo=0 ");
			buffer.append("and procurement=0 and (inventory_procurement.prodid!=0 or inventory_procurement.prodid is not null) ");
			if(!location.equals("0")){
				buffer.append("and inventory_procurement.location='"+location+"' ");
			}
			buffer.append("group by inventory_procurement.procurementid ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product  product = new Product();
				boolean isotherstate = checkIgstOrCgstSgst(rs.getString(1),rs.getString(4));
				ArrayList<Product> vatAllocationList = procurementDAO.getVatAllocationList(rs.getString(1),rs.getString(4));
				double zerotaxamt=0;
				double fivetaxamt =0;
				double fivesgst =0;
				double fivecgst =0;
				double fiveigst =0;
				double fivegst=0;
				double twelvetaxamt =0;
				double twelvesgst =0;
				double twelvecgst =0;
				double twelveigst =0;
				double twelvegst=0;
				double eighteentaxamt =0;
				double eighteensgst =0;
				double eighteencgst =0;
				double eighteenigst =0;
				double eighteengst=0;
				double twenteightaxamt =0;
				double twenteightsgst =0;
				double twenteightcgst =0;
				double twenteightigst =0;
				double twenteightgst=0;
				double totalamt=0;
				double totalvat=0;
				double netamt=0;
				for (Product product2 : vatAllocationList) {
					if(product2.getVatratee()==0){
						zerotaxamt = zerotaxamt + product2.getTaxableamt();
					}else if(product2.getVatratee()==5){
						fivetaxamt = fivetaxamt + product2.getTaxableamt();
						if(isotherstate){
							fivegst = product2.getTaxamts();
							fivesgst = fivegst/2;
							fivecgst = fivegst/2;
						}else{
							fivegst = product2.getTaxamts();
							fiveigst = product2.getTaxamts();
						}
					}else if(product2.getVatratee()==12){
						twelvetaxamt = twelvetaxamt + product2.getTaxableamt();
						if(isotherstate){
							twelvegst = product2.getTaxamts();
							twelvesgst = twelvegst/2;
							twelvecgst = twelvegst/2;
						}else{
							twelvegst = product2.getTaxamts();
							twelveigst = product2.getTaxamts();
						}
					}else if(product2.getVatratee()==18){
						eighteentaxamt = eighteentaxamt + product2.getTaxableamt();
						if(isotherstate){
							eighteengst = product2.getTaxamts();
							eighteensgst = eighteengst/2;
							eighteencgst = eighteengst/2;
						}else{
							eighteengst = product2.getTaxamts();
							eighteenigst = product2.getTaxamts();
						}
					}else if(product2.getVatratee()==28){
						twenteightaxamt = twenteightaxamt + product2.getTaxableamt();
						if(isotherstate){
							twenteightgst = product2.getTaxamts();
							twenteightsgst = twenteightgst/2;
							twenteightcgst = twenteightgst/2;
						}else{
							twenteightgst = product2.getTaxamts();
							twenteightigst = product2.getTaxamts();
						}
					}
				}
				totalamt = zerotaxamt + fivetaxamt +  twelvetaxamt+ eighteentaxamt+twenteightaxamt;
				totalvat =  fivegst + twelvegst + eighteengst + twenteightgst;
				totalamt = Math.round(totalamt * 100.0)/100.0;
				totalvat = Math.round(totalvat * 100.0)/100.0;
				netamt = totalamt + totalvat;
				netamt = Math.round(netamt * 100.0)/100.0;
				
				product.setDate(DateTimeUtils.getCommencingDate1(rs.getString(2)));
				product.setVendor(rs.getString(6));
				product.setTinno(rs.getString(7));
				product.setVoucherno(rs.getString(4));

				int res =procurementDAO.getProcurmentSeqNo(rs.getString(1));
			    String proSeqNo="";
			    if(res>0){
			    	proSeqNo=String.valueOf(res);
			    }
			    else{
			    	proSeqNo=rs.getString(1);
			    }
			    product.setProSeqNo(proSeqNo);
				
				product.setZerotaxamt(DateTimeUtils.changeFormat(Math.round(zerotaxamt * 100.0)/100.0));
				
				product.setFivetaxamt(DateTimeUtils.changeFormat(Math.round(fivetaxamt * 100.0)/100.0));
				product.setFivesgst(DateTimeUtils.changeFormat(Math.round(fivesgst*100.0)/100.0));
				product.setFivecgst(DateTimeUtils.changeFormat(Math.round(fivecgst*100.0)/100.0));
				product.setFiveigst(DateTimeUtils.changeFormat(Math.round(fiveigst*100.0)/100.0));
				
				product.setTwelvetaxamt(DateTimeUtils.changeFormat(Math.round(twelvetaxamt * 100.0)/100.0));
				product.setTwelvesgst(DateTimeUtils.changeFormat(Math.round(twelvesgst*100.0)/100.0));
				product.setTwelvecgst(DateTimeUtils.changeFormat(Math.round(twelvecgst*100.0)/100.0));
				product.setTwelveigst(DateTimeUtils.changeFormat(Math.round(twelveigst*100.0)/100.0));
				
				product.setEighteentaxamt(DateTimeUtils.changeFormat(Math.round(eighteentaxamt *100.0)/100.0));
				product.setEighteensgst(DateTimeUtils.changeFormat(Math.round(eighteensgst*100.0)/100.0));
				product.setEighteencgst(DateTimeUtils.changeFormat(Math.round(eighteencgst*100.0)/100.0));
				product.setEighteenigst(DateTimeUtils.changeFormat(Math.round(eighteenigst*100.0)/100.0));
				
				product.setTwenteightaxamt(DateTimeUtils.changeFormat(Math.round(twenteightaxamt *100.0)/100.0));
				product.setTwenteightsgst(DateTimeUtils.changeFormat(Math.round(twenteightsgst*100.0)/100.0));
				product.setTwenteightcgst(DateTimeUtils.changeFormat(Math.round(twenteightcgst*100.0)/100.0));
				product.setTwenteightigst(DateTimeUtils.changeFormat(Math.round(twenteightigst*100.0)/100.0));
				
				product.setTotalamt(DateTimeUtils.changeFormat(totalamt));
				product.setTotalVat(DateTimeUtils.changeFormat(totalvat));
				product.setNetamt(DateTimeUtils.changeFormat(Math.round(netamt)));
				product.setId(rs.getInt(8));
				arrayList.add(product);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	private boolean checkIgstOrCgstSgst(String procurementid, String voucherno) {
		boolean flag =true;
		try {
			String sql ="select igst from inventory_vendor_procurement where procurementid='"+procurementid+"' and voucherno='"+voucherno+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if(rs.getString(1)!=null){
					if(rs.getString(1).equals("") || rs.getString(1).equals("0")){
						flag = true;
					}else{
						flag = false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public ArrayList<Product> getInventoryGSTReturnReport(String fromdate, String todate, String location) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		ProcurementDAO procurementDAO = new JDBCProcurementDAO(connection);
		try {
			todate = todate +" "+"59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_return_grn_parent.id,inventory_return_grn_parent.vendorid,sec_out_no, ");
			buffer.append("inventory_return_grn_parent.datetime,name,tinno,netigst ");
			buffer.append("from inventory_return_grn_parent ");
			buffer.append("inner join inventory_product_return_log on inventory_product_return_log.grnreturnid = inventory_return_grn_parent.id ");
			buffer.append("inner join inventory_vendor on inventory_vendor.id = inventory_return_grn_parent.vendorid ");
			buffer.append("where iscancel=0 and inventory_return_grn_parent.datetime between '"+fromdate+"' and '"+todate+"' ");
			if(!location.equals("0")){
				buffer.append("and inventory_product_return_log.location='"+location+"' ");
			}
			buffer.append("group by inventory_product_return_log.grnreturnid ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product  product = new Product();
				boolean isotherstate = true;
				if(rs.getDouble(6)>0){
					isotherstate =false;
				}
				//ArrayList<Product> vatAllocationList = procurementDAO.getVatAllocationList(rs.getString(1),rs.getString(4));
				ArrayList<Product> vatAllocationList = procurementDAO.getVatAllocationOfReturnPrint(rs.getString(1));
				double zerotaxamt=0;
				double fivetaxamt =0;
				double fivesgst =0;
				double fivecgst =0;
				double fiveigst =0;
				double fivegst=0;
				double twelvetaxamt =0;
				double twelvesgst =0;
				double twelvecgst =0;
				double twelveigst =0;
				double twelvegst=0;
				double eighteentaxamt =0;
				double eighteensgst =0;
				double eighteencgst =0;
				double eighteenigst =0;
				double eighteengst=0;
				double twenteightaxamt =0;
				double twenteightsgst =0;
				double twenteightcgst =0;
				double twenteightigst =0;
				double twenteightgst=0;
				double totalamt=0;
				double totalvat=0;
				double netamt=0;
				for (Product product2 : vatAllocationList) {
					if(product2.getVatratee()==0){
						zerotaxamt = zerotaxamt + product2.getTaxableamt();
					}else if(product2.getVatratee()==5){
						fivetaxamt = fivetaxamt + product2.getTaxableamt();
						if(isotherstate){
							fivegst = product2.getTaxamts();
							fivesgst = fivegst/2;
							fivecgst = fivegst/2;
						}else{
							fivegst = product2.getTaxamts();
							fiveigst = product2.getTaxamts();
						}
					}else if(product2.getVatratee()==12){
						twelvetaxamt = twelvetaxamt + product2.getTaxableamt();
						if(isotherstate){
							twelvegst = product2.getTaxamts();
							twelvesgst = twelvegst/2;
							twelvecgst = twelvegst/2;
						}else{
							twelvegst = product2.getTaxamts();
							twelveigst = product2.getTaxamts();
						}
					}else if(product2.getVatratee()==18){
						eighteentaxamt = eighteentaxamt + product2.getTaxableamt();
						if(isotherstate){
							eighteengst = product2.getTaxamts();
							eighteensgst = eighteengst/2;
							eighteencgst = eighteengst/2;
						}else{
							eighteengst = product2.getTaxamts();
							eighteenigst = product2.getTaxamts();
						}
					}else if(product2.getVatratee()==28){
						twenteightaxamt = twenteightaxamt + product2.getTaxableamt();
						if(isotherstate){
							twenteightgst = product2.getTaxamts();
							twenteightsgst = twenteightgst/2;
							twenteightcgst = twenteightgst/2;
						}else{
							twenteightgst = product2.getTaxamts();
							twenteightigst = product2.getTaxamts();
						}
					}
				}
				totalamt = zerotaxamt + fivetaxamt +  twelvetaxamt+ eighteentaxamt+twenteightaxamt;
				totalvat =  fivegst + twelvegst + eighteengst + twenteightgst;
				totalamt = Math.round(totalamt * 100.0)/100.0;
				totalvat = Math.round(totalvat * 100.0)/100.0;
				netamt = totalamt + totalvat;
				netamt = Math.round(netamt * 100.0)/100.0;
				
				product.setDate(DateTimeUtils.getCommencingDate1(rs.getString(2).split(" ")[0]));
				product.setVendor(rs.getString(5));
				product.setTinno(rs.getString(6));
				product.setVoucherno(rs.getString(3));

			    product.setProSeqNo(rs.getString(1));
				
				product.setZerotaxamt(DateTimeUtils.changeFormat(Math.round(zerotaxamt * 100.0)/100.0));
				
				product.setFivetaxamt(DateTimeUtils.changeFormat(Math.round(fivetaxamt * 100.0)/100.0));
				product.setFivesgst(DateTimeUtils.changeFormat(Math.round(fivesgst*100.0)/100.0));
				product.setFivecgst(DateTimeUtils.changeFormat(Math.round(fivecgst*100.0)/100.0));
				product.setFiveigst(DateTimeUtils.changeFormat(Math.round(fiveigst*100.0)/100.0));
				
				product.setTwelvetaxamt(DateTimeUtils.changeFormat(Math.round(twelvetaxamt * 100.0)/100.0));
				product.setTwelvesgst(DateTimeUtils.changeFormat(Math.round(twelvesgst*100.0)/100.0));
				product.setTwelvecgst(DateTimeUtils.changeFormat(Math.round(twelvecgst*100.0)/100.0));
				product.setTwelveigst(DateTimeUtils.changeFormat(Math.round(twelveigst*100.0)/100.0));
				
				product.setEighteentaxamt(DateTimeUtils.changeFormat(Math.round(eighteentaxamt *100.0)/100.0));
				product.setEighteensgst(DateTimeUtils.changeFormat(Math.round(eighteensgst*100.0)/100.0));
				product.setEighteencgst(DateTimeUtils.changeFormat(Math.round(eighteencgst*100.0)/100.0));
				product.setEighteenigst(DateTimeUtils.changeFormat(Math.round(eighteenigst*100.0)/100.0));
				
				product.setTwenteightaxamt(DateTimeUtils.changeFormat(Math.round(twenteightaxamt *100.0)/100.0));
				product.setTwenteightsgst(DateTimeUtils.changeFormat(Math.round(twenteightsgst*100.0)/100.0));
				product.setTwenteightcgst(DateTimeUtils.changeFormat(Math.round(twenteightcgst*100.0)/100.0));
				product.setTwenteightigst(DateTimeUtils.changeFormat(Math.round(twenteightigst*100.0)/100.0));
				
				product.setTotalamt(DateTimeUtils.changeFormat(totalamt));
				product.setTotalVat(DateTimeUtils.changeFormat(totalvat));
				product.setNetamt(DateTimeUtils.changeFormat(Math.round(netamt)));
				product.setId(rs.getInt(1));
				arrayList.add(product);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Product> getopdtatreportlist(String fromDate, String toDate, Pagination pagination,String diaryuser) {
		ArrayList<Product> arrayList=new ArrayList<Product>();
		try {
			if(diaryuser==null){
				diaryuser="0";
			}
			StringBuilder builder = new StringBuilder();
			builder.append("select whopay,commencing, starttime, endtime,diaryusername,apm_available_slot.clientname,duration,charge,apmttypetext, complete_datetime, patient_arrived_time, patient_being_seen_time,date from apm_available_slot ");
			builder.append("left join apm_invoice on apm_invoice.appointmentid=apm_available_slot.id ");
			builder.append("where commencing between '"+fromDate+"' and '"+toDate+"' ");
			if(!diaryuser.equals("0")){
				builder.append("and diaryuserid='"+diaryuser+"' ");
			}
			builder.append("order by commencing ");
			String sql=builder.toString();
			if(pagination!=null){
				sql= pagination.getSQLQuery(sql);
			}
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product=new Product();
				if(rs.getString(1).equals("Client")){
					product.setWhopay("Self");
				}else{
				product.setWhopay(rs.getString(1));
				}
				product.setCommencing(DateTimeUtils.getCommencingDate1(rs.getString(2)));
				product.setStarttime(rs.getString(3));
				product.setEndtime(rs.getString(4));
				product.setDiaryusername(rs.getString(5));
				product.setClientname(rs.getString(6));
				product.setDuration(rs.getString(7));
				product.setCharge(rs.getString(8));
				product.setApmttypetext(rs.getString(9));
				if(rs.getString(10)!=null){
				String completedatetime=rs.getString(10);
				String[] data= completedatetime.split(" ");
				String datetime1 = DateTimeUtils.getCommencingDate1(data[0]);
				String completemergedatetime = datetime1+" "+data[1];
				product.setComplete_datetime(completemergedatetime);
				}else{
					product.setComplete_datetime("");
				}
				if(rs.getString(11)!=null){
				String arriveddatetime=rs.getString(11);
				String[] data1= arriveddatetime.split(" ");
				String datetime2 = DateTimeUtils.getCommencingDate1(data1[0]);
				String arrivemergedatetime = datetime2+" "+data1[1];
				product.setPatient_arrived_time(arrivemergedatetime);
				}else{
					product.setPatient_arrived_time("");
				}
				String seendatetime=rs.getString(12);
				if(seendatetime!=null){
				
				String[] data2= seendatetime.split(" ");
				String datetime3 = DateTimeUtils.getCommencingDate1(data2[0]);
				String seenmergedatetime = datetime3+" "+data2[1];
				product.setPatient_being_seen_time(seenmergedatetime);
				}else{
					product.setPatient_being_seen_time("");
				}
				product.setDate(DateTimeUtils.isNull(rs.getString(13)));
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int getopdtatteportcount(String fromDate, String toDate,String diaryuser) {
		int res = 0;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("select count(*) ");
			builder.append("from apm_available_slot ");
			builder.append("where commencing between '" + fromDate + "' and '" + toDate + "' ");
			if(!diaryuser.equals("0")){
				builder.append("and diaryuserid='"+diaryuser+"' ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(builder.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				res = rs.getInt(1);

							}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int deletecharges(String chargeid, String ipdid) {
		int result = 0;

		try {

			String sql = "delete from apm_invoice_assesments where id=" +chargeid+ " and ipdid='"+ipdid+"'";
			PreparedStatement ps = connection.prepareStatement(sql);
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public Product getOpeningClosingStock(String toDate, String location_filter, Pagination pagination) {
		Product product = new Product();
		try {
			String sql ="select sum(closing_stock+0), sum(closing_value+0) from inventory_catalogue_log where date='"+toDate+"' and location='"+location_filter+"' ";
			if(pagination!=null){
				sql= pagination.getSQLQuery(sql);
			}
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			product.setTotalclosingstock(0);
			product.setTotalclosingvalue(0);
			if (rs.next()) {
				product.setTotalclosingstock(rs.getInt(1));
				product.setTotalclosingvalue(rs.getDouble(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public ArrayList<Product> getCancelBillReport(String fromdate, String todate, String location,
			Pagination pagination) {
		ArrayList<Product> list = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select apm_medicine_bill.id,apm_medicine_bill.date,debit,apm_medicine_bill.clientid, ");
			buffer.append("apm_medicine_bill.pclientid,apm_medicine_bill.location,isreturn,time, ");
			buffer.append("inventory_product_log.userid,inventory_product_log.date,comment ");
			buffer.append("from apm_medicine_bill ");
			buffer.append("left join inventory_product_log on apm_medicine_bill.id = inventory_product_log.pharmacy_billno ");
			buffer.append("where apm_medicine_bill.date between '" + fromdate + "' and '" + todate + "' ");
			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}
			buffer.append("and apm_medicine_bill.deleted='1' ");
			buffer.append("order by apm_medicine_bill.id desc ");
			String sql1 = pagination.getSQLQuery(buffer.toString());
			PreparedStatement ps = connection.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setDate(rs.getString(2));
				product.setDebit(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3)*100.00)/100.00));
				product.setClientid(rs.getString(4));
				product.setPclientid(rs.getString(5));
				product.setLocation(rs.getString(6));
				product.setIsreturn(rs.getInt(7));
				product.setTime(rs.getString(8));
				String date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				product.setDateTime(date + " " + product.getTime());
				product.setCancel_userid(rs.getString(9));
				product.setCancel_date(DateTimeUtils.getCommencingDate1(rs.getString(10)));
				product.setCancel_notes(rs.getString(11));
				if (product.getIsreturn() > 0) {
					product.setType("Sales Return");
				} else {
					product.setType("Sales");
				}
				if (!product.getPclientid().equals("0")) {

					Priscription priscription = pharmacyDAO.getPharmacyPatient(product.getPclientid());
					product.setClienttype("-");
					product.setTpname("NO");
					product.setDoctor(priscription.getPractitionername());
					product.setClientname(priscription.getFullname());
				} else {
					Client client = clientDAO.getClientDetails(product.getClientid());
					if (client.getTypeName() != null) {
						if (!client.getTypeName().equals("0")) {
							product.setTpname("YES");
						} else {
							product.setTpname("NO");
						}
					} else {
						product.setTpname("NO");
					}
					product.setClientname(client.getTitle() + " " + client.getFirstName() + " " + client.getLastName());
					if (!product.getPriscid().equals("0")) {
						Priscription priscription = emrDAO.getEditPriscription(product.getPriscid());
						UserProfile userProfile = userProfileDAO
								.getUserprofileDetails(Integer.parseInt(priscription.getPrectionerid()));
						product.setDoctor(userProfile.getFullname());
					} else {
						product.setDoctor(" ");
					}
				}
				
				list.add(product);	
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int getCountCancelBillReport(String fromdate, String todate, String location) {
		int res =0;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select count(*) ");
			buffer.append("from apm_medicine_bill ");
			buffer.append("left join inventory_product_log on apm_medicine_bill.id = inventory_product_log.pharmacy_billno ");
			buffer.append("where apm_medicine_bill.date between '" + fromdate + "' and '" + todate + "' ");
			if (!location.equals("0")) {
				buffer.append("and apm_medicine_bill.location=" + location + " ");
			}
			buffer.append("and apm_medicine_bill.deleted='1' ");
			buffer.append("order by apm_medicine_bill.id desc ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				res=rs.getInt(1);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return res;
	}

	public ArrayList<Product> getBinCardReportNew(String location, String catalogueid, String fromdate, String todate,
			String abbrivation, String userwise, String location_filter, String filter_sortby) {
		ArrayList<Product> list= new ArrayList<Product>();
		ProcurementDAO procurementDAO = new JDBCProcurementDAO(connection);
		try {
			todate = todate+" "+"59:59:59";
			//Product pmaster= catalogueDAO.getProductDetails(catalogueid);
			StringBuffer buffer= new StringBuffer();
			//buffer.append("select prodid,date,voucherno,vendorid,qty from inventory_procurement where catalogueid="+catalogueid+" and date between '"+fromdate+"' and '"+todate+"' and confirm=1 and deleted=0 and gudreceipt=1 and iscancel=0  order by id ");
			buffer.append("select source, product_stock_log.location, userid, qty_in_out, productid, catalogueid, ");
			buffer.append("current_qty, previous_qty, change_qty, procurementid, returnsupplierid, pharmacysaleid, ");
			buffer.append("adjustmentid, indentid, consumeid, product_stock_log.datetime,product_name,qty_in_out ");
			buffer.append("from product_stock_log  ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = product_stock_log.catalogueid ");
			buffer.append("where catalogueid='"+catalogueid+"' and product_stock_log.datetime between '"+fromdate+"' and '"+todate+"'  ");
			if(filter_sortby.equals("0")){
				buffer.append("order by productid,product_stock_log.datetime ");
			}else{
				buffer.append("order by product_stock_log.datetime ");
			}
			
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs=ps.executeQuery();
			int balanceqty=0;
			while(rs.next()){
				    Product product=new Product();
				    String prodid= rs.getString(5);
				    int flag = isProcurementDonWithinDate(prodid);
				    if(flag>0){
				    	String date= rs.getString(16);
						String voucherno= "";
						//lokesh various print
						int printid=0;
						String link="";
						if(rs.getInt(10)>0){//procurment
							voucherno = ""+rs.getInt(10);
							int res =procurementDAO.getProcurmentSeqNo(rs.getString(10));
							printid=procurementDAO.getChildprocId(rs.getString(10));
							link="grnprintProcurement?id="+printid;
						    if(res>0){
						    	voucherno=String.valueOf(res);
						    }
						}else if(rs.getInt(11)>0){//spplier retuen
							voucherno = ""+rs.getInt(11);
							printid=rs.getInt(11);
							link="printreturngrnProduct?grnreturnid="+printid+"&status=1";
						}else if(rs.getInt(12)>0){//pharmacysale
							voucherno = ""+rs.getInt(12);
							printid=rs.getInt(12);
							link="viewbillPharmacy?selectedid=0&billno="+printid;
						}else if(rs.getInt(13)>0){//adjustment
							voucherno = ""+rs.getInt(13);
							printid=0;
						}else if(rs.getInt(14)>0){//Indent
							voucherno = ""+rs.getInt(14);
							printid=rs.getInt(14);
							if(rs.getString(1).equals("Indent In")||rs.getString(1).equals("Indent Out")){
								link="deliverPrintProduct?id="+printid+"&status=2";
							}else{
								link="deliverPrintDirectProduct?id="+printid;
							}
							
						}else if(rs.getInt(15)>0){//Consume
							voucherno = ""+rs.getInt(15);
							printid=0;
						}
						product.setPrintId(link);
						product.setProduct_id(prodid);
						product.setDate(date);
						product.setDocno(voucherno);
						product.setTranstype(DateTimeUtils.isNull(rs.getString(1)));
						
						product.setUserid(rs.getString(3));
						product.setCurrent_qty(""+rs.getInt(7));
						product.setPrevious_qty(""+rs.getInt(8));
						product.setChange_qty(""+rs.getInt(9));
						product.setProd_name(rs.getString(17));
						String source = rs.getString(1);
						boolean flagg = false;
						if(source.equals("Update Procurement")){
							int previous_qty = rs.getInt(8);
							int current_qty = rs.getInt(7);
							if(previous_qty<current_qty){
								product.setQtyinout_status("In");
								balanceqty = balanceqty+rs.getInt(9);
							}else if(previous_qty>current_qty){
								product.setQtyinout_status("Out");
								balanceqty = balanceqty-rs.getInt(9);
							}else{
								flagg = true;
							}
						}else{
							if(rs.getInt(18)==0){
								product.setQtyinout_status("In");
								balanceqty = balanceqty+rs.getInt(9);
							}else{
								product.setQtyinout_status("Out");
								balanceqty = balanceqty-rs.getInt(9);
							}
						}
						
						product.setBalanceqty(""+balanceqty);
						
						if(rs.getInt(14)>0){
							if(rs.getString(1).equals("Indent In")||rs.getString(1).equals("Indent Out")){
								String handover = getHandOverUserName(rs.getString(14));
								product.setUserid(handover);
							}else{
								product.setUserid(rs.getString(3));
							}
							
						}
						String datrr   = date.split(" ")[0];
						int openingstock = lastopeningstock(datrr,prodid);
						product.setOpeningstock(""+openingstock+"("+datrr+")");
						if(!flagg){
							list.add(product);
						}
						
				    }
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}

	private int lastopeningstock(String date, String prodid) {
		int res = 0;
		try {
			String newdate = date+" "+"59:59:59";
			String sql ="select current_qty from product_stock_log where datetime not between '"+date+"' and '"+newdate+"' and datetime<'"+date+"' and productid="+prodid+" order by id desc limit 1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				res =rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private String getHandOverUserName(String voucherno) {
		String handoverto = "";
		try {
			String sql ="select handover_to from inventory_parent_transfer_log where id='"+voucherno+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				handoverto = rs.getString(1);
				if(handoverto!=null){
			        String[] data = handoverto.split("]");
			        handoverto = data[0];
			    }else{
			        handoverto="";
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handoverto;
	}

	private int isProcurementDonWithinDate(String prodid) {
		int res =0;
		try {
			String sql ="select * from inventory_procurement where prodid='"+prodid+"' and date>='2019-04-01'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				res=1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getProdCurrentStock(String prodid) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "select stock from inventory_product where id = " + prodid + " ";

		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getProductIdFromGlobalID(String pid, String location) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "select id from inventory_product where global_prodid = " + pid + " and location='"+location+"' and stock>0 ";
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateGlobalProductId(int pid, int globalprod) {
		int res =0;
		try {
			String sql ="update inventory_product set global_prodid='"+globalprod+"' where id='"+pid+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			res = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public ArrayList<Master> getGenericMasterList() {
		ArrayList<Master> arrayList = new ArrayList<Master>();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select id,name from generic_name");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				arrayList.add(master);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Master> getMFGMasterList() {
		ArrayList<Master> arrayList = new ArrayList<Master>();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select id,mfg_name from mfg_details");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				arrayList.add(master);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int getStockAvailableNew(String catalogueid, String location) {
		int res=0;
		try {
			
			StringBuffer buffer= new StringBuffer();
			buffer.append("select sum(inventory_product.stock) from inventory_product inner join ");
			buffer.append("inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
			buffer.append("where inventory_product.catalogueid="+catalogueid+" and inventory_product.mrp!=0  ");
			if(location!=null){
				if(!location.equals("0")){
					buffer.append("and inventory_product.location='"+location+"' ");
				}
			}
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				 res= rs.getInt(1);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		} 
		return res;
	}

	public boolean checkInventoryProductLog(String todaydate, String string) {
		boolean flag = false;
		try {
			String sql="select * from inventory_stock_log where date='"+todaydate+"' and location='"+string+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public int saveInventoryStockLog(String todaydate, String string) {
		int res =0;
		try {
			String sql ="select id,stock,purchaseprice from inventory_product where location='"+string+"' and procurement=0 ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
			    String sql1 = "insert into inventory_stock_log (productid,qty,purchase_price,date,location) values (?,?,?,?,?)";
				PreparedStatement ps = connection.prepareStatement(sql1);
				ps.setString(1, ""+rs.getInt(1));
				ps.setString(2, ""+rs.getInt(2));
				ps.setString(3, ""+rs.getDouble(3));
				ps.setString(4, todaydate);
				ps.setString(5, string);
				res = ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public boolean checkInventoryOpeningStockLog(String todates, String string) {
		boolean flag = false;
		try {
			String sql="select * from inventory_catalogue_log where date='"+todates+"' and location='"+string+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean checkProductInProductLog(String todates, String catalogueid, String fromDate) {
		boolean flag = false;
		try {
			String enddate = todates+" "+"59:59:59";
			//String sql="select * from product_stock_log where date='"+todates+"' and location='"+string+"'";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select product_stock_log.id from product_stock_log ");
			buffer.append("inner join inventory_product on inventory_product.id=product_stock_log.productid ");
			buffer.append("where inventory_product.catalogueid='"+catalogueid+"' and product_stock_log.datetime between '"+fromDate+"' and '"+enddate+"' limit 1 ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public Product getCatalogueProductIn(String todates, String catalogueid, String string, String fromdate) {
		Product product= new Product();
		try {
			String toDate = todates +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("select sum(product_stock_log.change_qty),sum(product_stock_log.change_qty*(purchaseprice/pack)) from product_stock_log ");
			buffer.append("inner join inventory_product on product_stock_log.productid = inventory_product.id ");
			buffer.append("where qty_in_out=0 and product_stock_log.datetime between '"+fromdate+"' and '"+toDate+"' and source!='Update Procurement' ");
			if(!string.equals("0")){
				buffer.append("and inventory_product.location='"+string+"' ");
			}
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid="+catalogueid+" ");
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
		
	}

	public Product getCatalogueProductUpdateProIn(String todates, String catalogueid, String string, String fromdate) {
		Product product= new Product();
		try {
			String toDate = todates +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(product_stock_log.change_qty),sum(product_stock_log.change_qty*(purchaseprice/pack)) from product_stock_log ");
			buffer.append("inner join inventory_product on product_stock_log.productid = inventory_product.id ");
			buffer.append("where product_stock_log.datetime between '"+fromdate+"' and '"+toDate+"' and source='Update Procurement' ");
			buffer.append("and (change_qty+0)>0 and (previous_qty+0)<(current_qty+0) ");
			if(!string.equals("0")){
				buffer.append("and inventory_product.location='"+string+"' ");
			}
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid="+catalogueid+" ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			while (rs.next()) {
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public Product getCatalogueProductOut(String todates, String catalogueid, String string, String fromdate, int addpharmacy) {
		Product product= new Product();
		try {
			String toDate = todates +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("select sum(product_stock_log.change_qty),sum(product_stock_log.change_qty*(purchaseprice/pack)),sum(product_stock_log.change_qty*saleprice) from product_stock_log ");
			buffer.append("inner join inventory_product on product_stock_log.productid = inventory_product.id ");
			buffer.append("where qty_in_out=1 and product_stock_log.datetime between '"+fromdate+"' and '"+toDate+"' and source!='Update Procurement'   ");
			if(addpharmacy==1){
				buffer.append("and source!='Pharmacy Sale' ");
			}
			if(!string.equals("0")){
				buffer.append("and inventory_product.location='"+string+"' ");
			}
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid="+catalogueid+" ");
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalevalue(0);
			while (rs.next()) {
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(2));
				product.setSalevalue(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public Product getCatalogueProductUpdateProOut(String todates, String catalogueid, String string, String fromdate) {
		Product product= new Product();
		try {
			String toDate = todates +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(product_stock_log.change_qty),sum(product_stock_log.change_qty*(purchaseprice/pack)),sum(product_stock_log.change_qty*saleprice) from product_stock_log ");
			buffer.append("inner join inventory_product on product_stock_log.productid = inventory_product.id ");
			buffer.append("where product_stock_log.datetime between '"+fromdate+"' and '"+toDate+"' and source='Update Procurement' ");
			buffer.append("and (change_qty+0)>0 and (previous_qty+0)>(current_qty+0) ");
			if(!string.equals("0")){
				buffer.append("and inventory_product.location='"+string+"' ");
			}
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid="+catalogueid+" ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalevalue(0);
			while (rs.next()) {
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(2));
				product.setSalevalue(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public Product getCatalogueProductSaleOut(String todates, String catalogueid, String string, String fromdate) {
		Product product= new Product();
		try {
			String toDate = todates +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(product_stock_log.change_qty),sum(product_stock_log.change_qty*(purchaseprice/pack)),sum(product_stock_log.change_qty*charge) from product_stock_log ");
			buffer.append("inner join inventory_product on product_stock_log.productid = inventory_product.id ");
			buffer.append("inner join apm_medicine_charges on product_stock_log.pharmacysaleid = apm_medicine_charges.invoiceid ");
			buffer.append("where qty_in_out=1 and product_stock_log.datetime between '"+fromdate+"' and '"+toDate+"' and source='Pharmacy Sale' ");
			if(!string.equals("0")){
				buffer.append("and inventory_product.location='"+string+"' ");
			}
			if(catalogueid!=null){
				buffer.append("and inventory_product.catalogueid="+catalogueid+" ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			product.setSalevalue(0);
			while (rs.next()) {
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(2));
				product.setSalevalue(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public int changePayee(String chargeid, String val,String tpid) {
		int res=0;
		String sql="";
		PreparedStatement ps=null;
		try {
			if(val.equals("0")){
			 sql="update apm_invoice_assesments set paybuy='"+val+"' where id="+chargeid+"";
			}else{
				 sql="update apm_invoice_assesments set paybuy='"+val+"',thirdPartyId='"+tpid+"' where id="+chargeid+"";
			}
			ps=connection.prepareStatement(sql);
			res=ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
		
	}

	public ArrayList<Product> geProductListFast(String string, String location) {


		ArrayList<Product> list = new ArrayList<Product>();
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			 Calendar cal = Calendar.getInstance();
			 String todate = dateFormat.format(cal.getTime());   
			String sql = "";
		//StringBuffer buffer = new StringBuffer();
			if (!location.equals("0")) {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and location='"
						+ location + "' and expiry_date>'"+todate+"' and pack>0 order by prodname asc   ";
			} else {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and pack>0 and expiry_date>'"+todate+"' order by prodname asc ";

			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setStock(rs.getString(16));
				String genericname = rs.getString(22);
				if (genericname == null) {
					genericname = "GEN";
				}
				String medicinenameid = rs.getString(23);
				product.setBatch_no(rs.getString(24));
				product.setHsnno(rs.getString(25));
				if (product.getHsnno() == null) {
					product.setHsnno("");
				}
				product.setMedicinenameid(medicinenameid);
				
				String pro_code = getCatlogueProductCode(rs.getString(26));
				 product.setExpiry_date(rs.getString(17));
				String expiry= product.getExpiry_date();
				
				
				String data ="";
				if(pro_code!=null){
					if(pro_code.equals("")){
						data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
								+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
								+ ") (" + product.getStock() + ")  ";
					}else{
						data = product.getProduct_name() + "- " + genericname + "- " + pro_code + " - (" + product.getBatch_no() + "/"
								+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
								+ ") (" + product.getStock() + ")  ";
					}
				}else{
					data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}
				product.setGenericname(data);
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	
	}

	public ArrayList<Product> getExpiryMedicinePopup(String fromdate, String dt, String location) {
		ArrayList<Product> list = new ArrayList<Product>();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			String nowdate = dateFormat.format(calendar.getTime());
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_product.id,inventory_product.prodname,inventory_product.mrp,inventory_product.purchaseprice, ");
			buffer.append("inventory_product.saleprice,inventory_product.stock,inventory_product.expiry_date,inventory_product.supplierid, ");
			buffer.append("inventory_product.genericname,inventory_product.pack,inventory_product.location from inventory_product ");
			buffer.append("where inventory_product.expiry_date between '" + fromdate + "' and '" + dt + "' and inventory_product.stock>0 ");
			if (!location.equals("0")) {
				buffer.append("and inventory_product.location='" + location + "' ");
			}
			buffer.append("order by inventory_product.expiry_date asc ");
			String sql =buffer.toString();
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				product.setMrp(rs.getString(3));
				product.setPurchase_price(rs.getString(4));
				product.setSale_price(rs.getString(5));
				product.setStock(rs.getString(6));
				String expiry = rs.getString(7);
				if (product.getPurchase_price() == null) {
					product.setPurchase_price("0");
				}
				if (product.getStock() == null) {
					product.setStock("0");
				}
				int pack=1;
				if(rs.getInt(10)!=0){
					pack = rs.getInt(10);
				}
				double total = (rs.getDouble(4)/pack) * rs.getDouble(6);
				if (expiry == null) {
					expiry = nowdate;
				}
				product.setExpiry_date(DateTimeUtils.getCommencingDate1(expiry));
				product.setTotal(DateTimeUtils.changeFormat(total));

				/*Vendor vendor = inventoryVendorDAO.getVendor(rs.getString(8));
				product.setVendor(vendor.getName());
				product.setContact(vendor.getMobile_pri());*/
				
				product.setGenericname(rs.getString(9));
				//product.setVoucherno(rs.getString(10));

				long diffdays = DateTimeUtils.getDifferenceOfTwoDateDBFormat(nowdate, expiry);
				product.setDays("" + Math.abs(diffdays) + " Days");
				String locationame = pharmacyDAO.getPharmacyLocation(rs.getString(11));
				product.setLocationName(locationame);
				product.setStatus("3");
				list.add(product);			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Master> getWareHouseListRequest() {
		ArrayList<Master> list = new ArrayList<Master>();
		try {
			String sql = "select id,name from inventory_warehouse where isindent=1 ";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				list.add(master);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	public int saveEditProcurementLog(String datetime, String grnupdateuserid, String grn_pre_pack, String grn_new_pack,
		String grn_pre_freeqty, String grn_new_freeqty, String grn_pre_stock, String grn_new_stock,
		String grn_pre_qty, String grn_new_qty, String grn_procurementid, String grn_child_procid) {
		int res =0;
		try {
			StringBuffer buffer = new  StringBuffer();
			buffer.append("insert into procurement_edit_log (datetime, grnupdateuserid, grn_pre_pack, grn_new_pack, grn_pre_freeqty, grn_new_freeqty, grn_pre_stock, grn_new_stock, grn_pre_qty, grn_new_qty, grn_procurementid, grn_child_procid) values (?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			preparedStatement.setString(1, datetime);
			preparedStatement.setString(2, grnupdateuserid);
			preparedStatement.setString(3, grn_pre_pack);
			preparedStatement.setString(4, grn_new_pack);
			preparedStatement.setString(5, grn_pre_freeqty);
			preparedStatement.setString(6, grn_new_freeqty);
			preparedStatement.setString(7, grn_pre_stock);
			preparedStatement.setString(8, grn_new_stock);
			preparedStatement.setString(9, grn_pre_qty);
			preparedStatement.setString(10, grn_new_qty);
			preparedStatement.setString(11, grn_procurementid);
			preparedStatement.setString(12, grn_child_procid);
			res =preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int getProductIdFromBarcode(String pid, String location) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "select id from inventory_product where barcode = '" + pid + "' and location='"+location+"' and stock>0 ";
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Product> getEditGRNLogList(String fromdate, String todate, String location, String serchtext) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			todate = todate +"59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select procurement_edit_log.datetime, grnupdateuserid, grn_pre_pack, grn_new_pack, ");
			buffer.append("grn_pre_freeqty, grn_new_freeqty, grn_pre_stock, grn_new_stock,grn_pre_qty, grn_new_qty,prodname,apm_condition.name, ");
			buffer.append("inventory_catalogue.genericname,inventory_subcategory.name,inventory_category.name ");
			buffer.append("from procurement_edit_log ");
			buffer.append("inner join inventory_procurement on inventory_procurement.id =procurement_edit_log.grn_child_procid ");
			buffer.append("inner join inventory_product on inventory_procurement.prodid = inventory_product.id ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
			buffer.append("left join procurement_seqno on procurement_seqno.seq_procid =inventory_procurement.procurementid ");
			buffer.append("left join apm_condition on apm_condition.id = inventory_procurement.location ");
			buffer.append("left join inventory_category on inventory_category.id=inventory_catalogue.categoryid ");
			buffer.append("left join inventory_subcategory on inventory_subcategory.id=inventory_catalogue.subcategoryid ");
			buffer.append("where procurement_edit_log.datetime between '"+fromdate+"' and '"+todate+"' ");
			if(!location.equals("0")){
				buffer.append("and inventory_procurement.location='"+location+"' ");
			}
			if(serchtext!=null){
				buffer.append("and(inventory_procurement.grnno='"+serchtext+"' or sequenceno='"+serchtext+"' or  inventory_product.prodname like '"+serchtext+"%' ");
				buffer.append("or inventory_category.name like '"+serchtext+"%' or inventory_subcategory.name like '"+serchtext+"%')");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				String date = DateTimeUtils.getCommencingDate1(rs.getString(1).split(" ")[0]);
				product.setDateTime(date+" "+rs.getString(1).split(" ")[1]);
				product.setUserid(rs.getString(2));
				product.setGrn_pre_pack(rs.getString(3));
				product.setGrn_new_pack(rs.getString(4));
				product.setGrn_pre_freeqty(rs.getString(5));
				product.setGrn_new_freeqty(rs.getString(6));
				product.setGrn_pre_stock(rs.getString(7));
				product.setGrn_new_stock(rs.getString(8));
				product.setGrn_pre_qty(rs.getString(9));
				product.setGrn_new_qty(rs.getString(10));
				product.setProduct_name(rs.getString(11));
				product.setLocationName(rs.getString(12));
				product.setGenericname(rs.getString(13));
				product.setSubcategory(rs.getString(14));
				product.setCategory(rs.getString(15));
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public Product getManualStockEntry(String fromDate, String todates, String catalogueid, String location_filter) {


		Product product= new Product();
		try {
			todates = todates +" "+ "59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(inventory_product.manual_stock+0),sum(purchaseprice)/sum(pack),sum(inventory_product.manual_stock*(purchaseprice/pack))   ");
			buffer.append("from inventory_product ");
			buffer.append("where manual_date between '"+fromDate+"' and '"+todates+"'  and catalogueid='"+catalogueid+"' and manual_stock>0  ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double total=0;
			int totalqty=0;
			product.setTotal(DateTimeUtils.changeFormat(Math.round(total * 100.0)/100.0));
			product.setTotalqty(totalqty);
			product.setTotal_amount(total);
			
			while (rs.next()) {
				product.setTotal(DateTimeUtils.changeFormat(Math.round(rs.getDouble(3) * 100.0)/100.0));
				product.setTotalqty(rs.getInt(1));
				product.setTotal_amount(rs.getDouble(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	
	
	}

	public int getCountOfOpeningClosingCatalogue(String fromDate, String toDate, String searchbyprodname,
			String category_id, String location_filter) {
		int res =0;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_catalogue.id from inventory_catalogue ");
			buffer.append("inner join inventory_product on inventory_product.catalogueid = inventory_catalogue.id  ");
			buffer.append("where procurement=0 and product_name is not null and inventory_product.location='"+location_filter+"'  ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			if (searchbyprodname != null) {
				buffer.append("and (inventory_catalogue.product_name like ('%" + searchbyprodname + "%')  )  ");
			}
			buffer.append("group by inventory_catalogue.id order by inventory_catalogue.id  ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.last()) {
				res = rs.getRow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public ArrayList<Product> getListOfOpeningClosingCatalogue(String fromDate, String todates, Pagination pagination,
			String searchbyprodname, String category_id, String location_filter) {
		ArrayList<Product> arrayList = new ArrayList<Product>();
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_catalogue.id,inventory_catalogue.product_name,product_code from inventory_catalogue ");
			buffer.append("inner join inventory_product on inventory_product.catalogueid = inventory_catalogue.id  ");
			buffer.append("where procurement=0 and product_name is not null and inventory_product.location='"+location_filter+"'  ");
			if(!location_filter.equals("0")){
				buffer.append("and inventory_product.location='"+location_filter+"' ");
			}
			//buffer.append("and inventory_catalogue.id=42 ");
			if (searchbyprodname != null) {
				buffer.append("and (inventory_catalogue.product_name like ('%" + searchbyprodname + "%')  )  ");
			}
			buffer.append("group by inventory_catalogue.id order by inventory_catalogue.id  ");
			String sql =buffer.toString();
			if(pagination!=null){
				sql= pagination.getSQLQuery(sql);
			}
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int totalopeningstock =0;
			double totalopeingstockvalue =0;
			int totalqtyin =0;
			double totalqtyinvalue=0;
			int totalqtyout=0;
			double totalstockvalue=0;
			double totalsaleprice=0;
			int totalclosingstock=0;
			double totalclosingvalue=0;
			int totalunknownqty=0;
			double totalunknownvalue=0;
			
			while (rs.next()) {
				String catalogueid = rs.getString(1);
				double opeingstockvalue =0;
				int openingstock=0;
				double openingvalue =0;
				int qtyin=0;
				double qtyinvalue =0;
				int qtyout=0;
				double qtyoutvalue =0;
				double salevalue = 0;
				int closingstock=0;
				double closingvalue =0;
				
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				Date d=sdf1.parse(fromDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.add(Calendar.DATE, -1);
				String previousdate=sdf1.format(cal.getTime());
				
				Product product2 = getDayBeforeStockLog(previousdate,catalogueid,location_filter);
				opeingstockvalue = product2.getTotalclosingvalue();
				openingstock = product2.getTotalclosingstock();
				openingvalue = product2.getClosing();
				
				boolean flagss = checkProductInProductLog(todates,catalogueid,fromDate);
				if(flagss){
					Product qtyin_product = getCatalogueProductIn(todates,catalogueid,location_filter,fromDate);
					Product qtyin_product_update_pro =getCatalogueProductUpdateProIn(todates,catalogueid,location_filter,fromDate); 
					
					qtyin = qtyin_product.getTotalqty() + qtyin_product_update_pro.getTotalqty();
					qtyinvalue = qtyin_product.getTotal_amount() + qtyin_product_update_pro.getTotal_amount();
					
					Product qtyout_product = getCatalogueProductOut(todates,catalogueid,location_filter,fromDate,0);
					Product qtyout_product_update_pro = getCatalogueProductUpdateProOut(todates,catalogueid,location_filter,fromDate);
					//Product qtyout_product_sale = getCatalogueProductSaleOut(todates,catalogueid,location_filter,fromDate);
					
					qtyout = qtyout_product.getTotalqty() + qtyout_product_update_pro.getTotalqty()/*+qtyout_product_sale.getTotalqty()*/ ;
					qtyoutvalue = qtyout_product.getTotal_amount() + qtyout_product_update_pro.getTotal_amount()/*+qtyout_product_sale.getTotal_amount()*/; 
					salevalue = qtyout_product.getSalevalue() + qtyout_product_update_pro.getSalevalue()/*+qtyout_product_sale.getSalevalue()*/; 
				}
			/*	Product manualin = getManualStockEntry(fromDate,todates,catalogueid,location_filter);
				qtyin = qtyin + manualin.getTotalqty();
				qtyinvalue = qtyinvalue + manualin.getTotal_amount();*/
				closingstock=openingstock+qtyin-qtyout;
				closingvalue=openingvalue+qtyinvalue-qtyoutvalue;
				double unknownvalue =0;
				int unknownqty = 0;
				if((openingstock +qtyin)<qtyout){
					closingstock =0;
					closingvalue =0;
					unknownqty = qtyout - (openingstock +qtyin);
					unknownvalue = qtyoutvalue - (opeingstockvalue + qtyinvalue);
				}
				unknownvalue = Math.abs(unknownvalue);
				//Set up data
				Product product = new Product();
				Product masterdata = getProductDataByCatalogueid(catalogueid);
				product.setProduct_name(rs.getString(2));
				product.setPro_code(rs.getString(3));
				product.setCategory(masterdata.getCategory());
				product.setMfg(masterdata.getMfg());
				double tempclsoingvalue= closingvalue;
				
				if(closingvalue<0){
					closingvalue =0;
					tempclsoingvalue=0;
				}
				product.setUnknownqty(unknownqty);
				product.setUnknownvalue(Math.round(unknownvalue * 100.0)/100.0);
				product.setOpeningstock(""+openingstock);
				product.setOpeningstockvalue(Math.round(opeingstockvalue * 100.0)/100.0);
				product.setPurchaseqty(qtyin);
				product.setSale(""+qtyout);
				product.setSalevalue(Math.round(qtyoutvalue * 100.0)/100.0);
				product.setClosingstock(""+closingstock);
				product.setSv(DateTimeUtils.changeFormat(Math.round(closingvalue * 100.0)/100.0));
				product.setQtyinvalue(DateTimeUtils.changeFormat(qtyinvalue));
				product.setSale_price(DateTimeUtils.changeFormat(Math.round(salevalue * 100.0)/100.0));
				totalunknownqty = totalunknownqty + unknownqty;
				totalopeningstock =totalopeningstock + openingstock;
				totalopeingstockvalue =totalopeingstockvalue + opeingstockvalue;
				totalqtyin =totalqtyin + qtyin;
				totalqtyinvalue=qtyinvalue + totalqtyinvalue;
				totalqtyout = totalqtyout+qtyout;
				totalstockvalue = totalstockvalue+qtyoutvalue;
				totalsaleprice = totalsaleprice + salevalue;
				totalclosingstock = totalclosingstock + closingstock;
				totalclosingvalue = totalclosingvalue + tempclsoingvalue;
				totalunknownvalue = totalunknownvalue + unknownvalue;
				product.setTotalopeningstock(totalopeningstock);
				product.setTotalopeingstockvalue(totalopeingstockvalue);
				product.setTotalqtyin(totalqtyin);
				product.setTotalqtyinvalue(totalqtyinvalue);
				product.setTotalqtyout(totalqtyout);
				product.setTotalstockvalue(totalstockvalue);
				product.setTotalssaleprice(totalsaleprice);
				product.setTotalclosingstock(totalclosingstock);
				product.setTotalclosingvalue(totalclosingvalue);
				product.setTotalunknownqty(totalunknownqty);
				product.setTotalunknownvalue(totalunknownvalue);
				product.setCatalogueid(catalogueid);
				product.setFromdate(fromDate);
				product.setTodate(todates);
				arrayList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	private Product getDayBeforeStockLog(String previousdate, String catalogueid, String location_filter) {
		Product product = new Product();
		product.setTotalclosingstock(0);
		product.setTotalclosingvalue(0);
		product.setClosing(0);
		try {
			int qtyin=0;
			double qtyinvalue =0;
			int qtyout=0;
			double qtyoutvalue =0;
			int closingstock=0;
			double closingvalue =0;
			String fromDate = getFirstDateofStockLog(catalogueid);
			boolean flagss = checkProductInProductLog(previousdate,catalogueid,fromDate);
			if(flagss){
				Product qtyin_product = getCatalogueProductIn(previousdate,catalogueid,location_filter,fromDate);
				Product qtyin_product_update_pro =getCatalogueProductUpdateProIn(previousdate,catalogueid,location_filter,fromDate); 
				
				qtyin = qtyin_product.getTotalqty() + qtyin_product_update_pro.getTotalqty();
				qtyinvalue = qtyin_product.getTotal_amount() + qtyin_product_update_pro.getTotal_amount();
				
				Product qtyout_product = getCatalogueProductOut(previousdate,catalogueid,location_filter,fromDate,0);
				Product qtyout_product_update_pro = getCatalogueProductUpdateProOut(previousdate,catalogueid,location_filter,fromDate);
				//Product qtyout_product_sale = getCatalogueProductSaleOut(previousdate,catalogueid,location_filter,fromDate);
				
				qtyout = qtyout_product.getTotalqty() + qtyout_product_update_pro.getTotalqty()/*+qtyout_product_sale.getTotalqty()*/ ;
				qtyoutvalue = qtyout_product.getTotal_amount() + qtyout_product_update_pro.getTotal_amount()/*+qtyout_product_sale.getTotal_amount()*/; 
				//salevalue = qtyout_product.getSalevalue() + qtyout_product_update_pro.getSalevalue()+qtyout_product_sale.getSalevalue(); 
			}
			closingstock=qtyin-qtyout;
			closingvalue=qtyinvalue-qtyoutvalue;
			if(qtyin<qtyout){
				closingstock =0;
				closingvalue =0;
			}
			product.setTotalclosingstock(closingstock);
			product.setTotalclosingvalue(closingvalue);
			product.setClosing(closingvalue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	private String getFirstDateofStockLog(String catalogueid) {
		String date="";
		try {
			String sql="select datetime from product_stock_log where catalogueid='"+catalogueid+"' order by datetime asc limit 1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs  = preparedStatement.executeQuery();
			while (rs.next()) {
				date =rs.getString(1);
				date = DateTimeUtils.isNull(date);
				date = date.split(" ")[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public ArrayList<Product> getDeptMaterialSummaryList(String fromDate, String toDate, String department,
			String warehouseid, String category) {

		ArrayList<Product> materialissuereport = new ArrayList<Product>();
		try {
			String productname ="";
			ArrayList<Product> directdepartnamelist = getDirectMaterialIssueDept(fromDate,toDate,productname,department,warehouseid,category);
			ArrayList<Product> requestdepartnamelist = getRequestMaterialIssueDept(fromDate,toDate,productname,department,warehouseid,category);
			ArrayList<Product> arrayList = new ArrayList<Product>();
			arrayList.addAll(directdepartnamelist);
			arrayList.addAll(requestdepartnamelist);
			
			ArrayList<Product> arrayList2 = new ArrayList<Product>();
			int i=0;
			for (Product product : arrayList) {
					if(i==0){
						arrayList2.add(product);
					}
					if(i!=0){
						boolean flag = false;
						for (Product product1 : arrayList2) {
							if(product1.getName().equals(product.getName())){
								flag= true;
								break;
							}
						}
						if(!flag){
							arrayList2.add(product);
						}
					}
				i++;
			}
			
			for (Product string : arrayList2) {
				Product product = new Product();
				product.setLocationName(string.getName());
				Product directmaterialdata = getDirectDeptMaterialSummary(fromDate, toDate, productname, string.getTo_location(),string.getWarehouse_id(),category,string.getName());
				Product requestmaterialdata = getRequestDeptMaterialSummary(fromDate, toDate, productname, string.getTo_location(),string.getWarehouse_id(),category,string.getName());
				int ttlqty = requestmaterialdata.getReq_qty() + directmaterialdata.getReq_qty();
				double ttlamt =requestmaterialdata.getTotalAmt() + directmaterialdata.getTotalAmt();
				product.setTotal("");
				product.setTotalamt("");
				String fromdate1= fromDate.split("-")[0] +"-"+fromDate.split("-")[1];
				String todate1= toDate.split("-")[0] +"-"+toDate.split("-")[1];
				String frommonth="";
				String tomonth="";
				if(fromdate1.equals(todate1)){
					frommonth = DateTimeUtils.getMonthName(Integer.parseInt(fromDate.split("-")[1]))+"-"+fromDate.split("-")[0];
				}else{
					frommonth = DateTimeUtils.getMonthName(Integer.parseInt(fromDate.split("-")[1]))+"-"+fromDate.split("-")[0];
					tomonth = DateTimeUtils.getMonthName(Integer.parseInt(todate1.split("-")[1]))+"-"+todate1.split("-")[0];
				}
				product.setFrommonth(frommonth);
				product.setTomonth(tomonth);
				product.setQty(""+ttlqty);
				product.setTotalamt(String.valueOf(Math.round(ttlamt * 100.0) / 100.0));
				Product category1 = getCategory(category);
				product.setCategory(category1.getName());
				materialissuereport.add(product);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return materialissuereport;
	
	}

	private Product getDirectDeptMaterialSummary(String fromDate, String toDate, String productname,
			String department, String to_location, String category, String locationname) {
		Product product = new Product();
		try {
			toDate = toDate+" "+"59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(inventory_transfer_log.qty),sum(inventory_transfer_log.qty * (purchaseprice/inventory_product.pack)) ");
			buffer.append("from inventory_parent_transfer_log ");
			buffer.append("inner join inventory_transfer_log on inventory_transfer_log.parentid = inventory_parent_transfer_log.id ");
			buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.to_location ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_transfer_log.catlogueid ");
			buffer.append("inner join inventory_product on inventory_product.id = inventory_transfer_log.new_prodid ");
			buffer.append("where request_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='1' and from_location='"+to_location+"' and name is not null ");
			if(!productname.equals("")){
				buffer.append(" and inventory_catalogue.product_name like '"+productname+"%' ");
			}
			if(!category.equals("0")){
				buffer.append("and inventory_catalogue.categoryid='"+category+"' ");
			}
			buffer.append(" and inventory_parent_transfer_log.to_location='"+department+"' ");
			buffer.append("group by inventory_parent_transfer_log.to_location ");
			
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double amount =0;
			while (rs.next()) {
				product.setQty(rs.getString(1));
				Double price =rs.getDouble(2);
				price = Math.round(price * 100.0) / 100.0;
				product.setPurchase_price(""+price);
				amount = amount+price;
				product.setTotalamt(String.valueOf(Math.round(amount * 100.0) / 100.0));
				product.setReq_qty(rs.getInt(1));
				product.setTotalAmt(price);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	private Product getRequestDeptMaterialSummary(String fromDate, String toDate, String productname,
			String department, String from_location, String category, String locationName) {
		Product product = new Product();
		try {
			toDate = toDate+" "+"59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select sum(inventory_request_temp_log.qty),sum(inventory_request_temp_log.qty * (purchaseprice/inventory_product.pack)) ");
			buffer.append("from inventory_parent_transfer_log ");
			buffer.append("inner join inventory_request_temp_log on inventory_request_temp_log.parentid = inventory_parent_transfer_log.id ");
			buffer.append("inner join apm_condition on apm_condition.id = inventory_parent_transfer_log.from_location ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_request_temp_log.catlogueid ");
			buffer.append("inner join inventory_product on inventory_product.id = inventory_request_temp_log.new_prodid ");
			buffer.append("where transfer_date between '"+fromDate+"' and '"+toDate+"' and req_or_trans='0' and warehouse_id='"+from_location+"' and name is not null ");
			if(!productname.equals("")){
				buffer.append(" and inventory_catalogue.product_name like '"+productname+"%' ");
			}
			if(!category.equals("0")){
				buffer.append("and inventory_catalogue.categoryid='"+category+"' ");
			}
			buffer.append(" and inventory_parent_transfer_log.from_location='"+department+"' ");
			buffer.append("group by inventory_parent_transfer_log.from_location ");
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double amount =0;
			while (rs.next()) {
				product.setQty(rs.getString(1));
				Double price =rs.getDouble(2);
				price = Math.round(price * 100.0) / 100.0;
				product.setPurchase_price(""+price);
				amount = amount+price;
				product.setTotalamt(String.valueOf(Math.round(amount * 100.0) / 100.0));
				product.setReq_qty(rs.getInt(1));
				product.setTotalAmt(price);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	
	}

	public String getCatalogueidsOfIndentRequest(String parentid) {
		String catalogueid="0";
		try {
			String sql ="select catlogueid from inventory_transfer_log where parentid='"+parentid+"' group by catlogueid";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs= preparedStatement.executeQuery();
			while (rs.next()) {
				catalogueid = catalogueid+","+rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return catalogueid;
	}

	public boolean checkAlreadyInTempGRNPO(String loginsessionid, String cataloguid, String location) {
		boolean flag =false;
		try {
			String sql ="select * from grn_with_po_tempdata where catalogueid='"+cataloguid+"' and location='"+location+"' and logsessionid=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, loginsessionid);
			ResultSet rs= preparedStatement.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public int changeShowingdate(String updchargeid, String showdate) {
		int res=0;
		PreparedStatement ps=null;
		try {
			String sql="update apm_invoice_assesments set showing_date='"+showdate+"' where id="+updchargeid+"";
			ps=connection.prepareStatement(sql);
			res=ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public ArrayList<Product> getCatalogueListForSale(String string, String location) {


		ArrayList<Product> list = new ArrayList<Product>();
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String sql = "";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select inventory_catalogue.id, product_name,inventory_catalogue.genericname from inventory_product ");
			buffer.append("inner join inventory_catalogue on inventory_catalogue.id = inventory_product.catalogueid ");
			buffer.append("where stock>0 and inventory_product.pack>0 ");
			if(!location.equals("0")){
				buffer.append("and inventory_product.location='"+location+"' ");
			}
			buffer.append("group by inventory_catalogue.id ");
			buffer.append("order by product_name asc ");
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setProduct_name(rs.getString(2));
				String genericname = rs.getString(3);
				if (genericname == null) {
					genericname = "GEN";
				}
				String data = product.getProduct_name() + "- " + genericname;
				product.setGenericname(data);
				list.add(product);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	
	}

	public ArrayList<Product> getProductListForSale(String val, String location) {


		ArrayList<Product> list = new ArrayList<Product>();
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			String todate = dateFormat.format(cal.getTime());   
			String sql = "";
			if (!location.equals("0")) {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and location='"
						+ location + "' and expiry_date>='"+todate+"' and catalogueid='"+val+"' and pack>0 order by expiry_date asc, (id+0) asc   ";
			} else {
				sql = "select id, prodtypeid, supplierid, brandid, prodcode, prodname, mrp, purchaseprice, saleprice, purdiscount, salediscount, weight, unit, description, categoryid,stock,expiry_date,tax,userid,qty,lastmodified,genericname,mdicinenameid,batch_no,hsnno,catalogueid from inventory_product where stock>0 and pack>0 and expiry_date>='"+todate+"' and catalogueid='"+val+"' order by expiry_date asc, (id+0) asc ";
			}

			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Product product = new Product();
				product.setId(rs.getInt(1));
				product.setSubcategory_id(rs.getString(2));
				product.setVendor_id(rs.getString(3));
				product.setBrand_id(rs.getString(4));
				product.setProduct_code(rs.getString(5));
				product.setProduct_name(rs.getString(6));
				product.setMrp(rs.getString(7));
				product.setPurchase_price(rs.getString(8));
				product.setSale_price(rs.getString(9));
				product.setPurchase_discount(rs.getString(10));
				product.setSale_discount(rs.getString(11));
				product.setWeight(rs.getString(12));
				product.setUnit(rs.getString(13));
				product.setDescription(rs.getString(14));
				product.setCategory_id(rs.getString(15));
				product.setStock(rs.getString(16));
				product.setExpiry_date(rs.getString(17));
				product.setTax(rs.getString(18));
				product.setUserid(rs.getString(19));
				product.setQty(rs.getString(20));
				product.setLastmodified(rs.getString(21));

				String genericname = rs.getString(22);
				if (genericname == null) {
					genericname = "GEN";
				}

				String medicinenameid = rs.getString(23);
				product.setBatch_no(rs.getString(24));
				product.setHsnno(rs.getString(25));
				if (product.getHsnno() == null) {
					product.setHsnno("");
				}
				product.setMedicinenameid(medicinenameid);
				
				String pro_code = getCatlogueProductCode(rs.getString(26));
				
				String expiry= product.getExpiry_date();
				if(expiry!=null){
					if(!expiry.equals("")){
						try {
							String temp[] = expiry.split("-");
							expiry= temp[1]+"/"+temp[0];
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					expiry= "";
				}
				
				String data ="";
				/*if(pro_code!=null){
					if(pro_code.equals("")){
						data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
								+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
								+ ") (" + product.getStock() + ")  ";
					}else{
						data = product.getProduct_name() + "- " + genericname + "- " + pro_code + " - (" + product.getBatch_no() + "/"
								+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
								+ ") (" + product.getStock() + ")  ";
					}
				}else{
					data = product.getProduct_name() + "- " + genericname + " - (" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}*/
				if(pro_code!=null){
					if(pro_code.equals("")){
						data = "(" + product.getBatch_no() + "/"
								+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
								+ ") (" + product.getStock() + ")  ";
					}else{
						data = pro_code + " - (" + product.getBatch_no() + "/"
								+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
								+ ") (" + product.getStock() + ")  ";
					}
				}else{
					data = "(" + product.getBatch_no() + "/"
							+ product.getHsnno() + ") (" + expiry + ") (Rs." + product.getSale_price()
							+ ") (" + product.getStock() + ")  ";
				}
				product.setGenericname(data);
				list.add(product);
				

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	
	}

	public boolean getCheckStockFromGlobalID(String pid, String location) {

		PreparedStatement preparedStatement = null;
		boolean falg =false;
		String sql = "select id from inventory_product where global_prodid = " + pid + " and location='"+location+"' ";
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				falg  = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return falg;
	
	}

	public ArrayList<Master> getWareHouseListNew(String id) {
		ArrayList<Master> list = new ArrayList<Master>();
		try {
			String sql = "select id,name from inventory_warehouse where id="+id+"";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				list.add(master);

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public String getProductCatalogueLocation(String catalogueid) {
		String location="0";
		try {
			String sql="select location from inventory_catalogue where id="+catalogueid+"";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				location = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	



}
