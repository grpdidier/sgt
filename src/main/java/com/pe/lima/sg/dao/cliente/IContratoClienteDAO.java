package com.pe.lima.sg.dao.cliente;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.cliente.TblContratoCliente;

public interface IContratoClienteDAO extends BaseOperacionDAO<TblContratoCliente, Integer> {
	
	@Query(value = "select * from cli.tbl_contrato_cliente where estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblContratoCliente> listarAllActivos();
	
	@Query(value = "select * from cli.tbl_contrato_cliente where estado = '1' and codigo_contrato = :codigoContrato ORDER BY 1 ", nativeQuery = true)
	List<TblContratoCliente> listarAllActivosXContrato(@Param("codigoContrato") Integer intCodigoContrato);
	
	
	
}
