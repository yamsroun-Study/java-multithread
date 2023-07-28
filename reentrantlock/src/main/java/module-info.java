module yamsroun.reentrantlock {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens yamsroun.reentrantlock to javafx.fxml;
    exports yamsroun.reentrantlock;
}