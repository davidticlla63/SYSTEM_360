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
@Table(name = "detalle_cotizacion", schema = "public")
public class DetalleCotizacion implements Serializable,Comparable<DetalleCotizacion> {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="coeficiente_interes",nullable=false )
	private double coeficienteInteres;
	
	@Column(name="porcentaje_cuota_inicial",nullable=false )
	private double porcentajeCuotaInicial;
	
	@Column(name="precio_contado_nacional",nullable=false )
	private double precioContadoNacional;
	
	@Column(name="precio_contado_extranjero",nullable=false )
	private double precioContadoExtranjero;
	
	@Column(name="precio_credito_nacional",nullable=false )
	private double precioCreditoNacional;
	
	@Column(name="precio_credito_extranjero",nullable=false )
	private double precioCreditoExtranjero;
	
	@Column(name="precio_cuota_inicial_nacional",nullable=false )
	private double precioCuotaInicialNacional;
	
	@Column(name="precio_cuota_inicial_extranjera",nullable=false )
	private double precioCuotaInicialExtranjera;
	
	@Column(name="precio_cuota_mensual_nacional",nullable=false )
	private double precioCuotaMensualNacional;
	
	@Column(name="precio_cuota_mensual_extranjera",nullable=false )
	private double precioCuotaMensualExtranjera;
	
	@Column(name="interes_mensual_nacional",nullable=false )
	private double interesMensualNacional;
	
	@Column(name="interes_mensual_extranjero",nullable=false )
	private double interesMensualExtranjero;
	
	@Column(name="cantidad",nullable=true )
	private double cantidad;
	
	@Column(name="numero_cuotas",nullable=true )
	private double numeroCuotas;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_cotizacion", nullable=false)
	private Cotizacion cotizacion;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_producto", nullable=false)
	private Producto producto;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public DetalleCotizacion() {
		super();
		this.id = 0;
		this.cantidad = 1;
		this.estado = "AC";
	}
	
	public DetalleCotizacion(Integer id){
		this.id = id;
	}

	@Override
	public String toString() {
		return "DetalleCotizacion [id=" + id + ", porcentajeCuotaInicial="
				+ porcentajeCuotaInicial + ", precioContadoNacional="
				+ precioContadoNacional + ", precioContadoExtranjero="
				+ precioContadoExtranjero + ", precioCreditoNacional="
				+ precioCreditoNacional + ", precioCreditoExtranjero="
				+ precioCreditoExtranjero + ", precioCuotaInicialNacional="
				+ precioCuotaInicialNacional
				+ ", precioCuotaInicialExtranjera="
				+ precioCuotaInicialExtranjera
				+ ", precioCuotaMensualNacional=" + precioCuotaMensualNacional
				+ ", precioCuotaMensualExtranjera="
				+ precioCuotaMensualExtranjera + ", interesMensualNacional="
				+ interesMensualNacional + ", interesMensualExtranjero="
				+ interesMensualExtranjero + ", cantidad=" + cantidad
				+ ", numeroCuotas=" + numeroCuotas + ", cotizacion="
				+ cotizacion + ", producto=" + producto + ", estado=" + estado
				+ ", fechaRegistro=" + fechaRegistro + ", UsuarioRegistro="
				+ UsuarioRegistro + "]";
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
			if(!(obj instanceof DetalleCotizacion)){
				return false;
			}else{
				if(((DetalleCotizacion)obj).id==this.id){
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

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public double getNumeroCuotas() {
		return numeroCuotas;
	}

	public void setNumeroCuotas(double numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public double getPrecioContadoNacional() {
		return precioContadoNacional;
	}

	public void setPrecioContadoNacional(double precioContadoNacional) {
		this.precioContadoNacional = precioContadoNacional;
	}

	public double getPrecioContadoExtranjero() {
		return precioContadoExtranjero;
	}

	public void setPrecioContadoExtranjero(double precioContadoExtranjero) {
		this.precioContadoExtranjero = precioContadoExtranjero;
	}

	public double getPrecioCreditoNacional() {
		return precioCreditoNacional;
	}

	public void setPrecioCreditoNacional(double precioCreditoNacional) {
		this.precioCreditoNacional = precioCreditoNacional;
	}

	public double getPrecioCreditoExtranjero() {
		return precioCreditoExtranjero;
	}

	public void setPrecioCreditoExtranjero(double precioCreditoExtranjero) {
		this.precioCreditoExtranjero = precioCreditoExtranjero;
	}

	public double getPrecioCuotaInicialNacional() {
		return precioCuotaInicialNacional;
	}

	public void setPrecioCuotaInicialNacional(double precioCuotaInicialNacional) {
		this.precioCuotaInicialNacional = precioCuotaInicialNacional;
	}

	public double getPrecioCuotaInicialExtranjera() {
		return precioCuotaInicialExtranjera;
	}

	public void setPrecioCuotaInicialExtranjera(double precioCuotaInicialExtranjera) {
		this.precioCuotaInicialExtranjera = precioCuotaInicialExtranjera;
	}

	public double getPrecioCuotaMensualNacional() {
		return precioCuotaMensualNacional;
	}

	public void setPrecioCuotaMensualNacional(double precioCuotaMensualNacional) {
		this.precioCuotaMensualNacional = precioCuotaMensualNacional;
	}

	public double getPrecioCuotaMensualExtranjera() {
		return precioCuotaMensualExtranjera;
	}

	public void setPrecioCuotaMensualExtranjera(double precioCuotaMensualExtranjera) {
		this.precioCuotaMensualExtranjera = precioCuotaMensualExtranjera;
	}

	public double getPorcentajeCuotaInicial() {
		return porcentajeCuotaInicial;
	}

	public void setPorcentajeCuotaInicial(double porcentajeCuotaInicial) {
		this.porcentajeCuotaInicial = porcentajeCuotaInicial;
	}

	public double getInteresMensualNacional() {
		return interesMensualNacional;
	}

	public void setInteresMensualNacional(double interesMensualNacional) {
		this.interesMensualNacional = interesMensualNacional;
	}

	public double getInteresMensualExtranjero() {
		return interesMensualExtranjero;
	}

	public void setInteresMensualExtranjero(double interesMensualExtranjero) {
		this.interesMensualExtranjero = interesMensualExtranjero;
	}

	/**
	 * compareTo by ID
	 */
	@Override
	public int compareTo(DetalleCotizacion detalleCotizacion) {
		return this.id.compareTo(detalleCotizacion.id);
	}

	public double getCoeficienteInteres() {
		return coeficienteInteres;
	}

	public void setCoeficienteInteres(double coeficienteInteres) {
		this.coeficienteInteres = coeficienteInteres;
	}
}


