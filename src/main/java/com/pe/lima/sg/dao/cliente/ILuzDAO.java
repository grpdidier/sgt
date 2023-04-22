package com.pe.lima.sg.dao.cliente;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblLuz;

public interface ILuzDAO extends BaseOperacionDAO<TblLuz, Integer> {
	
	@Query(value = "select * from cli.tbl_luz where estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblLuz> listarAllActivos();
		
	@Query(value = "select * from cli.tbl_luz where codigo_suministro = :codigoSuministro AND fecha_fin = :fechaFin AND estado = '1' ", nativeQuery = true)
	TblLuz findBySuministroFechaVencimiento( @Param("codigoSuministro") Integer intCodigoSuministro,
											 @Param("fechaFin") Date datFechaFin);
	
	@Query(value = "select count(1) from cli.tbl_luz where codigo_suministro = :codigoSuministro AND fecha_fin = :fechaFin AND estado = '1' ", nativeQuery = true)
	Integer countBySuministroFechaVencimiento( @Param("codigoSuministro") Integer intCodigoSuministro,
											   @Param("fechaFin") Date datFechaFin);


	@Query(value = "select * from cli.tbl_luz where to_char(fecha_fin,'yyyy')  = :anio AND to_char(fecha_fin,'MM') = :mes AND estado = '1' ", nativeQuery = true)
	List<TblLuz> listarxAnioMes( @Param("anio") String strAnio,  @Param("mes") String strMes);
	
	@Query(value = "select * from cli.tbl_luz where codigo_luz = :codigoLuz AND estado = '1' ", nativeQuery = true)
	TblLuz findByLuz( @Param("codigoLuz") Integer intCodigoLuz);
	
}
