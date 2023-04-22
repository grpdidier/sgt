package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/*Contiene la información del sub total del moroso*/
@Getter
@Setter
public class MorosoSubTotalBean  implements Serializable, Comparable<MorosoSubTotalBean>{

	private static final long serialVersionUID = 1L;
	
	private BigDecimal subTotal;
	private BigDecimal subSaldo;
	private List<MorosoBean> listaMoroso;
	private String numeroTienda;
	
	public MorosoSubTotalBean(){
		this.subTotal = new BigDecimal(0);
		this.subSaldo = new BigDecimal(0);
		listaMoroso = new ArrayList<MorosoBean>();
	}


	@Override
    public int compareTo(MorosoSubTotalBean bean) {
        String numeroComparar = bean.getNumeroTienda().toUpperCase();
        
        /* For Ascending order*/
        return numeroTienda.compareTo(numeroComparar);

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
	
}
