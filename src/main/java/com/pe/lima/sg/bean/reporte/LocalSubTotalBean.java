package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/*Contiene la información del sub total del local x inmueble*/
@Getter
@Setter
public class LocalSubTotalBean  implements Serializable, Comparable<LocalSubTotalBean>{

	private static final long serialVersionUID = 1L;
	
	private BigDecimal subTotalAlquiler;
	private BigDecimal subTotalServicio;
	private List<LocalBean> listaLocal;
	private String numeroTienda;
	private String estado;
	private String orden;
	//Adicion de datos en el reporte - 2023.02.22
	private BigDecimal subTotalGarantia;
	
	public LocalSubTotalBean(){
		this.subTotalAlquiler = new BigDecimal(0);
		this.subTotalServicio = new BigDecimal(0);
		this.subTotalGarantia = new BigDecimal(0);
		listaLocal = new ArrayList<LocalBean>();
	}


	@Override
    public int compareTo(LocalSubTotalBean bean) {
		String ordenComparar = bean.getOrden();
        
        /* For Ascending order*/
        return orden.compareTo(ordenComparar);

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
	
}
