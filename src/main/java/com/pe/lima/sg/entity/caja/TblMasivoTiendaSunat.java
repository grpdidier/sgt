package com.pe.lima.sg.entity.caja;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import com.pe.lima.sg.presentacion.util.Constantes;

@Entity
@Table(name = "tbl_masivo_tienda_sunat", schema = "caj")
public class TblMasivoTiendaSunat implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private int codigoMasivoTienda;
	private String numeroTienda;
	private int codigoTienda;
	private int codigoContrato;
	private int codigoCxcDocumento;
	private String excluido;
	private BigDecimal monto;
	private String estado;	
	private Integer usuarioCreacion;	
	private Integer usuarioModificacion;
	private Date fechaCreacion;	
	private Date fechaModificacion;		
	private String ipCreacion;	
	private String ipModificacion;
	private int codigoComprobante;
	private TblMasivoSunat tblMasivoSunat;
	
	public TblMasivoTiendaSunat(int codigoMasivoTienda, String numeroTienda, int codigoContrato, int codigoCxcDocumento,
			String excluido, String estado, Integer usuarioCreacion, Integer usuarioModificacion, Date fechaCreacion,
			Date fechaModificacion, String ipCreacion, String ipModificacion, int codigoComprobante,
			TblMasivoSunat tblMasivoSunat) {
		super();
		this.codigoMasivoTienda = codigoMasivoTienda;
		this.numeroTienda = numeroTienda;
		this.codigoContrato = codigoContrato;
		this.codigoCxcDocumento = codigoCxcDocumento;
		this.excluido = excluido;
		this.estado = estado;
		this.usuarioCreacion = usuarioCreacion;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		this.ipCreacion = ipCreacion;
		this.ipModificacion = ipModificacion;
		this.codigoComprobante = codigoComprobante;
		this.tblMasivoSunat = tblMasivoSunat;
	}


	public TblMasivoTiendaSunat() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "codigo_masivo_tienda", unique = true, nullable = false)
	public int getCodigoMasivoTienda() {
		return codigoMasivoTienda;
	}


	public void setCodigoMasivoTienda(int codigoMasivoTienda) {
		this.codigoMasivoTienda = codigoMasivoTienda;
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

	

	@Column(name = "codigo_contrato")
	public int getCodigoContrato() {
		return codigoContrato;
	}


	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	@Column(name = "codigo_cxc_documento")
	public int getCodigoCxcDocumento() {
		return codigoCxcDocumento;
	}


	public void setCodigoCxcDocumento(int codigoCxcDocumento) {
		this.codigoCxcDocumento = codigoCxcDocumento;
	}

	@Column(name = "excluido", length = 1)
	public String getExcluido() {
		return excluido;
	}


	public void setExcluido(String excluido) {
		this.excluido = excluido;
	}

	@Column(name = "codigo_comprobante")
	public int getCodigoComprobante() {
		return codigoComprobante;
	}


	public void setCodigoComprobante(int codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="codigo_masivo")
	public TblMasivoSunat getTblMasivoSunat() {
		return tblMasivoSunat;
	}


	public void setTblMasivoSunat(TblMasivoSunat tblMasivoSunat) {
		this.tblMasivoSunat = tblMasivoSunat;
	}

	@Column(name = "codigo_tienda")
	public int getCodigoTienda() {
		return codigoTienda;
	}


	public void setCodigoTienda(int codigoTienda) {
		this.codigoTienda = codigoTienda;
	}


	@Column(name = "numero_tienda", length = 64)
	public String getNumeroTienda() {
		return numeroTienda;
	}


	public void setNumeroTienda(String numeroTienda) {
		this.numeroTienda = numeroTienda;
	}

	@Column(name = "monto")
	public BigDecimal getMonto() {
		return monto;
	}


	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
}
