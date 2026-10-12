package com.pe.lima.sg.entity.caja;

import java.io.Serializable;
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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tbl_api_llamada", schema = "caj")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "comprobante")
public class TblApiLlamada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_api")
    private Integer codigoApi;

    @Column(name = "respuesta", length = 256)
    private String respuesta;

    @Column(name = "nombre_api", length = 128)
    private String nombreApi;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "codigo_cxc")
    private Integer codigoCxc;
    
    @Column(name = "status")
    private Integer status;
    
    @Column(name = "usuario_creacion")
    private Integer usuarioCreacion;

    @Column(name = "ip_creacion", length = 64)
    private String ipCreacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_comprobante", nullable = false)
    private TblComprobanteSunat comprobante;
}
