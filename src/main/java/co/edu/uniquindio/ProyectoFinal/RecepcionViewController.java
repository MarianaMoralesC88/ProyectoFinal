package co.edu.uniquindio.ProyectoFinal;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.LinkedList;

public class RecepcionViewController {

    private App app;
    private RecepcionController recepcionController;
    private LinkedList<Ocupante> listOcupantes;

    @FXML
    private Label lblUsuario;

    @FXML
    private TableView<Reserva> tblReservas;

    @FXML
    private TableColumn<Reserva, String> clmCodigo;

    @FXML
    private TableColumn<Reserva, String> clmCanal;

    @FXML
    private TableColumn<Reserva, String> clmApartamento;

    @FXML
    private TableColumn<Reserva, String> clmTitular;

    @FXML
    private TableColumn<Reserva, String> clmEntrada;

    @FXML
    private TableColumn<Reserva, String> clmSalida;

    @FXML
    private TableColumn<Reserva, String> clmEstado;

    @FXML
    private TableColumn<Reserva, String> clmSaldo;

    @FXML
    private TextField txtValorPago;

    @FXML
    private ComboBox<MedioPago> cbxMedioPago;

    @FXML
    private TextField txtDescripcionCargo;

    @FXML
    private TextField txtValorCargo;

    @FXML
    private TextArea txtFolio;

    @FXML
    private ComboBox<String> cbxCanal;

    @FXML
    private TextField txtDocumentoTitular;

    @FXML
    private ComboBox<Apartamento> cbxApartamento;

    @FXML
    private DatePicker dpEntrada;

    @FXML
    private DatePicker dpSalida;

    @FXML
    private TextField txtCodigoExterno;

    @FXML
    private TextField txtOcupanteNombre;

    @FXML
    private DatePicker dpOcupanteNacimiento;

    @FXML
    private TableView<Ocupante> tblOcupantes;

    @FXML
    private TableColumn<Ocupante, String> clmOcupanteNombre;

    @FXML
    private TableColumn<Ocupante, String> clmOcupanteNacimiento;

    @FXML
    private TextArea txtConflictos;

    @FXML
    private TableView<Apartamento> tblApartamentos;

    @FXML
    private TableColumn<Apartamento, String> clmAptoCodigo;

    @FXML
    private TableColumn<Apartamento, String> clmAptoNombre;

    @FXML
    private TableColumn<Apartamento, String> clmAptoEstado;

    @FXML
    private TableColumn<Apartamento, String> clmAptoMotivo;

    @FXML
    private ComboBox<EstadoApartamento> cbxEstado;

    @FXML
    private TextField txtMotivo;

    @FXML
    private TextField txtNovedad;

