package com.pe.lima.sg.dao.seguridad;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.seguridad.TblPermiso;

public interface IPermisoDAO extends BaseOperacionDAO<TblPermiso, Integer> {
	
	
	@Query(value = "select * from seg.tbl_permiso where estado = '1' ", nativeQuery = true)
	List<TblPermiso> listarAllActivos();

	@Query(value = "select * from seg.tbl_permiso where codigo_permiso = :codigoPermiso ", nativeQuery = true)
	TblPermiso findOneByCodigo(@Param("codigoPermiso") Integer codigoPermiso);
}
