package com.erp360.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.erp360.model.Ejecutivo;
import com.erp360.util.FacesUtil;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@FacesConverter("ejecutivoConverter")
public class EjecutivoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
		try {
			if (value == null || value.length() == 0 || FacesUtil.isDummySelectItem(component, value)) {
				return null;
			}
			Integer data = Integer.valueOf(value);// get ID
			Ejecutivo p = new Ejecutivo();
			p.setId(data);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
		try {
			if (object == null  || (object instanceof String && ((String) object).length() == 0)) {
				return null;
			}
			
			// return String.valueOf(((PlanCuenta) object).getId()); 
			return String.valueOf(((Ejecutivo) object).getId()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
}
