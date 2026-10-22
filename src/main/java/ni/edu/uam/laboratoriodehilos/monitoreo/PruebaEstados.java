
package ni.edu.uam.laboratoriodehilos.monitoreo;

public class PruebaEstados {

    public static void main(String[] args) {

        // Crear dos tareas de prueba
        Runnable tarea = () -> {
            for (int i = 1; i <= 5; i++) {

                System.out.println(
                        Thread.currentThread().getName()
                                + " procesando unidad " + i
                );

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        };

        // Crear los hilos
        Thread hiloA = new Thread(tarea, "Ventanilla-A");
        Thread hiloB = new Thread(tarea, "Ventanilla-B");

        // Verificar el estado antes de iniciar
        System.out.println("=== ESTADO INICIAL ===");
        System.out.println("A: " + hiloA.getState());
        System.out.println("B: " + hiloB.getState());

        // Crear el monitor y ejecutarlo en otro hilo
        MonitorHilos monitor = new MonitorHilos(
                hiloA,
                hiloB,
                System.out::println
        );

        Thread hiloMonitor = new Thread(
                monitor, "Monitor-Estados"
        );

        // Iniciar primero el monitor
        hiloMonitor.start();

        // Pequeña pausa para observar NEW
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // Iniciar ambas ventanillas
        hiloA.start();
        hiloB.start();

        try {
            // Esperar que finalicen las ventanillas
            hiloA.join();
            hiloB.join();

            // Esperar que finalice el monitor
            hiloMonitor.join();

        } catch (InterruptedException e) {
            monitor.detener();
            hiloMonitor.interrupt();
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("=== ESTADO FINAL ===");
        System.out.println("A: " + hiloA.getState());
        System.out.println("B: " + hiloB.getState());
    }
}
