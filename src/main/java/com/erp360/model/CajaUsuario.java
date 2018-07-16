/**
 * @author david
 */
package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;



/**
 * Entity implementation class for Entity: Cargo
 *
 */
@Entity
@Table(name = "caja_usuario", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class CajaUsuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Size(max = 2)
	@Column(name = "estado", nullable = true)
	/*
	 * pendiente = PE aprobado = AP vigente = VI atrasado = AT procesado = PR
	 */
	private String estado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_caja")
	private Caja caja;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name = "fecha_modificacion", nullable = true)
	private Date fechaModificacion;
	
	@Column(name = "fecha_registro", nullable = true)
	private Date fechaRegistro;
	
	@Column(name = "usuario_registro", nullable = true)
	private String usuarioRegistro;
	
	private static final long serialVersionUID = 1L;
	
	
	
	public CajaUsuario() {
		super();
		this.id = 0;
		this.estado = "AC";
		this.usuarioRegistro = "";
	}

	public Integer getId() {
		return id;
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

	

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fecha_modificacion) {
		this.fechaModificacion = fecha_modificacion;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fecha_registro) {
		this.fechaRegistro = fecha_registro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuario_registro) {
		this.usuarioRegistro = usuario_registro;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (!(obj instanceof CajaUsuario)) {
				return false;
			} else {
				if (((CajaUsuario) obj).id == this.id ) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the precioSubTotal
	 */
	



	/**
	 * @return the cotizacion
	 */
	

	/**
	 * @return the concepto
	 */
	

	

	/**
	 * @param concepto
	 *            the concepto to set
	 */

	/**
	 * @return the cantidad
	 */

	/**
	 * @param cantidad
	 *            the cantidad to set
	 */
	

	


}
