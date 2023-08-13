package com.pe.lima.sg.presentacion.util;

public class Constantes {

	//Variable para el estado
	public static String ESTADO_ACTIVO 						= "S";
	public static String ESTADO_INACTIVO 					= "N";
	//Variable para el estado de un registro
	public static String ESTADO_REGISTRO_ACTIVO 			= "1";
	public static String ESTADO_REGISTRO_INACTIVO 			= "0";
	
	public static String NO_SELECCIONADO		 			= "-1";

	//Variable para el estado de un usuario
	public static String ESTADO_USUARIO_ACTIVO 				= "S";
	public static String ESTADO_USUARIO_INACTIVO 			= "N";
	public static String DESC_ESTADO_USUARIO_ACTIVO			= "ACTIVO";
	public static String DESC_ESTADO_USUARIO_INACTIVO		= "INACTIVO";
	
	
	public static String CAJA_CHICA_INGRESO 				= "IG";
	public static String CAJA_CHICA_GASTO		 			= "GS";
	public static String DESC_CAJA_CHICA_INGRESO			= "INGRESO";
	public static String DESC_CAJA_CHICA_GASTO				= "GASTOS";
	
	
	
	public static Integer CAJA_CHICA_ABIERTO				= 1;
	public static Integer CAJA_CHICA_CERRADO				= 0;
	public static String  DESC_CAJA_CHICA_ABIERTO			= "ABIERTO";
	public static String  DESC_CAJA_CHICA_CERRADO			= "CERRADO";

	//Variables para el tipo de caducidad de la contraseña
	public static String TIPO_CADUCIDAD_INDEFINIDO			= "INDEFINIDO";
	public static String TIPO_CADUCIDAD_CADUCA_30			= "CADUCA 30 DIAS";
	public static Integer NUMERO_DIAS_30					= 30;

	//Variables para las listas de session
	public static String MAP_TIPO_CADUCIDAD					= "mapTipoCaducidad";
	public static String MAP_ESTADO_USUARIO					= "mapEstadoUsuario";
	public static String MAP_TIPO_TIENDA					= "mapTipoTienda";
	public static String MAP_TIPO_PISO						= "mapTipoPiso";
	public static String MAP_TIPO_CONCEPTO					= "mapTipoConcepto";
	public static String MAP_TIPO_PERSONA					= "mapTipoPersona";
	public static String MAP_ESTADO_CIVIL					= "mapEstadoCivil";
	public static String MAP_SI_NO							= "mapSiNo";
	public static String MAP_LISTA_SUMINISTRO				= "mapListaSuministro";
	public static String MAP_LISTA_EDIFICIO					= "mapListaEdificio";
	public static String MAP_TIPO_MONEDA					= "mapTipoMoneda";
	public static String MAP_TIPO_COBRO						= "mapTipoCobro";
	public static String MAP_TIPO_GARANTIA					= "mapTipoGarantia";
	public static String MAP_TIPO_DOCUMENTO					= "mapTipoDocumento";
	public static String MAP_TIPO_RUBRO						= "mapTipoRubro";
	public static String MAP_TIPO_PERIODO_ADELANTO			= "mapTipoPeriodoAdelanto";
	public static String MAP_ESTADO_ASIGNACION				= "mapEstadoAsignacion";
	public static String MAP_MESES							= "mapMeses";
	public static String MAP_TIPO_COBRO_CXC					= "mapTipoCobroCxC";

	//Variables para la operacion
	public static String OPERACION_NUEVO					= "NUEVO";
	public static String OPERACION_EDITAR					= "EDITAR";
	
	public static Integer CODIGO_SERVICIO_MANTENIMIENTO 	= 1;
	
	//Variables para la Operacion del desembolso
	public static Integer ESTADO_DESEMBOLSO_ACTIVO						= 1;
	public static Integer ESTADO_DESEMBOLSO_SOLICITADA_REVERSION		= 2;
	public static Integer ESTADO_DESEMBOLSO_ACEPTADA_REVERSION			= 3;

	public static String OPERACION_COBRO					= "COBRO";
	public static String OPERACION_ADELANTO					= "ADELANTO";
	//Variables para el tipo de tienda
	public static String DESC_TIPO_TIENDA_TIENDA			= "TIENDA";
	public static String DESC_TIPO_TIENDA_ALMACEN			= "ALMACEN";
	public static String DESC_TIPO_TIENDA_BODEGA			= "BODEGA";

	public static String TIPO_TIENDA_TIENDA					= "TI";
	public static String TIPO_TIENDA_ALMACEN				= "AL";
	public static String TIPO_TIENDA_BODEGA					= "BO";

