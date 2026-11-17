package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public record ConflictoCanal(String codigoExterno, String codigoApartamento, LocalDate fechaEntrada,
                             LocalDate fechaSalida, String motivo) {
}
