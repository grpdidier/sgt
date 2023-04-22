package com.pe.lima.sg.bean.reporte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class IngresoEgresoBean implements Serializable, Comparable<IngresoEgresoBean>{

	private static final long serialVersionUID = 1L;
	
	private String 	nombre;
	private String	numero;
	private String tipo_cobro;
	private BigDecimal tipo_cambio;
	private String 	tipo_moneda;
	private Date	fecha_cobro;
	private BigDecimal monto_soles;
	private BigDecimal monto_dolares;
	private String nota;
	//Datos del Bancarizado
	private String tipo_pago;
	private String tipo_bancarizado;
	private String numero_operacion;
	private Date fecha_operacion;
	//Datos de Auditoria
	private Integer usurio_creacion;
	private Date fecha_creacion;
	//Otros datos
	private Integer codigo_edificio;
	//Descripcion del ingreso y gasto
	private String descripcion;
	//Datos del cliente
	private String numero_ruc;
	private String modulo;
	private String detalleModulo;
	
	@Override
    public int compareTo(IngresoEgresoBean bean) {
        String numeroComparar = bean.getNumero().toUpperCase();
        
        /* For Ascending order*/
        return numero.compareTo(numeroComparar);

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
	
	public IngresoEgresoBean() {
		super();
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getTipo_cobro() {
		return tipo_cobro;
	}


	public void setTipo_cobro(String tipo_cobro) {
		this.tipo_cobro = tipo_cobro;
	}


	public BigDecimal getTipo_cambio() {
		return tipo_cambio;
	}


	public void setTipo_cambio(BigDecimal tipo_cambio) {
		this.tipo_cambio = tipo_cambio;
	}


	public String getTipo_moneda() {
		return tipo_moneda;
	}


	public void setTipo_moneda(String tipo_moneda) {
		this.tipo_moneda = tipo_moneda;
	}


	public BigDecimal getMonto_soles() {
		return monto_soles;
	}


	public void setMonto_soles(BigDecimal monto_soles) {
		this.monto_soles = monto_soles;
	}


	public BigDecimal getMonto_dolares() {
		return monto_dolares;
	}


	public void setMonto_dolares(BigDecimal monto_dolares) {
		this.monto_dolares = monto_dolares;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public Date getFecha_cobro() {
		return fecha_cobro;
	}


	public void setFecha_cobro(Date fecha_cobro) {
		this.fecha_cobro = fecha_cobro;
	}


	public String getNota() {
		return nota;
	}


	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getTipo_pago() {
		return tipo_pago;
	}

	public void setTipo_pago(String tipo_pago) {
		this.tipo_pago = tipo_pago;
	}

	public String getTipo_bancarizado() {
		return tipo_bancarizado;
	}

	public void setTipo_bancarizado(String tipo_bancarizado) {
		this.tipo_bancarizado = tipo_bancarizado;
	}

	public String getNumero_operacion() {
		return numero_operacion;
	}

	public void setNumero_operacion(String numero_operacion) {
		this.numero_operacion = numero_operacion;
	}

	public Date getFecha_operacion() {
		return fecha_operacion;
	}

	public void setFecha_operacion(Date fecha_operacion) {
		this.fecha_operacion = fecha_operacion;
	}

	public Integer getUsurio_creacion() {
		return usurio_creacion;
	}

	public void setUsurio_creacion(Integer usurio_creacion) {
		this.usurio_creacion = usurio_creacion;
	}

	public Date getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public Integer getCodigo_edificio() {
		return codigo_edificio;
	}

	public void setCodigo_edificio(Integer codigo_edificio) {
		this.codigo_edificio = codigo_edificio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNumero_ruc() {
		return numero_ruc;
	}

	public void setNumero_ruc(String numero_ruc) {
		this.numero_ruc = numero_ruc;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getDetalleModulo() {
		return detalleModulo;
	}

	public void setDetalleModulo(String detalleModulo) {
		this.detalleModulo = detalleModulo;
	}


	

}
