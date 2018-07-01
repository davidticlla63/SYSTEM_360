package com.erp360.util;

import java.text.DecimalFormat;

public class NumberUtil {
	
	public static boolean isNumeric(Object value){
		try {
			Integer.parseInt((String) value);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	public static String decimalFormat(double value){
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		return df.format(value);
	}

}
