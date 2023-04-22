package com.pe.lima.sg.dao.mantenimiento;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseDAO;
import com.pe.lima.sg.entity.mantenimiento.TblTipoServicio;

public interface ITipoServicioDAO extends BaseDAO<TblTipoServicio, Integer> {
	
	@Query(value = "select * from mae.tbl_tipo_servicio where ( UPPER(nombre) LIKE '%'||UPPER(:nombre)||'%' OR :nombre IS NULL ) AND (estado = :estado OR :estado IS NULL ) ORDER BY nombre", nativeQuery = true)
	List<TblTipoServicio> findByNombreEstado(
										@Param("nombre") String strNombre,
										@Param("estado") Character chrEstado);

	@Query(value = "select * from mae.tbl_tipo_servicio where ( UPPER(nombre) LIKE '%'||UPPER(:filtro)||'%' OR :filtro IS NULL ) AND (estado = :filtro OR :filtro IS NULL )  ORDER BY nombre", nativeQuery = true)
	List<TblTipoServicio> listarConFiltros(@Param("filtro") String filtro);
	
	@Query(value = "select * from mae.tbl_tipo_servicio where ( UPPER(nombre) LIKE '%'||UPPER(:nombre)||'%' OR :nombre IS NULL ) AND estado = '1'  ORDER BY nombre", nativeQuery = true)
	List<TblTipoServicio> listarCriterios(	
										@Param("nombre") String strNombre);
	
	@Query(value = "select * from mae.tbl_tipo_servicio where estado = '1'  ORDER BY nombre", nativeQuery = true)
	List<TblTipoServicio> listarAllActivos();
	
	@Query(value = "select count(1) from mae.tbl_tipo_servicio where TRIM(UPPER(nombre)) = TRIM(UPPER(:nombre)) AND estado = '1' ", nativeQuery = true)
	Integer countOneByNombre(@Param("nombre") String strNombre);
	
	@Query(value = "select * from mae.tbl_tipo_servicio where UPPER(nombre) = UPPER(:nombre) AND estado = '1'  ORDER BY nombre", nativeQuery = true)
	List<TblTipoServicio> buscarOneByNombre(@Param("nombre") String strNombre);
	
	@Query(value = "select * from mae.tbl_tipo_servicio where estado = '1' AND rubro =:rubro ORDER BY nombre", nativeQuery = true)
	List<TblTipoServicio> listarAllActivosxRubro(@Param("rubro") String strRubro);
	
}
