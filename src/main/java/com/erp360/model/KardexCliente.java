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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "kardex_cliente", schema = "public")
public class KardexCliente implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "observacion")
	private String observacion;
	
	/*
	 * INGRESO X VENTA
	 * INGRESO X RESERVA
	 * EGRESO X PAGO PROVEEDORES
	 * EGRESO X PAGO SUELDO
	 */
	@Column(name = "motivo")
	private String motivo;
	
	/**
	 * INGRESO
	 * EGRESO
	 */
	@Column(name = "tipo", nullable = true)
	private String tipo;
	
	/*
	 * FACTURA
	 * RECIBO
	 * OTRO DOCUMENTO
	 */
	@Column(name = "tipo_documento")
	private String tipoDocumento;
	
	@Column(name = "fecha_documento", nullable = true)
	private Date fechaDocumento;

	/**
	 * SI CORRESPONDE A UNA FACTURA Nº 008723
	 */
	@Column(name = "numero_documento")
	private String numeroDocumento;
	
	/*
	 * BOLIVIANOS
	 * DOLAR
	 */
	@Column(name = "moneda")
	private String moneda;
	
	@Column(name = "tipo_cambio")
	private double tipoCambio;

	@Column(name = "total_importe_nacional")
	private double totalImporteNacional;
	
	@Column(name = "total_importe_extranjero")
	private double totalImporteExtranjero;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cliente", nullable = true)
	private Cliente cliente;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_notaVenta", nullable = true)
	private NotaVenta notaVenta;
	
	//campo no persistido en db
	@Transient
	private double saldo;

	//AC,IN,RM
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "fecha_registro", nullable = false)
	private Date fechaRegistro;
	
	@Column(name = "fecha_modificacion", nullable = true)
	private Date fechaModificacion;
	
	@Column(name = "usuario_registro", nullable = false)
	private String usuarioRegistro;

	public KardexCliente() {
		this.id = 0 ;
		this.tipoDocumento = "FACTURA";
		this.motivo = "INGRESO X VENTA";
		this.observacion = "Ninguna";
		this.numeroDocumento = "";
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
			if(!(obj instanceof KardexCliente)){
				return false;
			}else{
				if(((KardexCliente)obj).id.intValue()==this.id.intValue()){
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

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Date getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
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

	public double getTotalImporteNacional() {
		return totalImporteNacional;
	}

	public void setTotalImporteNacional(double totalImporteNacional) {
		this.totalImporteNacional = totalImporteNacional;
	}

	public double getTotalImporteExtranjero() {
		return totalImporteExtranjero;
	}

	public void setTotalImporteExtranjero(double totalImporteExtranjero) {
		this.totalImporteExtranjero = totalImporteExtranjero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

}
