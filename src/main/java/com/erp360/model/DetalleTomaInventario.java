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
@Table(name = "detalle_toma_inventario", schema = "public")
public class DetalleTomaInventario implements Serializable{

	private static final long serialVersionUID = 6101168071340551453L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private int cantidad;
	
	@Column(name="precio_venta_contado",nullable=true)
	private double precioVentaContado;
	
	@Column(name="precio_venta_credito",nullable=true)
	private double precioVentaCredito;
	
	@Column(name="precio_compra",nullable=true)
	private double precioCompra;
	
	@Column(name="total",nullable=true)
	private double total;
	
	@Column(name="observacion",nullable=true)
	private String observacion;
	
	@Column(name="cantidad_registrada")
	private double cantidadRegistrada;
	
	@Column(name="cantidad_verificada",nullable=true)
	private Double cantidadVerificada;
	
	@Column(name="diferencia",nullable=true)
	private Double diferencia;

	// bi-directional many-to-one association to PedidoMov
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_toma_inventario")
	private TomaInventario tomaInventario;

	// bi-directional many-to-one association to Producto
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "id_producto", nullable = true)
	private Producto producto;
//    
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_proveedor", nullable = true)
//	private Proveedor proveedor;
	
	private String estado;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	public DetalleTomaInventario() {
		super();
		this.id = 0 ;
		this.cantidad = 0;
		this.setEstado("AC");
		this.observacion = "";
		this.cantidadVerificada = null;
		this.diferencia = null;
	}
	
	@Override
	public String toString() {
		return "DetalleTomaInventario [id=" + id + ", cantidad=" + cantidad
				+ ", precioVentaContado=" + precioVentaContado
				+ ", precioVentaCredito=" + precioVentaCredito
				+ ", precioCompra=" + precioCompra + ", total=" + total
				+ ", observacion=" + observacion + ", cantidadRegistrada="
				+ cantidadRegistrada + ", cantidadVerificada="
				+ cantidadVerificada + ", diferencia=" + diferencia
				+ ", tomaInventario=" + tomaInventario + ", producto="
				+ producto  + ", estado=" + estado
				+ ", fechaRegistro=" + fechaRegistro + ", usuarioRegistro="
				+ usuarioRegistro + "]";
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
			if(!(obj instanceof DetalleTomaInventario)){
				return false;
			}else{
				if(((DetalleTomaInventario)obj).id.intValue()==this.id.intValue()){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
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

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public double getCantidadRegistrada() {
		return cantidadRegistrada;
	}

	public void setCantidadRegistrada(double cantidadRegistrada) {
		this.cantidadRegistrada = cantidadRegistrada;
	}

	public Double getCantidadVerificada() {
		return cantidadVerificada;
	}

	public void setCantidadVerificada(Double cantidadVerificada) {
		this.cantidadVerificada = cantidadVerificada;
	}

	public Double getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(Double diferencia) {
		this.diferencia = diferencia;
	}

	public TomaInventario getTomaInventario() {
		return tomaInventario;
	}

	public void setTomaInventario(TomaInventario tomaInventario) {
		this.tomaInventario = tomaInventario;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
//	public Proveedor getProveedor() {
//		return proveedor;
//	}
//
//	public void setProveedor(Proveedor proveedor) {
//		this.proveedor = proveedor;
//	}

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

}
