package com.pe.lima.sg;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import com.pe.lima.sg.bean.seguridad.AccesoDirectoBean;
import com.pe.lima.sg.bean.seguridad.GlobalPermisoBean;
import com.pe.lima.sg.dao.mantenimiento.IConceptoDAO;
import com.pe.lima.sg.dao.mantenimiento.IEdificioDAO;
import com.pe.lima.sg.dao.mantenimiento.IParametroDAO;
import com.pe.lima.sg.dao.mantenimiento.ISuministroDAO;
import com.pe.lima.sg.dao.mantenimiento.ITipoServicioDAO;
import com.pe.lima.sg.dao.seguridad.IOpcionDAO;
import com.pe.lima.sg.dao.seguridad.IUsuarioDAO;
import com.pe.lima.sg.entity.mantenimiento.TblConcepto;
import com.pe.lima.sg.entity.mantenimiento.TblEdificio;
import com.pe.lima.sg.entity.mantenimiento.TblParametro;
import com.pe.lima.sg.entity.mantenimiento.TblSuministro;
import com.pe.lima.sg.entity.mantenimiento.TblTipoServicio;
import com.pe.lima.sg.entity.seguridad.TblOpcion;
import com.pe.lima.sg.entity.seguridad.TblUsuario;
import com.pe.lima.sg.entity.seguridad.TblUsuarioPermiso;
import com.pe.lima.sg.facturador.bean.FiltroPdf;
import com.pe.lima.sg.facturador.bean.ParametroFacturadorBean;
import com.pe.lima.sg.facturador.db.util.IOperacionFacturador;
import com.pe.lima.sg.facturador.entity.TblComprobante;
import com.pe.lima.sg.facturador.entity.TblDetalleComprobante;
import com.pe.lima.sg.presentacion.util.Constantes;
import com.pe.lima.sg.presentacion.util.ListaUtilAction;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

	@Autowired
	AuthenticationSuccessHandler successHandler;
	
	@Autowired
	private IEdificioDAO edificioDao;
	
	@Autowired
	private IConceptoDAO conceptoDao;
	
	@Autowired
	private ITipoServicioDAO tipoServicioDao;
	
	@Autowired
	private ISuministroDAO suministroDao;
	
	@Autowired
	IParametroDAO parametroDao;
	
	@Autowired
	private IUsuarioDAO usuarioDao;
	
	
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
    	log.debug("[BCryptPasswordEncoder] Inicio");
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	log.debug("[configure] Inicio");
        /*http
                .authorizeRequests()
                    .antMatchers("/resources/**", "/registration").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").usernameParameter("login").passwordParameter("clave")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();*/
    	http.csrf().disable()
		// Permite iframe del mismo origen
		.headers().frameOptions().disable().and()
		// Permite el acceso a css
		.authorizeRequests().antMatchers("/css/**").permitAll()
		// Permite el acceso a images
		.antMatchers("/images/**").permitAll()
		// Permite el acceso a js
		.antMatchers("/js/**").permitAll()
		// Permite el acceso a java
		.antMatchers("/java/**").permitAll()
		.antMatchers("/jasperreport/**").permitAll()
		// Permite el acceso a la página de NoUser
		.antMatchers("/webjars/**").permitAll()
		// Permite el acceso a la página de restablecer contraseña
		.antMatchers("/restablecer/**").permitAll()
		// Cualquier solicitud debe ser autenticada
		.anyRequest().authenticated().and()
		// La página de login debe ser pública
		.formLogin().loginPage("/login").usernameParameter("login").passwordParameter("clave").successHandler(successHandler).permitAll().and()
		// El recurso de logout debe ser público
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/salir")).logoutSuccessUrl("/login");
		//.logout().permitAll();
    }

    @Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
    	log.debug("[authenticationSuccessHandler] return");
		return new AuthenticationSuccessHandler() {
			
			RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
			
			@Autowired
			IUsuarioDAO usuR;
			

			@Autowired
			private IOpcionDAO opcionDao;
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				log.debug("[onAuthenticationSuccess] Inicio");
				FiltroPdf filtro = null;
				IOperacionFacturador operacionFacturador		= null;
				Map<String, TblParametro> mapParametro			= null;
				List<ParametroFacturadorBean> listaParametro 	= null;
				List<TblParametro> listaParametroSistema		= null;
				if (authentication.isAuthenticated()) {
					String login = authentication.getName();
					TblUsuario u = usuR.findOneByLogin(login);
					log.debug("[onAuthenticationSuccess] usuario:"+u);
					if (u == null || u.getEstadoUsuario().equals(Constantes.ESTADO_INACTIVO)) {
						redirectStrategy.sendRedirect(request, response, "/login");
						
						new SecurityContextLogoutHandler().logout(request, response, authentication);
						SecurityContextHolder.getContext().setAuthentication(null);
						
						return;
					}else{
						request.getSession().setAttribute("UsuarioSession", u);
						/*Permisos Globales*/
						GlobalPermisoBean globalPermisoBean = this.validarPermisoGlobal(u);
						request.getSession().setAttribute("SessionPermiso", globalPermisoBean);
						request.getSession().setAttribute("id_usuario", u.getCodigoUsuario());
						log.debug("[onAuthenticationSuccess] id_usuario:"+u.getCodigoUsuario());
						/*Cargamos las variables de session*/
						request.getSession().setAttribute("SessionMapEstadoUsuario", ListaUtilAction.obtenerValoresEstadoUsuario2());
						request.getSession().setAttribute("SessionIngresoSalida", ListaUtilAction.obtenerValoresIngresoSalida());
						request.getSession().setAttribute("SessionEstadoCajaChica", ListaUtilAction.obtenerValoresEstadoCajaChica());
						request.getSession().setAttribute("SessionMapAnio", ListaUtilAction.obtenerAnios());
						request.getSession().setAttribute("SessionMapMeses", ListaUtilAction.obtenerValoresMesesSession());
						request.getSession().setAttribute("SessionMapTipoMoneda", ListaUtilAction.obtenerValoresTipoMoneda());
						log.debug("[onAuthenticationSuccess] SessionMapEstadoUsuario");
						/*Lista de Edificaciones: Inmuebles*/
						request.getSession().setAttribute("SessionMapEdificacion", obtenerValoresEdificacio());
						/*Lista de Concepto x Tipo: Ingreso y Gasto*/
						request.getSession().setAttribute("SessionMapConceptoIngreso", obtenerValoresConcepto(Constantes.CAJA_CHICA_INGRESO));
						request.getSession().setAttribute("SessionMapConceptoGasto", obtenerValoresConcepto(Constantes.CAJA_CHICA_GASTO));
						request.getSession().setAttribute("SessionMapConceptoAll", obtenerValoresAllConcepto());
						request.getSession().setAttribute("SessionMapConceptoAllMap", obtenerValoresAllConceptoMap());
						request.getSession().setAttribute("SessionMapUsuarioAllMap", obtenerAllUsuarioMap());
						request.getSession().setAttribute("SessionMapTipoCobro", ListaUtilAction.obtenerTipoCobro());
						/*Lista de Meses para el reporte de Moroso*/
						request.getSession().setAttribute("SessionMapMesesMoroso", ListaUtilAction.obtenerMesesMoroso());
						/*Lista de Servicios*/
						request.getSession().setAttribute("SessionMapServicio", obtenerValoresServicio());
						/*Lista de Servicios Aumentados*/
						request.getSession().setAttribute("SessionMapServicioAumentado", obtenerValoresServicioCompleto());
						/*Lista de Suministros*/
						request.getSession().setAttribute("SessionMapSuministro", obtenerValoresSuministro());
						/*Lista de tipo de Concepto: Ingreso - Egreso*/
						request.getSession().setAttribute("SessionMapTipoConcepto",ListaUtilAction.obtenerTipoIngresoEgreso());
						/*Lista de tipo de Pago*/
						request.getSession().setAttribute("SessionMapTipoPago",ListaUtilAction.obtenerTipoPago());
						/*Lista de tipo de Bancarizado*/
						request.getSession().setAttribute("SessionMapTipoBancarizado",ListaUtilAction.obtenerTipoBancarizado());
						/*Cargamos el menu asociado al usuario*/
						List<TblOpcion> listaOpciones 	= null;
						//Cargando los parametros del Facturador
						//Cargando los parametros del sistema
						listaParametroSistema = parametroDao.listarAllActivos();
						if (listaParametroSistema!=null){
							mapParametro = new HashMap<String, TblParametro>();
							for(TblParametro parametro:listaParametroSistema){
								mapParametro.put(parametro.getNombre(), parametro);
								//ruc de la empresa
								/*if (parametro.getNombre().equals(Constantes.PARAMETRO_RUC_EMPRESA)){
									Constantes.SUNAT_RUC_EMISOR = parametro.getDato();
									System.out.println("Constantes.SUNAT_RUC_EMISOR: "+Constantes.SUNAT_RUC_EMISOR);
								}*/
							}
						}
						/*Se comenta porque no esta implementado el facturador*/
						/*try{
							filtro = new FiltroPdf();
							this.asignarParametros(filtro, mapParametro);
							//Asignar Parametros en session
							request.getSession().setAttribute("SessionMapParametros", mapParametro);
							operacionFacturador = new OperacionFacturadorImp();
							listaParametro = operacionFacturador.consultarParametro(Constantes.SUNAT_PARAMETRO_SISTEMA, filtro);
							request.getSession().setAttribute("SessionListParametro",listaParametro);
						}catch(Exception e){
							request.getSession().setAttribute("SessionListParametro",listaParametro);
						}*/
						String strCadena = "";
						if (u.getTblPerfil() != null){
							listaOpciones = opcionDao.listarOpcionesPerfil(u.getTblPerfil().getCodigoPerfil());
							AccesoDirectoBean accesoDirectoBean = new AccesoDirectoBean();
							strCadena = this.generarArbol(listaOpciones, accesoDirectoBean);
							request.getSession().setAttribute("SessionAccesoDirecto",accesoDirectoBean);
						}
						request.getSession().setAttribute("SessionMenu", strCadena);
					}
				}
				log.debug("[onAuthenticationSuccess] Fin");
				
				redirectStrategy.sendRedirect(request, response, "/");
			}
			
			/*
			 * Revisión de Permisos y asignación
			 */
			private GlobalPermisoBean validarPermisoGlobal(TblUsuario tblUsuario){
				GlobalPermisoBean globalPermisoBean = new GlobalPermisoBean();
				if (tblUsuario.getTblUsuarioPermisos() != null && tblUsuario.getTblUsuarioPermisos().size()>0){
					for (TblUsuarioPermiso tblUsuarioPermiso : tblUsuario.getTblUsuarioPermisos()){
						if (tblUsuarioPermiso.getTblPermiso().getCode().equals(Constantes.MODULO_COBRO_FECHA_COBRO)){
							globalPermisoBean.setModCobFecCob(Constantes.ESTADO_ACTIVO);
						}
						if (tblUsuarioPermiso.getTblPermiso().getCode().equals(Constantes.MODULO_INGRESO_FECHA_INGRESO)){
							globalPermisoBean.setModIngFecIng(Constantes.ESTADO_ACTIVO);
						}
						if (tblUsuarioPermiso.getTblPermiso().getCode().equals(Constantes.MODULO_COBRO_ELIMINAR_COBRO)){
							globalPermisoBean.setModCobEliCob(Constantes.ESTADO_ACTIVO);
						}
						if (tblUsuarioPermiso.getTblPermiso().getCode().equals(Constantes.MODULO_INGRESO_BOTON_ELIMINAR)){
							globalPermisoBean.setModIngBotEli(Constantes.ESTADO_ACTIVO);
						}
						
					}
				}
				return globalPermisoBean;
			}
		    /**
			 * Generamos el arbol del menu
			 */
			private String generarArbol(List<TblOpcion> listaOpciones, AccesoDirectoBean accesoDirectoBean){
				String strResultado	= null;
				log.debug("[generarArbol] Inicio ");
				try{
					strResultado = Constantes.MENU_CABECERA_INI_PRINCIPAL;
					strResultado = strResultado + this.getOpcionesRecursivo(1000, listaOpciones, accesoDirectoBean);
					strResultado = strResultado + Constantes.MENU_CABECERA_FIN;
				}catch(Exception e){
					
				}
				log.debug("[generarArbol] Fin - strResultado: "+strResultado);
				return strResultado;
			}
			public void asignarParametros(FiltroPdf entidad, Map<String, TblParametro> mapParametro){
				TblParametro parametro =null;
				try{
					entidad.setComprobante(new TblComprobante());
					entidad.setDetalleComprobante(new TblDetalleComprobante());
					if (mapParametro!=null){
						
						//Ruta del repositorio Data
						parametro = mapParametro.get(Constantes.PARAMETRO_SUNAT_DATA);
						if (parametro!=null){
							entidad.setSunatData(parametro.getDato());
						}else{
							entidad.setSunatData(Constantes.SUNAT_FACTURADOR_RUTA_PRUEBA);
						}
						//Ruta de la Base de Datos
						parametro = mapParametro.get(Constantes.PARAMETRO_SUNAT_BD);
						if (parametro!=null){
							entidad.setSunatBD(parametro.getDato());
						}else{
							entidad.setSunatBD(Constantes.SUNAT_FACTURADOR_DB_LOCAL);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			private String getOpcionesRecursivo(Integer intModulo, List<TblOpcion> listaOpciones, AccesoDirectoBean accesoDirectoBean){
				String strResultado = "";
				log.debug("[getOpcionesRecursivo] Inicio - Modulo: "+intModulo);
				for(TblOpcion opcion: listaOpciones){
					if (opcion.getModulo().compareTo(intModulo)==0){
						//Se valida si es nodo u hoja
						if (opcion.getRuta()==null || opcion.getRuta().equals("")){
							//Si es nodo
							//strResultado = strResultado + "<li id=\"child_node_"+opcion.getCodigoOpcion()+"\"> "+opcion.getNombre()+"</a> ";
							strResultado = strResultado + "<li>"+ opcion.getNombre();
							String strTemporal = getOpcionesRecursivo(opcion.getCodigoOpcion(),listaOpciones, accesoDirectoBean);
							if (strTemporal.length()>0){
								strResultado = strResultado +"	<ul>" + strTemporal + "</ul>";
							}
							//strResultado = strResultado + getOpcionesRecursivo(opcion.getCodigoOpcion(),listaOpciones);
							strResultado = strResultado + "</li>";
							
						}else{
							//Si es hoja
							strResultado = strResultado + "<li id=\"child_node_"+opcion.getCodigoOpcion()+"\"> <a href=\"#\" onclick=\"jsOpcionMenu('"+opcion.getRuta()+"');\">"+opcion.getNombre()+"</a> </li>";
							log.debug("[getOpcionesRecursivo] opcion.getNombre(): "+opcion.getNombre());
							//strResultado = strResultado + "<li>"+ opcion.getNombre() + " <img src=\"/images/iconos/hoja.png\" alt=\"Hoja\" width=\"20px\"/> </li>";
							this.validaAccesoDirecto(accesoDirectoBean, opcion.getNombre());
						}
					}
				}
				log.debug("[getOpcionesRecursivo] Fin - resultado: "+strResultado);
				return strResultado;
			}
			/*Verificamos si tiene configurado el acceso directo*/
			public void validaAccesoDirecto(AccesoDirectoBean accesoDirectoBean, String nombre){
				if (nombre.equals("Locales")){
					accesoDirectoBean.setMostrarContrato(Constantes.ESTADO_ACTIVO);
				}
				if (nombre.equals("Cobro")){
					accesoDirectoBean.setMostrarCobro(Constantes.ESTADO_ACTIVO);
				}
				if (nombre.equals("Ingresos y Egresos")){
					accesoDirectoBean.setMostrarReporteIE(Constantes.ESTADO_ACTIVO);
				}
				
			}
			
		};
	}
    
    
    /*@Bean
    public JasperReportsViewResolver getJasperReportsViewResolver() {
        JasperReportsViewResolver resolver = new JasperReportsViewResolver();
        resolver.setPrefix("classpath:/jasperreports/");
        resolver.setSuffix(".jasper");
        resolver.setReportDataKey("datasource");
        resolver.setViewNames("*_report");
        resolver.setViewClass(JasperReportsMultiFormatView.class);
        resolver.setOrder(1);
        return resolver;
    }*/
    @Bean
    public ViewResolver beanNameViewResolver() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        return resolver;
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	log.debug("[configureGlobal] Inicio");
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
    /**
	 * Listado de edificios
	 * 
	 */
	public Map<String, Object> obtenerValoresEdificacio() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		List<TblEdificio> listaEdificio = null;
		try{
			log.debug("[obtenerValoresEdificacio] inicio");
			listaEdificio = edificioDao.listarAllActivos();
			if (listaEdificio!=null){
				for(TblEdificio edificio: listaEdificio){
					log.debug("[obtenerValoresEdificacio] edificio:"+edificio.getNombre());
					resultados.put(edificio.getNombre(), edificio.getCodigoEdificio());
				}
				
			}
			log.debug("[obtenerValoresEdificacio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaEdificio = null;
		}
		
		return resultados;
	}
	/**
	 * Listado de servicios
	 * 
	 */
	public Map<String, Object> obtenerValoresServicio() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		List<TblTipoServicio> listaTipoServicio = null;
		try{
			log.debug("[obtenerValoresServicio] inicio");
			listaTipoServicio = tipoServicioDao.listarAllActivosxRubro(Constantes.TIPO_RUBRO_SERVICIO);
			if (listaTipoServicio!=null){
				for(TblTipoServicio entidad: listaTipoServicio){
					log.debug("[obtenerValoresEdificacio] edificio:"+entidad.getNombre());
					resultados.put(entidad.getNombre(), entidad.getCodigoTipoServicio());
				}
				
			}
			log.debug("[obtenerValoresServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaTipoServicio = null;
		}
		
		return resultados;
	}
	/**
	 * Listado de servicios
	 * 
	 */
	public Map<String, Object> obtenerValoresServicioCompleto() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		List<TblTipoServicio> listaTipoServicio = null;
		try{
			log.debug("[obtenerValoresServicio] inicio");
			listaTipoServicio = tipoServicioDao.listarAllActivos();
			if (listaTipoServicio!=null){
				for(TblTipoServicio entidad: listaTipoServicio){
					log.debug("[obtenerValoresEdificacio] edificio:"+entidad.getNombre());
					resultados.put(entidad.getNombre(), entidad.getCodigoTipoServicio());
				}
				
			}
			log.debug("[obtenerValoresServicio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaTipoServicio = null;
		}
		
		return resultados;
	}
	/**
	 * Listado de suministro
	 * 
	 */
	public Map<String, Object> obtenerValoresSuministro() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		List<TblSuministro> listaSuministro = null;
		try{
			log.debug("[obtenerValoresSuministro] inicio");
			listaSuministro = suministroDao.listarAllActivos();
			if (listaSuministro!=null){
				for(TblSuministro entidad: listaSuministro){
					//log.debug("[obtenerValoresSuministro] edificio:"+entidad.getNumero());
					resultados.put(entidad.getNumero(), entidad.getCodigoSuministro());
				}
				
			}
			log.debug("[obtenerValoresSuministro] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaSuministro = null;
		}
		
		return resultados;
	}
	
	/**
	 * Listado de edificios
	 * 
	 */
	public Map<String, Object> obtenerValoresConcepto(String strTipoConcepto) {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		List<TblConcepto> listaConcepto = null;
		try{
			log.debug("[obtenerValoresConcepto] inicio");
			listaConcepto = conceptoDao.buscarxTipo(strTipoConcepto);
			if (listaConcepto!=null){
				for(TblConcepto entidad: listaConcepto){
					log.debug("[obtenerValoresConcepto] nombre:"+entidad.getNombre());
					resultados.put(entidad.getNombre(), entidad.getCodigoConcepto());
				}
				
			}
			log.debug("[obtenerValoresEdificacio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaConcepto = null;
		}
		
		return resultados;
	}
	
	public Map<String, Object> obtenerValoresAllConcepto() {
		Map<String, Object> resultados = new LinkedHashMap<String, Object>();
		List<TblConcepto> listaConcepto = null;
		try{
			log.debug("[obtenerValoresConcepto] inicio");
			listaConcepto = conceptoDao.listarAllActivos();
			if (listaConcepto!=null){
				for(TblConcepto entidad: listaConcepto){
					log.debug("[obtenerValoresConcepto] nombre:"+entidad.getNombre());
					resultados.put(entidad.getNombre(), entidad.getCodigoConcepto());
				}
				
			}
			log.debug("[obtenerValoresEdificacio] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaConcepto = null;
		}
		
		return resultados;
	}
	public Map<Integer, String> obtenerValoresAllConceptoMap() {
		Map<Integer, String> resultados = new LinkedHashMap<Integer, String>();
		List<TblConcepto> listaConcepto = null;
		try{
			log.debug("[obtenerValoresAllConceptoMap] inicio");
			listaConcepto = conceptoDao.listarAllActivos();
			if (listaConcepto!=null){
				for(TblConcepto entidad: listaConcepto){
					log.debug("[obtenerValoresAllConceptoMap] nombre:"+entidad.getNombre());
					resultados.put(entidad.getCodigoConcepto(), entidad.getNombre());
				}
				
			}
			log.debug("[obtenerValoresAllConceptoMap] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaConcepto = null;
		}
		
		return resultados;
	}
	//Listado de usuarios
	public Map<Integer, String> obtenerAllUsuarioMap() {
		Map<Integer, String> resultados = new LinkedHashMap<Integer, String>();
		List<TblUsuario> listaUsuario = null;
		try{
			log.debug("[obtenerAllUsuarioMap] inicio");
			listaUsuario = usuarioDao.findAll();
			if (listaUsuario!=null){
				for(TblUsuario entidad: listaUsuario){
					log.debug("[obtenerAllUsuarioMap] nombre:"+entidad.getNombre());
					resultados.put(entidad.getCodigoUsuario(), entidad.getNombre().toUpperCase());
				}
				
			}
			log.debug("[obtenerAllUsuarioMap] Fin");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listaUsuario = null;
		}
		
		return resultados;
	}
}