package com.pe.lima.sg.dao.mantenimiento;


import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;


public final class TiendaSpecifications {
	
	private TiendaSpecifications() {}

	
	public static Specification<TblTienda> conNumero(String strNumero) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strNumero);
			return cb.like(root.<String> get("numero"), valor.toUpperCase());
		};
	}
	public static Specification<TblTienda> conCodigoEdificio(Integer intCodigoEdificio) {
		return (root, query, cb) -> {
			return cb.equal(root.<TblEdificio> get("tblEdificio").<Integer>get("codigoEdificio"), intCodigoEdificio);
		};
	}
	public static Specification<TblTienda> conEstado(String strEstado) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstado);
			return cb.like(root.<String> get("estado"), valor);
		};
	}
	public static Specification<TblTienda> conEstadoTienda(String strEstadoTienda) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstadoTienda);
			return cb.like(root.<String> get("estadoTienda"), valor);
		};
	}
	/**
	 * Para la busqueda por Like %
	 * @param String
	 * @return String
	 */
	private static String obtenerValorString(String valor) {
		if (valor == null || valor.isEmpty() || valor.equals("-1")) {
			return "%";
		} else {
			return "%" + valor + "%";
		}
	}
}
