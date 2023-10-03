package com.pe.lima.sg.api.Interface;


import com.pe.lima.sg.api.bean.CredencialBean;
import com.pe.lima.sg.bean.caja.UbigeoBean;

public interface IApiOseCSV {

	public String obtenerToken(CredencialBean credencialBean) throws Exception;
	
	public Integer obtenerTicket(CredencialBean credencialBean);
	
	public Integer obtenerCDRDocumento(CredencialBean credencialBean);
	
	public Integer obtenerXMLDocumento(CredencialBean credencialBean);
	
	public Integer obtenerPDFDocumento(CredencialBean credencialBean);
	
	public Integer obtenerTicketParaMasivo(CredencialBean credencialBean);
	
	public UbigeoBean obtenerUbigeo(String ruc, String token, String urlUbigeo);
}
