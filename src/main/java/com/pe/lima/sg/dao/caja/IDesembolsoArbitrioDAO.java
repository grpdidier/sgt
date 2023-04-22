package com.pe.lima.sg.dao.caja;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblDesembolsoArbitrio;

public interface IDesembolsoArbitrioDAO extends BaseOperacionDAO<TblDesembolsoArbitrio, Integer> {
	
	@Query(value = "select * from caj.tbl_desembolso_arbitrio where identificacion = :identificacion AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	TblDesembolsoArbitrio getDesembolsoxIdentificacion(@Param("identificacion") String identificacion);
	
	@Query(value = "select * from caj.tbl_desembolso_arbitrio where identificacion = :codigoInterno ", nativeQuery = true)
	TblDesembolsoArbitrio getDesembolsoByCodigoInterno(@Param("codigoInterno") String codigoInterno);
	
	@Query(value = "select * from caj.tbl_desembolso_arbitrio where  (codigo_contrato = :codigo OR codigo_contrato in (select codigo_padre_contrato from cli.tbl_contrato where codigo_contrato = :codigo)) ORDER BY fecha_cobro DESC, codigo_desembolso_arbitrio DESC ", nativeQuery = true)
	List<TblDesembolsoArbitrio> listarAllCobroxTipo(@Param("codigo") Integer intCodigoContrato);
	
	@Query(value = "select * from caj.tbl_desembolso_arbitrio where estado = '1'  AND estado_operacion = :codigoOperacion ORDER BY 1 ", nativeQuery = true)
	List<TblDesembolsoArbitrio> listarAllRevesionxEstado(@Param("codigoOperacion") Integer intCodigoOperacion);
	
	@Query(value = "select * from caj.tbl_desembolso_arbitrio where codigo_desembolso_arbitrio = :id ", nativeQuery = true)
	TblDesembolsoArbitrio getDesembolsoArbitrioxId(@Param("id") Integer id);
	
}
