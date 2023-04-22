package com.pe.lima.sg.dao.caja;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblDetalleCajaChica;

public interface ICajaChicaDetalleDAO extends BaseOperacionDAO<TblDetalleCajaChica, Integer> {
	@Query(value = "select * from caj.tbl_detalle_caja_chica where codigo_caja_chica = :codigoCajaChica  AND estado = '1' ORDER BY fecha DESC ", nativeQuery = true)
	List<TblDetalleCajaChica> listarAllActivosxCajaChica(@Param("codigoCajaChica") Integer intCodigoCajaChica);
	
	@Query(value = "select * from caj.tbl_detalle_caja_chica where codigo_caja_chica = :codigoCajaChica AND (:tipoOperacion = '-1' or (:tipoOperacion <> '-1' and tipo_operacion = :tipoOperacion) )  AND estado = '1' ORDER BY fecha DESC ", nativeQuery = true)
	List<TblDetalleCajaChica> listarAllActivosxCajaChicaxTipoOperacionxConcepto(@Param("codigoCajaChica") Integer intCodigoCajaChica, @Param("tipoOperacion") String intTipoOperacion);
	
}
