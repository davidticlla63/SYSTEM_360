package com.erp360.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the almacen database table.
 * 
 */
@Entity 
@Table(name="producto" ,schema="public", uniqueConstraints = @UniqueConstraint(columnNames="codigo"))
public class Producto implements Serializable {

	private static final long serialVersionUID = 5047606646242681208L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String codigo;
	
	@Column(name="modelo", nullable= true)
	private String modelo;
	
	private String nombre;
	
	private String descripcion;
	
	@Column(name="precio_unitario", nullable= false)
	private double precioUnitario;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_linea_producto", nullable=false)
	private LineaProducto lineaProducto;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_unidad_medida", nullable=false)
	private UnidadMedida unidadMedidas;
	
	@OneToMany(mappedBy="producto", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<AlmacenProducto> almacenProductos;

	@Size(max = 2) //AC , IN
	@Column(name="estado", nullable=false )
	private String estado;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true)
	private Date fechaModificacion;
	
	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	@Column(name="precio_venta_contado",nullable=true)
	private double precioVentaContado;
	
	@Column(name="precio_venta_credito",nullable=true)
	private double precioVentaCredito;
	
	@Column(name="precio_compra",nullable=true)
	private double precioCompra;
	
	@Column(name="precio_compra_1",nullable=true)
	private double precioCompra1;
	
	@Column(name="precio_compra_2",nullable=true)
	private double precioCompra2;
	
	@Column(name="precio_compra_3",nullable=true)
	private double precioCompra3;
	
	@Column(name="precio_compra_4",nullable=true)
	private double precioCompra4;
	
	@Column(name="precio_compra_5",nullable=true)
	private double precioCompra5;
	
	@Column(name="precio_compra_6",nullable=true)
	private double precioCompra6;
	
	@Column(name="stock_min")
	private double stockMin;

	@Column(name="stock_max")
	private double stockMax;
	
	public Producto(Integer id, String codigo,String nombre,String descripcion) {
		this.id= id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	public Producto() {
		super();
		this.id = 0;
		this.nombre = "";
		this.precioUnitario = 0;
		this.precioCompra = 0;
		this.precioCompra1 = 0;
		this.precioCompra2 = 0;
		this.precioCompra3 = 0;
		this.precioCompra4 = 0;
		this.precioCompra5 = 0;
		this.precioVentaCredito = 0;
		this.precioVentaContado = 0;
		this.unidadMedidas = new UnidadMedida();
		this.lineaProducto = new LineaProducto();
		this.codigo = "";
	}
	
	@Override
	public String toString() {
		return  nombre +" " + descripcion;
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
			if(!(obj instanceof Producto)){
				return false;
			}else{
				if(((Producto)obj).id.intValue() == this.id.intValue()){
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

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public UnidadMedida getUnidadMedidas() {
		return unidadMedidas;
	}

	public void setUnidadMedidas(UnidadMedida unidadMedidas) {
		this.unidadMedidas = unidadMedidas;
	}

	public LineaProducto getLineaProducto() {
		return lineaProducto;
	}

	public void setLineaProducto(LineaProducto lineaProducto) {
		this.lineaProducto = lineaProducto;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public List<AlmacenProducto> getAlmacenProductos() {
		return almacenProductos;
	}

	public void setAlmacenProductos(List<AlmacenProducto> almacenProductos) {
		this.almacenProductos = almacenProductos;
	}

	public double getPrecioVentaContado() {
		return precioVentaContado;
	}

	public void setPrecioVentaContado(double precioVentaContado) {
		this.precioVentaContado = precioVentaContado;
	}

	public double getPrecioVentaCredito() {
		return precioVentaCredito;
	}

	public void setPrecioVentaCredito(double precioVentaCredito) {
		this.precioVentaCredito = precioVentaCredito;
	}

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public double getStockMin() {
		return stockMin;
	}

	public void setStockMin(double stockMin) {
		this.stockMin = stockMin;
	}

	public double getStockMax() {
		return stockMax;
	}

	public void setStockMax(double stockMax) {
		this.stockMax = stockMax;
	}

	public double getPrecioCompra1() {
		return precioCompra1;
	}

	public void setPrecioCompra1(double precioCompra1) {
		this.precioCompra1 = precioCompra1;
	}

	public double getPrecioCompra2() {
		return precioCompra2;
	}

	public void setPrecioCompra2(double precioCompra2) {
		this.precioCompra2 = precioCompra2;
	}

	public double getPrecioCompra3() {
		return precioCompra3;
	}

	public void setPrecioCompra3(double precioCompra3) {
		this.precioCompra3 = precioCompra3;
	}

	public double getPrecioCompra4() {
		return precioCompra4;
	}

	public void setPrecioCompra4(double precioCompra4) {
		this.precioCompra4 = precioCompra4;
	}

	public double getPrecioCompra5() {
		return precioCompra5;
	}

	public void setPrecioCompra5(double precioCompra5) {
		this.precioCompra5 = precioCompra5;
	}

	public double getPrecioCompra6() {
		return precioCompra6;
	}

	public void setPrecioCompra6(double precioCompra6) {
		this.precioCompra6 = precioCompra6;
	}

}