	//Variables para el tipo de concepto
	public static String DESC_TIPO_CONCEPTO_INGRESO			= "INGRESO";
	public static String DESC_TIPO_CONCEPTO_GASTO			= "GASTO";

	public static String TIPO_CONCEPTO_INGRESO				= "IG";
	public static String TIPO_CONCEPTO_GASTO				= "GS";

	//Variables para el cobro
	public static String DESC_TIPO_COBRO_INICIO				= "PRIMER DIA DEL MES";
	public static String DESC_TIPO_COBRO_FIN				= "FIN DE MES";
	public static String DESC_TIPO_COBRO_QUINCENA			= "QUINCENA";
	public static String DESC_TIPO_COBRO_FECHA				= "FECHA ESPECIFICA";

	public static String TIPO_COBRO_INICIO					= "PDM";
	public static String TIPO_COBRO_FIN						= "FDM";
	public static String TIPO_COBRO_QUINCENA				= "QUI";
	public static String TIPO_COBRO_FECHA					= "FES";

	//Variables para el cobro
	public static String DESC_TIPO_GARANTIA_CON				= "CON GARANTIA";
	public static String DESC_TIPO_GARANTIA_SIN				= "SIN GARANTIA";
	public static String DESC_TIPO_GARANTIA_LLAVE			= "LLAVE";
	public static String TIPO_GARANTIA_CON					= "CG";
	public static String TIPO_GARANTIA_SIN					= "SG";
	public static String TIPO_GARANTIA_LLAVE				= "LL";

	//Variables para el cobro
	public static String DESC_TIPO_DOC_FACTURA				= "FACTURA";
	public static String DESC_TIPO_DOC_BOLETA				= "BOLETA";
	public static String DESC_TIPO_DOC_INTERNO				= "INTERNO";
	public static String TIPO_DOC_FACTURA					= "FAC";
	public static String TIPO_DOC_BOLETA					= "BOL";
	public static String TIPO_DOC_INTERNO					= "INT";

	//Variables para el tipo de persona
	public static String DESC_TIPO_PERSONA_NATURAL			= "NATURAL";
	public static String DESC_TIPO_PERSONA_JURIDICA			= "JURIDICA";

	public static String TIPO_PERSONA_NATURAL				= "N";
	public static String TIPO_PERSONA_JURIDICA				= "J";

	//Variables para el estado civil
	public static String DESC_ESTADO_CIVIL_SOLTERO			= "SOLTERO";
	public static String DESC_ESTADO_CIVIL_CASADO			= "CASADO";
	public static String DESC_ESTADO_CIVIL_VIUDO			= "VIUDO";
	public static String DESC_ESTADO_CIVIL_DIVORCIADO		= "DIVORCIADO";

	public static String ESTADO_CIVIL_SOLTERO				= "SO";
	public static String ESTADO_CIVIL_CASADO				= "CA";
	public static String ESTADO_CIVIL_VIUDO					= "VI";
	public static String ESTADO_CIVIL_DIVORCIADO			= "DI";

	//Variable para el tipo SI/NO
	public static String DESC_TIPO_SI						= "SI";
	public static String DESC_TIPO_NO						= "NO";

	public static String TIPO_SI							= "S";
	public static String TIPO_NO							= "N";

	//Variables para el estado de la tienda
	public static String ESTADO_TIENDA_OCUPADO				= "CPD";
	public static String ESTADO_TIENDA_DESOCUPADO			= "DSC";
	public static String DESC_ESTADO_TIENDA_OCUPADO			= "OCUPADO";
	public static String DESC_ESTADO_TIENDA_DESOCUPADO		= "DESCUPADO";

	//Variables para el tipo de moneda
	public static String MONEDA_SOL							= "SO";
	public static String MONEDA_DOLAR						= "DO";
	public static String DESC_MONEDA_SOL					= "SOLES";
	public static String DESC_MONEDA_DOLAR					= "DOLARES";

	//Variables para el tipo de persona
	public static String DESC_TIPO_RUBRO_SERVICIO			= "SERVICIO";
	public static String DESC_TIPO_RUBRO_CONTRATO			= "CONTRATO";

	public static String TIPO_RUBRO_SERVICIO				= "SRV";
	public static String TIPO_RUBRO_CONTRATO				= "CNT";
	
