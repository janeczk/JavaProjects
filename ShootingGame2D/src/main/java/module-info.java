module org.example.java4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.java4 to javafx.fxml;
    exports org.example.java4;
}