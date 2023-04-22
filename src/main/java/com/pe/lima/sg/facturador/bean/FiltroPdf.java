package com.pe.lima.sg.facturador.bean;

import java.util.List;

import com.pe.lima.sg.facturador.entity.TblBandejaFacturador;
import com.pe.lima.sg.facturador.entity.TblBandejaFacturadorNota;
import com.pe.lima.sg.facturador.entity.TblComprobante;
import com.pe.lima.sg.facturador.entity.TblDetalleComprobante;
import com.pe.lima.sg.facturador.entity.TblDetalleNota;
import com.pe.lima.sg.facturador.entity.TblLeyenda;
import com.pe.lima.sg.facturador.entity.TblLeyendaNota;
import com.pe.lima.sg.facturador.entity.TblNota;
import com.pe.lima.sg.facturador.entity.TblSunatCabecera;
import com.pe.lima.sg.facturador.entity.TblSunatCabeceraNota;
import com.pe.lima.sg.facturador.entity.TblSunatDetalle;
import com.pe.lima.sg.facturador.entity.TblSunatDetalleNota;
import com.pe.lima.sg.facturador.entity.TblTributoGeneral;
import com.pe.lima.sg.facturador.entity.TblTributoGeneralNota;



public class FiltroPdf {


	private String login			= "";
	private String estado   		= "";
	private String estadoUsuario	= "";
	
	private String tipo				= "-1";
	private String dato				= "";
	private String fechaInicio		= null;
	private String fechaFin			= null;
	private String paterno			= "";
	private String materno			= "";
	private String dni				= "";
	private String ruc				= "";
	private String razonSocial		= "";
	private Integer codigo			= null;
	private Integer codigoEdificacion = null; //Inmueble
	private String 	strTienda		= ""; //Local
	private String strOperacion		= "";
	private Integer anio			= null;
	private String mes				= null;
	
	private String serie			= "";
	private String tipoComprobante	= "";
	//Cliente
	private String tipoDocumento	= "";
	private String numero			= "";
	private String nombre			= "";
	//Comprobante
	private String fechaEmision;
	private String fechaVencimiento;
	private String horaEmision;
	private String codigoProducto	= null;
	private String codigoFiltro		= null;
	private TblComprobante	comprobante 				= null;
	private List<TblDetalleComprobante> listaDetalle	= null;
	private TblDetalleComprobante detalleComprobante	= null;
	private TblBandejaFacturador bandejaFacturador		= null;
	private TblSunatCabecera sunatCabecera				= null;
	private List<TblSunatDetalle> listaDetalleSunat		= null;
	private List<ParametroFacturadorBean> listaParametro= null;
	private List<TblTributoGeneral> listaTributo		= null;
	private List<TblTributoGeneralNota> listaTributoNota= null;
	private String appRutaContexto						= null;
	private TblLeyenda leyendaSunat						= null;
	//Impuestos
	private String nombreIGV							= null;
	private Integer valorIGV							= null;
	private String nombreServicio						= null;
	private Integer valorServicio						= null;
	//Adicionales para el Reporte
	private String nombreDomicilioFiscal				= null;
	//Adicionales para el repositorio sunat
	private String sunatData							= null;
	private String	sunatBD								= null;
	//Notas
	private TblNota nota								= null;
	private TblDetalleNota detalleNota					= null;
	private List<TblDetalleNota> listaDetalleNota		= null;
	private TblSunatCabeceraNota sunatCabeceraNota		= null;
	private List<TblSunatDetalleNota> listaDetalleSunatNota		= null;
	private TblBandejaFacturadorNota bandejaFacturadorNota		= null;
	private TblLeyendaNota leyendaNotaSunat				= null;
	private Integer indice								= null;
	
	//Campos temporales para no tener conflicto con el comprobante en la pantalla de notas
	private String tipoComprobanteNota					= null;
	private String serieNota							= null;
	private String numeroNota							= null;
	//Configuracion de la serie
	private String flagSerieAutomatica					= null;
	
