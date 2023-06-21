package com.pe.lima.sg.dao.caja;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;

public interface IDetalleFormaPagoOseDAO extends BaseOperacionDAO<TblDetalleFormaPago, Integer> {
	
	@Query(value = "select * from caj.tbl_detalle_forma_pago where codigo_comprobante = :codigo ", nativeQuery = true)
	List<TblDetalleFormaPago> listarAllxComprobante(@Param("codigo") Integer intCodigoComprobante);

	
	
}
