package com.pe.lima.sg.facturador.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.facturador.entity.TblSunatTributoGeneral;


public interface ISunatTributoGeneralDAO extends BaseOperacionDAO<TblSunatTributoGeneral, Integer> {
	
	@Query(value = "select * from ope.tbl_sunat_tributo_general where codigo_cabecera = :codigoCabecera AND estado = '1' ", nativeQuery = true)
	List<TblSunatTributoGeneral> findByCodigoCabecera(@Param("codigoCabecera") int intCodigoCabecera);
}
