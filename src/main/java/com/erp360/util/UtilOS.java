package com.erp360.util;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
public class UtilOS {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
	    String OS = System.getProperty("os.name").toLowerCase();
		String OSArch = System.getProperty("os.arch").toLowerCase();
		String OSVersion = System.getProperty("os.version").toLowerCase();

		System.out.print("Sistema operativo: ");
		System.out.println(OS);

		if (isWindows()) {
			System.out.println("Es un Windows");
		} else if (isMac()) {
			System.out.println("Es un Mac");
		} else if (isUnix()) {
			System.out.println("Es un Unix/Linux");
		} else if (isSolaris()) {
			System.out.println("Es Solaris");
		} else {
			System.out.println("Sistema operativo no reconocido!!");
		}

		System.out.println("Version: "+OSVersion);
		System.out.println("Aquitectura: "+OSArch);
	}
	
	public static String obtenerSistemaOperativo(){
		return System.getProperty("os.name").toLowerCase();
	}

	public static boolean isWindows() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}

	public static boolean isSolaris() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("sunos") >= 0);
	}

}
