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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "orden_ingreso", schema = "public",uniqueConstraints = @UniqueConstraint(columnNames={"correlativo","id_gestion"}))
public class OrdenIngreso implements Serializable{

	private static final long serialVersionUID = -5245389763015492273L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String correlativo;//000001 = 6 digitos
	
	private String observacion;
	
	private String motivoIngreso;
	
	@Column(name = "tipo_documento")
	private String tipoDocumento;
	
	@Column(name = "fecha_documento", nullable = true)
	private Date fechaDocumento;

	@Column(name = "numero_documento")
	private String numeroDocumento;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;

	private String estado;
	
	@Column(name = "fecha_registro")
	private Date fechaRegistro;
	
	@Column(name = "total_importe")
	private double totalImporte;
	
	@Column(name="por_pagar",nullable=true )
	private boolean porPagar;
	
	@Column(name = "usuario_registro")
	private String usuarioRegistro;

	@Column(name = "fecha_aprobacion")
	private Date fechaAprobacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_proveedor", nullable = true)
	private Proveedor proveedor;

	// bi-directional many-to-one association to DetallePedidoMov
	@OneToMany(mappedBy = "ordenIngreso", fetch=FetchType.LAZY)
	private List<DetalleOrdenIngreso> detalleOrdenIngreso;

	@ManyToOne
	@JoinColumn(name = "id_usuario_aprobacion")
	private Usuario usuarioAprobacion;	

	@ManyToOne
	@JoinColumn(name = "id_almacen")
	private Almacen almacen;

	public OrdenIngreso() {
		this.id = 0 ;
		this.tipoDocumento = "SIN DOCUMENTO";
		this.motivoIngreso = "COMPRA";
		this.almacen = new Almacen();
		this.gestion = new Gestion();
		this.observacion = "Ninguna";
		this.numeroDocumento = "0";
		this.porPagar = false;
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
			if(!(obj instanceof OrdenIngreso)){
				return false;
			}else{
				if(((OrdenIngreso)obj).id.intValue()==this.id.intValue()){
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

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Usuario getUsuarioAprobacion() {
		return usuarioAprobacion;
	}

	public void setUsuarioAprobacion(Usuario usuarioAprobacion) {
		this.usuarioAprobacion = usuarioAprobacion;
	}

	public Date getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
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

	public List<DetalleOrdenIngreso> getDetalleOrdenIngreso() {
		return detalleOrdenIngreso;
	}

	public void setDetalleOrdenIngreso(List<DetalleOrdenIngreso> detalleOrdenIngreso) {
		this.detalleOrdenIngreso = detalleOrdenIngreso;
	}

	public String getMotivoIngreso() {
		return motivoIngreso;
	}

	public void setMotivoIngreso(String motivoIngreso) {
		this.motivoIngreso = motivoIngreso;
	}

	public Date getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(Date fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public boolean isPorPagar() {
		return porPagar;
	}

	public void setPorPagar(boolean porPagar) {
		this.porPagar = porPagar;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

}
