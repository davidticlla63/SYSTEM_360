package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import com.erp360.interfaces.ICajaDao;
import com.erp360.interfaces.ICajaSesionDao;
import com.erp360.model.Caja;
import com.erp360.model.CajaSesion;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;



@Stateless
public class CajaSesionDao extends DataAccessObjectGeneric<CajaSesion>
implements ICajaSesionDao {

	private @Inject ICajaDao cajaDao;
//	private @Inject IComprobanteDao comprobanteDao;

	public CajaSesionDao() {
		super(CajaSesion.class);
	}

	public String generateDeliveryInId(int id) {

		if (id < 10) {
			return "CAJA-000" + id;

		} else if (id >= 10 && id < 100) {
			return "CAJA-00" + id;

		} else if (id >= 100 && id < 1000) {
			return "CAJA-0" + id;

		} else {
			return "CAJA-" + id;

		}
	}


	public CajaSesion create(CajaSesion especialidad) {
		return super.create(especialidad);
	}

	@Override
	public CajaSesion update(CajaSesion especialidad) {
		return super.update(especialidad);
	}


	@Override
	public CajaSesion registrar(CajaSesion examen) {
		try {
			beginTransaction();
			examen = super.create(examen);
			Caja c=examen.getCaja();
			c.setOpcion("AB");
			cajaDao.modificar(c);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "CajaSesion");
			return examen;
		} catch (Exception e) {
			String cause = e.getMessage();
			System.out.println("Error cause "+cause);
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
//	public CajaSesion registrar(CajaSesion examen,Comprobante comprobante) {
//		try {
//			beginTransaction();
//			examen = super.create(examen);
//			comprobante = comprobanteDao.create(comprobante);
//			Caja c=examen.getCaja();
//			c.setOpcion("AB");
//			cajaDao.modificar(c);
//			commitTransaction();
//			FacesUtil.infoMessage("Registro Correcto", "CajaSesion");
//			return examen;
//		} catch (Exception e) {
//			String cause = e.getMessage();
//			System.out.println("Error cause "+cause);
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
//	@Override
//	public boolean modificar(CajaSesion cajaSesion,Comprobante comprobante){
//		try {
//
//			beginTransaction();
//			comprobante = comprobanteDao.create(comprobante);
//			cajaSesion = update(cajaSesion);
//			Caja c=cajaSesion.getCaja();
//			c.setOpcion("CE");
//			cajaDao.modificar(c);
//			commitTransaction();
//			FacesUtil.infoMessage("Modificacion Correcta", "Nota de Venta ");
//			return true;
//		} catch (Exception e) {
//			String cause = e.getMessage();
//			if (cause
//					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
//				FacesUtil.errorMessage("Ya existe un registro igual.");
//			} else {
//				FacesUtil.errorMessage("Error al registrar");
//			}
//			rollbackTransaction();
//			return false;
//		}
//
//
//	}
//	
	@Override
	public boolean modificar(CajaSesion examen){
		try {

			beginTransaction();
			examen = update(examen);
			Caja c=examen.getCaja();
			c.setOpcion("CE");
			cajaDao.modificar(c);
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
	@Override
	public VentaNotaVenta modificar(VentaNotaVenta examen,
			List<VentaNotaVentaDetalle> listCotizacionDetalle) {
		try {
			examen.setListCotizacionDetalles(new ArrayList<VentaNotaVentaDetalle>());
			beginTransaction();
			examen = update(examen);
			cotizacionDetalleDao.deleteDetail(examen);
			for (VentaNotaVentaDetalle cotizacionDetalle : listCotizacionDetalle) {
				cotizacionDetalle.setCotizacion(examen);
				cotizacionDetalle = cotizacionDetalleDao
						.create(cotizacionDetalle);
			}
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "VentaNotaVenta "
					+ examen.toString());
			return examen;
		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause
					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			} else {
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return null;
		}
	}*/

	@Override
	public boolean eliminar(CajaSesion especialidad) {
		try {
			beginTransaction();
			CajaSesion bar = update(especialidad);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "CajaSesion "
					+ especialidad.toString());
			return bar != null ? true : false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ICajaSesionDao#procesar(com.teds.spaps.model.CajaSesion)
	 */
	@Override
	public boolean procesar(CajaSesion caja) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ICajaDao#RetornarPorId(java.lang.Integer)
	 */
	@Override
	public CajaSesion RetornarPorId(Integer id) {
		return findById(id);

	}

	@Override
	public CajaSesion RetornarPorCaja(Caja caja) {
		String query = "SELECT p FROM CajaSesion p where p.procesada='false' and p.caja.id=:idcaja";
		Query q = getEntityManager().createQuery(query);
		q.setParameter("idcaja", caja.getId());
		return (CajaSesion)q.getSingleResult();
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ICajaDao#RetornarOnCompletePorCompania(com.teds.spaps.model.Compania, java.lang.String)
	 */
//	@Override
//	public List<CajaSesion> RetornarOnCompletePorCompania(Compania compania,
//			String nombre) {
//
//		return null;
//	}

	@Override
	public List<CajaSesion> listaPorCompaniaUSuarioYActivos(Empresa empresa, Usuario us) {
		String query =  "select c from CajaSesion c where c.estado='AC' and c.caja.empresa.id="+empresa.getId()+" ORDER BY c.id DESC";
		Query q = getEntityManager().createQuery(query);
		//	q.setParameter("nombre", "%"+nombre+"%");
		return (List<CajaSesion>)q.getResultList();
	}

	@Override
	public CajaSesion obtenerPorUsuarioyEmpresa(Usuario usuario, Empresa empresa){
		//Falta validar Por usuario y compañia	
		System.out.println("obtenerPorUsuarioyEmpresa..");
		String query =  "select c from CajaSesion c where c.procesada=false and c.caja.empresa.id="+empresa.getId()+" and c.usuario.id="+usuario.getId()+" ORDER BY c.id DESC ";
		System.out.println("query : "+query);
		Query q = getEntityManager().createQuery(query);
		List<CajaSesion> cS=q.setMaxResults(1).getResultList();
		if (cS.size()>0) {
			return cS.get(0);
		} else {
			return null;
		}
	}
	
	


}
