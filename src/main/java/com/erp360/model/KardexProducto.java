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

import com.erp360.model.Almacen;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity 
@Table(name="kardex_producto" ,schema="public")
public class KardexProducto implements Serializable{

	private static final long serialVersionUID = -3926411873175668698L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="tipo_transaccion")
	private String tipoTransaccion;// VENTA,COMPRA,TRASPASO,BAJAS

	@Column(name="tipo_movimiento")
	private String tipoMovimiento;//OI=orden de Ingreso , OS=orden de salida
	
	@Column(name="precio_entrada_compra")
	private double precioEntradaCompra;
	
	@Column(name="precio_entrada_venta")
	private double precioEntradaVenta;
	
	@Column(name="precio_salida")
	private double precioSalida;
	
	@Column(name="stock_entrante")
	private double stockEntrante;
	
	@Column(name="stock_saliente")
	private double stockSaliente;
	
	@Column(name="estado")
	private String estado;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	@Column(name="fecha_registro")
	private Date fechaRegistro;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_producto", nullable=true)
	private Producto producto;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_almacen", nullable=true)
	private Almacen almacen;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_orden_ingreso", nullable=true)
	private OrdenIngreso ordenIngreso;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_orden_salida", nullable=true)
	private OrdenSalida ordenSalida;
	
	public KardexProducto(){
		super();
		this.id = 0;
		this.precioEntradaCompra = 0;
		this.precioEntradaVenta = 0;
		this.precioSalida = 0;
		this.stockEntrante = 0;
		this.stockSaliente = 0;
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
			if(!(obj instanceof Almacen)){
				return false;
			}else{
				if(((KardexProducto)obj).id.intValue()==this.id.intValue()){
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

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public double getStockEntrante() {
		return stockEntrante;
	}

	public void setStockEntrante(double stockEntrante) {
		this.stockEntrante = stockEntrante;
	}

	public double getPrecioSalida() {
		return precioSalida;
	}

	public void setPrecioSalida(double precioSalida) {
		this.precioSalida = precioSalida;
	}

	public double getStockSaliente() {
		return stockSaliente;
	}

	public void setStockSaliente(double stockSaliente) {
		this.stockSaliente = stockSaliente;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public OrdenIngreso getOrdenIngreso() {
		return ordenIngreso;
	}

	public void setOrdenIngreso(OrdenIngreso ordenIngreso) {
		this.ordenIngreso = ordenIngreso;
	}

	public OrdenSalida getOrdenSalida() {
		return ordenSalida;
	}

	public void setOrdenSalida(OrdenSalida ordenSalida) {
		this.ordenSalida = ordenSalida;
	}

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public double getPrecioEntradaCompra() {
		return precioEntradaCompra;
	}

	public void setPrecioEntradaCompra(double precioEntradaCompra) {
		this.precioEntradaCompra = precioEntradaCompra;
	}

	public double getPrecioEntradaVenta() {
		return precioEntradaVenta;
	}

	public void setPrecioEntradaVenta(double precioEntradaVenta) {
		this.precioEntradaVenta = precioEntradaVenta;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

}