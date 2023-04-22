package com.pe.lima.sg.dao.caja;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblCxcBitacora;

public interface ICxCBitacoraDAO extends BaseOperacionDAO<TblCxcBitacora, Integer> {
	
	@Query(value = "select count(1) from caj.tbl_cxc_bitacora where anio = :anio AND mes = :mes AND tipo_cobro = :tipoCobro AND estado = '1' ", nativeQuery = true)
	Integer countTipoCobro( @Param("anio") Integer intAnio, @Param("mes")Integer intMes, @Param("tipoCobro") String strTipoCobro);
	
	@Query(value = "select count(1) from caj.tbl_cxc_bitacora where codigo_contrato = :codigoContrato AND anio = :anio AND mes = :mes AND tipo_cobro = :tipoCobro AND estado = '1' ", nativeQuery = true)
	Integer countTipoCobroxContrato( @Param("codigoContrato") Integer intCodigoContrato, @Param("anio") Integer intAnio, @Param("mes")Integer intMes, @Param("tipoCobro") String strTipoCobro);
	
	@Query(value = "select count(1) from caj.tbl_cxc_documento where tipo_referencia = 'LUZ' and codigo_referencia in (select codigo_luzxtienda from cli.tbl_luzxtienda where codigo_luz = :codigoLuz)", nativeQuery = true)
	Integer countCobroLuzGenerado( @Param("codigoLuz") Integer intCodigoLuz);
	
}
