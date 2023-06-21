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
@Table(name = "tbl_nota_sunat", schema = "caj")
public class TblNotaSunat implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int codigoNota;
	private String tipoComprobante;
	private String tipoNota;
	private String serie;
	private String numero;
	private String fechaEmision;
	private String horaEmision;
	private String sustento;
	private String moneda;
	private String estadoOperacion;
	private String numeroTicket;
	private String nombreCsv;
	private String nombreCdr;
	private String nombreXml;
	private String nombrePdf;
	private int codigoComprobante;
	private String estado;	
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;
	private Date fechaCreacion;	
	private Date fechaModificacion;		
	private String ipCreacion;	
	private String ipModificacion;
	private String numeroTienda;
	private String serieFactura;
	private String numeroFactura;
	private String tipoPago;
	
	public TblNotaSunat(int codigoNota, String tipoComprobante, String tipoNota, String serie, String numero,
			String fechaEmision, String horaEmision, String sustento, String moneda, String estadoOperacion,
			String numeroTicket, String nombreCsv, String nombreCdr, String nombreXml, String nombrePdf,
			int codigoComprobante, String estado, Integer usuarioCreacion, Integer usuarioModificacion,
			Date fechaCreacion, Date fechaModificacion, String ipCreacion, String ipModificacion) {
		super();
		this.codigoNota = codigoNota;
		this.tipoComprobante = tipoComprobante;
		this.tipoNota = tipoNota;
		this.serie = serie;
		this.numero = numero;
		this.fechaEmision = fechaEmision;
		this.horaEmision = horaEmision;
		this.sustento = sustento;
		this.moneda = moneda;
		this.estadoOperacion = estadoOperacion;
		this.numeroTicket = numeroTicket;
		this.nombreCsv = nombreCsv;
		this.nombreCdr = nombreCdr;
		this.nombreXml = nombreXml;
		this.nombrePdf = nombrePdf;
		this.codigoComprobante = codigoComprobante;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
	}


	public TblNotaSunat() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_nota", unique = true, nullable = false)
	public int getCodigoNota() {
		return codigoNota;
	}


	public void setCodigoNota(int codigoNota) {
		this.codigoNota = codigoNota;
	}


	@Column(name="tipo_comprobante", length = 8)
	public String getTipoComprobante() {
		return tipoComprobante;
	}


	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}


	@Column(name="tipo_nota", length = 8)
	public String getTipoNota() {
		return tipoNota;
	}


	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}


	@Column(name="serie", length = 8)
	public String getSerie() {
		return serie;
	}


	public void setSerie(String serie) {
		this.serie = serie;
	}

	@Column(name="numero", length = 16)
	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	@Column(name="fecha_emision", length = 10)
	public String getFechaEmision() {
		return fechaEmision;
	}


	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}


	@Column(name="hora_emision", length = 8)
	public String getHoraEmision() {
		return horaEmision;
	}


	public void setHoraEmision(String horaEmision) {
		this.horaEmision = horaEmision;
	}

	@Column(name="sustento", length = 256)
	public String getSustento() {
		return sustento;
	}


	public void setSustento(String sustento) {
		this.sustento = sustento;
	}


	@Column(name="moneda", length = 8)
	public String getMoneda() {
		return moneda;
	}


	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	@Column(name="estado_operacion", length = 32)
	public String getEstadoOperacion() {
		return estadoOperacion;
	}


	public void setEstadoOperacion(String estadoOperacion) {
		this.estadoOperacion = estadoOperacion;
	}

	@Column(name="numero_ticket", length = 256)
	public String getNumeroTicket() {
		return numeroTicket;
	}


	public void setNumeroTicket(String numeroTicket) {
		this.numeroTicket = numeroTicket;
	}

	@Column(name="nombre_csv", length = 128)
	public String getNombreCsv() {
		return nombreCsv;
	}


	public void setNombreCsv(String nombreCsv) {
		this.nombreCsv = nombreCsv;
	}

	@Column(name="nombre_cdr", length = 128)
	public String getNombreCdr() {
		return nombreCdr;
	}

	
	public void setNombreCdr(String nombreCdr) {
		this.nombreCdr = nombreCdr;
	}

	@Column(name="nombre_xml", length = 128)
	public String getNombreXml() {
		return nombreXml;
	}


	public void setNombreXml(String nombreXml) {
		this.nombreXml = nombreXml;
	}

	@Column(name="nombre_pdf", length = 128)
	public String getNombrePdf() {
		return nombrePdf;
	}


	public void setNombrePdf(String nombrePdf) {
		this.nombrePdf = nombrePdf;
	}

	@Column(name="codigo_comprobante")
	public int getCodigoComprobante() {
		return codigoComprobante;
	}


	public void setCodigoComprobante(int codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
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

	@Column(name = "numero_tienda", length = 64)
	public String getNumeroTienda() {
		return numeroTienda;
	}


	public void setNumeroTienda(String numeroTienda) {
		this.numeroTienda = numeroTienda;
	}

	@Column(name = "serie_comprobante", length = 8)
	public String getSerieFactura() {
		return serieFactura;
	}


	public void setSerieFactura(String serieFactura) {
		this.serieFactura = serieFactura;
	}

	@Column(name = "numero_comprobante", length = 16)
	public String getNumeroFactura() {
		return numeroFactura;
	}


	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	@Column(name = "tipo_pago", length = 3)
	public String getTipoPago() {
		return tipoPago;
	}


	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
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
	
}
