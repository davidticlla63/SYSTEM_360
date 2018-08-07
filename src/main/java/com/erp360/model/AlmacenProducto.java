package com.erp360.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name="almacen_producto" ,schema="public")
public class AlmacenProducto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="precio_venta_credito", nullable = true)
	private double precioVentaCredito;

	@Column(name="precio_venta_contado", nullable = true)
	private double precioVentaContado;

	@Column(name="precio_compra", nullable = true)
	private double precioCompra;

	@Column(name="porcentaje_venta_contado", nullable = true)
	private double porcentajeVentaContado;

	@Column(name="porcentaje_venta_credito", nullable = true)
	private double porcentajeVentaCredito;
	
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

	private double stock;

	@Column(name="stock_min")
	private double stockMin;

	@Column(name="stock_max")
	private double stockMax;
	
	@Column(name="garantia",nullable=true)
	private String garantia;
	
	@Column(name="serie",nullable=true)
	private String serie;
	
	@Column(name="fecha_expiracion",nullable=true)
	private Date fechaExpiracion;
	
	@Column(name = "ubicacion_fisica",nullable=true)
	private String ubicacionFisica;
	
	@Column(name = "numero_lote",nullable=true)
	private String numeroLote;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_almacen", nullable=false, referencedColumnName="id")
	private Almacen almacen;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_producto", nullable=false, referencedColumnName="id")
	private Producto producto;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_proveedor", nullable=true)
	private Proveedor proveedor;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_gestion", nullable=true)
	private Gestion gestion;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_orden_ingreso", nullable=true)
	private OrdenIngreso ordenIngreso;
	
	private String estado;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	public AlmacenProducto(Integer id,Producto producto){
		this.id = id;
		this.producto = new Producto();
	}

	public AlmacenProducto() {
		super();
		this.id = 0;
		this.producto = new Producto();
		this.proveedor = new Proveedor();
		this.almacen = new Almacen();
		this.precioCompra = 0;
		this.porcentajeVentaContado = 0;
		this.porcentajeVentaCredito = 0;
		this.precioVentaCredito = 0;
		this.precioVentaContado = 0;
		this.stock = 0;
		this.stockMax = 500;
		this.stockMin = 1;
		this.garantia = "1";
	}
	
	public AlmacenProducto(double stock,double precioVentaCredito,double precioVentaContado,double precioCompra) {
		this.stock = 0;
		this.precioVentaCredito = 0;
		this.precioVentaContado = 0;
		this.precioCompra = 0;
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

	public double getStock() {
		return this.stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Almacen getAlmacen() {
		return this.almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}


	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}


	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}


	public double getStockMax() {
		return stockMax;
	}

	public void setStockMax(double stockMax) {
		this.stockMax = stockMax;
	}

	public double getStockMin() {
		return stockMin;
	}

	public void setStockMin(double stockMin) {
		this.stockMin = stockMin;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public OrdenIngreso getOrdenIngreso() {
		return ordenIngreso;
	}

	public void setOrdenIngreso(OrdenIngreso ordenIngreso) {
		this.ordenIngreso = ordenIngreso;
	}

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

	@Override
	public String toString() {
		return "AlmacenProducto [id=" + id + ", precioVentaCredito="
				+ precioVentaCredito + ", precioVentaContado="
				+ precioVentaContado + ", precioCompra=" + precioCompra
				+ ", porcentajeVentaContado=" + porcentajeVentaContado
				+ ", porcentajeVentaCredito=" + porcentajeVentaCredito
				+ ", stock=" + stock + ", stockMin=" + stockMin + ", stockMax="
				+ stockMax + ", garantia=" + garantia + ", serie=" + serie
				+ ", almacen=" + almacen + ", producto=" + producto
				+ ", proveedor=" + proveedor + ", gestion=" + gestion
				+ ", ordenIngreso=" + ordenIngreso + ", estado=" + estado
				+ ", fechaRegistro=" + fechaRegistro + ", usuarioRegistro="
				+ usuarioRegistro + "]";
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

	public Date getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	public double getPrecioAlmacen() {
		return precioAlmacen;
	}

	public void setPrecioAlmacen(double precioAlmacen) {
		this.precioAlmacen = precioAlmacen;
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

}