package com.erp360.util;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
public class FileUtil {

	public static String obtenerExtencionFile(String fileName){
		// nameFile.substring(nameFile.lastIndexOf('.'), nameFile.length());
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i+1);
		}
		return extension;
	}

}
