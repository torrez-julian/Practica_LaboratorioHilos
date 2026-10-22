package ni.edu.uam.laboratoriodehilos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private HelloController controlador;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 850, 620);

        controlador = loader.getController();

        stage.setTitle("Laboratorio de Hilos - Ventanillas");
        stage.setMinWidth(760);
        stage.setMinHeight(560);
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            event.consume();
            controlador.cerrarAplicacion();
        });

        stage.show();
    }

    @Override
    public void stop() {
        if (controlador != null) {
            controlador.solicitarDetencion();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}