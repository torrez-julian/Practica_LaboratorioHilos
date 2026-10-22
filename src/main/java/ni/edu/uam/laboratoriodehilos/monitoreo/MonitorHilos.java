
package ni.edu.uam.laboratoriodehilos.monitoreo;

import java.util.function.Consumer;

public class MonitorHilos implements Runnable {

    private final Thread hiloA;
    private final Thread hiloB;
    private final Consumer<String> salida;
    private volatile boolean activo = true;

    public MonitorHilos(Thread hiloA, Thread hiloB,
                        Consumer<String> salida) {
        this.hiloA = hiloA;
        this.hiloB = hiloB;
        this.salida = salida;
    }

    @Override
    public void run() {
        Thread.State anteriorA = null;
        Thread.State anteriorB = null;

        while (activo && !Thread.currentThread().isInterrupted()) {

            Thread.State estadoA = hiloA.getState();
            Thread.State estadoB = hiloB.getState();

            // Registrar cuando alguno cambie de estado
            if (estadoA != anteriorA || estadoB != anteriorB) {

                salida.accept(
                        hiloA.getName() + " = " + estadoA
                                + " | "
                                + hiloB.getName() + " = " + estadoB
                );

                anteriorA = estadoA;
                anteriorB = estadoB;
            }

            // Finalizar cuando ambos hilos hayan terminado
            if (estadoA == Thread.State.TERMINATED &&
                    estadoB == Thread.State.TERMINATED) {
                break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        salida.accept("Monitor de hilos finalizado.");
    }

    public void detener() {
        activo = false;
    }
}
