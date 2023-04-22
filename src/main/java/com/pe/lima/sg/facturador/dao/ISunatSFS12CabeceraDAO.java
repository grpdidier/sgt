package com.pe.lima.sg.facturador.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.facturador.entity.TblSunatCabecera;



public interface ISunatSFS12CabeceraDAO extends BaseOperacionDAO<TblSunatCabecera, Integer> {
	
	@Query(value = "select * from ope.tbl_sunat_cabecera where codigo_comprobante = :codigoComprobante AND estado = '1' ", nativeQuery = true)
	TblSunatCabecera findByCodigoDocumento( @Param("codigoComprobante") Integer intCoigoComprobante);
	
	
	
}