	public FiltroPdf() {
		// TODO Auto-generated constructor stub
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getEstadoUsuario() {
		return estadoUsuario;
	}


	public void setEstadoUsuario(String estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getDato() {
		return dato;
	}


	public void setDato(String dato) {
		this.dato = dato;
	}


	


	public String getPaterno() {
		return paterno;
	}


	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}


	public String getMaterno() {
		return materno;
	}


	public void setMaterno(String materno) {
		this.materno = materno;
	}


	public String getDni() {
		return dni;
	}


	public void setDni(String dni) {
		this.dni = dni;
	}


	public String getRuc() {
		return ruc;
	}


	public void setRuc(String ruc) {
		this.ruc = ruc;
	}


	public String getRazonSocial() {
		return razonSocial;
	}


	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	public Integer getCodigo() {
		return codigo;
	}


	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}


	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	public String getFechaInicio() {
		return fechaInicio;
	}


	public String getFechaFin() {
		return fechaFin;
	}


	public Integer getCodigoEdificacion() {
		return codigoEdificacion;
	}


	public void setCodigoEdificacion(Integer codigoEdificacion) {
		this.codigoEdificacion = codigoEdificacion;
	}


	public String getStrTienda() {
		return strTienda;
	}


	public void setStrTienda(String strTienda) {
		this.strTienda = strTienda;
	}


	public String getStrOperacion() {
		return strOperacion;
	}


	public void setStrOperacion(String strOperacion) {
		this.strOperacion = strOperacion;
	}


	public Integer getAnio() {
		return anio;
	}


	public void setAnio(Integer anio) {
		this.anio = anio;
	}


	public TblComprobante getComprobante() {
		return comprobante;
	}


	public void setComprobante(TblComprobante comprobante) {
		this.comprobante = comprobante;
	}


	public List<TblDetalleComprobante> getListaDetalle() {
		return listaDetalle;
	}


	public void setListaDetalle(List<TblDetalleComprobante> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}


	public String getSerie() {
		return serie;
	}


	public void setSerie(String serie) {
		this.serie = serie;
	}


