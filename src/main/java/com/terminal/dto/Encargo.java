package com.terminal.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Encargo implements Serializable {
    private Integer id;
    private String detalle;
    private Date fechaIngreso;
    private Date fechaSalida;
    private Cliente cliente;
    private TipoEncargo tipoEncargo;
    private Double abono;
    private Double saldo;
    private Integer descuento;
    private List<Equipaje> equipajes;

    public Encargo(){}

    public Encargo(Integer id) {
        this.id = id;
    }

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TipoEncargo getTipoEncargo() {
        return tipoEncargo;
    }

    public void setTipoEncargo(TipoEncargo tipoEncargo) {
        this.tipoEncargo = tipoEncargo;
    }

    public Double getAbono() {
        return abono;
    }

    public void setAbono(Double abono) {
        this.abono = abono;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }

    public List<Equipaje> getEquipajes() {
        return equipajes;
    }

    public void setEquipajes(List<Equipaje> equipajes) {
        this.equipajes = equipajes;
    }
}
