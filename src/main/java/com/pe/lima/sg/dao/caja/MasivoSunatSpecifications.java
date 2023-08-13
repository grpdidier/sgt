package com.pe.lima.sg.dao.caja;


import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.caja.TblMasivoSunat;

public final class MasivoSunatSpecifications {
	
	private MasivoSunatSpecifications() {}
	

	public static Specification<TblMasivoSunat> conAnio(Integer intAnio) {
		return (root, query, cb) -> {
			if (intAnio.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<Integer> get("anio"), intAnio);
		};
	}
	public static Specification<TblMasivoSunat> conCodigoEdificio(Integer intCodigoEdificio) {
		return (root, query, cb) -> {
			if (intCodigoEdificio.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<Integer> get("codigoEdificio"), intCodigoEdificio);
		};
	}
	
	public static Specification<TblMasivoSunat> conEstado(String strEstado) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstado);
			return cb.like(root.<String> get("estado"), valor);
		};
	}
	public static Specification<TblMasivoSunat> conMes(String strMes) {
		return (root, query, cb) -> {
			if (strMes.equals("-1")) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<String> get("mes"), strMes);
		};
	}
	public static Specification<TblMasivoSunat> conTipoMasivo(String strTipo) {
		return (root, query, cb) -> {
			return cb.equal(root.<String> get("tipoMasivo"), strTipo);
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
