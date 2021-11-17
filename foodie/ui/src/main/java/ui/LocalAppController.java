package ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import ui.utils.CookbookInterface;
import ui.utils.LocalCookbookAccess;

public class LocalAppController extends AbstractController {

  @Override
  protected void setUpStorage() {
    dataAccess = new LocalCookbookAccess("checkCookbookff.json");
  }

  @FXML
  private Pane mainListView;


  public void initialize(URL url, ResourceBundle rb) {
    setUpStorage();
    initializeRecipesView();
  }

  @Override
  public void update() {
    mainListViewController.update();
  }

}
