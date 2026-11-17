package co.edu.uniquindio.ProyectoFinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class LoginViewController {

    private App app;
    private LoginController loginController;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    public void setApp(App app) {
        this.app = app;
        loginController = new LoginController(app.getAlojamiento());
    }

    @FXML
    void onIngresar(ActionEvent event) {
        Persona persona = loginController.autenticar(txtUsuario.getText(), txtContrasena.getText());

        if (persona != null) {
            loginController.cancelarReservasVencidas(LocalDateTime.now());
            app.setUsuarioActual(persona);

            if (persona.obtenerRol() == Rol.HUESPED) {
                app.openViewHuesped();
            } else if (persona.obtenerRol() == Rol.RECEPCIONISTA) {
                app.openViewRecepcion();
            } else {
                app.openViewAdmin();
            }
        } else {
            mostrarMensaje("Usuario o contrasena incorrectos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onRegistrarme(ActionEvent event) {
        app.openViewRegistro();
    }

    private void mostrarMensaje(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("SGA");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
