module foodie.ui {
  requires transitive foodie.core;
  requires transitive javafx.base;
  requires transitive javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires java.net.http;
  requires com.fasterxml.jackson.databind;


  exports foodie.ui;
  exports foodie.ui.controllers;
  exports foodie.ui.data;

  opens foodie.ui to javafx.graphics, javafx.fxml;
  opens foodie.ui.controllers to javafx.graphics, javafx.fxml;


}
