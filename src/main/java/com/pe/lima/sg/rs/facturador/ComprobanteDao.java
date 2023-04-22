package com.pe.lima.sg.rs.facturador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.facturador.DocumentoBean;
import com.pe.lima.sg.presentacion.util.Constantes;

@Component
public class ComprobanteDao {

	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	

	public List<DocumentoBean> getDocumentoSinComprobante(){

		DocumentoBean documentoBean 		= null;
		List<DocumentoBean> listaDocumento 	= null;
		Connection con = null;
		//String url = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = null;
		try{
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			
			con = DriverManager.getConnection(url);
			query = "SELECT tie.numero, per.nombre || ' ' || per.paterno || ' '  || per.materno AS nombre_cliente, "+
					"	    per.razon_social, "+
					"       doc.mes, "+
					"       doc.anio, "+
					"       doc.monto_cobrado, "+
					"       doc.nombre, "+
					"       doc.fecha_modificacion, "+
					"       tie.codigo_edificio, "+
					"       per.tipo_persona, "+
					"       per.numero_dni, "+
					"       per.numero_ruc, "+
					"       doc.codigo_cxc_doc, "+
					"       doc.fecha_fin "+
					" FROM  "+
					"	 caj.tbl_cxc_documento doc, "+
					"    cli.tbl_contrato con, "+
					"    mae.tbl_tienda tie, "+
					"    mae.tbl_persona per "+
					" WHERE  "+
					"    doc.saldo <= 0 "+
					"    and doc.numero_comprobante is NULL "+
					"    and doc.tipo_documento = 'FAC' "+
					"    and doc.codigo_contrato = con.codigo_contrato " + 
					"    and con.codigo_tienda = tie.codigo_tienda "+
					"    and con.codigo_persona = per.codigo_persona "+
					"    and doc.monto_cobrado > 0 "+
					" order by tie.numero, doc.fecha_fin  ASC "; 

			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();

			if (rs !=null){
				listaDocumento = new ArrayList<>();
				while(rs.next()){
					documentoBean = new DocumentoBean();
					String nombre = rs.getString("nombre_cliente");
					if (nombre == null || nombre.trim().length() <= 0){
						nombre = rs.getString("razon_social");
					}
					documentoBean.setNombreCliente(nombre);
					documentoBean.setNumeroTienda(rs.getString("numero"));
					documentoBean.setMes(rs.getString("mes"));
					documentoBean.setMontoCobrado(rs.getBigDecimal("monto_cobrado"));
					documentoBean.setAnio(rs.getString("anio"));
					documentoBean.setDescripcion(rs.getString("nombre"));
					documentoBean.setFechaFin(rs.getDate("fecha_fin"));
					documentoBean.setFechaModificacion(rs.getDate("fecha_modificacion"));
					documentoBean.setTipoPersona(rs.getString("tipo_persona"));
					if (documentoBean.getTipoPersona().equals(Constantes.PERSONA_NATURAL)){
						documentoBean.setNumeroDocumento(rs.getString("numero_dni"));
					}else{
						documentoBean.setNumeroDocumento(rs.getString("numero_ruc"));
					}
					documentoBean.setCodigoCxC(rs.getInt("codigo_cxc_doc"));
					listaDocumento.add(documentoBean);
				}
				
			}

		}catch(Exception e){
			e.printStackTrace();
			listaDocumento = null;
		}finally{
			documentoBean = null;
			rs = null;
			pstmt = null;
			query = null;
			query = null;
			try{
				if (con !=null && !con.isClosed()){
					con.close();
					con = null;
				}
			}catch(Exception e){
				e.printStackTrace();
				con = null;
			}
		}
		return listaDocumento;
	}
}
