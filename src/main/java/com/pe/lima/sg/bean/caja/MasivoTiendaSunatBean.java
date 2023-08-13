package com.pe.lima.sg.bean.caja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class MasivoTiendaSunatBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoMasivoTienda;
	private String numeroTienda;
	private int codigoTienda;
	private int codigoContrato;
	private int codigoCxcDocumento;
	private String excluido;
	private String estado;	
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;
	private Date fechaCreacion;	
	private Date fechaModificacion;		
	private String ipCreacion;	
	private String ipModificacion;
	private int codigoComprobante;
	//Campos adicionales
	private String nombreEdificio;
	private String nombrePeriodo;
	private BigDecimal monto;
	
	

	

}
