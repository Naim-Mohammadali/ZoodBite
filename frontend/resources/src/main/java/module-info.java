module example.org.tst1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens example.org.tst1 to javafx.fxml;
    exports example.org.tst1;
}