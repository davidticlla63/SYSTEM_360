package com.erp360.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orden_salida", schema = "public")
public class OrdenSalida implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	//Test
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String correlativo;
	
	private String motivoSalida ;
	
	@Column(name = "fecha_pedido", nullable = true)
	private Date fechaPedido;

	@Column(name = "fecha_aprobacion", nullable = true)
	private Date fechaAprobacion;
	
	@Column(name = "total_importe")
	private double totalImporte;
	
	@Column(name = "total_importe_extranjero", nullable = true)
	private double totalImporteExtranjero;
	
	@Column(name = "moneda", nullable = true)
	private String moneda;
	
	//length=225
	@Column(name = "observacion", nullable = true)
	private String observacion;

	// bi-directional many-to-one association 
	@OneToMany(mappedBy = "ordenSalida")
	private List<DetalleOrdenSalida> detalleOrdenSalida;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_almacen", nullable = true)
	private Almacen almacen;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_nota_venta", nullable = true)
	private NotaVenta notaVenta;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_orden_traspaso", nullable = true)
	private OrdenTraspaso ordenTraspaso;
	
	private String estado;
	
	@Column(name = "fecha_registro")
	private Date fechaRegistro;
	
	@Column(name = "usuario_registro")
	private String usuarioRegistro;

	public OrdenSalida() {
		this.id = 0 ;
		this.almacen = new Almacen();
		this.observacion = "Ninguna";
		this.motivoSalida = "";
		this.totalImporte = 0;
		this.totalImporteExtranjero = 0;
		this.moneda = "DOLAR";
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}
	
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
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

	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	
	public double getTotalImporte() {
		return totalImporte;
	}

	public void setTotalImporte(double totalImporte) {
		this.totalImporte = totalImporte;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}
	
	public List<DetalleOrdenSalida> getDetalleOrdenSalida() {
		return detalleOrdenSalida;
	}

	public void setDetalleOrdenSalida(List<DetalleOrdenSalida> detalleOrdenSalida) {
		this.detalleOrdenSalida = detalleOrdenSalida;
	}

	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getMotivoSalida() {
		return motivoSalida;
	}

	public void setMotivoSalida(String motivoSalida) {
		this.motivoSalida = motivoSalida;
	}

	public double getTotalImporteExtranjero() {
		return totalImporteExtranjero;
	}

	public void setTotalImporteExtranjero(double totalImporteExtranjero) {
		this.totalImporteExtranjero = totalImporteExtranjero;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public OrdenTraspaso getOrdenTraspaso() {
		return ordenTraspaso;
	}

	public void setOrdenTraspaso(OrdenTraspaso ordenTraspaso) {
		this.ordenTraspaso = ordenTraspaso;
	}

}
