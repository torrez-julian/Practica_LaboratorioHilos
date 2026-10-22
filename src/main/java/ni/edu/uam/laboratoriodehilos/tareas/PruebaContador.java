package ni.edu.uam.laboratoriodehilos.tareas;




import ni.edu.uam.laboratoriodehilos.modelos.ContadorCompartido;

public class PruebaContador {

    public static void main(String[] args) {

        for (int prueba = 1; prueba <= 10; prueba++) {

            ContadorCompartido contador =
                    new ContadorCompartido();

            Thread ventanillaA = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    contador.incrementar();
                }
            }, "Ventanilla-A");

            Thread ventanillaB = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    contador.incrementar();
                }
            }, "Ventanilla-B");

            ventanillaA.start();
            ventanillaB.start();

            try {
                ventanillaA.join();
                ventanillaB.join();

                System.out.println(
                        "Prueba " + prueba
                                + " | Total: " + contador.getTotal()
                );

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Prueba interrumpida.");
                return;
            }
        }
    }
}
