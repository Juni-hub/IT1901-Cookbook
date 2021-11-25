package foodie.ui;

import core.Ingredient;
import core.Recipe;
import foodie.ui.utils.CookbookAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

/**
 * Loads the scene that displays a single recipe. Ability to set favorite and open recipe editor.
 */
public class ViewRecipeController extends AbstractController {

  private ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
  private int portion;
  // private CookbookAccess dataAccess = AbstractController.getAccess();
  private Recipe viewRecipe;

  // private Stage stage;

  @FXML
  private Label recipeTitle;

  @FXML
  private Label labelTag;

  @FXML
  private Label portions;

  @FXML
  private ListView<Ingredient> ingredientsListView;

  @FXML
  private TextArea textField;

  @FXML
  private Button faveButton;

  @FXML
  private Button backButton;

  @FXML
  private Button decreaseButton;

  @FXML
  private Button increaseButton;

  /**
   * Sets or removes favorite for Recipe.
   */
  public void favoriseRecipeButton(ActionEvent ae) {
    if (viewRecipe.getFav() == true) {
      viewRecipe.setFav(false);
      faveButton.setText("Add to favorite");
    } else {
      viewRecipe.setFav(true);
      faveButton.setText("Remove from favorite");
    }
    dataAccess.editRecipe(viewRecipe.getName(), viewRecipe);

  }

  /**
   * Increments portion size in IngredientListView.
   */
  public void incPortion(ActionEvent event) {
    if (this.portion > 0) {
      alterPortions(portion + 1);
    }
  }

  /**
   * Decreases portions size in IngredientListView.
   */
  public void decPortion(ActionEvent event) {
    if (this.portion != 1 && portion != 0) {
      alterPortions(portion - 1);
    }
  }

  /**
   * Alters portions and updated IngredientListView.
   *
   * @param portionSize new portion size
   */
  public void alterPortions(int portionSize) {
    viewRecipe.setPortions(portionSize);
    ingredients.setAll(viewRecipe.getIngredients());
    portions.setText(Integer.toString(portionSize));
    this.portion = portionSize;
  }

  public void initialize(URL location, ResourceBundle resources) {
    ingredientsListView.setCellFactory(listView -> {
      IngredientListCell listCell = new IngredientListCell();
      return listCell;
    });
    ingredientsListView.setItems(ingredients);
    
  }

  /**
   * Loads new RecipeController with selected recipe for editing and sets page to edit recipe.
   *
   * @param ae
   * @throws IOException if failed or interrupted I/O operations
   */
  public void changeSceneToEditRecipe(ActionEvent ae) throws IOException {
    FxmlModel model = SceneHandler.getScenes().get(SceneName.NEWRECIPE);
    Scene scene = model.getScene();

    NewRecipeController controller = (NewRecipeController) model.getController();
    controller.setSelectedRecipe(selectedRecipe);
    controller.setCookbookAccess(dataAccess);
    controller.update();
    stage.setScene(scene);

  }

  @FXML
  private void handleBackbutton() {
    FxmlModel model = SceneHandler.getScenes().get(SceneName.MAIN);
    Scene scene = model.getScene();
    model.getController().update();
    stage.setScene(scene);
  }

  public void initData(Recipe recipe) {
    this.viewRecipe = recipe;
    this.portion = recipe.getPortions();
    if (recipe.getName() != null) {
      recipeTitle.setText(recipe.getName());
    } else {
      recipeTitle.setText("oppskrift");
    }
    if (recipe.getPortions() == 0){
      portions.setText("0");
      portions.setVisible(false);
      increaseButton.setVisible(false);
      decreaseButton.setVisible(false);
    } else {
      portions.setVisible(true);
      portions.setText(Integer.toString(recipe.getPortions()));
      increaseButton.setVisible(true);
      decreaseButton.setVisible(true);
    }
    if (!recipe.getIngredients().isEmpty()) {
      // ingredients.clear();
      ingredients.setAll(recipe.getIngredients());
    } else{
      ingredients.clear();
    }
    if (!(recipe.getDescription().isEmpty() || recipe.getDescription().isBlank())) {
      textField.setText(recipe.getDescription());
    }
    if (recipe.getFav() == true) {
      faveButton.setText("Remove from favorite");
    } else {
      faveButton.setText("Add to favorite");
    }
    if (!recipe.getLabel().isBlank()) {
      labelTag.setText(recipe.getLabel());
    } else {
      labelTag.setText("");
    }

  }


  /**
   * Updates page when switching back to scene.
   */
  @Override
  public void update() {
    System.out.println(selectedRecipe.toString());
    initData(this.selectedRecipe);
  }

  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @Override
  protected void setUpStorage() {
    
  }

  public void setDataAccess(CookbookAccess dataAccess){
    this.dataAccess = dataAccess;
  } 
  

}