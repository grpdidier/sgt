package com.pe.lima.sg.dao.caja;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblSunatCabecera;


public interface ISunatCabeceraDAO extends BaseOperacionDAO<TblSunatCabecera, Integer> {
	
		
	@Query(value = "select * from caj.tbl_sunat_cabecera where codigo_cxc_doc = :cxcDocumento AND estado = '1' ", nativeQuery = true)
	TblSunatCabecera findByCodigoDocumento( @Param("cxcDocumento") Integer intCxcDocumento);
	
	@Query(value = "select * from caj.tbl_sunat_cabecera where codigo_cxc_doc = :cxcDocumento AND estado = '1' ", nativeQuery = true)
	List<TblSunatCabecera> listarSunatCabeceraxDocumento( @Param("cxcDocumento") Integer intCxcDocumento);
	
}
