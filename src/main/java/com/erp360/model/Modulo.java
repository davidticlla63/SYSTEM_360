package com.erp360.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name = "modulo", schema = "public")
public class Modulo implements  Serializable, Comparable<Modulo> {

	private static final long serialVersionUID = 5422461008458515295L;
	private Integer id;
	private String nombre;
	@Column(name = "modulo_icono", nullable = true)
	private String moduloIcono;
	private String estado;

	public Modulo() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}
	
	@Override
	public String toString() {
		return nombre ;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof Modulo)){
				return false;
			}else{
				if(((Modulo)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}

	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Override
	public int compareTo(Modulo o) {
		if (this.id < o.id) {
			return -1;
		}
		if (this.id > o.id) {
			return 1;
		}
		return 0;
	}

	public String getModuloIcono() {
		return moduloIcono;
	}

	public void setModuloIcono(String moduloIcono) {
		this.moduloIcono = moduloIcono;
	}
}


