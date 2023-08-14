package com.pe.lima.sg.dao.caja;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblMasivoSunat;


public interface IMasivoSunatDAO extends BaseOperacionDAO<TblMasivoSunat, Integer> {
	
	@Query(value = "select count(1) from caj.tbl_masivo_sunat where periodo = :periodo AND codigo_edificio = :codigoEdificio AND estado = '1' ", nativeQuery = true)
	Integer existeMasivoxEmpresaxPeriodo( @Param("periodo") String strPeriodo, @Param("codigoEdificio")Integer intCodigoEmpresa);
		
}
