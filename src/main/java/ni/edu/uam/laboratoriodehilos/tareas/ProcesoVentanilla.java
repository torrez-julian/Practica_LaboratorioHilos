package ni.edu.uam.laboratoriodehilos.tareas;

import ni.edu.uam.laboratoriodehilos.modelos.ContadorCompartido;

import java.util.Objects;
import java.util.function.Consumer;

public class ProcesoVentanilla implements Runnable {

    private final String nombre;
    private final int unidades;
    private final ContadorCompartido contador;
    private final Consumer<String> registrar;

    private volatile boolean cancelado;

    // Conserva la compatibilidad con las pruebas existentes.
    public ProcesoVentanilla(String nombre, int unidades) {
        this(
                nombre,
                unidades,
                new ContadorCompartido(),
                System.out::println
        );
    }

    // Constructor para integrar ambas ventanillas con JavaFX.
    public ProcesoVentanilla(
            String nombre,
            int unidades,
            ContadorCompartido contador,
            Consumer<String> registrar
    ) {
        if (unidades <= 0) {
            throw new IllegalArgumentException(
                    "Las unidades deben ser mayores que cero."
            );
        }

        this.nombre = Objects.requireNonNull(nombre);
        this.unidades = unidades;
        this.contador = Objects.requireNonNull(contador);
        this.registrar = Objects.requireNonNull(registrar);
    }

    public void cancelar() {
        cancelado = true;
    }

    private boolean debeDetenerse() {
        return cancelado || Thread.currentThread().isInterrupted();
    }

    @Override
    public void run() {
        int completadas = 0;

        registrar.accept(
                nombre + " inició en el hilo "
                        + Thread.currentThread().getName()
        );

        try {
            for (int unidad = 1; unidad <= unidades; unidad++) {
                if (debeDetenerse()) {
                    break;
                }

                registrar.accept(
                        nombre + " procesando unidad "
                                + unidad + "/" + unidades
                );

                Thread.sleep(500);

                if (debeDetenerse()) {
                    break;
                }

                contador.incrementar();
                completadas++;

                registrar.accept(
                        nombre + " completó la unidad "
                                + unidad + "/" + unidades
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            registrar.accept(nombre + ": espera interrumpida.");
        } finally {
            String resultado = completadas == unidades
                    ? "trabajo completado"
                    : "proceso detenido";

            registrar.accept(
                    nombre + ": " + resultado
                            + ". Unidades completadas: "
                            + completadas + "/" + unidades
            );
        }
    }
}