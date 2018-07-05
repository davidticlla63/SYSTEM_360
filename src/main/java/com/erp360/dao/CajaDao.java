package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.erp360.interfaces.ICajaDao;
import com.erp360.model.Caja;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.IdGenerator;



@Stateless
public class CajaDao extends DataAccessObjectGeneric<Caja>
		implements ICajaDao {

	//private @Inject ICajaDetalleDao cotizacionDetalleDao;
     private IdGenerator generador=new IdGenerator();
	public CajaDao() {
		super(Caja.class);
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
	
	
	public Caja create(Caja especialidad) {
		return super.create(especialidad);
	}

	@Override
	public Caja update(Caja especialidad) {
		return super.update(especialidad);
	}

	
	@Override
	public Caja registrar(Caja examen) {
		try {
			beginTransaction();
			examen = super.create(examen);
		    examen.setCodigo(generador.generarCodigo("C",examen.getId()));
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Caja");
			return examen;
		} catch (Exception e) {
			String cause = e.getMessage();
			System.out.println("Error cause "+cause);
			if (cause
					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement : "+cause)) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			} else {
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return null;
		}
	}
	
	public boolean modificar(Caja examen){
try {
			
			beginTransaction();
			examen = update(examen);
			commitTransaction();
			FacesUtil.infoMessage("Modificacion Correcta", "Caja");
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
	public boolean eliminar(Caja especialidad) {
		try {
			beginTransaction();
			Caja bar = update(especialidad);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Caja "
					+ especialidad.toString());
			return bar != null ? true : false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ICajaDao#procesar(com.teds.spaps.model.Caja)
	 */
	@Override
	public boolean procesar(Caja caja) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ICajaDao#RetornarPorId(java.lang.Integer)
	 */
	@Override
	public Caja RetornarPorId(Integer id) {
	return findById(id);
		
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ICajaDao#RetornarOnCompletePorCompania(com.teds.spaps.model.Compania, java.lang.String)
	 */
	@Override
	public List<Caja> RetornarOnCompletePorEmpresa(Empresa compania,Usuario usuario,
			String nombre) {
		String query =  "select c from Caja c join c.listaUsuarios lu join lu.usuario us where us.id= :idUsuario  and c.opcion='CE' and upper(translate(c.nombre, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) "
				+ "LIKE :nombre ";
		Query q = getEntityManager().createQuery(query);
		q.setParameter("idUsuario", usuario.getId());
		q.setParameter("nombre", "%"+nombre+"%");
		return (List<Caja>)q.getResultList();
	}

	@Override
	public List<Caja> listaPorEmpresaYActivos(Empresa empresa) {
		String query =  "select c from Caja c where c.estado='AC' ORDER BY c.id ASC";
		Query q = getEntityManager().createQuery(query);
	//	q.setParameter("nombre", "%"+nombre+"%");
		return (List<Caja>)q.getResultList();
	}

	
	@Override
	public List<Caja> obtenerPorEmpresa(String nombre,
			Empresa empresa) {
		return findAllActiveOtherTableAndParameterForNameAutoComplete(
				"descripcion", nombre, "estado", "AC", "empresa",
				empresa.getId());
	}


}
