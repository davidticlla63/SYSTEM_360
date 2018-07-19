package com.erp360.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name = "nota_venta", schema = "public")
public class NotaVenta implements Serializable {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="codigo", nullable=false )
	private String codigo;

	@Column(name="monto_total", nullable=false )
	private double montoTotal;

	@Column(name="monto_total_extranjero", nullable=true )
	private double montoTotalExtranjero;
	
	@Column(name="saldo_financiar_nacional", nullable=true )
	private double saldoFinanciarNacional;
	
	@Column(name="saldo_financiar_extranjero", nullable=true )
	private double saldoFinanciarExtranjero;

	@Column(name="tipo_cambio", nullable=true )
	private double tipoCambio;
	
	//aplicado para cotizaciones
	@Column(name="fecha_caducidad", nullable=true )
	private Date fechaCaducidad;
	
	//aplicado para reserva
	@Column(name="monto_reserva", nullable=true)
	private double montoReserva;
	
	//aplicado para reserva
	@Column(name="monto_reserva_extranjero", nullable=true )
	private double montoReservaExtranjero;

	//BOLIVIANOS , DOLAR
	@Column(name="moneda", nullable=true )
	private String moneda;
	
	//MENSUAL , QUINCENAL , SEMANAL
	@Column(name="forma_pago", nullable=true )
	private String formaPago;
	
	@Column(name="coeficiente_interes", nullable=true )
	private double coeficienteInteres;
	
	@Column(name="numero_cuotas", nullable=true )
	private Integer numeroCuotas;

	@Column(name="porcentaje_cuota_inicial", nullable=false )
	private double porcentajeCuotaInicial;

	@Column(name="cuota_inicial", nullable=false )
	private double cuotaInicial;
	
	@Column(name="cuota_inicial_extranjero", nullable=true )
	private double cuotaInicialExtranjero;

	@Column(name="concepto", nullable=false )
	private String concepto;

	@Column(name="tipo_venta", nullable=false )
	private String tipoVenta;//{CONTADO | CREDITO}

	@Column(name="estado_pago", nullable=true ) // {CO=COTIZACION | RE=RESERVA |  PN=PENDIENTE DE PAGO | PG=PAGADO COMPLETAMENTE,PGP=PAGADO PARCIALMENTE}
	private String estadoPago;
	
	@Column(name="fecha_pago_inicial", nullable=true )
	private Date fechaPagoInicial;

	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="id_cliente", nullable=false, referencedColumnName ="id")
	private Cliente cliente;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_ejecutivo", nullable=true, referencedColumnName ="id")
	private Ejecutivo ejecutivo;


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_empresa", nullable=false, referencedColumnName ="id")
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_gestion", nullable = true, referencedColumnName ="id")
	private Gestion gestion;
	
	@Column(name="fecha_venta",nullable=true )
	private Date fechaVenta;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_movimiento_caja", nullable = true)
	private MovimientoCaja movimientoCaja;
	
	@OneToMany(mappedBy="notaVenta", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<PlanCobranza> listPlanCobranza;
	
	@OneToMany(mappedBy="notaVenta", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<DetalleNotaVenta> detailSalesNotes;
	 
	@OneToMany(mappedBy="notaVenta", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<CuentaCobrar> receivables;
	
	@Size(max = 2) //AC , IN
	private String estado;

	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;

	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public NotaVenta() {
		super();
		this.id = 0;
		this.codigo = "0";
		this.montoTotal = 0;
		this.montoTotalExtranjero = 0;
		this.tipoCambio = 0;
		this.cuotaInicial= 0;
		this.cuotaInicialExtranjero = 0;
		this.estado = "AC";
		this.tipoVenta = "CREDITO";
		this.moneda = "DOLAR";
		this.ejecutivo = new Ejecutivo();
		this.porcentajeCuotaInicial = 0;
		this.concepto = "Nota de cargo por Venta de producto(s), Cuota Inicial.";
		this.formaPago = "MENSUAL";
		this.coeficienteInteres = 2.5;
		this.numeroCuotas = 3;
		this.saldoFinanciarNacional = 0;
		this.saldoFinanciarExtranjero = 0;
		this.montoReserva = 0;
		this.montoReservaExtranjero = 0;
		this.detailSalesNotes = new ArrayList<>();
		this.listPlanCobranza = new ArrayList<>(); 
	}

	@Override
	public String toString() {
		return "NotaVenta [id=" + id + ", codigo=" + codigo + ", montoTotal="
				+ montoTotal + ", montoTotalExtranjero=" + montoTotalExtranjero
				+ ", tipoCambio=" + tipoCambio + ", porcentajeCuotaInicial="
				+ porcentajeCuotaInicial + ", cuotaInicial=" + cuotaInicial
				+ ", concepto=" + concepto + ", tipoVenta=" + tipoVenta
				+ ", estadoPago=" + estadoPago + ", cliente=" + cliente
				+ ", ejecutivo=" + ejecutivo + ", empresa=" + empresa + ", gestion="
				+ gestion + ", estado=" + estado + ", fechaRegistro="
				+ fechaRegistro + ", UsuarioRegistro=" + UsuarioRegistro + "]";
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
			if(!(obj instanceof NotaVenta)){
				return false;
			}else{
				if(((NotaVenta)obj).id==this.id){
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
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return UsuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		UsuarioRegistro = usuarioRegistro;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getTipoVenta() {
		return tipoVenta;
	}

	public void setTipoVenta(String tipoVenta) {
		this.tipoVenta = tipoVenta;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public double getCuotaInicial() {
		return cuotaInicial;
	}

	public void setCuotaInicial(double cuotaInicial) {
		this.cuotaInicial = cuotaInicial;
	}

	public String getEstadoPago() {
		return estadoPago;
	}

	public void setEstadoPago(String estadoPago) {
		this.estadoPago = estadoPago;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public Ejecutivo getEjecutivo() {
		return ejecutivo;
	}

	public void setEjecutivo(Ejecutivo ejecutivo) {
		this.ejecutivo = ejecutivo;
	}

	public double getPorcentajeCuotaInicial() {
		return porcentajeCuotaInicial;
	}

	public void setPorcentajeCuotaInicial(double porcentajeCuotaInicial) {
		this.porcentajeCuotaInicial = porcentajeCuotaInicial;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public double getMontoTotalExtranjero() {
		return montoTotalExtranjero;
	}

	public void setMontoTotalExtranjero(double montoTotalExtranjero) {
		this.montoTotalExtranjero = montoTotalExtranjero;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public double getCoeficienteInteres() {
		return coeficienteInteres;
	}

	public void setCoeficienteInteres(double coeficienteInteres) {
		this.coeficienteInteres = coeficienteInteres;
	}

	public Integer getNumeroCuotas() {
		return numeroCuotas;
	}

	public void setNumeroCuotas(Integer numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public Date getFechaPagoInicial() {
		return fechaPagoInicial;
	}

	public void setFechaPagoInicial(Date fechaPagoInicial) {
		this.fechaPagoInicial = fechaPagoInicial;
	}
	
	public double getCuotaInicialExtranjero() {
		return cuotaInicialExtranjero;
	}

	public void setCuotaInicialExtranjero(double cuotaInicialExtranjero) {
		this.cuotaInicialExtranjero = cuotaInicialExtranjero;
	}

	public MovimientoCaja getMovimientoCaja() {
		return movimientoCaja;
	}

	public void setMovimientoCaja(MovimientoCaja movimientoCaja) {
		this.movimientoCaja = movimientoCaja;
	}

	public double getSaldoFinanciarNacional() {
		return saldoFinanciarNacional;
	}

	public void setSaldoFinanciarNacional(double saldoFinanciarNacional) {
		this.saldoFinanciarNacional = saldoFinanciarNacional;
	}

	public double getSaldoFinanciarExtranjero() {
		return saldoFinanciarExtranjero;
	}

	public void setSaldoFinanciarExtranjero(double saldoFinanciarExtranjero) {
		this.saldoFinanciarExtranjero = saldoFinanciarExtranjero;
	}

	public List<PlanCobranza> getListPlanCobranza() {
		return listPlanCobranza;
	}

	public void setListPlanCobranza(List<PlanCobranza> listPlanCobranza) {
		this.listPlanCobranza = listPlanCobranza;
	}

	public List<DetalleNotaVenta> getDetailSalesNotes() {
		return detailSalesNotes;
	}

	public void setDetailSalesNotes(List<DetalleNotaVenta> detailSalesNotes) {
		this.detailSalesNotes = detailSalesNotes;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public double getMontoReserva() {
		return montoReserva;
	}

	public void setMontoReserva(double montoReserva) {
		this.montoReserva = montoReserva;
	}

	public double getMontoReservaExtranjero() {
		return montoReservaExtranjero;
	}

	public void setMontoReservaExtranjero(double montoReservaExtranjero) {
		this.montoReservaExtranjero = montoReservaExtranjero;
	}

	public List<CuentaCobrar> getReceivables() {
		return receivables;
	}

	public void setReceivables(List<CuentaCobrar> receivables) {
		this.receivables = receivables;
	}

	public Date getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

}