	//Variables para el tipo de cobro
	public static String DESC_TIPO_COBRO_ALQUILER			= "ALQUILER";
	public static String DESC_TIPO_COBRO_SERVICIO			= "SERVICIO";
	public static String DESC_TIPO_COBRO_LUZ				= "LUZ";
	public static String DESC_TIPO_COBRO_ARBITRIO			= "ARBITRIO";
	public static String DESC_TIPO_COBRO_GARANTIA			= "GARANTIA";
	
	public static String TIPO_COBRO_ALQUILER				= "ALQ";
	public static String TIPO_COBRO_SERVICIO				= "SER";
	public static String TIPO_COBRO_LUZ						= "LUZ";
	public static String TIPO_COBRO_ARBITRIO				= "ARB";
	public static String TIPO_COBRO_PRIMER_COBRO			= "PRM";
	public static String TIPO_COBRO_GARANTIA				= "GAR";

	public static String TIPO_COBRO_PRIMER_COBRO_ALQUILER	= "PRM-ALQ";
	public static String TIPO_COBRO_PRIMER_COBRO_GARANTIA	= "PRM-GAR";
	public static String TIPO_COBRO_PRIMER_COBRO_SERVICIO	= "PRM-SER";
	//Variables del arbol del menu
	public static String MENU_CABECERA_INI					= "<div class=\"innertube\"> <div id=\"jstree_demo_div2\"> <div id=\"jstree2\"> <ul>";
	public static String MENU_CABECERA_INI_PRINCIPAL		= "<div class=\"innertube\"> <div id=\"jstree_demo_div\"> <div id=\"jstree\"> <ul>";
	public static String MENU_CABECERA_FIN					= "</ul> </div> </div> </div>";

	//Variables del listado de Opciones para el perfil
	public static String ROW_OPCION_INI					= "<div class=\"row\">";
	public static String ROW_OPCION_FIN					= "</div>";
	public static String COL_OPCION_INI_01				= "<div class=\"col-md-"; //Falta el tamaño de la columna
	public static String COL_OPCION_INI_02				= "mb-3\">";
	public static String COL_OPCION_FIN					= "</div>";

	//Estados del Contrato
	public static String ESTADO_CONTRATO_PENDIENTE			= "PND";
	public static String ESTADO_CONTRATO_VIGENTE			= "VGN";
	public static String ESTADO_CONTRATO_RENOVADO			= "RNV";
	public static String ESTADO_CONTRATO_FINALIZADO			= "FNL";

	public static String DESC_ESTADO_CONTRATO_PENDIENTE		= "PENDIENTE";
	public static String DESC_ESTADO_CONTRATO_VIGENTE		= "VIGENTE";
	public static String DESC_ESTADO_CONTRATO_RENOVADO		= "RENOVADO";

	//Variables para la operacion
	public static String CADENA_CERO						= "0";
	public static String PREFIJO_CONTRATO					= "CNT";

	//Variables para el trimestre
	public static String TRIMESTRE_PRIMERO					= "TRIMESTRE 1";
	public static String TRIMESTRE_SEGUNDO					= "TRIMESTRE 2";
	public static String TRIMESTRE_TERCERO					= "TRIMESTRE 3";
	public static String TRIMESTRE_CUARTO					= "TRIMESTRE 4";

	//Variables de operacion 
	public static String PAGINADO_ANTERIOR					= "A";
	public static String PAGINADO_SIGUIENTE					= "S";

	//Variables de Parametros
	/*Indica el inmueble que se mostrará por defecto*/
	public static String PAR_INMUEBLE						= "INMUEBLE";
	/*Indica el porcentaje de incremento para el arbitrio*/
	public static String PAR_ARBITRIO_PORCENTAJE_INCREMENTO	= "ARBITRIO_INCREMENTO";
	/*Indica el año de inicio de las listas en el sistema*/
	public static String PAR_ANIO_INICIO					= "ANIO_INICIO";
	/*Indica el periodo de adelanto*/
	public static String PAR_PERIODO_ADELANTO				= "PERIODO_ADELANTO";
	/*Indica el año de inicio de las listas en el sistema*/
	public static String PAR_FIN_CONTRATO					= "FIN_CONTRATO";
	
	public static Integer PAR_ADD_1_DIA						= 1;
	/*Indica la descripcion del cobro del alquiler*/
	public static String PAR_SUNAT_ALQUILER					= "DESCRIPCION_SUNAT_LOCAL_ALQUILER";
	public static String PAR_SUNAT_SERVICIO					= "DESCRIPCION_SUNAT_SERVICIO_ALQUILER";

