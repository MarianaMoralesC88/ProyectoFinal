package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDateTime;

public class LoginController {
    private Alojamiento alojamiento;

    public LoginController(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public Persona autenticar(String usuario, String contrasena) {
        return alojamiento.autenticar(usuario, contrasena);
    }

    public int cancelarReservasVencidas(LocalDateTime ahora) {
        return alojamiento.cancelarReservasVencidas(ahora);
    }

    public boolean registrarHuesped(Huesped huesped) {
        return alojamiento.registrarHuesped(huesped);
    }
}
