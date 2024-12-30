module org.example.java5 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.java5 to javafx.fxml;
    exports org.example.java5;
}