	//Variables para el periodo de adelanto
	public static String PERIODO_ADELANTO_0					= "0";
	public static String PERIODO_ADELANTO_1					= "1";
	public static String PERIODO_ADELANTO_2					= "2";
	public static String PERIODO_ADELANTO_3					= "3";
	public static String PERIODO_ADELANTO_4					= "4";
	public static String PERIODO_ADELANTO_5					= "5";
	public static String PERIODO_ADELANTO_6					= "6";
	public static String PERIODO_ADELANTO_7					= "7";
	public static String PERIODO_ADELANTO_8					= "8";
	public static String PERIODO_ADELANTO_9					= "9";
	public static String PERIODO_ADELANTO_10				= "10";
	public static String PERIODO_ADELANTO_11				= "11";
	public static String PERIODO_ADELANTO_12				= "12";

	public static String DESC_PERIODO_ADELANTO_0			= "NINGUNO";
	public static String DESC_PERIODO_ADELANTO_1			= "1 MES";
	public static String DESC_PERIODO_ADELANTO_2			= "2 MESES";
	public static String DESC_PERIODO_ADELANTO_3			= "3 MESES";
	public static String DESC_PERIODO_ADELANTO_4			= "4 MESES";
	public static String DESC_PERIODO_ADELANTO_5			= "5 MESES";
	public static String DESC_PERIODO_ADELANTO_6			= "6 MESES";
	public static String DESC_PERIODO_ADELANTO_7			= "7 MESES";
	public static String DESC_PERIODO_ADELANTO_8			= "8 MESES";
	public static String DESC_PERIODO_ADELANTO_9			= "9 MESES";
	public static String DESC_PERIODO_ADELANTO_10			= "10 MESES";
	public static String DESC_PERIODO_ADELANTO_11			= "11 MESES";
	public static String DESC_PERIODO_ADELANTO_12			= "12 MESES";

	//Variables del tipo de servicio
	public static Integer TIPO_SERVICIO_ALQUILER			= 6;
	public static Integer TIPO_SERVICIO_GARANTIA			= 9;

	//Primeros Cobros
	public static String DESC_PRIMEROS_COBROS				= "Registro de primeros cobros";
	public static String DESC_PRIMEROS_COBROS_ALQUILER		= "PRIMER COBRO ALQUILER";
	public static String DESC_PRIMEROS_COBROS_SERVICIO		= "PRIMER COBRO SERVICIO";
	public static String DESC_PRIMEROS_COBROS_GARANTIA		= "PRIMER COBRO GARANTIA";
	public static String DESC_CUOTA_ALQUILER				= "CUOTA ALQUILER";
	public static String DESC_CUOTA_SERVICIO				= "CUOTA SERVICIO";
	public static String DESC_CUOTA_LUZ						= "MENSUALIDAD LUZ";
	//Variable para indentificar la asignacion de arbitrios
	public static String ESTADO_ASIGNADO					= "ASIGNADO";
	public static String ESTADO_PENDIENTE					= "PENDIENTE";
	
	//Variable para los meses
	public static String MES_01								= "01";
	public static String MES_02								= "02";
	public static String MES_03								= "03";
	public static String MES_04								= "04";
	public static String MES_05								= "05";
	public static String MES_06								= "06";
	public static String MES_07								= "07";
	public static String MES_08								= "08";
	public static String MES_09								= "09";
	public static String MES_10								= "10";
	public static String MES_11								= "11";
	public static String MES_12								= "12";
	
	public static String MES_01_MOROSO						= "01/02/";
	public static String MES_02_MOROSO						= "01/03/";
	public static String MES_03_MOROSO						= "01/04/";
	public static String MES_04_MOROSO						= "01/05/";
	public static String MES_05_MOROSO						= "01/06/";
	public static String MES_06_MOROSO						= "01/07/";
	public static String MES_07_MOROSO						= "01/08/";
	public static String MES_08_MOROSO						= "01/09/";
	public static String MES_09_MOROSO						= "01/10/";
	public static String MES_10_MOROSO						= "01/11/";
	public static String MES_11_MOROSO						= "01/12/";
	public static String MES_12_MOROSO						= "01/01/";
	
	public static String DESC_MES_01						= "ENERO";
	public static String DESC_MES_02						= "FEBRERO";
	public static String DESC_MES_03						= "MARZO";
	public static String DESC_MES_04						= "ABRIL";
	public static String DESC_MES_05						= "MAYO";
	public static String DESC_MES_06						= "JUNIO";
	public static String DESC_MES_07						= "JULIO";
	public static String DESC_MES_08						= "AGOSTO";
	public static String DESC_MES_09						= "SEPTIEMBRE";
	public static String DESC_MES_10						= "OCTUBRE";
	public static String DESC_MES_11						= "NOVIEMBRE";
	public static String DESC_MES_12						= "DICIEMBRE";
	
