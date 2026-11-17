package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;
import java.util.LinkedList;

public class Apartamento {
    private String codigo;
    private String nombre;
    private int numeroDormitorios;
    private int capacidadMaxima;
    private String caracteristicas;
    private int tarifaBaja;
    private int tarifaMedia;
    private int tarifaAlta;
    private EstadoApartamento estado;
    private String motivoFueraServicio;
    private boolean activo;

    private LinkedList<Bloqueo> listBloqueos;
    private LinkedList<Novedad> listNovedades;

    public Apartamento(String codigo, String nombre, int numeroDormitorios, int capacidadMaxima,
                       String caracteristicas, int tarifaBaja, int tarifaMedia, int tarifaAlta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.numeroDormitorios = numeroDormitorios;
        this.capacidadMaxima = capacidadMaxima;
        this.caracteristicas = caracteristicas;
        this.tarifaBaja = tarifaBaja;
        this.tarifaMedia = tarifaMedia;
        this.tarifaAlta = tarifaAlta;
        estado = EstadoApartamento.PREPARADO;
        motivoFueraServicio = "";
        activo = true;
        listBloqueos = new LinkedList<>();
        listNovedades = new LinkedList<>();
    }

    public int obtenerTarifa(TipoTemporada temporada) {
        int tarifa = tarifaBaja;

        if (temporada == TipoTemporada.MEDIA) {
            tarifa = tarifaMedia;
        } else if (temporada == TipoTemporada.ALTA) {
            tarifa = tarifaAlta;
        }

        return tarifa;
    }

    public boolean actualizarTarifas(int tarifaBaja, int tarifaMedia, int tarifaAlta) {
        boolean actualizado = false;

        if (tarifaBaja > 0 && tarifaMedia > 0 && tarifaAlta > 0) {
            this.tarifaBaja = tarifaBaja;
            this.tarifaMedia = tarifaMedia;
            this.tarifaAlta = tarifaAlta;
            actualizado = true;
        }

        return actualizado;
    }

    public boolean cambiarEstado(EstadoApartamento nuevoEstado, String motivo) {
        boolean cambiado = false;

        if (nuevoEstado == EstadoApartamento.FUERA_DE_SERVICIO) {
            if (motivo != null && !motivo.isBlank()) {
                estado = nuevoEstado;
                motivoFueraServicio = motivo;
                cambiado = true;
            }
        } else if (nuevoEstado != null) {
            estado = nuevoEstado;
            motivoFueraServicio = "";
            cambiado = true;
        }

        return cambiado;
    }

    public boolean registrarBloqueo(Bloqueo bloqueo) {
        boolean registrado = false;

        if (bloqueo != null && !bloqueo.fechaFin().isBefore(bloqueo.fechaInicio())
                && bloqueo.motivo() != null && !bloqueo.motivo().isBlank()) {
            listBloqueos.add(bloqueo);
            registrado = true;
        }

        return registrado;
    }

    public boolean estaBloqueado(LocalDate noche) {
        boolean bloqueado = false;

        for (Bloqueo bloqueo : listBloqueos) {
            if (bloqueo.incluyeNoche(noche)) {
                bloqueado = true;
            }
        }

        return bloqueado;
    }

    public boolean registrarNovedad(Novedad novedad) {
        boolean registrada = false;

        if (novedad != null && novedad.descripcion() != null && !novedad.descripcion().isBlank()) {
            listNovedades.add(novedad);
            registrada = true;
        }

        return registrada;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroDormitorios() {
        return numeroDormitorios;
    }

    public void setNumeroDormitorios(int numeroDormitorios) {
        this.numeroDormitorios = numeroDormitorios;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public int getTarifaBaja() {
        return tarifaBaja;
    }

    public int getTarifaMedia() {
        return tarifaMedia;
    }

    public int getTarifaAlta() {
        return tarifaAlta;
    }

    public EstadoApartamento getEstado() {
        return estado;
    }

    public String getMotivoFueraServicio() {
        return motivoFueraServicio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LinkedList<Bloqueo> getListBloqueos() {
        return listBloqueos;
    }

    public LinkedList<Novedad> getListNovedades() {
        return listNovedades;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
