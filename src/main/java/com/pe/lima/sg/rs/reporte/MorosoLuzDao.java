package com.pe.lima.sg.rs.reporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.reporte.MorosoLuzBean;

@Component
public class MorosoLuzDao {

	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	
	
	public List<MorosoLuzBean> getReporteMorosoLuz(String fechaFin){

		MorosoLuzBean luzXVencerBean = null;
		List<MorosoLuzBean> listaLuz 	= null;
		Connection con = null;
		//String url = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = null;
		try{
			url = urlTienda + userUrl + nombreUsuario + userPass + credencialUsuario;
			con = DriverManager.getConnection(url);
			query = "SELECT per.nombre || ' ' || per.paterno || ' '  || per.materno AS nombre_cliente, "+
					"	    per.razon_social, "+
					"       tie.numero, "+
					"       con.monto_alquiler, "+
					"       doc.fecha_fin, "+
					"       doc.saldo "+
					" FROM caj.tbl_cxc_documento doc, "+
					"	 cli.tbl_contrato con, "+
					"    mae.tbl_persona per, "+
					"    mae.tbl_tienda tie "+
					" WHERE con.codigo_contrato = doc.codigo_contrato "+
					"    AND doc.tipo_referencia = 'LUZ' "+
					"    AND con.codigo_tienda = tie.codigo_tienda "+
					"    AND con.codigo_persona = per.codigo_persona "+
					"    AND con.estado = '1' "+
					"    AND con.estado_contrato in ('PND', 'VGN') " + 
					"    AND con.fecha_fin <= ? :: date "+
					"    AND doc.saldo > 0 "+
					" ORDER BY nombre_cliente, tie.numero ,  doc.fecha_fin  ASC "; 

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, fechaFin);
			rs = pstmt.executeQuery();

			if (rs !=null){
				listaLuz = new ArrayList<MorosoLuzBean>();
				while(rs.next()){
					luzXVencerBean = new MorosoLuzBean();
					String nombre = rs.getString("nombre_cliente");
					if (nombre == null || nombre.trim().length() <= 0){
						nombre = rs.getString("razon_social");
					}
					luzXVencerBean.setNombreCliente(nombre);
					luzXVencerBean.setNumeroTienda(rs.getString("numero"));
					luzXVencerBean.setMontoLuz(rs.getBigDecimal("saldo"));
					luzXVencerBean.setFechaVencimiento(rs.getDate("fecha_fin"));
					listaLuz.add(luzXVencerBean);
				}
				
			}

		}catch(Exception e){
			e.printStackTrace();
			listaLuz = null;
		}finally{
			luzXVencerBean = null;
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
		return listaLuz;
	}
}
