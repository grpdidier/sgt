package com.pe.lima.sg.dao.caja;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblSunatDetalle;
public interface ISunatDetalleDAO extends BaseOperacionDAO<TblSunatDetalle, Integer> {
	
	@Query(value = "select * from caj.tbl_sunat_detalle where codigo_cabecera = :codigoCabecera AND estado = '1' ", nativeQuery = true)
	List<TblSunatDetalle> listarSunatDetallexCabecera( @Param("codigoCabecera") Integer intCodigoCabecera);
	
	
}
