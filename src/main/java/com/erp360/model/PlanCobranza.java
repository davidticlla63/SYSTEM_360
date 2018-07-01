package com.erp360.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name="plan_cobranza",schema="public")
public class PlanCobranza  implements java.io.Serializable {

private static final long serialVersionUID = 4260981264042548162L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="numero_pago", nullable=false)
	private Integer numeroPago;
	
	//{NC=NO_COBRADO | CO=COBRADO | PN=PENDIENTE(ESTO ES CUANDO ES RESERVA O COTIZACION)}
	@Column(name="estado_cobro", nullable=true)
	private String estadoCobro;

	@Column(name="monto_nacional", nullable=false)
	private double montoNacional;
	
	@Column(name="monto_extranjero", nullable=true)
	private double montoExtranjero;
	
	@Column(name="tipo_cambio", nullable=true)
	private double tipoCambio;
	
	@Column(name="interes_mensual", nullable=true)
	private double interesMensual;
	
	@Column(name="interes_mensual_extranjero", nullable=true)
	private double interesMensualExtranjero;
	
	@Column(name="tipo_moneda", nullable=true)
	private String tipoMoneda;
	
	@Column(name="fecha_pago", nullable=true)
	private Date fechaPago;
	
	@Column(name="dias_mora", nullable=true)
	private int diasMora;
	
	@Column(name="monto_multa", nullable=true)
	private double montoMulta;
	
	@Column(name="monto_multa_extranjera", nullable=true)
	private double montoMultaExtranjera;
	
	@Transient
	private String cobro;
	
	@ManyToOne(fetch=FetchType.LAZY )
	@JoinColumn(name="id_nota_venta", nullable=true)
	private NotaVenta notaVenta;

	//ACTIVO
	@Column(name="estado", nullable=false, length=2)
	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_registro", nullable=false)
	private Date fechaRegistro;

	@Column(name="usuario_registro", length=30)
	private String usuarioRegistro;

	public PlanCobranza() {
		super();
		this.id = 0;
		this.estadoCobro = "NC";
		this.interesMensual = 0;
		this.interesMensualExtranjero = 0;
		this.diasMora = 0;
		this.montoMulta = 0;
		this.montoMultaExtranjera = 0;
		this.cobro = "UNSELECTED";
	}

	@Override
	public String toString() {
		return "PlanCobranza [id=" + id + ", numeroPago=" + numeroPago
				+ ", estadoCobro=" + estadoCobro + ", montoNacional="
				+ montoNacional + ", montoExtranjero=" + montoExtranjero
				+ ", tipoCambio=" + tipoCambio + ", interesMensual="
				+ interesMensual + ", interesMensualExtranjero="
				+ interesMensualExtranjero + ", tipoMoneda=" + tipoMoneda
				+ ", fechaPago=" + fechaPago + ", diasMora=" + diasMora
				+ ", montoMulta=" + montoMulta + ", cobro=" + cobro
				+ ", notaVenta=" + notaVenta
				+ ", estado=" + estado + ", fechaRegistro=" + fechaRegistro
				+ ", usuarioRegistro=" + usuarioRegistro + "]";
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

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public double getMontoNacional() {
		return montoNacional;
	}

	public void setMontoNacional(double montoNacional) {
		this.montoNacional = montoNacional;
	}

	public double getMontoExtranjero() {
		return montoExtranjero;
	}

	public void setMontoExtranjero(double montoExtranjero) {
		this.montoExtranjero = montoExtranjero;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Integer getNumeroPago() {
		return numeroPago;
	}

	public void setNumeroPago(Integer numeroPago) {
		this.numeroPago = numeroPago;
	}

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public String getEstadoCobro() {
		return estadoCobro;
	}

	public void setEstadoCobro(String estadoCobro) {
		this.estadoCobro = estadoCobro;
	}
	
	public double getInteresMensual() {
		return interesMensual;
	}

	public void setInteresMensual(double interesMensual) {
		this.interesMensual = interesMensual;
	}

	public double getInteresMensualExtranjero() {
		return interesMensualExtranjero;
	}

	public void setInteresMensualExtranjero(double interesMensualExtranjero) {
		this.interesMensualExtranjero = interesMensualExtranjero;
	}

	public int getDiasMora() {
		return diasMora;
	}

	public void setDiasMora(int diasMora) {
		this.diasMora = diasMora;
	}

	public double getMontoMulta() {
		return montoMulta;
	}

	public void setMontoMulta(double montoMulta) {
		this.montoMulta = montoMulta;
	}

	public String getCobro() {
		return cobro;
	}

	public void setCobro(String cobro) {
		this.cobro = cobro;
	}

	public double getMontoMultaExtranjera() {
		return montoMultaExtranjera;
	}

	public void setMontoMultaExtranjera(double montoMultaExtranjera) {
		this.montoMultaExtranjera = montoMultaExtranjera;
	}

}
