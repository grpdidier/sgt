package com.pe.lima.sg.dao.cliente;


import java.util.List;
import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblContrato;

public interface IContratoDAO extends BaseOperacionDAO<TblContrato, Integer> {
	
	@Query(value = "select count(1) from cli.tbl_contrato ", nativeQuery = true)
	Integer countContrato();
	
	@Query(value = "select * from cli.tbl_contrato where numero = :numeroContrato ", nativeQuery = true)
	TblContrato findByNumeroContrato( @Param("numeroContrato") String strNumeroContrato);

	@Query(value = "select * from cli.tbl_contrato where codigo_contrato = :codigoContrato ", nativeQuery = true)
	TblContrato findByCodigoContrato( @Param("codigoContrato") int intCodigoContrato);
	
	@Query(value = "select * from cli.tbl_contrato where codigo_tienda = :codigoTienda AND estado = '1' AND estado_contrato IN ('VGN', 'RNV', 'PND') order by codigo_tienda , codigo_contrato desc, estado_contrato desc LIMIT 1", nativeQuery = true)
	TblContrato findByNumeroTienda( @Param("codigoTienda") Integer intCodigoTienda);
	
	@Query(value = "select * from cli.tbl_contrato where estado = '1' AND estado_contrato IN ('VGN', 'RNV') ", nativeQuery = true)
	List<TblContrato> listAllContratoActivos( );
	
	@Query(value = "select * from cli.tbl_contrato where fecha_inicio<= :fecha AND fecha_fin >= :fecha AND estado = '1' AND estado_contrato IN ('VGN', 'RNV', 'PND') and monto_alquiler > 0", nativeQuery = true)
	List<TblContrato> listAllContratoActivosxFecha( @Param("fecha") Date datFecha);
	
	@Query(value = "select * from cli.tbl_contrato where codigo_tienda = :codigoTienda AND estado = '1' AND estado_contrato IN ('VGN', 'RNV', 'PND') order by codigo_tienda , codigo_contrato desc, estado_contrato desc LIMIT 1", nativeQuery = true)
	TblContrato findByNumeroTiendaParaFacturar( @Param("codigoTienda") Integer intCodigoTienda);
	
	
	@Query(value = "select * from cli.tbl_contrato where codigo_contrato = :codigoContrato AND fecha_inicio<= :fecha AND fecha_fin >= :fecha AND estado = '1' AND estado_contrato IN ('VGN', 'RNV', 'PND')  and monto_alquiler > 0", nativeQuery = true)
	List<TblContrato> listAllContratoActivosxFechaxContrato(@Param("codigoContrato") int intCodigoContrato, @Param("fecha") Date datFecha);
	
	@Query(value = "select * from cli.tbl_contrato where codigo_contrato = :codigoContrato ", nativeQuery = true)
	List<TblContrato> listAllContratoActivosxCodigoContrato(@Param("codigoContrato") int intCodigoContrato);
	
	@Query(value = "select * from cli.tbl_contrato where codigo_tienda = :codigoTienda AND estado = '1' AND estado_contrato IN ('VGN', 'PND') order by codigo_tienda , codigo_contrato desc, estado_contrato desc LIMIT 1", nativeQuery = true)
	TblContrato findByNumeroTiendaParaArbitrios( @Param("codigoTienda") Integer intCodigoTienda);
	
}