    @FXML
    void initialize() {
        listOcupantes = new LinkedList<>();
        cbxMedioPago.getItems().addAll(MedioPago.values());
        cbxEstado.getItems().addAll(EstadoApartamento.values());
        cbxCanal.getItems().add("DIRECTA");
        cbxCanal.getItems().add("EXTERNA");
        cbxCanal.setValue("DIRECTA");

        clmCodigo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCodigo()));
        clmCanal.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().obtenerCanal()));
        clmApartamento.setCellValueFactory(dato -> new SimpleStringProperty(
                dato.getValue().getTheApartamento().getCodigo()));
        clmTitular.setCellValueFactory(dato -> new SimpleStringProperty(
                dato.getValue().getTheTitular().getNombre()));
        clmEntrada.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getFechaEntrada())));
        clmSalida.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getFechaSalida())));
        clmEstado.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getEstado())));
        clmSaldo.setCellValueFactory(dato -> new SimpleStringProperty(
                "$" + dato.getValue().getTheFolio().calcularSaldo()));

        clmOcupanteNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().nombre()));
        clmOcupanteNacimiento.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().fechaNacimiento())));

        clmAptoCodigo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCodigo()));
        clmAptoNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombre()));
        clmAptoEstado.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getEstado())));
        clmAptoMotivo.setCellValueFactory(dato -> new SimpleStringProperty(
                dato.getValue().getMotivoFueraServicio()));

        tblReservas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarFolio(newValue));
    }

    public void setApp(App app) {
        this.app = app;
        recepcionController = new RecepcionController(app.getAlojamiento());
        lblUsuario.setText("Recepcionista: " + app.getUsuarioActual().getNombre());
        cbxApartamento.getItems().addAll(recepcionController.obtenerApartamentos());
        cargarReservas();
        cargarApartamentos();
        cargarConflictos();
    }

    private void cargarReservas() {
        tblReservas.getItems().clear();
        tblReservas.getItems().addAll(recepcionController.obtenerReservas());
        tblReservas.refresh();
    }

    private void cargarApartamentos() {
        tblApartamentos.getItems().clear();
        tblApartamentos.getItems().addAll(recepcionController.obtenerApartamentos());
        tblApartamentos.refresh();
    }

    private void cargarOcupantes() {
        tblOcupantes.getItems().clear();
        tblOcupantes.getItems().addAll(listOcupantes);
    }

    private void cargarConflictos() {
        String texto = "Conflictos de canal: " + recepcionController.obtenerConflictos().size() + "\n";

        for (ConflictoCanal conflicto : recepcionController.obtenerConflictos()) {
            texto += conflicto.codigoExterno() + " - " + conflicto.codigoApartamento() + " - "
                    + conflicto.fechaEntrada() + " a " + conflicto.fechaSalida() + " - " + conflicto.motivo() + "\n";
        }

        txtConflictos.setText(texto);
    }

    private void actualizarVista(Reserva reserva) {
        cargarReservas();
        cargarApartamentos();
        mostrarFolio(reserva);
    }

    @FXML
    void onConfirmar(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (recepcionController.confirmarReserva(reserva.getCodigo())) {
            mostrarMensaje("Reserva CONFIRMADA", Alert.AlertType.INFORMATION);
            actualizarVista(reserva);
        } else {
            mostrarMensaje("No se confirmo: la reserva debe estar PENDIENTE y tener pagado el anticipo de $"
                    + reserva.calcularAnticipoRequerido(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onCheckIn(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (recepcionController.registrarCheckIn(reserva.getCodigo())) {
            mostrarMensaje("Check-in registrado. La reserva esta EN CURSO", Alert.AlertType.INFORMATION);
            actualizarVista(reserva);
        } else {
            mostrarMensaje("No se hizo el check-in: la reserva debe estar CONFIRMADA, debe haber llegado"
                    + " la fecha de entrada y el apartamento debe estar PREPARADO", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onCheckOut(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (recepcionController.registrarCheckOut(reserva.getCodigo())) {
            mostrarMensaje("Check-out registrado. La reserva esta FINALIZADA y el apartamento queda"
                    + " PENDIENTE DE PREPARACION", Alert.AlertType.INFORMATION);
            actualizarVista(reserva);
        } else {
            mostrarMensaje("No se hizo el check-out: la reserva debe estar EN CURSO", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onNoShow(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (recepcionController.declararNoShow(reserva.getCodigo())) {
            mostrarMensaje("Reserva declarada NO-SHOW. Se retiene el 100 %", Alert.AlertType.INFORMATION);
            actualizarVista(reserva);
        } else {
            mostrarMensaje("No se declaro: la reserva debe estar CONFIRMADA y deben haber pasado"
                    + " las 10:00 p. m. del dia de entrada", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onCancelarReserva(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (recepcionController.cancelarReserva(reserva.getCodigo())) {
            mostrarMensaje("Reserva CANCELADA. Valor a devolver: $" + reserva.getValorDevolucion(),
                    Alert.AlertType.INFORMATION);
            actualizarVista(reserva);
        } else {
            mostrarMensaje("Solo se puede cancelar una reserva PENDIENTE o CONFIRMADA", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onRegistrarPago(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();
        int valor = leerEntero(txtValorPago);

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (valor <= 0 || cbxMedioPago.getValue() == null) {
            mostrarMensaje("Ingrese el valor y el medio de pago", Alert.AlertType.WARNING);
        } else if (recepcionController.registrarPago(reserva.getCodigo(), cbxMedioPago.getValue(), valor)) {
            mostrarMensaje("Pago registrado", Alert.AlertType.INFORMATION);
            txtValorPago.clear();
            actualizarVista(reserva);
        } else {
            mostrarMensaje("No se registro el pago: la reserva ya no esta activa", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onRegistrarCargo(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();
        int valor = leerEntero(txtValorCargo);

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (valor <= 0 || txtDescripcionCargo.getText().isBlank()) {
            mostrarMensaje("Ingrese la descripcion y el valor del servicio", Alert.AlertType.WARNING);
        } else if (recepcionController.registrarCargoServicio(reserva.getCodigo(),
                txtDescripcionCargo.getText(), valor)) {
            mostrarMensaje("Cargo registrado", Alert.AlertType.INFORMATION);
            txtDescripcionCargo.clear();
            txtValorCargo.clear();
            actualizarVista(reserva);
        } else {
            mostrarMensaje("No se registro el cargo: la reserva ya no esta activa", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onAgregarOcupante(ActionEvent event) {
        if (txtOcupanteNombre.getText().isBlank() || dpOcupanteNacimiento.getValue() == null) {
            mostrarMensaje("Ingrese el nombre y la fecha de nacimiento del ocupante", Alert.AlertType.WARNING);
        } else {
            listOcupantes.add(new Ocupante(txtOcupanteNombre.getText(), dpOcupanteNacimiento.getValue()));
            txtOcupanteNombre.clear();
            dpOcupanteNacimiento.setValue(null);
            cargarOcupantes();
        }
    }

    @FXML
    void onLimpiarOcupantes(ActionEvent event) {
        listOcupantes = new LinkedList<>();
        cargarOcupantes();
    }

    @FXML
    void onCrearReserva(ActionEvent event) {
        Apartamento apartamento = cbxApartamento.getValue();

        if (txtDocumentoTitular.getText().isBlank() || apartamento == null
                || dpEntrada.getValue() == null || dpSalida.getValue() == null) {
            mostrarMensaje("Ingrese el documento del titular, el apartamento y las fechas", Alert.AlertType.WARNING);
        } else if (cbxCanal.getValue().equals("EXTERNA") && txtCodigoExterno.getText().isBlank()) {
            mostrarMensaje("Ingrese el codigo de la plataforma externa", Alert.AlertType.WARNING);
        } else {
            Reserva reserva;

            if (cbxCanal.getValue().equals("EXTERNA")) {
                reserva = recepcionController.recibirReservaExterna(txtCodigoExterno.getText(),
                        txtDocumentoTitular.getText(), apartamento.getCodigo(), listOcupantes,
                        dpEntrada.getValue(), dpSalida.getValue());
            } else {
                reserva = recepcionController.crearReservaDirecta(txtDocumentoTitular.getText(),
                        apartamento.getCodigo(), listOcupantes, dpEntrada.getValue(), dpSalida.getValue());
            }

            if (reserva != null) {
                mostrarMensaje("Reserva " + reserva.getCodigo() + " creada en estado PENDIENTE."
                        + "\nValor: $" + reserva.getValorEstancia()
                        + "\nComision del canal: $" + reserva.calcularComision(), Alert.AlertType.INFORMATION);
                listOcupantes = new LinkedList<>();
                cargarOcupantes();
                txtCodigoExterno.clear();
                cargarReservas();
            } else {
                mostrarMensaje("No se creo la reserva. Revise que el huesped este registrado, las fechas,"
                        + " la capacidad y la disponibilidad del apartamento.", Alert.AlertType.ERROR);
            }

            cargarConflictos();
        }
    }

    @FXML
    void onRegistrarHuesped(ActionEvent event) {
        app.openViewRegistro();
    }

    @FXML
    void onCambiarEstado(ActionEvent event) {
        Apartamento apartamento = tblApartamentos.getSelectionModel().getSelectedItem();

        if (apartamento == null || cbxEstado.getValue() == null) {
            mostrarMensaje("Seleccione un apartamento de la tabla y el nuevo estado", Alert.AlertType.WARNING);
        } else if (recepcionController.cambiarEstadoApartamento(apartamento.getCodigo(), cbxEstado.getValue(),
                txtMotivo.getText())) {
            mostrarMensaje("Estado actualizado", Alert.AlertType.INFORMATION);
            txtMotivo.clear();
            cargarApartamentos();
        } else {
            mostrarMensaje("Para pasar a FUERA DE SERVICIO debe escribir el motivo", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onRegistrarNovedad(ActionEvent event) {
        Apartamento apartamento = tblApartamentos.getSelectionModel().getSelectedItem();

        if (apartamento == null || txtNovedad.getText().isBlank()) {
            mostrarMensaje("Seleccione un apartamento de la tabla y escriba la novedad", Alert.AlertType.WARNING);
        } else if (recepcionController.registrarNovedad(apartamento.getCodigo(), txtNovedad.getText())) {
            mostrarMensaje("Novedad registrada. El apartamento tiene "
                    + apartamento.getListNovedades().size() + " novedades", Alert.AlertType.INFORMATION);
            txtNovedad.clear();
        }
    }

    @FXML
    void onCerrarSesion(ActionEvent event) {
        app.openViewLogin();
    }

    private void mostrarFolio(Reserva reserva) {
        if (reserva != null) {
            String texto = reserva + "\nTitular: " + reserva.getTheTitular() + " - Personas: "
                    + reserva.contarPersonas() + "\n\nMovimientos del folio:\n";

            for (IMovimiento movimiento : reserva.getTheFolio().obtenerMovimientos()) {
                texto += movimiento.getFecha() + " - " + movimiento.obtenerDescripcion()
                        + " - $" + movimiento.getValor() + "\n";
            }

            texto += "\nTotal cargos: $" + reserva.getTheFolio().calcularTotalCargos();
            texto += "\nTotal pagos: $" + reserva.getTheFolio().calcularTotalPagos();
            texto += "\nSaldo: $" + reserva.getTheFolio().calcularSaldo();
            texto += "\nAnticipo requerido: $" + reserva.calcularAnticipoRequerido();
            texto += "\nComision del canal: $" + reserva.calcularComision();

            if (reserva.getEstado() == EstadoReserva.CANCELADA || reserva.getEstado() == EstadoReserva.NO_SHOW) {
                texto += "\nValor a devolver: $" + reserva.getValorDevolucion();
            }

            txtFolio.setText(texto);
        }
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
