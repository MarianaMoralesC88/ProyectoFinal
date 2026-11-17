package co.edu.uniquindio.ProyectoFinal;

public class Recepcionista extends Persona {
    private boolean activo;

    public Recepcionista(String nombre, String documento, String telefono, String correo,
                         String usuario, String contrasena) {
        super(nombre, documento, telefono, correo, usuario, contrasena);
        activo = true;
    }

    @Override
    public Rol obtenerRol() {
        return Rol.RECEPCIONISTA;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
