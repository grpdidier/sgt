package com.pe.lima.sg.facturador.entity;
// Generated 17/12/2017 10:06:23 AM by Hibernate Tools 5.2.3.Final

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pe.lima.sg.facturador.bean.AuditoriaBean;
import com.pe.lima.sg.presentacion.util.Constantes;

/**
 * TblComprobante generated by hbm2java
 */
@Entity
@Table(name = "tbl_comprobante", schema = "ope")
public class TblComprobante implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoComprobante;
	private String tipoOperacion;
	private String codigoDomicilio;
	private String fechaEmision;
	private String fechaVencimiento;
	private String tipoComprobante;
	private String serie;
	private String numero;
	private String moneda;
	private String tipoDocumento;
	private String numeroDocumento;
	private String nombreCliente;
	private String direccionCliente;
	
	private BigDecimal descuentosGlobales 		= new BigDecimal("0");
	private BigDecimal totalOtrosCargos			= new BigDecimal("0");
	private BigDecimal totalDescuento			= new BigDecimal("0");
	
	private BigDecimal totalOpGravada			= new BigDecimal("0");
	private BigDecimal totalOpInafecta			= new BigDecimal("0");
	private BigDecimal totalOpExonerada			= new BigDecimal("0");
	private BigDecimal totalIgv					= new BigDecimal("0");
	private BigDecimal sumatoriaIsc				= new BigDecimal("0");
	private BigDecimal sumatorioaOtrosTributos	= new BigDecimal("0");
	private BigDecimal totalImporte				= new BigDecimal("0");
	
	private BigDecimal valorOpGratuitas			= new BigDecimal("0");
	private BigDecimal valorOpGratuita			= new BigDecimal("0");
	
	private Integer codigoInsumo;
	private String estado;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private Integer usuarioCreacion;
	private Integer usuarioModificacion;
	private String ipModificacion;
	private String ipCreacion;
	private Set<TblDetalleComprobante> tblDetalleComprobantes = new HashSet<TblDetalleComprobante>(0);
	private Set<TblSunatCabecera> tblSunatCabeceras = new HashSet<TblSunatCabecera>(0);
	private String codigoVerificacion;
	private String estadoOperacion				= "00";
	private String fechaGeneracion;
	private String fechaComunicado;
	private String descripcionBaja;
	//private Integer codigoEntidad;
	private String horaEmision;
	private BigDecimal sumTributo		= new BigDecimal("0");
	private BigDecimal totValorVenta	= new BigDecimal("0");
	private BigDecimal totPrecioVenta	= new BigDecimal("0");
	private BigDecimal totDescuento		= new BigDecimal("0");
	private BigDecimal sumOtrosCargos	= new BigDecimal("0");
	private BigDecimal totAnticipos		= new BigDecimal("0");
	private BigDecimal impTotalVenta	= new BigDecimal("0");
	private String versionUbl;
	private String customizacionDoc;
	private Set<TblTributoGeneral> tblTributoGenerals = new HashSet<>(0);
	private Set<TblBandejaFacturador> tblBandejaFacturadors = new HashSet<TblBandejaFacturador>(0);
	//private Set<TblLeyenda> tblLeyendas 		= new HashSet<TblLeyenda>(0);
	private Set<TblNota> tblNotas 				= new HashSet<TblNota>(0);
	//private TblEmpresa tblEmpresa;
	private Integer codigoEntidad;
	
	
	public TblComprobante() {
	}

	public TblComprobante(int codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}

	public TblComprobante(int codigoComprobante, String tipoOperacion, String codigoDomicilio, String fechaEmision,
			String fechaVencimiento, String tipoComprobante, String serie, String numero, String moneda,
			String tipoDocumento, String numeroDocumento, String nombreCliente, String direccionCliente,
			BigDecimal descuentosGlobales, BigDecimal totalDescuento, BigDecimal sumatoriaIsc,
			BigDecimal sumatorioaOtrosTributos, BigDecimal valorOpGratuitas, BigDecimal totalOpGravada,
			BigDecimal totalOpInafecta, BigDecimal totalOpExonerada, BigDecimal totalIgv, BigDecimal totalOtrosCargos,
			BigDecimal totalImporte, BigDecimal valorOpGratuita, Integer codigoInsumo, String estado,
			Date fechaCreacion, Date fechaModificacion, Integer usuarioCreacion, Integer usuarioModificacion,
			String ipModificacion, String ipCreacion, Set<TblDetalleComprobante>  tblDetalleComprobantes, Set<TblSunatCabecera> tblSunatCabeceras, String codigoVerificacion,
			String fechaGeneracion, String fechaComunicado, String descripcionBaja,
			String estadoOperacion, Set<TblBandejaFacturador> tblBandejaFacturadors, /*Set<TblLeyenda> tblLeyendas, */Set<TblNota> tblNotas, /*TblEmpresa tblEmpresa,*/
			Integer codigoEntidad,String versionUbl, String customizacionDoc,
			String horaEmision, BigDecimal sumTributo, BigDecimal totValorVenta, BigDecimal totPrecioVenta,
			BigDecimal totDescuento, BigDecimal sumOtrosCargos, BigDecimal totAnticipos, BigDecimal impTotalVenta,Set<TblTributoGeneral> tblTributoGenerals) {
		this.codigoComprobante = codigoComprobante;
		this.tipoOperacion = tipoOperacion;
		this.codigoDomicilio = codigoDomicilio;
		this.fechaEmision = fechaEmision;
		this.fechaVencimiento = fechaVencimiento;
		this.tipoComprobante = tipoComprobante;
		this.serie = serie;
		this.numero = numero;
		this.moneda = moneda;
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.nombreCliente = nombreCliente;
		this.direccionCliente = direccionCliente;
		this.descuentosGlobales = descuentosGlobales;
		this.totalDescuento = totalDescuento;
		this.sumatoriaIsc = sumatoriaIsc;
		this.sumatorioaOtrosTributos = sumatorioaOtrosTributos;
		this.valorOpGratuitas = valorOpGratuitas;
		this.totalOpGravada = totalOpGravada;
		this.totalOpInafecta = totalOpInafecta;
		this.totalOpExonerada = totalOpExonerada;
		this.totalIgv = totalIgv;
		this.totalOtrosCargos = totalOtrosCargos;
		this.totalImporte = totalImporte;
		this.valorOpGratuita = valorOpGratuita;
		this.codigoInsumo = codigoInsumo;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.ipModificacion = ipModificacion;
		this.ipCreacion = ipCreacion;
		this.tblDetalleComprobantes = tblDetalleComprobantes;
		this.tblSunatCabeceras = tblSunatCabeceras;
		this.codigoVerificacion = codigoVerificacion;
		this.estadoOperacion = estadoOperacion;
		this.fechaGeneracion = fechaGeneracion;
		this.fechaComunicado = fechaComunicado;
		this.descripcionBaja = descripcionBaja;
		//this.codigoEntidad = codigoEntidad;
		this.horaEmision = horaEmision;
		this.sumTributo = sumTributo;
		this.totValorVenta = totValorVenta;
		this.totPrecioVenta = totPrecioVenta;
		this.totDescuento = totDescuento;
		this.sumOtrosCargos = sumOtrosCargos;
		this.totAnticipos = totAnticipos;
		this.impTotalVenta = impTotalVenta;
		this.versionUbl = versionUbl;
		this.customizacionDoc = customizacionDoc;
		this.tblTributoGenerals = tblTributoGenerals;
		this.tblBandejaFacturadors = tblBandejaFacturadors;
		//this.tblLeyendas = tblLeyendas;
		this.tblNotas = tblNotas;
		//this.tblEmpresa = tblEmpresa;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_comprobante", unique = true, nullable = false)
	public int getCodigoComprobante() {
		return this.codigoComprobante;
	}

	public void setCodigoComprobante(int codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}

	@Column(name = "tipo_operacion", length = 8)
	public String getTipoOperacion() {
		return this.tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	@Column(name = "codigo_domicilio", length = 8)
	public String getCodigoDomicilio() {
		return this.codigoDomicilio;
	}

	public void setCodigoDomicilio(String codigoDomicilio) {
		this.codigoDomicilio = codigoDomicilio;
	}

	@Column(name = "fecha_emision", length = 10)
	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	@Column(name = "fecha_vencimiento", length = 10)
	public String getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	@Column(name = "tipo_comprobante", length = 8)
	public String getTipoComprobante() {
		return this.tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	@Column(name = "serie", length = 8)
	public String getSerie() {
		return this.serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	@Column(name = "numero", length = 16)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "moneda", length = 8)
	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	@Column(name = "tipo_documento", length = 8)
	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Column(name = "numero_documento", length = 32)
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@Column(name = "nombre_cliente", length = 1500)
	public String getNombreCliente() {
		return this.nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	@Column(name = "direccion_cliente", length = 256)
	public String getDireccionCliente() {
		return this.direccionCliente;
	}

	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}

	@Column(name = "descuentos_globales", precision = 12)
	public BigDecimal getDescuentosGlobales() {
		return this.descuentosGlobales;
	}

	public void setDescuentosGlobales(BigDecimal descuentosGlobales) {
		this.descuentosGlobales = descuentosGlobales;
	}

	@Column(name = "total_descuento", precision = 12)
	public BigDecimal getTotalDescuento() {
		return this.totalDescuento;
	}

	public void setTotalDescuento(BigDecimal totalDescuento) {
		this.totalDescuento = totalDescuento;
	}

	@Column(name = "sumatoria_isc", precision = 12, scale = 2)
	public BigDecimal getSumatoriaIsc() {
		return this.sumatoriaIsc;
	}

	public void setSumatoriaIsc(BigDecimal sumatoriaIsc) {
		this.sumatoriaIsc = sumatoriaIsc;
	}

	@Column(name = "sumatorioa_otros_tributos", precision = 12)
	public BigDecimal getSumatorioaOtrosTributos() {
		return this.sumatorioaOtrosTributos;
	}

	public void setSumatorioaOtrosTributos(BigDecimal sumatorioaOtrosTributos) {
		this.sumatorioaOtrosTributos = sumatorioaOtrosTributos;
	}

	@Column(name = "valor_op_gratuitas", precision = 12)
	public BigDecimal getValorOpGratuitas() {
		return this.valorOpGratuitas;
	}

	public void setValorOpGratuitas(BigDecimal valorOpGratuitas) {
		this.valorOpGratuitas = valorOpGratuitas;
	}

	@Column(name = "total_op_gravada", precision = 12)
	public BigDecimal getTotalOpGravada() {
		return this.totalOpGravada;
	}

	public void setTotalOpGravada(BigDecimal totalOpGravada) {
		this.totalOpGravada = totalOpGravada;
	}

	@Column(name = "total_op_inafecta", precision = 12)
	public BigDecimal getTotalOpInafecta() {
		return this.totalOpInafecta;
	}

	public void setTotalOpInafecta(BigDecimal totalOpInafecta) {
		this.totalOpInafecta = totalOpInafecta;
	}

	@Column(name = "total_op_exonerada", precision = 12)
	public BigDecimal getTotalOpExonerada() {
		return this.totalOpExonerada;
	}

	public void setTotalOpExonerada(BigDecimal totalOpExonerada) {
		this.totalOpExonerada = totalOpExonerada;
	}

	@Column(name = "total_igv", precision = 12)
	public BigDecimal getTotalIgv() {
		return this.totalIgv;
	}

	public void setTotalIgv(BigDecimal totalIgv) {
		this.totalIgv = totalIgv;
	}

	@Column(name = "total_otros_cargos", precision = 12)
	public BigDecimal getTotalOtrosCargos() {
		return this.totalOtrosCargos;
	}

	public void setTotalOtrosCargos(BigDecimal totalOtrosCargos) {
		this.totalOtrosCargos = totalOtrosCargos;
	}

	@Column(name = "total_importe", precision = 12)
	public BigDecimal getTotalImporte() {
		return this.totalImporte;
	}

	public void setTotalImporte(BigDecimal totalImporte) {
		this.totalImporte = totalImporte;
	}

	@Column(name = "valor_op_gratuita", precision = 12)
	public BigDecimal getValorOpGratuita() {
		return this.valorOpGratuita;
	}

	public void setValorOpGratuita(BigDecimal valorOpGratuita) {
		this.valorOpGratuita = valorOpGratuita;
	}

	@Column(name = "codigo_insumo")
	public Integer getCodigoInsumo() {
		return this.codigoInsumo;
	}

	public void setCodigoInsumo(Integer codigoInsumo) {
		this.codigoInsumo = codigoInsumo;
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

	@Column(name = "ip_modificacion", length = 64)
	public String getIpModificacion() {
		return this.ipModificacion;
	}

	public void setIpModificacion(String ipModificacion) {
		this.ipModificacion = ipModificacion;
	}

	@Column(name = "ip_creacion", length = 64)
	public String getIpCreacion() {
		return this.ipCreacion;
	}

	public void setIpCreacion(String ipCreacion) {
		this.ipCreacion = ipCreacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblComprobante")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblDetalleComprobante>  getTblDetalleComprobantes() {
		return this.tblDetalleComprobantes;
	}

	public void setTblDetalleComprobantes(Set<TblDetalleComprobante>  tblDetalleComprobantes) {
		this.tblDetalleComprobantes = tblDetalleComprobantes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblComprobante")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblSunatCabecera> getTblSunatCabeceras() {
		return this.tblSunatCabeceras;
	}

	public void setTblSunatCabeceras(Set<TblSunatCabecera> tblSunatCabeceras) {
		this.tblSunatCabeceras = tblSunatCabeceras;
	}

	@Column(name = "codigo_verificacion", length = 64)
	public String getCodigoVerificacion() {
		return this.codigoVerificacion;
	}

	public void setCodigoVerificacion(String codigoVerificacion) {
		this.codigoVerificacion = codigoVerificacion;
	}

	@Column(name = "estado_operacion", length = 8)
	public String getEstadoOperacion() {
		return this.estadoOperacion;
	}

	public void setEstadoOperacion(String estadoOperacion) {
		this.estadoOperacion = estadoOperacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblComprobante")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblBandejaFacturador> getTblBandejaFacturadors() {
		return this.tblBandejaFacturadors;
	}

	public void setTblBandejaFacturadors(Set<TblBandejaFacturador> tblBandejaFacturadors) {
		this.tblBandejaFacturadors = tblBandejaFacturadors;
	}

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblComprobante")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblLeyenda> getTblLeyendas() {
		return this.tblLeyendas;
	}

	public void setTblLeyendas(Set<TblLeyenda> tblLeyendas) {
		this.tblLeyendas = tblLeyendas;
	}*/
	
	@Column(name = "fecha_generacion", length = 10)
	public String getFechaGeneracion() {
		return this.fechaGeneracion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	@Column(name = "fecha_comunicado", length = 10)
	public String getFechaComunicado() {
		return this.fechaComunicado;
	}

	public void setFechaComunicado(String fechaComunicado) {
		this.fechaComunicado = fechaComunicado;
	}

	@Column(name = "descripcion_baja", length = 100)
	public String getDescripcionBaja() {
		return this.descripcionBaja;
	}

	public void setDescripcionBaja(String descripcionBaja) {
		this.descripcionBaja = descripcionBaja;
	}
	

	/*@Column(name = "codigo_entidad")
	public Integer getCodigoEntidad() {
		return this.codigoEntidad;
	}

	public void setCodigoEntidad(Integer codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}*/

	@Column(name = "hora_emision", length = 14)
	public String getHoraEmision() {
		return this.horaEmision;
	}

	public void setHoraEmision(String horaEmision) {
		this.horaEmision = horaEmision;
	}

	@Column(name = "sum_tributo", precision = 15)
	public BigDecimal getSumTributo() {
		return this.sumTributo;
	}

	public void setSumTributo(BigDecimal sumTributo) {
		this.sumTributo = sumTributo;
	}

	@Column(name = "tot_valor_venta", precision = 15)
	public BigDecimal getTotValorVenta() {
		return this.totValorVenta;
	}

	public void setTotValorVenta(BigDecimal totValorVenta) {
		this.totValorVenta = totValorVenta;
	}

	@Column(name = "tot_precio_venta", precision = 15)
	public BigDecimal getTotPrecioVenta() {
		return this.totPrecioVenta;
	}

	public void setTotPrecioVenta(BigDecimal totPrecioVenta) {
		this.totPrecioVenta = totPrecioVenta;
	}

	@Column(name = "tot_descuento", precision = 15)
	public BigDecimal getTotDescuento() {
		return this.totDescuento;
	}

	public void setTotDescuento(BigDecimal totDescuento) {
		this.totDescuento = totDescuento;
	}

	@Column(name = "sum_otros_cargos", precision = 15)
	public BigDecimal getSumOtrosCargos() {
		return this.sumOtrosCargos;
	}

	public void setSumOtrosCargos(BigDecimal sumOtrosCargos) {
		this.sumOtrosCargos = sumOtrosCargos;
	}

	@Column(name = "tot_anticipos", precision = 15)
	public BigDecimal getTotAnticipos() {
		return this.totAnticipos;
	}

	public void setTotAnticipos(BigDecimal totAnticipos) {
		this.totAnticipos = totAnticipos;
	}

	@Column(name = "imp_total_venta", precision = 15)
	public BigDecimal getImpTotalVenta() {
		return this.impTotalVenta;
	}

	public void setImpTotalVenta(BigDecimal impTotalVenta) {
		this.impTotalVenta = impTotalVenta;
	}

	@Column(name = "version_ubl", length = 8)
	public String getVersionUbl() {
		return this.versionUbl;
	}

	public void setVersionUbl(String versionUbl) {
		this.versionUbl = versionUbl;
	}

	@Column(name = "customizacion_doc", length = 8)
	public String getCustomizacionDoc() {
		return this.customizacionDoc;
	}

	public void setCustomizacionDoc(String customizacionDoc) {
		this.customizacionDoc = customizacionDoc;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblComprobante")
	public Set<TblTributoGeneral> getTblTributoGenerals() {
		return this.tblTributoGenerals;
	}

	public void setTblTributoGenerals(Set<TblTributoGeneral> tblTributoGenerals) {
		this.tblTributoGenerals = tblTributoGenerals;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblComprobante")
	@JsonManagedReference(value="tablaManage-TablaBack")
	public Set<TblNota> getTblNotas() {
		return this.tblNotas;
	}

	public void setTblNotas(Set<TblNota> tblNotas) {
		this.tblNotas = tblNotas;
	}
	
	/*
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codigo_entidad", nullable = false)
	@JsonBackReference(value="tablaManage-TablaBack")
	public TblEmpresa getTblEmpresa() {
		return this.tblEmpresa;
	}

	public void setTblEmpresa(TblEmpresa tblEmpresa) {
		this.tblEmpresa = tblEmpresa;
	}
	*/
	public void setAuditoriaCreacion(AuditoriaBean auditoriaBean){
		try{
			this.setFechaCreacion(new Date(System.currentTimeMillis()));
			this.setIpCreacion(auditoriaBean.getIpCreacion());
			this.setEstado(Constantes.ESTADO_REGISTRO_ACTIVO);
			this.setUsuarioCreacion(auditoriaBean.getCodigoUsuario());
		}catch(Exception e){
			e.printStackTrace();
			this.setUsuarioCreacion(0);
		}
	}
	
	public void setAuditoriaModificacion(AuditoriaBean auditoriaBean){
		try{
			this.setFechaModificacion(new Date(System.currentTimeMillis()));
			this.setIpModificacion(auditoriaBean.getIpCreacion());
			
			this.setUsuarioModificacion(auditoriaBean.getCodigoUsuario());
		}catch(Exception e){
			e.printStackTrace();
			this.setUsuarioModificacion(0);
		}
	}

	public Integer getCodigoEntidad() {
		return codigoEntidad;
	}

	public void setCodigoEntidad(Integer codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}
}
