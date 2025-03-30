package com.microservicio.gimnasio.clase.dto;

import java.io.Serializable;

public class ResumenEntrenamiento implements Serializable {
    private int totalAsistentes;
    private int numeroClases;

    public ResumenEntrenamiento() {
        this.totalAsistentes = 0;
        this.numeroClases = 0;
    }

    /**
     * Actualiza el resumen agregando los datos de entrenamiento.
     * @param datos Los datos a agregar.
     * @return El resumen actualizado.
     */
    public ResumenEntrenamiento actualizar(DatosEntrenamiento datos) {
        this.totalAsistentes += datos.getCantidadAsistentes();
        this.numeroClases++;
        return this;
    }

    // Getters y Setters
    public int getTotalAsistentes() { return totalAsistentes; }
    public void setTotalAsistentes(int totalAsistentes) { this.totalAsistentes = totalAsistentes; }
    public int getNumeroClases() { return numeroClases; }
    public void setNumeroClases(int numeroClases) { this.numeroClases = numeroClases; }
}
