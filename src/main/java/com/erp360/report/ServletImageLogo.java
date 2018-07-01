package com.erp360.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.erp360.dao.EmpresaDao;
import com.erp360.model.Empresa;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@WebServlet("/ServletImageLogo")
public class ServletImageLogo extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private @Inject EmpresaDao empresaDao;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletImageLogo() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = "";
		String type = "";
		id = request.getParameter("id");
		type = request.getParameter("type");
		System.out.println("ServletImageLogo --->  doGet  : "+request.getContextPath()+"  |  id:"+id+"- type:"+type);
		byte[] imagenData = null;
		try{

			if(type.equals("EMPRESA")){
				Empresa empresa = empresaDao.findById(Integer.valueOf(id));
				imagenData = empresa.getFotoPerfil();
			}else{//USUARIO
				//Usuario usuario=usuarioRepository.findById(Integer.valueOf(id));
				imagenData = null;
			}
			if (imagenData == null) {
				if(type.equals("EMPRESA")){
					imagenData =  toByteArrayUsingJava(getImageDefaul("logo.png" ,"image/jpeg").getStream());
				}else{
					imagenData =  toByteArrayUsingJava(getImageDefaul("avatar.png" ,"image/jpg").getStream());
				}
			}
			response.setContentType("image/jpeg");
			response.setHeader("Content-Disposition", "inline; filename=imagen.jpg");
			response.setHeader("Cache-control", "public");
			ServletOutputStream sout = response.getOutputStream();
			sout.write(imagenData);
			sout.flush();
			sout.close();
		}catch(Exception e){
			System.out.println("Error doGet: "+e.getMessage());
		}
	}

	private StreamedContent getImageDefaul(String namePhoto,String type) {//logo-siga.png , image/png
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream stream = classLoader
				.getResourceAsStream(namePhoto);
		return new DefaultStreamedContent(stream, type);
	}

	public static byte[] toByteArrayUsingJava(InputStream is) throws IOException{ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while(reads != -1){
			baos.write(reads); reads = is.read(); 
		}
		return baos.toByteArray();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}