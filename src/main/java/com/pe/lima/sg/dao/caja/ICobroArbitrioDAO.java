package com.pe.lima.sg.dao.caja;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblCobroArbitrio;

public interface ICobroArbitrioDAO extends BaseOperacionDAO<TblCobroArbitrio, Integer> {
	
	@Query(value = "select * from caj.tbl_cobro_arbitrio where codigo_desembolso_arbitrio = :codigoDesembolso  ORDER BY 1 ", nativeQuery = true)
	List<TblCobroArbitrio> listarAllActivosxDesembolso(@Param("codigoDesembolso") Integer intCodigoDesembolso);
	
	
	@Query(value = "select count(1) from caj.tbl_cobro_arbitrio where codigo_arbitrio = :codigoArbitrio ", nativeQuery = true)
	Integer countCobroArbitrio(@Param("codigoArbitrio") Integer intCodigoArbitrio);
	
	@Query(value = "select sum(monto_soles) from caj.tbl_cobro_arbitrio where codigo_arbitrio = :codigoArbitrio and estado = '1' ", nativeQuery = true)
	BigDecimal montoCobroArbitrio(@Param("codigoArbitrio") Integer intCodigoArbitrio);
	
}
