package com.pe.lima.sg.dao.seguridad;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.seguridad.TblUsuarioPermiso;

public interface IUsuarioPermisoDAO extends BaseOperacionDAO<TblUsuarioPermiso, Integer> {
	
	
	@Query(value = "select * from seg.tbl_usuario_permiso where codigo_usuario = :codigoUsuario and codigo_permiso = :codigoPermiso ", nativeQuery = true)
	TblUsuarioPermiso findOneByUsuarioPermiso(@Param("codigoUsuario") Integer codigoUsuario, @Param("codigoPermiso") Integer codigoPermiso);

}
