module org.example.java3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;

    opens org.example.java3 to javafx.fxml;
    exports org.example.java3;
}