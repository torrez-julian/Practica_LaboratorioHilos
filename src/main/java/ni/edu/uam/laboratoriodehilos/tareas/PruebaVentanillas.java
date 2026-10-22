
package ni.edu.uam.laboratoriodehilos.tareas;

public class PruebaVentanillas {

    public static void main(String[] args) {

        ProcesoVentanilla tareaA = new ProcesoVentanilla("Ventanilla A", 5);
        ProcesoVentanilla tareaB = new ProcesoVentanilla("Ventanilla B", 5);

        Thread hiloA = new Thread(tareaA, "Ventanilla-A");
        Thread hiloB = new Thread(tareaB, "Ventanilla-B");

        System.out.println("Iniciando las dos ventanillas...");

        hiloA.start();
        hiloB.start();

        try {
            hiloA.join();
            hiloB.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("La prueba fue interrumpida.");
            return;
        }

        System.out.println("Ambas ventanillas finalizaron.");
    }
}

