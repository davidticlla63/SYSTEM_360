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
@Table(name = "detalle_orden_ingreso", schema = "public")
public class DetalleOrdenIngreso implements Serializable{

	private static final long serialVersionUID = 6101168071340551453L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private double cantidad;
	
	@Column(name="cantidad_minima", nullable = true)
	private double cantidadMinima;

	private String observacion;

	private double total;
	
	@Column(name="total_compra", nullable = true)
	private double totalCompra;
	
	@Column(name="garantia",nullable=true)
	private String garantia;
	
	@Column(name="serie",nullable=true)
	private String serie;
	
	@Column(name="precio_venta_credito", nullable = true)
	private double precioVentaCredito;
	
	@Column(name="precio_venta_contado", nullable = true)
	private double precioVentaContado;
	
	@Column(name="precio_compra", nullable = true)
	private double precioCompra;
	
	@Column(name="precio_almacen", nullable = true)
	private double precioAlmacen;
	
	@Column(name="precio_1", nullable = true)
	private double precio1;
	
	@Column(name="precio_2", nullable = true)
	private double precio2;
	
	@Column(name="precio_3", nullable = true)
	private double precio3;
	
	@Column(name="precio_4", nullable = true)
	private double precio4;
	
	@Column(name="precio_5", nullable = true)
	private double precio5;
	
	@Column(name="precio_6", nullable = true)
	private double precio6;
	
	@Column(name="porcentaje_venta_contado", nullable = true)
	private double porcentajeVentaContado;
	
	@Column(name="porcentaje_venta_credito", nullable = true)
	private double porcentajeVentaCredito;
	
	@Column(name="fecha_expiracion",nullable=true)
	private Date fechaExpiracion;
	
	@Column(name = "ubicacion_fisica",nullable=true)
	private String ubicacionFisica;
	
	@Column(name = "numero_lote",nullable=true)
	private String numeroLote;

	// bi-directional many-to-one association to PedidoMov
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_orden_ingreso")
	private OrdenIngreso ordenIngreso;

	// bi-directional many-to-one association to Producto
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_producto", nullable = true)
	private Producto producto;
//
//	// bi-directional many-to-one association to Producto
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_proveedor", nullable = true)
//	private Proveedor proveedor;
	
	private String estado;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	public DetalleOrdenIngreso() {
		super();
		this.id = 0 ;
		this.cantidad = 1;
		this.setEstado("AC");
		this.precioCompra = 0;
		this.porcentajeVentaContado = 0;
		this.porcentajeVentaCredito = 0;
		this.precioVentaCredito = 0;
		this.precioVentaContado = 0;
		this.cantidadMinima = 1;
		this.observacion = "ninguna";
		this.garantia="0";
		this.serie="0";
		this.totalCompra = 0;
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
			if(!(obj instanceof DetalleOrdenIngreso)){
				return false;
			}else{
				if(((DetalleOrdenIngreso)obj).id.intValue() == this.id.intValue()){
					return true;
				}else{
					return false;
				}
			}
		}
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
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

	public OrdenIngreso getOrdenIngreso() {
		return ordenIngreso;
	}

	public void setOrdenIngreso(OrdenIngreso ordenIngreso) {
		this.ordenIngreso = ordenIngreso;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

//	public Proveedor getProveedor() {
//		return proveedor;
//	}
//
//	public void setProveedor(Proveedor proveedor) {
//		this.proveedor = proveedor;
//	}

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public double getPorcentajeVentaContado() {
		return porcentajeVentaContado;
	}

	public void setPorcentajeVentaContado(double porcentajeVentaContado) {
		this.porcentajeVentaContado = porcentajeVentaContado;
	}

	public double getPorcentajeVentaCredito() {
		return porcentajeVentaCredito;
	}

	public void setPorcentajeVentaCredito(double porcentajeVentaCredito) {
		this.porcentajeVentaCredito = porcentajeVentaCredito;
	}

	public double getPrecioVentaCredito() {
		return precioVentaCredito;
	}

	public void setPrecioVentaCredito(double precioVentaCredito) {
		this.precioVentaCredito = precioVentaCredito;
	}

	public double getPrecioVentaContado() {
		return precioVentaContado;
	}

	public void setPrecioVentaContado(double precioVentaContado) {
		this.precioVentaContado = precioVentaContado;
	}

	public double getCantidadMinima() {
		return cantidadMinima;
	}

	public void setCantidadMinima(double cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}

	public String getGarantia() {
		return garantia;
	}

	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public double getTotalCompra() {
		return totalCompra;
	}

	public void setTotalCompra(double totalCompra) {
		this.totalCompra = totalCompra;
	}

	public Date getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	public String getUbicacionFisica() {
		return ubicacionFisica;
	}

	public void setUbicacionFisica(String ubicacionFisica) {
		this.ubicacionFisica = ubicacionFisica;
	}

	public String getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
	}

	public double getPrecio1() {
		return precio1;
	}

	public void setPrecio1(double precio1) {
		this.precio1 = precio1;
	}

	public double getPrecio2() {
		return precio2;
	}

	public void setPrecio2(double precio2) {
		this.precio2 = precio2;
	}

	public double getPrecio3() {
		return precio3;
	}

	public void setPrecio3(double precio3) {
		this.precio3 = precio3;
	}

	public double getPrecio4() {
		return precio4;
	}

	public void setPrecio4(double precio4) {
		this.precio4 = precio4;
	}

	public double getPrecio5() {
		return precio5;
	}

	public void setPrecio5(double precio5) {
		this.precio5 = precio5;
	}

	public double getPrecio6() {
		return precio6;
	}

	public void setPrecio6(double precio6) {
		this.precio6 = precio6;
	}

	public double getPrecioAlmacen() {
		return precioAlmacen;
	}

	public void setPrecioAlmacen(double precioAlmacen) {
		this.precioAlmacen = precioAlmacen;
	}

}
