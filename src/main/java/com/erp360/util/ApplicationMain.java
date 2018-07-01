package com.erp360.util;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


@Named
@Startup
@ApplicationScoped
public class ApplicationMain {
	
	
	private int contadorTest = 0;

	@PostConstruct
	public void initAplicationMain(){
		contadorTest = contadorTest + 1;
	}

	public int getContadorTest() {
		return contadorTest;
	}

	public void setContadorTest(int contadorTest) {
		this.contadorTest = contadorTest;
	}

}
