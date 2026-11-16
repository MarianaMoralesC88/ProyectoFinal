package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public record Bloqueo(LocalDate fechaInicio, LocalDate fechaFin, String motivo) {

    public boolean incluyeNoche(LocalDate noche) {
        return !noche.isBefore(fechaInicio) && !noche.isAfter(fechaFin);
    }
}
