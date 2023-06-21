

/*******************************************************************************************************
 * 
 * *****************************************************************************************************
 */
// JavaScript Document
	var isIE = document.all?true:false;
	var isNS = document.layers?true:false;
	function fSoloDigitos( e,decReq ) {
	    var key 	= (isIE) ? window.event.keyCode : e.which;
	    var obj 	= (isIE) ? event.srcElement : e.target;
	    var isNum 	= (key > 47 && key < 58) ? true:false;
	    var dotOK 	= (decReq=='decOK' && key == 46 && obj.value.indexOf('.')==-1) ? true:false;
		//BUG-SIC-96 JABF 2011.12.12
	    //window.event.keyCode = (!isNum && !dotOK && isIE) ? 0:key;
	    //e.which = (!isNum && !dotOK && isNS) ? 0:key;   
	    return (isNum || dotOK );
	}   
function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    		if (charCode > 31 && (charCode != 46 &&(charCode < 48 || charCode > 57)))
        return false;
    return true;
}

function jsLetra(){
	document.forms[0].action = '/menu';
	document.forms[0].submit();
}
/*
 * Invoca a la opción descrita en el menu
 */
function jsOpcionMenu(opcion){
	//alert(opcion);
	document.forms[0].action = '/' + opcion;
	//document.forms[0].action = opcion;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
	
	
}

/*
 * Invoca a la opción descrita en el menu
 */
function jsOpcionModuloListado(opcion, id){
	//alert(opcion);
	document.forms[0].action = '/' + opcion + '/'+id;
	//document.forms[0].action = opcion;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsSeleccion(nodo){
	//alert(nodo);
	if (nodo != undefined){
		$('#jstree').jstree(true).select_node(nodo);
		$('#jstree').jstree('select_node', nodo);
		$.jstree.reference('#jstree').select_node(nodo);
	}else{
		alert('No se selecciono');
	}
}

/* *********************************************************************************************************************************
 * MODULO DE CONTRATOS
 ********************************************************************************************************************************* */
function jsSeleccionarRadio(indice){
	var obj = document.getElementById('idRadioContrato'+indice);
	if( !obj.checked){
		obj.checked = !obj.checked;
	}
	if (obj.checked){
		document.getElementById("idContratoEscogido").value = obj.value;
	}else{
		document.getElementById("idContratoEscogido").value = "";
	}
}
function jsEditarContratoMenu(){
	var codigoContrato = document.getElementById("idContratoEscogido").value;
	if (codigoContrato == ""){
		alert("Debe seleccionar un Contrato");
		return ;
	}else{
		document.forms[0].action = '/contrato/editar/'+codigoContrato;
		document.forms[0].method = 'GET';
		document.forms[0].submit();
	}
}
function jsEliminarContratoMenu(){
	var codigoContrato = document.getElementById("idContratoEscogido").value;
	if (codigoContrato == ""){
		alert("Debe seleccionar un Contrato");
		return ;
	}else{
		if(confirm('Desea eliminar el contrato seleccionado?')){
			document.forms[0].action = '/contrato/eliminar/'+codigoContrato;
			document.forms[0].method = 'GET';
			document.forms[0].submit();
		}
	}
}
function jsRenovarContratoMenu(){
	var codigoContrato = document.getElementById("idContratoEscogido").value;
	if (codigoContrato == ""){
		alert("Debe seleccionar un Contrato");
		return ;
	}else{
		document.forms[0].action = '/renovarcontrato/editar/contrato/'+codigoContrato;
		document.forms[0].method = 'GET';
		document.forms[0].submit();
	}
	
}
function jsFinalizarContratoMenu(){
	var codigoContrato = document.getElementById("idContratoEscogido").value;
	if (codigoContrato == ""){
		alert("Debe seleccionar un Contrato");
		return ;
	}else{
		document.forms[0].action = '/finalizacion/editar/contrato/'+codigoContrato;
		document.forms[0].method = 'GET';
		document.forms[0].submit();
	}
}
function jsLimpiarFiltroContrato(){
	clearAllForm(document.getElementById("form"))
}

