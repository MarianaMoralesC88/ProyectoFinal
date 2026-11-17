package co.edu.uniquindio.ProyectoFinal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private Alojamiento alojamiento;
    private Apartamento apartamento;
    private LocalDateTime ahora;
    private LocalDate entrada;
    private LocalDate salida;

    @BeforeEach
    void preparar() {
        Administrador administrador = new Administrador("Admin", "1", "300", "admin@uq.co", "admin", "1234");
        alojamiento = new Alojamiento("Aparta-Suites UQ", administrador);
        apartamento = new Apartamento("A1", "Suite Cocora", 2, 4, "Balcon", 50000, 70000, 100000);
        alojamiento.registrarApartamento(apartamento);
        alojamiento.registrarHuesped(new Huesped("Laura Gomez", "100", "311", "laura@correo.com", "laura", "abcd",
                LocalDate.of(1990, 5, 10)));
        ahora = LocalDateTime.of(2026, 11, 16, 10, 0);
        entrada = LocalDate.of(2026, 12, 1);
        salida = LocalDate.of(2026, 12, 4);
    }

    private Reserva crearReserva(boolean portal) {
        LinkedList<Ocupante> ocupantes = new LinkedList<>();
        ocupantes.add(new Ocupante("Pedro", LocalDate.of(1988, 1, 1)));
        Reserva reserva;

        if (portal) {
            reserva = alojamiento.crearReservaPortal("100", "A1", ocupantes, entrada, salida, ahora);
        } else {
            reserva = alojamiento.crearReservaDirecta("100", "A1", ocupantes, entrada, salida, ahora);
        }

        return reserva;
    }

    @DisplayName("Portal: no se confirma sin el anticipo del 30 %")
    @Test
    void testAnticipoPortal() {
        Reserva reserva = crearReserva(true);

        boolean sinAnticipo = alojamiento.confirmarReserva(reserva.getCodigo());
        alojamiento.registrarPago(reserva.getCodigo(), MedioPago.TARJETA, 90000, ahora.toLocalDate());
        boolean conAnticipo = alojamiento.confirmarReserva(reserva.getCodigo());

        assertEquals(300000, reserva.getValorEstancia());
        assertEquals(90000, reserva.calcularAnticipoRequerido());
        assertFalse(sinAnticipo);
        assertTrue(conAnticipo);
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
    }

    @DisplayName("Directa: nace Pendiente y se confirma sin anticipo")
    @Test
    void testConfirmacionDirecta() {
        Reserva reserva = crearReserva(false);

        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertEquals(0, reserva.calcularAnticipoRequerido());
        assertTrue(alojamiento.confirmarReserva(reserva.getCodigo()));
        assertFalse(alojamiento.confirmarReserva(reserva.getCodigo()));
    }

    @DisplayName("Ciclo de vida: check-in, check-out y cambios no permitidos")
    @Test
    void testCicloDeVida() {
        Reserva reserva = crearReserva(false);
        String codigo = reserva.getCodigo();

        assertFalse(alojamiento.registrarCheckIn(codigo, entrada));
        alojamiento.confirmarReserva(codigo);
        assertFalse(alojamiento.registrarCheckIn(codigo, entrada.minusDays(1)));
        alojamiento.cambiarEstadoApartamento("A1", EstadoApartamento.EN_PREPARACION, "");
        assertFalse(alojamiento.registrarCheckIn(codigo, entrada));
        alojamiento.cambiarEstadoApartamento("A1", EstadoApartamento.PREPARADO, "");

        assertTrue(alojamiento.registrarCheckIn(codigo, entrada));
        assertEquals(EstadoReserva.EN_CURSO, reserva.getEstado());
        assertEquals(EstadoApartamento.OCUPADO, apartamento.getEstado());
        assertFalse(alojamiento.cancelarReserva(codigo, entrada));

        assertTrue(alojamiento.registrarCheckOut(codigo));
        assertEquals(EstadoReserva.FINALIZADA, reserva.getEstado());
        assertEquals(EstadoApartamento.PENDIENTE_PREPARACION, apartamento.getEstado());
        assertFalse(alojamiento.registrarCheckOut(codigo));
        assertFalse(reserva.esActiva());
    }

    @DisplayName("Politica de cancelacion: 0 %, 50 % y 100 % de retencion")
    @Test
    void testPoliticaCancelacion() {
        Reserva reserva = crearReserva(false);
        alojamiento.registrarPago(reserva.getCodigo(), MedioPago.TRANSFERENCIA, 300000, ahora.toLocalDate());

        assertEquals(300000, reserva.calcularDevolucion(entrada.minusDays(10)));
        assertEquals(150000, reserva.calcularDevolucion(entrada.minusDays(5)));
        assertEquals(0, reserva.calcularDevolucion(entrada.minusDays(1)));

        assertTrue(alojamiento.cancelarReserva(reserva.getCodigo(), entrada.minusDays(5)));
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertEquals(150000, reserva.getValorDevolucion());
        assertFalse(alojamiento.confirmarReserva(reserva.getCodigo()));
    }

    @DisplayName("No-show: solo despues de las 10:00 p. m. del dia de entrada")
    @Test
    void testNoShow() {
        Reserva reserva = crearReserva(false);
        alojamiento.registrarPago(reserva.getCodigo(), MedioPago.EFECTIVO, 100000, ahora.toLocalDate());
        alojamiento.confirmarReserva(reserva.getCodigo());

        boolean antesDeLaHora = alojamiento.declararNoShow(reserva.getCodigo(), entrada.atTime(21, 0));
        boolean despuesDeLaHora = alojamiento.declararNoShow(reserva.getCodigo(), entrada.atTime(22, 30));

        assertFalse(antesDeLaHora);
        assertTrue(despuesDeLaHora);
        assertEquals(EstadoReserva.NO_SHOW, reserva.getEstado());
        assertEquals(0, reserva.getValorDevolucion());
    }
}
