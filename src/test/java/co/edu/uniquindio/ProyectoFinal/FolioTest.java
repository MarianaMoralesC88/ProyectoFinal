package co.edu.uniquindio.ProyectoFinal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FolioTest {

    private Folio folio;
    private LocalDate fecha;

    @BeforeEach
    void preparar() {
        folio = new Folio();
        fecha = LocalDate.of(2026, 11, 16);
        folio.agregarCargo(new Cargo(TipoCargo.ALOJAMIENTO, "Estancia", 300000, fecha));
    }

    @DisplayName("Saldo: cargos menos pagos parciales con distintos medios")
    @Test
    void testSaldoConPagosParciales() {
        folio.agregarCargo(new Cargo(TipoCargo.SERVICIO_ADICIONAL, "Desayuno", 40000, fecha));
        folio.registrarPago(new Pago(MedioPago.EFECTIVO, 100000, fecha));
        folio.registrarPago(new Pago(MedioPago.TARJETA, 50000, fecha));

        assertEquals(340000, folio.calcularTotalCargos());
        assertEquals(150000, folio.calcularTotalPagos());
        assertEquals(190000, folio.calcularSaldo());
        assertEquals(4, folio.obtenerMovimientos().size());
    }

    @DisplayName("Pagos inalterables: la correccion es un movimiento contrario")
    @Test
    void testCorreccion() {
        folio.registrarPago(new Pago(MedioPago.EFECTIVO, 100000, fecha));
        folio.registrarPago(new Pago(MedioPago.EFECTIVO, -100000, fecha));

        assertEquals(2, folio.getListPagos().size());
        assertEquals(0, folio.calcularTotalPagos());
        assertEquals(300000, folio.calcularSaldo());
    }

    @DisplayName("No se aceptan movimientos en cero ni pagos sin medio")
    @Test
    void testMovimientosNoValidos() {
        assertFalse(folio.agregarCargo(new Cargo(TipoCargo.PENALIDAD, "Sin valor", 0, fecha)));
        assertFalse(folio.registrarPago(new Pago(MedioPago.TARJETA, 0, fecha)));
        assertFalse(folio.registrarPago(new Pago(null, 5000, fecha)));
        assertEquals(1, folio.obtenerMovimientos().size());
    }
}
