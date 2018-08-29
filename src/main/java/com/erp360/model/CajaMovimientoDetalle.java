

package com.erp360.model;
/**
 * @author david
 */
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;








/**
 * 
 * @author david
 *
 */
@Entity
@Table(name = "caja_movimiento_detalle", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class CajaMovimientoDetalle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_caja_movimiento", nullable = false)
	private CajaMovimiento cajaMovimiento;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_sucursal", nullable = false)
	private Sucursal sucursal;
	
	@Column(name = "descripcion", nullable = true)
	private String descripcion;

	
	
	@Size(max = 2)// AC , IN , RM
	@Column(name = "estado", nullable = false)
	private String estado;

	
	
	@Column(name = "monto", nullable = false)
	private Double monto;
	
	@Column(name = "monto_extranjero", nullable = false)
	private Double montoExtranjero;
	
	

	@Column(name = "tipo_cambio", nullable = false)
	private double tipoCambio;

	@Column(name = "usuario_registro", nullable = false)
	private String usuarioRegistro;

	@Column(name = "fecha_registro", nullable = false)
	private Date fechaRegistro;

	@Column(name = "fecha_modificacion", nullable = true)
	private Date fechaModificacion;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_nota_venta", nullable = true)
	private NotaVenta notaVenta;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cobranza", nullable = true)
	private Cobranza cobranza;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_pago_comision", nullable = true)
	private PagoComision pagoComision;
	
	public CajaMovimientoDetalle() {
		this.id = 0;
		this.estado = "AC";
		this.usuarioRegistro = "";
		this.monto=new Double(0);
		this.montoExtranjero=new Double(0);
		
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
			if (!(obj instanceof CajaMovimientoDetalle)) {
				return false;
			} else {
				if (((CajaMovimientoDetalle) obj).id == this.id) {
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


	

	public Double getMonto() {
		return monto;
	}


	public void setMonto(Double monto) {
		this.monto = monto;
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

	
	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}	

	public CajaMovimiento getCajaMovimiento() {
		return cajaMovimiento;
	}

	public void setCajaMovimiento(CajaMovimiento cajaMovimiento) {
		this.cajaMovimiento = cajaMovimiento;
	}

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}

	public PagoComision getPagoComision() {
		return pagoComision;
	}

	public void setPagoComision(PagoComision pagoComision) {
		this.pagoComision = pagoComision;
	}

	}
