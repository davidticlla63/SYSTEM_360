package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name = "cotizacion", schema = "public")
public class Cotizacion implements Serializable {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="codigo", nullable=false )
	private String codigo;

	@Column(name="monto_total", nullable=false )
	private double montoTotal;

	@Column(name="monto_total_extranjero", nullable=true )
	private double montoTotalExtranjero;

	@Column(name="tipo_cambio", nullable=true )
	private double tipoCambio;

	//BOLIVIANOS , DOLAR
	@Column(name="moneda", nullable=true )
	private String moneda;

	@Column(name="concepto", nullable=false )
	private String concepto;
	
	@Column(name="fecha_caducidad", nullable=false )
	private Date fechaCaducidad;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_cliente", nullable=true)
	private Cliente cliente;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_encargado_venta", nullable=true)
	private EncargadoVenta encargadoVenta;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;

	@Size(max = 2) //AC , IN
	private String estado;

	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;

	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public Cotizacion() {
		super();
		this.id = 0;
		this.codigo = "0";
		this.montoTotal = 0;
		this.montoTotalExtranjero = 0;
		this.tipoCambio = 0;
		this.estado = "PN";
		this.moneda = "DOLAR";
		this.encargadoVenta = new EncargadoVenta();
		this.concepto = "Los precios estan sujetos a cambios sin previo aviso.";
	}

	@Override
	public String toString() {
		return "NotaVenta [id=" + id + ", codigo=" + codigo + ", montoTotal="
				+ montoTotal + ", montoTotalExtranjero=" + montoTotalExtranjero
				+ ", tipoCambio=" + tipoCambio + ", concepto=" + concepto + ", cliente=" + cliente
				+ ", encargadoVenta=" + encargadoVenta + ", reservaVenta="
				+ ", empresa=" + empresa + ", gestion="
				+ gestion + ", estado=" + estado + ", fechaRegistro="
				+ fechaRegistro + ", UsuarioRegistro=" + UsuarioRegistro + "]";
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
			if(!(obj instanceof Cotizacion)){
				return false;
			}else{
				if(((Cotizacion)obj).id==this.id){
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

	public String getUsuarioRegistro() {
		return UsuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		UsuarioRegistro = usuarioRegistro;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public EncargadoVenta getEncargadoVenta() {
		return encargadoVenta;
	}

	public void setEncargadoVenta(EncargadoVenta encargadoVenta) {
		this.encargadoVenta = encargadoVenta;
	}
	
	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public double getMontoTotalExtranjero() {
		return montoTotalExtranjero;
	}

	public void setMontoTotalExtranjero(double montoTotalExtranjero) {
		this.montoTotalExtranjero = montoTotalExtranjero;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	
	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}


}


