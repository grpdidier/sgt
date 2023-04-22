package com.pe.lima.sg.dao.cliente;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblAdelanto;

public interface IAdelantoDAO extends BaseOperacionDAO<TblAdelanto, Integer> {
	
	@Query(value = "select * from caj.tbl_adelanto where ((:fechaInicio IS NULL) or ((:fechaInicio IS NOT NULL) and (fecha_adelanto between to_date(:fechaInicio,'yyyy-MM-dd') and to_date(:fechaFin,'yyyy-MM-dd'))))  AND estado = '1'  ORDER BY fecha_adelanto", nativeQuery = true)
	List<TblAdelanto> listarCriterios(	
										@Param("fechaInicio") String datFechaInicio,
										@Param("fechaFin") String datFechaFin);
	
	@Query(value = "select * from caj.tbl_adelanto where estado = '1' ORDER BY fecha_adelanto, tipo_rubro ", nativeQuery = true)
	List<TblAdelanto> listarAllActivos();
	
	@Query(value = "select count(1) from caj.tbl_adelanto where fecha_adelanto = :fecha AND estado = '1' ", nativeQuery = true)
	Integer countOneByFecha(@Param("fecha") Date datFecha);
	
	@Query(value = "select * from caj.tbl_adelanto where fecha_adelanto = :fecha AND estado = '1'  ORDER BY fecha_adelanto", nativeQuery = true)
	List<TblAdelanto> buscarOneByFecha(@Param("fecha") Date datFecha);
	
	@Query(value = "select * from caj.tbl_adelanto where codigo_desembolso = :codigoDesembolso and tipo_rubro <> 'ARB'  ORDER BY 1 ", nativeQuery = true)
	List<TblAdelanto> listarAllActivosxDesembolso(@Param("codigoDesembolso") Integer intCodigoDesembolso);
	
	@Query(value = "select * from caj.tbl_adelanto where codigo_desembolso = :codigoDesembolso and tipo_rubro = 'ARB'  ORDER BY 1 ", nativeQuery = true)
	List<TblAdelanto> listarAllActivosxDesembolsoArbitrio(@Param("codigoDesembolso") Integer intCodigoDesembolso);
	
	@Query(value = "select * from caj.tbl_adelanto where codigo_contrato = :codigoContrato AND tipo_rubro NOT IN ('GAR') AND estado = '1'  ORDER BY 1 desc", nativeQuery = true)
	List<TblAdelanto> listarAllActivosxContrato(@Param("codigoContrato") Integer intCodigoContrato);
	
	@Query(value = "select * from caj.tbl_adelanto where codigo_contrato = :codigoContrato AND tipo_rubro = 'GAR' AND estado = '1'  ORDER BY 1 ", nativeQuery = true)
	List<TblAdelanto> listarAllActivosxContratoGarantia(@Param("codigoContrato") Integer intCodigoContrato);
	
	
	
}
