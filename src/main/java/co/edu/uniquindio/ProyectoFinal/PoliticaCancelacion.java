package co.edu.uniquindio.ProyectoFinal;

public record PoliticaCancelacion(int diasDevolucionTotal, int diasRetencionParcial, int porcentajeRetencionParcial) {

    public int calcularPorcentajeRetencion(int diasAntelacion) {
        int porcentaje = 100;

        if (diasAntelacion > diasDevolucionTotal) {
            porcentaje = 0;
        } else if (diasAntelacion >= diasRetencionParcial) {
            porcentaje = porcentajeRetencionParcial;
        }

        return porcentaje;
    }
}
