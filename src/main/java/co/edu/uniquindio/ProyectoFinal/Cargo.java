package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public class Cargo implements IMovimiento {
    private TipoCargo tipo;
    private String descripcion;
    private int valor;
    private LocalDate fecha;

    public Cargo(TipoCargo tipo, String descripcion, int valor, LocalDate fecha) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.valor = valor;
        this.fecha = fecha;
    }

    @Override
    public String obtenerDescripcion() {
        return "Cargo " + tipo + ": " + descripcion;
    }

    public TipoCargo getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
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
