package com.pe.lima.sg.entity.caja;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import com.pe.lima.sg.presentacion.util.Constantes;

@Entity
@Table(name = "tbl_masivo_sunat", schema = "caj")
public class TblMasivoSunat implements java.io.Serializable {
	

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
	private Date fechaProceso;	
	
	public TblMasivoSunat(int codigoMasivo, int codigoEdificio, String periodo, int xmlGenerado, int xmlError,
			int xmlTotal, int xmlIntento, int cdrGenerado, int cdrError, int cdrTotal, int cdrIntento, int pdfGenerado,
			int pdfError, int pdfTotal, int pdfIntento, int totalProcesada, int totalExcluido, String estadoMasivo,
			String fechaEmision, String estado, Integer usuarioCreacion, Integer usuarioModificacion,
			Date fechaCreacion, Date fechaModificacion, String ipCreacion, String ipModificacion) {
		super();
		this.codigoMasivo = codigoMasivo;
		this.codigoEdificio = codigoEdificio;
		this.periodo = periodo;
		this.xmlGenerado = xmlGenerado;
		this.xmlError = xmlError;
		this.xmlTotal = xmlTotal;
		this.xmlIntento = xmlIntento;
		this.cdrGenerado = cdrGenerado;
		this.cdrError = cdrError;
		this.cdrTotal = cdrTotal;
		this.cdrIntento = cdrIntento;
		this.pdfGenerado = pdfGenerado;
		this.pdfError = pdfError;
		this.pdfTotal = pdfTotal;
		this.pdfIntento = pdfIntento;
		this.totalProcesada = totalProcesada;
		this.totalExcluido = totalExcluido;
		this.estadoMasivo = estadoMasivo;
		this.fechaEmision = fechaEmision;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
	}


	public TblMasivoSunat() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_masivo", unique = true, nullable = false)
	public int getCodigoMasivo() {
		return codigoMasivo;
	}


	public void setCodigoMasivo(int codigoMasivo) {
		this.codigoMasivo = codigoMasivo;
	}

