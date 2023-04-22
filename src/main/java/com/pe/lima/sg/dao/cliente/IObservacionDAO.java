package com.pe.lima.sg.dao.cliente;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblObservacion;

public interface IObservacionDAO extends BaseOperacionDAO<TblObservacion, Integer> {
	
	@Query(value = "select * from cli.tbl_observacion where codigo_contrato = :codigoContrato AND estado = '1' ", nativeQuery = true)
	List<TblObservacion> listarObservacionxContrato(@Param("codigoContrato") Integer intCodigoContrato);
	
	
}