	//Datos de los documentos
	public static String TIPO_COMPROBANTE_FACTURA			= "01";
	public static String TIPO_COMPROBANTE_BOLETA			= "03";
	public static String TIPO_COMPROBANTE_NOTA_CREDITO		= "07";
	public static String TIPO_COMPROBANTE_NOTA_DEBITO		= "08";
	public static String TIPO_COMPROBANTE_INTERNO			= "99"; //SOLO PARA DOC INTERNOS
	
	//SERIE: Tipo de Operacion
	public static String SERIE_TIPO_OPERACION_MENSUAL			= "MNS";
	public static String SERIE_TIPO_OPERACION_PRIMEROS_COBROS	= "PRM";
	public static String SERIE_TIPO_OPERACION_INDIVIDUAL		= "IND";
	public static String SERIE_TIPO_OPERACION_CONTRATO			= "CNT";
	
	//Catalogo 17: Codigo de tipo de Operacion
	//public static String SUNAT_TIPO_OPERACION_VENTA_INTERNA					= "01";
	
	public static Integer SUNAT_CODIGO_DOMICILIO_FISCAL						= 0;	
	
	public static String SUNAT_TIPO_DOCUMENTO_REGISTRO_UNICO_CONTRIBUYENTE	= "6";
	public static String SUNAT_TIPO_DOCUMENTO_DOC_NACIONAL_IDENTIDAD		= "1";
	
	public static String SUNAT_TIPO_MONEDA_SOLES							= "PEN";
	public static String SUNAT_TIPO_MONEDA_DOLAR							= "USD";
	
	public static Integer SUNAT_IGV											= 18;
	
	public static String SUNAT_RUC_EMISOR									= "20386431427";
	public static String SUNAT_RAZON_SOCIAL_EMISOR							= "GRUPO LA REYNA S.A.C.";
	public static String SUNAT_NOMBRE_COMERCIAL_EMISOR						= "CENT. COM. LA REYNA DE GAMARRA";
	public static String SUNAT_UBIGEO_EMISOR								= "150140";//inei --> "140130";
	public static String SUNAT_DIRECCION_EMISOR								= "AV. CIRCUNVALACIÓN DEL CLUB GOLF LOS INCAS NRO 202 INTERIOR 402";
	public static String SUNAT_URBANIZACION_EMISOR							= "URB. CLUB GOLF LOS INCAS";
	public static String SUNAT_ARCHIVO_EXTENSION_CABECERA					= "CAB";
	public static String SUNAT_ARCHIVO_EXTENSION_DETALLE					= "DET";
	
	public static String SUNAT_UNIDAD_MEDIDA								= "NIU";
	
	public static String SUNAT_CANTIDAD_UNITARIA							= "1";
	
	public static String SUNAT_AFECTACION_IGV_GRAVADO_OPE_ONEROSO			= "10";
	
	public static String SUNAT_PIPE											= "|";
	public static String SUNAT_COMA											= ",";
	
	public static Integer SUNAT_LONGITUD_SERIE								= 3;
	public static Integer SUNAT_LONGITUD_NUMERO								= 8;
	
	public static String SUNAT_FACTURADOR_RUTA								= "G:\\data0\\facturador\\DATA\\";
	//public static String SUNAT_FACTURADOR_RUTA_PRUEBA						= "G:\\prueba\\";
	//public static String SUNAT_FACTURADOR_RUTA_PRUEBA						= "C:\\03.Data_Sunat\\";  
	
	//Descripcion para la reversion
	public static String DESC_REVERSION_COBRO								= " REVERSION SOLICITADA Y APROBADA";
	
	public static String CODIGO_INGRESO										= "I";
	public static String CODIGO_EGRESO										= "E";
	public static String DESCRIPCION_INGRESO								= "INGRESO";
	public static String DESCRIPCION_EGRESO									= "EGRESO";
	
	public static String TIPO_PAGO_ALQUILER_CODIGO							= "ALQ";
	public static String TIPO_PAGO_SERVICIO_CODIGO							= "SER";
	public static String TIPO_PAGO_ALQUILER_DESCRIPCION						= "ALQUILER";
	public static String TIPO_PAGO_SERVICIO_DESCRIPCION						= "SERVICIO";	
	
