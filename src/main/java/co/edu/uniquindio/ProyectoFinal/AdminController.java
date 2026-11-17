package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;
import java.util.LinkedList;

public class AdminController {
    private Alojamiento alojamiento;

    public AdminController(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public LinkedList<Apartamento> obtenerApartamentos() {
        return alojamiento.getListApartamentos();
    }

    public boolean registrarApartamento(Apartamento apartamento) {
        return alojamiento.registrarApartamento(apartamento);
    }

    public boolean actualizarApartamento(String codigo, String nombre, int numeroDormitorios,
                                         int capacidadMaxima, String caracteristicas) {
        return alojamiento.actualizarApartamento(codigo, nombre, numeroDormitorios, capacidadMaxima, caracteristicas);
    }

    public boolean eliminarApartamento(String codigo) {
        return alojamiento.eliminarApartamento(codigo);
    }

    public boolean actualizarTarifas(String codigo, int tarifaBaja, int tarifaMedia, int tarifaAlta) {
        return alojamiento.actualizarTarifas(codigo, tarifaBaja, tarifaMedia, tarifaAlta);
    }

    public boolean registrarBloqueo(String codigo, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        return alojamiento.registrarBloqueo(codigo, fechaInicio, fechaFin, motivo);
    }

    public LinkedList<Recepcionista> obtenerRecepcionistas() {
        return alojamiento.getListRecepcionistas();
    }

    public boolean contratarRecepcionista(Recepcionista recepcionista) {
        return alojamiento.contratarRecepcionista(recepcionista);
    }

    public boolean actualizarRecepcionista(String documento, String nombre, String telefono, String correo) {
        return alojamiento.actualizarRecepcionista(documento, nombre, telefono, correo);
    }

    public boolean desvincularRecepcionista(String documento) {
        return alojamiento.desvincularRecepcionista(documento);
    }

    public LinkedList<Temporada> obtenerTemporadas() {
        return alojamiento.getListTemporadas();
    }

    public boolean registrarTemporada(Temporada temporada) {
        return alojamiento.registrarTemporada(temporada);
    }

    public PoliticaCancelacion obtenerPolitica() {
        return alojamiento.getThePolitica();
    }

    public boolean configurarPoliticaCancelacion(int diasDevolucionTotal, int diasRetencionParcial,
                                                 int porcentajeRetencionParcial) {
        return alojamiento.configurarPoliticaCancelacion(diasDevolucionTotal, diasRetencionParcial,
                porcentajeRetencionParcial);
    }

    public String generarReporte(LocalDate desde, LocalDate hasta) {
        String reporte = "REPORTE DEL " + desde + " AL " + hasta + "\n";

        reporte += "\nOcupacion por apartamento (noches ocupadas):\n";
        for (Apartamento apartamento : alojamiento.getListApartamentos()) {
            reporte += "  " + apartamento + ": "
                    + alojamiento.calcularNochesOcupadas(apartamento.getCodigo(), desde, hasta) + "\n";
        }

        reporte += "\nIngresos del periodo: $" + alojamiento.calcularIngresosPeriodo(desde, hasta) + "\n";

        reporte += "\nReservas por canal (incluye canceladas y no-show):\n";
        reporte += "  PORTAL: " + alojamiento.contarReservasPorCanal("PORTAL") + "\n";
        reporte += "  DIRECTA: " + alojamiento.contarReservasPorCanal("DIRECTA") + "\n";
        reporte += "  EXTERNA: " + alojamiento.contarReservasPorCanal("EXTERNA") + "\n";
        reporte += "  Conflictos de canal: " + alojamiento.getListConflictos().size() + "\n";

        reporte += "\nSaldos pendientes:\n";
        for (Reserva reserva : alojamiento.obtenerReservasConSaldoPendiente()) {
            reporte += "  " + reserva.getCodigo() + " - " + reserva.getTheTitular().getNombre()
                    + ": $" + reserva.getTheFolio().calcularSaldo() + "\n";
        }

        reporte += "\nApartamento con mas novedades: ";
        Apartamento conMasNovedades = alojamiento.obtenerApartamentoConMasNovedades();
        if (conMasNovedades != null) {
            reporte += conMasNovedades + " (" + conMasNovedades.getListNovedades().size() + ")\n";
        } else {
            reporte += "ninguno\n";
        }

        return reporte;
    }
}
