package com.erp360.util;

import java.io.IOException;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
public class PageUtil {
	
	/**
	 * Redireccionar a la pagina
	 * @param page "pages/module/page.xhtml";
	 */
	public static void cargarPagina(String page){
		try {
			//http://localhost:8080/mundovirtualhttp://localhost:8080/mundovirtual/pages/proceso/list-orden-ingreso.xhtml
			String url = FacesUtil.getUrl()+page+"?faces-redirect=true";
			System.out.println("***** url: "+url);
			FacesUtil.redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
