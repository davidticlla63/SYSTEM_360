package com.erp360.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Almacen;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.Gestion;
import com.erp360.model.OrdenIngreso;
import com.erp360.model.Producto;
import com.erp360.util.E;
import com.erp360.util.EDKardexProducto;
import com.erp360.util.O;
import com.erp360.util.P;
import com.erp360.util.Q;
import com.erp360.util.R;
import com.erp360.util.S;
import com.erp360.util.U;
import com.erp360.util.V;
import com.erp360.util.W;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class AlmacenProductoDao extends DataAccessObjectJpa<AlmacenProducto,E,R,S,O, P, Q, U, V, W> {

	public AlmacenProductoDao(){
		super(AlmacenProducto.class);
	}

	public AlmacenProducto registrar(AlmacenProducto almacenProducto){
		try{
			beginTransaction();
			almacenProducto = create(almacenProducto);
			commitTransaction();
			return almacenProducto;
		}catch(Exception e){
			rollbackTransaction();
			return null;
		}
	}
	
	public boolean modificar(AlmacenProducto almacenProducto){
		try{
			beginTransaction();
			update(almacenProducto);
			commitTransaction();
			return true;
		}catch(Exception e){
			rollbackTransaction();
			return false;
		}
	}
	
	/**
	 * 
	 * @param alm
	 * @param tipo
	 * @param fechIni
	 * @param fechFin
	 * @return
	 */
	public List<EDKardexProducto> obtenerKardexPorFechas(Almacen alm, Producto producto, Date fechIni, Date fechFin) {
		String query = "select con.producto.id,con.producto.nombreProducto, case when con.tipoMovimiento='INGRESO' then con.cantidad else 0 end  as ingreso,case when con.tipoMovimiento='SALIDA' then con.cantidad else 0 end  as egreso ,con.fechaRegistro"
				+ " ,con.movimiento,con.usuarioRegistro from CardexProducto con where (con.estado='AC' or con.estado='IN') "
				+ " and con.almacen.id="
				+ alm.getId()
				+ " and con.producto.id="
				+ producto.getId()
				+ " and con.fechaRegistro between :fechIni and :fechFin " 
				+ " order by con.fechaRegistro desc";

		//Query quer = em.createQuery(query);
		//quer.setParameter("fechIni", fechIni);
		//quer.setParameter("fechFin", fechFin);
		//System.out.println("entro");
		List<EDKardexProducto> listCatalogo = new ArrayList<EDKardexProducto>();
//		for (Iterator it = quer.getResultList().iterator(); it.hasNext();) {
//			Object[] row = (Object[]) it.next();
//			EDCatalogoPrecios structureCatalogoPrecios = new EDCatalogoPrecios(
//					(Integer) row[0], row[1].toString(), (double) row[2]);
//
//			listCatalogo.add(structureCatalogoPrecios);
//		}
		System.out.println("paso  : " + listCatalogo.size());

		return listCatalogo;
	}
	/**
	 * Metodo [Primero Entrar Primero Salir]
	 * @param producto
	 * @return AlmacenProducto
	 */
	public AlmacenProducto obtenerAlmacenProductoPorPEPS(Producto producto){
		String query = "select em from AlmacenProducto em where em.estado='AC' and em.stock>0 and em.producto.id="+producto.getId()+" order by em.id asc";
		List<AlmacenProducto> list = executeQueryResulList(query);
		return list.size()>0?list.get(0):null;
	}
	
	public AlmacenProducto obtenerAlmacenProductoPorUEPS(Producto producto){
		String query = "select em from AlmacenProducto em where em.estado='AC' and em.stock>0 and em.producto.id="+producto.getId()+" order by em.id desc";
		List<AlmacenProducto> list = executeQueryResulList(query);
		return list.size()>0?list.get(0):null;
	}
	
	public AlmacenProducto obtenerAlmacenProductoPorPPP(Producto producto){
		return null;
	}
	
	public List<AlmacenProducto> obtenerTodosOrdenadosPorI(Gestion gestion) {
		String query = "select ser from AlmacenProducto ser where ser.estado='AC' or ser.estado='IN' and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<AlmacenProducto> obtenerTodosPorOrdenIngreso(OrdenIngreso ordenIngreso) {
		String query = "select em from AlmacenProducto em where (em.estado='AC' or em.estado='IN') and em.ordenIngreso.id="+ordenIngreso.getId()+" order by em.id desc";
		return executeQueryResulList(query);
	}

	public AlmacenProducto findByAlmacenProducto(Gestion gestion,Almacen almacen,Producto producto) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.almacen.id="
					+ almacen.getId() + " and em.producto.id="+producto.getId();
			return (AlmacenProducto) executeQuerySingleResult(query);
		}catch(Exception e){
			return null;
		}
	}	
	
	public List<AlmacenProducto> findByAlmacenProductoAndFecha(Gestion gestion,Almacen almacen,Producto producto,Date fecha) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.almacen.id="
					+ almacen.getId() + " and em.producto.id="+producto.getId()+" and em.fechaRegistro='"+fecha+"'";
			return executeQueryResulList(query);
		}catch(Exception e){
			return new ArrayList<AlmacenProducto>();
		}
	}	

	public AlmacenProducto findByProductoConStockPromedio(Gestion gestion,Producto producto) {
		List<AlmacenProducto> almacenProductos = new ArrayList<AlmacenProducto>();
		AlmacenProducto almacenProducto = new AlmacenProducto(0,0,0,0);
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() +" and em.stock>0";
			almacenProductos =  executeQueryResulList(query);
			for(AlmacenProducto ap: almacenProductos){
				almacenProducto.setStock(almacenProducto.getStock() + ap.getStock());
				almacenProducto.setPrecioVentaContado(almacenProducto.getPrecioVentaContado() + ap.getPrecioVentaContado());
				almacenProducto.setPrecioVentaCredito(almacenProducto.getPrecioVentaCredito() + ap.getPrecioVentaCredito());
			}
			almacenProducto.setPrecioVentaContado(almacenProducto.getPrecioVentaContado()/almacenProductos.size());
			almacenProducto.setPrecioVentaCredito(almacenProducto.getPrecioVentaCredito()/almacenProductos.size());
		}catch(Exception e){
			//AlmacenProducto
		}
		return almacenProducto;
	}
	
	/**
	 * PEPS
	 * Obtener List<AlmacenProducto> por fechas
	 * @param almacen 
	 * @param producto
	 * @return List<AlmacenProducto>
	 */
	public List<AlmacenProducto> findAllByProductoAndAlmacenOrderByFecha(Gestion gestion, Almacen almacen,Producto producto) {
		try{
			String query = "select em from AlmacenProducto em where em.estado='AC' and em.producto.id="
					+ producto.getId() +" and em.almacen.id="+almacen.getId()+" and em.gestion.id="+gestion.getId()+" order by em.fechaRegistro asc";
			return executeQueryResulList(query);
		}catch(Exception e){
			return null;
		}
	}

	public List<AlmacenProducto> findByAlmacen(Gestion gestion,Almacen almacen) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.stock > 0 and em.gestion.id="+gestion.getId()+" and em.almacen.id="
					+ almacen.getId() ;
			return executeQueryResulList(query);
		}catch(Exception e){
			return null;
		}
	}

	public List<AlmacenProducto> findAllByProducto(Gestion gestion,Producto producto) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() ;
			return executeQueryResulList(query);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<AlmacenProducto> findAllByProductoAndGestion(Producto producto,Gestion gestion) {
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() ;
			return executeQueryResulList(query);
		}catch(Exception e){
			return null;
		}
	}

	public List<AlmacenProducto> findProductoConStockOrderedByID(Gestion gestion) {
		String query = "select ser from AlmacenProducto ser where ser.estado='AC' and ser.stock > 0 and ser.gestion.id="+gestion.getId()+" order by ser.id asc";
		return executeQueryResulList(query);
	}
	
	public List<AlmacenProducto> findProductoConStockOrderedByIDAndGestion( Gestion gestion) {
		String query = "select em from AlmacenProducto em where em.estado='AC' and (em.stock > 0) and em.gestion.id="+gestion.getId()+" order by em.producto.nombre asc";
		return executeQueryResulList(query);
	}
	
	public List<AlmacenProducto> findProductoConOSinStockOrderedByIDAndGestion( Gestion gestion) {
		String query = "select em from AlmacenProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" order by em.producto.nombre asc";
		return executeQueryResulList(query);
	}
	
	public double findPrecioPromedioByProducto(Gestion gestion,Producto producto) {
		double promedio = 0;
		try{
			String query = "select em from AlmacenProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() ;
			List<AlmacenProducto> list = executeQueryResulList(query);
			for(AlmacenProducto a:list){
				promedio = promedio + a.getPrecioVentaContado();
			}
			return list.size()>0?promedio/list.size():promedio;
		}catch(Exception e){
			return promedio;
		}
	}
	
	public List<AlmacenProducto> obtenerPorConsulta(String query){
		//String query = "select em from AlmacenProducto ";
		return findAllActivosByQueryAndTwoParameter("estado","AC","producto.nombre", query);
	}
	
	public List<Producto> getAllByNameAndCode(String nameAndCode){
		String query = "SELECT pr FROM AlmacenProducto em,Producto pr WHERE em.producto.id=pr.id and em.estado='AC' AND ( upper(translate(pr.nombre, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) LIKE '%"+nameAndCode+"%' OR upper(translate(pr.codigo, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) LIKE '%"+nameAndCode+"%' ) GROUP BY pr.id ORDER BY pr.nombre DESC";
		//new com.erp360.model.Producto(pr.id,pr.codigo,pr.nombre,pr.descripcion) return executeQueryResulList(query);
		return super.getEm().createQuery(query, Producto.class).getResultList();
	}
	
	public List<Producto> obtenerTodosPorNombreCodigo(String nombreCodigo){
		String query = "select distinct pr from AlmacenProducto em,Producto pr where em.producto.id=pr.id and em.estado='AC' and ( upper(translate(pr.nombre, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"+nombreCodigo+"%' or upper(translate(pr.codigo, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"+nombreCodigo+"%' )";
		//new com.erp360.model.Producto(pr.id,pr.codigo,pr.nombre,pr.descripcion) return executeQueryResulList(query);
		return super.getEm().createQuery(query, Producto.class).getResultList();
	}
	
	public List<AlmacenProducto> findAllProductoForQueryNombreAndAlmacen(String criterio,Almacen almacen) {
		try {
			String query = "select em from AlmacenProducto em,Producto pr,Almacen al where em.almacen.id=al.id and em.producto.id=pr.id and em.estado='AC' and upper(pr.nombre) like '%" + criterio + "%' and al.id="+almacen.getId()+" order by pr.nombre asc";
			return executeQueryResulList(query);
		} catch (Exception e) {
			return null;
		}
	}

}
