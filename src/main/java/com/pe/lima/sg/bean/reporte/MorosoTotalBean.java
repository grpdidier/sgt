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

	private BigDecimal total;
	private BigDecimal saldo;
	private List<MorosoSubTotalBean> listaMorosoSubTotal;
	
	public MorosoTotalBean(){
		this.total = new BigDecimal(0);
		this.saldo = new BigDecimal(0);
		this.listaMorosoSubTotal = new ArrayList<MorosoSubTotalBean>();
	}
}
