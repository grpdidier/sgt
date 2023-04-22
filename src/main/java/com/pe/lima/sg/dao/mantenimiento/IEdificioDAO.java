package com.pe.lima.sg.dao.mantenimiento;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;

public interface IEdificioDAO extends BaseDAO<TblEdificio, Integer> {
	
	@Query(value = "select * from mae.tbl_edificio where ( UPPER(nombre) LIKE '%'||UPPER(:nombre)||'%' OR :nombre IS NULL ) AND (estado = :estado OR :estado IS NULL ) ORDER BY 1", nativeQuery = true)
	List<TblEdificio> findByNombreEstado(
										@Param("nombre") String strNombre,
										@Param("estado") String strEstado);

	@Query(value = "select * from mae.tbl_edificio where ( UPPER(nombre) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND (estado = :filtro OR :filtro IS NULL ) ORDER BY 1", nativeQuery = true)
	List<TblEdificio> listarConFiltros(@Param("filtro") String filtro);
	
	@Query(value = "select * from mae.tbl_edificio where ( UPPER(nombre) LIKE '%'||UPPER(:nombre)||'%' OR :nombre IS NULL )  AND estado = '1' ORDER BY 1", nativeQuery = true)
	List<TblEdificio> listarCriterios(	
										@Param("nombre") String strNombre);
	
	@Query(value = "select * from mae.tbl_edificio where estado = '1' ", nativeQuery = true)
	List<TblEdificio> listarAllActivos();
	
	@Query(value = "select count(1) from mae.tbl_edificio where TRIM(UPPER(nombre)) = TRIM(UPPER(:nombre)) AND estado = '1' ", nativeQuery = true)
	Integer countOneByNombre(@Param("nombre") String strNombre);
	
	@Query(value = "select * from mae.tbl_edificio where UPPER(nombre) = UPPER(:nombre) AND estado = '1' ", nativeQuery = true)
	List<TblEdificio> buscarOneByNombre(@Param("nombre") String strNombre);
}