	public static String TIPO_OPERACION_VENTA_INTERNA_CODIGO				= "0101";
	public static String TIPO_OPERACION_DETRACCION_CODIGO					= "1001";
	public static String TIPO_OPERACION_VENTA_INTERNA_DESCRIPCION			= "VENTA INTERNA";
	public static String TIPO_OPERACION_DETRACCIONDESCRIPCION				= "OPERACION SUJETA A DETRACCION";	
	
	public static String RESPUESTA_BUSQUEDA_EXITOSA							= "Se encontró registros con los criterios ingresados";
	
	public static Integer REFINANCIAR_TAB_ALQUILER						= 0;
	public static Integer REFINANCIAR_TAB_SERVICIO						= 1;
	public static Integer REFINANCIAR_TAB_LUZ							= 2;
	
	public static String SUNAT_TIPO_OPERACION_EXPORTACION					= "02";
	
	public static String SUNAT_TIPO_AFECTACION_GRAVADO						= "GRAVADO";
	public static String SUNAT_TIPO_AFECTACION_EXONERADO					= "EXONERADO";
	public static String SUNAT_TIPO_AFECTACION_INAFECTO						= "INAFECTO";
	public static String SUNAT_TIPO_AFECTACION_EXPORTACION					= "EXPORTACION";
	
	public static String SUNAT_VERSION_UBL									= "2.1";
	public static String SUNAT_CUSTOMIZACION								= "2.0";
	
	public static String SUNAT_SIN_CODIGO									= "-";
	
	public static String SUNAT_TRIBUTO_CODIGO								= "1";	
	public static String SUNAT_TRIBUTO_NOMBRE								= "2";	
	public static String SUNAT_TRIBUTO_TIPO									= "3";	
	
	public static String SUNAT_EXONERADO_OPERACION_GRATUITA					= "21";
	public static String SUNAT_INAFECTO_OPERACION_GRATUITA					= "37";
	
	
	public static String SUNAT_ARCHIVO_EXTENSION_TRIBUTO					= "TRI";
	public static String SUNAT_ARCHIVO_EXTENSION_ADICIONAL_DETALLE			= "ADE";
	public static String SUNAT_ARCHIVO_EXTENSION_LEYENDA					= "LEY";
	public static String SUNAT_ARCHIVO_EXTENSION_CABECERA_NOTA				= "NOT";
	//Leyenda
	public static String SUNAT_LEYENDA_DETRACCION_3001						= "3001";
	public static String SUNAT_LEYENDA_MONTO_LETRAS_1000					= "1000";
	
	//DATOS DEL COMPROBANTE
	public static String SUNAT_TIPO_OPERACION_VENTA_INTERNA					= "0101";
	public static String SUNAT_CODIGO_DOMICILIO_FISCAL_DEFAULT				= "01";
	public static String SUNAT_TIPO_DOCUMENTO_DNI							= "1";
	public static String SUNAT_TIPO_DOCUMENTO_RUC							= "6";
	public static String SUNAT_CODIGO_PRODUCTO_DEFAULT						= "ALQ001";
	
	public static String SUNAT_SERIE_BOLETA									= "B001";
	public static String SUNAT_SERIE_FACTURA								= "F001";
	
	//Datos Persona
	public static String PERSONA_NATURAL									= "N";
	public static String PERSONA_JURIDICA									= "J";
	//Parametro de la suna
	public static String  SUNAT_PARAMETRO_SISTEMA							= "PARASIST";
	
	public static String SUNAT_PARAMETRO_NOMBRE_COMERCIAL					= "NOMCOM";
	public static String SUNAT_PARAMETRO_RAZON_SOCIAL						= "RAZON";
	public static String SUNAT_PARAMETRO_DIRECCION_EMISOR					= "DIRECC";
	public static String SUNAT_PARAMETRO_DEPARTAMENTO_EMISOR				= "DEPAR";
	public static String SUNAT_PARAMETRO_PROVINCIA_EMISOR					= "PROVIN";
	public static String SUNAT_PARAMETRO_DISTRITO_EMISOR					= "DISTR";
	public static String SUNAT_PARAMETRO_URBANIZACION_EMISOR				= "URBANIZA";
	public static String SUNAT_PARAMETRO_RUC_EMISOR							= "NUMRUC";
	public static String SUNAT_PARAMETRO_FUNCIONAMIENTO						= "FUNCIO";
	//Comprobantes
	public static String SUNAT_CODIGO_COMPROBANTE_FACTURA					= "01";
	public static String SUNAT_CODIGO_COMPROBANTE_BOLETA					= "03";
	public static String SUNAT_CODIGO_COMPROBANTE_NOTA_CREDITO				= "07";
	public static String SUNAT_CODIGO_COMPROBANTE_NOTA_DEBITO				= "08";
	
