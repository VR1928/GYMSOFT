package com.apm.Master.eu.bi;

import java.util.ArrayList;

import com.apm.Master.eu.entity.Declaration;
import com.apm.Master.eu.entity.Master;
import com.apm.common.utils.Pagination;

public interface DeclarationDAO {

	ArrayList<Declaration> getDeclarationList(Pagination pagination, String searchText);

	int getTotalDeclarationCount(String searchText);

	int saveDeclaration(Declaration declaration,int id);

	Declaration getDeclaration(int id, Declaration declaration);

	int updateDeclaration(Declaration declaration, int id);

	int deleteDeclaration(int id, Declaration declaration);
	public ArrayList<Master> getMasterList();

}
