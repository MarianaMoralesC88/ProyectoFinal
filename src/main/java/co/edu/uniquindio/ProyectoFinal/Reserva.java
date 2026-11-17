package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public abstract class Reserva {
    protected String codigo;
    protected LocalDate fechaEntrada;
    protected LocalDate fechaSalida;
    protected LocalDateTime fechaCreacion;
    protected EstadoReserva estado;
    protected int valorEstancia;
    protected int valorDevolucion;

    protected Apartamento theApartamento;
    protected Huesped theTitular;
    protected LinkedList<Ocupante> listOcupantes;
    protected Folio theFolio;
    protected PoliticaCancelacion thePolitica;

    public Reserva(String codigo, Apartamento theApartamento, Huesped theTitular,
                   LinkedList<Ocupante> listOcupantes, LocalDate fechaEntrada, LocalDate fechaSalida,
                   int valorEstancia, PoliticaCancelacion thePolitica, LocalDateTime fechaCreacion) {
        this.codigo = codigo;
        this.theApartamento = theApartamento;
        this.theTitular = theTitular;
        this.listOcupantes = listOcupantes;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.valorEstancia = valorEstancia;
        this.thePolitica = thePolitica;
        this.fechaCreacion = fechaCreacion;
        estado = EstadoReserva.PENDIENTE;
        valorDevolucion = 0;
        theFolio = new Folio();
        theFolio.agregarCargo(new Cargo(TipoCargo.ALOJAMIENTO, "Estancia " + fechaEntrada + " a " + fechaSalida,
                valorEstancia, fechaCreacion.toLocalDate()));
    }

    public abstract int calcularAnticipoRequerido();

    public abstract int calcularComision();

    public abstract String obtenerCanal();

    public boolean esActiva() {
        return estado == EstadoReserva.PENDIENTE || estado == EstadoReserva.CONFIRMADA
                || estado == EstadoReserva.EN_CURSO;
    }

    public int calcularNumeroNoches() {
        int noches = 0;

        for (LocalDate noche = fechaEntrada; noche.isBefore(fechaSalida); noche = noche.plusDays(1)) {
            noches++;
        }

        return noches;
    }

    public boolean ocupaNoche(LocalDate noche) {
        return !noche.isBefore(fechaEntrada) && noche.isBefore(fechaSalida);
    }

    public boolean seCruzaCon(LocalDate entrada, LocalDate salida) {
        return entrada.isBefore(fechaSalida) && fechaEntrada.isBefore(salida);
    }

    public int contarPersonas() {
        return listOcupantes.size() + 1;
    }

    public boolean confirmar() {
        boolean confirmada = false;

        if (estado == EstadoReserva.PENDIENTE
                && theFolio.calcularTotalPagos() >= calcularAnticipoRequerido()) {
            estado = EstadoReserva.CONFIRMADA;
            confirmada = true;
        }

        return confirmada;
    }

    public int contarDiasAntelacion(LocalDate fechaCancelacion) {
        int dias = 0;

        for (LocalDate dia = fechaCancelacion; dia.isBefore(fechaEntrada); dia = dia.plusDays(1)) {
            dias++;
        }

        return dias;
    }

    public int calcularDevolucion(LocalDate fechaCancelacion) {
        int diasAntelacion = contarDiasAntelacion(fechaCancelacion);
        int porcentaje = thePolitica.calcularPorcentajeRetencion(diasAntelacion);
        int retencion = valorEstancia * porcentaje / 100;
        int devolucion = theFolio.calcularTotalPagos() - retencion;

        if (devolucion < 0) {
            devolucion = 0;
        }

        return devolucion;
    }

    public boolean cancelar(LocalDate fechaCancelacion) {
        boolean cancelada = false;

        if (estado == EstadoReserva.PENDIENTE || estado == EstadoReserva.CONFIRMADA) {
            valorDevolucion = calcularDevolucion(fechaCancelacion);
            estado = EstadoReserva.CANCELADA;
            cancelada = true;
        }

        return cancelada;
    }

    public boolean vencioPlazoConfirmacion(LocalDateTime ahora) {
        return estado == EstadoReserva.PENDIENTE && ahora.isAfter(fechaCreacion.plusHours(24));
    }

    public boolean cancelarPorVencimiento(LocalDateTime ahora) {
        boolean cancelada = false;

        if (vencioPlazoConfirmacion(ahora)) {
            valorDevolucion = theFolio.calcularTotalPagos();
            estado = EstadoReserva.CANCELADA;
            cancelada = true;
        }

        return cancelada;
    }

    public boolean hacerCheckIn(LocalDate hoy) {
        boolean realizado = false;

        if (estado == EstadoReserva.CONFIRMADA && !hoy.isBefore(fechaEntrada)
                && theApartamento.getEstado() == EstadoApartamento.PREPARADO) {
            estado = EstadoReserva.EN_CURSO;
            theApartamento.cambiarEstado(EstadoApartamento.OCUPADO, "");
            realizado = true;
        }

        return realizado;
    }

    public boolean hacerCheckOut() {
        boolean realizado = false;

        if (estado == EstadoReserva.EN_CURSO) {
            estado = EstadoReserva.FINALIZADA;
            theApartamento.cambiarEstado(EstadoApartamento.PENDIENTE_PREPARACION, "");
            realizado = true;
        }

        return realizado;
    }

    public boolean declararNoShow(LocalDateTime ahora) {
        boolean declarado = false;
        LocalDateTime horaLimite = fechaEntrada.atTime(22, 0);

        if (estado == EstadoReserva.CONFIRMADA && !ahora.isBefore(horaLimite)) {
            valorDevolucion = theFolio.calcularTotalPagos() - valorEstancia;
            if (valorDevolucion < 0) {
                valorDevolucion = 0;
            }
            estado = EstadoReserva.NO_SHOW;
            declarado = true;
        }

        return declarado;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public int getValorEstancia() {
        return valorEstancia;
    }

    public int getValorDevolucion() {
        return valorDevolucion;
    }

    public Apartamento getTheApartamento() {
        return theApartamento;
    }

    public Huesped getTheTitular() {
        return theTitular;
    }

    public LinkedList<Ocupante> getListOcupantes() {
        return listOcupantes;
    }

    public Folio getTheFolio() {
        return theFolio;
    }

    public PoliticaCancelacion getThePolitica() {
        return thePolitica;
    }

    @Override
    public String toString() {
        return codigo + " - " + obtenerCanal() + " - " + theApartamento.getCodigo() + " - "
                + fechaEntrada + " a " + fechaSalida + " - " + estado;
    }
}
