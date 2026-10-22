
package ni.edu.uam.laboratoriodehilos.tareas;

public class ProcesoVentanilla implements Runnable {

    private final String nombre;
    private final int unidades;

    public ProcesoVentanilla(String nombre, int unidades) {
        this.nombre = nombre;
        this.unidades = unidades;
    }

    @Override
    public void run() {
        for (int i = 1; i <= unidades; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            System.out.println(nombre + " procesando unidad " + i
                    + " | Hilo: " + Thread.currentThread().getName());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println(nombre + " ha terminado su trabajo.");
    }
}

