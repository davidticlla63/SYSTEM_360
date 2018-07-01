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
@Table(name = "toma_inventario", schema = "public")
public class TomaInventario implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "tipo",nullable=true)
	private String tipo;//INICIAL,PARCIAL,FINAL
	
	private String correlativo;
	
	@Column(name = "nombre_responsable")
	private String nombreResponsable;
	
	@Column(name = "nombre_inventariador")
	private String nombreInventariador;
	
	@Column(name = "estado_revision",nullable=true)
	private String estadoRevision;//si ya fue revisado
	
	@Column(name = "fecha_revision",nullable=true)
	private Date fechaRevision;

	@Column(name = "fecha")
	private Date fecha;

	private String estado;
	
	@Column(name = "fecha_registro")
	private Date fechaRegistro;
	
	@Column(name = "usuario_registro")
	private String usuarioRegistro;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;
	
	@ManyToOne
	@JoinColumn(name = "id_almacen")
	private Almacen almacen;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_proveedor", nullable = true)
	private Proveedor proveedor;
	
	// bi-directional many-to-one association to DetallePedidoMov
	@OneToMany(mappedBy = "tomaInventario")
	private List<DetalleTomaInventario> detalleTomaInventario;

	public TomaInventario() {
		super();
		this.id = 0 ;
		this.nombreInventariador = "";
		this.nombreResponsable = "";
		this.setEstadoRevision("NO");
		this.tipo = "PARCIAL";
		this.gestion = new Gestion();
	}
	
	@Override
	public String toString() {
		return "TomaInventario [id=" + id + ", tipo=" + tipo + ", correlativo="
				+ correlativo + ", nombreResponsable=" + nombreResponsable
				+ ", nombreInventariador=" + nombreInventariador
				+ ", estadoRevision=" + estadoRevision + ", fechaRevision="
				+ fechaRevision + ", fecha=" + fecha + ", estado=" + estado
				+ ", fechaRegistro=" + fechaRegistro + ", usuarioRegistro="
				+ usuarioRegistro + ", gestion=" + gestion
				+ ", detalleTomaInventario=" + detalleTomaInventario + "]";
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
			if(!(obj instanceof TomaInventario)){
				return false;
			}else{
				if(((TomaInventario)obj).id.intValue()==this.id.intValue()){
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

	public String getNombreResponsable() {
		return nombreResponsable;
	}

	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<DetalleTomaInventario> getDetalleTomaInventario() {
		return detalleTomaInventario;
	}

	public void setDetalleTomaInventario(List<DetalleTomaInventario> detalleTomaInventario) {
		this.detalleTomaInventario = detalleTomaInventario;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getNombreInventariador() {
		return nombreInventariador;
	}

	public void setNombreInventariador(String nombreInventariador) {
		this.nombreInventariador = nombreInventariador;
	}

	public String getEstadoRevision() {
		return estadoRevision;
	}

	public void setEstadoRevision(String estadoRevision) {
		this.estadoRevision = estadoRevision;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

}
