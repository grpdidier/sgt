package com.pe.lima.sg.dao.caja;


import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.caja.TblCajaChica;

public final class CajaChicaSpecifications {
	
	private CajaChicaSpecifications() {}
	

	public static Specification<TblCajaChica> conAnio(Integer intAnio) {
		return (root, query, cb) -> {
			if (intAnio.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<Integer> get("anio"), intAnio);
		};
	}
	public static Specification<TblCajaChica> conMes(Integer intMes) {
		return (root, query, cb) -> {
			if (intMes.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<Integer> get("mes"), intMes);
		};
	}
	
	public static Specification<TblCajaChica> conEstado(String strEstado) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstado);
			return cb.like(root.<String> get("estado"), valor);
		};
	}
	public static Specification<TblCajaChica> conNombre(String strNombre) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strNombre);
			return cb.like(root.<String> get("nombre"), valor);
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
