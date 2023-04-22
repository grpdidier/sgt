package com.pe.lima.sg.dao.mantenimiento;


import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.mantenimiento.TblPersona;


public final class PersonaSpecifications {
	
	private PersonaSpecifications() {}

	
	public static Specification<TblPersona> conNombre(String strNombre) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strNombre);
			return cb.like(root.<String> get("nombre"), valor.toUpperCase());
		};
	}
	
	public static Specification<TblPersona> conPaterno(String strPaterno) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strPaterno);
			return cb.like(root.<String> get("paterno"), valor.toUpperCase());
		};
	}
	
	public static Specification<TblPersona> conMaterno(String strMaterno) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strMaterno);
			return cb.like(root.<String> get("materno"), valor.toUpperCase());
		};
	}
	public static Specification<TblPersona> conDNI(String strDni) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strDni);
			return cb.like(root.<String> get("numeroDni"), valor.toUpperCase());
		};
	}
	public static Specification<TblPersona> conRuc(String strRuc) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strRuc);
			return cb.like(root.<String> get("numeroRuc"), valor.toUpperCase());
		};
	}
	public static Specification<TblPersona> conRazonSocial(String strRazonSocial) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strRazonSocial);
			return cb.like(root.<String> get("razonSocial"), valor.toUpperCase());
		};
	}
	public static Specification<TblPersona> conEstado(String strEstado) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstado);
			return cb.like(root.<String> get("estado"), valor);
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
