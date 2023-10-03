package com.pe.lima.sg.api.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredencialBean {
	private String path;
	private Integer status;
	//Token
	private String resourceToken;
	private String clientSecret;
	private String grantType;
	private String userName;
	private String password;
	//Documento
	private String resourceDocumento;
	private String csvFileName;
	private String accessToken;
	//CDR
	private String resourceCdr;
	private String ticket;
	private String cdrFileName;
	//XML
	private String resourceXml;
	private String xmlFileName;
	//PDF
	private String resourcePdf;
	private String pdfFileName;
	//UBIGEO
	private String urlUbigeo;
	private String tokenUbigeo;
}
