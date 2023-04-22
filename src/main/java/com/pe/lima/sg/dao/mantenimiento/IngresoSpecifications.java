package com.pe.lima.sg.dao.mantenimiento;


import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblIngreso;

public final class IngresoSpecifications {
	
	private IngresoSpecifications() {}
	
	
	public static Specification<TblIngreso> conEdificio(Integer intCodigoEdificio) {
		return (root, query, cb) -> {
			if (intCodigoEdificio != null && intCodigoEdificio > 0) {
				return cb.equal(
						root.<TblEdificio>get("tblEdificio").<Integer> get("codigoEdificio"),	intCodigoEdificio);
			} else {
				return cb.equal(cb.literal(1), 1);
			}
		};
	}
	
	public static Specification<TblIngreso> conCodigoInterno(String codigoInterno) {
		return (root, query, cb) -> {
			if (codigoInterno != null && !codigoInterno.isEmpty()) {
				return cb.equal(
						root.<String> get("codigoInterno"),	codigoInterno);
			} else {
				return cb.equal(cb.literal(1), 1);
			}
		};
	}
	
	
	public static Specification<TblIngreso> conEstado(String strEstado) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstado);
			return cb.like(root.<String> get("estado"), valor);
		};
	}
	
	
	
	public static Specification<TblIngreso> conFechaIngreso(Date datFecha) {
		return (root, query, cb) -> {
			if (datFecha != null){
				return cb.greaterThanOrEqualTo(root.<Date> get("fechaIngreso"), datFecha);
			}else{
				return cb.equal(cb.literal(1), 1);
			}
			
		};
	}

	
	/**
	 * Para la busqueda por Like %
	 * @param String
	 * @return String
	 */
	private static String obtenerValorString(String valor) {
		if (valor == null || valor.isEmpty()) {
			return "%";
		} else {
			return "%" + valor + "%";
		}
	}
}
