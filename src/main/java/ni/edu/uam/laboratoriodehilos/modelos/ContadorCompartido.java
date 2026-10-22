package ni.edu.uam.laboratoriodehilos.modelos;



public class ContadorCompartido {

    private int total = 0;

    public synchronized void incrementar() {
        total++;
    }

    public synchronized int getTotal() {
        return total;
    }
}
