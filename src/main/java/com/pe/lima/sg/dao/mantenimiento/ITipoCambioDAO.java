package com.pe.lima.sg.dao.mantenimiento;


import java.util.List;
import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.entity.mantenimiento.TblTipoCambio;

public interface ITipoCambioDAO extends BaseDAO<TblTipoCambio, Integer> {
	
	@Query(value = "select * from mae.tbl_tipo_cambio where ( fecha between :fechaInicio and :fechaFin ) AND (estado = :estado OR :estado IS NULL ) ORDER BY fecha", nativeQuery = true)
	List<TblTipoCambio> findByFechaEstado(
										@Param("fechaInicio") Date datFechaInicio,
										@Param("fechaFin") Date datFechaFin,
										@Param("estado") Character chrEstado);

	@Query(value = "select * from mae.tbl_tipo_cambio where ( fecha between :filtro and :filtro ) AND (estado = :filtro OR :filtro IS NULL )  ORDER BY fecha", nativeQuery = true)
	List<TblTipoCambio> listarConFiltros(@Param("filtro") String filtro);
	
	@Query(value = "select * from mae.tbl_tipo_cambio where ((:fechaInicio IS NULL) or ((:fechaInicio IS NOT NULL) and (fecha between to_date(:fechaInicio,'yyyy-MM-dd') and to_date(:fechaFin,'yyyy-MM-dd'))))  AND estado = '1'  ORDER BY fecha desc", nativeQuery = true)
	List<TblTipoCambio> listarCriterios(	
										@Param("fechaInicio") String datFechaInicio,
										@Param("fechaFin") String datFechaFin);
	
	@Query(value = "select * from mae.tbl_tipo_cambio where estado = '1'  ORDER BY fecha", nativeQuery = true)
	List<TblTipoCambio> listarAllActivos();
	
	@Query(value = "select count(1) from mae.tbl_tipo_cambio where fecha = :fecha AND estado = '1' ", nativeQuery = true)
	Integer countOneByFecha(@Param("fecha") Date datFecha);
	
	@Query(value = "select * from mae.tbl_tipo_cambio where fecha = :fecha AND estado = '1'  ORDER BY fecha", nativeQuery = true)
	List<TblTipoCambio> buscarOneByFecha(@Param("fecha") Date datFecha);
	
	@Query(value="select * from mae.tbl_tipo_cambio order by 1 desc limit 1", nativeQuery = true)
	TblTipoCambio obtenerUltimoTipoCambio();
}
