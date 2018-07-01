package com.erp360.dao;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.erp360.model.Almacen;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.DetalleProducto;
import com.erp360.model.Gestion;
import com.erp360.model.Producto;
import com.erp360.model.Proveedor;
/**
 * @Pattern Fachada
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class FachadaOrdenIngreso implements Serializable{

	private static final long serialVersionUID = 4222532906084076924L;

	private @Inject AlmacenProductoDao almacenProductoDao;
	private @Inject DetalleProductoDao detalleProductoDao;

	public void actualizarStockExistente(Gestion gestionSesion,Almacen almacen,Producto prod ,double newStock)  {
		try{
			//0 . verificar si existe el producto en el almacen
			AlmacenProducto almProd =  almacenProductoDao.findByAlmacenProducto(gestionSesion,almacen,prod);
			System.out.println("aqui ");
			if(almProd != null){
				// 1 .  si existe el producto
				double oldStock = almProd.getStock();
				almProd.setStock(oldStock + newStock);
				almacenProductoDao.modificar(almProd);
			}

		}catch(Exception e){

			System.out.println("ERROR actualizarStock() "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * registro en la tabla almacen_producto, actualiza el stock y el precio(promedio agrupando los productos)
	 * @param almacen
	 * @param proveedor
	 * @param prod
	 * @param newStock
	 * @param date
	 * @param precioUnitario
	 * @param usuarioSession
	 * @throws Exception
	 */
	public void actualizarStock(Gestion gesstion ,Almacen almacen,Proveedor proveedor,Producto prod ,double newStock,Date date,double precioUnitario,String usuarioSession)  {
		try{
			AlmacenProducto almProd = new AlmacenProducto();
			almProd = new AlmacenProducto();
			almProd.setAlmacen(almacen);
			almProd.setProducto(prod);
			almProd.setProveedor(proveedor);
			almProd.setStock(newStock);
			almProd.setPrecioVentaContado(precioUnitario);
			almProd.setEstado("AC");
			almProd.setFechaRegistro(date);
			almProd.setUsuarioRegistro(usuarioSession);
			almProd.setGestion(gesstion);
			almacenProductoDao.registrar(almProd);
		}catch(Exception e){
			System.out.println("ERROR actualizarStock() "+e.getMessage());
		}
	}

	/**
	 * cargar en la ttabla detalle_producto, reegistros de productos, para luego utilizar el metodo PEPS
	 * @param fechaActual
	 * @param almacen
	 * @param producto
	 * @param cantidad
	 * @param precio
	 * @param fecha
	 * @param correlativoTransaccion
	 * @param usuarioSession
	 * @throws Exception
	 */
	public void cargarDetalleProducto(Gestion gestion,Date fechaActual,Almacen almacen,Producto producto,double cantidad, double precio, Date fecha, String correlativoTransaccion,String usuarioSession ) {
		try{
			DetalleProducto detalleProducto = new DetalleProducto();
			detalleProducto.setCodigo("OI"+correlativoTransaccion+fecha.toString());
			detalleProducto.setAlmacen(almacen);
			detalleProducto.setEstado("AC");
			detalleProducto.setPrecioVentaContado(precio);
			detalleProducto.setStockActual(cantidad);
			detalleProducto.setStockInicial(cantidad);
			detalleProducto.setCorrelativoTransaccion(correlativoTransaccion);
			detalleProducto.setFecha(fecha);
			detalleProducto.setFechaRegistro(fechaActual);
			detalleProducto.setProducto(producto);
			detalleProducto.setUsuarioRegistro(usuarioSession);
			detalleProducto.setGestion(gestion);
			detalleProductoDao.registrar(detalleProducto);
		}catch(Exception e){
			System.out.println("ERROR cargarDetalleProducto() "+e.getMessage());
		}
	}

}
