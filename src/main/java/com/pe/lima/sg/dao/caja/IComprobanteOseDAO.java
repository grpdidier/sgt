package com.pe.lima.sg.dao.caja;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblComprobanteSunat;

public interface IComprobanteOseDAO extends BaseOperacionDAO<TblComprobanteSunat, Integer> {
	
	@Query(value = "select com.* from caj.tbl_comprobante_sunat com, caj.tbl_masivo_sunat mas, caj.tbl_masivo_tienda_sunat mti where com.codigo_comprobante = mti.codigo_comprobante and mti.codigo_masivo = mas.codigo_masivo and mas.codigo_edificio = :codigoEdificio and mas.periodo = :periodo and mas.tipo_masivo = 'ALQUILER'  AND com.estado = '1' and mas.codigo_masivo = :codigoMasivo ORDER BY 1 ", nativeQuery = true)
	List<TblComprobanteSunat> listarComprobantexEdificioxPeriodoAlquiler(@Param("codigoEdificio") int intCodigoEdificio, @Param("periodo") String strPeriodo, @Param("codigoMasivo") int intCodigoMasivo);

	@Query(value = "select com.* from caj.tbl_comprobante_sunat com, caj.tbl_masivo_sunat mas, caj.tbl_masivo_tienda_sunat mti where com.codigo_comprobante = mti.codigo_comprobante and mti.codigo_masivo = mas.codigo_masivo and mas.codigo_edificio = :codigoEdificio and mas.periodo = :periodo and mas.tipo_masivo = 'SERVICIO'  AND com.estado = '1' and mas.codigo_masivo = :codigoMasivo ORDER BY 1 ", nativeQuery = true)
	List<TblComprobanteSunat> listarComprobantexEdificioxPeriodoServicio(@Param("codigoEdificio") int intCodigoEdificio, @Param("periodo") String strPeriodo, @Param("codigoMasivo") int intCodigoMasiv);

	
}
