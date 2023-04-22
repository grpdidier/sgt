package com.pe.lima.sg.dao.mantenimiento;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.mantenimiento.TblSerie;

public interface ISerieDAO extends BaseOperacionDAO<TblSerie, Integer> {
	
	@Query(value = "select * from mae.tbl_serie where tipo_comprobante = :tipoComprobante AND estado = '1' ", nativeQuery = true)
	TblSerie buscarOneByTipoComprobante(@Param("tipoComprobante") String strTipoComprobante);
	
}
