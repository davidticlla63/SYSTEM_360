package com.erp360.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import com.erp360.enums.TipoMovimiento;
import com.erp360.enums.TipoPago;
import com.erp360.interfaces.ICajaDao;
import com.erp360.interfaces.ICajaMovimientoDao;
import com.erp360.interfaces.ICajaSesionDao;
import com.erp360.model.Caja;
import com.erp360.model.CajaMovimiento;
import com.erp360.model.CajaSesion;
import com.erp360.model.Sucursal;
import com.erp360.util.CajaIngreso;
import com.erp360.util.FacesUtil;
import com.erp360.util.Time;

@Stateless
public class CajaMovimientoDao extends DataAccessObjectGeneric<CajaMovimiento>
		implements ICajaMovimientoDao {

	private @Inject ICajaDao cajaDao;
	private @Inject ICajaSesionDao cajaOperacionDao;
//	private @Inject IComprobanteDao comprobanteDao;

	public CajaMovimientoDao() {
		super(CajaMovimiento.class);
	}

	public String generateDeliveryInId(int id) {

		if (id < 10) {
			return "VTA-000" + id;

		} else if (id >= 10 && id < 100) {
			return "VTA-00" + id;

		} else if (id >= 100 && id < 1000) {
			return "VTA-0" + id;

		} else {
			return "VTA-" + id;

		}
	}

	public CajaMovimiento create(CajaMovimiento especialidad) {
		return super.create(especialidad);
	}

	@Override
	public CajaMovimiento update(CajaMovimiento especialidad) {
		return super.update(especialidad);
	}

	@Override
	public CajaMovimiento registrar(CajaMovimiento examen) {
		try {
			beginTransaction();
			examen = super.create(examen);
			Caja c = examen.getCajaSesion().getCaja();
			c.setOpcion("AB");
			cajaDao.modificar(c);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "CajaMovimiento");
			return examen;
		} catch (Exception e) {
			String cause = e.getMessage();
			System.out.println("Error cause " + cause);
			if (cause
					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			} else {
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return null;
		}
	}
	
	
//	@Override
//	public CajaMovimiento registrar(CajaMovimiento examen,Comprobante comprobante) {
//		try {
//			beginTransaction();
//			examen = super.create(examen);
//			
//			Caja c = examen.getCajaSesion().getCaja();
//			c.setOpcion("AB");
//			cajaDao.modificar(c);
//			commitTransaction();
//			FacesUtil.infoMessage("Registro Correcto", "CajaMovimiento");
//			return examen;
//		} catch (Exception e) {
//			String cause = e.getMessage();
//			System.out.println("Error cause " + cause);
//			if (cause
//					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
//				FacesUtil.errorMessage("Ya existe un registro igual.");
//			} else {
//				FacesUtil.errorMessage("Error al registrar");
//			}
//			rollbackTransaction();
//			return null;
//		}
//	}
//	
	/***
	 * 
	 */
	@Override
	public CajaMovimiento registrarMovimientoDeCaja(CajaSesion cajaSesion,TipoMovimiento tipoMovimiento,String tipo,double total,TipoPago tipoPago) {
		CajaMovimiento cm = new CajaMovimiento();
		cm.setCajaSesion(cajaSesion);
		cm.setEstado("AC");
		cm.setFechaRegistro(new Date());
		cm.setMonto(total);
		cm.setProcesada(false);
		cm.setTipo("I");
		cm.setTipoMovimiento(TipoMovimiento.VEN);
		cm.setTipoPago(tipoPago);
		CajaSesion cajaOperacion = cajaOperacionDao
				.RetornarPorCaja(cajaSesion.getCaja());
		cm.setCajaSesion(cajaOperacion);
		return super.create(cm);
	}

	public boolean modificar(CajaMovimiento examen) {
		try {

			beginTransaction();
			examen = update(examen);
			commitTransaction();
			FacesUtil.infoMessage("Modificacion Correcta", "Nota de Venta ");
			return true;
		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause
					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			} else {
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}

	}

	/*
	 * @Override public VentaNotaVenta modificar(VentaNotaVenta examen,
	 * List<VentaNotaVentaDetalle> listCotizacionDetalle) { try {
	 * examen.setListCotizacionDetalles(new ArrayList<VentaNotaVentaDetalle>());
	 * beginTransaction(); examen = update(examen);
	 * cotizacionDetalleDao.deleteDetail(examen); for (VentaNotaVentaDetalle
	 * cotizacionDetalle : listCotizacionDetalle) {
	 * cotizacionDetalle.setCotizacion(examen); cotizacionDetalle =
	 * cotizacionDetalleDao .create(cotizacionDetalle); } commitTransaction();
	 * FacesUtil.infoMessage("Modificación Correcta", "VentaNotaVenta " +
	 * examen.toString()); return examen; } catch (Exception e) { String cause =
	 * e.getMessage(); if (cause .contains(
	 * "org.hibernate.exception.ConstraintViolationException: could not execute statement"
	 * )) { FacesUtil.errorMessage("Ya existe un registro igual."); } else {
	 * FacesUtil.errorMessage("Error al modificar"); } rollbackTransaction();
	 * return null; } }
	 */

	@Override
	public boolean eliminar(CajaMovimiento especialidad) {
		try {
			beginTransaction();
			CajaMovimiento bar = update(especialidad);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "CajaMovimiento "
					+ especialidad.toString());
			return bar != null ? true : false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.teds.spaps.interfaces.dao.ICajaMovimientoDao#procesar(com.teds.spaps
	 * .model.CajaMovimiento)
	 */
	@Override
	public boolean procesar(CajaMovimiento caja) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.teds.spaps.interfaces.dao.ICajaDao#RetornarPorId(java.lang.Integer)
	 */
	@Override
	public CajaMovimiento RetornarPorId(Integer id) {
		return findById(id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.teds.spaps.interfaces.dao.ICajaDao#RetornarOnCompletePorCompania(
	 * com.teds.spaps.model.Compania, java.lang.String)
	 */
//	@Override
//	public List<CajaMovimiento> RetornarOnCompletePorCompania(
//			Compania compania, String nombre) {
//
//		return null;
//	}

	@Override
	public List<CajaIngreso> listarPorSesionEIngresosYModoDePago(
			CajaSesion cajaSesion) {
		String query = "select SUM(c.monto), c.tipoPago from CajaMovimiento c where c.cajaSesion.id= :idCajaSesion and c.tipo='I' GROUP BY c.tipoPago";
		Query q = getEntityManager().createQuery(query);
		//q.setParameter("idcaja", cajaSesion.getCaja().getId());
		q.setParameter("idCajaSesion", cajaSesion.getId());
		List<Object[]> listaObjetos = q.getResultList();
		List<CajaIngreso> cajaIngreso = new ArrayList<>();
		for (Object[] objects : listaObjetos) {
			CajaIngreso ci = new CajaIngreso();
			Double x = (Double) objects[0];
			ci.setSuma(x);
			TipoPago cm = (TipoPago) objects[1];
			ci.setTipoPago(cm);
			cajaIngreso.add(ci);
		}
		List<CajaIngreso> cx=new ArrayList<>();
		for (TipoPago tP : TipoPago.values()) {
			boolean f = false;
			for (CajaIngreso cajaIngreso2 : cajaIngreso) {
				
				if (cajaIngreso2.getTipoPago().equals(tP)) {
					f = true;
				}
				
			}
			if (!f) {
                CajaIngreso cp=new CajaIngreso();
                cp.setSuma(0.0);
                cp.setTipoPago(tP);
                cx.add(cp);
				}
		}
		for (CajaIngreso cajaIngreso2 : cx) {
			cajaIngreso.add(cajaIngreso2);
		}
		return cajaIngreso;
	}

	@Override
	public List<CajaMovimiento> listarMovimientosPorSesion(CajaSesion cajaSesion) {		
		return findAllActiveByParameterAsc("cajaSesion", cajaSesion.getId());
	}
	
	

	@Override
	public List<CajaMovimiento> obtenerMovimientosSinComprobantes(CajaSesion cajaSesion) {
		String query = "select em from CajaMovimiento  em  where   em.cajaSesion.id=" + cajaSesion.getId()
				+ " and em.movimientoInterno=true and em.tipo='E' and em.id not in (select mc.movimiento.id from  MovimientoComprobante mc ) order by em.id desc";
		return executeQueryResulList(query);
	}
	
	@Override
	public List<CajaMovimiento> obtenerPorSucursalEntreFechas(Sucursal sucursal,Caja caja,
			Date fechaini, Date fechafin) {
		String query = "select em from CajaMovimiento  em  where   em.sucursal.id=" + sucursal.getId()
				+ " and em.cajaSesion.caja.id=" + caja.getId()+" and  to_number(to_char(em.fechaRegistro  ,'YYYYMMDD'), '999999999')>="
				+ Time.obtenerFormatoYYYYMMDD(fechaini)
				+ " and  to_number(to_char(em.fechaRegistro ,'YYYYMMDD'), '999999999')<="
				+ Time.obtenerFormatoYYYYMMDD(fechafin) + "  order by em.id desc";
		return executeQueryResulList(query);
	}
	
	@Override
	public List<CajaMovimiento> obtenerPorSessionCajaEntreFechas(CajaSesion cajaSesion,
			Date fechaini, Date fechafin) {
		// return findAllActiveForThwoDatesAndThwoObject("sucursal",
		// sucursal.getId(), "fechaRegistro", fechaini, "fechaRegistro",
		// fechafin);
		return findAllActiveForThwoDatesAndThwoObjectStateDiferent("cajaSesion",
				cajaSesion.getId(), "fechaRegistro", fechaini, "fechaRegistro",
				fechafin, "estado", "RM");
	}
	
	@Override
	public List<CajaIngreso> listarPorSesionEgresosYModoDePago(
			CajaSesion cajaSesion) {
		String query = "select SUM(c.monto), c.tipoPago from CajaMovimiento c where c.cajaSesion.id= :idCajaSesion and c.tipo='E' GROUP BY c.tipoPago";
		Query q = getEntityManager().createQuery(query);
		//q.setParameter("idcaja", cajaSesion.getCaja().getId());
		q.setParameter("idCajaSesion", cajaSesion.getId());
		List<Object[]> listaObjetos = q.getResultList();
		List<CajaIngreso> cajaIngreso = new ArrayList<>();
		for (Object[] objects : listaObjetos) {
			CajaIngreso ci = new CajaIngreso();
			Double x = (Double) objects[0];
			ci.setSuma(x);
			TipoPago cm = (TipoPago) objects[1];
			ci.setTipoPago(cm);
			cajaIngreso.add(ci);
		}
		return cajaIngreso;
	}

}
