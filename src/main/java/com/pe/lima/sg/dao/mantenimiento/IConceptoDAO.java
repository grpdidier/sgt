package com.pe.lima.sg.dao.mantenimiento;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.entity.mantenimiento.TblConcepto;

public interface IConceptoDAO extends BaseDAO<TblConcepto, Integer> {
	
	@Query(value = "select * from mae.tbl_concepto where ( UPPER(nombre) LIKE '%'||UPPER(:nombre)||'%' OR :nombre IS NULL ) AND (estado = :estado OR :estado IS NULL ) ORDER BY tipo, nombre", nativeQuery = true)
	List<TblConcepto> findByNombreEstado(
										@Param("nombre") String strNombre,
										@Param("estado") String chrEstado);
	
	@Query(value = "select * from mae.tbl_concepto where ( UPPER(nombre) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL )  AND ( UPPER(tipo) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL )AND (estado = :filtro OR :filtro IS NULL ) ORDER BY tipo, nombre", nativeQuery = true)
	List<TblConcepto> listarConFiltros(@Param("filtro") String filtro);

	
	@Query(value = "select * from mae.tbl_concepto where ( UPPER(nombre) LIKE '%'||UPPER(:nombre)||'%' OR :nombre IS NULL ) AND (tipo = :tipo OR :tipo = '-1') AND estado = '1' ORDER BY tipo, nombre", nativeQuery = true)
	List<TblConcepto> listarCriterios(	
										@Param("nombre") String strNombre,
										@Param("tipo") String strTipo);
	
	@Query(value = "select * from mae.tbl_concepto where estado = '1' ORDER BY tipo, nombre ", nativeQuery = true)
	List<TblConcepto> listarAllActivos();
	
	@Query(value = "select count(1) from mae.tbl_concepto where TRIM(UPPER(nombre)) = TRIM(UPPER(:nombre)) AND estado = '1' ", nativeQuery = true)
	Integer countOneByNombre(@Param("nombre") String strNombre);
	
	@Query(value = "select * from mae.tbl_concepto where UPPER(nombre) = UPPER(:nombre) AND estado = '1' ORDER BY tipo, nombre", nativeQuery = true)
	List<TblConcepto> buscarOneByNombre(@Param("nombre") String strNombre);
	
	@Query(value = "select * from mae.tbl_concepto where tipo =:tipo AND estado = '1' ORDER BY tipo, nombre", nativeQuery = true)
	List<TblConcepto> buscarxTipo(@Param("tipo") String strTipo);
	
}
