package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public record Ocupante(String nombre, LocalDate fechaNacimiento) {

    public int calcularEdad(LocalDate fecha) {
        int edad = 0;

        while (!fechaNacimiento.plusYears(edad + 1).isAfter(fecha)) {
            edad++;
        }

        return edad;
    }
}
