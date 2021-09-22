package ui;

import java.net.URL;
import java.util.ResourceBundle;

import core.Ingredient;
import core.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ViewRecipeController implements Initializable {

    private Recipe selectedRecipe;
    private ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
    private int portion;

    @FXML
    private Label recipeTitle;

    @FXML
    private Label portions;

    @FXML
	private ListView<Ingredient> ingredientsListView;

    @FXML
    private TextArea textField;


    public void incPortion(ActionEvent event) {
		if (portion > 0) {
			alterPortions(portion + 1);
		}
	}

	public void decPortion(ActionEvent event) {
		if (portion != 1 && portion != 0) {
			alterPortions(portion - 1);
		}
	}

    public void alterPortions(int portionSize){
        selectedRecipe.setPortions(portionSize);
        ingredients.setAll(selectedRecipe.getIngredients());
        portions.setText(Integer.toString(portionSize));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ingredientsListView.setItems(ingredients);
        textField.setText("Hmm her var det tomt...");
    }

    public void initData(Recipe recipe) {
        selectedRecipe = recipe;
        portion = recipe.getPortions();
        if (selectedRecipe.getName() != null) {
			recipeTitle.setText(selectedRecipe.getName());
		} else {
			recipeTitle.setText("oppskrift");
		}
        portions.setText(Integer.toString(recipe.getPortions()));
        if (!recipe.getIngredients().isEmpty()){
            ingredients.addAll(recipe.getIngredients());
        }
        if (!(recipe.getDescription().isEmpty() || recipe.getDescription().isBlank())) {
            textField.setText(recipe.getDescription());
        }
        
    }

}
