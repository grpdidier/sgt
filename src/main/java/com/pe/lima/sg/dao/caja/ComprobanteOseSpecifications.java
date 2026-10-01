package com.pe.lima.sg.dao.caja;


import javax.persistence.criteria.Expression;

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
	
	/**
	 * Filtra por año y mes sobre fecha_emision (formato yyyy-MM-dd).
	 * Si anio o mes vienen vacíos, se ignora ese criterio.
	 */
	public static Specification<TblComprobanteSunat> conAnioMes(String anio, String mes) {
		return (root, query, cb) -> {
			boolean tieneAnio = anio != null && !anio.trim().isEmpty() && !anio.trim().equals("-1");
			boolean tieneMes  = mes  != null && !mes.trim().isEmpty()  && !mes.trim().equals("-1");

			if (!tieneAnio && !tieneMes) {
				return cb.conjunction(); // sin filtro
			}

			Expression<String> fecha = root.<String> get("fechaEmision");

			if (tieneAnio && tieneMes) {
				String mesFmt = String.format("%02d", Integer.parseInt(mes.trim()));
				// yyyy-MM-%
				return cb.like(fecha, anio.trim() + "-" + mesFmt + "-%");
			}
			if (tieneAnio) {
				// yyyy-%
				return cb.like(fecha, anio.trim() + "-%");
			}
			// solo mes: ____-MM-%
			String mesFmt = String.format("%02d", Integer.parseInt(mes.trim()));
			return cb.like(fecha, "____-" + mesFmt + "-%");
		};
	}
}
