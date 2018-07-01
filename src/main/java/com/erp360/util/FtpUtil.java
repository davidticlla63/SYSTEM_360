package com.erp360.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
public class FtpUtil {
	
	public static final String REMOTE_DIRECTORY="/home/ftp/berater";  /*  /home/berater/ */

	/**
	 * si funciona
	 * @param user
	 * @param pass
	 * @param ip
	 * @param filename
	 * @return
	 */
	public static InputStream downloadFile(String user,String pass ,String ip,String filename){
		JSch sftp = new JSch();
		// Instancio el objeto session para la transferencia
		Session session = null;
		// instancio el canal sftp
		ChannelSftp channelSftp = null;
		int ftpPort = 22;
		String remoteDirectory = REMOTE_DIRECTORY;
		try {
			// Inciciamos el JSch con el usuario, host y puerto
			session = sftp.getSession(user, ip, ftpPort);
			// Seteamos el password
			session.setPassword(pass);
			// El SFTP requiere un intercambio de claves
			// con esta propiedad le decimos que acepte la clave
			// sin pedir confirmación
			Properties prop = new Properties();
			prop.put("StrictHostKeyChecking", "no");
			session.setConfig(prop);

			session.connect();

			// Abrimos el canal de sftp y conectamos
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
			// Convertimos el archivo a transferir en un OutputStream
			File tempFile = File.createTempFile("tempFile", ".pdf");
			OutputStream os = new BufferedOutputStream(new FileOutputStream( tempFile));
			// Iniciamos la transferencia
			String pPathFile = remoteDirectory.concat(filename);
			channelSftp.get(pPathFile, os);
			return new FileInputStream(tempFile);
		} catch (Exception e) {
			return null;
		} finally {
			// Cerramos el canal y session
			if (channelSftp.isConnected())
				channelSftp.disconnect();
			if (session.isConnected())
				session.disconnect();
		}// end try
	}

	/**
	 * si funciona
	 * @param username
	 * @param password
	 * @param hostname
	 * @param input
	 * @param filename
	 */
	public static void upLoadFile(String username, String password, String hostname, InputStream input,String filename){
		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;
		ChannelSftp c = null;
		int ftpPort = 22;
		String p = filename.contains("/ADJ/")?"/ADJ/":filename.contains("/MANUAL/")?"/MANUAL/":"";
		String remoteDirectory = REMOTE_DIRECTORY.concat(p);//  "/home/berater/ADJ/";
		//Now connect and SFTP to the SFTP Server
		try {
			//Create a session sending through our username and password
			session = jsch.getSession(username, hostname, ftpPort);
			System.out.println("***   FTP Session created.   ***");
			session.setPassword(password);
			session.setPassword(password);

			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			System.out.println("***   Session connected.   ***");

			//Open the SFTP channel
			System.out.println("***   Opening FTP Channel.   ***");
			channel = session.openChannel("sftp");
			channel.connect();
			c = (ChannelSftp) channel;

			//Change to the remote directory
			System.out.println("***   Changing to FTP remote dir: " + remoteDirectory + "   ***");
			c.cd(remoteDirectory);

			//Send the file we generated
			try {
				//String filename = "myfile.txt";

				System.out.println("***   Storing file as remote filename: " + filename + "   ***");

				//ByteArrayInputStream bis = new ByteArrayInputStream(contents.getBytes());
				c.put(input, filename.replace("/ADJ/", "").replace("/MANUAL/", ""));
				//return true;
			} catch (Exception e) {
				System.out.println("***   Storing remote file failed. " + e.toString() + "   ***");
				throw e;
			}
		} catch (Exception e) {
			System.out.println("***   Unable to connect to FTP server. " + e.toString() + "   ***");
			//throw e;
		} finally {
			//
			//Disconnect from the FTP server
			//
			try {
				if(session != null)
					session.disconnect();

				if(channel != null)
					channel.disconnect();

				if(c != null)
					c.quit();
			} catch (Exception exc) {
				System.out.println("***   Unable to disconnect from FTP server. " + exc.toString()+"   ***");
			}

			System.out.println("***   SFTP Process Complete.   ***");
		}
	}

//	/**
//	 * Para subida de archivos en el puerto 21
//	 * @param user
//	 * @param pass
//	 * @param ip
//	 * @param input
//	 * @param filename
//	 */
//	public static void upLoadFile2(String user, String pass, String ip, InputStream input,String filename){
//
//		FTPClient client = new FTPClient();
//		FileInputStream fis = null;
//		//filename = "/home/berater".concat(filename);
//
//		try {
//
//			//---
//			client.connect(ip);//,22);
//			client.login(user, pass);
//			client.enterLocalPassiveMode();
//
//			client.setFileType(FTP.BINARY_FILE_TYPE);
//
//			// Create an InputStream of the file to be uploaded
//			//
//			//String filename = "Touch.dat";
//			//fis = new FileInputStream(filename);
//
//			//
//			// Store file to server
//			//
//			client.storeFile(filename, input);
//			client.logout();
//			System.out.println("upLoadFile:"+filename+" - OK");
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (fis != null) {
//					fis.close();
//				}
//				client.disconnect();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	/**
//	 * Download port 21
//	 * @param user
//	 * @param pass
//	 * @param ip
//	 * @param filename
//	 * @return
//	 */
//	public static InputStream downloadFile2(String user,String pass ,String ip,String filename){
//		try{
//			FTPClient ftpClient = new FTPClient();
//			ftpClient.connect(ip,21);//, 22);
//			ftpClient.login(user, pass);
//			ftpClient.enterLocalPassiveMode();
//			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//			//filename = "/home/berater".concat(filename);
//
//			File tempFile = File.createTempFile("tempFile", ".pdf");
//			OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(tempFile));
//			boolean success = ftpClient.retrieveFile(filename, outputStream1);
//			outputStream1.close();
//
//			return new FileInputStream(tempFile);
//		}catch(Exception e){
//			return null;
//		}
//	}
}
