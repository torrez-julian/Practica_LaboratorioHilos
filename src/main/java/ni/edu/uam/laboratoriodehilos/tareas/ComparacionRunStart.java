
package ni.edu.uam.laboratoriodehilos.tareas;

public class ComparacionRunStart {

    public static void main(String[] args) {

        System.out.println("PRUEBA 1: Ejecutando run()");

        Thread hiloA = new Thread(
                new ProcesoVentanilla("Ventanilla A", 5),
                "Ventanilla-A"
        );

        Thread hiloB = new Thread(
                new ProcesoVentanilla("Ventanilla B", 5),
                "Ventanilla-B"
        );

        hiloA.run();
        hiloB.run();

        System.out.println("\nPRUEBA 2: Ejecutando start()");

        Thread hiloC = new Thread(
                new ProcesoVentanilla("Ventanilla A", 5),
                "Ventanilla-A"
        );

        Thread hiloD = new Thread(
                new ProcesoVentanilla("Ventanilla B", 5),
                "Ventanilla-B"
        );

        hiloC.start();
        hiloD.start();

        try {
            hiloC.join();
            hiloD.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("La comparación fue interrumpida.");
            return;
        }

        System.out.println("Comparación finalizada.");
    }
}

