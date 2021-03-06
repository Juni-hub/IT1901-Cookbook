package foodie.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import foodie.core.Cookbook;
import foodie.core.Recipe;
import java.io.IOException;

/**
 * Deserializer for Cookbook-class.
 */
class CookbookReader extends JsonDeserializer<Cookbook> {

  private RecipeReader recipeReader = new RecipeReader();

  @Override
  public Cookbook deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    if (treeNode instanceof ObjectNode objectNode) {
      Cookbook cookbook = new Cookbook();
      JsonNode recipesNode = objectNode.get("recipes");
      for (JsonNode recipeNode : (ArrayNode) recipesNode) {
        Recipe recipe = recipeReader.deserialize(recipeNode);
        cookbook.addRecipe(recipe);
      }
      return cookbook;
    }
    return null;
  }
}
