package com.pe.lima.sg.dao.cliente;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.pe.lima.sg.entity.cliente.TblContrato;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblPersona;
import com.pe.lima.sg.entity.mantenimiento.TblTienda;

public final class ContratoSpecifications {
	
	private ContratoSpecifications() {}
	
	public static Specification<TblContrato> conNombre(String strNombre) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strNombre);
			return cb.like( cb.upper(root.<TblPersona> get("tblPersona").<String>get("nombre")), valor.toUpperCase());
		};
	}
	
	public static Specification<TblContrato> conPaterno(String strPaterno) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strPaterno);
			return cb.like(cb.upper(root.<TblPersona> get("tblPersona").<String> get("paterno")), valor.toUpperCase());
		};
	}
	
	public static Specification<TblContrato> conMaterno(String strMaterno) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strMaterno);
			return cb.like(cb.upper(root.<TblPersona> get("tblPersona").<String> get("materno")), valor.toUpperCase());
		};
	}
	
	public static Specification<TblContrato> conEdificio(Integer intCodigoEdificio) {
		return (root, query, cb) -> {
			if (intCodigoEdificio != null && intCodigoEdificio > 0) {
				return cb.equal(
						root.<TblTienda> get("tblTienda").<TblEdificio>get("tblEdificio").<Integer> get("codigoEdificio"),	intCodigoEdificio);
			} else {
				return cb.equal(cb.literal(1), 1);
			}
		};
	}
	public static Specification<TblContrato> conTienda(String strNumero) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strNumero);
			return cb.like(cb.upper(root.<TblTienda> get("tblTienda").<String>get("numero")), valor.toUpperCase());
		};
	}
	
	public static Specification<TblContrato> conRuc(String strRuc) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strRuc);
			return cb.like(cb.upper(root.<TblPersona> get("tblPersona").<String> get("numeroRuc")), valor.toUpperCase());
		};
	}
	
	public static Specification<TblContrato> conRazonSocial(String strRazonSocial) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strRazonSocial);
			return cb.like(cb.upper(root.<TblPersona> get("tblPersona").<String> get("razonSocial")), valor.toUpperCase());
		};
	}
	
	public static Specification<TblContrato> conEstado(String strEstado) {
		return (root, query, cb) -> {
			String valor = obtenerValorString(strEstado);
			return cb.like(root.<String> get("estado"), valor);
		};
	}
	
	public static Specification<TblContrato> conEstadoContrato(String strEstadoContrato) {
		return (root, query, cb) -> {
			return cb.equal(root.<String> get("estadoContrato"), strEstadoContrato);
		};
	}
	
	public static Specification<TblContrato> conListaEstadoContrato(List<String> listaEstado) {
		return (root, query, cb) -> {
			return root.get("estadoContrato").in(listaEstado);
			//return cb.in(root.<String> get("estadoContrato"), listaEstado);
		};
	}
	
	public static Specification<TblContrato> conFechaFinContrato(Date datFechaFin) {
		return (root, query, cb) -> {
			if (datFechaFin != null){
				return cb.lessThanOrEqualTo(root.<Date> get("fechaFin"), datFechaFin);
			}else{
				return cb.equal(cb.literal(1), 1);
			}
			
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
