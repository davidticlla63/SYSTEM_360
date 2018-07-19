package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.erp360.interfaces.ITipoConceptoDao;
import com.erp360.model.TipoConcepto;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.IdGenerator;



@Stateless
public class TipoConceptoDao extends DataAccessObjectGeneric<TipoConcepto>
		implements ITipoConceptoDao {

	//private @Inject ITipoConceptoDetalleDao cotizacionDetalleDao;
     private IdGenerator generador=new IdGenerator();
	public TipoConceptoDao() {
		super(TipoConcepto.class);
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
	
	
	public TipoConcepto create(TipoConcepto especialidad) {
		return super.create(especialidad);
	}

	@Override
	public TipoConcepto update(TipoConcepto especialidad) {
		return super.update(especialidad);
	}

	
	@Override
	public TipoConcepto registrar(TipoConcepto examen) {
		try {
			beginTransaction();
			examen = super.create(examen);
//		    examen.setCodigo(generador.generarCodigo("C",examen.getId()));
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "TipoConcepto");
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
	
	public boolean modificar(TipoConcepto examen){
try {
			
			beginTransaction();
			examen = update(examen);
			commitTransaction();
			FacesUtil.infoMessage("Modificacion Correcta", "TipoConcepto");
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
	public boolean eliminar(TipoConcepto especialidad) {
		try {
			beginTransaction();
			TipoConcepto bar = update(especialidad);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "TipoConcepto "
					+ especialidad.toString());
			return bar != null ? true : false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ITipoConceptoDao#procesar(com.teds.spaps.model.TipoConcepto)
	 */
	@Override
	public boolean procesar(TipoConcepto caja) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ITipoConceptoDao#RetornarPorId(java.lang.Integer)
	 */
	@Override
	public TipoConcepto RetornarPorId(Integer id) {
	return findById(id);
		
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ITipoConceptoDao#RetornarOnCompletePorCompania(com.teds.spaps.model.Compania, java.lang.String)
	 */
	@Override
	public List<TipoConcepto> RetornarOnCompletePorEmpresa(Empresa compania,
			String nombre) {
		
		return  findAllAndParameterForNameAutoComplete("nombre",nombre,"empresa",compania.getId());
	}

	@Override
	public List<TipoConcepto> obtenerPorEmpresa(Empresa compania) {
	 return findActiveParameter("empresa",compania.getId());
	}
	@Override
	public List<TipoConcepto> listaPorEmpresaYActivos(Empresa empresa) {
		String query =  "select c from TipoConcepto c where c.estado='AC' and c.empresa.id="+empresa.getId()+" ORDER BY c.id ASC";
		Query q = getEntityManager().createQuery(query);
	//	q.setParameter("nombre", "%"+nombre+"%");
		
		return (List<TipoConcepto>)q.getResultList();
	}

	
	@Override
	public List<TipoConcepto> obtenerPorEmpresa(String nombre,
			Empresa empresa) {
		return findAllActiveOtherTableAndParameterForNameAutoComplete(
				"concepto", nombre, "estado", "AC", "empresa",
				empresa.getId());
	}


	@Override
	public  boolean esCajero(Usuario usuario,TipoConcepto caja){
		String query="select c from TipoConcepto c join c.listaUsuarios lu join lu.usuario us where us.id= "+usuario.getId()+" and c.id="+caja.getId()+" and c.opcion='CE'";
		return executeQueryResulList(query).size()>0;
	}
	
	
}
