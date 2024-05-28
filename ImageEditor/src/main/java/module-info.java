module application.imageeditor {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                    requires org.kordamp.ikonli.javafx;
    requires java.desktop;

    opens application.imageeditor to javafx.fxml;
    exports application.imageeditor;
}