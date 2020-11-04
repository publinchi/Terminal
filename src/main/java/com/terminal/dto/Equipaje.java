package com.terminal.dto;

import java.util.Date;

public class Equipaje {
    private Integer id;
    private String detalle;
    private Date fechaIngreso;
    private Date fechaSalida;
    private Encargo encargo;
    private TipoEncargo tipoEncargo;
    private Double valor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Encargo getEncargo() {
        return encargo;
    }

    public void setEncargo(Encargo encargo) {
        this.encargo = encargo;
    }

    public TipoEncargo getTipoEncargo() {
        return tipoEncargo;
    }

    public void setTipoEncargo(TipoEncargo tipoEncargo) {
        this.tipoEncargo = tipoEncargo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
