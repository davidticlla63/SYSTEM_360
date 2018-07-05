package com.erp360.interfaces;

import java.util.Date;
import java.util.List;

import com.erp360.model.Empresa;
import com.erp360.model.TipoCambio;

public interface ITipoCambioDao {

	
	public TipoCambio registrar(TipoCambio tipoCambio);

	
	public TipoCambio modificar(TipoCambio usuario);

	public List<TipoCambio> obtenerOrdenAscPorId();
	public List<TipoCambio> obtenerOrdenDescPorId();

	public  List<TipoCambio> obtenerPorEmpresa(Empresa empresa);

	public TipoCambio obtenerPorEmpresaYFecha(Empresa empresa,Date fecha);
	public TipoCambio obtenerPorEmpresaDiaAnterior(Empresa empresa);

	public TipoCambio obtenerUltimoRegistro(Empresa empresa);
}
