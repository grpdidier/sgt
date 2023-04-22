package com.pe.lima.sg.dao.mantenimiento;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;

public interface ISuministroDAO extends BaseDAO<TblSuministro, Integer> {
	
	@Query(value = "select * from mae.tbl_suministro where ( UPPER(numero) LIKE '%'||UPPER(:numero)||'%' OR :numero IS NULL ) AND (estado = :estado OR :estado IS NULL ) ORDER BY numero", nativeQuery = true)
	List<TblSuministro> findByNumeroEstado(
										@Param("numero") String strNumero,
										@Param("estado") String chrEstado);

	@Query(value = "select * from mae.tbl_suministro where ( UPPER(nombre) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND (estado = :filtro OR :filtro IS NULL ) ORDER BY numero", nativeQuery = true)
	List<TblSuministro> listarConFiltros(@Param("filtro") String filtro);
	
	@Query(value = "select * from mae.tbl_suministro where ( UPPER(numero) LIKE '%'||UPPER(:numero)||'%' OR :numero IS NULL ) AND (estado_suministro = :estadoSuministro OR :estadoSuministro = '-1') AND estado = '1' ORDER BY numero", nativeQuery = true)
	List<TblSuministro> listarCriterios(	
										@Param("numero") String strNumero,
										@Param("estadoSuministro") String estadoSuministro);
	
	@Query(value = "select * from mae.tbl_suministro where estado = '1'  ORDER BY numero", nativeQuery = true)
	List<TblSuministro> listarAllActivos();
	
	@Query(value = "select count(1) from mae.tbl_suministro where TRIM(UPPER(numero)) = TRIM(UPPER(:numero)) AND estado = '1' ", nativeQuery = true)
	Integer countOneByNumero(@Param("numero") String strNumero);
	
	@Query(value = "select * from mae.tbl_suministro where UPPER(numero) = UPPER(:numero) AND estado = '1'  ORDER BY numero", nativeQuery = true)
	List<TblSuministro> buscarOneByNumero(@Param("numero") String strNumero);
	
	@Query(value = "select * from mae.tbl_suministro where estado_suministro = 'S' AND estado = '1'  ORDER BY numero", nativeQuery = true)
	List<TblSuministro> listarAllActivosDesocupado();
}
