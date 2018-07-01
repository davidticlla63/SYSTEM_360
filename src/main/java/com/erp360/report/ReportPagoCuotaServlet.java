package com.erp360.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.erp360.util.Conexion;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@WebServlet("/ReportPagoCuota")
public class ReportPagoCuotaServlet extends HttpServlet {

	private static final long serialVersionUID = -4777861118160332227L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletOutputStream servletOutputStream = response.getOutputStream();
		Connection conn = null;
		JasperReport jasperReport;
		JasperPrint jasperPrint;

		Statement stmt = null;
		ResultSet rs = null;

		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx
					.lookup(Conexion.datasourse);
			conn = ds.getConnection();

			String pRazonSocial = request.getParameter("pRazonSocial");
			String pDireccion = request.getParameter("pDireccion");
			String pTelefono = request.getParameter("pTelefono");
			String pIdNotaVenta = request.getParameter("pIdNotaVenta");
			String pIdEmpresa = request.getParameter("pIdEmpresa");

			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()- request.getRequestURI().length())+ request.getContextPath() + "/";

			String URL_SERVLET_LOGO = urlPath+"ServletImageLogo?id="+pIdEmpresa+"&type=EMPRESA";
			// create a map of parameters to pass to the report.
			Map parameters = new HashMap();
			parameters.put("pLogo", URL_SERVLET_LOGO);
			parameters.put("pRazonSocial", pRazonSocial);
			parameters.put("pDireccion", pDireccion);
			parameters.put("pTelefono", pTelefono);
			parameters.put("pIdNotaVenta", new Integer(pIdNotaVenta));

			String rutaReporte = "";
			rutaReporte = urlPath + "resources/report/cobranza/reportPagoCuota.jasper";

			// find file .jasper
			jasperReport = (JasperReport) JRLoader.loadObject(new URL(
					rutaReporte));
			// fill JasperPrint using fillReport() method
			jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, conn);

			response.setContentType("application/pdf");
			JasperExportManager.exportReportToPdfStream(jasperPrint,
					servletOutputStream);

			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (Exception e) {
			// display stack trace in the browser
			e.printStackTrace();
			System.out.println("Error al ingresar JasperReportServlet: "
					+ e.getMessage());
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