	@Column(name="estado", length = 1)
	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "usuario_creacion")
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}


	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	@Column(name = "usuario_modificacion")
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}


	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", length = 29)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}


	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_modificacion", length = 29)
	public Date getFechaModificacion() {
		return fechaModificacion;
	}


	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	@Column(name = "ip_creacion", length = 64)
	public String getIpCreacion() {
		return ipCreacion;
	}


	public void setIpCreacion(String ipCreacion) {
		this.ipCreacion = ipCreacion;
	}

	@Column(name = "ip_modificacion", length = 64)
	public String getIpModificacion() {
		return ipModificacion;
	}


	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}


	public void setAuditoriaCreacion(HttpServletRequest request){
		Integer idUsuario 		= null;
		String strIdUsuario 	= null;
		try{
			this.setFechaCreacion(new Date(System.currentTimeMillis()));
			this.setIpCreacion(request.getRemoteAddr());
			this.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			strIdUsuario = (String)request.getSession().getAttribute("id_usuario");
			if (strIdUsuario==null){
				idUsuario = 1;
			}else{
				idUsuario = new Integer(strIdUsuario);
			}
			this.setUsuarioCreacion(idUsuario);
		}catch(Exception e){
			e.printStackTrace();
			this.setUsuarioCreacion(0);
		}finally{
			idUsuario 		= null;
			strIdUsuario	= null;
		}
	}
	
	public void setAuditoriaModificacion(HttpServletRequest request){
		Integer idUsuario 		= null;
		String strIdUsuario 	= null;
		try{
			this.setFechaModificacion(new Date(System.currentTimeMillis()));
			this.setIpModificacion(request.getRemoteAddr());
			strIdUsuario = (String)request.getSession().getAttribute("id_usuario");
			if (strIdUsuario==null){
				idUsuario = 1;
			}else{
				idUsuario = new Integer(strIdUsuario);
			}
			this.setUsuarioModificacion(idUsuario);
		}catch(Exception e){
			e.printStackTrace();
			this.setUsuarioModificacion(0);
		}finally{
			idUsuario 		= null;
			strIdUsuario	= null;
		}
	}

	@Column(name = "periodo", length = 16)
	public String getPeriodo() {
		return periodo;
	}


	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	@Column(name = "xml_generado")
	public int getXmlGenerado() {
		return xmlGenerado;
	}


	public void setXmlGenerado(int xmlGenerado) {
		this.xmlGenerado = xmlGenerado;
	}

	@Column(name = "xml_error")
	public int getXmlError() {
		return xmlError;
	}


	public void setXmlError(int xmlError) {
		this.xmlError = xmlError;
	}

	@Column(name = "xml_total")
	public int getXmlTotal() {
		return xmlTotal;
	}


	public void setXmlTotal(int xmlTotal) {
		this.xmlTotal = xmlTotal;
	}

	@Column(name = "xml_intento")
	public int getXmlIntento() {
		return xmlIntento;
	}


	public void setXmlIntento(int xmlIntento) {
		this.xmlIntento = xmlIntento;
	}

	@Column(name = "cdr_generado")
	public int getCdrGenerado() {
		return cdrGenerado;
	}

	
	public void setCdrGenerado(int cdrGenerado) {
		this.cdrGenerado = cdrGenerado;
	}

	@Column(name = "cdr_error")
	public int getCdrError() {
		return cdrError;
	}


	public void setCdrError(int cdrError) {
		this.cdrError = cdrError;
	}

	@Column(name = "cdr_total")
	public int getCdrTotal() {
		return cdrTotal;
	}


	public void setCdrTotal(int cdrTotal) {
		this.cdrTotal = cdrTotal;
	}

	@Column(name = "cdr_intento")
	public int getCdrIntento() {
		return cdrIntento;
	}


	public void setCdrIntento(int cdrIntento) {
		this.cdrIntento = cdrIntento;
	}

	@Column(name = "pdf_generado")
	public int getPdfGenerado() {
		return pdfGenerado;
	}


	public void setPdfGenerado(int pdfGenerado) {
		this.pdfGenerado = pdfGenerado;
	}

	@Column(name = "pdf_error")
	public int getPdfError() {
		return pdfError;
	}


	public void setPdfError(int pdfError) {
		this.pdfError = pdfError;
	}

	@Column(name = "pdf_total")
	public int getPdfTotal() {
		return pdfTotal;
	}


	public void setPdfTotal(int pdfTotal) {
		this.pdfTotal = pdfTotal;
	}

	@Column(name = "pdf_intento")
	public int getPdfIntento() {
		return pdfIntento;
	}


	public void setPdfIntento(int pdfIntento) {
		this.pdfIntento = pdfIntento;
	}

	@Column(name = "total_procesada")
	public int getTotalProcesada() {
		return totalProcesada;
	}


	public void setTotalProcesada(int totalProcesada) {
		this.totalProcesada = totalProcesada;
	}

	@Column(name = "total_excluido")
	public int getTotalExcluido() {
		return totalExcluido;
	}


	public void setTotalExcluido(int totalExcluido) {
		this.totalExcluido = totalExcluido;
	}

	@Column(name = "estado_masivo", length = 16)
	public String getEstadoMasivo() {
		return estadoMasivo;
	}


	public void setEstadoMasivo(String estadoMasivo) {
		this.estadoMasivo = estadoMasivo;
	}

	@Column(name = "fecha_emision", length = 10)
	public String getFechaEmision() {
		return fechaEmision;
	}


	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	@Column(name = "codigo_edificio")
	public int getCodigoEdificio() {
		return codigoEdificio;
	}


	public void setCodigoEdificio(int codigEdificio) {
		this.codigoEdificio = codigEdificio;
	}

	@Column(name = "tipo_masivo", length = 8)
	public String getTipoMasivo() {
		return tipoMasivo;
	}


	public void setTipoMasivo(String tipoMasivo) {
		this.tipoMasivo = tipoMasivo;
	}


	@Column(name = "anio")
	public Integer getAnio() {
		return anio;
	}


	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	@Column(name = "mes", length = 8)
	public String getMes() {
		return mes;
	}


	public void setMes(String mes) {
		this.mes = mes;
	}

	@Column(name = "csv_enviado")
	public int getCsvEnviado() {
		return csvEnviado;
	}


	public void setCsvEnviado(int csvEnviado) {
		this.csvEnviado = csvEnviado;
	}

	@Column(name = "csv_error")
	public int getCsvError() {
		return csvError;
	}


	public void setCsvError(int csvError) {
		this.csvError = csvError;
	}

	@Column(name = "csv_total")
	public int getCsvTotal() {
		return csvTotal;
	}


	public void setCsvTotal(int csvTotal) {
		this.csvTotal = csvTotal;
	}

	@Column(name = "csv_intento")
	public int getCsvIntento() {
		return csvIntento;
	}


	public void setCsvIntento(int csvIntento) {
		this.csvIntento = csvIntento;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_proceso", length = 29)
	public Date getFechaProceso() {
		return fechaProceso;
	}


	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	
}
