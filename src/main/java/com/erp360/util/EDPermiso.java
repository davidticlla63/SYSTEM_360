package com.erp360.util;

import com.erp360.model.Modulo;
import com.erp360.model.Permiso;


public class EDPermiso {

	private Integer id;
	private String nombre;
	private Modulo modulo;
	private Permiso permiso;
	private boolean check;

	public EDPermiso(Integer id, String nombre, Modulo modulo, Permiso permiso,boolean check) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.modulo = modulo;
		this.permiso = permiso;
		this.check = check;
	}
	
	@Override
	public String toString(){
		return nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public Permiso getPermiso() {
		return permiso;
	}

	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
	}



	// Constructor
	public EDPermiso() {
		super();		
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


}
