package com.pe.lima.sg.dao.cliente;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblContratoPrimerCobro;

public interface IContratoPrimerCobroDAO extends BaseOperacionDAO<TblContratoPrimerCobro, Integer> {
	
	@Query(value = "select * from cli.tbl_contrato_primer_cobro where estado = '1' and codigo_contrato = :codigoContrato ORDER BY 1 ", nativeQuery = true)
	List<TblContratoPrimerCobro> listarAllActivosXContrato(@Param("codigoContrato") Integer intCodigoContrato);
	
	@Query(value = "select * from cli.tbl_contrato_primer_cobro where estado = '1' and codigo_tipo_servicio = 1 and codigo_contrato = :codigoContrato ORDER BY 1 ", nativeQuery = true)
	List<TblContratoPrimerCobro> listarServicioXContrato(@Param("codigoContrato") Integer intCodigoContrato);
	
}
