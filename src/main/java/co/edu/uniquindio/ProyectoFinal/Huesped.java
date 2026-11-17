package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;

public class Huesped extends Persona {
    private LocalDate fechaNacimiento;
    private String rutaFoto;

    public Huesped(String nombre, String documento, String telefono, String correo,
                   String usuario, String contrasena, LocalDate fechaNacimiento) {
        super(nombre, documento, telefono, correo, usuario, contrasena);
        this.fechaNacimiento = fechaNacimiento;
        rutaFoto = "";
    }

    @Override
    public Rol obtenerRol() {
        return Rol.HUESPED;
    }

    public int calcularEdad(LocalDate fecha) {
        int edad = 0;

        while (!fechaNacimiento.plusYears(edad + 1).isAfter(fecha)) {
            edad++;
        }

        return edad;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }
}
