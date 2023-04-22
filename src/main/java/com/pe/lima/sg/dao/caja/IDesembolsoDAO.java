package com.pe.lima.sg.dao.caja;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblDesembolso;

public interface IDesembolsoDAO extends BaseOperacionDAO<TblDesembolso, Integer> {
	
	@Query(value = "select * from caj.tbl_desembolso where estado = '1' AND estado_operacion <= 2 ORDER BY 1 ", nativeQuery = true)
	List<TblDesembolso> listarAllActivos();
		
	@Query(value = "select * from caj.tbl_desembolso where codigo_contrato = :codigoContrato AND estado = '1'  AND estado_operacion <= 3 ORDER BY 1 ", nativeQuery = true)
	List<TblDesembolso> listarAllActivosxContrato(@Param("codigoContrato") Integer intCodigoContrato);
	
	@Query(value = "select * from caj.tbl_desembolso where identificacion = :identificacion AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	TblDesembolso getDesembolsoxIdentificacion(@Param("identificacion") String identificacion);
	
	@Query(value = "select * from caj.tbl_desembolso where estado = '1'  AND estado_operacion = :codigoOperacion ORDER BY 1 ", nativeQuery = true)
	List<TblDesembolso> listarAllRevesionxEstado(@Param("codigoOperacion") Integer intCodigoOperacion);
	
	@Query(value = "select * from caj.tbl_desembolso where identificacion = :codigoInterno ", nativeQuery = true)
	TblDesembolso getDesembolsoByCodigoInterno(@Param("codigoInterno") String codigoInterno);
	
	@Query(value = "select * from caj.tbl_desembolso where tipo_cobro = :tipo and (codigo_contrato = :codigo OR codigo_contrato in (select codigo_padre_contrato from cli.tbl_contrato where codigo_contrato = :codigo)) ORDER BY fecha_cobro DESC, codigo_desembolso DESC ", nativeQuery = true)
	List<TblDesembolso> listarAllCobroxTipo(@Param("codigo") Integer intCodigoContrato, @Param("tipo") String tipoCobro);
	
}
