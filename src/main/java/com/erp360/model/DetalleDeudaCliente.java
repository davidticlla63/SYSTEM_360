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

@Entity
@Table(name = "detalle_deuda_cliente", schema = "public")
public class DetalleDeudaCliente implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "moneda")
	private String moneda;
	
	@Column(name = "tipo_cambio")
	private double tipoCambio;

	@Column(name = "total_importe_nacional")
	private double totalImporteNacional;
	
	@Column(name = "total_importe_extranjero")
	private double totalImporteExtranjero;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_deuda_dliente", nullable = true)
	private CuentaCobrar deudaCliente;

	//AC,IN,RM,PN=Cunado tiene saldo pendiente,PR=Cuando la completo de pagar
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "fecha_registro", nullable = false)
	private Date fechaRegistro;
	
	@Column(name = "fecha_modificacion", nullable = true)
	private Date fechaModificacion;
	
	@Column(name = "usuario_registro", nullable = false)
	private String usuarioRegistro;

	public DetalleDeudaCliente() {
		this.id = 0 ;
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
			if(!(obj instanceof DetalleDeudaCliente)){
				return false;
			}else{
				if(((DetalleDeudaCliente)obj).id.intValue()==this.id.intValue()){
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

	public CuentaCobrar getDeudaCliente() {
		return deudaCliente;
	}

	public void setDeudaCliente(CuentaCobrar deudaCliente) {
		this.deudaCliente = deudaCliente;
	}

}
