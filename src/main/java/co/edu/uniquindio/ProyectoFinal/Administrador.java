package co.edu.uniquindio.ProyectoFinal;

public class Administrador extends Persona {

    public Administrador(String nombre, String documento, String telefono, String correo,
                         String usuario, String contrasena) {
        super(nombre, documento, telefono, correo, usuario, contrasena);
    }

    @Override
    public Rol obtenerRol() {
        return Rol.ADMINISTRADOR;
    }
}
