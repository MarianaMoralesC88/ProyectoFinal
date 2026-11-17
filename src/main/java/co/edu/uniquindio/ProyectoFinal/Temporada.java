package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public class Temporada {
    private TipoTemporada tipo;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Temporada(TipoTemporada tipo, String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public boolean incluyeFecha(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    public TipoTemporada getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    @Override
    public String toString() {
        return tipo + " - " + nombre + " (" + fechaInicio + " a " + fechaFin + ")";
    }
}
