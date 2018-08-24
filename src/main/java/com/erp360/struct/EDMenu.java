/**
 * @author david
 */
package com.erp360.struct;

import java.util.ArrayList;
import java.util.List;

import com.erp360.model.Modulo;
import com.erp360.model.Permiso;

/**
 * @author david
 *
 */
public class EDMenu {
	private Modulo modulo;
	private List<Permiso> menus = new ArrayList<>();

	/**
	 * @return the modulo
	 */
	public Modulo getModulo() {
		return modulo;
	}

	/**
	 * @param modulo
	 *            the modulo to set
	 */
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	/**
	 * @return the menus
	 */
	public List<Permiso> getMenus() {
		return menus;
	}

	/**
	 * @param menus
	 *            the menus to set
	 */
	public void setMenus(List<Permiso> menus) {
		this.menus = menus;
	}

	@SuppressWarnings("el-syntax")
	public String styleDisplay() {
		return "display:#{accesos2Controller.verificarModulo('" + modulo.getNombre() + "')}";
	}

	@SuppressWarnings("el-syntax")
	public String classPath() {
		String string = "#{";
		for (Permiso menu : menus) {
			string = string + "(view.viewId.equals(\'" + menu.getRuta() + "\')) ? \'active\' : ";
		}
		return string + "\'inactive\'}";
	}

}
