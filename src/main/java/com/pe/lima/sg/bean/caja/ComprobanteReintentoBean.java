package com.pe.lima.sg.bean.caja;



import com.pe.lima.sg.api.bean.CredencialBean;
import com.pe.lima.sg.entity.caja.TblComprobanteSunat;
import com.pe.lima.sg.entity.caja.TblMasivoSunat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComprobanteReintentoBean {
	private CredencialBean credencial;
	private TblComprobanteSunat tblComprobanteSunat;
	private TblMasivoSunat tblMasivoSunat;
	private Integer codigoCxCDocumento;
}
