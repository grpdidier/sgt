package com.pe.lima.sg.api.Interface;


import com.pe.lima.sg.api.bean.CredencialBean;

public interface IApiOseCSV {

	public String obtenerToken(CredencialBean credencialBean) throws Exception;
	
	public Integer obtenerTicket(CredencialBean credencialBean);
	
	public Integer obtenerCDRDocumento(CredencialBean credencialBean);
	
	public Integer obtenerXMLDocumento(CredencialBean credencialBean);
	
	public Integer obtenerPDFDocumento(CredencialBean credencialBean);
}
