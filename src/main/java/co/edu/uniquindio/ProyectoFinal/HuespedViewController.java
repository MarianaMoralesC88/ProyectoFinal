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

import java.time.LocalDate;
import java.util.LinkedList;

public class HuespedViewController {

    private App app;
    private HuespedController huespedController;
    private Huesped huesped;
    private LinkedList<Ocupante> listOcupantes;

    @FXML
    private Label lblUsuario;

    @FXML
    private DatePicker dpEntrada;

    @FXML
    private DatePicker dpSalida;

    @FXML
    private TextField txtPersonas;

    @FXML
    private TableView<Apartamento> tblApartamentos;

    @FXML
    private TableColumn<Apartamento, String> clmAptoCodigo;

    @FXML
    private TableColumn<Apartamento, String> clmAptoNombre;

    @FXML
    private TableColumn<Apartamento, String> clmAptoCapacidad;

    @FXML
    private TableColumn<Apartamento, String> clmAptoCaracteristicas;

    @FXML
    private TableColumn<Apartamento, String> clmAptoTarifas;

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
    private TextArea txtCotizacion;

    @FXML
    private TableView<Reserva> tblReservas;

    @FXML
    private TableColumn<Reserva, String> clmResCodigo;

    @FXML
    private TableColumn<Reserva, String> clmResApartamento;

    @FXML
    private TableColumn<Reserva, String> clmResEntrada;

    @FXML
    private TableColumn<Reserva, String> clmResSalida;

    @FXML
    private TableColumn<Reserva, String> clmResEstado;

    @FXML
    private TableColumn<Reserva, String> clmResValor;

    @FXML
    private TableColumn<Reserva, String> clmResSaldo;

    @FXML
    private TextField txtValorPago;

    @FXML
    private ComboBox<MedioPago> cbxMedioPago;

    @FXML
    private TextArea txtDetalleReserva;

    @FXML
    private Label lblDocumento;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCorreo;

