package com.pe.lima.sg.facturador.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.pe.lima.sg.facturador.bean.AuditoriaBean;
import com.pe.lima.sg.facturador.bean.ComprobanteBean;
import com.pe.lima.sg.facturador.bean.ErrorBean;
import com.pe.lima.sg.facturador.bean.ValidacionBean;
import com.pe.lima.sg.facturador.dao.IComprobanteDAO;
import com.pe.lima.sg.facturador.dao.IDetalleComprobanteDAO;
import com.pe.lima.sg.facturador.dao.ILeyendaDAO;
import com.pe.lima.sg.facturador.dao.ISerieSFS12DAO;
import com.pe.lima.sg.facturador.dao.ISunatSFS12CabeceraDAO;
import com.pe.lima.sg.facturador.dao.ISunatSFS12DetalleDAO;
import com.pe.lima.sg.facturador.dao.ISunatTributoGeneralDAO;
import com.pe.lima.sg.facturador.dao.ITributoGeneralDAO;
import com.pe.lima.sg.facturador.entity.TblComprobante;
import com.pe.lima.sg.facturador.entity.TblDetalleComprobante;
import com.pe.lima.sg.facturador.entity.TblEmpresa;
import com.pe.lima.sg.facturador.entity.TblLeyenda;
import com.pe.lima.sg.facturador.entity.TblSerie;
import com.pe.lima.sg.facturador.entity.TblSunatCabecera;
import com.pe.lima.sg.facturador.entity.TblSunatDetalle;
import com.pe.lima.sg.facturador.entity.TblSunatTributoGeneral;
import com.pe.lima.sg.facturador.entity.TblTributoGeneral;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.NumberToLetterConverter;
import com.pe.lima.sg.presentacion.util.UtilSGT;

@Service
public class ComprobanteSFS12 extends OperacionUtil implements IComprobanteSFS12 {
	@Autowired
	private IComprobanteDAO comprobanteDao;
	
	@Autowired
	private ISerieSFS12DAO serieDao;

	@Autowired
	private IDetalleComprobanteDAO detalleComprobanteDao;

	@Autowired
	private ITributoGeneralDAO tributoGeneralDAO;

	@Autowired
	private ISunatTributoGeneralDAO sunatTributoGeneralDAO;

	@Autowired
	private ILeyendaDAO leyendaDao;

	@Autowired
	private ISunatSFS12CabeceraDAO sunatCabeceraDao;

	@Autowired
	private ISunatSFS12DetalleDAO sunatDetalleDao;
	
	private boolean setError(ValidacionBean validacionBean, List<ErrorBean> listaError, String nombre, String descripcion){
		ErrorBean erroBean = null;
		erroBean = new ErrorBean();
		erroBean.setNombreCampo(nombre);
		erroBean.setDescripcion(descripcion);
		validacionBean.setResultado(false);
		listaError.add(erroBean);
		return false;
	}
	/** Valida los datos obligatorios del comprobante*/
	public boolean validarComprobante(List<ComprobanteBean> listaComprobante, List<ValidacionBean> listaValidacion, TblEmpresa empresa){
		boolean resultado = true;
		ValidacionBean validacionBean = null;
		List<ErrorBean> listaError = null;
		TblComprobante entidad = null;
		for(ComprobanteBean comprobante: listaComprobante){
			validacionBean = new ValidacionBean();
			validacionBean.setResultado(true);
			listaError = new ArrayList<>();
			validacionBean.setListaError(listaError);
			listaValidacion.add(validacionBean);
			entidad = comprobante.getComprobante();
			//Tipo de Operacion 
			if (entidad.getTipoOperacion() == null || entidad.getTipoOperacion().equals("")){
				resultado = this.setError(validacionBean, listaError, "Tipo de Operación", "Debe ingresar el tipo de operación");
			}
			//Codigo Domicilio Fiscal
			if (entidad.getCodigoDomicilio() == null || entidad.getCodigoDomicilio().equals("")){
				resultado = this.setError(validacionBean, listaError, "Código de Domicilio", "Debe ingresar el domicilio fiscal o anexo");
			}
			//Fecha Emision
			if (entidad.getFechaEmision() == null || entidad.getFechaEmision().equals("")){
				resultado = this.setError(validacionBean, listaError, "Fecha de Emisión", "Debe ingresar la fecha de emisión");
			}
			//Tipo de Comprobante
			if (entidad.getTipoComprobante() == null || entidad.getTipoComprobante().equals("")){
				resultado = this.setError(validacionBean, listaError, "Tipo de Comprobante", "Debe ingresar el tipo de comprobante");
			}
			//Serie
			if (entidad.getSerie() == null || entidad.getSerie().equals("")){
				resultado = this.setError(validacionBean, listaError, "Numero de Serie", "Debe ingresar la serie del comprobante");
			}
			//Numero
			if (entidad.getNumero() == null || entidad.getNumero().equals("")){
				resultado = this.setError(validacionBean, listaError, "Número de Comprobate", "Debe ingresar el número del comprobante");
			}else{
				entidad.setNumero(UtilSGT.completarCeros(entidad.getNumero(), Constantes.SUNAT_LONGITUD_NUMERO));
			}
			//Validando existencia del comprobante
			Integer total = comprobanteDao.totalComprobante(entidad.getTipoComprobante(), entidad.getSerie(), entidad.getNumero(), empresa.getCodigoEntidad());
			if (total > 0){
				resultado = this.setError(validacionBean, listaError, "Total de Comprobantes", "Se encontró un comprobante anteriormente registrado Tipo ["+entidad.getTipoComprobante()+"] - Serie ["+entidad.getSerie()+"] - Numero ["+entidad.getNumero()+"]");
			}
			//Moneda
			if (entidad.getMoneda() == null || entidad.getMoneda().equals("")){
				resultado = this.setError(validacionBean, listaError, "Tipo de Moneda", "Debe ingresar el tipo de moneda");
			}
			//Tipo Documento
			if (entidad.getTipoDocumento() == null || entidad.getTipoDocumento().equals("")){
				resultado = this.setError(validacionBean, listaError, "Tipo de Documento", "Debe ingresar el tipo de documento del cliente");
			}
			//Numero Documento
			if (entidad.getNumeroDocumento()== null || entidad.getNumeroDocumento().equals("")){
				resultado = this.setError(validacionBean, listaError, "Número de Documento", "Debe ingresar el numero del documento del cliente");
			}
			//Nombre o Razon Social
			if (entidad.getNombreCliente()== null || entidad.getNombreCliente().equals("")){
				resultado = this.setError(validacionBean, listaError, "Nombre del Cliente", "Debe ingresar el nombre o razón social del cliente");
			}
			if (comprobante.getListaDetalle() == null || comprobante.getListaDetalle().size() <= 0){
				resultado = this.setError(validacionBean, listaError, "Detalle del Comprobante", "Debe ingresar el detalle del comprobante");
			}
			if (comprobante.getLeyenda() != null && comprobante.getLeyenda().getCodigoSunat() != null && !comprobante.getLeyenda().getCodigoSunat().equals("")){
				if (comprobante.getLeyenda().getDescripcionSunat() == null || comprobante.getLeyenda().getDescripcionSunat().equals("")){
					resultado = this.setError(validacionBean, listaError, "Leyenda", "Debe ingresar la descripción de la leyenda");
				}
			}
			if (entidad.getValorOpGratuitas() != null && entidad.getValorOpGratuitas().doubleValue() > 0 ){
				for(TblDetalleComprobante detalle: comprobante.getListaDetalle()){
					if (!detalle.getTipoAfectacion().equals(Constantes.SUNAT_EXONERADO_OPERACION_GRATUITA) &&
							!detalle.getTipoAfectacion().equals(Constantes.SUNAT_INAFECTO_OPERACION_GRATUITA)){
						resultado = this.setError(validacionBean, listaError, "Tipo de Afectación", "Si existe Valor referencial unitario en la operacion no onerosas con monto mayor a cero, la operacion debe ser gratuita");
					}
				}
			}
			comprobante.setResultadoValidacion(validacionBean.isResultado());
			
		}
		return resultado;
	}
	
