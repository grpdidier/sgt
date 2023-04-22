package com.pe.lima.sg.dao.cliente;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblLuzxtienda;

public interface ILuzxTiendaDAO extends BaseOperacionDAO<TblLuzxtienda, Integer> {
	
	@Query(value = "select * from cli.tbl_luzxtienda where estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblLuzxtienda> listarAllActivos();
		
	
	@Query(value = "select * from cli.tbl_luzxtienda where codigo_luz = :codigoLuz AND codigo_tienda = :codigoTienda AND estado = '1' ", nativeQuery = true)
	TblLuzxtienda listarLuzTienda(@Param("codigoLuz") Integer intCodigoLuz,
								  @Param("codigoTienda") Integer intCodigoTienda);
	
	@Query(value = "select * from cli.tbl_luzxtienda where codigo_luzxtienda = :codigoLuzTienda and estado = '1' ", nativeQuery = true)
	TblLuzxtienda findLuzTienda(@Param("codigoLuzTienda") Integer intCodigoLuzTienda);
		
	@Query(value = "select * from cli.tbl_luzxtienda where codigo_luz = :codigoLuz AND estado = '1' ", nativeQuery = true)
	List<TblLuzxtienda> listarLuzTiendaxLuz(@Param("codigoLuz") Integer intCodigoLuz);
	
	@Query(value = "select * from cli.tbl_luzxtienda where codigo_tienda = :codigoTienda AND estado = '1' ", nativeQuery = true)
	List<TblLuzxtienda> listarLuzTiendaxTienda(@Param("codigoTienda") Integer intCodigoTienda);
	

	@Query(value = "select * from cli.tbl_luzxtienda where codigo_contrato = :codigoContrato AND estado = '1' ", nativeQuery = true)
	List<TblLuzxtienda> listarLuzTiendaxContrato(@Param("codigoContrato") Integer intCodigoContrato);
	
	@Query(value = "select * from cli.tbl_luzxtienda lxt, mae.tbl_tienda tie where lxt.codigo_tienda = tie.codigo_tienda AND tie.codigo_suministro = :codigoSuministro AND  lxt.fecha_fin = :fechaFin AND lxt.estado = '1' ", nativeQuery = true)
	List<TblLuzxtienda> listarLuzTiendaxSuministro( @Param("codigoSuministro") Integer intCodigoSuministro,
													@Param("fechaFin") Date datFechaFin);
	
	@Query(value = "select * from cli.tbl_luzxtienda lxt, mae.tbl_tienda tie where lxt.codigo_tienda = tie.codigo_tienda AND tie.codigo_suministro = :codigoSuministro AND  lxt.fecha_fin = :fechaFin AND  lxt.monto_generado > 0 AND lxt.estado = '1' ", nativeQuery = true)
	List<TblLuzxtienda> listarLuzTiendaxSuministroConMonto( @Param("codigoSuministro") Integer intCodigoSuministro,	@Param("fechaFin") Date datFechaFin);
	
	@Query(value = "select * from cli.tbl_luzxtienda lxt  where codigo_contrato = :codigoContrato AND lxt.fecha_fin = :fechaFin AND  lxt.monto_generado > 0 AND lxt.estado = '1' ", nativeQuery = true)
	List<TblLuzxtienda> listarLuzTiendaxSuministroConMontoxContrato( @Param("codigoContrato") Integer intCodigoContrato, @Param("fechaFin") Date datFechaFin);
	
	@Query(value = "select sum(monto_contrato) from cli.tbl_luzxtienda where codigo_luz = :codigoLuz AND estado = '1' ", nativeQuery = true)
	BigDecimal totalMontoContrato(@Param("codigoLuz") Integer intCodigoLuz);
	
	@Query(value = "select lxt.* from cli.tbl_luzxtienda lxt, mae.tbl_tienda tie where lxt.codigo_tienda = tie.codigo_tienda AND tie.codigo_tienda = :codigoTienda AND lxt.codigo_luz = :codigoLuz AND  lxt.fecha_fin = :fechaFin AND lxt.estado = '1' ", nativeQuery = true)
	TblLuzxtienda getTiendaxTiendaLuzFecha( @Param("codigoLuz") Integer intCodigoLuz,
														@Param("codigoTienda") Integer intCodigoTienda,
														@Param("fechaFin") Date datFechaFin);
	
}
