package com.pe.lima.sg.bean.caja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class CobroBean extends MontoBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date fechaCobro;
	private BigDecimal montoSoles;
	private BigDecimal montoDolares;
	private BigDecimal tipoCambio;
	private String tipoCobro;
	private String tipoOperacion;
	private Integer codigoContrato;
	private Integer codigoDesembolso;
	private Date fechaCreacion;
	private String estado;
	private boolean seleccionado;
	private Integer codigoAdelanto;
	private Integer intMesCobro;
	//para la reversion del adelanto
	private BigDecimal montoSolesConsumido;
	private BigDecimal montoDolaresConsumido;
	private String tipoMoneda;
	//para el registro de bancarizado
	private String tipoPago;
	private String tipoBancarizado;
	private String numeroOperacion;
	private Date fechaOperacion;
	//para los mensajes
	private BigDecimal saldoMesMensaje;
	private String nombreMesMensaje;
	private String mensajeCobro;
	
	private String nota;
	
	

	

}
