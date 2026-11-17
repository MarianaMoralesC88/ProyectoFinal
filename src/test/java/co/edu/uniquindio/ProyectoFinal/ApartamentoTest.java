package co.edu.uniquindio.ProyectoFinal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ApartamentoTest {

    private Apartamento apartamento;

    @BeforeEach
    void preparar() {
        apartamento = new Apartamento("A1", "Suite Cocora", 2, 4, "Balcon", 50000, 70000, 100000);
    }

    @DisplayName("Tarifa por persona por noche segun la temporada")
    @Test
    void testTarifaPorTemporada() {
        assertEquals(50000, apartamento.obtenerTarifa(TipoTemporada.BAJA));
        assertEquals(70000, apartamento.obtenerTarifa(TipoTemporada.MEDIA));
        assertEquals(100000, apartamento.obtenerTarifa(TipoTemporada.ALTA));
        assertFalse(apartamento.actualizarTarifas(0, 70000, 100000));
    }

    @DisplayName("Fuera de servicio exige registrar el motivo")
    @Test
    void testFueraDeServicio() {
        assertEquals(EstadoApartamento.PREPARADO, apartamento.getEstado());
        assertFalse(apartamento.cambiarEstado(EstadoApartamento.FUERA_DE_SERVICIO, ""));
        assertTrue(apartamento.cambiarEstado(EstadoApartamento.FUERA_DE_SERVICIO, "Danio en la tuberia"));
        assertEquals("Danio en la tuberia", apartamento.getMotivoFueraServicio());
        assertTrue(apartamento.cambiarEstado(EstadoApartamento.EN_PREPARACION, ""));
        assertEquals("", apartamento.getMotivoFueraServicio());
    }

    @DisplayName("Bloqueo de un rango de fechas con su motivo")
    @Test
    void testBloqueo() {
        boolean sinMotivo = apartamento.registrarBloqueo(new Bloqueo(LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 3), ""));
        boolean valido = apartamento.registrarBloqueo(new Bloqueo(LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 3), "Pintura"));

        assertFalse(sinMotivo);
        assertTrue(valido);
        assertTrue(apartamento.estaBloqueado(LocalDate.of(2026, 12, 3)));
        assertFalse(apartamento.estaBloqueado(LocalDate.of(2026, 12, 4)));
    }

    @DisplayName("La edad del ocupante se calcula a partir de la fecha de nacimiento")
    @Test
    void testEdadOcupante() {
        Ocupante ocupante = new Ocupante("Sofia", LocalDate.of(2020, 12, 10));

        assertEquals(5, ocupante.calcularEdad(LocalDate.of(2026, 12, 9)));
        assertEquals(6, ocupante.calcularEdad(LocalDate.of(2026, 12, 10)));
    }
}
