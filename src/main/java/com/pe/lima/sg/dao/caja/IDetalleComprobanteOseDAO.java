package com.pe.lima.sg.dao.caja;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblDetalleComprobante;

public interface IDetalleComprobanteOseDAO extends BaseOperacionDAO<TblDetalleComprobante, Integer> {
	
	@Query(value = "select * from caj.tbl_detalle_comprobante_sunat det where codigo_comprobante = :codigo ", nativeQuery = true)
	TblDetalleComprobante getDetallexComprobante(@Param("codigo") Integer intCodigoComprobante);

	
}