	public String getTipoComprobante() {
		return tipoComprobante;
	}


	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}


	public TblDetalleComprobante getDetalleComprobante() {
		return detalleComprobante;
	}


	public void setDetalleComprobante(TblDetalleComprobante detalleComprobante) {
		this.detalleComprobante = detalleComprobante;
	}


	public String getCodigoProducto() {
		return codigoProducto;
	}


	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}


	public String getFechaEmision() {
		return fechaEmision;
	}


	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}


	public String getFechaVencimiento() {
		return fechaVencimiento;
	}


	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}


	public String getCodigoFiltro() {
		return codigoFiltro;
	}


	public void setCodigoFiltro(String codigoFiltro) {
		this.codigoFiltro = codigoFiltro;
	}


	public String getTipoDocumento() {
		return tipoDocumento;
	}


	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}


	public TblBandejaFacturador getBandejaFacturador() {
		return bandejaFacturador;
	}


	public void setBandejaFacturador(TblBandejaFacturador bandejaFacturador) {
		this.bandejaFacturador = bandejaFacturador;
	}


	public TblSunatCabecera getSunatCabecera() {
		return sunatCabecera;
	}


	public void setSunatCabecera(TblSunatCabecera sunatCabecera) {
		this.sunatCabecera = sunatCabecera;
	}


	public List<TblSunatDetalle> getListaDetalleSunat() {
		return listaDetalleSunat;
	}


	public void setListaDetalleSunat(List<TblSunatDetalle> listaDetalleSunat) {
		this.listaDetalleSunat = listaDetalleSunat;
	}


	public List<ParametroFacturadorBean> getListaParametro() {
		return listaParametro;
	}


	public void setListaParametro(List<ParametroFacturadorBean> listaParametro) {
		this.listaParametro = listaParametro;
	}


	public String getAppRutaContexto() {
		return appRutaContexto;
	}


	public void setAppRutaContexto(String appRutaContexto) {
		this.appRutaContexto = appRutaContexto;
	}


	public String getNombreIGV() {
		return nombreIGV;
	}


	public void setNombreIGV(String nombreIGV) {
		this.nombreIGV = nombreIGV;
	}


	public Integer getValorIGV() {
		return valorIGV;
	}


	public void setValorIGV(Integer valorIGV) {
		this.valorIGV = valorIGV;
	}


	public String getNombreServicio() {
		return nombreServicio;
	}


	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}


	public Integer getValorServicio() {
		return valorServicio;
	}


	public void setValorServicio(Integer valorServicio) {
		this.valorServicio = valorServicio;
	}


	public TblLeyenda getLeyendaSunat() {
		return leyendaSunat;
	}


	public void setLeyendaSunat(TblLeyenda leyendaSunat) {
		this.leyendaSunat = leyendaSunat;
	}


	public String getNombreDomicilioFiscal() {
		return nombreDomicilioFiscal;
	}


	public void setNombreDomicilioFiscal(String nombreDomicilioFiscal) {
		this.nombreDomicilioFiscal = nombreDomicilioFiscal;
	}


	public String getSunatData() {
		return sunatData;
	}


	public void setSunatData(String sunatData) {
		this.sunatData = sunatData;
	}


	public String getSunatBD() {
		return sunatBD;
	}


	public void setSunatBD(String sunatBD) {
		this.sunatBD = sunatBD;
	}


	public TblNota getNota() {
		return nota;
	}


	public void setNota(TblNota nota) {
		this.nota = nota;
	}


	public TblDetalleNota getDetalleNota() {
		return detalleNota;
	}


	public void setDetalleNota(TblDetalleNota detalleNota) {
		this.detalleNota = detalleNota;
	}


	public List<TblDetalleNota> getListaDetalleNota() {
		return listaDetalleNota;
	}


	public void setListaDetalleNota(List<TblDetalleNota> listaDetalleNota) {
		this.listaDetalleNota = listaDetalleNota;
	}


	public TblSunatCabeceraNota getSunatCabeceraNota() {
		return sunatCabeceraNota;
	}


	public void setSunatCabeceraNota(TblSunatCabeceraNota sunatCabeceraNota) {
		this.sunatCabeceraNota = sunatCabeceraNota;
	}


	public List<TblSunatDetalleNota> getListaDetalleSunatNota() {
		return listaDetalleSunatNota;
	}


	public void setListaDetalleSunatNota(List<TblSunatDetalleNota> listaDetalleSunatNota) {
		this.listaDetalleSunatNota = listaDetalleSunatNota;
	}


	public TblBandejaFacturadorNota getBandejaFacturadorNota() {
		return bandejaFacturadorNota;
	}


	public void setBandejaFacturadorNota(TblBandejaFacturadorNota bandejaFacturadorNota) {
		this.bandejaFacturadorNota = bandejaFacturadorNota;
	}


	public TblLeyendaNota getLeyendaNotaSunat() {
		return leyendaNotaSunat;
	}


	public void setLeyendaNotaSunat(TblLeyendaNota leyendaNotaSunat) {
		this.leyendaNotaSunat = leyendaNotaSunat;
	}


	public String getTipoComprobanteNota() {
		return tipoComprobanteNota;
	}


	public void setTipoComprobanteNota(String tipoComprobanteNota) {
		this.tipoComprobanteNota = tipoComprobanteNota;
	}


	public String getSerieNota() {
		return serieNota;
	}


	public void setSerieNota(String serieNota) {
		this.serieNota = serieNota;
	}


	public String getNumeroNota() {
		return numeroNota;
	}


	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}


	public Integer getIndice() {
		return indice;
	}


	public void setIndice(Integer indice) {
		this.indice = indice;
	}


	public String getMes() {
		return mes;
	}


	public void setMes(String mes) {
		this.mes = mes;
	}


	public String getHoraEmision() {
		return horaEmision;
	}


	public void setHoraEmision(String horaEmision) {
		this.horaEmision = horaEmision;
	}


	public List<TblTributoGeneral> getListaTributo() {
		return listaTributo;
	}


	public void setListaTributo(List<TblTributoGeneral> listaTributo) {
		this.listaTributo = listaTributo;
	}


	public List<TblTributoGeneralNota> getListaTributoNota() {
		return listaTributoNota;
	}


	public void setListaTributoNota(List<TblTributoGeneralNota> listaTributoNota) {
		this.listaTributoNota = listaTributoNota;
	}


	public String getFlagSerieAutomatica() {
		return flagSerieAutomatica;
	}


	public void setFlagSerieAutomatica(String flagSerieAutomatica) {
		this.flagSerieAutomatica = flagSerieAutomatica;
	}

}
