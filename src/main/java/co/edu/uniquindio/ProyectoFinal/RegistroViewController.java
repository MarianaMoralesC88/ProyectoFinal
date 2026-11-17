package co.edu.uniquindio.ProyectoFinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroViewController {

    private App app;
    private LoginController loginController;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDocumento;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private DatePicker dpFechaNacimiento;

    public void setApp(App app) {
        this.app = app;
        loginController = new LoginController(app.getAlojamiento());
    }

    @FXML
    void onRegistrar(ActionEvent event) {
        if (txtNombre.getText().isBlank() || txtDocumento.getText().isBlank() || txtUsuario.getText().isBlank()
                || txtContrasena.getText().isBlank() || dpFechaNacimiento.getValue() == null) {
            mostrarMensaje("Complete nombre, documento, usuario, contrasena y fecha de nacimiento",
                    Alert.AlertType.WARNING);
        } else {
            Huesped nuevoHuesped = new Huesped(txtNombre.getText(), txtDocumento.getText(), txtTelefono.getText(),
                    txtCorreo.getText(), txtUsuario.getText(), txtContrasena.getText(), dpFechaNacimiento.getValue());

            if (loginController.registrarHuesped(nuevoHuesped)) {
                mostrarMensaje("Huesped registrado", Alert.AlertType.INFORMATION);
                volver();
            } else {
                mostrarMensaje("No se hizo el registro: el documento o el usuario ya existen", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void onVolver(ActionEvent event) {
        volver();
    }

    private void volver() {
        if (app.getUsuarioActual() != null && app.getUsuarioActual().obtenerRol() == Rol.RECEPCIONISTA) {
            app.openViewRecepcion();
        } else {
            app.openViewLogin();
        }
    }

    private void mostrarMensaje(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("SGA");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
