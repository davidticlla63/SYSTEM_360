package com.erp360.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import java.util.HashMap;
import java.util.Map;






//--datasource
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.hibernate.validator.internal.util.privilegedactions.NewInstance;

import com.erp360.util.Conexion;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@WebServlet("/ReporteTomaInventario")
public class ReporteTomaInventario  extends HttpServlet{

	private static final long serialVersionUID = 4293037836240051425L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream servletOutputStream = response.getOutputStream();
		JasperReport jasperReport;
		Connection conn = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx
					.lookup(Conexion.datasourse);
			conn = ds.getConnection();
			
			Integer pIdTomaInventario = Integer.parseInt(request.getParameter("pIdTomaInventario"));
			String  pUsuario = request.getParameter("pUsuario");
			String urlPath = request.getRequestURL().toString();
			String pRazonSocial = request.getParameter("pRazonSocial");
			String pDireccion = request.getParameter("pDireccion");
			String pTelefono = request.getParameter("pTelefono");
			String pIdEmpresa = request.getParameter("pIdEmpresa");

			
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String rutaReporte = urlPath+"resources/report/inventario/toma_inventario.jasper";			
			String URL_SERVLET_LOGO = urlPath+"ServletImageLogo?id=1&type=EMPRESA";
			// create a map of parameters to pass to the report.   
			@SuppressWarnings("rawtypes")
			Map parameters = new HashMap();
			parameters.put("pIdTomaInventario",new Integer(pIdTomaInventario));
			parameters.put("pUsuario", pUsuario);
			parameters.put("pLogo", URL_SERVLET_LOGO);
			parameters.put("pRazonSocial", pRazonSocial);
			parameters.put("pDireccion", pDireccion);
			parameters.put("pTelefono", pTelefono);
			//find file .jasper
			jasperReport = (JasperReport)JRLoader.loadObject (new URL(rutaReporte));
			JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, conn);
			response.setContentType("application/pdf");// text/html  application/pdf   application/html
			JasperExportManager.exportReportToPdfStream(jasperPrint2,servletOutputStream);
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());			
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
