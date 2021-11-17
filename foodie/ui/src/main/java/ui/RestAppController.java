package ui;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import ui.utils.CookbookInterface;
import ui.utils.RemoteCookbookAccess;

public class RestAppController extends AbstractController {

  @FXML
  private Pane mainListView;

  @FXML
  ListViewController mainListViewController;

  private URI uriSetup() {
    URI newUri = null;
    try {
      newUri = new URI("http://localhost:8080/cookbook");
    } catch (URISyntaxException e) {
      System.out.println(e.getMessage());
    }
    return newUri;

  }

  public void initialize(URL url, ResourceBundle rb) {
    setUpStorage();
    initializeRecipesView();
  }

  @Override
  protected void setUpStorage() {
    dataAccess = new RemoteCookbookAccess(uriSetup());
  }

  public void setCookbookAccess(CookbookInterface access) {
    this.dataAccess = access;
    initializeRecipesView();
  }

  @Override
  public void update() {
    mainListViewController.update();
  }

}
