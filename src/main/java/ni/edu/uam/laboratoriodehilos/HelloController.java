package ni.edu.uam.laboratoriodehilos;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import ni.edu.uam.laboratoriodehilos.modelos.ContadorCompartido;
import ni.edu.uam.laboratoriodehilos.tareas.ProcesoVentanilla;

public class HelloController {

    private static final int UNIDADES_POR_VENTANILLA = 5;
    private static final int TOTAL_ESPERADO =
            UNIDADES_POR_VENTANILLA * 2;

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnDetener;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblContador;

    @FXML
    private Label lblEstadoA;

    @FXML
    private Label lblEstadoB;

    @FXML
    private TextArea txtRegistro;

    private ContadorCompartido contador;

    private ProcesoVentanilla procesoA;
    private ProcesoVentanilla procesoB;

    private Thread hiloA;
    private Thread hiloB;

    private Timeline monitor;

    // Estos campos pertenecen al hilo gráfico de JavaFX.
    private boolean sesionActiva;
    private boolean detencionSolicitada;
    private boolean cerrando;
    private int numeroSesion;

    @FXML
    private void initialize() {
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);

        lblEstado.setText("Listo para iniciar");
        lblContador.setText("0");
        lblEstadoA.setText("Sin iniciar");
        lblEstadoB.setText("Sin iniciar");

        monitor = new Timeline(
                new KeyFrame(
                        Duration.millis(100),
                        event -> revisarSesion()
                )
        );

        monitor.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void iniciar() {
        if (sesionActiva || cerrando || hayHilosActivos()) {
            return;
        }

        numeroSesion++;
        sesionActiva = true;
        detencionSolicitada = false;

        // Cada sesión tiene su propio contador y sus propios hilos.
        contador = new ContadorCompartido();

        int sesionActual = numeroSesion;

        procesoA = new ProcesoVentanilla(
                "Ventanilla A",
                UNIDADES_POR_VENTANILLA,
                contador,
                mensaje -> registrar(sesionActual, mensaje)
        );

        procesoB = new ProcesoVentanilla(
                "Ventanilla B",
                UNIDADES_POR_VENTANILLA,
                contador,
                mensaje -> registrar(sesionActual, mensaje)
        );

        hiloA = new Thread(procesoA, "Ventanilla-A");
        hiloB = new Thread(procesoB, "Ventanilla-B");

        btnIniciar.setDisable(true);
        btnDetener.setDisable(false);

        lblEstado.setText(
                "Sesión " + numeroSesion + " en ejecución"
        );

        escribir("\n--- SESIÓN " + numeroSesion + " ---");
        escribir("Contador inicial: 0");
        escribir("Estados antes de iniciar: "
                + hiloA.getState() + " / " + hiloB.getState());

        actualizarIndicadores();

        hiloA.start();
        hiloB.start();

        monitor.playFromStart();
    }

    @FXML
    private void detener() {
        solicitarDetencion();
    }

    public void solicitarDetencion() {
        if (!sesionActiva || detencionSolicitada) {
            return;
        }

        detencionSolicitada = true;

        btnDetener.setDisable(true);
        lblEstado.setText("Deteniendo ventanillas...");

        escribir("Se solicitó detener la sesión.");

        procesoA.cancelar();
        procesoB.cancelar();

        hiloA.interrupt();
        hiloB.interrupt();

        // Iniciar permanece deshabilitado hasta confirmar el fin.
    }

    private boolean hayHilosActivos() {
        return (hiloA != null && hiloA.isAlive())
                || (hiloB != null && hiloB.isAlive());
    }

    private void revisarSesion() {
        if (!sesionActiva) {
            return;
        }

        actualizarIndicadores();

        // Ambos hilos ya fueron iniciados antes de activar el monitor.
        if (!hayHilosActivos()) {
            finalizarSesion();
        }
    }

    private void actualizarIndicadores() {
        if (contador != null) {
            lblContador.setText(
                    String.valueOf(contador.getTotal())
            );
        }

        if (hiloA != null) {
            lblEstadoA.setText(hiloA.getState().name());
        }

        if (hiloB != null) {
            lblEstadoB.setText(hiloB.getState().name());
        }
    }

    private void finalizarSesion() {
        monitor.stop();
        actualizarIndicadores();

        sesionActiva = false;

        int total = contador.getTotal();

        if (total == TOTAL_ESPERADO) {
            lblEstado.setText("Sesión completada");
        } else if (detencionSolicitada) {
            lblEstado.setText("Sesión detenida");
        } else {
            lblEstado.setText("Sesión finalizada de forma incompleta");
        }

        escribir("Estados finales: "
                + hiloA.getState() + " / " + hiloB.getState());
        escribir("Contador final: " + total);
        escribir("Ambas ventanillas terminaron.");

        btnDetener.setDisable(true);
        btnIniciar.setDisable(cerrando);

        if (cerrando) {
            Platform.exit();
        } else {
            escribir("Podés iniciar una nueva sesión.");
        }
    }

    private void registrar(int sesion, String mensaje) {
        Platform.runLater(() -> {
            // Evita mostrar mensajes atrasados de otra sesión.
            if (sesion == numeroSesion) {
                escribir(mensaje);
            }
        });
    }

    private void escribir(String mensaje) {
        // Se invoca únicamente desde el hilo gráfico.
        txtRegistro.appendText(
                mensaje + System.lineSeparator()
        );
    }

    public void cerrarAplicacion() {
        cerrando = true;
        btnIniciar.setDisable(true);

        if (sesionActiva) {
            solicitarDetencion();
        } else {
            Platform.exit();
        }
    }
}