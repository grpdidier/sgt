package com.pe.lima.sg.bean.caja;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.pe.lima.sg.entity.caja.TblDesembolso;
import com.pe.lima.sg.entity.caja.TblDesembolsoArbitrio;
import com.pe.lima.sg.entity.cliente.TblContrato;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CobroGeneralBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private TblContrato contrato;
	private CobroBean cobroAlquiler;
	private CobroServicioBean cobroServicio;
	private CobroLuzBean cobroLuz;
	private CobroPrimerCobro cobroPrimerCobro;
	private CobroArbitrioBean cobroArbitrio;
	private List<TblDesembolso> listaCobroAlquiler;
	private List<TblDesembolso> listaCobroServicio;
	private List<TblDesembolso> listaCobroLuz;
	private List<TblDesembolsoArbitrio> listaCobroArbitrio;
	private List<TblDesembolso> listaCobroPrimerCobro;
	private Integer indiceTab = 1;
	private Map<Integer, String> mapUsuario;
	
	
	
	
	

}
