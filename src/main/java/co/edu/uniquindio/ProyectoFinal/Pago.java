package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public class Pago implements IMovimiento {
    private MedioPago medioPago;
    private int valor;
    private LocalDate fecha;

    public Pago(MedioPago medioPago, int valor, LocalDate fecha) {
        this.medioPago = medioPago;
        this.valor = valor;
        this.fecha = fecha;
    }

    @Override
    public String obtenerDescripcion() {
        return "Pago en " + medioPago;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    @Override
    public int getValor() {
        return valor;
    }

    @Override
    public LocalDate getFecha() {
        return fecha;
    }
}
