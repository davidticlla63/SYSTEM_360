package com.erp360.model;

import java.io.Serializable;
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

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name = "cuenta_cobrar", schema = "public",uniqueConstraints = @UniqueConstraint(columnNames={"correlativo"}))
public class CuentaCobrar implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String correlativo;//000001 = 6 digitos
	
	@Column(name = "observacion")
	private String observacion;
	
	//BOLIVIANOS DOLAR
	@Column(name = "moneda")
	private String moneda;
	
	@Column(name = "tipo_cambio")
	private double tipoCambio;
	
	@Column(name = "saldo_nacional")
	private double saldoNacional;
	
	@Column(name = "saldo_extranjero")
	private double saldoExtranjero;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_nota_venta", nullable = true)
	private NotaVenta notaVenta;
	
	@OneToMany(mappedBy="cuentaCobrar", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Cobranza> cobranzas;

	//AC,IN,RM,PN=Cunado tiene saldo pendiente,PR=Cuando la completo de pagar
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "fecha_registro", nullable = false)
	private Date fechaRegistro;
	
	@Column(name = "fecha_modificacion", nullable = true)
	private Date fechaModificacion;
	
	@Column(name = "usuario_registro", nullable = false)
	private String usuarioRegistro;

	public CuentaCobrar() {
		this.id = 0 ;
		this.observacion = "Ninguna";
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
			if(!(obj instanceof CuentaCobrar)){
				return false;
			}else{
				if(((CuentaCobrar)obj).id.intValue()==this.id.intValue()){
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

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public double getSaldoNacional() {
		return saldoNacional;
	}

	public void setSaldoNacional(double saldoNacional) {
		this.saldoNacional = saldoNacional;
	}

	public double getSaldoExtranjero() {
		return saldoExtranjero;
	}

	public void setSaldoExtranjero(double saldoExtranjero) {
		this.saldoExtranjero = saldoExtranjero;
	}

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public List<Cobranza> getCobranzas() {
		return cobranzas;
	}

	public void setCobranzas(List<Cobranza> cobranzas) {
		this.cobranzas = cobranzas;
	}

}
