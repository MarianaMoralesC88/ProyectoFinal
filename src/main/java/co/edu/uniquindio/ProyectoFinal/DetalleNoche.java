package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public record DetalleNoche(LocalDate noche, TipoTemporada temporada, int tarifa, int personasQuePagan, int subtotal) {
}
