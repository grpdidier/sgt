package com.pe.lima.sg.dao.caja;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblApiLlamada;

public interface IApiLlamadaDAO extends BaseOperacionDAO<TblApiLlamada, Integer> {
	
	@Query(value = "select * from caj.tbl_api_llamada where codigo_comprobante =:codigoContrato  ORDER BY 1 ", nativeQuery = true)
	List<TblApiLlamada> findByCodigoComprobante(@Param("codigoContrato") Integer codigoComprobante);
	
	
}
