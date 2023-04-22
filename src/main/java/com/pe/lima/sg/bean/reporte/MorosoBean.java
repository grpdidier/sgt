package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/*Contiene la informacion de los morosos de los distintos servicios - Tal como viene de la BD*/
@Getter
@Setter
public class MorosoBean implements Serializable, Comparable<MorosoBean>{

	private static final long serialVersionUID = 1L;
	
	private String 	nombreInmueble;
	private String	numeroTienda;
	private String vencimiento;
	private String 	nombreCliente;
	private BigDecimal total;
	private BigDecimal saldo;
	private String 	tipoMoneda;
	private String tipoReferencia; // indica ALQ, SER, LUZ, etc
	
	@Override
    public int compareTo(MorosoBean bean) {
        String numeroComparar = bean.getNumeroTienda().toUpperCase();
        
        /* For Ascending order*/
        return numeroTienda.compareTo(numeroComparar);

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
	
}
