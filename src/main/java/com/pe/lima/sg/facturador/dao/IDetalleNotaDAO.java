package com.pe.lima.sg.facturador.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.facturador.entity.TblDetalleNota;

public interface IDetalleNotaDAO extends BaseOperacionDAO<TblDetalleNota, Integer> {
	
	@Query(value = "select * from ope.tbl_detalle_nota where codigo_nota = :codigoNota AND estado = '1' ", nativeQuery = true)
	List<TblDetalleNota> listarxNotaDetalle(@Param("codigoNota") Integer intCodigoNota);
	
}
