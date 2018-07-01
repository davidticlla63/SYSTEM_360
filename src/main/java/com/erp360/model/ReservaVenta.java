package com.erp360.model;

import java.io.Serializable;
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
@Table(name = "reserva_venta", schema = "public")
public class ReservaVenta implements Serializable {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="codigo", nullable=false )
	private String codigo;
	
	@Column(name="monto_reserva", nullable=true )
	private double montoReserva;
	
	@Column(name="monto_reserva_extranjero", nullable=true )
	private double montoReservaExtranjero;
	
	@Column(name="monto_total", nullable=false )
	private double montoTotal;
	
	@Column(name="monto_total_extranjero", nullable=true )
	private double montoTotalExtranjero;
	
	@Column(name="tipo_cambio", nullable=true )
	private double tipoCambio;
	
	//BOLIVIANOS , DOLAR
	@Column(name="moneda", nullable=true )
	private String moneda;
	
	//MENSUAL , QUINCENAL , SEMANAL
	@Column(name="forma_pago", nullable=true )
	private String formaPago;
	
	@Column(name="porcentaje_cuota_inicial", nullable=true )
	private double porcentajeCuotaInicial;
	
	@Column(name="cuota_inicial", nullable=true )
	private double cuotaInicial;
	
	@Column(name="cuota_inicial_extranjero", nullable=true )
	private double cuotaInicialExtranjero;
	
	@Column(name="concepto", nullable=false )
	private String concepto;
	
	@Column(name="tipo_venta", nullable=false )
	private String tipoVenta;//{CONTADO | CREDITO}
	
	@Column(name="coeficiente_interes", nullable=true )
	private double coeficienteInteres;
	
	@Column(name="numero_cuotas", nullable=true )
	private Integer numeroCuotas;
	
	@Column(name="estado_pago", nullable=true ) // {PN=PENDIENTE | PG=PAGADO}
	private String estadoPago;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_cliente", nullable=false)
	private Cliente cliente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_encargado_venta", nullable=true)
	private EncargadoVenta encargadoVenta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;
	
	@Column(name="fecha_pago_inicial", nullable=true )
	private Date fechaPagoInicial;
	
	@OneToMany(mappedBy="reservaVenta", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DetalleReservaVenta> listDetalleReservaVenta ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_movimiento_caja", nullable = true)
	private MovimientoCaja movimientoCaja;
	
	@Size(max = 2) //PN ,AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public ReservaVenta() {
		super();
		this.id = 0;
		this.codigo = "0";
		this.montoTotal = 0;
		this.montoTotalExtranjero = 0;
		this.montoReservaExtranjero = 5;
		this.tipoCambio = 0;
		this.montoReserva = 0;
		this.cuotaInicial= 0;
		this.cuotaInicialExtranjero = 0;
		this.estado = "PN";
		this.tipoVenta = "CREDITO";
		this.estadoPago = "PN";
		this.encargadoVenta = new EncargadoVenta();
		this.porcentajeCuotaInicial = 0;
		this.moneda = "DOLAR";
		this.concepto = "Nota de cargo por reserva de producto(s)";
		this.formaPago = "MENSUAL";
		this.coeficienteInteres = 2.5;
		this.numeroCuotas = 3;
	}
	
	@Override
	public String toString() {
		return "ReservaVenta [id=" + id + ", codigo=" + codigo
				+ ", montoReserva=" + montoReserva
				+ ", montoReservaExtranjero=" + montoReservaExtranjero
				+ ", montoTotal=" + montoTotal + ", montoTotalExtranjero="
				+ montoTotalExtranjero + ", tipoCambio=" + tipoCambio
				+ ", moneda=" + moneda + ", formaPago=" + formaPago
				+ ", porcentajeCuotaInicial=" + porcentajeCuotaInicial
				+ ", cuotaInicial=" + cuotaInicial
				+ ", cuotaInicialExtranjero=" + cuotaInicialExtranjero
				+ ", concepto=" + concepto + ", tipoVenta=" + tipoVenta
				+ ", coeficienteInteres=" + coeficienteInteres
				+ ", numeroCuotas=" + numeroCuotas + ", estadoPago="
				+ estadoPago + ", cliente=" + cliente + ", encargadoVenta="
				+ encargadoVenta + ", empresa=" + empresa + ", gestion="
				+ gestion + ", fechaPagoInicial=" + fechaPagoInicial
				+ ", listDetalleReservaVenta=" + listDetalleReservaVenta
				+ ", movimientoCaja=" + movimientoCaja + ", estado=" + estado
				+ ", fechaRegistro=" + fechaRegistro + ", UsuarioRegistro="
				+ UsuarioRegistro + "]";
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
			if(!(obj instanceof ReservaVenta)){
				return false;
			}else{
				if(((ReservaVenta)obj).id==this.id){
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

	public EncargadoVenta getEncargadoVenta() {
		return encargadoVenta;
	}

	public void setEncargadoVenta(EncargadoVenta encargadoVenta) {
		this.encargadoVenta = encargadoVenta;
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

	public List<DetalleReservaVenta> getListDetalleReservaVenta() {
		return listDetalleReservaVenta;
	}

	public void setListDetalleReservaVenta(List<DetalleReservaVenta> listDetalleReservaVenta) {
		this.listDetalleReservaVenta = listDetalleReservaVenta;
	}

	public double getMontoReserva() {
		return montoReserva;
	}

	public void setMontoReserva(double montoReserva) {
		this.montoReserva = montoReserva;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public double getMontoReservaExtranjero() {
		return montoReservaExtranjero;
	}

	public void setMontoReservaExtranjero(double montoReservaExtranjero) {
		this.montoReservaExtranjero = montoReservaExtranjero;
	}

	public double getCuotaInicialExtranjero() {
		return cuotaInicialExtranjero;
	}

	public void setCuotaInicialExtranjero(double cuotaInicialExtranjero) {
		this.cuotaInicialExtranjero = cuotaInicialExtranjero;
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

	public MovimientoCaja getMovimientoCaja() {
		return movimientoCaja;
	}

	public void setMovimientoCaja(MovimientoCaja movimientoCaja) {
		this.movimientoCaja = movimientoCaja;
	}

}


