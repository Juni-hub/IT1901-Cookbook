package foodie.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import foodie.core.Cookbook;
import foodie.core.Ingredient;
import foodie.core.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for CookbookPersistence-class. Works as integration test.
 */
public class CookbookPersistenceTest {

  private String defaultCookbookPath =
      System.getProperty("user.dir") + File.separator + ("/src/test/resources/foodie/json/test-cookbook.json");
  private CookbookPersistence persistence;
  private ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    persistence = new CookbookPersistence();
    mapper = new ObjectMapper().registerModule(new CookbookModule());
  }

  private Cookbook createDefaultCookbook() {
    Recipe r1 = new Recipe("Cake");
    r1.setPortions(1);
    r1.setDescription("Recipe for cake");
    r1.setLabel("breakfast");
    r1.addIngredient(new Ingredient("Flour", 200.0, "g"));
    r1.addIngredient(new Ingredient("Egg", 2.0, "stk"));
    Recipe r2 = new Recipe("Hot chocolate");
    r2.setPortions(1);
    r2.setDescription("Good dessert");
    r2.addIngredient(new Ingredient("Sugar", 1.5, "dl"));
    r2.addIngredient(new Ingredient("Cocoa", 1.0, "dl"));
    Cookbook cookbook = new Cookbook();
    cookbook.addRecipe(r1);
    cookbook.addRecipe(r2);
    return cookbook;
  }

  public void writeCookbook(Cookbook cookbook) {
    try (Writer writer = new FileWriter(new File(persistence.getSaveFilePath()), StandardCharsets.UTF_8)) {
      mapper.writerWithDefaultPrettyPrinter().writeValue(writer, cookbook);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private void checkCookbook(Cookbook defaultCookbook, Cookbook testCookbook) {
    assertEquals(defaultCookbook.getRecipes().toString(), testCookbook.getRecipes().toString(),
        "List of recipes was wrong");
    assertEquals(defaultCookbook.getRecipes().get(0).getDescription(),
        testCookbook.getRecipes().get(0).getDescription(), "Description of first recipe was wrong");
  }

  @Test
  public void createObjectMapper() {
    ObjectMapper mapper = CookbookPersistence.createObjectMapper();
    assertNotNull(mapper, "Objectmapper was null");
    assertEquals(mapper.getRegisteredModuleIds().toString(), "[CookbookModule]",
        "The registered modules in mapper was wrong");
  }

  @Test
  public void createModule() {
    SimpleModule module = CookbookPersistence.createModule();
    assertNotNull(module, "Module was null");
    assertEquals(module.getClass(), CookbookModule.class, "Module is not a CookbookModule");
  }

  @Test
  public void readCookbook() {
    Cookbook cookbook = createDefaultCookbook();
    try (Reader reader = new FileReader(defaultCookbookPath, StandardCharsets.UTF_8)) {
      Cookbook readCookbook = persistence.readCookbook(reader);
      checkCookbook(cookbook, readCookbook);
    } catch (IOException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void writeCookbook() {
    Cookbook cookbook = createDefaultCookbook();
    persistence.setSaveFile("/cookbook");
    try (Writer writer = new FileWriter(new File(persistence.getSaveFilePath()), StandardCharsets.UTF_8)) {
      persistence.writeCookbook(cookbook, writer);
      String writtenFile = Files.readString(Paths.get(persistence.getSaveFilePath()));
      String defaultFile = Files.readString(Paths.get(defaultCookbookPath));
      assertEquals(defaultFile, writtenFile, "File was not written correctly");

      // cleanup
      Files.delete(Paths.get(persistence.getSaveFilePath()));
    } catch (IOException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void loadCookbook() {
    // test not allowed to load cookbook without setting path
    assertThrows(IllegalStateException.class, () -> {
      persistence.loadCookbook();}, "No exception was thrown even though saveFilePath was not set");

    // test loading cookbook from existing file
    try {
      persistence.setSaveFile("/cookbook");
      writeCookbook(createDefaultCookbook());
      Cookbook testCookbook = persistence.loadCookbook();
      checkCookbook(createDefaultCookbook(), testCookbook);

      // cleanup
      Files.delete(Paths.get(persistence.getSaveFilePath()));
    } catch (IOException e) {
      fail(e.getMessage());
    }

    // test loading cookbook from nonexisting file
    try {
      persistence.setSaveFile("/cookbook");
      Cookbook cookbook = persistence.loadCookbook();
      assertEquals(0, cookbook.getRecipes().size(), "List of recipes in cookbook should be empty");

      // cleanup
      Files.delete(Paths.get(persistence.getSaveFilePath()));
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSaveCookbook() {
    Cookbook cookbook = createDefaultCookbook();

    // test not allowed to save cookbook without setting path
    assertThrows(IllegalStateException.class, () -> {
      persistence.saveCookbook(cookbook);
    }, "No exception was thrown even though saveFilePath was not set");

    // test saving cookbook to existing file
    try {
      persistence.setSaveFile("/cookbook");
      writeCookbook(cookbook);
      cookbook.addRecipe(new Recipe("Popcorn"));
      persistence.saveCookbook(cookbook);
      Reader reader = new FileReader(persistence.getSaveFilePath(), StandardCharsets.UTF_8);
      Cookbook newCookbook = mapper.readValue(reader, Cookbook.class);
      checkCookbook(cookbook, newCookbook);

      // cleanup
      Files.delete(Paths.get(persistence.getSaveFilePath()));

    } catch (IOException e) {
      fail(e.getMessage());
    }

    // test saving cookbook to new file
    try {
      persistence.setSaveFile("/cookbook");
      persistence.saveCookbook(createDefaultCookbook());
      Reader reader = new FileReader(persistence.getSaveFilePath(), StandardCharsets.UTF_8);
      Cookbook newCookbook = mapper.readValue(reader, Cookbook.class);
      checkCookbook(createDefaultCookbook(), newCookbook);

      // cleanup
      Files.delete(Paths.get(persistence.getSaveFilePath()));

    } catch (IOException e) {
      fail(e.getMessage());
    }

  }

}
