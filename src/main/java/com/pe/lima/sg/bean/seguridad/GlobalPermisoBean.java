package com.pe.lima.sg.bean.seguridad;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/* Esta clase permite configurar los permisos en toda la aplicación
 * Cada vez que se asigne un permiso sobre algun modulo, se debe agregar el atributo en esta clase
 * 
 * */
@Getter
@Setter
public class GlobalPermisoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/*Modulo de Cobros - Opción Fecha de Cobro*/
	private String modCobFecCob = "N";

	/*Modulo de Ingreso - Opción Fecha de Ingreso*/
	private String modIngFecIng = "N";
	
	/*Modulo de Cobros - Opción Eliminar Cobro - Reversion*/
	private String modCobEliCob = "N";
	
	/*Modulo de Ingreso - Opción Elimar Ingreso*/
	private String modIngBotEli = "N";
	
	

}
