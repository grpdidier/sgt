package com.pe.lima.sg.dao.caja;


import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.caja.TblCxcBitacora;

public final class CxCBitacoraSpecifications {
	
	private CxCBitacoraSpecifications() {}
	

	public static Specification<TblCxcBitacora> conAnio(Integer intAnio) {
		return (root, query, cb) -> {
			if (intAnio.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<String> get("anio"), intAnio);
		};
	}
	
	public static Specification<TblCxcBitacora> conContrato(Integer intCodigoContrato) {
		return (root, query, cb) -> {
			if (intCodigoContrato.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<Integer> get("codigoContrato"), intCodigoContrato);
		};
	}
	
	public static Specification<TblCxcBitacora> conCodigoContrato(Integer intCodigoContrato) {
		return (root, query, cb) -> {
			if (intCodigoContrato.compareTo(0) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<Integer> get("codigoContrato"), intCodigoContrato);
		};
	}
	
	public static Specification<TblCxcBitacora> conMes(Integer intMes) {
		return (root, query, cb) -> {
			if (intMes.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<String> get("mes"), intMes);
		};
	}

		
	public static Specification<TblCxcBitacora> conTipoCobro(String strTipoCobro) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strTipoCobro);
			return cb.like(root.<String> get("tipoCobro"), valor);
		};
	}
	
	public static Specification<TblCxcBitacora> conTipoOperacion(String strTipoOperacion) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strTipoOperacion);
			return cb.like(root.<String> get("tipoOperacion"), valor);
		};
	}
	
	public static Specification<TblCxcBitacora> conEstado(String strEstado) {
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
