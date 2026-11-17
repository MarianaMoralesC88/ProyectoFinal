package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class RecepcionController {
    private Alojamiento alojamiento;

    public RecepcionController(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public LinkedList<Reserva> obtenerReservas() {
        return alojamiento.getListReservas();
    }

    public LinkedList<Apartamento> obtenerApartamentos() {
        return alojamiento.getListApartamentos();
    }

    public LinkedList<ConflictoCanal> obtenerConflictos() {
        return alojamiento.getListConflictos();
    }

    public Reserva crearReservaDirecta(String documentoTitular, String codigoApartamento,
                                       LinkedList<Ocupante> ocupantes, LocalDate entrada, LocalDate salida) {
        return alojamiento.crearReservaDirecta(documentoTitular, codigoApartamento, ocupantes, entrada, salida,
                LocalDateTime.now());
    }

    public Reserva recibirReservaExterna(String codigoExterno, String documentoTitular, String codigoApartamento,
                                         LinkedList<Ocupante> ocupantes, LocalDate entrada, LocalDate salida) {
        return alojamiento.recibirReservaExterna(codigoExterno, documentoTitular, codigoApartamento, ocupantes,
                entrada, salida, LocalDateTime.now());
    }

    public boolean confirmarReserva(String codigo) {
        return alojamiento.confirmarReserva(codigo);
    }

    public boolean registrarCheckIn(String codigo) {
        return alojamiento.registrarCheckIn(codigo, LocalDate.now());
    }

    public boolean registrarCheckOut(String codigo) {
        return alojamiento.registrarCheckOut(codigo);
    }

    public boolean declararNoShow(String codigo) {
        return alojamiento.declararNoShow(codigo, LocalDateTime.now());
    }

    public boolean cancelarReserva(String codigo) {
        return alojamiento.cancelarReserva(codigo, LocalDate.now());
    }

    public boolean registrarPago(String codigo, MedioPago medioPago, int valor) {
        return alojamiento.registrarPago(codigo, medioPago, valor, LocalDate.now());
    }

    public boolean registrarCargoServicio(String codigo, String descripcion, int valor) {
        return alojamiento.registrarCargoServicio(codigo, descripcion, valor, LocalDate.now());
    }

    public boolean cambiarEstadoApartamento(String codigo, EstadoApartamento estado, String motivo) {
        return alojamiento.cambiarEstadoApartamento(codigo, estado, motivo);
    }

    public boolean registrarNovedad(String codigo, String descripcion) {
        return alojamiento.registrarNovedad(codigo, descripcion, LocalDate.now());
    }
}
