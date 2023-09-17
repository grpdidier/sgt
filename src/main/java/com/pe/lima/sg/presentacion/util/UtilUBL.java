package com.pe.lima.sg.presentacion.util;

import java.util.ArrayList;
import java.util.List;

import com.pe.lima.sg.bean.Ubl.TagUbl;
import com.pe.lima.sg.bean.Ubl.TagUblCredito;
import com.pe.lima.sg.bean.caja.FacturaBean;
import com.pe.lima.sg.entity.caja.TblDetalleFormaPago;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class UtilUBL {
	
	
	
	public static List<TagUbl> nodoUblHeader(FacturaBean facturaBean) {
		List<TagUbl> lista = new ArrayList<>();
		/*Datos de la factura electronica*/
		asignarDatosFactura(lista, facturaBean);
		/*Signature del comprobante*/
		asignarFirma(lista);
		/*Datos del emisor del comprobante*/
		asignarDatosEmisorComprobante(lista, facturaBean);
		/*Datos del receptor del comprobante*/
		asignarDatosReceptorComprobante(lista,facturaBean);
		/*Datos para la detracción*/
		if (facturaBean.getFactura().getTipoOperacion().equals(Constantes.TIPO_OPERACION_DETRACCION_CODIGO)) {
			asignarDatosDetraccion(lista,facturaBean);
		}
		/*Forma de pago del comprobante (Mandatorio)*/
		asignarDatosFormaDePago(lista,facturaBean);
		/*Totales de la Factura*/
		asignarDatosTotalImpuesto(lista,facturaBean);
		return lista;
	}
	public static List<TagUbl> nodoUblDetail(FacturaBean facturaBean){
		List<TagUbl> lista = new ArrayList<>();
		/*Datos del detalle o ítem de la factura*/
		asignarDatosDetalleItemFactura(lista,facturaBean);
		/*Total de tributos del item*/
		asignarDatosTributoItemFactura(lista,facturaBean);
		/*Monto IGV o IVAP del ítem*/
		asignarDatosMontosIGVItemFactura(lista,facturaBean);
		/*Descripción del ítem*/
		asignarDatosDescripcionItemFactura(lista,facturaBean);
		
		return lista;
	}
	/*I. Datos del detalle o ítem de la factura (Mandatorio)*/
	private static void asignarDatosDetalleItemFactura(List<TagUbl> lista, FacturaBean facturaBean) {
		/*- Número de orden del Ítem*/
		/*M*/lista.add(setDatos("InvoiceLine/ID","1"));
		/*Cantidad y unidad de medida del ítem*/
		lista.add(setDatos("InvoiceLine/Note","UNIDAD"));
		/*M*/lista.add(setDatos("InvoiceLine/InvoicedQuantity","1"));
		/*M*/lista.add(setDatos("InvoiceLine/InvoicedQuantity/@unitCode",Constantes.SUNAT_UNIDAD_MEDIDA));
		/*C*/lista.add(setDatos("InvoiceLine/InvoicedQuantity/@unitCodeListAgencyName","United Nations Economic Commission for Europe"));
		/*C*/lista.add(setDatos("InvoiceLine/InvoicedQuantity/@unitCodeListID","UN/ECE rec 20"));
		/*- Valor de venta del ítem*/
		/*M*/lista.add(setDatos("InvoiceLine/LineExtensionAmount",facturaBean.getFacturaDetalle().getPrecioUnitario().toString()));
		/*M*/lista.add(setDatos("InvoiceLine/LineExtensionAmount/@currencyID",facturaBean.getFacturaDetalle().getMoneda()));
		/*- Precio de venta unitario del ítem*/
		/*M*/lista.add(setDatos("InvoiceLine/PricingReference/AlternativeConditionPrice/PriceAmount",facturaBean.getFacturaDetalle().getPrecioVentaUnitario().toString()));
		/*M*/lista.add(setDatos("InvoiceLine/PricingReference/AlternativeConditionPrice/PriceAmount/@currencyID",facturaBean.getFacturaDetalle().getMoneda()));
		/*M*/lista.add(setDatos("InvoiceLine/PricingReference/AlternativeConditionPrice/PriceTypeCode",Constantes.CODIGO_PRECIO_UNITARIO_INCLUYE_IGV));
		/*C*/lista.add(setDatos("InvoiceLine/PricingReference/AlternativeConditionPrice/PriceTypeCode/@listName","Tipo de Precio"));
		/*C*/lista.add(setDatos("InvoiceLine/PricingReference/AlternativeConditionPrice/PriceTypeCode/@listAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("InvoiceLine/PricingReference/AlternativeConditionPrice/PriceTypeCode/@listURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo16"));
		
		
	}
	/*III. Total de tributos del item*/
	private static void asignarDatosTributoItemFactura(List<TagUbl> lista, FacturaBean facturaBean) {
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxAmount",facturaBean.getFacturaDetalle().getValorIgv().toString()));
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxAmount/@currencyID",facturaBean.getFacturaDetalle().getMoneda()));
	}
	/*IV. Monto IGV o IVAP del ítem (Mandatorio)*/
	private static void asignarDatosMontosIGVItemFactura(List<TagUbl> lista, FacturaBean facturaBean) {
		/*Monto base IGV*/
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxableAmount",facturaBean.getFacturaDetalle().getPrecioUnitario().toString()));
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxableAmount/@currencyID",facturaBean.getFacturaDetalle().getMoneda()));
		/*Monto del IGV*/
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxAmount",facturaBean.getFacturaDetalle().getValorIgv().toString()));
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxAmount/@currencyID",facturaBean.getFacturaDetalle().getMoneda()));
		/*Porcentaje IGV */
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/Percent",Constantes.SUNAT_IGV.toString()));
		/*Afectación IGV o IVAP del item*/
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxExemptionReasonCode",Constantes.SUNAT_AFECTACION_IGV_GRAVADO_OPE_ONEROSO));
		/*C*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxExemptionReasonCode/@listAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxExemptionReasonCode/@listName","Afectacion del IGV"));
		/*C*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxExemptionReasonCode/@listURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo07"));
		/*- Codigo de tributo del item*/
		/*M*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/ID",Constantes.CODIGO_IMPUESTO_GENERAL_A_LAS_VENTAS));
		/*C*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/ID/@schemeAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/ID/@schemeName","Codigo de tributos"));
		/*C*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/ID/@schemeURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05"));
		/*C*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/Name","IGV"));
		/*C*/lista.add(setDatos("InvoiceLine/TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/TaxTypeCode","VAT"));
		
	}
	/*VIII. Descripción del ítem (Mandatorio)*/
	private static void asignarDatosDescripcionItemFactura(List<TagUbl> lista, FacturaBean facturaBean) {
		/*M*/lista.add(setDatos("InvoiceLine/Item/Description",facturaBean.getFacturaDetalle().getDescripcionConcepto()));
		/*C*/lista.add(setDatos("InvoiceLine/Item/SellersItemIdentification/ID","0001"));
		/*M*/lista.add(setDatos("InvoiceLine/Price/PriceAmount",facturaBean.getFacturaDetalle().getPrecioUnitario().toString()));
		/*M*/lista.add(setDatos("InvoiceLine/Price/PriceAmount/@currencyID",facturaBean.getFacturaDetalle().getMoneda()));
	}
	
	
	/*I. Datos de la Factura electrónica (Mandatorio) */
	private static void asignarDatosFactura(List<TagUbl> lista, FacturaBean facturaBean) {
		/*M*/lista.add(setDatos("UBLVersionID","2.1"));
		/*M*/lista.add(setDatos("CustomizationID","2.0"));
		/**<Serie>-<Número>*/
		/*M*/lista.add(setDatos("ID",facturaBean.getFactura().getSerie()+ "-" + facturaBean.getFactura().getNumero()));
		/**<FechaDeEmision> - YYYY-MM-DD*/
		/*M*/lista.add(setDatos("IssueDate",facturaBean.getFactura().getFechaEmision()));
		/*Tipo de documento : 01 - Factura*/
		/*M*/lista.add(setDatos("InvoiceTypeCode",Constantes.TIPO_COMPROBANTE_FACTURA));
		/*Tipo de operacion: 0101 - Venta interna / 1001 - Con Detracción*/
		/*M*/lista.add(setDatos("InvoiceTypeCode/@listID",facturaBean.getFactura().getTipoOperacion()));
		/*C*/lista.add(setDatos("InvoiceTypeCode/@listAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("InvoiceTypeCode/@listName","Tipo de Documento"));
		/*C*/lista.add(setDatos("InvoiceTypeCode/@listURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01"));
		/*Leyendas*/
		if (facturaBean.getFactura().getTipoOperacion().equals(Constantes.TIPO_OPERACION_DETRACCION_CODIGO)) {
			asignarLeyendasDetraccion(lista, facturaBean);
		}else {
			asignarLeyedaSinDetraccion(lista, facturaBean);
		}
		/**<TipoMoneda>Tipo de moneda del comprobante */
		/*M*/lista.add(setDatos("DocumentCurrencyCode",facturaBean.getFactura().getMoneda()));
		/*C*/lista.add(setDatos("DocumentCurrencyCode/@listID","ISO 4217 Alpha"));
		/*C*/lista.add(setDatos("DocumentCurrencyCode/@listName","Currency"));
		/*C*/lista.add(setDatos("DocumentCurrencyCode/@listAgencyName","United Nations Economic Commission for Europe"));
		/**<CantidadItems>Cantidad de items en la factura, opcional*/
		lista.add(setDatos("LineCountNumeric","1"));
	}
	private static void asignarLeyedaSinDetraccion(List<TagUbl> lista, FacturaBean facturaBean) {
		/*C*/lista.add(setDatos("Note",NumberToLetterConverter.convertNumberToLetter(facturaBean.getFactura().getTotal().doubleValue(),facturaBean.getFactura().getMoneda())));
		/*C*/lista.add(setDatos("Note/@languageLocaleID","1000"));
	}
	private static void asignarLeyendasDetraccion(List<TagUbl> lista, FacturaBean facturaBean) {
		/*C*/lista.add(setDatos("Note[1]",NumberToLetterConverter.convertNumberToLetter(facturaBean.getFactura().getTotal().doubleValue(),facturaBean.getFactura().getMoneda())));
		/*C*/lista.add(setDatos("Note[1]/@languageLocaleID","1000"));
		/*C*/lista.add(setDatos("Note[2]","COMPROBANTE DE DETRACCION"));
		/*C*/lista.add(setDatos("Note[2]/@languageLocaleID","2006"));
	}
	/*III. Signature (Mandatorio)*/
	private static void asignarFirma(List<TagUbl> lista) {
		/*M*/lista.add(setDatos("Signature/ID","IDSignKG"));
		/*M*/lista.add(setDatos("Signature/SignatoryParty/PartyIdentification/ID",Constantes.SUNAT_RUC_EMISOR));
		/*M*/lista.add(setDatos("Signature/SignatoryParty/PartyName/Name",Constantes.SUNAT_RAZON_SOCIAL_EMISOR));
		/*M*/lista.add(setDatos("Signature/DigitalSignatureAttachment/ExternalReference/URI","#SignST"));
	}
	/*IV. Datos del emisor del comprobante (Mandatorio)*/
	private static void asignarDatosEmisorComprobante(List<TagUbl> lista, FacturaBean facturaBean) {
		/*Tipo y Numero de RUC del emisor*/
		/*M*/lista.add(setDatos("AccountingSupplierParty/Party/PartyIdentification/ID",Constantes.SUNAT_RUC_EMISOR));
		/*M*/lista.add(setDatos("AccountingSupplierParty/Party/PartyIdentification/ID/@schemeID",Constantes.SUNAT_TIPO_DOCUMENTO_REGISTRO_UNICO_CONTRIBUYENTE));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyIdentification/ID/@schemeName","Documento de Identidad"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyIdentification/ID/@schemeAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyIdentification/ID/@schemeURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06"));
		/* Nombre comercial del emisor */
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyName/Name",Constantes.SUNAT_NOMBRE_COMERCIAL_EMISOR));
		/* Apellidos y nombres, denominación o razón social del emisor*/
		/*M*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationName",Constantes.SUNAT_RAZON_SOCIAL_EMISOR));
		/* Codigo de ubigeo (Domicilio fiscaldel emisor) */
		/*M*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/ID",Constantes.SUNAT_UBIGEO_EMISOR));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/ID/@schemeAgencyName","PE:INEI"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/ID/@schemeName","Ubigeos"));
		/*  Codigo de establecimiento(Domicilio fiscal del emisor)*/
		/**<CodigoEstablecimiento>*/
		/*M*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/AddressTypeCode",Constantes.CODIGO_ESTABLECIMIENTO));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/AddressTypeCode/@listAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/AddressTypeCode/@listName","Establecimientos anexos"));
		/*Domicilio fiscal del emisor*/
		
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/CitySubdivisionName",Constantes.SUNAT_URBANIZACION_EMISOR));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/CityName","LIMA"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/CountrySubentity","LIMA"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/District","SANTIAGO DE SURCO"));
		/*M*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/AddressLine/Line",Constantes.SUNAT_DIRECCION_EMISOR));
		
		/*Codigo de pais (Domicilio fiscal del emisor)*/
		/*M*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/Country/IdentificationCode","PE"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/Country/IdentificationCode/@listID","ISO 3166-1"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/Country/IdentificationCode/@listAgencyName","United Nations Economic Commission for Europe"));
		/*C*/lista.add(setDatos("AccountingSupplierParty/Party/PartyLegalEntity/RegistrationAddress/Country/IdentificationCode/@listName","Country"));
	}
	/*V. Datos del receptor del comprobante (Mandatorio)*/
	private static void asignarDatosReceptorComprobante(List<TagUbl> lista, FacturaBean facturaBean) {
		/* Tipo y número de documento del receptor*/
		/**<NumeroDocumentoReceptor>*/
		/*M*/lista.add(setDatos("AccountingCustomerParty/Party/PartyIdentification/ID",facturaBean.getFactura().getClienteNumero().trim()));
		/*M*/lista.add(setDatos("AccountingCustomerParty/Party/PartyIdentification/ID/@schemeID",Constantes.SUNAT_TIPO_DOCUMENTO_REGISTRO_UNICO_CONTRIBUYENTE));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyIdentification/ID/@schemeName","Documento de Identidad"));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyIdentification/ID/@schemeAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyIdentification/ID/@schemeURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06"));
		/**<NombreComercialReceptor> Nombre comercial del receptor */
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyName/Name",facturaBean.getFactura().getClienteNombre()));
		/* Apellidos y nombres, denominación o razón social del emisor*/
		/**<NombreApellidoRazonSocialReceptor>*/
		/*M*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationName",facturaBean.getFactura().getClienteNombre()));
		/* Codigo de ubigeo (Domicilio fiscal del receptor) */
		/**<CodigoUbigeoReceptor>*/
		/*M*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/ID",facturaBean.getUbigeo().getUbigeoSunat()));//TODO: UBIGEO DEL RECEPTOR
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/ID/@schemeAgencyName","PE:INEI"));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/ID/@schemeName","Ubigeos	"));
		/* Domicilio fiscal del recepto*/
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/CitySubdivisionName",facturaBean.getUbigeo().getNombreDepartamento()));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/CityName",facturaBean.getUbigeo().getNombreDepartamento()));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/CountrySubentity",facturaBean.getUbigeo().getNombreProvincia()));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/District",facturaBean.getUbigeo().getNombreDistrito()));
		/**<DireccionCompletaDetalladaReceptor>*/
		/*M*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/AddressLine/Line",facturaBean.getUbigeo().getDireccionCompleta())); //Direccion completa:facturaBean.getFactura().getClienteDireccion()
		/*  Codigo de pais (Domicilio fiscal del receptor) */
		/*M*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/Country/IdentificationCode","PE"));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/Country/IdentificationCode/@listID","ISO 3166-1"));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/Country/IdentificationCode/@listAgencyName","United Nations Economic Commission for Europe"));
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/PartyLegalEntity/RegistrationAddress/Country/IdentificationCode/@listName","Country"));
		/* Correo del receptor*/
		/**<CorreoElectronicoReceptor>*/
		/*C*/lista.add(setDatos("AccountingCustomerParty/Party/Contact/ElectronicMail",""));
	}
	
	/*VI. Datos para la detracción de la Factura (Condicional)*/
	private static void asignarDatosDetraccion(List<TagUbl> lista, FacturaBean facturaBean) {
		/*M*/lista.add(setDatos("PaymentMeans/ID","Detraccion"));
		/**<MedioPago>*/
		/*M*/lista.add(setDatos("PaymentMeans/PaymentMeansCode","001")); //TODO: Pendiente medio de pago 
		/*C*/lista.add(setDatos("PaymentMeans/PaymentMeansCode/@listName","Medio de pago"));
		/*C*/lista.add(setDatos("PaymentMeans/PaymentMeansCode/@listAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("PaymentMeans/PaymentMeansCode/@listURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo59"));
		/*Número de cuenta en el Banco de la Nación*/
		/**<NumeroCuenta>*/
		/*M*/lista.add(setDatos("PaymentMeans/PayeeFinancialAccount/ID",Constantes.NUMERO_CTA_DETRACCION_LA_REYNA));
		/*Código del bien o servicio sujeto a detracción*/
		/*M*/lista.add(setDatos("PaymentTerms[1]/ID","Detraccion"));
		/**<CodigoBienServicioConDetraccion>*/
		/*M*/lista.add(setDatos("PaymentTerms[1]/PaymentMeansID",Constantes.CATALOGO_54_CODIGO_ARRENDAMIENTO_BIENES_Y_SERVICIO));
		/*C*/lista.add(setDatos("PaymentTerms[1]/PaymentMeansID/@schemeName","Codigo de detraccion"));
		/*C*/lista.add(setDatos("PaymentTerms[1]/PaymentMeansID/@schemeAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("PaymentTerms[1]/PaymentMeansID/@schemeURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo54"));
		/*Monto y porcentaje de la detracción*/
		/**<MontoNeto> Total - TotalxPorcentaje*/
		lista.add(setDatos("PaymentTerms[1]/Note",facturaBean.getFactura().getDetracionTotal().toString()));
		/**<PorcentajeTasaDetraccion>*/
		/*M*/lista.add(setDatos("PaymentTerms[1]/PaymentPercent",Constantes.PORCENTAJE_DE_TASA_DE_DETRACCION));
		/**<MontoDetraccion> TotalxPorcentaje*/
		/*M*/lista.add(setDatos("PaymentTerms[1]/Amount",facturaBean.getFactura().getDetracionMonto().toString()));
		/**<MonedaDetraccion>*/
		/*M*/lista.add(setDatos("PaymentTerms[1]/Amount/@currencyID","PEN"));
		
		
	}
	/*VII. Forma de pago del comprobante (Mandatorio)*/
	private static void asignarDatosFormaDePago(List<TagUbl> lista, FacturaBean facturaBean) {
		lista.add(setDatos("PaymentTerms[2]/ID","FormaPago"));
		lista.add(setDatos("PaymentTerms[2]/PaymentMeansID",UtilSGT.getFormaPagoSunat(facturaBean.getFactura().getFormaPago()))); 
		if (facturaBean.getFactura().getFormaPago().equals(Constantes.FORMA_PAGO_CREDITO)) {
			if (facturaBean.getFactura().getDetracionMonto()==null) {
				lista.add(setDatos("PaymentTerms[2]/Amount",facturaBean.getFactura().getTotal().toString()));
			}else {
				//lista.add(setDatos("PaymentTerms[2]/Amount",facturaBean.getFactura().getDetracionMonto().toString()));
				lista.add(setDatos("PaymentTerms[2]/Amount",facturaBean.getFactura().getDetracionTotal().toString()));
			}
			lista.add(setDatos("PaymentTerms[2]/Amount/@currencyID",facturaBean.getFactura().getMoneda())); 
			List<TagUblCredito> listaCredito = obtenerFormaPagoCredito(facturaBean.getListaFormaPago());
			asignarFormaPagoCredito(lista,listaCredito);
		}
		
	}
	/*XII. Totales de la Factura (Mandatorio)*/
	private static void asignarDatosTotalImpuesto(List<TagUbl> lista, FacturaBean facturaBean) {
		/*Total de impuestos */
		/*M*/lista.add(setDatos("TaxTotal/TaxAmount",facturaBean.getFactura().getTotalIgv().toString()));
		/*M*/lista.add(setDatos("TaxTotal/TaxAmount/@currencyID",facturaBean.getFactura().getMoneda()));
		/*Total valor de venta operaciones gravadas */
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxableAmount",facturaBean.getFactura().getTotalGravados().toString()));	//70 SOLES	
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxableAmount/@currencyID",facturaBean.getFactura().getMoneda()));
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxAmount",facturaBean.getFactura().getTotalIgv().toString()));		//12.6 SOLES
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxAmount/@currencyID",facturaBean.getFactura().getMoneda()));
		/*Total valor de venta gravadas IGV o IVAP*/
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/ID",Constantes.CODIGO_IMPUESTO_GENERAL_A_LAS_VENTAS));
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/ID/@schemeName","Codigo de tributos"));
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/ID/@schemeAgencyName","PE:SUNAT"));
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/ID/@schemeURI","urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05"));
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/Name","IGV"));
		/*C*/lista.add(setDatos("TaxTotal/TaxSubtotal/TaxCategory/TaxScheme/TaxTypeCode","VAT"));
		/*Total valor de venta del comprobante*/
		/*M*/lista.add(setDatos("LegalMonetaryTotal/LineExtensionAmount",facturaBean.getFactura().getTotalGravados().toString()));	//70 soles
		/*M*/lista.add(setDatos("LegalMonetaryTotal/LineExtensionAmount/@currencyID",facturaBean.getFactura().getMoneda()));
		/*Total precio de venta del comprobante*/
		/*M*/lista.add(setDatos("LegalMonetaryTotal/TaxInclusiveAmount",facturaBean.getFactura().getTotal().toString()));  //82.6 soles
		/*M*/lista.add(setDatos("LegalMonetaryTotal/TaxInclusiveAmount/@currencyID",facturaBean.getFactura().getMoneda()));
		/*IMPORTE TOTAL DEL COMPROBANTE*/
		/*M*/lista.add(setDatos("LegalMonetaryTotal/PayableAmount",facturaBean.getFactura().getTotal().toString())); //82.6 SOLES
		/*M*/lista.add(setDatos("LegalMonetaryTotal/PayableAmount/@currencyID",facturaBean.getFactura().getMoneda()));
		
	}
	private static void asignarFormaPagoCredito(List<TagUbl> lista, List<TagUblCredito> listaCredito) {
		for(TagUblCredito credito: listaCredito) {
			lista.add(setDatos(credito.getIdentificador().getNodo(), credito.getIdentificador().getValor()));
			lista.add(setDatos(credito.getCuota().getNodo(), credito.getCuota().getValor()));
			lista.add(setDatos(credito.getMonto().getNodo(), credito.getMonto().getValor()));
			lista.add(setDatos(credito.getMoneda().getNodo(), credito.getMoneda().getValor()));
			lista.add(setDatos(credito.getFecha().getNodo(), credito.getFecha().getValor()));
		}
		
	}
	private static List<TagUblCredito> obtenerFormaPagoCredito(List<TblDetalleFormaPago> listaFormaPagoCredito) {
		List<TagUblCredito> listaCredito = new ArrayList<>();
		TagUblCredito credito = null;
		Integer contador = 2;
		Integer numeroCuota = 0;
		for(TblDetalleFormaPago detalle:listaFormaPagoCredito) {
			contador++;
			numeroCuota++;
			credito = new TagUblCredito();
			credito.setIdentificador(setDatos("PaymentTerms["+contador+"]/ID", "FormaPago"));
			credito.setCuota(setDatos("PaymentTerms["+contador+"]/PaymentMeansID", "Cuota"+String.format("%03d", numeroCuota)));
			credito.setMonto(setDatos("PaymentTerms["+contador+"]/Amount", detalle.getMonto().toString()));
			credito.setMoneda(setDatos("PaymentTerms["+contador+"]/Amount/@currencyID", detalle.getMoneda()));
			try {
				credito.setFecha(setDatos("PaymentTerms["+contador+"]/PaymentDueDate", UtilSGT.getFecha("yyyy-MM-dd", detalle.getFecha())));
			}catch(Exception e) {
				log.error("[obtenerFormaPagoCredito] Error: "+e.getMessage());
				credito.setFecha(setDatos("PaymentTerms["+contador+"]/PaymentDueDate", UtilSGT.getFechaYYYYMMDD().toString()));
			}
				listaCredito.add(credito);
		}
		return listaCredito;
	}
	private static TagUbl setDatos(String nodo, String valor) {
		TagUbl tag = new TagUbl();
		tag.setNodo(nodo);
		tag.setValor(valor);
		return tag;
	}
}
