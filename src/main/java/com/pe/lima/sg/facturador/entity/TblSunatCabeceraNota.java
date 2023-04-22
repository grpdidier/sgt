package com.pe.lima.sg.facturador.entity;
// Generated 7/01/2018 06:30:19 PM by Hibernate Tools 5.2.3.Final

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pe.lima.sg.presentacion.util.Constantes;

/**
 * TblSunatCabeceraNota generated by hbm2java
 */
@Entity
@Table(name = "tbl_sunat_cabecera_nota", schema = "ope")
public class TblSunatCabeceraNota implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int codigoCabecera;
	private TblNota tblNota;
	private String fechaEmision;
	private String tipoNota;
	private String descripcionTipoNota;
	private String codigoComprobanteAfecta;
	private String serieNumeroAfecta;
	private String tipoDocumentoUsuario;
	private String numeroDocumento;
	private String razonSocial;
	private String tipoMoneda;
	private BigDecimal sumaCargo;
	private BigDecimal operacionGravada;
	private BigDecimal operacionInafecta;
	private BigDecimal operacionExonerada;
	private BigDecimal montoIgv;
	private BigDecimal montoIsc;
	private BigDecimal otrosTributos;
	private BigDecimal importeTotal;
	private String nombreArchivo;
	private String estado;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private String ipCreacion;
	private String ipModificacion;
	private String tipoOperacion;
	private String horaEmision;
	private String sumTributo;
	private String totValorVenta;
	private String totPrecioVenta;
	private String totDescuento;
	private String totAnticipo;
	private String versionUbl;
	private String cutomizacionDocumento;
	private String codigoDomicilio;
	private String sumOtrosCargos;
	private Set<TblSunatDetalleNota> tblSunatDetalleNotas = new HashSet<TblSunatDetalleNota>(0);

	public TblSunatCabeceraNota() {
	}

	public TblSunatCabeceraNota(int codigoCabecera, TblNota tblNota) {
		this.codigoCabecera = codigoCabecera;
		this.tblNota = tblNota;
	}

	public TblSunatCabeceraNota(int codigoCabecera, TblNota tblNota, String fechaEmision, String tipoNota,
			String descripcionTipoNota, String codigoComprobanteAfecta, String serieNumeroAfecta,
			String tipoDocumentoUsuario, String numeroDocumento, String razonSocial, String tipoMoneda,
			BigDecimal sumaCargo, BigDecimal operacionGravada, BigDecimal operacionInafecta,
			BigDecimal operacionExonerada, BigDecimal montoIgv, BigDecimal montoIsc, BigDecimal otrosTributos,
			BigDecimal importeTotal, String nombreArchivo, String estado, Date fechaCreacion, Date fechaModificacion,
			Integer usuarioCreacion, Integer usuarioModificacion, String ipCreacion, String ipModificacion,
			String tipoOperacion, String horaEmision, String sumTributo, String totValorVenta, String totPrecioVenta,
			String totDescuento, String totAnticipo, String versionUbl, String cutomizacionDocumento, String codigoDomicilio, String sumOtrosCargos,
			Set<TblSunatDetalleNota> tblSunatDetalleNotas) {
		this.codigoCabecera = codigoCabecera;
		this.tblNota = tblNota;
		this.fechaEmision = fechaEmision;
		this.tipoNota = tipoNota;
		this.descripcionTipoNota = descripcionTipoNota;
		this.codigoComprobanteAfecta = codigoComprobanteAfecta;
		this.serieNumeroAfecta = serieNumeroAfecta;
		this.tipoDocumentoUsuario = tipoDocumentoUsuario;
		this.numeroDocumento = numeroDocumento;
		this.razonSocial = razonSocial;
		this.tipoMoneda = tipoMoneda;
		this.sumaCargo = sumaCargo;
		this.operacionGravada = operacionGravada;
		this.operacionInafecta = operacionInafecta;
		this.operacionExonerada = operacionExonerada;
		this.montoIgv = montoIgv;
		this.montoIsc = montoIsc;
		this.otrosTributos = otrosTributos;
		this.importeTotal = importeTotal;
		this.nombreArchivo = nombreArchivo;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
		this.tipoOperacion = tipoOperacion;
		this.horaEmision = horaEmision;
		this.sumTributo = sumTributo;
		this.totValorVenta = totValorVenta;
		this.totPrecioVenta = totPrecioVenta;
		this.totDescuento = totDescuento;
		this.totAnticipo = totAnticipo;
		this.versionUbl = versionUbl;
		this.cutomizacionDocumento = cutomizacionDocumento;
		this.codigoDomicilio = codigoDomicilio;
		this.sumOtrosCargos = sumOtrosCargos;
		this.tblSunatDetalleNotas = tblSunatDetalleNotas;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_cabecera", unique = true, nullable = false)
	public int getCodigoCabecera() {
		return this.codigoCabecera;
	}

	public void setCodigoCabecera(int codigoCabecera) {
		this.codigoCabecera = codigoCabecera;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_nota", nullable = false)
	@JsonBackReference(value="tablaManage-TablaBack")
	public TblNota getTblNota() {
		return this.tblNota;
	}

	public void setTblNota(TblNota tblNota) {
		this.tblNota = tblNota;
	}

	@Column(name = "fecha_emision", length = 10)
	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	@Column(name = "tipo_nota", length = 8)
	public String getTipoNota() {
		return this.tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	@Column(name = "descripcion_tipo_nota", length = 250)
	public String getDescripcionTipoNota() {
		return this.descripcionTipoNota;
	}

	public void setDescripcionTipoNota(String descripcionTipoNota) {
		this.descripcionTipoNota = descripcionTipoNota;
	}

	@Column(name = "codigo_comprobante_afecta", length = 8)
	public String getCodigoComprobanteAfecta() {
		return this.codigoComprobanteAfecta;
	}

	public void setCodigoComprobanteAfecta(String codigoComprobanteAfecta) {
		this.codigoComprobanteAfecta = codigoComprobanteAfecta;
	}

	@Column(name = "serie_numero_afecta", length = 13)
	public String getSerieNumeroAfecta() {
		return this.serieNumeroAfecta;
	}

	public void setSerieNumeroAfecta(String serieNumeroAfecta) {
		this.serieNumeroAfecta = serieNumeroAfecta;
	}

	@Column(name = "tipo_documento_usuario", length = 1)
	public String getTipoDocumentoUsuario() {
		return this.tipoDocumentoUsuario;
	}

	public void setTipoDocumentoUsuario(String tipoDocumentoUsuario) {
		this.tipoDocumentoUsuario = tipoDocumentoUsuario;
	}

	@Column(name = "numero_documento", length = 15)
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@Column(name = "razon_social", length = 100)
	public String getRazonSocial() {
		return this.razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	@Column(name = "tipo_moneda", length = 3)
	public String getTipoMoneda() {
		return this.tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	@Column(name = "suma_cargo", precision = 12)
	public BigDecimal getSumaCargo() {
		return this.sumaCargo;
	}

	public void setSumaCargo(BigDecimal sumaCargo) {
		this.sumaCargo = sumaCargo;
	}

	@Column(name = "operacion_gravada", precision = 12)
	public BigDecimal getOperacionGravada() {
		return this.operacionGravada;
	}

	public void setOperacionGravada(BigDecimal operacionGravada) {
		this.operacionGravada = operacionGravada;
	}

	@Column(name = "operacion_inafecta", precision = 12)
	public BigDecimal getOperacionInafecta() {
		return this.operacionInafecta;
	}

	public void setOperacionInafecta(BigDecimal operacionInafecta) {
		this.operacionInafecta = operacionInafecta;
	}

	@Column(name = "operacion_exonerada", precision = 12)
	public BigDecimal getOperacionExonerada() {
		return this.operacionExonerada;
	}

	public void setOperacionExonerada(BigDecimal operacionExonerada) {
		this.operacionExonerada = operacionExonerada;
	}

	@Column(name = "monto_igv", precision = 12)
	public BigDecimal getMontoIgv() {
		return this.montoIgv;
	}

	public void setMontoIgv(BigDecimal montoIgv) {
		this.montoIgv = montoIgv;
	}

	@Column(name = "monto_isc", precision = 12)
	public BigDecimal getMontoIsc() {
		return this.montoIsc;
	}

	public void setMontoIsc(BigDecimal montoIsc) {
		this.montoIsc = montoIsc;
	}

	@Column(name = "otros_tributos", precision = 12)
	public BigDecimal getOtrosTributos() {
		return this.otrosTributos;
	}

	public void setOtrosTributos(BigDecimal otrosTributos) {
		this.otrosTributos = otrosTributos;
	}

	@Column(name = "importe_total", precision = 12)
	public BigDecimal getImporteTotal() {
		return this.importeTotal;
	}

	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}

	@Column(name = "nombre_archivo", length = 128)
	public String getNombreArchivo() {
		return this.nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	@Column(name = "estado", length = 1)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", length = 29)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_modificacion", length = 29)
	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	@Column(name = "usuario_creacion")
	public Integer getUsuarioCreacion() {
		return this.usuarioCreacion;
	}

	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	@Column(name = "usuario_modificacion")
	public Integer getUsuarioModificacion() {
		return this.usuarioModificacion;
	}

	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	@Column(name = "ip_creacion", length = 64)
	public String getIpCreacion() {
		return this.ipCreacion;
	}

	public void setIpCreacion(String ipCreacion) {
		this.ipCreacion = ipCreacion;
	}

	@Column(name = "ip_modificacion", length = 64)
	public String getIpModificacion() {
		return this.ipModificacion;
	}

	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}

	@Column(name = "tipo_operacion", length = 4)
	public String getTipoOperacion() {
		return this.tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	@Column(name = "hora_emision", length = 14)
	public String getHoraEmision() {
		return this.horaEmision;
	}

	public void setHoraEmision(String horaEmision) {
		this.horaEmision = horaEmision;
	}

	@Column(name = "sum_tributo", length = 15)
	public String getSumTributo() {
		return this.sumTributo;
	}

	public void setSumTributo(String sumTributo) {
		this.sumTributo = sumTributo;
	}

	@Column(name = "tot_valor_venta", length = 15)
	public String getTotValorVenta() {
		return this.totValorVenta;
	}

	public void setTotValorVenta(String totValorVenta) {
		this.totValorVenta = totValorVenta;
	}

	@Column(name = "tot_precio_venta", length = 15)
	public String getTotPrecioVenta() {
		return this.totPrecioVenta;
	}

	public void setTotPrecioVenta(String totPrecioVenta) {
		this.totPrecioVenta = totPrecioVenta;
	}

	@Column(name = "tot_descuento", length = 15)
	public String getTotDescuento() {
		return this.totDescuento;
	}

	public void setTotDescuento(String totDescuento) {
		this.totDescuento = totDescuento;
	}

	@Column(name = "tot_anticipo", length = 15)
	public String getTotAnticipo() {
		return this.totAnticipo;
	}

	public void setTotAnticipo(String totAnticipo) {
		this.totAnticipo = totAnticipo;
	}

	@Column(name = "version_ubl", length = 3)
	public String getVersionUbl() {
		return this.versionUbl;
	}

	public void setVersionUbl(String versionUbl) {
		this.versionUbl = versionUbl;
	}

	@Column(name = "cutomizacion_documento", length = 3)
	public String getCutomizacionDocumento() {
		return this.cutomizacionDocumento;
	}

	public void setCutomizacionDocumento(String cutomizacionDocumento) {
		this.cutomizacionDocumento = cutomizacionDocumento;
	}

	@Column(name = "codigo_domicilio", length = 8)
	public String getCodigoDomicilio() {
		return this.codigoDomicilio;
	}

	public void setCodigoDomicilio(String codigoDomicilio) {
		this.codigoDomicilio = codigoDomicilio;
	}
	
	@Column(name = "sum_otros_cargos", length = 15)
	public String getSumOtrosCargos() {
		return this.sumOtrosCargos;
	}

	public void setSumOtrosCargos(String sumOtrosCargos) {
		this.sumOtrosCargos = sumOtrosCargos;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblSunatCabeceraNota")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblSunatDetalleNota> getTblSunatDetalleNotas() {
		return this.tblSunatDetalleNotas;
	}

	public void setTblSunatDetalleNotas(Set<TblSunatDetalleNota> tblSunatDetalleNotas) {
		this.tblSunatDetalleNotas = tblSunatDetalleNotas;
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