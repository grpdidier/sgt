package com.pe.lima.sg.facturador.db.util;



import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.pe.lima.sg.facturador.bean.BandejaFacturadorBean;
import com.pe.lima.sg.facturador.bean.BandejaFacturadorNotaBean;
import com.pe.lima.sg.facturador.bean.CatalogoErrorFacturadorBean;
import com.pe.lima.sg.facturador.bean.FiltroPdf;
import com.pe.lima.sg.facturador.bean.ParametroFacturadorBean;

@NoRepositoryBean
public interface IOperacionFacturador {

	public abstract BandejaFacturadorBean consultarBandejaComprobantes(BandejaFacturadorBean bandeja, FiltroPdf entidad)
		    throws Exception;
	
	public abstract CatalogoErrorFacturadorBean consultarCatalogoError(CatalogoErrorFacturadorBean error, FiltroPdf entidad)
		    throws Exception;
	
	public abstract List<ParametroFacturadorBean> consultarParametro(String idParametro, FiltroPdf entidad)
		    throws Exception;
	
	public abstract BandejaFacturadorNotaBean consultarBandejaNota(BandejaFacturadorNotaBean bandeja, FiltroPdf entidad)
		    throws Exception;
}