	/** Realiza el calculo del comprobante y setea los datos adicionales y campos de auditoria*/
	public boolean calcularAsignarDatosComprobante(List<ComprobanteBean> listaComprobante , AuditoriaBean auditoriaBean){
		boolean resultado = false;
		
		this.calculoCabeceraComprobante(listaComprobante);
		this.setDatosComprobanteSFS12(listaComprobante);
		this.setDatosDetalleComprobanteSFS12(listaComprobante);
		this.setDatosTributosComprobanteSFS12(listaComprobante);
		
		resultado = true;
		
		return resultado;
	}
	
	public void asignarNumeroComprobante(ComprobanteBean entidad,TblSerie serieFactura, TblSerie serieBoleta ){
		if (entidad.getComprobante().getTipoComprobante().equals(Constantes.TIPO_COMPROBANTE_FACTURA)){
			UtilSGT.obtenerSiguienteComprobante(serieFactura);
			entidad.getComprobante().setSerie(serieFactura.getPrefijoSerie());
			entidad.getComprobante().setNumero(UtilSGT.completarCeros(serieFactura.getSecuencialSerie().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
		}else{
			UtilSGT.obtenerSiguienteComprobante(serieBoleta);
			entidad.getComprobante().setSerie(serieBoleta.getPrefijoSerie());
			entidad.getComprobante().setNumero(UtilSGT.completarCeros(serieBoleta.getSecuencialSerie().toString(),Constantes.SUNAT_LONGITUD_NUMERO));
		}
		
	}
	/** Registro de los datos del comprobante y generacion el archivo*/
	public boolean registrarComprobante(List<ComprobanteBean> listaComprobante, List<ErrorBean> listaError, TblEmpresa empresa, AuditoriaBean auditoriaBean){
		boolean resultado 					= false;
		ErrorBean errorBean 				= null;
		TblSunatCabecera cabecera			= null;
		List<TblSunatDetalle> listaDetalle	= new ArrayList<TblSunatDetalle>();
		List<TblSunatTributoGeneral> listaTriSunat = new ArrayList<>();
		TblSerie serieFactura					= null;
		TblSerie serieBoleta					= null;
		
		serieFactura = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_FACTURA);
		serieBoleta = serieDao.buscarOneByTipoComprobante(Constantes.TIPO_COMPROBANTE_BOLETA);
		for(ComprobanteBean entidad:listaComprobante ){
			if (entidad.isResultadoValidacion()){
				errorBean = new ErrorBean();
				listaError.add(errorBean);
				//Se asignan los numeros de comprobantes
				this.asignarNumeroComprobante(entidad, serieFactura, serieBoleta);
				//Registro del comprobante
				errorBean = this.grabarComprobante(entidad);
				
				if (errorBean.isEstado()){
					TblComprobante comprobanteTemp = comprobanteDao.obtenerComprobante(entidad.getComprobante().getCodigoVerificacion());
					//Registro del detalle del comprobante
					errorBean = this.grabarDetalleComprobante(entidad,  auditoriaBean, comprobanteTemp, empresa);
					
					if(errorBean.isEstado()){
						//Registro del tributo del comprobante
						errorBean = this.grabarTributo(entidad, comprobanteTemp);
						
						if (errorBean.isEstado()){
							String nombreLeyendaFile = Constantes.SUNAT_RUC_EMISOR+"-"+comprobanteTemp.getTipoComprobante()+"-"+comprobanteTemp.getSerie()+"-"+comprobanteTemp.getNumero()+"."+Constantes.SUNAT_ARCHIVO_EXTENSION_LEYENDA;
							//Registro de la leyenda del comprobante
							errorBean = this.grabarLeyenda(entidad, empresa, auditoriaBean, comprobanteTemp);
							
							if(errorBean.isEstado()){
								cabecera = this.registrarCabeceraSunat(comprobanteTemp, auditoriaBean);
								if (cabecera != null){
									boolean registroOK = this.registrarDetalleCabeceraSunat(cabecera, entidad, comprobanteTemp, auditoriaBean, listaDetalle);
									//registro de los tributos para la sunat
									if (registroOK){
										registroOK = this.registrarTributoSunat(cabecera, auditoriaBean, entidad, listaTriSunat);
										if (registroOK){
											//Generar Archivo
											errorBean = generarArchivoSunat(cabecera, entidad, nombreLeyendaFile, comprobanteTemp, listaDetalle, listaTriSunat);
										}
									}
								}
							}
						}
						
					}
				}
			}
			
			
		}
		//Actualizamos las series
		serieDao.save(serieBoleta);
		serieDao.save(serieFactura);
		
		resultado = true;
		return resultado;
	}
	
	public ErrorBean generarArchivoSunat(TblSunatCabecera cabecera, ComprobanteBean entidad, String nombreLeyendaFile, TblComprobante comprobanteTemp, List<TblSunatDetalle> listaDetalle, List<TblSunatTributoGeneral> listaTributo){
		ErrorBean errorBean = new ErrorBean();
		if (this.generarArchivoCabecera(cabecera, entidad)){
			if (this.generarArchivoDetalle(listaDetalle, entidad)){
				if (this.generarArchivoTributo(listaTributo, entidad)){
					if (this.generarArchivoLeyenda(entidad.getLeyenda(),comprobanteTemp, nombreLeyendaFile, entidad)){
						//Generar adicional del detalle
						/**SE DEBE ANALIZAR LA NECESIDAD DEL ARCHIVO ADICIONAL - SUNAT EN LA VERSION 1.2 MODIFICO EL ARCHIVO POR ELLO SE COMENTA*/
					}else{
						errorBean.setDescripcion("Se generó un error en la creacion de la leyenda del archivo SUNAT");
						errorBean.setEstado(false);
					}
				}else{
					errorBean.setDescripcion("Se generó un error en la creacion del tributo en el archivo SUNAT");
					errorBean.setEstado(false);
				}
			}else{
				errorBean.setDescripcion("Se generó un error en la creacion de la leyenda del archivo SUNAT");
				errorBean.setEstado(false);
			}
		}else{
			errorBean.setDescripcion("Se generó un error en el regitro de la cabecera del archivo SUNAT");
			errorBean.setEstado(false);
		}

		errorBean.setEstado(true);
		
		return errorBean;
	}
	
	
	public boolean registrarTributoSunat(TblSunatCabecera cabecera,AuditoriaBean auditoriaBean, ComprobanteBean entidad, List<TblSunatTributoGeneral> listaTriSunat){
		TblSunatTributoGeneral tributoSunat = null;
		boolean resultado = false;
		for(TblTributoGeneral tblTributoGeneral: entidad.getListaTributo()){
			tributoSunat = this.registroTributoSunat(cabecera, tblTributoGeneral, auditoriaBean, entidad);
			if (tributoSunat !=null){
				listaTriSunat.add(tributoSunat);
			}else{
				resultado = false;
				break;
			}
		}
		resultado = true;
		
		return resultado;
	}
	
	private TblSunatTributoGeneral registroTributoSunat(TblSunatCabecera cabecera, TblTributoGeneral tblTributoGeneral, AuditoriaBean auditoriaBean, ComprobanteBean entidad){
		TblSunatTributoGeneral tblSunatTributoGeneral = new TblSunatTributoGeneral();

		tblSunatTributoGeneral.setIdentificadorTributo(tblTributoGeneral.getIdentificadorTributo());
		tblSunatTributoGeneral.setNombreTributo(tblTributoGeneral.getNombreTributo());
		tblSunatTributoGeneral.setCodigoTipoTributo(tblTributoGeneral.getCodigoTipoTributo());
		tblSunatTributoGeneral.setBaseImponible(UtilSGT.getRoundDecimalString(tblTributoGeneral.getBaseImponible(), 2));
		tblSunatTributoGeneral.setMontoTributoItem(UtilSGT.getRoundDecimalString(tblTributoGeneral.getMontoTributoItem(), 2));

		tblSunatTributoGeneral.setAuditoriaCreacion(auditoriaBean);
		tblSunatTributoGeneral.setTblSunatCabecera(cabecera);
		tblSunatTributoGeneral.setCodigoCabecera(cabecera.getCodigoCabecera());
		tblSunatTributoGeneral.setNombreArchivo(Constantes.SUNAT_RUC_EMISOR+"-"+entidad.getComprobante().getTipoComprobante()+"-"+entidad.getComprobante().getSerie()+"-"+entidad.getComprobante().getNumero()+"."+Constantes.SUNAT_ARCHIVO_EXTENSION_TRIBUTO);
		sunatTributoGeneralDAO.save(tblSunatTributoGeneral);

		return tblSunatTributoGeneral;
	}
	
	/*Registra los datos del detalle de la cabecera de sunat*/
	public boolean registrarDetalleCabeceraSunat(TblSunatCabecera cabecera, ComprobanteBean entidad, TblComprobante comprobanteTemp, AuditoriaBean auditoriaBean, List<TblSunatDetalle> listaDetalle ){
		boolean resultado 				= false;
		TblSunatDetalle detalleSunat	= null;
		for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
			detalleSunat = this.registrarDetalleSunat(cabecera, comprobanteTemp, detalle, auditoriaBean, entidad);
			if (detalleSunat !=null){
				listaDetalle.add(detalleSunat);
			}else{
				resultado = false;
				break;
			}
		}
		resultado = true;
		return resultado;
	}
	
	
	
	
	public ErrorBean grabarComprobante(ComprobanteBean entidad){
		ErrorBean errorBean = new ErrorBean();
		try{
			comprobanteDao.save(entidad.getComprobante());
			errorBean.setEstado(true);
		}catch(Exception e){
			errorBean.setDescripcion(e.getMessage());
			errorBean.setEstado(false);
			e.printStackTrace();
		}
		return errorBean;
	}
	
