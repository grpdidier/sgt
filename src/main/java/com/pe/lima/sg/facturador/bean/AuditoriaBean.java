package com.pe.lima.sg.facturador.bean;

public class AuditoriaBean {
	private Integer codigoUsuario;
	private String ipCreacion;
	
	
	public AuditoriaBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AuditoriaBean(Integer codigoUsuario, String ipCreacion) {
		super();
		this.codigoUsuario = codigoUsuario;
		this.ipCreacion = ipCreacion;
	}
	public Integer getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(Integer codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	public String getIpCreacion() {
		return ipCreacion;
	}
	public void setIpCreacion(String ipCreacion) {
		this.ipCreacion = ipCreacion;
	}
	
}
