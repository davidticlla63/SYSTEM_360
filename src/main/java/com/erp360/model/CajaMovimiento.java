

package com.erp360.model;
/**
 * @author david
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import com.erp360.enums.TipoMovimiento;
import com.erp360.enums.TipoPago;








/**
 * 
 * @author david
 *
 */
@Entity
@Table(name = "caja_movimiento", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class CajaMovimiento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_caja_sesion", nullable = false)
	private CajaSesion cajaSesion;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_sucursal", nullable = false)
	private Sucursal sucursal;
	
	@Column(name = "descripcion", nullable = true)
	private String descripcion;
	
	@Column(name = "ducumento", nullable = true)
	private String ducumento;

	@Column(name = "numero_tarjeta", nullable = true)
	private String numeroTarjeta;
	
	@Column(name = "numero_cheque", nullable = true)
	private String numeroCheque;
	
	@Size(max = 2)// AC , IN , RM
	@Column(name = "estado", nullable = false)
	private String estado;

	@Size(max = 2)// E , I 
	@Column(name = "tipo", nullable = false)
	private String tipo;
	

	@Column(name = "tipo_movimiento", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoMovimiento tipoMovimiento ;
	
	@Column(name = "tipo_pago", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoPago tipoPago;
	
	@Column(name = "monto", nullable = false)
	private Double monto;
	
	@Column(name = "monto_extranjero", nullable = false)
	private Double montoExtranjero;
	
	@Transient
	private Double montoRecibido;
	
	@Transient
	private Double cambio;

	@Column(name = "tipo_cambio", nullable = false)
	private double tipoCambio;

	@Column(name = "procesada", nullable = true)
	private boolean procesada;

	@Column(name = "usuario_registro", nullable = false)
	private String usuarioRegistro;

	@Column(name = "fecha_registro", nullable = false)
	private Date fechaRegistro;

	@Column(name = "fecha_modificacion", nullable = true)
	private Date fechaModificacion;

	@Column(name = "movimiento_interno", nullable = true)
	private boolean movimientoInterno;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_plan_cuenta", nullable = true)
//	private PlanCuenta planCuenta;
	
	@Column(name = "razon_social", nullable = true)
	private String razonSocial;
	
	@Column(name = "monto_literal", nullable = true)
	private String montoLiteral;
	
//	@Column(name = "saldo_nacional", nullable = false)
//	private Double saldoNacional;
//	
//	@Column(name = "saldo_extranjero", nullable = false)
//	private Double saldoExtranjero;
//	
	@Transient
	private Concepto concepto;
	
	@OneToMany(mappedBy = "cajaMovimiento", orphanRemoval=true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CajaMovimientoDetalle> listaCajaMovimientoDetalles = new ArrayList<CajaMovimientoDetalle>();
	
	public CajaMovimiento() {
		this.id = 0;
		this.estado = "AC";
		this.usuarioRegistro = "";
		this.monto=new Double(0);
		this.montoExtranjero=new Double(0);
		this.montoRecibido=new Double(0);
		this.cambio=new Double(0);
		this.concepto= new Concepto();
//		this.saldoExtranjero=new Double(0);
//		this.saldoNacional= new Double(0);
		this.tipo="E";
		this.tipoPago=TipoPago.EFE;
		this.tipoMovimiento=TipoMovimiento.PAG;
		this.movimientoInterno=true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (!(obj instanceof CajaMovimiento)) {
				return false;
			} else {
				if (((CajaMovimiento) obj).id == this.id) {
					return true;
				} else {
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

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	public boolean isProcesada() {
		return procesada;
	}
	public void setProcesada(boolean procesada) {
		this.procesada = procesada;
	}
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getMonto() {
		return monto;
	}


	public void setMonto(Double monto) {
		this.monto = monto;
	}


	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}


	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public CajaSesion getCajaSesion() {
		return cajaSesion;
	}

	public void setCajaSesion(CajaSesion cajaSesion) {
		this.cajaSesion = cajaSesion;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	

	public Double getMontoExtranjero() {
		return montoExtranjero;
	}

	public void setMontoExtranjero(Double montoExtranjero) {
		this.montoExtranjero = montoExtranjero;
	}

	public Double getMontoRecibido() {
		return montoRecibido;
	}

	public void setMontoRecibido(Double montoRecibido) {
		this.montoRecibido = montoRecibido;
	}

	public Double getCambio() {
		return cambio;
	}

	public void setCambio(Double cambio) {
		this.cambio = cambio;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public boolean isMovimientoInterno() {
		return movimientoInterno;
	}

	public void setMovimientoInterno(boolean movimientoInterno) {
		this.movimientoInterno = movimientoInterno;
	}

//	public PlanCuenta getPlanCuenta() {
//		return planCuenta;
//	}
//
//	public void setPlanCuenta(PlanCuenta planCuenta) {
//		this.planCuenta = planCuenta;
//	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getMontoLiteral() {
		return montoLiteral;
	}

	public void setMontoLiteral(String montoLiteral) {
		this.montoLiteral = montoLiteral;
	}

	public List<CajaMovimientoDetalle> getListaCajaMovimientoDetalles() {
		return listaCajaMovimientoDetalles;
	}

	public void setListaCajaMovimientoDetalles(
			List<CajaMovimientoDetalle> listaCajaMovimientoDetalles) {
		this.listaCajaMovimientoDetalles = listaCajaMovimientoDetalles;
	}

	public String getDucumento() {
		return ducumento;
	}

	public void setDucumento(String ducumento) {
		this.ducumento = ducumento;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

//	public Double getSaldoNacional() {
//		return saldoNacional;
//	}
//
//	public void setSaldoNacional(Double saldoNacional) {
//		this.saldoNacional = saldoNacional;
//	}
//
//	public Double getSaldoExtranjero() {
//		return saldoExtranjero;
//	}
//
//	public void setSaldoExtranjero(Double saldoExtranjero) {
//		this.saldoExtranjero = saldoExtranjero;
//	}
//
	}
