package com.erp360.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder.Case;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Time implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1808620452373692675L;

	private Integer hora;
	private Integer minuto;
	private Integer segundo;

	public Time() {
		super();
		this.hora = 0;
		this.minuto = 0;
		this.segundo = 0;
	}

	public Time(Integer hora, Integer minuto, Integer segundo) {
		super();
		this.hora = hora;
		this.minuto = minuto;
		this.segundo = segundo;
	}

	public Integer getHora() {
		return hora;
	}

	public void setHora(Integer hora) {
		this.hora = hora;
	}

	public Integer getMinuto() {
		return minuto;
	}

	public void setMinuto(Integer minuto) {
		this.minuto = minuto;
	}

	public Integer getSegundo() {
		return segundo;
	}

	public void setSegundo(Integer segundo) {
		this.segundo = segundo;
	}

	static public Time toTime(String time) {
		String[] value = time.split(":");
		Time hora = new Time(Integer.valueOf(value[0]),
				Integer.valueOf(value[1]), Integer.valueOf(value[2]));
		return hora;

	}

	static public String convertTimeToString(Date time) {
		return time.getHours() + ":" + time.getMinutes() + ":"
				+ time.getSeconds();

	}

	static public String convertDateToString(Date time) {
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fecha = dt.format(time);
		System.out.println("Fecha: " + fecha);
		return fecha;

	}

	static public String convertSimpleDateToString(Date time) {
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		String fecha = dt.format(time);
		System.out.println("Fecha: " + fecha);
		return fecha;

	}

	static public boolean mayorT1T2(Time inicio, Time fin) {
		if (inicio.getHora().doubleValue() > fin.getHora().doubleValue()) {
			return true;
		} else if (inicio.getHora().doubleValue() == fin.getHora()
				.doubleValue()) {
			if (inicio.getMinuto().doubleValue() > fin.getMinuto()
					.doubleValue()) {
				return true;
			} else if (inicio.getMinuto().doubleValue() == fin.getMinuto()
					.doubleValue()) {
				if (inicio.getSegundo().doubleValue() > fin.getSegundo()
						.doubleValue()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	 public static Date setCeroMillisecond(Date date) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.set(Calendar.MILLISECOND, 0);
	      /*  calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.HOUR, 0);*/

	        return calendar.getTime();
	    }

	static public boolean bettweenToDate(Time time1, Time time2, Time time3) {
		if (mayorT1T2(time3, time2)) {
			return false;
		} else {

			if (!mayorT1T2(time1, time2)) {
				if (mayorT1T2(time3, time1)) {
					if (mayorT1T2(time3, time2)) {
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}

		}
	}

	static public boolean may(Date time1, Date time2) {

		long lantes = time1.getTime();
		long lahora = time2.getTime();

		long hora = (lahora - lantes) / 3600000;
		if (lantes > lahora) {
			return true;
		} else {
			return false;
		}

	}

	static public Date sumarRestarDiasFecha(Date fecha, int dias) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.DAY_OF_YEAR, dias); // numero de días a añadir, o
													// restar en caso de días<0
		return calendar.getTime(); // Devuelve el objeto Date con los nuevos
									// días añadidos

	}
	
	static public Date sumarRestarHorasFecha(Date fecha, int horas) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.HOUR_OF_DAY, horas); // numero de días a añadir, o
													// restar en caso de días<0
		return calendar.getTime(); // Devuelve el objeto Date con los nuevos
									// días añadidos

	}

	@Override
	public String toString() {
		return "Time [hora=" + hora + ", minuto=" + minuto + ", segundo="
				+ segundo + "]";
	}

	public String toConvertToString() {
		return hora + ":" + minuto + ":" + segundo;
	}

	static public String obtenerFormatoYYYYMMDD(Date date) {
		try {
			String DATE_FORMAT = "yyyyMMdd";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String fecha= sdf.format(date);
			System.out.println(fecha);
			return fecha;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en obtenerFormatoYYYYMMDD: "
					+ e.getMessage());
			return null;
		}
	}

	static public String obtenerFormatoYYYYMMDDHH(Date date) {
		try {
			String DATE_FORMAT = "yyyyMMddkk";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String fecha = sdf.format(date);
			System.out.println(fecha);
			return fecha;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en obtenerFormatoYYYYMMDDHH: "
					+ e.getMessage());
			return null;
		}
	}

	static public String obtenerFormatoYYYYMMDDHHMISS(Date date) {
		try {
			String DATE_FORMAT = "yyyyMMddkkmmss";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String fecha = sdf.format(date);
			System.out.println(fecha);
			return fecha;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en obtenerFormatoYYYYMMDDHHMISS: "
					+ e.getMessage());
			return null;
		}
	}

	static public String mes(int mes) {
		switch (mes) {
		case 1:
			return "Enero";
		case 2:
			return "Febrero";
		case 3:
			return "Marzo";
		case 4:
			return "Abril";
		case 5:
			return "Mayo";
		case 6:
			return "Junio";
		case 7:
			return "Julio";
		case 8:
			return "Agosto";
		case 9:
			return "Septiembre";
		case 10:
			return "Octubre";
		case 11:
			return "Noviembre";
		case 12:
			return "Diciembre";

		}
		return null;
	}

	static public String obtenerFormatoYYYYMM(Date date) {
		try {
			String DATE_FORMAT = "yyyyMM";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return sdf.format(date);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en obtenerFormatoYYYYMM: "
					+ e.getMessage());
			return null;
		}
	}
	
	static public String obtenerFormatoMM(Date date) {
		try {
			String DATE_FORMAT = "MM";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return sdf.format(date);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en obtenerFormatoMM: "
					+ e.getMessage());
			return null;
		}
	}

	public static Date getPrimerDiaDelMes(Date fecha) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMinimum(Calendar.DAY_OF_MONTH),
				cal.getMinimum(Calendar.HOUR_OF_DAY),
				cal.getMinimum(Calendar.MINUTE),
				cal.getMinimum(Calendar.SECOND));
		System.out.println(cal.getTime());
		return cal.getTime();
	}

	public static Date getUltimoDiaDelMes(Date fecha) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.getActualMaximum(Calendar.DAY_OF_MONTH),
				cal.getMaximum(Calendar.HOUR_OF_DAY),
				cal.getMaximum(Calendar.MINUTE),
				cal.getMaximum(Calendar.SECOND));
		System.out.println(cal.getTime());
		return cal.getTime();
	}

}
