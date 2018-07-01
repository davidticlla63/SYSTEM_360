package com.erp360.util;

import java.util.Date;

public class UtilPostGre {

	private String user = "erp360";
	private String database = "erp360_db";
	private String password = "erp360";
	private String puerto = "5432";
	private String host = "localhost";
	
	//DIRECCION DONDE SE HACE LAS COPIAS DE SEGURIDAD
	private String path = "D:\\BACKUP";
	
	//direccion de bin de POSTGRES
	private String dirBin="C:\\Program Files\\PostgreSQL\\9.3\\bin";


	public boolean backUp() {
		try {
			Process p;
			ProcessBuilder pb;
			String archivo=database+Time.obtenerFormatoYYYYMMDDHHMISS(new Date())+".backup";
			path=path+"\\"+archivo;
			pb = new ProcessBuilder(
					dirBin+"\\pg_dump.exe",
					"-i", "-h", host, "-p", puerto, "-U", user, "-F", "c",
					"-b", "-v", "-f", path, database);
			pb.environment().put("PGPASSWORD", password);
			pb.redirectErrorStream(true);
			p = pb.start();
			System.out.println("Termino respaldo");
			return true;
		} catch (Exception e) {
			System.err.println("Error en : " + e.getStackTrace());
			return false;
		}
	}

	public boolean restore() {
		try {
			ProcessBuilder pb;
			pb = new ProcessBuilder(
					dirBin+"\\pg_restore.exe",
					"-i", "-h", host, "-p", puerto, "-U", user, "-d", database,
					"-v", path);
			pb.environment().put("PGPASSWORD", password);
			pb.redirectErrorStream(true);
			pb.start();
			System.out.println("Termino restauracion");
			return true;
		} catch (Exception e) {
			System.err.println("Error en : " + e.getStackTrace());
			return false;
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDirBin() {
		return dirBin;
	}

	public void setDirBin(String dirBin) {
		this.dirBin = dirBin;
	}

	public static void main(String[] args) {
		UtilPostGre p = new UtilPostGre();
		p.backUp();
	}

}
