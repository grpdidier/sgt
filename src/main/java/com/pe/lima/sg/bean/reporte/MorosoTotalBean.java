package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/*Contiene la información del total de todos los morosos*/
@Getter
@Setter
public class MorosoTotalBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigDecimal totalSoles;
	private BigDecimal saldoSoles;
	private List<MorosoSubTotalBean> listaMorosoSubTotal;
	private BigDecimal totalDolares;
	private BigDecimal saldoDolares;
	
	public MorosoTotalBean(){
		this.totalSoles = new BigDecimal(0);
		this.saldoSoles = new BigDecimal(0);
		this.totalDolares = new BigDecimal(0);
		this.saldoDolares = new BigDecimal(0);
		this.listaMorosoSubTotal = new ArrayList<MorosoSubTotalBean>();
	}
}
