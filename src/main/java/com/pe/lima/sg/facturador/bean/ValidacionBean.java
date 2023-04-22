package com.pe.lima.sg.facturador.bean;

import java.util.List;

public class ValidacionBean {
	private boolean resultado;
	private List<ErrorBean> listaError;
		
	
	public ValidacionBean() {
		super();
	}
	public ValidacionBean(boolean resultado, List<ErrorBean> listaError) {
		super();
		this.resultado = resultado;
		this.listaError = listaError;
	}
	public boolean isResultado() {
		return resultado;
	}
	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}
	public List<ErrorBean> getListaError() {
		return listaError;
	}
	public void setListaError(List<ErrorBean> listaError) {
		this.listaError = listaError;
	}
	
	
	}
