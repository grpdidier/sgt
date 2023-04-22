package com.pe.lima.sg.dao.caja;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pe.lima.sg.dao.BaseOperacionDAO;
import com.pe.lima.sg.entity.caja.TblCobro;

public interface ICobroDAO extends BaseOperacionDAO<TblCobro, Integer> {
	
	@Query(value = "select * from caj.tbl_cobro where estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblCobro> listarAllActivos();
		
	@Query(value = "select * from caj.tbl_cobro where codigo_cxc_doc = :codigo AND estado = '1' ORDER BY 1 ", nativeQuery = true)
	List<TblCobro> listarAllActivosxDocumento(@Param("codigo") Integer intCodigoCxcDocumento);
	
	@Query(value = "select * from caj.tbl_cobro where codigo_desembolso = :codigoDesembolso  ORDER BY 1 ", nativeQuery = true)
	List<TblCobro> listarAllActivosxDesembolso(@Param("codigoDesembolso") Integer intCodigoDesembolso);
	
	@Query(value = "select * from caj.tbl_cobro cob where cob.tipo_cobro = :tipo and cob.codigo_cxc_doc in (select codigo_cxc_doc from caj.tbl_cxc_documento dc where dc.codigo_contrato = :codigo) ORDER BY 1 DESC ", nativeQuery = true)
	List<TblCobro> listarAllCobroxTipo(@Param("codigo") Integer intCodigoContrato, @Param("tipo") String tipoCobro);

	
}