	public ErrorBean grabarLeyenda(ComprobanteBean entidad, TblEmpresa empresa, AuditoriaBean auditoriaBean, TblComprobante comprobanteTemp){
		ErrorBean errorBean = new ErrorBean();
		errorBean.setEstado(true);
		if (entidad.getLeyenda()!=null && !entidad.getLeyenda().getCodigoSunat().equals("")){
			entidad.getLeyenda().setTblComprobante(comprobanteTemp);
			entidad.getLeyenda().setNombreArchivo(empresa.getRuc()+"-"+comprobanteTemp.getTipoComprobante()+"-"+comprobanteTemp.getSerie()+"-"+comprobanteTemp.getNumero()+"."+Constantes.SUNAT_ARCHIVO_EXTENSION_LEYENDA);
			try{
				leyendaDao.save(entidad.getLeyenda());
				
			}catch(Exception e){
				errorBean.setDescripcion(e.getMessage() + "Leyenda - Comprobante: "+comprobanteTemp.getCodigoComprobante());
				errorBean.setEstado(false);
			}
		}
		return errorBean;
	}
	
	public ErrorBean grabarDetalleComprobante(ComprobanteBean entidad,  AuditoriaBean auditoriaBean, TblComprobante comprobanteTemp, TblEmpresa empresa){
		ErrorBean errorBean = new ErrorBean();
		//this.actualizarSerie(entidad.getComprobante().getTipoComprobante(), entidad.getComprobante().getSerie(), entidad.getComprobante().getNumero(),  empresa.getCodigoEntidad(), auditoriaBean);
		
		for(TblDetalleComprobante detalle: entidad.getListaDetalle()){
			detalle.setTblComprobante(comprobanteTemp);
			try{
				detalleComprobanteDao.save(detalle);
				errorBean.setEstado(true);
			}catch(Exception e){
				errorBean.setDescripcion(e.getMessage() + " Detalle - Comprobante: "+comprobanteTemp.getCodigoComprobante());
				errorBean.setEstado(false);
				break;
			}
		}
		return errorBean;
	}
	
