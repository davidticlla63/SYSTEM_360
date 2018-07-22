package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.erp360.interfaces.ITipoClienteDao;
import com.erp360.model.TipoCliente;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.IdGenerator;



@Stateless
public class TipoClienteDao extends DataAccessObjectGeneric<TipoCliente>
		implements ITipoClienteDao {

	//private @Inject ITipoClienteDetalleDao cotizacionDetalleDao;
     private IdGenerator generador=new IdGenerator();
	public TipoClienteDao() {
		super(TipoCliente.class);
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
	
	
	public TipoCliente create(TipoCliente especialidad) {
		return super.create(especialidad);
	}

	@Override
	public TipoCliente update(TipoCliente especialidad) {
		return super.update(especialidad);
	}

	
	@Override
	public TipoCliente registrar(TipoCliente examen) {
		try {
			beginTransaction();
			examen = super.create(examen);
//		    examen.setCodigo(generador.generarCodigo("C",examen.getId()));
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "TipoCliente");
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
	
	public boolean modificar(TipoCliente examen){
try {
			
			beginTransaction();
			examen = update(examen);
			commitTransaction();
			FacesUtil.infoMessage("Modificacion Correcta", "TipoCliente");
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
	public boolean eliminar(TipoCliente especialidad) {
		try {
			beginTransaction();
			TipoCliente bar = update(especialidad);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "TipoCliente "
					+ especialidad.toString());
			return bar != null ? true : false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ITipoClienteDao#procesar(com.teds.spaps.model.TipoCliente)
	 */
	@Override
	public boolean procesar(TipoCliente caja) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ITipoClienteDao#RetornarPorId(java.lang.Integer)
	 */
	@Override
	public TipoCliente RetornarPorId(Integer id) {
	return findById(id);
		
	}

	/* (non-Javadoc)
	 * @see com.teds.spaps.interfaces.dao.ITipoClienteDao#RetornarOnCompletePorCompania(com.teds.spaps.model.Compania, java.lang.String)
	 */
	@Override
	public List<TipoCliente> RetornarOnCompletePorEmpresa(Empresa compania,
			String nombre) {
		
		return  findAllAndParameterForNameAutoComplete("nombre",nombre,"empresa",compania.getId());
	}

	@Override
	public List<TipoCliente> obtenerPorEmpresa(Empresa compania) {
	 return findActiveParameter("empresa",compania.getId());
	}
	@Override
	public List<TipoCliente> listaPorEmpresaYActivos(Empresa empresa) {
		String query =  "select c from TipoCliente c where c.estado='AC' and c.empresa.id="+empresa.getId()+" ORDER BY c.id ASC";
		Query q = getEntityManager().createQuery(query);
	//	q.setParameter("nombre", "%"+nombre+"%");
		
		return (List<TipoCliente>)q.getResultList();
	}

	
	@Override
	public List<TipoCliente> obtenerPorEmpresa(String nombre,
			Empresa empresa) {
		return findAllActiveOtherTableAndParameterForNameAutoComplete(
				"concepto", nombre, "estado", "AC", "empresa",
				empresa.getId());
	}


	@Override
	public  boolean esCajero(Usuario usuario,TipoCliente caja){
		String query="select c from TipoCliente c join c.listaUsuarios lu join lu.usuario us where us.id= "+usuario.getId()+" and c.id="+caja.getId()+" and c.opcion='CE'";
		return executeQueryResulList(query).size()>0;
	}
	
	
}