    @FXML
    void initialize() {
        listOcupantes = new LinkedList<>();
        cbxMedioPago.getItems().addAll(MedioPago.values());

        clmAptoCodigo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCodigo()));
        clmAptoNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombre()));
        clmAptoCapacidad.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getCapacidadMaxima())));
        clmAptoCaracteristicas.setCellValueFactory(dato -> new SimpleStringProperty(
                dato.getValue().getCaracteristicas()));
        clmAptoTarifas.setCellValueFactory(dato -> new SimpleStringProperty(
                "$" + dato.getValue().getTarifaBaja() + " / $" + dato.getValue().getTarifaMedia()
                        + " / $" + dato.getValue().getTarifaAlta()));

        clmOcupanteNombre.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().nombre()));
        clmOcupanteNacimiento.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().fechaNacimiento())));

        clmResCodigo.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCodigo()));
        clmResApartamento.setCellValueFactory(dato -> new SimpleStringProperty(
                dato.getValue().getTheApartamento().getNombre()));
        clmResEntrada.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getFechaEntrada())));
        clmResSalida.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getFechaSalida())));
        clmResEstado.setCellValueFactory(dato -> new SimpleStringProperty(
                String.valueOf(dato.getValue().getEstado())));
        clmResValor.setCellValueFactory(dato -> new SimpleStringProperty(
                "$" + dato.getValue().getValorEstancia()));
        clmResSaldo.setCellValueFactory(dato -> new SimpleStringProperty(
                "$" + dato.getValue().getTheFolio().calcularSaldo()));

        tblReservas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetalleReserva(newValue));
    }

    public void setApp(App app) {
        this.app = app;
        huespedController = new HuespedController(app.getAlojamiento());
        huesped = (Huesped) app.getUsuarioActual();

        lblUsuario.setText("Huesped: " + huesped.getNombre());
        lblDocumento.setText(huesped.getDocumento());
        txtNombre.setText(huesped.getNombre());
        txtTelefono.setText(huesped.getTelefono());
        txtCorreo.setText(huesped.getCorreo());
        cargarReservas();
    }

    private void cargarReservas() {
        tblReservas.getItems().clear();
        tblReservas.getItems().addAll(huespedController.obtenerReservasHuesped(huesped.getDocumento()));
        tblReservas.refresh();
    }

    private void cargarOcupantes() {
        tblOcupantes.getItems().clear();
        tblOcupantes.getItems().addAll(listOcupantes);
    }

    private boolean fechasCompletas() {
        boolean completas = dpEntrada.getValue() != null && dpSalida.getValue() != null;

        if (!completas) {
            mostrarMensaje("Seleccione la fecha de entrada y la fecha de salida", Alert.AlertType.WARNING);
        }

        return completas;
    }

    @FXML
    void onBuscarDisponibles(ActionEvent event) {
        int personas = leerEntero(txtPersonas);

        if (fechasCompletas()) {
            if (personas <= 0) {
                mostrarMensaje("Ingrese el numero de personas", Alert.AlertType.WARNING);
            } else {
                LinkedList<Apartamento> disponibles = huespedController.consultarDisponibles(
                        dpEntrada.getValue(), dpSalida.getValue(), personas);
                tblApartamentos.getItems().clear();
                tblApartamentos.getItems().addAll(disponibles);

                if (disponibles.isEmpty()) {
                    mostrarMensaje("No hay apartamentos disponibles para esas fechas y ese numero de personas",
                            Alert.AlertType.INFORMATION);
                }
            }
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
    void onCotizar(ActionEvent event) {
        Apartamento apartamento = tblApartamentos.getSelectionModel().getSelectedItem();

        if (fechasCompletas()) {
            if (apartamento == null) {
                mostrarMensaje("Seleccione un apartamento de la tabla", Alert.AlertType.WARNING);
            } else {
                LinkedList<DetalleNoche> detalle = huespedController.cotizar(apartamento, dpEntrada.getValue(),
                        dpSalida.getValue(), huesped, listOcupantes);
                String texto = "Cotizacion de " + apartamento + "\n";
                int total = 0;

                for (DetalleNoche noche : detalle) {
                    texto += noche.noche() + " - " + noche.temporada() + " - $" + noche.tarifa() + " x "
                            + noche.personasQuePagan() + " = $" + noche.subtotal() + "\n";
                    total += noche.subtotal();
                }

                texto += "Personas: " + (listOcupantes.size() + 1) + "\n";
                texto += "Valor total: $" + total + "\n";
                texto += "Anticipo del 30 %: $" + (total * 30 / 100);
                txtCotizacion.setText(texto);
            }
        }
    }

    @FXML
    void onReservar(ActionEvent event) {
        Apartamento apartamento = tblApartamentos.getSelectionModel().getSelectedItem();

        if (fechasCompletas()) {
            if (apartamento == null) {
                mostrarMensaje("Seleccione un apartamento de la tabla", Alert.AlertType.WARNING);
            } else {
                Reserva reserva = huespedController.crearReservaPortal(huesped.getDocumento(),
                        apartamento.getCodigo(), listOcupantes, dpEntrada.getValue(), dpSalida.getValue());

                if (reserva != null) {
                    mostrarMensaje("Reserva " + reserva.getCodigo() + " creada en estado PENDIENTE."
                            + "\nValor: $" + reserva.getValorEstancia()
                            + "\nAnticipo requerido: $" + reserva.calcularAnticipoRequerido()
                            + "\nTiene 24 horas para pagar el anticipo.", Alert.AlertType.INFORMATION);
                    listOcupantes = new LinkedList<>();
                    cargarOcupantes();
                    tblApartamentos.getItems().clear();
                    txtCotizacion.clear();
                    cargarReservas();
                } else {
                    mostrarMensaje("No se pudo crear la reserva. Revise las fechas, la capacidad del apartamento"
                            + " y que siga disponible.", Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    void onPagar(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();
        int valor = leerEntero(txtValorPago);

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (valor <= 0 || cbxMedioPago.getValue() == null) {
            mostrarMensaje("Ingrese el valor y el medio de pago", Alert.AlertType.WARNING);
        } else if (huespedController.registrarPago(reserva.getCodigo(), cbxMedioPago.getValue(), valor)) {
            String mensaje = "Pago registrado";

            if (reserva.obtenerCanal().equals("PORTAL") && huespedController.confirmarReserva(reserva.getCodigo())) {
                mensaje += ". La reserva quedo CONFIRMADA";
            }

            mostrarMensaje(mensaje, Alert.AlertType.INFORMATION);
            txtValorPago.clear();
            cargarReservas();
            mostrarDetalleReserva(reserva);
        } else {
            mostrarMensaje("No se registro el pago: la reserva ya no esta activa", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onVerDevolucion(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else {
            mostrarMensaje("Ha pagado $" + reserva.getTheFolio().calcularTotalPagos()
                    + ".\nSi cancela hoy se le devolverian $" + reserva.calcularDevolucion(LocalDate.now()),
                    Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void onCancelarReserva(ActionEvent event) {
        Reserva reserva = tblReservas.getSelectionModel().getSelectedItem();

        if (reserva == null) {
            mostrarMensaje("Seleccione una reserva de la tabla", Alert.AlertType.WARNING);
        } else if (huespedController.cancelarReserva(reserva.getCodigo())) {
            mostrarMensaje("Reserva cancelada. Valor a devolver: $" + reserva.getValorDevolucion(),
                    Alert.AlertType.INFORMATION);
            cargarReservas();
            mostrarDetalleReserva(reserva);
        } else {
            mostrarMensaje("Solo se puede cancelar una reserva PENDIENTE o CONFIRMADA", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onGuardarPerfil(ActionEvent event) {
        if (txtNombre.getText().isBlank()) {
            mostrarMensaje("El nombre no puede estar vacio", Alert.AlertType.WARNING);
        } else if (huespedController.actualizarHuesped(huesped.getDocumento(), txtNombre.getText(),
                txtTelefono.getText(), txtCorreo.getText())) {
            lblUsuario.setText("Huesped: " + huesped.getNombre());
            mostrarMensaje("Perfil actualizado", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void onCerrarSesion(ActionEvent event) {
        app.openViewLogin();
    }

    private void mostrarDetalleReserva(Reserva reserva) {
        if (reserva != null) {
            String texto = reserva + "\nPersonas: " + reserva.contarPersonas()
                    + " - Noches: " + reserva.calcularNumeroNoches() + "\n\nMovimientos del folio:\n";

            for (IMovimiento movimiento : reserva.getTheFolio().obtenerMovimientos()) {
                texto += movimiento.getFecha() + " - " + movimiento.obtenerDescripcion()
                        + " - $" + movimiento.getValor() + "\n";
            }

            texto += "\nTotal cargos: $" + reserva.getTheFolio().calcularTotalCargos();
            texto += "\nTotal pagos: $" + reserva.getTheFolio().calcularTotalPagos();
            texto += "\nSaldo: $" + reserva.getTheFolio().calcularSaldo();

            if (reserva.getEstado() == EstadoReserva.CANCELADA || reserva.getEstado() == EstadoReserva.NO_SHOW) {
                texto += "\nValor a devolver: $" + reserva.getValorDevolucion();
            }

            txtDetalleReserva.setText(texto);
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
