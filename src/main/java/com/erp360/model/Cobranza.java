package com.erp360.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name="cobranza",schema="public",uniqueConstraints = @UniqueConstraint(columnNames={"codigo"}))
public class Cobranza  implements java.io.Serializable {

private static final long serialVersionUID = 4260981264042548162L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="codigo", nullable=false)
	private String codigo;
	
	@Column(name="monto_nacional", nullable=false)
	private double montoNacional;
	
	@Column(name="monto_extranjero", nullable=true)
	private double montoExtranjero;
	
	@Column(name="tipo_cambio", nullable=true)
	private double tipoCambio;
	
	@Column(name="tipo_moneda", nullable=true)
	private String tipoMoneda;
	
	@Column(name="glosa", nullable=false)
	private String glosa;
	
	@Column(name="fecha_cobranza", nullable=true)
	private Date fechaCobranza;
	
	@ManyToOne(fetch=FetchType.EAGER )
	@JoinColumn(name="id_cuenta_cobrar", nullable=true )
	private CuentaCobrar cuentaCobrar;
	
	@ManyToOne(fetch=FetchType.LAZY )
	@JoinColumn(name="id_gestion", nullable=true )
	private Gestion gestion;
	
	@OneToMany(mappedBy="cobranza", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<DetalleCobranza> detalleCobranzas;

	@Column(name="estado", nullable=false, length=2)
	private String estado;

	@Column(name="fecha_registro", nullable=false)
	private Date fechaRegistro;

	@Column(name="usuario_registro", length=30)
	private String usuarioRegistro;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_movimiento_caja", nullable = true)
	private MovimientoCaja movimientoCaja;

	public Cobranza() {
		super();
		this.id = 0;
		this.glosa = "ninguno";
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
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getGlosa() {
		return glosa;
	}

	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}

	public List<DetalleCobranza> getDetalleCobranzas() {
		return detalleCobranzas;
	}

	public void setDetalleCobranzas(List<DetalleCobranza> detalleCobranzas) {
		this.detalleCobranzas = detalleCobranzas;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public CuentaCobrar getCuentaCobrar() {
		return cuentaCobrar;
	}

	public void setCuentaCobrar(CuentaCobrar cuentaCobrar) {
		this.cuentaCobrar = cuentaCobrar;
	}

	public Date getFechaCobranza() {
		return fechaCobranza;
	}

	public void setFechaCobranza(Date fechaCobranza) {
		this.fechaCobranza = fechaCobranza;
	}

	public MovimientoCaja getMovimientoCaja() {
		return movimientoCaja;
	}

	public void setMovimientoCaja(MovimientoCaja movimientoCaja) {
		this.movimientoCaja = movimientoCaja;
	}

}
