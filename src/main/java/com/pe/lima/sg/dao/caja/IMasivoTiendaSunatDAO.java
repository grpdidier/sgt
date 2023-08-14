package com.pe.lima.sg.dao.caja;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblMasivoTiendaSunat;


public interface IMasivoTiendaSunatDAO extends BaseOperacionDAO<TblMasivoTiendaSunat, Integer> {
	
	@Query(value = "select * from caj.tbl_masivo_tienda_sunat where codigo_masivo = :codigoMasivo AND estado = '1' ", nativeQuery = true)
	List<TblMasivoTiendaSunat> listarActivosxMasivo( @Param("codigoMasivo") Integer intCodigoMasivo);
	
	@Query(value = "select * from caj.tbl_masivo_tienda_sunat where codigo_masivo = :codigoMasivo AND numero_tienda = :numeroTienda and estado = '1' ", nativeQuery = true)
	TblMasivoTiendaSunat obtenerMasivoTiendaxPeriodo(@Param("codigoMasivo") Integer intCodigoMasivo, @Param("numeroTienda") String strNumeroTienda);
}
