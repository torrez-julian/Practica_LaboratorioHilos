module ni.edu.uam.laboratoriodehilos {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.laboratoriodehilos to javafx.fxml;
    exports ni.edu.uam.laboratoriodehilos;
}