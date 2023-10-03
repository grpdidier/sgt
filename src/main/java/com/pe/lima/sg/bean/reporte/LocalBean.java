package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/*Contiene la informacion de los locales con alquiler y servicios - Tal como viene de la BD*/
@Getter
@Setter
public class LocalBean implements Serializable, Comparable<LocalBean>{

	private static final long serialVersionUID = 1L;
	
	private String 	nombreInmueble;
	private String	numeroTienda;		//Local
	private String 	nombreCliente;		
	private String 	estado;
	private BigDecimal montoAlquiler;
	private BigDecimal montoServicio;
	private String 	orden;
	//Adicion de datos en el reporte - 2023.02.22
	private String rucCliente;
	private String fechaInicioContrato;
	private String fechaFinContrato;
	private BigDecimal montoGarantia;
	private String fechaFinalizacion;
	
	@Override
    public int compareTo(LocalBean bean) {
        String numeroComparar = bean.getNumeroTienda().toUpperCase();
        
        /* For Ascending order*/
        return numeroTienda.compareTo(numeroComparar);

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
	
}
