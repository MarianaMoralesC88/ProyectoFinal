package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class ReservaDirecta extends Reserva {

    public ReservaDirecta(String codigo, Apartamento theApartamento, Huesped theTitular,
            LinkedList<Ocupante> listOcupantes, LocalDate fechaEntrada, LocalDate fechaSalida,
            int valorEstancia, PoliticaCancelacion thePolitica, LocalDateTime fechaCreacion) {
        super(codigo, theApartamento, theTitular, listOcupantes, fechaEntrada, fechaSalida,
                valorEstancia, thePolitica, fechaCreacion);
    }

    @Override
    public int calcularAnticipoRequerido() {
        return 0;
    }

    @Override
    public int calcularComision() {
        return 0;
    }

    @Override
    public String obtenerCanal() {
        return "DIRECTA";
    }
}
