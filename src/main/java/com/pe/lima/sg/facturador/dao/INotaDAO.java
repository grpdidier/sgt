package com.pe.lima.sg.facturador.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.facturador.entity.TblNota;


public interface INotaDAO extends BaseOperacionDAO<TblNota, Integer> {
	@Query(value = "select * from ope.tbl_nota where codigo_verificacion = :codigoVerificacion AND estado = '1' ", nativeQuery = true)
	TblNota obtenerNota(@Param("codigoVerificacion") String strCodigoVerificacion);
	
	@Query(value = "select count(1)  from ope.tbl_nota where tipo_comprobante = :tipoComprobante AND serie = :serie AND numero = :numero AND estado = '1' ", nativeQuery = true)
	Integer totalNota(@Param("tipoComprobante") String strTipoComprobante, @Param("serie") String strSerie, @Param("numero") String strNumero);
	
	@Query(value = "select * from ope.tbl_nota where fecha_emision like :anioMes   AND estado = '1' ", nativeQuery = true)
	List<TblNota> findAllxAnioMes(@Param("anioMes") String strAnioMes);
	
}
