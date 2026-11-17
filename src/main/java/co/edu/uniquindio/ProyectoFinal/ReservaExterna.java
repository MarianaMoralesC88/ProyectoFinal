package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class ReservaExterna extends Reserva {
    private String codigoExterno;

    public ReservaExterna(String codigo, Apartamento theApartamento, Huesped theTitular,
            LinkedList<Ocupante> listOcupantes, LocalDate fechaEntrada, LocalDate fechaSalida,
            int valorEstancia, PoliticaCancelacion thePolitica, LocalDateTime fechaCreacion, String codigoExterno) {
        super(codigo, theApartamento, theTitular, listOcupantes, fechaEntrada, fechaSalida,
                valorEstancia, thePolitica, fechaCreacion);
        this.codigoExterno = codigoExterno;
    }

    @Override
    public int calcularAnticipoRequerido() {
        return 0;
    }

    @Override
    public int calcularComision() {
        return valorEstancia * 15 / 100;
    }

    @Override
    public String obtenerCanal() {
        return "EXTERNA";
    }

    public String getCodigoExterno() {
        return codigoExterno;
    }
}