	//PARAMETRO
	public static String PARAMETRO_SALDO_CERO								= "SALDO_CERO";
	public static String PARAMETRO_IGV										= "IGV";
	public static String PARAMETRO_SERVICIO									= "SERVICIO";
	public static String PARAMETRO_DETRACCION								= "DETRACCION";
	public static String PARAMETRO_TIPO_SISTEMA_CALCULO_ISC					= "TIPO_ISC";
	
	public static String PARAMETRO_TIPO_OPERACION							= "TIPO_OPERACION";
	public static String PARAMETRO_TIPO_OPERACION_SFS_12					= "TIPO_OPERACION_SFS_12";
	public static String PARAMETRO_CODIGO_DOMICILIO_FISCAL					= "CODIGO_DOMICILIO_FISCAL";
	public static String PARAMETRO_TIPO_COMPROBANTE							= "TIPO_COMPROBANTE";
	public static String PARAMETRO_MONEDA									= "MONEDA";
	public static String PARAMETRO_SERIE									= "SERIE";
	public static String PARAMETRO_UNIDAD_MEDIDA							= "UNIDAD_MEDIDA";
	public static String PARAMETRO_AFECTACION_IGV							= "AFECTACION_IGV";
	public static String PARAMETRO_SUNAT_DATA								= "SUNAT_DATA";
	public static String PARAMETRO_SUNAT_BD									= "SUNAT_BD";
	public static String PARAMETRO_RUC_EMPRESA								= "RUC_EMPRESA";
	public static String PARAMETRO_SERIE_FACTURA							= "SERIE_FACTURA";
	public static String PARAMETRO_SERIE_AUTOMATICA							= "SERIE_AUTOMATICA";
	public static String SUNAT_FACTURADOR_RUTA_PRUEBA						= "";//"G:\\data0\\facturador\\DATA\\";//"D:\\sunat_archivos\\sfs\\DATA\\";//"D:\\data0\\facturador\\DATA\\";//"D:\\data01\\";
	public static String SUNAT_FACTURADOR_DB_LOCAL							= "";//"G:\\Jetty\\bd\\BDFacturador.db";//"D:\\SUNAT\\servers\\sfs\\bd\\BDFacturador.db";//"D:\\Jetty\\bd\\BDFacturador.db";//"D:/Bill/BDFacturador.db";
	
	public static Integer CODIGO_INMUEBLE_LA_REYNA							= 1;
	public static Integer CODIGO_TIPO_GASTO_DIVERSO							= 3;
	public static Integer CODIGO_TIPO_INGRESO_OTROS							= 17;
	
	//Variables para la operacion
	public static String TIPO_PAGO_COD_EFECTIVO								= "EFE";
	public static String TIPO_PAGO_COD_BANCARIZADO							= "BAN";
	public static String TIPO_PAGO_DES_EFECTIVO								= "EFECTIVO";
	public static String TIPO_PAGO_DES_BANCARIZADO							= "BANCARIZADO";
	//Constantes de Permisos
	public static String MODULO_COBRO_FECHA_COBRO							= "COBFEC";
	public static String MODULO_INGRESO_FECHA_INGRESO						= "INGFEC";
	public static String MODULO_COBRO_ELIMINAR_COBRO						= "COBELI";
	public static String MODULO_INGRESO_BOTON_ELIMINAR						= "INGELI";
	//Bancarizado
	public static String TIPO_BANCARIZADO_COD_TRANSFERENCIA				= "01";
	public static String TIPO_BANCARIZADO_COD_DEPOSITO					= "02";
	public static String TIPO_BANCARIZADO_COD_CHEQUE					= "03";
	public static String TIPO_BANCARIZADO_COD_OTROS						= "04";
	public static String TIPO_BANCARIZADO_COD_POS						= "05";
	
	public static String TIPO_BANCARIZADO_DES_TRANSFERENCIA				= "TRANSFERENCIA";
	public static String TIPO_BANCARIZADO_DES_DEPOSITO					= "DEPOSITO";
	public static String TIPO_BANCARIZADO_DES_CHEQUE					= "CHEQUE";
	public static String TIPO_BANCARIZADO_DES_OTROS						= "OTROS";
	public static String TIPO_BANCARIZADO_DES_POS						= "IZIPAY (POS)";
	
	//Estados para el reporte de Local x Inmuebles
	public static String ESTADO_LIBRE 									= "LIBRE";
	public static String ESTADO_ALQUILADO 								= "ALQUILADO";
	public static String ESTADO_VENDIDO 								= "VENDIDO";
	