	public ErrorBean grabarTributo(ComprobanteBean entidad, TblComprobante comprobanteTemp){
		ErrorBean errorBean = new ErrorBean();
		for(TblTributoGeneral tributo: entidad.getListaTributo()){
			tributo.setTblComprobante(comprobanteTemp);
			tributo.setCodigoComprobante(comprobanteTemp.getCodigoComprobante());
			try{
				tributoGeneralDAO.save(tributo);
				errorBean.setEstado(true);
			}catch(Exception e){
				errorBean.setDescripcion(e.getMessage() + "Tributo - Comprobante: "+comprobanteTemp.getCodigoComprobante());
				errorBean.setEstado(false);
				break;
			}
		}
		return errorBean;
	}
	/*Actualizamos la serie luego de registrar el comprobante*/
	/*public void actualizarSerie(String tipoComprobante, String numeroSerie, String numeroComprobante, Integer codigoEntidad, AuditoriaBean auditoriaBean){
		List<TblSerie> listaSerie = null;
		TblSerie serie = null;
		Integer nuevoNumero = null;
		listaSerie = serieDao.buscarOneByNombre(tipoComprobante, numeroSerie, codigoEntidad);
		if(listaSerie!=null && !listaSerie.isEmpty()){
			nuevoNumero = Integer.parseInt(numeroComprobante)+1;
			serie = listaSerie.get(0);
			serie.setAuditoriaModificacion(auditoriaBean);
			serie.setNumeroComprobante(UtilSGT.completarCeros(nuevoNumero.toString(), Constantes.SUNAT_LONGITUD_NUMERO));
			serie.setSecuencialSerie(nuevoNumero);
			serieDao.save(serie);
		}
	}*/
	
