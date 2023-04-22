package com.pe.lima.sg.rs.reporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pe.lima.sg.bean.reporte.MorososAlquilerBean;

@Component
public class MorosoAlquilerDao {

	@Value("${spring.datasource.url}")
	private String urlTienda;
	
	@Value("${spring.datasource.username}")
	private String nombreUsuario;
	
	@Value("${spring.datasource.password}")
	private String credencialUsuario;
	
	private final String userUrl	= "?user=";
	private final String userPass	= "&password=";
	
	private String url;
	

	public List<MorososAlquilerBean> getReporteMorosoAlquiler(String fechaFin){

		MorososAlquilerBean morososAlquilerBean = null;
		List<MorososAlquilerBean> listaMorosos 	= null;
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
					"       cxc.saldo, "+
					"       cxc.fecha_fin, "+
					"       cxc.tipo_moneda "+
					" FROM caj.tbl_cxc_documento cxc, "+
					"	 cli.tbl_contrato con, "+
					"    mae.tbl_persona per, "+
					"    mae.tbl_tienda tie "+
					" WHERE cxc.codigo_contrato = con.codigo_contrato "+
					"    AND con.codigo_tienda = tie.codigo_tienda "+
					"    AND con.codigo_persona = per.codigo_persona "+
					"    AND con.estado = '1' "+
					"    AND con.estado_contrato in ('PND', 'VGN') " + 
					"    AND cxc.tipo_referencia = 'ALQ' "+
					"    AND cxc.saldo > 0 "+
					"    AND cxc.fecha_fin <= ? :: date "+
					" ORDER BY nombre_cliente, tie.numero ,  cxc.fecha_fin  ASC "; 

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, fechaFin);
			rs = pstmt.executeQuery();

			if (rs !=null){
				listaMorosos = new ArrayList<MorososAlquilerBean>();
				while(rs.next()){
					morososAlquilerBean = new MorososAlquilerBean();
					String nombre = rs.getString("nombre_cliente");
					if (nombre == null || nombre.trim().length() <= 0){
						nombre = rs.getString("razon_social");
					}
					morososAlquilerBean.setNombreCliente(nombre);
					morososAlquilerBean.setNumeroTienda(rs.getString("numero"));
					morososAlquilerBean.setTipoMoneda(rs.getString("tipo_moneda"));
					morososAlquilerBean.setMontoDolares(rs.getBigDecimal("saldo"));
					morososAlquilerBean.setFechaVencimiento(rs.getDate("fecha_fin"));
					
					listaMorosos.add(morososAlquilerBean);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			listaMorosos = null;
		}finally{
			morososAlquilerBean = null;
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
		return listaMorosos;
	}
}
