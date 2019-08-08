package com.terminal.dto;

public class TipoEncargo {
    private Integer id;
    private String nombre;
    private Double valor;

    public TipoEncargo(Integer id) {
        this.id = id;
    }

    public TipoEncargo(Integer id, String nombre, Double valor) {
        this.id = id;
        this.nombre = nombre;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String toString () {
        return this.nombre;
    }
}
