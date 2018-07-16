package com.erp360.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import com.erp360.util.Conexion;

@WebServlet("/ReportPdfServlet")
public class ReportPdfServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6864764769799543284L;

	@SuppressWarnings("unused")
	@Inject
	private FacesContext facesContext;

	// @SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked" })
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletOutputStream servletOutputStream = response.getOutputStream();

		JasperReport jasperReport;

		Connection conn = null;
		DataSource ds = null;
		Context ctx = null;
		try {

			try {

				ctx = new InitialContext();
				ds = (DataSource) ctx.lookup(Conexion.datasourse);

				conn = ds.getConnection();

				if (conn != null) {
					System.out
							.println("Conexion Exitosa JDBC com.edb.Driver...");
				} else {
					System.out.println("Error Conexion JDBC com.edb.Driver...");
				}

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error al conectar JDBC: " + e.getMessage());
			}

			try {
				if (request.getSession().getAttribute("parameter") != null
						&& request.getSession().getAttribute("path") != null) {

					Map<String, Object> parameters = (Map<String, Object>) request.getSession().getAttribute("parameter");
					String path = (String) request.getSession().getAttribute("path");

					System.out.println("Parametros : " + parameters.toString());
					System.out.println("rutaReporte : " + path);

					jasperReport = (JasperReport) JRLoader.loadObject(new URL(path));
					JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, parameters, conn);
					response.setContentType("application/pdf");// text/html
																// application/pdf
																// application/html
					JasperExportManager.exportReportToPdfStream(jasperPrint2,servletOutputStream);
					servletOutputStream.flush();
					servletOutputStream.close();
				} else {
					System.out.println("Parametros nulos");
					request.getSession().removeAttribute("parameter");
					request.getSession().removeAttribute("path");
					return;
				}

			} catch (Exception e) {
				// display stack trace in the browser
				e.printStackTrace();
				System.out.println("Error al ingresar Reportes: "
						+ e.getMessage());
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				e.printStackTrace(printWriter);
				response.setContentType("text/plain");
				response.getOutputStream().print(stringWriter.toString());

			}
		} finally {
			try {
				if (!conn.isClosed()) {
					System.out.println("cerrando conexion...");
					conn.close();
					request.getSession().removeAttribute("parameter");
					request.getSession().removeAttribute("path");
				}
			} catch (Exception e) {
				System.out.println("No se pudo cerrar la conexion, Error: "
						+ e.getMessage());
			}
		}

	}

}
