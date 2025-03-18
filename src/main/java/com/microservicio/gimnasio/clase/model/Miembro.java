package com.microservicio.gimnasio.clase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Miembro {

    private Long id;
    private String nombre;
    private String apellido;
    private Date fechaDeRegistro;
    private String email;
    private Long telefono;

    public Miembro(Long id, String nombre, String apellido, Date fechaDeRegistro, String email, Long telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeRegistro = fechaDeRegistro;
        this.email = email;
        this.telefono = telefono;
    }

    public Miembro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaDeRegistro() {
        return fechaDeRegistro;
    }

    public void setFechaDeRegistro(Date fechaDeRegistro) {
        this.fechaDeRegistro = fechaDeRegistro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

}


