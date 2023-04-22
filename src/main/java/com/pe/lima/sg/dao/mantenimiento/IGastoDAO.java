package com.pe.lima.sg.dao.mantenimiento;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.mantenimiento.TblGasto;

public interface IGastoDAO extends BaseOperacionDAO<TblGasto, Integer> {
	
	@Query(value = "select * from caj.tbl_gasto where codigo_interno = :codigo AND estado = '1' ", nativeQuery = true)
	List<TblGasto> buscarxCodigoInterno(@Param("codigo") String codigoInterno);
	
}
