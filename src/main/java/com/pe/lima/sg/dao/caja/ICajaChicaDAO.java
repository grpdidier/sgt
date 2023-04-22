package com.pe.lima.sg.dao.caja;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblCajaChica;

public interface ICajaChicaDAO extends BaseOperacionDAO<TblCajaChica, Integer> {
	
	@Query(value = "select count(1) from caj.tbl_caja_chica where anio = :anio AND mes = :mes AND nombre = :nombre AND estado = '1' ", nativeQuery = true)
	Integer countAnioMesNombreCajaChica( @Param("anio") Integer intAnio, @Param("mes")Integer intMes, @Param("nombre") String strNombre);
	
	@Query(value = "select * from caj.tbl_caja_chica where anio = :anio AND mes = :mes AND nombre = :nombre AND estado = '1' ", nativeQuery = true)
	List<TblCajaChica> listarAnioMesNombreCajaChica( @Param("anio") Integer intAnio, @Param("mes")Integer intMes, @Param("nombre") String strNombre);
	
}