	//Parametros para el modulo OSE
	public static String PARAMETRO_DOMICILIO_FISCAL						= "LA_REYNA_DIRECCION_FISCAL";
	public static String PARAMETRO_PORCENTAJE_DETRACCION_ALQUILER		= "DETRACCION_PORCENTAJE_ALQUILER";
	public static String PARAMETRO_PORCENTAJE_DETRACCION_SERVICIO		= "DETRACCION_PORCENTAJE_SERVICIO";
	public static String PARAMETRO_MONTO_DETRACCION_ALQUILER			= "DETRACCION_MONTO_ALQUILER";
	public static String PARAMETRO_MONTO_DETRACCION_SERVICIO			= "DETRACCION_MONTO_SERVICIO";

	public static String FORMA_PAGO_CONTADO								= "CONTADO";
	public static String FORMA_PAGO_CREDITO								= "CREDITO";
	public static Integer CODIGO_LA_REYNA								= 1;
	//Variables para el estado de la tienda
	public static String ESTADO_OPERACION_REGISTRADO					= "REGISTRADO";
	public static String ESTADO_OPERACION_ENVIADO						= "ENVIADO";
	public static String ESTADO_OPERACION_PROCESADO						= "PROCESADO";
	
	public static String NUMERO_CTA_DETRACCION_LA_REYNA					 = "00000710245";
	//Catálogo No. 54: Códigos de bienes y servicios sujetos a detracción
	public static String CATALOGO_54_CODIGO_ARRENDAMIENTO_BIENES_Y_SERVICIO = "019";
	
	//Porcentaje de detraccion
	public static String PORCENTAJE_DE_TASA_DE_DETRACCION 				= "10.00";
	//Códigos de tipos de tributos
	public static String CODIGO_IMPUESTO_GENERAL_A_LAS_VENTAS			= "1000";
	//Precio unitario (incluye el IGV)
	public static String CODIGO_PRECIO_UNITARIO_INCLUYE_IGV				= "01";			
	//Codigo de establecimiento
	public static String CODIGO_ESTABLECIMIENTO							= "0000";
	//Datos para la coneccion con el API OSE
	public static String ENCODE_UTF_8									= "UTF-8";
	public static String GRANT_TYPE_NAME								= "grant_type";
	public static String USER_NAME										= "username";
	public static String PASSWORD										= "password";
	public static String ACCESS_TOKEN									= "access_token";
	public static String BODY_FILE										= "file";
	public static String BODY_FILE_EXTENSION							= "text/csv";
	public static String HEADER_AUTORIZATION							= "Authorization";
	public static String HEADER_BEARER									= "Bearer ";
	public static String HEADER_BASIC									= "Basic ";
	//URL DE EFACT
	public static String RUTA_FILE_OSE									= "RUTA_FILE_OSE";
	public static String URL_EFACT_TOKEN								= "URL_EFACT_TOKEN";
	public static String URL_EFACT_DOCUMENTO							= "URL_EFACT_DOCUMENTO";
	public static String URL_EFACT_CDR									= "URL_EFACT_CDR";
	public static String URL_EFACT_XML									= "URL_EFACT_XML";
	public static String URL_EFACT_PDF									= "URL_EFACT_PDF";
	
	public static String EFACT_CLIENT_SECRET							= "CLIENT_SECRET";
	public static String EFACT_GRANT_TYPE								= "GRANT_TYPE";
	public static String EFACT_USER_NAME								= "USER_NAME";
	public static String EFACT_PASSWORD									= "PASSWORD";
	
	public static String TIPO_OPERACION_NOTA_CREDITO_CODIGO					= "07";
	public static String TIPO_OPERACION_NOTA_DEBITO_CODIGO					= "08";
	public static String TIPO_OPERACION_NOTA_CREDITO_DESCRIPCION			= "07: NOTA CREDITO";
	public static String TIPO_OPERACION_NOTA_DEBITO_DESCRIPCION				= "08: NOTA DEBITO";	
	
	public static String SUNAT_TIPO_NOTA_01_ANULACION_CODIGO					= "01";
	//Modulo Masivo
	public static String MASIVO_TIPO_ALQUILER							= "ALQUILER";
	/*Estados*/
	public static String MASIVO_ESTADO_REGISTRADO							= "REGISTRADO";
	public static String MASIVO_ESTADO_EN_PROCESO							= "EN PROCESO";
	public static String MASIVO_ESTADO_FINALIZADO							= "FINALIZADO";
}
