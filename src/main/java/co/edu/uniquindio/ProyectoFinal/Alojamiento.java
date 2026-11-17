package co.edu.uniquindio.ProyectoFinal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Alojamiento {
    private String nombre;
    private int consecutivoReservas;

    private Administrador theAdministrador;
    private PoliticaCancelacion thePolitica;
    private LinkedList<Apartamento> listApartamentos;
    private LinkedList<Huesped> listHuespedes;
    private LinkedList<Recepcionista> listRecepcionistas;
    private LinkedList<Temporada> listTemporadas;
    private LinkedList<Reserva> listReservas;
    private LinkedList<ConflictoCanal> listConflictos;

    public Alojamiento(String nombre, Administrador theAdministrador) {
        this.nombre = nombre;
        this.theAdministrador = theAdministrador;
        consecutivoReservas = 0;
        thePolitica = new PoliticaCancelacion(7, 2, 50);
        listApartamentos = new LinkedList<>();
        listHuespedes = new LinkedList<>();
        listRecepcionistas = new LinkedList<>();
        listTemporadas = new LinkedList<>();
        listReservas = new LinkedList<>();
        listConflictos = new LinkedList<>();
    }

    public Persona autenticar(String usuario, String contrasena) {
        Persona encontrada = null;

        if (theAdministrador.validarCredenciales(usuario, contrasena)) {
            encontrada = theAdministrador;
        }

        for (Recepcionista recepcionista : listRecepcionistas) {
            if (recepcionista.isActivo() && recepcionista.validarCredenciales(usuario, contrasena)) {
                encontrada = recepcionista;
            }
        }

        for (Huesped huesped : listHuespedes) {
            if (huesped.validarCredenciales(usuario, contrasena)) {
                encontrada = huesped;
            }
        }

        return encontrada;
    }

    public boolean existeUsuario(String usuario) {
        boolean existe = theAdministrador.getUsuario().equals(usuario);

        for (Recepcionista recepcionista : listRecepcionistas) {
            if (recepcionista.getUsuario().equals(usuario)) {
                existe = true;
            }
        }

        for (Huesped huesped : listHuespedes) {
            if (huesped.getUsuario().equals(usuario)) {
                existe = true;
            }
        }

        return existe;
    }

    public boolean registrarHuesped(Huesped huesped) {
        boolean registrado = false;

        if (huesped != null && buscarHuesped(huesped.getDocumento()) == null
                && !existeUsuario(huesped.getUsuario())) {
            listHuespedes.add(huesped);
            registrado = true;
        }

        return registrado;
    }

    public boolean actualizarHuesped(String documento, String nombre, String telefono, String correo) {
        boolean actualizado = false;
        Huesped huesped = buscarHuesped(documento);

        if (huesped != null) {
            huesped.setNombre(nombre);
            huesped.setTelefono(telefono);
            huesped.setCorreo(correo);
            actualizado = true;
        }

        return actualizado;
    }

    public Huesped buscarHuesped(String documento) {
        Huesped encontrado = null;

        for (Huesped huesped : listHuespedes) {
            if (huesped.getDocumento().equals(documento)) {
                encontrado = huesped;
            }
        }

        return encontrado;
    }

    public boolean contratarRecepcionista(Recepcionista recepcionista) {
        boolean contratado = false;

        if (recepcionista != null && buscarRecepcionista(recepcionista.getDocumento()) == null
                && !existeUsuario(recepcionista.getUsuario())) {
            listRecepcionistas.add(recepcionista);
            contratado = true;
        }

        return contratado;
    }

    public boolean actualizarRecepcionista(String documento, String nombre, String telefono, String correo) {
        boolean actualizado = false;
        Recepcionista recepcionista = buscarRecepcionista(documento);

        if (recepcionista != null) {
            recepcionista.setNombre(nombre);
            recepcionista.setTelefono(telefono);
            recepcionista.setCorreo(correo);
            actualizado = true;
        }

        return actualizado;
    }

    public boolean desvincularRecepcionista(String documento) {
        boolean desvinculado = false;
        Recepcionista recepcionista = buscarRecepcionista(documento);

        if (recepcionista != null && recepcionista.isActivo()) {
            recepcionista.setActivo(false);
            desvinculado = true;
        }

        return desvinculado;
    }

    public Recepcionista buscarRecepcionista(String documento) {
        Recepcionista encontrado = null;

        for (Recepcionista recepcionista : listRecepcionistas) {
            if (recepcionista.getDocumento().equals(documento)) {
                encontrado = recepcionista;
            }
        }

        return encontrado;
    }

    public boolean registrarApartamento(Apartamento apartamento) {
        boolean registrado = false;

        if (apartamento != null && buscarApartamento(apartamento.getCodigo()) == null
                && apartamento.getCapacidadMaxima() > 0 && apartamento.getTarifaBaja() > 0
                && apartamento.getTarifaMedia() > 0 && apartamento.getTarifaAlta() > 0) {
            listApartamentos.add(apartamento);
            registrado = true;
        }

        return registrado;
    }

    public boolean actualizarApartamento(String codigo, String nombre, int numeroDormitorios,
                                         int capacidadMaxima, String caracteristicas) {
        boolean actualizado = false;
        Apartamento apartamento = buscarApartamento(codigo);

        if (apartamento != null && capacidadMaxima > 0) {
            apartamento.setNombre(nombre);
            apartamento.setNumeroDormitorios(numeroDormitorios);
            apartamento.setCapacidadMaxima(capacidadMaxima);
            apartamento.setCaracteristicas(caracteristicas);
            actualizado = true;
        }

        return actualizado;
    }

    public boolean eliminarApartamento(String codigo) {
        boolean eliminado = false;
        Apartamento apartamento = buscarApartamento(codigo);

        if (apartamento != null && apartamento.isActivo() && !tieneReservasActivas(apartamento)) {
            apartamento.setActivo(false);
            eliminado = true;
        }

        return eliminado;
    }

    public boolean actualizarTarifas(String codigo, int tarifaBaja, int tarifaMedia, int tarifaAlta) {
        boolean actualizado = false;
        Apartamento apartamento = buscarApartamento(codigo);

        if (apartamento != null) {
            actualizado = apartamento.actualizarTarifas(tarifaBaja, tarifaMedia, tarifaAlta);
        }

        return actualizado;
    }

    public boolean cambiarEstadoApartamento(String codigo, EstadoApartamento nuevoEstado, String motivo) {
        boolean cambiado = false;
        Apartamento apartamento = buscarApartamento(codigo);

        if (apartamento != null) {
            cambiado = apartamento.cambiarEstado(nuevoEstado, motivo);
        }

        return cambiado;
    }

    public boolean registrarBloqueo(String codigo, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        boolean registrado = false;
        Apartamento apartamento = buscarApartamento(codigo);

        if (apartamento != null && !fechaFin.isBefore(fechaInicio)
                && !tieneReservaActivaEnRango(apartamento, fechaInicio, fechaFin.plusDays(1))) {
            registrado = apartamento.registrarBloqueo(new Bloqueo(fechaInicio, fechaFin, motivo));
        }

        return registrado;
    }

    public boolean registrarNovedad(String codigo, String descripcion, LocalDate fecha) {
        boolean registrada = false;
        Apartamento apartamento = buscarApartamento(codigo);

        if (apartamento != null) {
            registrada = apartamento.registrarNovedad(new Novedad(fecha, descripcion));
        }

        return registrada;
    }

    public Apartamento buscarApartamento(String codigo) {
        Apartamento encontrado = null;

        for (Apartamento apartamento : listApartamentos) {
            if (apartamento.getCodigo().equals(codigo)) {
                encontrado = apartamento;
            }
        }

        return encontrado;
    }

    public boolean registrarTemporada(Temporada temporada) {
        boolean registrada = false;

        if (temporada != null && temporada.getTipo() != TipoTemporada.BAJA
                && !temporada.getFechaFin().isBefore(temporada.getFechaInicio())) {
            listTemporadas.add(temporada);
            registrada = true;
        }

        return registrada;
    }

    public TipoTemporada obtenerTemporada(LocalDate fecha) {
        TipoTemporada tipo = TipoTemporada.BAJA;

        for (Temporada temporada : listTemporadas) {
            if (temporada.incluyeFecha(fecha)) {
                if (temporada.getTipo() == TipoTemporada.ALTA) {
                    tipo = TipoTemporada.ALTA;
                } else if (tipo == TipoTemporada.BAJA) {
                    tipo = TipoTemporada.MEDIA;
                }
            }
        }

        return tipo;
    }

    public boolean configurarPoliticaCancelacion(int diasDevolucionTotal, int diasRetencionParcial,
                                                 int porcentajeRetencionParcial) {
        boolean configurada = false;

        if (diasRetencionParcial >= 0 && diasDevolucionTotal >= diasRetencionParcial
                && porcentajeRetencionParcial >= 0 && porcentajeRetencionParcial <= 100) {
            thePolitica = new PoliticaCancelacion(diasDevolucionTotal, diasRetencionParcial,
                    porcentajeRetencionParcial);
            configurada = true;
        }

        return configurada;
    }

    private boolean tieneReservaActivaEnRango(Apartamento apartamento, LocalDate entrada, LocalDate salida) {
        boolean tiene = false;

        for (Reserva reserva : listReservas) {
            if (reserva.getTheApartamento() == apartamento && reserva.esActiva()
                    && reserva.seCruzaCon(entrada, salida)) {
                tiene = true;
            }
        }

        return tiene;
    }

    private boolean tieneReservasActivas(Apartamento apartamento) {
        boolean tiene = false;

        for (Reserva reserva : listReservas) {
            if (reserva.getTheApartamento() == apartamento && reserva.esActiva()) {
                tiene = true;
            }
        }

        return tiene;
    }

    public boolean estaDisponible(Apartamento apartamento, LocalDate entrada, LocalDate salida) {
        boolean disponible = apartamento.isActivo()
                && apartamento.getEstado() != EstadoApartamento.FUERA_DE_SERVICIO
                && !tieneReservaActivaEnRango(apartamento, entrada, salida);

        for (LocalDate noche = entrada; noche.isBefore(salida); noche = noche.plusDays(1)) {
            if (apartamento.estaBloqueado(noche)) {
                disponible = false;
            }
        }

        return disponible;
    }

    public LinkedList<Apartamento> consultarDisponibles(LocalDate entrada, LocalDate salida, int numeroPersonas) {
        LinkedList<Apartamento> disponibles = new LinkedList<>();

        if (salida.isAfter(entrada)) {
            for (Apartamento apartamento : listApartamentos) {
                if (apartamento.getCapacidadMaxima() >= numeroPersonas
                        && estaDisponible(apartamento, entrada, salida)) {
                    disponibles.add(apartamento);
                }
            }
        }

        return disponibles;
    }

    public int contarPersonasQuePagan(Huesped titular, LinkedList<Ocupante> ocupantes, LocalDate fechaEntrada) {
        int personas = 0;

        if (titular.calcularEdad(fechaEntrada) >= 6) {
            personas++;
        }

        for (Ocupante ocupante : ocupantes) {
            if (ocupante.calcularEdad(fechaEntrada) >= 6) {
                personas++;
            }
        }

        return personas;
    }

    public LinkedList<DetalleNoche> cotizar(Apartamento apartamento, LocalDate entrada, LocalDate salida,
                                            int personasQuePagan) {
        LinkedList<DetalleNoche> detalle = new LinkedList<>();

        for (LocalDate noche = entrada; noche.isBefore(salida); noche = noche.plusDays(1)) {
            TipoTemporada temporada = obtenerTemporada(noche);
            int tarifa = apartamento.obtenerTarifa(temporada);
            detalle.add(new DetalleNoche(noche, temporada, tarifa, personasQuePagan, tarifa * personasQuePagan));
        }

        return detalle;
    }

    public int calcularValorEstancia(Apartamento apartamento, LocalDate entrada, LocalDate salida,
                                     int personasQuePagan) {
        int valor = 0;

        for (DetalleNoche detalleNoche : cotizar(apartamento, entrada, salida, personasQuePagan)) {
            valor += detalleNoche.subtotal();
        }

        return valor;
    }

    private boolean validarDatosReserva(Apartamento apartamento, Huesped titular, LinkedList<Ocupante> ocupantes,
                                        LocalDate entrada, LocalDate salida, LocalDate hoy) {
        return apartamento != null && titular != null && ocupantes != null
                && salida.isAfter(entrada) && !entrada.isBefore(hoy)
                && ocupantes.size() + 1 <= apartamento.getCapacidadMaxima()
                && estaDisponible(apartamento, entrada, salida);
    }

    private String generarCodigoReserva() {
        consecutivoReservas++;
        return "R" + consecutivoReservas;
    }

    public Reserva crearReservaPortal(String documentoTitular, String codigoApartamento,
                                      LinkedList<Ocupante> ocupantes, LocalDate entrada, LocalDate salida,
                                      LocalDateTime ahora) {
        Reserva nuevaReserva = null;
        Apartamento apartamento = buscarApartamento(codigoApartamento);
        Huesped titular = buscarHuesped(documentoTitular);

        if (validarDatosReserva(apartamento, titular, ocupantes, entrada, salida, ahora.toLocalDate())) {
            int valor = calcularValorEstancia(apartamento, entrada, salida,
                    contarPersonasQuePagan(titular, ocupantes, entrada));
            nuevaReserva = new ReservaPortal(generarCodigoReserva(), apartamento, titular, ocupantes,
                    entrada, salida, valor, thePolitica, ahora);
            listReservas.add(nuevaReserva);
        }

        return nuevaReserva;
    }

    public Reserva crearReservaDirecta(String documentoTitular, String codigoApartamento,
                                       LinkedList<Ocupante> ocupantes, LocalDate entrada, LocalDate salida,
                                       LocalDateTime ahora) {
        Reserva nuevaReserva = null;
        Apartamento apartamento = buscarApartamento(codigoApartamento);
        Huesped titular = buscarHuesped(documentoTitular);

        if (validarDatosReserva(apartamento, titular, ocupantes, entrada, salida, ahora.toLocalDate())) {
            int valor = calcularValorEstancia(apartamento, entrada, salida,
                    contarPersonasQuePagan(titular, ocupantes, entrada));
            nuevaReserva = new ReservaDirecta(generarCodigoReserva(), apartamento, titular, ocupantes,
                    entrada, salida, valor, thePolitica, ahora);
            listReservas.add(nuevaReserva);
        }

        return nuevaReserva;
    }

    public Reserva recibirReservaExterna(String codigoExterno, String documentoTitular, String codigoApartamento,
                                         LinkedList<Ocupante> ocupantes, LocalDate entrada, LocalDate salida,
                                         LocalDateTime ahora) {
        Reserva nuevaReserva = null;
        Apartamento apartamento = buscarApartamento(codigoApartamento);
        Huesped titular = buscarHuesped(documentoTitular);

        if (buscarReservaExterna(codigoExterno) != null) {
            listConflictos.add(new ConflictoCanal(codigoExterno, codigoApartamento, entrada, salida,
                    "El codigo externo ya tiene una reserva"));
        } else if (validarDatosReserva(apartamento, titular, ocupantes, entrada, salida, ahora.toLocalDate())) {
            int valor = calcularValorEstancia(apartamento, entrada, salida,
                    contarPersonasQuePagan(titular, ocupantes, entrada));
            nuevaReserva = new ReservaExterna(generarCodigoReserva(), apartamento, titular, ocupantes,
                    entrada, salida, valor, thePolitica, ahora, codigoExterno);
            listReservas.add(nuevaReserva);
        } else {
            listConflictos.add(new ConflictoCanal(codigoExterno, codigoApartamento, entrada, salida,
                    "El apartamento no esta disponible o los datos no son validos"));
        }

        return nuevaReserva;
    }

    public Reserva buscarReserva(String codigo) {
        Reserva encontrada = null;

        for (Reserva reserva : listReservas) {
            if (reserva.getCodigo().equals(codigo)) {
                encontrada = reserva;
            }
        }

        return encontrada;
    }

    public ReservaExterna buscarReservaExterna(String codigoExterno) {
        ReservaExterna encontrada = null;

        for (Reserva reserva : listReservas) {
            if (reserva instanceof ReservaExterna) {
                ReservaExterna externa = (ReservaExterna) reserva;
                if (externa.getCodigoExterno().equals(codigoExterno)) {
                    encontrada = externa;
                }
            }
        }

        return encontrada;
    }

    public LinkedList<Reserva> obtenerReservasHuesped(String documento) {
        LinkedList<Reserva> reservas = new LinkedList<>();

        for (Reserva reserva : listReservas) {
            if (reserva.getTheTitular().getDocumento().equals(documento)) {
                reservas.add(reserva);
            }
        }

        return reservas;
    }

    public boolean confirmarReserva(String codigo) {
        boolean confirmada = false;
        Reserva reserva = buscarReserva(codigo);

        if (reserva != null) {
            confirmada = reserva.confirmar();
        }

        return confirmada;
    }

    public boolean cancelarReserva(String codigo, LocalDate fechaCancelacion) {
        boolean cancelada = false;
        Reserva reserva = buscarReserva(codigo);

        if (reserva != null) {
            cancelada = reserva.cancelar(fechaCancelacion);
        }

        return cancelada;
    }

    public boolean registrarCheckIn(String codigo, LocalDate hoy) {
        boolean realizado = false;
        Reserva reserva = buscarReserva(codigo);

        if (reserva != null) {
            realizado = reserva.hacerCheckIn(hoy);
        }

        return realizado;
    }

    public boolean registrarCheckOut(String codigo) {
        boolean realizado = false;
        Reserva reserva = buscarReserva(codigo);

        if (reserva != null) {
            realizado = reserva.hacerCheckOut();
        }

        return realizado;
    }

    public boolean declararNoShow(String codigo, LocalDateTime ahora) {
        boolean declarado = false;
        Reserva reserva = buscarReserva(codigo);

        if (reserva != null) {
            declarado = reserva.declararNoShow(ahora);
        }

        return declarado;
    }

    public int cancelarReservasVencidas(LocalDateTime ahora) {
        int canceladas = 0;

        for (Reserva reserva : listReservas) {
            if (reserva.cancelarPorVencimiento(ahora)) {
                canceladas++;
            }
        }

        return canceladas;
    }

    public boolean registrarPago(String codigoReserva, MedioPago medioPago, int valor, LocalDate fecha) {
        boolean registrado = false;
        Reserva reserva = buscarReserva(codigoReserva);

        if (reserva != null && reserva.esActiva() && valor > 0) {
            registrado = reserva.getTheFolio().registrarPago(new Pago(medioPago, valor, fecha));
        }

        return registrado;
    }

    public boolean registrarCargoServicio(String codigoReserva, String descripcion, int valor, LocalDate fecha) {
        boolean registrado = false;
        Reserva reserva = buscarReserva(codigoReserva);

        if (reserva != null && reserva.esActiva() && valor > 0) {
            registrado = reserva.getTheFolio().agregarCargo(
                    new Cargo(TipoCargo.SERVICIO_ADICIONAL, descripcion, valor, fecha));
        }

        return registrado;
    }

    public boolean corregirPago(String codigoReserva, MedioPago medioPago, int valor, LocalDate fecha) {
        boolean corregido = false;
        Reserva reserva = buscarReserva(codigoReserva);

        if (reserva != null && valor > 0) {
            corregido = reserva.getTheFolio().registrarPago(new Pago(medioPago, -valor, fecha));
        }

        return corregido;
    }

    public boolean corregirCargo(String codigoReserva, String descripcion, int valor, LocalDate fecha) {
        boolean corregido = false;
        Reserva reserva = buscarReserva(codigoReserva);

        if (reserva != null && valor > 0) {
            corregido = reserva.getTheFolio().agregarCargo(
                    new Cargo(TipoCargo.SERVICIO_ADICIONAL, "Correccion: " + descripcion, -valor, fecha));
        }

        return corregido;
    }

    public int calcularNochesOcupadas(String codigoApartamento, LocalDate desde, LocalDate hasta) {
        int noches = 0;

        for (Reserva reserva : listReservas) {
            if (reserva.getTheApartamento().getCodigo().equals(codigoApartamento)
                    && reserva.getEstado() != EstadoReserva.CANCELADA
                    && reserva.getEstado() != EstadoReserva.NO_SHOW) {
                for (LocalDate noche = desde; !noche.isAfter(hasta); noche = noche.plusDays(1)) {
                    if (reserva.ocupaNoche(noche)) {
                        noches++;
                    }
                }
            }
        }

        return noches;
    }

    public int calcularIngresosPeriodo(LocalDate desde, LocalDate hasta) {
        int ingresos = 0;

        for (Reserva reserva : listReservas) {
            for (Pago pago : reserva.getTheFolio().getListPagos()) {
                if (!pago.getFecha().isBefore(desde) && !pago.getFecha().isAfter(hasta)) {
                    ingresos += pago.getValor();
                }
            }
        }

        return ingresos;
    }

    public int contarReservasPorCanal(String canal) {
        int cantidad = 0;

        for (Reserva reserva : listReservas) {
            if (reserva.obtenerCanal().equals(canal)) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public LinkedList<Reserva> obtenerReservasConSaldoPendiente() {
        LinkedList<Reserva> reservas = new LinkedList<>();

        for (Reserva reserva : listReservas) {
            if (reserva.getEstado() != EstadoReserva.CANCELADA && reserva.getEstado() != EstadoReserva.NO_SHOW
                    && reserva.getTheFolio().calcularSaldo() > 0) {
                reservas.add(reserva);
            }
        }

        return reservas;
    }

    public Apartamento obtenerApartamentoConMasNovedades() {
        Apartamento mayor = null;

        for (Apartamento apartamento : listApartamentos) {
            if (!apartamento.getListNovedades().isEmpty() && (mayor == null
                    || apartamento.getListNovedades().size() > mayor.getListNovedades().size())) {
                mayor = apartamento;
            }
        }

        return mayor;
    }

    public String getNombre() {
        return nombre;
    }

    public Administrador getTheAdministrador() {
        return theAdministrador;
    }

    public PoliticaCancelacion getThePolitica() {
        return thePolitica;
    }

    public LinkedList<Apartamento> getListApartamentos() {
        return listApartamentos;
    }

    public LinkedList<Huesped> getListHuespedes() {
        return listHuespedes;
    }

    public LinkedList<Recepcionista> getListRecepcionistas() {
        return listRecepcionistas;
    }

    public LinkedList<Temporada> getListTemporadas() {
        return listTemporadas;
    }

    public LinkedList<Reserva> getListReservas() {
        return listReservas;
    }

    public LinkedList<ConflictoCanal> getListConflictos() {
        return listConflictos;
    }
}
