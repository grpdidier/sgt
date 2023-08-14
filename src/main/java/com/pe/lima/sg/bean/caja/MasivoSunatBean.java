package com.pe.lima.sg.bean.caja;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class MasivoSunatBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoMasivo;
	private int codigoEdificio;
	private String periodo;
	private int csvEnviado;
	private int csvError;
	private int csvTotal;
	private int csvIntento;
	private int xmlGenerado;
	private int xmlError;
	private int xmlTotal;
	private int xmlIntento;
	private int cdrGenerado;
	private int cdrError;
	private int cdrTotal;
	private int cdrIntento;
	private int pdfGenerado;
	private int pdfError;
	private int pdfTotal;
	private int pdfIntento;
	private int totalProcesada;
	private int totalExcluido;
	private String estadoMasivo;
	private String fechaEmision;
	private String tipoMasivo;
	private Integer anio;
	private String mes;
	private String estado;	
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;
	private Date fechaCreacion;	
	private Date fechaModificacion;		
	private String ipCreacion;	
	private String ipModificacion;
	//Datos adicionales
	private String nombreEdificio;
	private String flagAdicionar;
	private String flagProcesa;
	private String flagEliminar;
	private Integer codigoTienda;
	//Lista de tiendas a generar factura
	private List<MasivoTiendaSunatBean> listaTiendaSunat;
	//Tiendas excluidas
	private Map<Integer,String> mapTiendaExcluidas;
	private Map<String,Integer> mapTiendaExcluidasComboBox;
	//Para los comprobantes
	List<ComprobanteSunatBean> listaComprobanteBean;
	
	

	

}
