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
@Table(name = "detalle_nota_venta", schema = "public")
public class DetalleNotaVenta implements Serializable {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private double precio;
	
	@Column(name="precio_extranjero",nullable=true )
	private double precioExtranjero;
	
	@Column(name="precio_contado_nacional",nullable=true )
	private double precioContadoNacional;
	
	@Column(name="precio_contado_extranjero",nullable=true )
	private double precioContadoExtranjero;
	
	@Column(name="porcentaje_descuento",nullable=true )
	private double porcentajeDescuento;
	
	private double cantidad;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_nota_venta", nullable=false)
	private NotaVenta notaVenta;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_producto", nullable=false)
	private Producto producto;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public DetalleNotaVenta() {
		super();
		this.id = 0;
		this.precioExtranjero = 0;
		this.precio = 0;
		this.cantidad = 1;
		this.estado = "AC";
		this.precioContadoNacional = 0;
		this.precioContadoExtranjero = 0;
		this.porcentajeDescuento = 0;
	}
	
	@Override
	public String toString() {
		return ""+id ;
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
			if(!(obj instanceof DetalleNotaVenta)){
				return false;
			}else{
				if(((DetalleNotaVenta)obj).id==this.id){
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

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioExtranjero() {
		return precioExtranjero;
	}

	public void setPrecioExtranjero(double precioExtranjero) {
		this.precioExtranjero = precioExtranjero;
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

	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}
}


