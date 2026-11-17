package co.edu.uniquindio.ProyectoFinal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class AlojamientoTest {

    private Alojamiento alojamiento;
    private LocalDateTime ahora;
    private LinkedList<Ocupante> sinOcupantes;

    @BeforeEach
    void preparar() {
        Administrador administrador = new Administrador("Admin", "1", "300", "admin@uq.co", "admin", "1234");
        alojamiento = new Alojamiento("Aparta-Suites UQ", administrador);
        alojamiento.registrarApartamento(new Apartamento("A1", "Suite Cocora", 2, 4, "Balcon", 50000, 70000, 100000));
        alojamiento.registrarApartamento(new Apartamento("A2", "Suite Salento", 1, 2, "Chimenea", 40000, 60000, 90000));
        alojamiento.registrarHuesped(new Huesped("Laura Gomez", "100", "311", "laura@correo.com", "laura", "abcd",
                LocalDate.of(1990, 5, 10)));
        alojamiento.registrarTemporada(new Temporada(TipoTemporada.ALTA, "Fin de anio",
                LocalDate.of(2026, 12, 15), LocalDate.of(2027, 1, 15)));
        ahora = LocalDateTime.of(2026, 11, 16, 10, 0);
        sinOcupantes = new LinkedList<>();
    }

    @DisplayName("Sin doble venta: dos reservas activas no comparten noche")
    @Test
    void testSinDobleVenta() {
        Reserva primera = alojamiento.crearReservaDirecta("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4), ahora);
        Reserva cruzada = alojamiento.crearReservaPortal("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 3), LocalDate.of(2026, 12, 5), ahora);
        Reserva siguiente = alojamiento.crearReservaPortal("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 4), LocalDate.of(2026, 12, 6), ahora);

        assertNotNull(primera);
        assertNull(cruzada);
        assertNotNull(siguiente);
        assertEquals(2, alojamiento.getListReservas().size());
    }

    @DisplayName("Capacidad maxima: los menores tambien cuentan")
    @Test
    void testCapacidadMaxima() {
        LinkedList<Ocupante> ocupantes = new LinkedList<>();
        ocupantes.add(new Ocupante("Pedro", LocalDate.of(1988, 1, 1)));
        ocupantes.add(new Ocupante("Sofia", LocalDate.of(2023, 1, 1)));

        Reserva reserva = alojamiento.crearReservaDirecta("100", "A2", ocupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 3), ahora);

        assertNull(reserva);
    }

    @DisplayName("Fechas validas: minimo una noche y no antes de hoy")
    @Test
    void testFechasValidas() {
        Reserva sinNoches = alojamiento.crearReservaDirecta("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 1), ahora);
        Reserva enElPasado = alojamiento.crearReservaDirecta("100", "A1", sinOcupantes,
                LocalDate.of(2026, 11, 15), LocalDate.of(2026, 11, 18), ahora);

        assertNull(sinNoches);
        assertNull(enElPasado);
    }

    @DisplayName("Calculo del valor: noche por noche y sin cobrar menores de 6 anios")
    @Test
    void testCalculoValor() {
        LinkedList<Ocupante> ocupantes = new LinkedList<>();
        ocupantes.add(new Ocupante("Pedro", LocalDate.of(1988, 1, 1)));
        ocupantes.add(new Ocupante("Sofia", LocalDate.of(2022, 6, 1)));

        Reserva reserva = alojamiento.crearReservaDirecta("100", "A1", ocupantes,
                LocalDate.of(2026, 12, 13), LocalDate.of(2026, 12, 16), ahora);
        LinkedList<DetalleNoche> detalle = alojamiento.cotizar(alojamiento.buscarApartamento("A1"),
                LocalDate.of(2026, 12, 13), LocalDate.of(2026, 12, 16), 2);

        assertNotNull(reserva);
        assertEquals(3, reserva.calcularNumeroNoches());
        assertEquals(3, reserva.contarPersonas());
        assertEquals(400000, reserva.getValorEstancia());
        assertEquals(3, detalle.size());
        assertEquals(TipoTemporada.BAJA, detalle.get(0).temporada());
        assertEquals(TipoTemporada.ALTA, detalle.get(2).temporada());
        assertEquals(200000, detalle.get(2).subtotal());
    }

    @DisplayName("Bloqueos: una noche bloqueada no esta disponible")
    @Test
    void testBloqueo() {
        boolean bloqueado = alojamiento.registrarBloqueo("A1", LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 2), "Mantenimiento electrico");
        Reserva reserva = alojamiento.crearReservaDirecta("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 3), ahora);

        assertTrue(bloqueado);
        assertNull(reserva);
        assertEquals(1, alojamiento.consultarDisponibles(LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 3), 2).size());
    }

    @DisplayName("Reservas externas: el choque se rechaza y queda el conflicto de canal")
    @Test
    void testReservaExterna() {
        Reserva directa = alojamiento.crearReservaDirecta("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4), ahora);
        Reserva externaCruzada = alojamiento.recibirReservaExterna("EXT-1", "100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 2), LocalDate.of(2026, 12, 5), ahora);
        Reserva externa = alojamiento.recibirReservaExterna("EXT-2", "100", "A2", sinOcupantes,
                LocalDate.of(2026, 12, 2), LocalDate.of(2026, 12, 4), ahora);
        Reserva codigoRepetido = alojamiento.recibirReservaExterna("EXT-2", "100", "A2", sinOcupantes,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), ahora);

        assertNotNull(directa);
        assertNull(externaCruzada);
        assertTrue(directa.esActiva());
        assertNotNull(externa);
        assertEquals(12000, externa.calcularComision());
        assertNull(codigoRepetido);
        assertEquals(2, alojamiento.getListConflictos().size());
    }

    @DisplayName("Plazo de confirmacion: a las 24 horas se cancela y libera las noches")
    @Test
    void testPlazoConfirmacion() {
        Reserva portal = alojamiento.crearReservaPortal("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4), ahora);

        int antes = alojamiento.cancelarReservasVencidas(ahora.plusHours(23));
        int despues = alojamiento.cancelarReservasVencidas(ahora.plusHours(25));
        Reserva nueva = alojamiento.crearReservaDirecta("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4), ahora.plusHours(26));

        assertEquals(0, antes);
        assertEquals(1, despues);
        assertEquals(EstadoReserva.CANCELADA, portal.getEstado());
        assertNotNull(nueva);
    }

    @DisplayName("Valor congelado: cambiar tarifas no afecta reservas creadas")
    @Test
    void testValorCongelado() {
        Reserva reserva = alojamiento.crearReservaDirecta("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 3), ahora);

        boolean actualizado = alojamiento.actualizarTarifas("A1", 80000, 90000, 120000);
        alojamiento.configurarPoliticaCancelacion(10, 3, 30);

        assertTrue(actualizado);
        assertEquals(100000, reserva.getValorEstancia());
        assertEquals(7, reserva.getThePolitica().diasDevolucionTotal());
    }

    @DisplayName("Autenticacion por rol y desvinculacion de recepcionistas")
    @Test
    void testAutenticacion() {
        alojamiento.contratarRecepcionista(new Recepcionista("Carlos", "200", "312", "carlos@uq.co", "carlos", "xyz"));

        assertNotNull(alojamiento.autenticar("admin", "1234"));
        assertNotNull(alojamiento.autenticar("laura", "abcd"));
        assertNotNull(alojamiento.autenticar("carlos", "xyz"));
        assertNull(alojamiento.autenticar("laura", "otra"));
        assertTrue(alojamiento.desvincularRecepcionista("200"));
        assertNull(alojamiento.autenticar("carlos", "xyz"));
        assertFalse(alojamiento.registrarHuesped(new Huesped("Otro", "300", "313", "o@correo.com", "admin", "1",
                LocalDate.of(2000, 1, 1))));
    }

    @DisplayName("Reportes: ingresos, canal, ocupacion y saldos")
    @Test
    void testReportes() {
        Reserva directa = alojamiento.crearReservaDirecta("100", "A1", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4), ahora);
        alojamiento.crearReservaPortal("100", "A2", sinOcupantes,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 2), ahora);
        alojamiento.registrarPago(directa.getCodigo(), MedioPago.EFECTIVO, 50000, LocalDate.of(2026, 11, 16));
        alojamiento.registrarNovedad("A2", "Lampara rota", LocalDate.of(2026, 11, 16));

        assertEquals(50000, alojamiento.calcularIngresosPeriodo(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30)));
        assertEquals(1, alojamiento.contarReservasPorCanal("DIRECTA"));
        assertEquals(1, alojamiento.contarReservasPorCanal("PORTAL"));
        assertEquals(3, alojamiento.calcularNochesOcupadas("A1", LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 31)));
        assertEquals(2, alojamiento.obtenerReservasConSaldoPendiente().size());
        assertEquals("A2", alojamiento.obtenerApartamentoConMasNovedades().getCodigo());
    }
}