//Busqueda de Local
function jsListarLocales(){
	/*var idEdificio = document.getElementById('codigoEdificio').value;
	if (idEdificio == '-1'){
		alert('Debe seleccionar un Inmueble antes de listar los Locales');
		
	}else{
		
	}*/
	document.getElementById('idLocalesContratoNuevo').disabled = true;
	document.forms[0].action = '/contrato/locales' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Selecciona un arbitrio para edicion
function jsEditarArbitrios(codigoArbitrio){
	document.getElementById('codigoArbitrio').value = codigoArbitrio;
	document.forms[0].action = '/contrato/arbitrio/editar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
//Asignacion de un Local
function jsSeleccionarLocales(idCodigoTienda){
	document.getElementById('codigo').value = idCodigoTienda;
	document.forms[0].action = '/contrato/locales/seleccionar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Busqueda de Clientes
function jsListarCliente(){
	document.getElementById('idClienteContratoNuevoEdicion').disabled = true;
	document.forms[0].action = '/contrato/clientes' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Asignacion de un cliente
function jsSeleccionarCliente(idCodigoPersona){
	document.getElementById('codigo').value = idCodigoPersona;
	document.forms[0].action = '/contrato/clientes/seleccionar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsRegresarContrato(){
	document.forms[0].action = '/contrato/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsRegresarContratoDeLocal(){
	document.forms[0].action = '/contrato/local/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Adiciona un servicio en la lista
function jsAdicionarServicio(){
	//validar los campos
	document.getElementById('btnServicioContratoNuevo').disabled = true;
	document.forms[0].action = '/contrato/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}

function jsAdicionarObservacionEdicion(){
	document.getElementById('btnObservacionContratoEdicion').disabled = true;
	document.forms[0].action = '/contrato/observacion/edicion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
function jsAdicionarObservacion(){
	document.getElementById('idObservacionContratoNuevo').disabled = true;
	document.forms[0].action = '/contrato/observacion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
//Adicionar primeros cobros en la lista
function jsAdicionarPrimerosCobros(){
	//validar los campos
	document.forms[0].action = '/contrato/cobro' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}

//Adicionar primeros cobros en la lista
function jsAdicionarPrimerosCobrosEdicion(){
	//validar los campos
	document.forms[0].action = '/contrato/cobro/edicion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Historial de Clientes
function jsHistorialCliente(){
	document.forms[0].action = '/contrato/cliente/historial' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
	
}
//Actualiza la fecha fin de contrato
function jsActualizarFecha(){
	document.forms[0].action = '/contrato/fecha/actualizar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
	
}

/* *********************************************************************************************************************************
 * MODULO DE CAMBIOS EN EL CONTRATO
 ********************************************************************************************************************************* */


//Busqueda de Clientes
function jsListarClienteCambioContrato(){
	document.getElementById('idClienteCambioEdicion').disabled = true;
	document.forms[0].action = '/cambiocontrato/clientes' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Historial de Clientes
function jsHistorialClienteCambioContrato(){
	document.forms[0].action = '/cambiocontrato/cliente/historial' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Retorna al modulo de cambios en el contrato 
function jsRegresarCambioContrato(){
	document.forms[0].action = '/cambiocontrato/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Adiciona un servicio en la lista
function jsAdicionarServicioCambioContrato(){
	//validar los campos
	document.getElementById('idServicioCambioEdicion').disabled = true;
	document.forms[0].action = '/cambiocontrato/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Adicionar Observacion
function jsAdicionarObservacionCambioContrato(){
	document.getElementById('idObservacionCambioEdicion').disabled = true;
	document.forms[0].action = '/cambiocontrato/observacion_v2/edicion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
//Actualiza los datos del contrato
function jsActualizarContratoCambioContrato(){
	document.getElementById('idActualizarCambioEdicion').disabled = true;
	document.forms[0].action = '/cambiocontrato/editar/guardar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Actualizar los datos del alquiler
function jsActualizarAlquiler(){
	document.getElementById('idActualizarAlquiler').disabled = true;
	document.forms[0].action = '/cambiocontrato/editar/guardar/alquiler' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsActualizarServicio(){
	document.getElementById('idActualizarAlquiler').disabled = true;
	document.forms[0].action = '/cambiocontrato/editar/guardar/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
//Elimina un servicio
function jsEliminarServicioCambioContrato(indice){
	document.getElementById('idIndice').value = indice;
	document.forms[0].action = '/cambiocontrato/eliminar/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}

function jsObtenerDatosClientesCambioContrato(codigoCliente){
	document.forms[0].action = '/cambiocontrato/clientes/seleccionar/'+codigoCliente;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
/* *********************************************************************************************************************************
 * MODULO DE ARBITRIOS
 ********************************************************************************************************************************* */
function jsGenerarArbitrios(){
	document.forms[0].action = '/cliente/arbitrio/nuevo' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}

function jsCargarTiendaArbitrio(){
	document.forms[0].action = '/cliente/arbitrio/local' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsAsignarArbitrio(){
	document.forms[0].action = '/cliente/arbitrio/asignar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsMostrarxEstadoPendiente(){
	document.forms[0].action = '/cliente/arbitrio/pendiente/estado' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
/* *********************************************************************************************************************************
 * MODULO DE LUZ x TIENDA - Candidato a eliminar
 ********************************************************************************************************************************* */

function jsCargarTiendas(){
	document.forms[0].action = '/cliente/lucesxtienda/suministro' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsCargarUltimoDia(){
	
	if (document.getElementById('anioFiltro').value != -1){
		document.forms[0].action = '/cliente/lucesxtienda/ultimodia' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();
	}else{
		alert('Debe seleccionar un año');
		return false;
	}
	
}
function jsCargarTienda(){
	document.forms[0].action = '/cliente/lucesxtienda/tienda' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsGenerarMontosLuz(){
	document.forms[0].action = '/cliente/lucesxtienda/calculo' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}


/* *********************************************************************************************************************************
 * MODULO DE APROBACION DE CONTRATO
 ********************************************************************************************************************************* */

function jsAprobarContrato(){
	document.forms[0].action = '/aprobacion/editar/guardar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsListarClienteAprobacion(){

	document.forms[0].action = '/aprobacion/clientes' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();

}
function jsRegresarAprobacion(){
	document.forms[0].action = '/aprobacion/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Historial de Clientes
function jsHistorialClienteAprobacion(){
	document.forms[0].action = '/aprobacion/cliente/historial' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
	
}
//Adicionar un Servicio
function jsAdicionarServicioAprobacion(){
	document.forms[0].action = '/aprobacion/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}

//Adiciona observacion
function jsAdicionarObservacionAprobacion(){
	document.forms[0].action = '/aprobacion/observacion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
//Elimina un servicio
function jsEliminarServicioAprobacion(indice){
	document.getElementById('idIndice').value = indice;
	document.forms[0].action = '/aprobacion/eliminar/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}

/* *********************************************************************************************************************************
 * MODULO DE SUMINISTRO x TIENDA
 ********************************************************************************************************************************* */
function jsCargarSuministroSuministroxInmueble(){
	document.forms[0].action = '/suministroxinmueble/suministro' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}

function jsCargarSuministroNuevoSuministroxInmueble(){
	document.forms[0].action = '/suministroxinmueble/suministro/c' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}

function jsGenerarRegistroCobro(){
	document.getElementById('idBotonGenerar').style.display = 'none';
	document.forms[0].action = '/cliente/suministroxinmueble/generarcobro' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsEliminarRegistro(){
	if (confirm('Esta seguro de eliminar los registros')){
		document.getElementById('idBotonGenerar').style.display = 'none';
		document.forms[0].action = '/cliente/suministroxinmueble/eliminar' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();
	}
	
}
/* *********************************************************************************************************************************
 * MODULO DE COBRO
 ********************************************************************************************************************************* */
function jsCalcularAlquilerDolares(){
	var montoSoles = document.getElementById('idMontoSoleAlquiler').value;
	var tipoCambio = document.getElementById('idTipoCambioAlquiler').value;
	document.getElementById('idMontoDolarAlquiler').value = montoSoles / tipoCambio;	
}
function jsAdicionarCobroAlquiler(){
	document.forms[0].action = '/cobro/alquiler' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsCalcularServicioDolares(){
	var montoSoles = document.getElementById('idMontoSoleServicio').value;
	var tipoCambio = document.getElementById('idTipoCambioServicio').value;
	document.getElementById('idMontoDolarServicio').value = montoSoles / tipoCambio;	
}
function jsAdicionarCobroServicio(){
	document.forms[0].action = '/cobro/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsCalcularLuzDolares(){
	var montoSoles = document.getElementById('idMontoSoleLuz').value;
	var tipoCambio = document.getElementById('idTipoCambioLuz').value;
	document.getElementById('idMontoDolarLuz').value = montoSoles / tipoCambio;	
}
function jsOcultarDivTipoCambioAlquiler(){
	var objDiv = document.getElementById('idAlquilerDivTipoCambio');
	objDiv.style.display = 'none';
	objDiv = document.getElementById('idAlquilerDivCalculado');
	objDiv.style.display = 'none';
}
function jsMostrarDivTipoCambioAlquiler(){
	var objDiv = document.getElementById('idAlquilerDivTipoCambio');
	objDiv.style.display = '';
	objDiv = document.getElementById('idAlquilerDivCalculado');
	objDiv.style.display = '';
}

function jsCalcularMontoAlTipoCambioAlquiler(idValor, idMoneda, idValorCambio, idTipoCambio, idMensaje, idSaldo, idMes){
	try{
		var monedaAlquilerContrato = document.getElementById('idContratoAlqTipoMoneda').value;
		if (monedaAlquilerContrato == document.getElementById(idMoneda).value){
			jsOcultarDivTipoCambioAlquiler();
			document.getElementById(idTipoCambio).value = '0';
		}else{
			jsMostrarDivTipoCambioAlquiler();
		}
		
		var montoComparacion;
		var tipoCambio = document.getElementById(idTipoCambio).value;
		var monto = document.getElementById(idValor).value;
		
		if (document.getElementById(idMoneda).value =='DO'){
			document.getElementById(idValorCambio).value = monto * tipoCambio;
			montoComparacion = monto;
		}else if (document.getElementById(idMoneda).value =='SO'){
			if (tipoCambio > 0 || tipoCambio <0) {
				document.getElementById(idValorCambio).value = (monto / tipoCambio).toFixed(2);
				montoComparacion = (monto / tipoCambio).toFixed(2);
			}else{
				document.getElementById(idValorCambio).value = '0';
			}
			
		}else{
			document.getElementById(idValorCambio).value = '0';
			montoComparacion = 0;
		}
		//Calculo del mensaje de alquiler
		var montoMes = document.getElementById(idSaldo).value;
		if (montoMes == 0) {
			//No se encontro lista de deuda
			document.getElementById(idMensaje).innerHTML = 'Adelanto del mes '+document.getElementById(idMes).value;
		}else{
			if (montoComparacion == 0){
				//No se ingreso ningun monto que se este cobrano
				document.getElementById(idMensaje).innerHTML = '';
			}else{
				if (Number(montoComparacion) >= Number(montoMes) ){
					//El monto ingresado es mayor al saldo a cobrar
					document.getElementById(idMensaje).innerHTML = 'Cancelo el mes '+document.getElementById(idMes).value;
				
				}else{
					if (Number(montoMes) - Number(montoComparacion) <= 1){
						//El monto ingresado es menor al saldo a cobrar pero la diferencia es menor a 1 dolar
						document.getElementById(idMensaje).innerHTML = 'Cancelo el mes '+document.getElementById(idMes).value;
					}else{
						document.getElementById(idMensaje).innerHTML = 'Adelanto del mes '+document.getElementById(idMes).value;
					}
							
					
				}
			}
		}
		
		//Mensaje
		document.getElementById('idMsgCobroAlquiler').value = document.getElementById(idMensaje).innerHTML;
		
	}catch(err){
		document.getElementById(idValorCambio).value = '0';
	}
	
	
}
function jsOcultarDivTipoCambioServicio(){
	var objDiv = document.getElementById('idServicioDivTipoCambio');
	objDiv.style.display = 'none';
	objDiv = document.getElementById('idServicioDivCalculado');
	objDiv.style.display = 'none';
}
function jsMostrarDivTipoCambioServicio(){
	var objDiv = document.getElementById('idServicioDivTipoCambio');
	objDiv.style.display = '';
	objDiv = document.getElementById('idServicioDivCalculado');
	objDiv.style.display = '';
}
function jsCalcularMontoAlTipoCambioServicio(idValor, idMoneda, idValorCambio, idTipoCambio, idMensaje, idSaldo, idMes){
	try{
		
		if ('SO' == document.getElementById(idMoneda).value){
			jsOcultarDivTipoCambioServicio();
			document.getElementById(idTipoCambio).value = '0';
		}else{
			jsMostrarDivTipoCambioServicio();
		}
		
		var montoComparacion;
		var tipoCambio = document.getElementById(idTipoCambio).value;
		var monto = document.getElementById(idValor).value;
		if (document.getElementById(idMoneda).value =='DO'){
			document.getElementById(idValorCambio).value = monto * tipoCambio;
			montoComparacion = monto * tipoCambio;
		}else if (document.getElementById(idMoneda).value =='SO'){
			if (tipoCambio > 0 || tipoCambio <0) {
				document.getElementById(idValorCambio).value = (monto / tipoCambio).toFixed(2);
				montoComparacion = monto;
			}else{
				document.getElementById(idValorCambio).value = '0';
				montoComparacion = monto;
			}
			
		}else{
			document.getElementById(idValorCambio).value = '0';
			montoComparacion = 0;
		}
		//Calculo del mensaje de servicio
		var montoMes = document.getElementById(idSaldo).value;
		if (montoMes == 0) {
			//No se encontro lista de deuda
			document.getElementById(idMensaje).innerHTML = 'Adelanto del mes '+document.getElementById(idMes).value;
		}else{
			if (montoComparacion == 0){
				//No se ingreso ningun monto que se este cobrano
				document.getElementById(idMensaje).innerHTML = '';
			}else{
				if (Number(montoComparacion) >= Number(montoMes) ){
					//El monto ingresado es mayor al saldo a cobrar
					document.getElementById(idMensaje).innerHTML = 'Cancelo el mes '+document.getElementById(idMes).value;
				
				}else{
					if (Number(montoMes) <= Number(montoComparacion) ){
						//El monto ingresado es menor al saldo a cobrar 
						document.getElementById(idMensaje).innerHTML = 'Cancelo el mes '+document.getElementById(idMes).value;
					}else{
						document.getElementById(idMensaje).innerHTML = 'Adelanto del mes '+document.getElementById(idMes).value;
					}
							
					
				}
			}
		}
		
		//Mensaje
		document.getElementById('idMsgCobroServicio').value = document.getElementById(idMensaje).innerHTML;
		
	}catch(err){
		document.getElementById(idValorCambio).value = '0';
	}
	
	
}

function jsCalcularMontoAlTipoCambioLuz(idValor, idMoneda, idValorCambio, idTipoCambio, idMensaje, idSaldo, idMes){
	try{
		var montoComparacion;
		var tipoCambio = document.getElementById(idTipoCambio).value;
		var monto = document.getElementById(idValor).value;
		if (document.getElementById(idMoneda).value =='DO'){
			document.getElementById(idValorCambio).value = monto * tipoCambio;
			montoComparacion = monto * tipoCambio;
		}else if (document.getElementById(idMoneda).value =='SO'){
			if (tipoCambio > 0 || tipoCambio <0) {
				document.getElementById(idValorCambio).value = (monto / tipoCambio).toFixed(2);
				montoComparacion = monto;
			}else{
				document.getElementById(idValorCambio).value = '0';
				montoComparacion = monto;
			}
			
		}else{
			document.getElementById(idValorCambio).value = '0';
			montoComparacion = 0;
		}
		//Calculo del mensaje de servicio
		var montoMes = document.getElementById(idSaldo).value;
		if (montoMes == 0) {
			//No se encontro lista de deuda
			document.getElementById(idMensaje).innerHTML = 'Adelanto del mes '+document.getElementById(idMes).value;
		}else{
			if (montoComparacion == 0){
				//No se ingreso ningun monto que se este cobrano
				document.getElementById(idMensaje).innerHTML = '';
			}else{
				if (Number(montoComparacion) >= Number(montoMes) ){
					//El monto ingresado es mayor al saldo a cobrar
					document.getElementById(idMensaje).innerHTML = 'Cancelo el mes '+document.getElementById(idMes).value;
				
				}else{
					if (Number(montoMes) <= Number(montoComparacion) ){
						//El monto ingresado es menor al saldo a cobrar 
						document.getElementById(idMensaje).innerHTML = 'Cancelo el mes '+document.getElementById(idMes).value;
					}else{
						document.getElementById(idMensaje).innerHTML = 'Adelanto del mes '+document.getElementById(idMes).value;
					}
							
					
				}
			}
		}
		
		//Mensaje
		document.getElementById('idMsgCobroLuz').value = document.getElementById(idMensaje).innerHTML;
		
		
	}catch(err){
		document.getElementById(idValorCambio).value = '0';
	}
	
	
}


function jsCalcularMontoAlTipoCambioArbitrio(idValor, idMoneda, idValorCambio, idTipoCambio, idMensaje, idSaldo, idMes){

	try{
		var montoComparacion;
		var tipoCambio = document.getElementById(idTipoCambio).value;
		var monto = document.getElementById(idValor).value;
		if (document.getElementById(idMoneda).value =='DO'){
			document.getElementById(idValorCambio).value = monto * tipoCambio;
			montoComparacion = monto * tipoCambio;
		}else if (document.getElementById(idMoneda).value =='SO'){
			if (tipoCambio > 0 || tipoCambio <0) {
				document.getElementById(idValorCambio).value = (monto / tipoCambio).toFixed(2);
				montoComparacion = monto;
			}else{
				document.getElementById(idValorCambio).value = '0';
				montoComparacion = monto;
			}
			
		}else{
			document.getElementById(idValorCambio).value = '0';
			montoComparacion = 0;
		}
		//Calculo del mensaje de servicio
		var montoMes = document.getElementById(idSaldo).value;
		if (montoMes == 0) {
			//No se encontro lista de deuda
			document.getElementById(idMensaje).innerHTML = 'Adelanto del periodo: '+document.getElementById(idMes).value;
		}else{
			if (montoComparacion == 0){
				//No se ingreso ningun monto que se este cobrano
				document.getElementById(idMensaje).innerHTML = '';
			}else{
				if (Number(montoComparacion) >= Number(montoMes) ){
					//El monto ingresado es mayor al saldo a cobrar
					document.getElementById(idMensaje).innerHTML = 'Cancelo el periodo: '+document.getElementById(idMes).value;
				
				}else{
					if (Number(montoMes) <= Number(montoComparacion) ){
						//El monto ingresado es menor al saldo a cobrar 
						document.getElementById(idMensaje).innerHTML = 'Cancelo el periodo: '+document.getElementById(idMes).value;
					}else{
						document.getElementById(idMensaje).innerHTML = 'Adelanto del periodo: '+document.getElementById(idMes).value;
					}
							
					
				}
			}
		}
		
		//Mensaje
		document.getElementById('idMsgCobroArbitrio').value = document.getElementById(idMensaje).innerHTML;
		
		
	}catch(err){
		alert(err);
		document.getElementById(idValorCambio).value = '0';
	}
	
	
}
function jsAdicionarCobroPrimerCobro(){
	document.forms[0].action = '/cobro/primerCobro' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//registra un cobro de primeros cobros
function jsRegistrarCobroCxCIndividual(){
	document.forms[0].action = '/cobro/primerCobro/individual' ;
	//Asignando los valores
	document.getElementsByName('intCodigoPrimerCobro')[0].value = document.getElementsByName('intCodigoPrimerCobro')[1].value;
	document.getElementsByName('fechaCobroPrimerCobro')[0].value = document.getElementsByName('fechaCobroPrimerCobro')[1].value;
	document.getElementsByName('tipoMonedaPrimerCobro')[0].value = document.getElementsByName('tipoMonedaPrimerCobro')[1].value;
	document.getElementsByName('montoSolesPrimerCobro')[0].value = document.getElementsByName('montoSolesPrimerCobro')[1].value;
	document.getElementsByName('tipoCambioPrimerCobro')[0].value = document.getElementsByName('tipoCambioPrimerCobro')[1].value;
	document.getElementsByName('montoDolaresPrimerCobro')[0].value = document.getElementsByName('montoDolaresPrimerCobro')[1].value;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
	
}
function jsAdicionarCobroGarantia(){
	document.forms[0].action = '/cobro/garantia' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsAdicionarCobroLuz(){
	document.forms[0].action = '/cobro/luz' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsAdicionarCobroArbitrio(){
	document.forms[0].action = '/cobro/arbitrio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsRegresarCobro(){
	document.forms[0].action = '/cobro/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}

function jsRegresarCobroGet(){
	document.forms[0].action = '/cobro/edicion/regresar' ;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsRegresarCobroAdelanto(){
	document.forms[0].action = '/cobro/adelanto/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsAdicionarObservacionCobro(){
	document.forms[0].action = '/cobro/observacion/edicion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
function jsRegresarDesembolso(){
	document.forms[0].action = '/cobro/desembolso/detalle/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsValidarSolicitudReversion(){
	var motivo = document.getElementById('idMotivo').value;
	if (motivo != ''){
		if(confirm('Esta seguro de registrar la Solicitud de Reversión?')){ 	
			jsRegistrarSolicitudReversion();
		}else{
			return false;
		}
	}else{
		alert('Debe ingresar un motivo para realizar el registro');
		return false;
	}
}
function jsRegistrarSolicitudReversion(){
	document.forms[0].action = '/cobro/desembolso/detalle/reversar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}

function jsRealizarCobrodeAdelanto(){
	if(confirm('Esta seguro de aplicar el cobro?')){ 	
		document.forms[0].action = '/cobro/adelanto/aplicar' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();
	}else{
		return false;
	}
}
function jsRealizarCobrodeGarantia(){
	if(confirm('Esta seguro de aplicar el cobro?')){ 	
		document.forms[0].action = '/cobro/garantia/aplicar' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();
	}else{
		return false;
	}
}
function jsRegistrarCobroCxC(){
	/*if(confirm('Esta seguro de registrar el cobro?')){ 	
		document.forms[0].action = '/cobro/editar/guardar' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();
	}else{
		return false;
	}*/
	var montoAlquiler = document.getElementById('idMontoDolarAlquiler').value;
	var montoServicio = document.getElementById('idMontoDolarServicio').value;
	var montoLuz = document.getElementById('idMontoDolarLuz').value;
	/*if (montoAlquiler == '' && montoServicio == '' && montoLuz == '' ){
		alert('Debe ingresar el monto de cobro para poder continuar.');
	}else{
		if (Number(montoAlquiler) <= 0 && Number(montoServicio) <= 0 && Number(montoLuz) <= 0 ){
			alert('Debe ingresar el monto de cobro para poder continuar.');
		}else{*/
			
				document.forms[0].action = '/cobro/editar/previo' ;
				document.forms[0].method = 'POST';
				document.forms[0].submit();
			
		/*}*/
		
	//}
}

function jsRegistrarCobroLocal(){
	if (confirm('Esta seguro de registrar')){
		document.forms[0].action = '/cobro/editar/guardar' ;
		document.forms[0].method = 'GET';
		document.forms[0].submit();
	}
}
function jsHistorialCobroCxC(){
	document.forms[0].action = '/cobro/historial/alquiler';
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}

function jsMostrarBancarizado(objSel, idBancarizado, idNumero, idFecha){

	if (objSel.value == 'EFE' || objSel.value == '-1'){
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = 'none';
		objDiv = document.getElementById(idNumero);
		objDiv.style.display = 'none';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = 'none';
	}else{
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = '';
		objDiv = document.getElementById(idNumero);
		objDiv.style.display = '';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = '';
	}
}

function jsValidarTipoPago(objSel, idDiv){
	if (objSel.value == 'EFE' || objSel.value == '-1'){
		var objDiv = document.getElementById(idDiv);
		objDiv.style.display = '';
		
	}else{
		var objDiv = document.getElementById(idDiv);
		objDiv.style.display = 'none';
	}
}
function jsValidarTipoPagoEfectivo(nameObj, idDiv){
	var objSel = document.getElementById(nameObj);
	jsValidarTipoPago(objSel, idDiv)
	
}
function jsMostrarBancarizadoServicio(objSel, idBancarizado, idNumero, idFecha){
	if (objSel.value == 'EFE'|| objSel.value == '-1'){
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = 'none';
		objDiv = document.getElementById(idNumero);
		objDiv.style.display = 'none';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = 'none';
	}else{
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = '';
		objDiv = document.getElementById(idNumero);
		objDiv.style.display = '';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = '';
	}
}
function jsMostrarBancarizadoLuz(objSel, idBancarizado, idNumero, idFecha){
	if (objSel.value == 'EFE'|| objSel.value == '-1'){
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = 'none';
		objDiv = document.getElementById(idNumero);
		objDiv.style.display = 'none';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = 'none';
	}else{
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = '';
		objDiv = document.getElementById(idNumero);
		objDiv.style.display = '';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = '';
	}
}
function jsInicializaBancarizado(){
	var objSel 			= document.getElementById('idTipoPago');
	var objSelServicio 	= document.getElementById('idTipoPagoServicio');
	var objSelLuz 		= document.getElementById('idTipoPagoLuz');
	jsMostrarBancarizado(objSel,'idDivTipoOperacion','idDivNumeroOperacion','idDivFechaOperacion');
	jsMostrarBancarizadoServicio(objSelServicio,'idDivTipoOperacionServicio','idDivNumeroOperacionServicio','idDivFechaOperacionServicio');
	jsMostrarBancarizadoLuz(objSelLuz,'idDivTipoOperacionLuz','idDivNumeroOperacionLuz','idDivFechaOperacionLuz');
	
}
/* *********************************************************************************************************************************
 * MODULO DE APROBACION O RECHAZO DE REVERSION
 ********************************************************************************************************************************* */
function jsRegresarReversion(){
	document.forms[0].action = '/reversion/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsRechazarReversion(){
	if (confirm('Esta seguro de rechazar la Solicitud de Reversión?')){
		document.forms[0].action = '/reversion/rechazar' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();
	}else{
		return false;
	}
}
function jsAprobarReversion(){
	if (confirm('Esta seguro de aprobar la Solicitud de Reversión?')){
		document.forms[0].action = '/reversion/aceptar' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();
	}else{
		return false;
	}
}
/* *********************************************************************************************************************************
 * MODULO DE FINALIZACION
 ********************************************************************************************************************************* */
function jsAdicionarObservacionFinalizacions(){
	document.forms[0].action = '/finalizacion/observacion/edicion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
function jsRegresarFinalizacion(){
	document.forms[0].action = '/finalizacion/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsFinalizarContrato(){
	document.forms[0].action = '/finalizacion/editar/guardar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsObtenerDatosContratoFinalizacion(codigoContrato){
	document.forms[0].action = '/finalizacion/editar/'+codigoContrato;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
//Adicionar Observacion
function jsAdicionarObservacionFinalizacionContrato(){
	document.getElementById('idObservacionFinalizacionEdicion').disabled = true;
	document.forms[0].action = '/finalizacion/observacion/edicion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}

/* *********************************************************************************************************************************
 * MODULO DE RENOVACION
 ********************************************************************************************************************************* */


//Busqueda de Clientes
function jsListarClienteRenovarContrato(){
	document.getElementById('idClienteRenovacionEdicion').disabled = true;
	document.forms[0].action = '/renovarcontrato/clientes' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Historial de Clientes
function jsHistorialClienteRenovarContrato(){
	document.forms[0].action = '/renovarcontrato/cliente/historial' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Retorna al modulo de renovars en el contrato 
function jsRegresarRenovarContrato(){
	document.forms[0].action = '/renovarcontrato/regresar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Adiciona un servicio en la lista
function jsAdicionarServicioRenovarContrato(){
	//validar los campos
	document.getElementById('idServicioRenovacionEdicion').disabled = true;
	document.forms[0].action = '/renovarcontrato/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Adicionar Observacion
function jsAdicionarObservacionRenovarContrato(){
	document.getElementById('idObservacionRenovacionEdicion').disabled = true;
	document.forms[0].action = '/renovarcontrato/observacion/edicion' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
//Actualiza los datos del contrato
function jsActualizarContratoRenovarContrato(){
	document.getElementById('idActualizarRenovarEdicion').disabled = true;
	document.forms[0].action = '/renovarcontrato/editar/guardar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
//Elimina un servicio
function jsEliminarServicioRenovarContrato(indice){
	document.getElementById('idIndice').value = indice;
	document.forms[0].action = '/renovarcontrato/eliminar/servicio' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
//Editar el contrato para renovar
function jsObtenerDatosContratoRenotacion(codigoContrato){
	document.forms[0].action = '/renovarcontrato/editar/'+codigoContrato;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}

function jsObtenerDatosClientesRenovarContrato(codigoCliente){
	document.forms[0].action = '/renovarcontrato/clientes/seleccionar/'+codigoCliente;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
/* *********************************************************************************************************************************
 * MODULO DE CAJA CHICA
 ********************************************************************************************************************************* */

function jsCargarConcepto(){
	 //display the spinner
	   $('#ajaxLoader').show();
	 
	   //first, let's get rid of the default "SELECT" option if it exists
	   var defaultOption = $("#tipoOperacion option[value='-1']");
	   if (defaultOption) defaultOption.remove();
	 
	   //get the selected id
	   var tipoOperacionId = $('#tipoOperacion').val();
	 
	   //get the url for the ajax call
	   var url = "./concepto/" + tipoOperacionId;
	 
	   //do the ajax call
	   $.get(url, populateConceptoInfo);
	
}

function populateConceptoInfo(data) {
   var status = data.responseStatus;
 
   //check the response to make sure it's ok
   if (status == "Ok") {
      var response = data.response;
 
      alert("ok");
   }
 
   //hide the spinner again
   $('#ajaxLoader').hide();
}


function jsRegistrarIngreso(){
	document.forms[0].action = '/caja/ccdetalle/nuevo/ingreso/' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
function jsMostrarBancarizadoIngreso(objSel, idBancarizado, idFecha){

	if (objSel.value == 'EFE' || objSel.value == '-1'){
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = 'none';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = 'none';
	}else{
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = '';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = '';
	}
}
function jsSetFocusIngreso(idBancarizado, idFecha){
	document.getElementById('idMontoIngreso').focus();

	if (document.getElementById('idTipoPago').value == 'EFE'){
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = 'none';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = 'none';
	}else{
		var objDiv = document.getElementById(idBancarizado);
		objDiv.style.display = '';
		objDiv = document.getElementById(idFecha);
		objDiv.style.display = '';
	}
	
}
function jsRegistrarGasto(){
	document.forms[0].action = '/caja/ccdetalle/nuevo/gasto/' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
function jsAsignarIdCobro (codigoPrimerCobro){
	document.getElementsByName("intCodigoPrimerCobro")[0]. value = codigoPrimerCobro;
	document.getElementsByName("intCodigoPrimerCobro")[1]. value = codigoPrimerCobro;
}
function clearAllForm(frm_elements){
	for (i = 0; i < frm_elements.length; i++)
	{
	    field_type = frm_elements[i].type.toLowerCase();
	    //alert(field_type);
	    switch (field_type)
	    {
	    case "text":
	    case "password":
	    case "textarea":
	    case "hidden":
	        frm_elements[i].value = "";
	        break;
	    case "radio":
	    case "checkbox":
	        if (frm_elements[i].checked)
	        {
	            frm_elements[i].checked = false;
	        }
	        break;
	    case "select":
	    case "select-one":
	    case "select-multi":
	        frm_elements[i].selectedIndex = 0;
	        break;
	    default:
	        break;
	    }
	}

}


/* *********************************************************************************************************************************
 * MODULO DE CONTRATOS - ACCESOS RAPIDOS
 ********************************************************************************************************************************* */
function jsObtenerDatosLocales(codigoLocal){
	document.forms[0].action = '/contrato/locales/seleccionar/'+codigoLocal;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsObtenerDatosClientes(codigoCliente){
	document.forms[0].action = '/contrato/clientes/seleccionar/'+codigoCliente;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}

function jsObtenerDatosContrato(codigoContrato){
	document.forms[0].action = '/contrato/editar/'+codigoContrato;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsObtenerDatosContratoCambio(codigoContrato){
	document.forms[0].action = '/cambiocontrato/editar/'+codigoContrato;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsObtenerDatosContratoCobro(codigoContrato){
	document.forms[0].action = '/cobro/editar/'+codigoContrato;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}

function jsObtenerDatosAdelantos(){
	codigoContrato = document.getElementById('idContrato').value;
	document.forms[0].action = '/cobro/historial/adelanto/'+codigoContrato;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}

function jsSetFocusLocales(){
	document.getElementById('idLocal').focus();
}
function jsEliminarContrato(idContrato){
	if(confirm('Desea eliminar el contrato seleccionado?')){
		document.getElementById('idEliminarContratoEdicion').disabled = true;
		document.forms[0].action = '/contrato/eliminar/'+idContrato;
		document.forms[0].method = 'GET';
		document.forms[0].submit();
	}
}
function jsSetFocusCobro(){
	document.getElementById('idLocal').focus();
}
function jsSetFocusTipoCambioNuevo(){
	document.getElementById('idValor').focus();
}
function jsSetFocusCobroEditar(){
	document.getElementById('idMontoSoleAlquiler').focus();
	
}
function jsSetFocusGasto(){
	document.getElementById('idMontoGasto').focus();
}
/* *********************************************************************************************************************************
 * MODULO DE REFINANCIACION
 ********************************************************************************************************************************* */


function jsObtenerDatosContratoRefinanciacion(codigoContrato){
	document.forms[0].action = '/refinanciar/editar/'+codigoContrato;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}

function jsObtenerMontoAlquilerRefinanciacion(indice){
	document.forms[0].action = '/refinanciar/editar/alquiler/monto/'+indice;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsAsignarSaldoAlquiler(obj){
	var montoAlquiler = document.getElementById('idNuevoMontoAlquiler').value;
	 document.getElementById('idNuevoSaldoAlquiler').value = montoAlquiler;
}
function jsCancelarRefinanciarAlquiler(){
	document.forms[0].action = '/refinanciar/editar/alquiler/cancelar';
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsRegistrarRefinanciarAlquiler(){
	if (confirm('Esta seguro de registrar')){
		document.forms[0].action = '/refinanciar/editar/alquiler/grabar/' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();	
	}
}


function jsObtenerMontoServicioRefinanciacion(indice){
	document.forms[0].action = '/refinanciar/editar/servicio/monto/'+indice;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsAsignarSaldoServicio(obj){
	var montoAlquiler = document.getElementById('idNuevoMontoServicio').value;
	 document.getElementById('idNuevoSaldoServicio').value = montoAlquiler;
}
function jsCancelarRefinanciarServicio(){
	document.forms[0].action = '/refinanciar/editar/servicio/cancelar';
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsRegistrarRefinanciarServicio(){
	if (confirm('Esta seguro de registrar')){
		document.forms[0].action = '/refinanciar/editar/servicio/grabar/' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();	
	}
}

function jsObtenerMontoLuzRefinanciacion(indice){
	document.forms[0].action = '/refinanciar/editar/luz/monto/'+indice;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsAsignarSaldoLuz(obj){
	var montoAlquiler = document.getElementById('idNuevoMontoLuz').value;
	 document.getElementById('idNuevoSaldoLuz').value = montoAlquiler;
}
function jsCancelarRefinanciarLuz(){
	document.forms[0].action = '/refinanciar/editar/luz/cancelar';
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsRegistrarRefinanciarLuz(){
	if (confirm('Esta seguro de registrar')){
		document.forms[0].action = '/refinanciar/editar/luz/grabar/' ;
		document.forms[0].method = 'POST';
		document.forms[0].submit();	
	}
}


/* *********************************************************************************************************************************
 * MODULO DE GASTO
 ********************************************************************************************************************************* */


function jsObtenerDatosGasto(codigoGasto){
	document.forms[0].action = '/gasto/editar/'+codigoGasto;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsEliminarDatosGasto(codigoGasto){
	if(confirm('Desea eliminar el Gasto seleccionado?')){
		document.getElementById('idEliminarGastoEdicion').style.visibility = 'hidden';
		document.forms[0].action = '/gasto/eliminar/'+codigoGasto;
		document.forms[0].method = 'GET';
		document.forms[0].submit();
	}
}
/* *********************************************************************************************************************************
 * MODULO DE INGRESO
 ********************************************************************************************************************************* */


function jsObtenerDatosIngreso(codigoIngreso){
	document.forms[0].action = '/ingreso/editar/'+codigoIngreso;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}

function jsEliminarDatosIngreso(codigoIngreso){
	if(confirm('Desea eliminar el Ingreso seleccionado?')){
		document.getElementById('idEliminarIngresoEdicion').style.visibility = 'hidden';
		document.forms[0].action = '/ingreso/eliminar/'+codigoIngreso;
		document.forms[0].method = 'GET';
		document.forms[0].submit();
	}
}

/* *********************************************************************************************************************************
 * MODULO DEL FACTURADOR
 ********************************************************************************************************************************* */

function jsGenerarComprobante(){
	document.forms[0].action = '/comprobante/generar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
	
}


/* *********************************************************************************************************************************
 * Control de los botones
 ********************************************************************************************************************************* */
function jsDeshabilitaBoton(obj){
	obj.style.display = 'none';
	//alert('ocultado');
}
function jsMostrarContrato(){
	document.forms[0].action = '/contratos' ;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsMostrarCobro(){
	document.forms[0].action = '/cobro' ;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsMostrarGasto(){
	document.forms[0].action = '/gastos' ;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsMostrarIngreso(){
	document.forms[0].action = '/ingresos' ;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}
function jsReporteIngresoEgreso(){
	document.forms[0].action = '/ingresoegresos' ;
	document.forms[0].method = 'GET';
	document.forms[0].submit();
}


/* *********************************************************************************************************************************
 * Luz x suministro
 ********************************************************************************************************************************* */
function jsCalcularMontoxCobrar(objMonto, idRecibo, idPorCobrar, idSinTienda){
	if (objMonto.value != '' && ! isNaN(objMonto.value)){
		document.getElementById(idPorCobrar).value = parseFloat(document.getElementById(idRecibo).value).toFixed(1) -  parseFloat(document.getElementById(idSinTienda).value).toFixed(1) - parseFloat(objMonto.value).toFixed(1);
		document.getElementById(idPorCobrar).value = parseFloat(document.getElementById(idPorCobrar).value).toFixed(1);
		if (document.getElementById(idPorCobrar).value == '-0.0'){
			document.getElementById(idPorCobrar).value = "0.0";
		}		
		
	}
}

/* *********************************************************************************************************************************
 * BUG Arbitrios
 ********************************************************************************************************************************* */

function jsEjecutarActualizacionFecha(){
	var x;
    var name=prompt("Proceso de validación de fecha, ingrese clave del Super Usuario","...");
    if (name!=null && name == "DESTINY"){
    	document.forms[0].action = '/cliente/arbitrios/fecha' ;
    	document.forms[0].method = 'POST';
    	document.forms[0].submit();	
   }
}
/* *********************************************************************************************************************************
 * Modulo de Factura: Alquiler y Servicio
 ********************************************************************************************************************************* */
function jsListarProducto(){
	document.forms[0].action = '/facturas/consulta' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}
function jsAgregarPagoCredito(){
	document.forms[0].action = '/facturas/formapago/agregar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsMostrarFormaPagoDatos(obj){
	document.forms[0].action = '/facturas/formapago/mostrar' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}

function jsLimpiarCriterios(){
	clearAllForm(document.getElementById("form"))
}
/* *********************************************************************************************************************************
 * PERSONA: Ubigeo
 ********************************************************************************************************************************* */

function jsCargarProvinciaInei(obj){
	document.forms[0].action = '/persona/provincia' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
function jsCargarDistritoInei(obj){
	document.forms[0].action = '/persona/distrito' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();
}
/* *********************************************************************************************************************************
 * Modulo de Notas de Credito
 ********************************************************************************************************************************* */
function jsListarFacturas(){
	document.forms[0].action = '/notas/consulta' ;
	document.forms[0].method = 'POST';
	document.forms[0].submit();	
}



