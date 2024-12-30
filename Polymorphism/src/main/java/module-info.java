module org.example.java2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.java2 to javafx.fxml;
    exports org.example.java2;
}