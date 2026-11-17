package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public interface IMovimiento {

    int getValor();

    LocalDate getFecha();

    String obtenerDescripcion();
}
