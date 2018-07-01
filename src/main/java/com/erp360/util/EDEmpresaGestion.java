package com.erp360.util;

import com.erp360.model.Empresa;
import com.erp360.model.Gestion;

public class EDEmpresaGestion {
	
	private Empresa empresa;
	private Gestion gestion;
	
	public EDEmpresaGestion(){
		
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Gestion getGestion() {
		return gestion;
	}
	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	
}
