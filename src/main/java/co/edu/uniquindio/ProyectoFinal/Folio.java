package co.edu.uniquindio.ProyectoFinal;

import java.util.LinkedList;

public class Folio {
    private LinkedList<Cargo> listCargos;
    private LinkedList<Pago> listPagos;

    public Folio() {
        listCargos = new LinkedList<>();
        listPagos = new LinkedList<>();
    }

    public boolean agregarCargo(Cargo cargo) {
        boolean agregado = false;

        if (cargo != null && cargo.getValor() != 0) {
            listCargos.add(cargo);
            agregado = true;
        }

        return agregado;
    }

    public boolean registrarPago(Pago pago) {
        boolean registrado = false;

        if (pago != null && pago.getValor() != 0 && pago.getMedioPago() != null) {
            listPagos.add(pago);
            registrado = true;
        }

        return registrado;
    }

    public int calcularTotalCargos() {
        int total = 0;

        for (Cargo cargo : listCargos) {
            total += cargo.getValor();
        }

        return total;
    }

    public int calcularTotalPagos() {
        int total = 0;

        for (Pago pago : listPagos) {
            total += pago.getValor();
        }

        return total;
    }

    public int calcularSaldo() {
        return calcularTotalCargos() - calcularTotalPagos();
    }

    public LinkedList<IMovimiento> obtenerMovimientos() {
        LinkedList<IMovimiento> movimientos = new LinkedList<>();

        movimientos.addAll(listCargos);
        movimientos.addAll(listPagos);

        return movimientos;
    }

    public LinkedList<Cargo> getListCargos() {
        return listCargos;
    }

    public LinkedList<Pago> getListPagos() {
        return listPagos;
    }
}
