package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class HuespedController {
    private Alojamiento alojamiento;

    public HuespedController(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public LinkedList<Apartamento> consultarDisponibles(LocalDate entrada, LocalDate salida, int numeroPersonas) {
        return alojamiento.consultarDisponibles(entrada, salida, numeroPersonas);
    }

    public LinkedList<DetalleNoche> cotizar(Apartamento apartamento, LocalDate entrada, LocalDate salida,
                                            Huesped titular, LinkedList<Ocupante> ocupantes) {
        int personasQuePagan = alojamiento.contarPersonasQuePagan(titular, ocupantes, entrada);
        return alojamiento.cotizar(apartamento, entrada, salida, personasQuePagan);
    }

    public Reserva crearReservaPortal(String documentoTitular, String codigoApartamento,
                                      LinkedList<Ocupante> ocupantes, LocalDate entrada, LocalDate salida) {
        return alojamiento.crearReservaPortal(documentoTitular, codigoApartamento, ocupantes, entrada, salida,
                LocalDateTime.now());
    }

    public LinkedList<Reserva> obtenerReservasHuesped(String documento) {
        return alojamiento.obtenerReservasHuesped(documento);
    }

    public boolean registrarPago(String codigoReserva, MedioPago medioPago, int valor) {
        return alojamiento.registrarPago(codigoReserva, medioPago, valor, LocalDate.now());
    }

    public boolean confirmarReserva(String codigoReserva) {
        return alojamiento.confirmarReserva(codigoReserva);
    }

    public boolean cancelarReserva(String codigoReserva) {
        return alojamiento.cancelarReserva(codigoReserva, LocalDate.now());
    }

    public boolean actualizarHuesped(String documento, String nombre, String telefono, String correo) {
        return alojamiento.actualizarHuesped(documento, nombre, telefono, correo);
    }
}
