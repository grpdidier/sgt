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
public class CobroLuzBean  extends MontoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date fechaCobroLuz;
	private BigDecimal montoSolesLuz;
	private BigDecimal montoDolaresLuz;
	private BigDecimal tipoCambioLuz;
	private Integer codigoTipoLuz;
	private String tipoMonedaLuz;
	//para el registro de bancarizado
	private String tipoPago;
	private String tipoBancarizado;
	private String numeroOperacion;
	private Date fechaOperacion;
	//para los mensajes
	private BigDecimal saldoMesMensaje;
	private String nombreMesMensaje;
	private String mensajeCobro;
}
