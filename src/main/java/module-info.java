module org.example.finalproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.finalproject.controller to javafx.fxml;
    exports org.example.finalproject.controller;

    opens org.example.finalproject.model to javafx.fxml;
    exports org.example.finalproject.model;

    opens org.example.finalproject.model.extras to javafx.fxml;
    exports org.example.finalproject.model.extras;

    opens org.example.finalproject to javafx.fxml;
    exports org.example.finalproject;
}