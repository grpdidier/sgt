package com.pe.lima.sg.dao.caja;


import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.caja.TblComprobanteSunat;

public final class ComprobanteOseSpecifications {
	
	private ComprobanteOseSpecifications() {}
	

	public static Specification<TblComprobanteSunat> conTipoPago(String strTipoPago) {
		return (root, query, cb) -> {
			if (strTipoPago.equals("-1")) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<String> get("tipoPago"), strTipoPago);
		};
	}
	
	
	public static Specification<TblComprobanteSunat> conSerie(String strSerie) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strSerie);
			return cb.like(root.<String> get("serie"), valor.toUpperCase());
		};
	}
	public static Specification<TblComprobanteSunat> conNumero(String strNumero) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strNumero);
			return cb.like(root.<String> get("numero"), valor.toUpperCase());
		};
	}
	public static Specification<TblComprobanteSunat> conTienda(String strTienda) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strTienda);
			return cb.like(root.<String> get("numeroTienda"), valor);
		};
	}
	public static Specification<TblComprobanteSunat> conEstado(String strEstado) {
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
