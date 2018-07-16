/**
 * @author david
 */
package com.erp360.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;





/**
 * 
 * ESTA CLASE ES PARA MANEJAR TURNOS DE CAJAS
 *
 */
@Entity
@Table(name = "caja_sesion", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class CajaSesion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_caja", nullable = false)
	private Caja caja;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario", nullable = true)
	private Usuario usuario;

	@Size(max = 15)// AC , IN , RM
	@Column(name = "estado", nullable = false)
	private String estado;

	@Column(name = "monto_inicial", nullable = false)
	private Double montoInicial=0.0;

	@Column(name = "monto_final", nullable = true)
	private Double montoFinal=0.0;

	@Column(name = "diferencia", nullable = true)
	private Double diferencia=0.0;
	
	@Column(name = "procesada", nullable = true)
	private boolean procesada;

	@Column(name = "fecha_apertura", nullable = true)
	private Date fechaApertura;

	@Column(name = "fecha_cierre", nullable = true)
	private Date fechaCierre;

	@Column(name = "usuario_registro", nullable = false)
	private String usuarioRegistro;

	@Column(name = "fecha_registro", nullable = false)
	private Date fechaRegistro;

	@Column(name = "fecha_modificacion", nullable = true)
	private Date fechaModificacion;

	@Size(max = 255)
	@Column(name = "observacion", nullable = false)
	private String observacion;
	
	@OneToMany(mappedBy = "cajaSesion", orphanRemoval=true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CajaMovimiento> listaCajaMovimientos = new ArrayList<>();

	public CajaSesion() {

		this.id = 0;

		this.estado = "AC";

		this.observacion="Ninguna";
		this.usuarioRegistro = "";
	}




	@Override
	public String toString() {
		return "CajaOperacion [id=" + id + ", caja=" + caja + ", estado="
				+ estado + ", procesada=" + procesada + ", fechaApertura="
				+ fechaApertura + ", fechaCierre=" + fechaCierre
				+ ", usuarioRegistro=" + usuarioRegistro + ", fechaRegistro="
				+ fechaRegistro + ", fechaModificacion=" + fechaModificacion
				+ "]";
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
			if (!(obj instanceof CajaSesion)) {
				return false;
			} else {
				if (((CajaSesion) obj).id == this.id) {
					return true;
				} else {
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

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}
	public boolean isProcesada() {
		return procesada;
	}
	public void setProcesada(boolean procesada) {
		this.procesada = procesada;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public Double getMontoInicial() {
		return montoInicial;
	}

	public void setMontoInicial(Double montoInicial) {
		this.montoInicial = montoInicial;
	}
	public Double getMontoFinal() {
		return montoFinal;
	}

	public void setMontoFinal(Double montoFinal) {
		this.montoFinal = montoFinal;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Double getDiferencia() {
		return diferencia;
	}
	public void setDiferencia(Double diferencia) {
		this.diferencia = diferencia;
	}


	public List<CajaMovimiento> getListaCajaMovimientos() {
		return listaCajaMovimientos;
	}

	public void setListaCajaMovimientos(List<CajaMovimiento> listaCajaMovimientos) {
		this.listaCajaMovimientos = listaCajaMovimientos;
	}




	public Usuario getUsuario() {
		return usuario;
	}




	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


}
