package com.pe.lima.sg.dao.caja;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblCxcDocumento;

public interface ICxCDocumentoDAO extends BaseOperacionDAO<TblCxcDocumento, Integer> {

	@Query(value = "select * from caj.tbl_cxc_documento where estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblCxcDocumento> listarAllActivos();

	@Query(value = "select * from caj.tbl_cxc_documento where tipo_referencia = :tipoReferencia AND codigo_referencia = :codigoReferencia AND anio = :anio AND mes =:mes AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	TblCxcDocumento findByAnioMesCodigoReferencia(@Param("tipoReferencia") String strTipoReferencia, @Param("codigoReferencia") Integer intCodigoReferencia, @Param("anio") Integer intAnio, @Param("mes")Integer intMes);

	//Valida si existe algun registro con cobro
	@Query(value = "select count(1) from caj.tbl_cxc_documento where codigo_cxc_bitacora = :codigoBitacora AND anio = :anio AND mes =:mes AND estado = '1'  AND monto_cobrado > 0 ", nativeQuery = true)
	Integer countTotalCobro(@Param("codigoBitacora") int intCodigoBitacora, @Param("anio") Integer intAnio, @Param("mes")Integer intMes);

	//Listado de documentos por codigo de bitacora
	@Query(value = "select * from caj.tbl_cxc_documento where codigo_cxc_bitacora =:codigoBitacora AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblCxcDocumento> listarDocumentoxBitacora(@Param("codigoBitacora") int intCodigoBitacora);

	@Query(value = "select * from caj.tbl_cxc_documento where tipo_referencia = :tipoReferencia AND codigo_referencia = :codigoReferencia AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	TblCxcDocumento findByArbitrioCodigoReferencia(@Param("tipoReferencia") String strTipoReferencia, @Param("codigoReferencia") Integer intCodigoReferencia);

	//Listado de documentos por codigo de contrato y tipo referencia
	@Query(value = "select * from caj.tbl_cxc_documento where codigo_contrato =:codigoContrato AND  tipo_referencia =:tipoReferencia AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblCxcDocumento> listarDocumentoxReferencia(@Param("codigoContrato") int intCodigoContrato, @Param("tipoReferencia") String strTipoReferencia);

	//Listado de documentos por codigo de contrato y tipo referencia
	@Query(value = "select * from caj.tbl_cxc_documento where codigo_contrato =:codigoContrato AND  tipo_referencia =:tipoReferencia AND estado = '1'  AND saldo > 0 ORDER BY 1 ", nativeQuery = true)
	List<TblCxcDocumento> listarDocumentoxReferenciaxSaldo(@Param("codigoContrato") int intCodigoContrato, @Param("tipoReferencia") String strTipoReferencia);

		
	//Listado de documentos por codigo de contrato, tipo referencia y saldos mayores a cero
	@Query(value = "select * from caj.tbl_cxc_documento where codigo_contrato =:codigoContrato AND  tipo_referencia =:tipoReferencia AND estado = '1' AND saldo > 0 ORDER BY 1 ", nativeQuery = true)
	List<TblCxcDocumento> listarCxCxContrato(@Param("codigoContrato") int intCodigoContrato, @Param("tipoReferencia") String strTipoReferencia);

	//Obtiene el ultimo mes de documento por cobrar
	@Query(value = "select max(fecha_fin) + 1 from caj.tbl_cxc_documento where codigo_contrato =:codigoContrato AND  tipo_referencia =:tipoReferencia AND estado = '1' ", nativeQuery = true)
	Date ultimoMesCxC(@Param("codigoContrato") int intCodigoContrato, @Param("tipoReferencia") String strTipoReferencia);
	
	//Listado de documentos por codigo de contrato, tipo referencia y saldos mayores a cero
	@Query(value = "select * from caj.tbl_cxc_documento where codigo_referencia =:codigoReferencia AND  tipo_referencia =:tipoReferencia AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblCxcDocumento> listarCxCxContratoxTipoServicio(@Param("codigoReferencia") int intCodigoReferencia, @Param("tipoReferencia") String strTipoReferencia);

	@Query(value = "select * from caj.tbl_cxc_documento where tipo_referencia = :tipoReferencia AND tipo_documento = 'FAC' AND anio = :anio AND mes =:mes AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblCxcDocumento> listarCxCByAnioMes(@Param("tipoReferencia") String strTipoReferencia, @Param("anio") Integer intAnio, @Param("mes")Integer intMes);

}
