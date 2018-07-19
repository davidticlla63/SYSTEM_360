/**
 * @author david
 */
package com.erp360.converter;

/**
 * @author david
 *
 */
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpServletRequest;

import com.erp360.controller.ConceptoController;
import com.erp360.model.TipoConcepto;

@FacesConverter("tipoConceptoConverter")
public class TipoConceptoConverter implements Converter {

	/*
	 * @ManagedProperty("#{pacientes}") private List<Paciente> listaPacientes;
	 */

	// private @Inject IPacienteDao pacienteDao;

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				/*
				 * ConsultaController service = (ConsultaController) fc
				 * .getExternalContext().getApplicationMap()
				 * .get("consultaController");
				 * 
				 * String url = ((HttpServletRequest) fc.getCurrentInstance()
				 * .getExternalContext().getRequest()).getRequestURI();
				 * System.out.println("url = " + url); String[] protocolo =
				 * url.split("/");
				 */
				@SuppressWarnings("static-access")
				String url = ((HttpServletRequest) fc.getCurrentInstance()
						.getExternalContext().getRequest()).getRequestURI();
				String[] protocolo = url.split("/");
				System.out.println("Protocolo : "
						+ protocolo[protocolo.length - 1]);
				List<TipoConcepto> pacientes = new ArrayList<TipoConcepto>();

				if (protocolo[protocolo.length - 1]
						.equalsIgnoreCase("concepto.xhtml")) {
					if (protocolo[protocolo.length - 2]
							.equalsIgnoreCase("caja")) {
						pacientes = ConceptoController.tipoConceptos;
					} 
				}

				TipoConcepto paciente = getObjectFromList(pacientes,
						Integer.valueOf(value));
				if (paciente != null) {
					return paciente;
				} else {
					return null;
				}
			} catch (NumberFormatException e) {
				throw new ConverterException();
			}
		} else {
			return null;
		}
	}

	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			return String.valueOf(((TipoConcepto) object).getId());
		} else {
			return null;
		}
	}



	private TipoConcepto getObjectFromList(final List<?> list,
			final Integer identifier) {
		for (final Object object : list) {
			final TipoConcepto team = (TipoConcepto) object;
			if (team.getId().equals(identifier)) {
				return team;
			}
		}
		return null;
	}

	/*
	 * public List<Paciente> getListaPacientes() { return listaPacientes; }
	 * 
	 * public void setListaPacientes(List<Paciente> listaPacientes) {
	 * this.listaPacientes = listaPacientes; }
	 */

}