	public TblSunatCabecera registrarCabeceraSunat(TblComprobante comprobante, AuditoriaBean auditoriaBean){
		TblSunatCabecera cabecera = null;
		try{
			cabecera = new TblSunatCabecera();
			//Tipo Operacion
			cabecera.setTipoOperacion(comprobante.getTipoOperacion());
			//Fecha Emision
			//cabecera.setFechaEmision(UtilSGT.getFecha("yyyy-MM-dd"));
			cabecera.setFechaEmision(comprobante.getFechaEmision());
			cabecera.setHoraEmision(comprobante.getHoraEmision());
			cabecera.setFechaVencimiento(comprobante.getFechaVencimiento());
			//Domicilio Fiscal
			cabecera.setDomicilioFiscal(new Integer(comprobante.getCodigoDomicilio()));
			//Datos del Cliente
			cabecera.setTipoDocumentoUsuario(comprobante.getTipoDocumento());
			cabecera.setNumeroDocumento(comprobante.getNumeroDocumento());
			cabecera.setRazonSocial(comprobante.getNombreCliente());
			//Moneda
			cabecera.setTipoMoneda(comprobante.getMoneda());

			//sumatoria tributos
			cabecera.setSumTributo(UtilSGT.getRoundDecimalString(comprobante.getSumTributo(), 2));
			//total valor venta
			cabecera.setTotValorVenta(UtilSGT.getRoundDecimalString(comprobante.getTotValorVenta(), 2));
			//total precio venta
			cabecera.setTotPrecioVenta(UtilSGT.getRoundDecimalString(comprobante.getTotPrecioVenta(), 2));
			//total descuento
			cabecera.setTotDescuento(UtilSGT.getRoundDecimalString(comprobante.getTotalDescuento(), 2));
			//sumatoria otros cargos
			cabecera.setSumOtrosCargos(UtilSGT.getRoundDecimalString(comprobante.getSumOtrosCargos(), 2));
			//anticipos
			cabecera.setTotAnticipos(UtilSGT.getRoundDecimalString(comprobante.getTotAnticipos(), 2));
			//importe total de la venta
			cabecera.setImpTotalVenta(UtilSGT.getRoundDecimalString(comprobante.getImpTotalVenta(), 2));

			cabecera.setVersionUbl(comprobante.getVersionUbl());
			cabecera.setCustomizacionDoc(comprobante.getCustomizacionDoc());


			//Lo que sigue esta obsoleto
			//Descuentos Globales
			cabecera.setSumaDescuento(UtilSGT.getRoundDecimal(comprobante.getDescuentosGlobales(),2));
			cabecera.setSumaCargo(UtilSGT.getRoundDecimal(comprobante.getTotalOtrosCargos(),2));
			cabecera.setTotalDescuento(UtilSGT.getRoundDecimal(comprobante.getTotalDescuento(),2));
			cabecera.setOperacionGravada(UtilSGT.getRoundDecimal(comprobante.getTotalOpGravada(),2));
			//Total valor de venta - Operaciones inafectas
			cabecera.setOperacionInafecta(UtilSGT.getRoundDecimal(comprobante.getTotalOpInafecta(),2));
			//Total valor de venta - Operaciones exoneradas
			cabecera.setOperacionExonerada(UtilSGT.getRoundDecimal(comprobante.getTotalOpExonerada(),2));
			//Sumatoria IGV
			cabecera.setMontoIgv(UtilSGT.getRoundDecimal(comprobante.getTotalIgv(),2));
			//Sumatoria ISC
			cabecera.setMontoIsc(UtilSGT.getRoundDecimal(comprobante.getSumatoriaIsc(),2));
			//Sumatoria otros tributos
			cabecera.setOtrosTributos(UtilSGT.getRoundDecimal(comprobante.getSumatorioaOtrosTributos(),2));
			//Importe total de la venta, cesión en uso o del servicio prestado
			cabecera.setImporteTotal(UtilSGT.getRoundDecimal(comprobante.getTotalImporte(),2));
			//Nombre archivo
			cabecera.setNombreArchivo(Constantes.SUNAT_RUC_EMISOR+"-"+comprobante.getTipoComprobante()+"-"+comprobante.getSerie()+"-"+comprobante.getNumero()+"."+Constantes.SUNAT_ARCHIVO_EXTENSION_CABECERA);
			cabecera.setAuditoriaCreacion(auditoriaBean);
			//CxC Documento
			cabecera.setTblComprobante(comprobante);
			//Registro de la cabecera de la sunat
			sunatCabeceraDao.save(cabecera);
			cabecera = sunatCabeceraDao.findByCodigoDocumento(comprobante.getCodigoComprobante());
		}catch(Exception e){
			e.printStackTrace();
			cabecera = null;
		}
		return cabecera;
	}

