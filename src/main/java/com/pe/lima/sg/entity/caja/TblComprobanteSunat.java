package com.pe.lima.sg.entity.caja;

import java.io.Serializable;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pe.lima.sg.presentacion.util.Constantes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * The persistent class for the tbl_comprobante database table.
 * 
 */
@Entity
@Table(name="tbl_comprobante_sunat", schema = "caj")
public class TblComprobanteSunat implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tipoOperacion;
	private String domicilioFiscal;
	private String serie;
	private String numero;
	private String fechaEmision;	
	private String horaEmision;	
	private String fechaVencimiento;	
	private String clienteTipoDocumento;
	private String clienteNumero;
	private String clienteDireccion;
	private String clienteNombre;	
	private BigDecimal totalGravados;	
	private BigDecimal totalIgv;
	private BigDecimal total;	
	private String formaPago;	
	
	private Integer codigoComprobante;
	private BigDecimal detracionMonto;
	private BigDecimal detracionPorcentaje;	
	private BigDecimal detracionTotal;	
	private String estado;	
	private Date fechaCreacion;	
	private Date fechaModificacion;	
	
	private String ipCreacion;	
	private String ipModificacion;	
	private String moneda;
	
	
		
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;
	private Set<TblDetalleComprobante> tblDetalleComprobantes = new HashSet<TblDetalleComprobante>(0);
	private Set<TblDetalleFormaPago> tblDetalleFormaPagos = new HashSet<TblDetalleFormaPago>(0);
	private String tipoPago;
	private Integer codigoContrato;
	private String estadoOperacion;
	private String numeroTienda;
	private String numeroTicket;
	private String nombreCsv;
	private String nombreCdr;
	private String nombreXml;
	private String nombrePdf;
	private Integer codigoNota;
	private String serieNota;
	private String numeroNota;

	public TblComprobanteSunat() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_comprobante", unique = true, nullable = false)
	public Integer getCodigoComprobante() {
		return this.codigoComprobante;
	}
	public void setCodigoComprobante(Integer codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}

	@Column(name="cliente_direccion", length = 512)
	public String getClienteDireccion() {
		return this.clienteDireccion;
	}
	public void setClienteDireccion(String clienteDireccion) {
		this.clienteDireccion = clienteDireccion;
	}

	@Column(name="cliente_nombre", length = 512)
	public String getClienteNombre() {
		return this.clienteNombre;
	}
	public void setClienteNombre(String clienteNombre) {
		this.clienteNombre = clienteNombre;
	}

	@Column(name="cliente_numero", length = 16)
	public String getClienteNumero() {
		return this.clienteNumero;
	}
	public void setClienteNumero(String clienteNumero) {
		this.clienteNumero = clienteNumero;
	}

	@Column(name="cliente_tipo_documento")
	public String getClienteTipoDocumento() {
		return this.clienteTipoDocumento;
	}
	public void setClienteTipoDocumento(String clienteTipoDocumento) {
		this.clienteTipoDocumento = clienteTipoDocumento;
	}

	@Column(name="detracion_monto", precision = 12)
	public BigDecimal getDetracionMonto() {
		return this.detracionMonto;
	}	public void setDetracionMonto(BigDecimal detracionMonto) {
		this.detracionMonto = detracionMonto;
	}
	
	@Column(name="detracion_porcentaje", precision = 6)
	public BigDecimal getDetracionPorcentaje() {
		return this.detracionPorcentaje;
	}
	public void setDetracionPorcentaje(BigDecimal detracionPorcentaje) {
		this.detracionPorcentaje = detracionPorcentaje;
	}

	@Column(name="detracion_total", precision = 12)
	public BigDecimal getDetracionTotal() {
		return this.detracionTotal;
	}
	public void setDetracionTotal(BigDecimal detracionTotal) {
		this.detracionTotal = detracionTotal;
	}

	@Column(name="domicilio_fiscal", length = 512)
	public String getDomicilioFiscal() {
		return this.domicilioFiscal;
	}
	public void setDomicilioFiscal(String domicilioFiscal) {
		this.domicilioFiscal = domicilioFiscal;
	}
	
	@Column(name="fecha_emision", length = 10)
	public String getFechaEmision() {
		return this.fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	
	@Column(name="fecha_vencimiento", length = 10)
	public String getFechaVencimiento() {
		return this.fechaVencimiento;	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	@Column(name="forma_pago", length = 8)
	public String getFormaPago() {
		return this.formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	@Column(name="hora_emision", length = 8)
	public String getHoraEmision() {
		return this.horaEmision;
	}
	public void setHoraEmision(String horaEmision) {
		this.horaEmision = horaEmision;
	}	

	@Column(name="moneda", length = 8)
	public String getMoneda() {
		return this.moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	@Column(name="numero", length = 16)
	public String getNumero() {
		return this.numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name="serie", length = 8)
	public String getSerie() {
		return this.serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	@Column(name="tipo_operacion", length = 8)
	public String getTipoOperacion() {
		return this.tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	
	public BigDecimal getTotal() {
		return this.total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	@Column(name="total_gravados", precision = 12)
	public BigDecimal getTotalGravados() {
		return this.totalGravados;
	}

	public void setTotalGravados(BigDecimal totalGravados) {
		this.totalGravados = totalGravados;
	}

	@Column(name="total_igv", precision = 12)
	public BigDecimal getTotalIgv() {
		return this.totalIgv;
	}
	public void setTotalIgv(BigDecimal totalIgv) {
		this.totalIgv = totalIgv;
	}

	

	
	
	@Column(name = "estado", length = 1)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblComprobante")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblDetalleComprobante> getTblDetalleComprobantes() {
		return tblDetalleComprobantes;
	}

	public void setTblDetalleComprobantes(Set<TblDetalleComprobante> tblDetalleComprobantes) {
		this.tblDetalleComprobantes = tblDetalleComprobantes;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblComprobante")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblDetalleFormaPago> getTblDetalleFormaPagos() {
		return tblDetalleFormaPagos;
	}

	public void setTblDetalleFormaPagos(Set<TblDetalleFormaPago> tblDetalleFormaPagos) {
		this.tblDetalleFormaPagos = tblDetalleFormaPagos;
	}

	@Column(name="tipo_pago", length = 3)
	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	@Column(name="codigo_contrato")
	public Integer getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	@Column(name="estado_operacion")
	public String getEstadoOperacion() {
		return estadoOperacion;
	}

	public void setEstadoOperacion(String estadoOperacion) {
		this.estadoOperacion = estadoOperacion;
	}

	@Column(name="numero_tienda")
	public String getNumeroTienda() {
		return numeroTienda;
	}

	public void setNumeroTienda(String numeroTienda) {
		this.numeroTienda = numeroTienda;
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

	@Column(name="codigo_nota")
	public Integer getCodigoNota() {
		return codigoNota;
	}

	public void setCodigoNota(Integer codigoNota) {
		this.codigoNota = codigoNota;
	}
	
	@Column(name="serie_nota", length = 8)
	public String getSerieNota() {
		return serieNota;
	}

	public void setSerieNota(String serieNota) {
		this.serieNota = serieNota;
	}

	@Column(name="numero_nota", length = 16)
	public String getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
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