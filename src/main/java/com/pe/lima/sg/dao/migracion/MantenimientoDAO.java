package com.pe.lima.sg.dao.migracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.pe.lima.sg.entity.mantenimiento.TblPersona;

@Component
public class MantenimientoDAO implements IMantenimientoDAO{
	private static final String URL =  "jdbc:ucanaccess://c:/Migracion/Tienda.mdb;memory=false";
	public Connection conn;
	
	public void abrirConexion(){
		try {
			conn = DriverManager.getConnection(URL,"","");
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public List<TblPersona> listarPersona(){
		List<TblPersona> listaPersona 	= null;
		TblPersona persona 				= null;
		String query 					= null;
		PreparedStatement pstmt 		= null;
		ResultSet rs 					= null;
		try {
			//final String fileName = "c:/Migracion/Tienda.mdb";
			 Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="+fileName;
			//conn = DriverManager.getConnection(URL,"","");
			 //conn = DriverManager.getConnection(url,"","");
			 conn = DriverManager.getConnection("jdbc:odbc:tienda");
			
			query = "SELECT                               "+
					"	cfg_persona.idpersona,            "+
					"	cfg_persona.tipo, 		          "+
					"	cfg_persona.paterno,              "+
					"	cfg_persona.materno,              "+
					"	cfg_persona.nombre,               "+
					"	cfg_persona.direccionempresa,     "+
					"	cfg_persona.direccioncasa,        "+
					"	cfg_persona.razonsocial,          "+
					"	cfg_persona.telefono,             "+
					"	cfg_persona.celular,              "+
					"	cfg_persona.email,                "+
					"	cfg_persona.docidentidad,         "+
					"	cfg_persona.ruc,                  "+
					"	cfg_persona.observacion,          "+
					"	cfg_persona.cargo,                "+
					"	cfg_persona.esproveedor, 	      "+
					"	cfg_persona.escliente		      "+
					"FROM cfg_persona;                    ";
			
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			if (rs !=null){
				listaPersona = new ArrayList<>();
				while(rs.next()){
					persona = new TblPersona();
					persona.setCodigoPersona(new Integer(rs.getString("idpersona")));
					String tipo = rs.getString("tipo");
					if (tipo.equals("2")){
						persona.setTipoPersona("J");
					}else{
						persona.setTipoPersona("N");
					}
					persona.setNombre(rs.getString("nombre"));
					persona.setPaterno(rs.getString("paterno"));
					persona.setMaterno(rs.getString("materno"));
					persona.setDireccionEmpresa(rs.getString("direccionempresa"));
					persona.setDireccionCasa(rs.getString("direccioncasa"));
					persona.setRazonSocial(rs.getString("razonsocial"));
					persona.setTelefono1(rs.getString("telefono"));
					persona.setCelular1(rs.getString("celular"));
					persona.setCorreoElectronico(rs.getString("email"));
					persona.setNumeroDni(rs.getString("docidentidad"));
					persona.setNumeroRuc(rs.getString("ruc"));
					persona.setObservacion(rs.getString("observacion"));
					String proveedor = rs.getString("esproveedor");
					if (proveedor.equals("1")){
						persona.setTipoProveedor("S");
					}else{
						persona.setTipoProveedor("N");
					}
					String cliente = rs.getString("escliente");
					if (cliente.equals("1")){
						persona.setTipoCliente("S");
					}else{
						persona.setTipoCliente("N");
					}
					listaPersona.add(persona);
				}
				
			}
			
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return listaPersona;
	}
	
}
