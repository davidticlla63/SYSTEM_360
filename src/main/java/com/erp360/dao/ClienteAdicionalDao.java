package com.erp360.dao;

import javax.ejb.Stateless;

import com.erp360.model.ClienteAdicional;
import com.erp360.model.Cliente;
import com.erp360.util.E;
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
public class ClienteAdicionalDao extends DataAccessObjectJpa<ClienteAdicional,E,R,S,O, P, Q, U, V, W> {

	public ClienteAdicionalDao(){
		super(ClienteAdicional.class);
	}

	public ClienteAdicional obtenerPorCLiente(Cliente cliente) {
		try{
		String query = "select ser from ClienteAdicional ser where (ser.estado='AC' or ser.estado='IN') and ser.cliente.id="+cliente.getId();
		return executeQuerySingleResult(query);
		}catch(Exception e){
			return null;
		}
	}
}
