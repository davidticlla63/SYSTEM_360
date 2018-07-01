package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * @author Mauricio.Bejarano.Rivera
 * 
 */
@Entity
@Table(name = "parametro_cobranza", schema = "public")
public class ParametroCobranza implements Serializable {

	private static final long serialVersionUID = 7580337636061446309L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	//{ VOS=VARIAS ORDEN SALIDA |  OT= ORDEN TRASPASO }
	@Column(name="tipo_disminucion_stock",nullable=false )
	private String tipoDisminucionStock;//tipo de disminucion de stock (A traves de Varias orden de salida o A travez de Una orden traspaso)
	
	//------
	@Size(max = 2) //AC , IN
	@Column(name="estado",nullable=true )
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public ParametroCobranza() {
		super();
		this.id = 0;
		this.tipoDisminucionStock = "VOS";
	}
	
	@Override
	public String toString() {
		return String.valueOf(id);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof ParametroCobranza)){
				return false;
			}else{
				if(((ParametroCobranza)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getTipoDisminucionStock() {
		return tipoDisminucionStock;
	}

	public void setTipoDisminucionStock(String tipoDisminucionStock) {
		this.tipoDisminucionStock = tipoDisminucionStock;
	}

	
}


