package com.pe.lima.sg.dao.cliente;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblArbitrio;

public interface IArbitrioDAO extends BaseOperacionDAO<TblArbitrio, Integer> {
	
	
	@Query(value = "select * from cli.tbl_arbitrio where anio = :anio AND estado = '1' ORDER BY anio, codigo_tienda, fecha_inicio", nativeQuery = true)
	List<TblArbitrio> listarCriterios(	
										@Param("anio") Integer intAnio);
	
	@Query(value = "select * from cli.tbl_arbitrio where estado = '1' ORDER BY anio, codigo_tienda, fecha_inicio ", nativeQuery = true)
	List<TblArbitrio> listarAllActivos();
	
	@Query(value = "select * from cli.tbl_arbitrio where estado = '1' ORDER BY codigo_arbitrio ", nativeQuery = true)
	List<TblArbitrio> listarAllActivosOrderByCodigo();
	
	@Query(value = "select * from cli.tbl_arbitrio where codigo_tienda = :codigoTienda AND estado_arbitrio = 'S' AND estado = '1' ORDER BY anio, codigo_tienda, fecha_inicio ", nativeQuery = true)
	ArrayList<TblArbitrio> listarAllActivosxTienda(@Param("codigoTienda") Integer intCodigoTienda);
	
	@Query(value = "select * from cli.tbl_arbitrio where codigo_contrato = :codigoContrato AND estado_arbitrio = 'S' AND estado = '1' ORDER BY anio, codigo_tienda, fecha_inicio ", nativeQuery = true)
	ArrayList<TblArbitrio> listarAllActivosxContrato(@Param("codigoContrato") Integer intCodigoCotrato);
	
	@Query(value = "select * from cli.tbl_arbitrio where codigo_tienda = :codigoTienda AND anio =:anio AND estado_arbitrio = 'S' AND estado = '1' ORDER BY anio, codigo_tienda, fecha_inicio ", nativeQuery = true)
	ArrayList<TblArbitrio> listarAllActivosxTiendaxAnio(@Param("codigoTienda") Integer intCodigoTienda, @Param("anio") Integer intAnio);
	
	@Query(value = "select * from cli.tbl_arbitrio where fecha_inicio <= :fechaArbitrio AND fecha_fin  >= :fechaArbitrio AND codigo_contrato > 0 AND estado_arbitrio = 'S' AND estado = '1' ORDER BY anio, codigo_tienda, fecha_inicio ", nativeQuery = true)
	ArrayList<TblArbitrio> listarAllActivosxFecha(@Param("fechaArbitrio") Date datFechaArbitrio);
	
	@Query(value = "select * from cli.tbl_arbitrio where codigo_contrato = :codigoContrato AND fecha_inicio <= :fechaArbitrio AND fecha_fin  >= :fechaArbitrio AND codigo_contrato > 0 AND estado_arbitrio = 'S' AND estado = '1' ORDER BY anio, codigo_tienda, fecha_inicio ", nativeQuery = true)
	ArrayList<TblArbitrio> listarAllActivosxFechaxContrato(@Param("codigoContrato") Integer intCodigoCotrato, @Param("fechaArbitrio") Date datFechaArbitrio);
	
}
