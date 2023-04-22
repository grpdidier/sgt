package com.pe.lima.sg.dao.cliente;


import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.cliente.TblArbitrio;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;

public final class ArbitriosSpecifications {
	
	private ArbitriosSpecifications() {}
	
	public static Specification<TblArbitrio> conCodigoInmueble(Integer intCodigoInmueble) {
		return (root, query, cb) -> {
			if (intCodigoInmueble.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<TblTienda> get("tblTienda").<TblEdificio> get("tblEdificio").<Integer>get("codigoEdificio"), intCodigoInmueble);
		};
	}
	
	public static Specification<TblArbitrio> conAnio(Integer intAnio) {
		return (root, query, cb) -> {
			if (intAnio == null || intAnio.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<String> get("anio"), intAnio);
		};
	}
	
	public static Specification<TblArbitrio> conCodigoTienda(Integer intCodigoTienda) {
		return (root, query, cb) -> {
			return cb.equal(root.<TblTienda> get("tblTienda").<Integer>get("codigoTienda"), intCodigoTienda);
		};
	}
	
	public static Specification<TblArbitrio> conSaldoPositivoArbitrio(BigDecimal saldo) {
		return (root, query, cb) -> {
			return cb.greaterThan(root.<BigDecimal> get("saldo"), saldo );
		};
	}
	
	public static Specification<TblArbitrio> conLocal(String strLocal) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strLocal);
			return cb.like(root.<TblTienda> get("tblTienda").<String> get("numero"), valor);
		};
	}
	
	public static Specification<TblArbitrio> conEstadoTienda(String strEstado) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstado);
			return cb.like(root.<TblTienda> get("tblTienda").<String> get("estado"), valor);
		};
	}
	
	public static Specification<TblArbitrio> conEstadoArbitrio(String strEstado) {
		return (root, query, cb) -> {
			return cb.equal(root.<String> get("estado"), strEstado);
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
