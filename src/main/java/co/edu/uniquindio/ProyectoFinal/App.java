package co.edu.uniquindio.ProyectoFinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class App extends Application {
    private Stage primaryStage;
    private Alojamiento alojamiento;
    private Persona usuarioActual;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("SGA - Aparta-Suites UQ");
        primaryStage.setMinHeight(680);
        primaryStage.setMinWidth(980);
        initData();
        openViewLogin();
    }

    private void initData() {
        Administrador administrador = new Administrador("Administrador UQ", "1000", "3100000000",
                "admin@apartasuites.co", "admin", "admin123");
        alojamiento = new Alojamiento("Aparta-Suites UQ", administrador);

        alojamiento.contratarRecepcionista(new Recepcionista("Carlos Ruiz", "2000", "3110000000",
                "recepcion@apartasuites.co", "recepcion", "recep123"));
        alojamiento.registrarHuesped(new Huesped("Laura Gomez", "3000", "3120000000",
                "laura@correo.com", "laura", "laura123", LocalDate.of(1990, 5, 10)));

        alojamiento.registrarApartamento(new Apartamento("A101", "Suite Cocora", 2, 4,
                "Balcon, vista a las montanas", 60000, 80000, 110000));
        alojamiento.registrarApartamento(new Apartamento("A102", "Suite Salento", 1, 2,
                "Chimenea", 50000, 70000, 95000));
        alojamiento.registrarApartamento(new Apartamento("A201", "Suite Quindio", 3, 6,
                "Jacuzzi, balcon", 70000, 90000, 130000));

        alojamiento.registrarTemporada(new Temporada(TipoTemporada.ALTA, "Diciembre y enero",
                LocalDate.of(2026, 12, 15), LocalDate.of(2027, 1, 15)));
        alojamiento.registrarTemporada(new Temporada(TipoTemporada.ALTA, "Semana Santa",
                LocalDate.of(2027, 3, 21), LocalDate.of(2027, 3, 28)));
        alojamiento.registrarTemporada(new Temporada(TipoTemporada.ALTA, "Vacaciones de mitad de anio",
                LocalDate.of(2027, 6, 15), LocalDate.of(2027, 7, 15)));
        alojamiento.registrarTemporada(new Temporada(TipoTemporada.MEDIA, "Puente de diciembre",
                LocalDate.of(2026, 12, 5), LocalDate.of(2026, 12, 8)));
    }

    public void openViewLogin() {
        try {
            usuarioActual = null;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("login.fxml"));
            VBox rootLayout = (VBox) loader.load();
            LoginViewController loginViewController = loader.getController();
            loginViewController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openViewRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("registro.fxml"));
            VBox rootLayout = (VBox) loader.load();
            RegistroViewController registroViewController = loader.getController();
            registroViewController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openViewHuesped() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("huesped.fxml"));
            VBox rootLayout = (VBox) loader.load();
            HuespedViewController huespedViewController = loader.getController();
            huespedViewController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openViewRecepcion() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("recepcion.fxml"));
            VBox rootLayout = (VBox) loader.load();
            RecepcionViewController recepcionViewController = loader.getController();
            recepcionViewController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openViewAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("admin.fxml"));
            VBox rootLayout = (VBox) loader.load();
            AdminViewController adminViewController = loader.getController();
            adminViewController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public Persona getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Persona usuarioActual) {
        this.usuarioActual = usuarioActual;
    }
}
