package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/*Contiene la información del total de todos los locales x inmueble*/
@Getter
@Setter
public class LocalTotalBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigDecimal totalAlquiler;
	private BigDecimal totalServicio;
	private List<LocalSubTotalBean> listaLocalSubTotal;
	//Adicion de datos en el reporte - 2023.02.22
	private BigDecimal totalGarantia;
	public LocalTotalBean(){
		this.totalAlquiler = new BigDecimal(0);
		this.totalServicio = new BigDecimal(0);
		this.totalGarantia = new BigDecimal(0);
		this.listaLocalSubTotal = new ArrayList<LocalSubTotalBean>();
	}
}
