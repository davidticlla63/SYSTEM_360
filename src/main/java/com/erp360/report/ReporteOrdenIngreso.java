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
import javax.sql.DataSource;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import java.util.HashMap;
import java.util.Map;

import com.erp360.util.Conexion;

import javax.naming.Context;
import javax.naming.InitialContext;


@WebServlet("/ReporteOrdenIngreso")
public class ReporteOrdenIngreso  extends HttpServlet{

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

			String pRazonSocial = request.getParameter("pRazonSocial");
			String pDireccion = request.getParameter("pDireccion");
			String pTelefono = request.getParameter("pTelefono");
			Integer pIdOrdenIngreso = Integer.parseInt(request.getParameter("pIdOrdenIngreso"));
			String  pUsuario = request.getParameter("pUsuario");

			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

			String rutaReporte = urlPath+"resources/report/inventario/orden_ingreso.jasper";
			String URL_SERVLET_LOGO = urlPath+"ServletImageLogo?id=1&type=EMPRESA";

			Map parameters = new HashMap();
			parameters.put("pIdOrdenIngreso",new Integer(pIdOrdenIngreso));
			parameters.put("pUsuario", pUsuario);
			parameters.put("pLogo", URL_SERVLET_LOGO);
			parameters.put("pRazonSocial", pRazonSocial);
			parameters.put("pDireccion", pDireccion);
			parameters.put("pTelefono", pTelefono);

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
