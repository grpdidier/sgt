package com.pe.lima.sg.dao.cliente;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblContratoServicio;

public interface IContratoServicioDAO extends BaseOperacionDAO<TblContratoServicio, Integer> {
	
	@Query(value = "select * from cli.tbl_contrato_servicio where estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblContratoServicio> listarAllActivos();
		
	@Query(value = "select * from cli.tbl_contrato_servicio where estado = '1' and codigo_contrato = :codigoContrato ORDER BY 1 ", nativeQuery = true)
	List<TblContratoServicio> listarAllActivosXContrato(@Param("codigoContrato") Integer intCodigoContrato);
	
	@Query(value = "select cs.* from cli.tbl_contrato_servicio cs, cli.tbl_contrato cn where cs.codigo_contrato = cn.codigo_contrato AND cn.fecha_inicio<= :fecha AND cn.fecha_fin >= :fecha AND cs.estado = '1' AND estado_contrato IN ('VGN', 'RNV') ORDER BY 1 ", nativeQuery = true)
	List<TblContratoServicio> listarAllActivosxFecha( @Param("fecha") Date datFecha);
	
	@Query(value = "select cs.* from cli.tbl_contrato_servicio cs, cli.tbl_contrato cn where cs.codigo_contrato = cn.codigo_contrato AND cn.fecha_inicio<= :fecha AND cn.fecha_fin >= :fecha AND cs.estado = '1' AND estado_contrato IN ('VGN', 'RNV') AND cn.codigo_contrato = :codigoContrato ", nativeQuery = true)
	List<TblContratoServicio> listarAllActivosxFechaxContrato( @Param("codigoContrato") Integer intCodigoContrato, @Param("fecha") Date datFecha);
	
}
