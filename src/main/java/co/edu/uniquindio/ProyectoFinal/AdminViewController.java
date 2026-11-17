package co.edu.uniquindio.ProyectoFinal;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminViewController {

    private App app;
    private AdminController adminController;

    @FXML
    private Label lblUsuario;

    @FXML
    private TableView<Apartamento> tblApartamentos;

    @FXML
    private TableColumn<Apartamento, String> clmCodigo;

    @FXML
    private TableColumn<Apartamento, String> clmNombre;

    @FXML
    private TableColumn<Apartamento, String> clmDormitorios;

    @FXML
    private TableColumn<Apartamento, String> clmCapacidad;

    @FXML
    private TableColumn<Apartamento, String> clmTarifas;

    @FXML
    private TableColumn<Apartamento, String> clmActivo;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDormitorios;

    @FXML
    private TextField txtCapacidad;

    @FXML
    private TextField txtCaracteristicas;

    @FXML
    private TextField txtTarifaBaja;

    @FXML
    private TextField txtTarifaMedia;

    @FXML
    private TextField txtTarifaAlta;

    @FXML
    private DatePicker dpBloqueoInicio;

    @FXML
    private DatePicker dpBloqueoFin;

    @FXML
    private TextField txtMotivoBloqueo;

    @FXML
    private TableView<Recepcionista> tblEmpleados;

    @FXML
    private TableColumn<Recepcionista, String> clmEmpDocumento;

    @FXML
    private TableColumn<Recepcionista, String> clmEmpNombre;

    @FXML
    private TableColumn<Recepcionista, String> clmEmpTelefono;

    @FXML
    private TableColumn<Recepcionista, String> clmEmpCorreo;

    @FXML
    private TableColumn<Recepcionista, String> clmEmpUsuario;

    @FXML
    private TableColumn<Recepcionista, String> clmEmpActivo;

    @FXML
    private TextField txtEmpNombre;

    @FXML
    private TextField txtEmpDocumento;

    @FXML
    private TextField txtEmpTelefono;

    @FXML
    private TextField txtEmpCorreo;

    @FXML
    private TextField txtEmpUsuario;

    @FXML
    private PasswordField txtEmpContrasena;

    @FXML
    private ComboBox<TipoTemporada> cbxTipoTemporada;

    @FXML
    private TextField txtNombreTemporada;

    @FXML
    private DatePicker dpTemporadaInicio;

    @FXML
    private DatePicker dpTemporadaFin;

    @FXML
    private TableView<Temporada> tblTemporadas;

    @FXML
    private TableColumn<Temporada, String> clmTempTipo;

    @FXML
    private TableColumn<Temporada, String> clmTempNombre;

    @FXML
    private TableColumn<Temporada, String> clmTempInicio;

    @FXML
    private TableColumn<Temporada, String> clmTempFin;

    @FXML
    private TextField txtDiasTotal;

    @FXML
    private TextField txtDiasParcial;

    @FXML
    private TextField txtPorcentaje;

    @FXML
    private DatePicker dpDesde;

    @FXML
    private DatePicker dpHasta;

    @FXML
    private TextArea txtReporte;

    @FXML
    void initialize() {
        cbxTipoTemporada.getItems().add(TipoTemporada.MEDIA);
        cbxTipoTemporada.getItems().add(TipoTemporada.ALTA);

        clmCodigo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCodigo()));
        clmNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombre()));
        clmDormitorios.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getNumeroDormitorios())));
        clmCapacidad.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getCapacidadMaxima())));
        clmTarifas.setCellValueFactory(dato -> new SimpleStringProperty(
                "$" + dato.getValue().getTarifaBaja() + " / $" + dato.getValue().getTarifaMedia()
                        + " / $" + dato.getValue().getTarifaAlta()));
        clmActivo.setCellValueFactory(dato -> new SimpleStringProperty(textoActivo(dato.getValue().isActivo())));

        clmEmpDocumento.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getDocumento()));
        clmEmpNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombre()));
        clmEmpTelefono.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getTelefono()));
        clmEmpCorreo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCorreo()));
        clmEmpUsuario.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getUsuario()));
        clmEmpActivo.setCellValueFactory(dato -> new SimpleStringProperty(textoActivo(dato.getValue().isActivo())));

        clmTempTipo.setCellValueFactory(dato -> new SimpleStringProperty(String.valueOf(dato.getValue().getTipo())));
        clmTempNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombre()));
        clmTempInicio.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getFechaInicio())));
        clmTempFin.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getFechaFin())));

        tblApartamentos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarApartamento(newValue));
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarEmpleado(newValue));
    }

    public void setApp(App app) {
        this.app = app;
        adminController = new AdminController(app.getAlojamiento());
        lblUsuario.setText("Administrador: " + app.getUsuarioActual().getNombre());
        cargarApartamentos();
        cargarEmpleados();
        cargarTemporadas();
        cargarPolitica();
    }

    private String textoActivo(boolean activo) {
        String texto = "No";

        if (activo) {
            texto = "Si";
        }

        return texto;
    }

    private void cargarApartamentos() {
        tblApartamentos.getItems().clear();
        tblApartamentos.getItems().addAll(adminController.obtenerApartamentos());
        tblApartamentos.refresh();
    }

    private void cargarEmpleados() {
        tblEmpleados.getItems().clear();
        tblEmpleados.getItems().addAll(adminController.obtenerRecepcionistas());
        tblEmpleados.refresh();
    }

    private void cargarTemporadas() {
        tblTemporadas.getItems().clear();
        tblTemporadas.getItems().addAll(adminController.obtenerTemporadas());
    }

    private void cargarPolitica() {
        PoliticaCancelacion politica = adminController.obtenerPolitica();
        txtDiasTotal.setText(String.valueOf(politica.diasDevolucionTotal()));
        txtDiasParcial.setText(String.valueOf(politica.diasRetencionParcial()));
        txtPorcentaje.setText(String.valueOf(politica.porcentajeRetencionParcial()));
    }

    private void mostrarApartamento(Apartamento apartamento) {
        if (apartamento != null) {
            txtCodigo.setText(apartamento.getCodigo());
            txtNombre.setText(apartamento.getNombre());
            txtDormitorios.setText(String.valueOf(apartamento.getNumeroDormitorios()));
            txtCapacidad.setText(String.valueOf(apartamento.getCapacidadMaxima()));
            txtCaracteristicas.setText(apartamento.getCaracteristicas());
            txtTarifaBaja.setText(String.valueOf(apartamento.getTarifaBaja()));
            txtTarifaMedia.setText(String.valueOf(apartamento.getTarifaMedia()));
            txtTarifaAlta.setText(String.valueOf(apartamento.getTarifaAlta()));
        }
    }

    private void mostrarEmpleado(Recepcionista recepcionista) {
        if (recepcionista != null) {
            txtEmpDocumento.setText(recepcionista.getDocumento());
            txtEmpNombre.setText(recepcionista.getNombre());
            txtEmpTelefono.setText(recepcionista.getTelefono());
            txtEmpCorreo.setText(recepcionista.getCorreo());
            txtEmpUsuario.setText(recepcionista.getUsuario());
        }
    }

    @FXML
    void onAgregarApartamento(ActionEvent event) {
        if (txtCodigo.getText().isBlank() || txtNombre.getText().isBlank() || leerEntero(txtDormitorios) < 0) {
            mostrarMensaje("Ingrese el codigo, el nombre y el numero de dormitorios", Alert.AlertType.WARNING);
        } else {
            Apartamento nuevoApartamento = new Apartamento(txtCodigo.getText(), txtNombre.getText(),
                    leerEntero(txtDormitorios), leerEntero(txtCapacidad), txtCaracteristicas.getText(),
                    leerEntero(txtTarifaBaja), leerEntero(txtTarifaMedia), leerEntero(txtTarifaAlta));

            if (adminController.registrarApartamento(nuevoApartamento)) {
                mostrarMensaje("Apartamento registrado", Alert.AlertType.INFORMATION);
                cargarApartamentos();
            } else {
                mostrarMensaje("No se registro: el codigo ya existe o la capacidad y las tarifas no son validas",
                        Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void onActualizarApartamento(ActionEvent event) {
        if (adminController.actualizarApartamento(txtCodigo.getText(), txtNombre.getText(),
                leerEntero(txtDormitorios), leerEntero(txtCapacidad), txtCaracteristicas.getText())) {
            mostrarMensaje("Apartamento actualizado", Alert.AlertType.INFORMATION);
            cargarApartamentos();
        } else {
            mostrarMensaje("No se actualizo: el codigo no existe o la capacidad no es valida", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onEliminarApartamento(ActionEvent event) {
        if (adminController.eliminarApartamento(txtCodigo.getText())) {
            mostrarMensaje("Apartamento eliminado. Queda inactivo y ya no se puede reservar",
                    Alert.AlertType.INFORMATION);
            cargarApartamentos();
        } else {
            mostrarMensaje("No se elimino: el codigo no existe, ya esta inactivo o tiene reservas activas",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onActualizarTarifas(ActionEvent event) {
        if (adminController.actualizarTarifas(txtCodigo.getText(), leerEntero(txtTarifaBaja),
                leerEntero(txtTarifaMedia), leerEntero(txtTarifaAlta))) {
            mostrarMensaje("Tarifas actualizadas. Las reservas ya creadas conservan su valor",
                    Alert.AlertType.INFORMATION);
            cargarApartamentos();
        } else {
            mostrarMensaje("No se actualizaron: el codigo no existe o las tarifas no son validas",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onRegistrarBloqueo(ActionEvent event) {
        if (dpBloqueoInicio.getValue() == null || dpBloqueoFin.getValue() == null
                || txtMotivoBloqueo.getText().isBlank()) {
            mostrarMensaje("Ingrese las fechas y el motivo del bloqueo", Alert.AlertType.WARNING);
        } else if (adminController.registrarBloqueo(txtCodigo.getText(), dpBloqueoInicio.getValue(),
                dpBloqueoFin.getValue(), txtMotivoBloqueo.getText())) {
            mostrarMensaje("Bloqueo registrado", Alert.AlertType.INFORMATION);
            txtMotivoBloqueo.clear();
        } else {
            mostrarMensaje("No se registro: el codigo no existe, las fechas no son validas"
                    + " o hay una reserva activa en ese rango", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onContratar(ActionEvent event) {
        if (txtEmpNombre.getText().isBlank() || txtEmpDocumento.getText().isBlank()
                || txtEmpUsuario.getText().isBlank() || txtEmpContrasena.getText().isBlank()) {
            mostrarMensaje("Ingrese nombre, documento, usuario y contrasena", Alert.AlertType.WARNING);
        } else {
            Recepcionista nuevoRecepcionista = new Recepcionista(txtEmpNombre.getText(), txtEmpDocumento.getText(),
                    txtEmpTelefono.getText(), txtEmpCorreo.getText(), txtEmpUsuario.getText(),
                    txtEmpContrasena.getText());

            if (adminController.contratarRecepcionista(nuevoRecepcionista)) {
                mostrarMensaje("Recepcionista contratado", Alert.AlertType.INFORMATION);
                txtEmpContrasena.clear();
                cargarEmpleados();
            } else {
                mostrarMensaje("No se contrato: el documento o el usuario ya existen", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void onActualizarEmpleado(ActionEvent event) {
        if (adminController.actualizarRecepcionista(txtEmpDocumento.getText(), txtEmpNombre.getText(),
                txtEmpTelefono.getText(), txtEmpCorreo.getText())) {
            mostrarMensaje("Datos actualizados", Alert.AlertType.INFORMATION);
            cargarEmpleados();
        } else {
            mostrarMensaje("No se actualizo: el documento no existe", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onDesvincular(ActionEvent event) {
        if (adminController.desvincularRecepcionista(txtEmpDocumento.getText())) {
            mostrarMensaje("Recepcionista desvinculado. Ya no puede iniciar sesion", Alert.AlertType.INFORMATION);
            cargarEmpleados();
        } else {
            mostrarMensaje("No se desvinculo: el documento no existe o ya esta inactivo", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onRegistrarTemporada(ActionEvent event) {
        if (cbxTipoTemporada.getValue() == null || txtNombreTemporada.getText().isBlank()
                || dpTemporadaInicio.getValue() == null || dpTemporadaFin.getValue() == null) {
            mostrarMensaje("Ingrese el tipo, el nombre y las fechas de la temporada", Alert.AlertType.WARNING);
        } else {
            Temporada nuevaTemporada = new Temporada(cbxTipoTemporada.getValue(), txtNombreTemporada.getText(),
                    dpTemporadaInicio.getValue(), dpTemporadaFin.getValue());

            if (adminController.registrarTemporada(nuevaTemporada)) {
                mostrarMensaje("Temporada registrada", Alert.AlertType.INFORMATION);
                txtNombreTemporada.clear();
                cargarTemporadas();
            } else {
                mostrarMensaje("No se registro: la fecha final no puede ser anterior a la inicial",
                        Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void onGuardarPolitica(ActionEvent event) {
        if (adminController.configurarPoliticaCancelacion(leerEntero(txtDiasTotal), leerEntero(txtDiasParcial),
                leerEntero(txtPorcentaje))) {
            mostrarMensaje("Politica guardada. Aplica para las reservas que se creen desde ahora",
                    Alert.AlertType.INFORMATION);
        } else {
            mostrarMensaje("Valores no validos: los dias deben ser positivos y el porcentaje entre 0 y 100",
                    Alert.AlertType.ERROR);
        }

        cargarPolitica();
    }

    @FXML
    void onGenerarReporte(ActionEvent event) {
        if (dpDesde.getValue() == null || dpHasta.getValue() == null
                || dpHasta.getValue().isBefore(dpDesde.getValue())) {
            mostrarMensaje("Seleccione un rango de fechas valido", Alert.AlertType.WARNING);
        } else {
            txtReporte.setText(adminController.generarReporte(dpDesde.getValue(), dpHasta.getValue()));
        }
    }

    @FXML
    void onCerrarSesion(ActionEvent event) {
        app.openViewLogin();
    }

    private int leerEntero(TextField campo) {
        int numero = -1;

        try {
            numero = Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException e) {
            numero = -1;
        }

        return numero;
    }

    private void mostrarMensaje(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("SGA");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
