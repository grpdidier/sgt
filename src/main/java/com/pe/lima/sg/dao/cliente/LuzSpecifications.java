package com.pe.lima.sg.dao.cliente;


import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.cliente.TblLuz;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;

public final class LuzSpecifications {
	
	private LuzSpecifications() {}
	
	public static Specification<TblLuz> conNumero(String strNumero) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strNumero);
			return cb.equal(root.<TblSuministro> get("tblSuministro").<String> get("numero"), valor);
		};
	}
	
	public static Specification<TblLuz> conFechaFin(Date datFechaFin) {
		return (root, query, cb) -> {
			return cb.equal(root.<Date> get("fechaFin"), datFechaFin);
		};
	}
	
	
	public static Specification<TblLuz> conEstado(String strEstado) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstado);
			return cb.like(root.<TblTienda> get("tblTienda").<String> get("estado"), valor);
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