	/*
	 * Registro de los datos del detalle para la sunat
	 */
	public TblSunatDetalle registrarDetalleSunat(TblSunatCabecera cabecera, TblComprobante comprobante, TblDetalleComprobante detalleComprobante, AuditoriaBean auditoriaBean, ComprobanteBean entidad){
		TblSunatDetalle detalle 				= null;
		try{
			if (comprobante.getTipoOperacion().equals(Constantes.SUNAT_TIPO_OPERACION_EXPORTACION)){
				detalle = this.calculoDetalleSunatExportacion(cabecera, comprobante, detalleComprobante, auditoriaBean, entidad);
			}else{
				detalle = this.calculoDetalleSunatNacional(cabecera, comprobante, detalleComprobante, auditoriaBean, entidad);	
			}

			//sunatDetalleDao.save(detalle);
		}catch(Exception e){
			e.printStackTrace();
			detalle = null;
		}
		return detalle;
	}
	/*
	 * Calculo de venta Exportacion - Detalle Sunat
	 */
	private TblSunatDetalle calculoDetalleSunatExportacion(TblSunatCabecera cabecera, TblComprobante comprobante, TblDetalleComprobante detalleComprobante, AuditoriaBean auditoriaBean, ComprobanteBean entidad){
		TblSunatDetalle detalle 				= null;
		BigDecimal temporal						= null;
		try{
			//registro del detalle del comprobante
			detalle = new TblSunatDetalle();
			//Código de unidad de medida por ítem
			detalle.setCodigoUnidad(detalleComprobante.getUnidadMedida());
			//Cantidad de unidades por ítem
			detalle.setCantidad(detalleComprobante.getCantidad().toString());
			//Código de producto
			detalle.setCodigoProducto("");
			//Codigo producto SUNAT
			detalle.setCodigoProductoSunat("");
			//Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características.
			detalle.setDescripcion(detalleComprobante.getDescripcion());
			//Valor unitario por ítem
			//detalle.setValorUnitario(cabecera.getOperacionGravada().toString());
			//detalle.setValorUnitario(this.obtenerTotalMontoGravada(detalleComprobante.getPrecioTotal(), Constantes.SUNAT_IGV).toString());
			temporal = OperacionUtil.obtenerTotalMontoGravada(detalleComprobante.getPrecioFinal(), 0 , entidad.getValorServicio()); //IGV = 0
			temporal = temporal.divide(new BigDecimal(detalleComprobante.getCantidad().toString()), 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);

			detalle.setValorUnitario(temporal.toString());
			//Descuentos por item
			BigDecimal descuento = new BigDecimal((detalleComprobante.getPrecioTotal().doubleValue()*detalleComprobante.getDescuento().doubleValue()/100)).setScale(2, RoundingMode.HALF_UP);
			detalle.setDescuento(descuento.toString());
			//Monto de IGV por ítem
			//detalle.setMontoIgv(this.obtenerTotalImpuestoGravada(detalleComprobante.getPrecioFinal(), Constantes.SUNAT_IGV).toString());
			detalle.setMontoIgv(OperacionUtil.obtenerTotalImpuestoGravada(detalleComprobante.getPrecioFinal(), 0 , entidad.getValorServicio()).toString()); //IGV = 0
			//Afectación al IGV por ítem
			detalle.setAfectacionIgv(detalleComprobante.getTipoAfectacion());
			//Monto de ISC por ítem
			detalle.setMontoIsc("0");
			//Tipo de sistema ISC
			detalle.setTipoIsc("");
			//Precio de venta unitario por item
			detalle.setPrecioVentaUnitario(detalleComprobante.getPrecioTotal().toString());
			//Valor de venta por ítem
			detalle.setValorVentaItem(detalleComprobante.getPrecioFinal().toString());
			//Archivo
			detalle.setNombreArchivo(Constantes.SUNAT_RUC_EMISOR+"-"+comprobante.getTipoComprobante()+"-"+comprobante.getSerie()+"-"+comprobante.getNumero()+"."+Constantes.SUNAT_ARCHIVO_EXTENSION_DETALLE);
			//Falta auditoria
			detalle.setAuditoriaCreacion(auditoriaBean);
			detalle.setTblSunatCabecera(cabecera);
			sunatDetalleDao.save(detalle);
		}catch(Exception e){
			e.printStackTrace();
			detalle = null;
		}
		return detalle;
	}
	private TblSunatDetalle calculoDetalleSunatNacional(TblSunatCabecera cabecera, TblComprobante comprobante, TblDetalleComprobante detalleComprobante, AuditoriaBean auditoriaBean, ComprobanteBean entidad){
		TblSunatDetalle detalle 				= null;
		//BigDecimal temporal						= null;
		try{
			//registro del detalle del comprobante
			detalle = new TblSunatDetalle();
			//Código de unidad de medida por ítem
			detalle.setCodigoUnidad(detalleComprobante.getUnidadMedida());
			//Cantidad de unidades por ítem
			detalle.setCantidad(UtilSGT.getRoundDecimalString(detalleComprobante.getCantidad(), 2));
			//Código de producto
			detalle.setCodigoProducto(detalleComprobante.getCodigoProducto());
			//Codigo producto SUNAT
			detalle.setCodigoProductoSunat(Constantes.SUNAT_SIN_CODIGO);
			//Descripción detallada del servicio prestado, bien vendido o cedido en uso, indicando las características.
			detalle.setDescripcion(detalleComprobante.getDescripcion());

			//valor unitario
			detalle.setValorUnitario(UtilSGT.getRoundDecimalString(detalleComprobante.getValorUnitario(), 4));
			//Sumatoria tributos por item
			detalle.setSumTributosItem(UtilSGT.getRoundDecimalString(detalleComprobante.getSumTributosItem(), 2));

			//Tributo: Códigos de tipos de tributos IGV
			detalle.setTribCodTipoTributoIgv(detalleComprobante.getTribCodTipoTributoIgv());
			//Tributo: Monto de IGV por ítem
			detalle.setTribMontoIgv(UtilSGT.getRoundDecimalString(detalleComprobante.getTribMontoIgv(), 2));
			//Tributo: Base Imponible IGV por Item
			detalle.setTribBaseImponibleIgv(UtilSGT.getRoundDecimalString(detalleComprobante.getTribBaseImponibleIgv(), 2));
			//Tributo: Nombre de tributo por item
			detalle.setTribNombreTributo(detalleComprobante.getTribNombreTributo());
			//Tributo: Código de tipo de tributo por Item
			detalle.setTribCodTipoTributo(detalleComprobante.getTribCodTipoTributo());
			//Tributo: Afectación al IGV por ítem
			detalle.setTribAfectacionIgv(detalleComprobante.getTribAfectacionIgv());
			//Tributo: Porcentaje de IGV
			detalle.setTribPorcentajeIgv(detalleComprobante.getTribPorcentajeIgv());
			//Tributo ISC: Códigos de tipos de tributos ISC
			detalle.setIscCodTipoTributoIsc(detalleComprobante.getIscCodTipoTributoIsc());
			//Tributo ISC: Monto de ISC por ítem
			detalle.setIscMontoIsc(UtilSGT.getRoundDecimalString(detalleComprobante.getIscMontoIsc(), 2));
			//Tributo ISC: Base Imponible ISC por Item
			detalle.setIscBaseImponibleIsc(UtilSGT.getRoundDecimalString(detalleComprobante.getIscBaseImponibleIsc(), 2));
			//Tributo ISC: Nombre de tributo por item
			detalle.setIscNombreTributo(detalleComprobante.getIscNombreTributo());
			//Tributo ISC: Código de tipo de tributo por Item
			detalle.setIscCodTipoTributo(detalleComprobante.getIscCodTipoTributo());
			//Tributo ISC: Tipo de sistema ISC
			detalle.setIscTipoSistema(detalleComprobante.getIscTipoSistema());
			//Tributo ISC: Porcentaje de ISC
			detalle.setIscPorcentaje(detalleComprobante.getIscPorcentaje().toString());
			//Tributo Otro: Códigos de tipos de tributos OTRO
			detalle.setOtroCodTipoTributoOtro(detalleComprobante.getOtroBaseImponibleOtro().toString());
			//Tributo Otro: Monto de tributo OTRO por iItem
			detalle.setOtroMontoTributo(UtilSGT.getRoundDecimalString(detalleComprobante.getOtroMontoTributo(), 2));
			//Tributo Otro: Base Imponible de tributo OTRO por Item
			detalle.setOtroBaseImponibleOtro(UtilSGT.getRoundDecimalString(detalleComprobante.getOtroBaseImponibleOtro(), 2));
			//Tributo Otro:  Nombre de tributo OTRO por item
			detalle.setOtroNombreTributo(detalleComprobante.getOtroNombreTributo());
			//Tributo Otro: Código de tipo de tributo OTRO por Item
			detalle.setOtroCodTipoTributo(detalleComprobante.getOtroCodTipoTributo());
			//Tributo Otro: Porcentaje de tributo OTRO por Item
			detalle.setOtroPorcentaje(detalleComprobante.getOtroPorcentaje());
			//Precio de venta unitario 
			detalle.setPrecioVentaUnitario(UtilSGT.getRoundDecimalString(detalleComprobante.getPrecioVentaUnitario(), 4));
			//Valor de venta por Item 
			detalle.setValorVentaItem(UtilSGT.getRoundDecimalString(detalleComprobante.getValorVentaItem(), 2));
			//Valor REFERENCIAL unitario (gratuitos) 
			detalle.setValorReferencialUnitario(UtilSGT.getRoundDecimalString(detalleComprobante.getValorReferencialUnitario(), 2));

			//Resto de datos obsoletos		


			//Valor unitario por ítem
			//detalle.setValorUnitario(cabecera.getOperacionGravada().toString());
			//detalle.setValorUnitario(this.obtenerTotalMontoGravada(detalleComprobante.getPrecioTotal(), Constantes.SUNAT_IGV).toString());
			//temporal = this.obtenerTotalMontoGravada(detalleComprobante.getPrecioFinal(), entidad.getValorIGV(), entidad.getValorServicio());
			//temporal = temporal.divide(new BigDecimal(detalleComprobante.getCantidad().toString()) , 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);

			//detalle.setValorUnitario(temporal.toString());
			//Descuentos por item
			BigDecimal descuento = new BigDecimal((detalleComprobante.getPrecioTotal().doubleValue()*detalleComprobante.getDescuento().doubleValue()/100)).setScale(2, RoundingMode.HALF_UP);
			detalle.setDescuento(descuento.toString());
			//Monto de IGV por ítem
			//detalle.setMontoIgv(this.obtenerTotalImpuestoGravada(detalleComprobante.getPrecioFinal(), Constantes.SUNAT_IGV).toString());
			detalle.setMontoIgv(UtilSGT.getRoundDecimalString(detalleComprobante.getTribMontoIgv(), 2));
			//Afectación al IGV por ítem
			detalle.setAfectacionIgv(detalleComprobante.getTipoAfectacion());
			//Monto de ISC por ítem
			detalle.setMontoIsc("0");
			//Tipo de sistema ISC
			detalle.setTipoIsc("");
			//Precio de venta unitario por item
			//detalle.setPrecioVentaUnitario(detalleComprobante.getPrecioTotal().toString());
			//Valor de venta por ítem
			//detalle.setValorVentaItem(detalleComprobante.getPrecioFinal().toString());
			//Archivo
			detalle.setNombreArchivo(Constantes.SUNAT_RUC_EMISOR+"-"+comprobante.getTipoComprobante()+"-"+comprobante.getSerie()+"-"+comprobante.getNumero()+"."+Constantes.SUNAT_ARCHIVO_EXTENSION_DETALLE);
			//Falta auditoria
			detalle.setAuditoriaCreacion(auditoriaBean);
			detalle.setTblSunatCabecera(cabecera);
			sunatDetalleDao.save(detalle);
		}catch(Exception e){
			e.printStackTrace();
			detalle = null;
		}
		return detalle;
	}
	
