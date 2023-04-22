package com.pe.lima.sg.dao.caja;


import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;


import com.pe.lima.sg.entity.caja.TblCxcDocumento;

public final class CxCDocumentoSpecifications {
	
	private CxCDocumentoSpecifications() {}
	
	public static Specification<TblCxcDocumento> conCodigoReferencia(Integer intCodigoReferencia) {
		return (root, query, cb) -> {
			return cb.equal(root.<Integer> get("codigoReferencia"), intCodigoReferencia );
		};
	}
	
	public static Specification<TblCxcDocumento> conCodigoContrato(Integer intCodigoContrato) {
		return (root, query, cb) -> {
			return cb.equal(root.<Integer> get("codigoContrato"), intCodigoContrato );
		};
	}
	public static Specification<TblCxcDocumento> conSaldoPositivo(BigDecimal saldo) {
		return (root, query, cb) -> {
			return cb.greaterThan(root.<BigDecimal> get("saldo"), saldo );
		};
	}
	public static Specification<TblCxcDocumento> conSaldoCero(BigDecimal saldo) {
		return (root, query, cb) -> {
			return cb.equal(root.<BigDecimal> get("saldo"), saldo );
		};
	}
	public static Specification<TblCxcDocumento> conEstadoDocumento(String estado) {
		return (root, query, cb) -> {
			return cb.equal(root.<String> get("estado"), estado );
		};
	}
	
	public static Specification<TblCxcDocumento> conAnioActual(String fechaActual) {
		return (root, query, cb) -> {
			return cb.lessThanOrEqualTo(cb.function("to_char", String.class, root.get("fechaFin"), cb.literal("YYYYMM")) , fechaActual );
		};
	}
	public static Specification<TblCxcDocumento> conTipoReferencia(String strTipoReferencia) {
		return (root, query, cb) -> {
			return cb.equal(root.<String> get("tipoReferencia"), strTipoReferencia );
		};
	}
	
	public static Specification<TblCxcDocumento> conAnio(Integer intAnio) {
		return (root, query, cb) -> {
			if (intAnio.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<String> get("anio"), intAnio);
		};
	}
	
	public static Specification<TblCxcDocumento> conMes(Integer intMes) {
		return (root, query, cb) -> {
			if (intMes.compareTo(-1) == 0) {
				return cb.equal(cb.literal(1), 1);
			}
			return cb.equal(root.<String> get("mes"), intMes);
		};
	}

	
	/**
	 * Para la busqueda por Like %
	 * @param String
	 * @return String
	 */
	/*private static String obtenerValorString(String valor) {
		if (valor == null || valor.isEmpty()) {
			return "%";
		} else {
			return "%" + valor + "%";
		}
	}*/
}
