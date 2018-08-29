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
@Table(name="ejecutivo_comisiones" ,schema="public")
public class EjecutivoComisiones implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_ejecutivo", nullable=true)
	private Ejecutivo ejecutivo;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_nota_venta", nullable=true)
	private NotaVenta notaVenta;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_pago_comision", nullable=true)
	private PagoComision pagoComision;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_cobranza", nullable=true)
	private Cobranza cobranza;
	
	@Column(name="pagado")
	private boolean pagado;
	
	@Column(name = "importe", nullable = false)
	private Double importe;
	
	@Column(name = "egreso", nullable = true)
	private Double egreso;
	
	@Column(name = "porcentaje", nullable = false)
	private Double porcentaje;
	
	private String estado;
	
	@Transient
	private double saldo;
	
	@Transient
	private String concepto;
	
	@Transient
	private String tipoMovimiento;//VENTA,CUOTA,PAGO

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	@Column(name="fecha_modificacion",nullable=true)
	private Date fechaModificacion;

	public EjecutivoComisiones() {
		super();
		this.id = 0;	
		this.ejecutivo= new Ejecutivo();
	}
	
	@Override
	public String toString() {
		return "AlmacenEncargado [id=" + id + ", estado=" + estado
				+ ", fechaRegistro=" + fechaRegistro + ", usuarioRegistro="
				+ usuarioRegistro  + "]";
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
			if(!(obj instanceof EjecutivoComisiones)){
				return false;
			}else{
				if(((EjecutivoComisiones)obj).id==this.id){
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

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Ejecutivo getEjecutivo() {
		return ejecutivo;
	}

	public void setEjecutivo(Ejecutivo ejecutivo) {
		this.ejecutivo = ejecutivo;
	}

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public boolean isPagado() {
		return pagado;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Double getEgreso() {
		return egreso;
	}

	public void setEgreso(Double egreso) {
		this.egreso = egreso;
	}

	public PagoComision getPagoComision() {
		return pagoComision;
	}

	public void setPagoComision(PagoComision pagoComision) {
		this.pagoComision = pagoComision;
	}

}