	/*
	 * Genera un archivo plano Cabecera
	 */
	public boolean generarArchivoCabecera(TblSunatCabecera cabecera, ComprobanteBean entidad){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		//String FILENAME = "G:\\data0\\gamarra\\DATA\\"+cabecera.getNombreArchivo();
		//String FILENAME = Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA + cabecera.getNombreArchivo();
		String FILENAME = entidad.getSunatData() + cabecera.getNombreArchivo();
		try{
			cadena = cabecera.getTipoOperacion() + Constantes.SUNAT_PIPE +
					cabecera.getFechaEmision() + Constantes.SUNAT_PIPE +
					cabecera.getHoraEmision() + Constantes.SUNAT_PIPE +
					cabecera.getFechaVencimiento() + Constantes.SUNAT_PIPE +
					//cabecera.getDomicilioFiscal() + Constantes.SUNAT_PIPE +
					"0000" + Constantes.SUNAT_PIPE +
					cabecera.getTipoDocumentoUsuario() + Constantes.SUNAT_PIPE +
					cabecera.getNumeroDocumento() + Constantes.SUNAT_PIPE +
					cabecera.getRazonSocial()	+ Constantes.SUNAT_PIPE +
					cabecera.getTipoMoneda() + Constantes.SUNAT_PIPE +
					cabecera.getSumTributo() + Constantes.SUNAT_PIPE +
					cabecera.getTotValorVenta() + Constantes.SUNAT_PIPE +
					cabecera.getTotPrecioVenta() + Constantes.SUNAT_PIPE +
					cabecera.getTotDescuento() + Constantes.SUNAT_PIPE +
					cabecera.getSumOtrosCargos() + Constantes.SUNAT_PIPE +
					cabecera.getTotAnticipos() + Constantes.SUNAT_PIPE +
					cabecera.getImpTotalVenta() + Constantes.SUNAT_PIPE +
					cabecera.getVersionUbl() + Constantes.SUNAT_PIPE +
					cabecera.getCustomizacionDoc();

			bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
			bufferedWriter.write(cadena); 
			resultado = true;

		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}finally{
			try{
				if (bufferedWriter !=null){
					bufferedWriter.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}

		}
		return resultado;
	}
	/*
	 * Genera un archivo plano Detalle
	 */
	public boolean generarArchivoDetalle(List<TblSunatDetalle> listaDetalle, ComprobanteBean entidad){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		String FILENAME = null;//Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA + detalle.getNombreArchivo();
		try{
			for(TblSunatDetalle detalle:listaDetalle){
				if (FILENAME == null){
					//FILENAME = Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA + detalle.getNombreArchivo();
					FILENAME = entidad.getSunatData() + detalle.getNombreArchivo();
					bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
				}else{
					bufferedWriter.newLine();
				}
				cadena = detalle.getCodigoUnidad() + Constantes.SUNAT_PIPE +
						detalle.getCantidad() + Constantes.SUNAT_PIPE +
						detalle.getCodigoProducto() + Constantes.SUNAT_PIPE +
						detalle.getCodigoProductoSunat() + Constantes.SUNAT_PIPE +
						detalle.getDescripcion() + Constantes.SUNAT_PIPE +
						detalle.getValorUnitario()	+ Constantes.SUNAT_PIPE +
						detalle.getSumTributosItem()+ Constantes.SUNAT_PIPE +
						detalle.getTribCodTipoTributoIgv()	+ Constantes.SUNAT_PIPE +
						detalle.getTribMontoIgv()	+ Constantes.SUNAT_PIPE +
						detalle.getTribBaseImponibleIgv()	+ Constantes.SUNAT_PIPE +
						detalle.getTribNombreTributo()	+ Constantes.SUNAT_PIPE +
						detalle.getTribCodTipoTributo() + Constantes.SUNAT_PIPE +
						detalle.getTribAfectacionIgv()	+ Constantes.SUNAT_PIPE +
						detalle.getTribPorcentajeIgv()	+ Constantes.SUNAT_PIPE +
						detalle.getIscCodTipoTributoIsc()	+ Constantes.SUNAT_PIPE +
						detalle.getIscMontoIsc() + Constantes.SUNAT_PIPE +
						detalle.getIscBaseImponibleIsc() + Constantes.SUNAT_PIPE +
						detalle.getIscNombreTributo() + Constantes.SUNAT_PIPE +
						detalle.getIscCodTipoTributo()	+ Constantes.SUNAT_PIPE +
						detalle.getIscTipoSistema() + Constantes.SUNAT_PIPE +
						detalle.getIscPorcentaje()	+ Constantes.SUNAT_PIPE +
						detalle.getOtroCodTipoTributoOtro() + Constantes.SUNAT_PIPE +
						detalle.getOtroMontoTributo()	+ Constantes.SUNAT_PIPE +
						detalle.getOtroBaseImponibleOtro()	+ Constantes.SUNAT_PIPE +
						detalle.getOtroNombreTributo()	+ Constantes.SUNAT_PIPE +
						detalle.getOtroCodTipoTributo() + Constantes.SUNAT_PIPE +
						detalle.getIscPorcentaje()	+ Constantes.SUNAT_PIPE +
						detalle.getPrecioVentaUnitario() + Constantes.SUNAT_PIPE +
						detalle.getValorVentaItem() + Constantes.SUNAT_PIPE +
						detalle.getValorReferencialUnitario();
				bufferedWriter.write(cadena); 

			}
			resultado = true;

		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}finally{
			try{
				if (bufferedWriter !=null){
					bufferedWriter.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return resultado;
	}
	
	/*
	 * Genera un archivo plano Tributo
	 */
	public boolean generarArchivoTributo(List<TblSunatTributoGeneral> listaTributo, ComprobanteBean entidad){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		String FILENAME = null;
		try{
			for(TblSunatTributoGeneral tributo:listaTributo){
				if (FILENAME == null){
					FILENAME = entidad.getSunatData() + tributo.getNombreArchivo();
					bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
				}else{
					bufferedWriter.newLine();
				}
				cadena = tributo.getIdentificadorTributo() + Constantes.SUNAT_PIPE +
						tributo.getNombreTributo() + Constantes.SUNAT_PIPE +
						tributo.getCodigoTipoTributo() + Constantes.SUNAT_PIPE +
						tributo.getBaseImponible() + Constantes.SUNAT_PIPE +
						tributo.getMontoTributoItem();
				bufferedWriter.write(cadena); 

			}
			resultado = true;

		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}finally{
			try{
				if (bufferedWriter !=null){
					bufferedWriter.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return resultado;
	}
	
	/*
	 * Genera un archivo plano Leyenda
	 */
	public boolean generarArchivoLeyenda(TblLeyenda leyenda, TblComprobante comprobante, String nombreFile, ComprobanteBean entidad){
		boolean resultado = false;
		String cadena = null;
		BufferedWriter bufferedWriter = null;
		//String FILENAME = "G:\\data0\\facturador\\DATA\\"+cabecera.getNombreArchivo();
		//String FILENAME = Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA + nombreFile;
		String FILENAME = entidad.getSunatData() + nombreFile;
		try{


			bufferedWriter = new BufferedWriter(new FileWriter(FILENAME, true));
			//Leyenda de moneda
			if (comprobante.getTotalImporte().doubleValue() > 0){
				cadena = Constantes.SUNAT_LEYENDA_MONTO_LETRAS_1000 + 
						Constantes.SUNAT_PIPE + NumberToLetterConverter.convertNumberToLetter(comprobante.getTotalImporte().doubleValue(), comprobante.getMoneda());
			}else{
				//Tomamos el valor de Operaciones Gratuitas
				cadena = Constantes.SUNAT_LEYENDA_MONTO_LETRAS_1000 + 
						Constantes.SUNAT_PIPE + NumberToLetterConverter.convertNumberToLetter(comprobante.getValorOpGratuitas().doubleValue(), comprobante.getMoneda());
			}

			bufferedWriter.write(cadena);
			//Leyenda adicional
			if (leyenda !=null && !leyenda.getCodigoSunat().equals("")){
				bufferedWriter.newLine();
				cadena = leyenda.getCodigoSunat() + Constantes.SUNAT_PIPE +	leyenda.getDescripcionSunat();
				bufferedWriter.write(cadena); 
			}
			resultado = true;

		}catch(Exception e){
			e.printStackTrace();
			resultado = false;
		}finally{
			try{
				if (bufferedWriter !=null){
					bufferedWriter.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}

		}
		return resultado;
	}
}
