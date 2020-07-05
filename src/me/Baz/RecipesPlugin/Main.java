package me.Baz.RecipesPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() { 
		toConsole(Util.DARK_GREEN + "Recipes Plugin is now starting up!");
		
		if(this.getConfig().getConfigurationSection("recipes") != null) {
			this.getConfig().getConfigurationSection("recipes").getKeys(false).forEach(key -> {
				ConfigurationSection section = this.getConfig().getConfigurationSection("recipes." + key);
				new MinecraftRecipe(deserialiseShapedRecipe(section, key));
			});
		}
		
		
		for(MinecraftRecipe recipe : RecipeManager.recipes.values()) {
			this.getServer().addRecipe(recipe.getRecipe());
		}

		RecipesGUI recipesGUI = new RecipesGUI();
		this.getServer().getPluginCommand("recipes").setExecutor(recipesGUI);
		this.getServer().getPluginCommand("recipe").setExecutor(recipesGUI);
	}
	
	@Override
	public void onDisable() {
		toConsole(Util.DARK_RED + "Recipes Plugin is now shutting down!");
		
		Map<String, Object> configMap = new HashMap<>();
		for(String key : RecipeManager.recipes.keySet())  {
			configMap.put(key, serialiseShapedRecipe(RecipeManager.recipes.get(key).getRecipe()));
		}
		
		this.getConfig().set("recipes", configMap);
		this.saveConfig();
	}
	
	public static Map<String, Object> serialiseShapedRecipe(ShapedRecipe recipe) {
		Map<String, Object> recipeMap = new HashMap<>();
		Map<String, Object> ingredientMap = new HashMap<>();
		
		//To transfer the initial ingredient map into a more useful version 
		//(I'm sorry if this is shit code, it's currently 07:00 and I haven't slept)
		Map<Character, ItemStack> tempMap = recipe.getIngredientMap();
		for(Character character : tempMap.keySet()) {
			ingredientMap.put(character.toString(), tempMap.get(character).getType().name());
		}
		
		recipeMap.put("shape", Arrays.asList(recipe.getShape()));
		recipeMap.put("ingredients", ingredientMap);
		recipeMap.put("result", recipe.getResult().serialize());
		
		return recipeMap;
	}
	
	public ShapedRecipe deserialiseShapedRecipe(ConfigurationSection section, String key) {
		
		ConfigurationSection resultSection = section.getConfigurationSection("result");
		ItemStack result = ItemStack.deserialize(resultSection.getValues(false));
		
		String[] shape = section.getStringList("shape").toArray(new String[3]);

		ConfigurationSection ingredientSection = section.getConfigurationSection("ingredients");
		Map<String, Object> ingredientMap = ingredientSection.getValues(false);
		
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, key), result);
		recipe.shape(shape);
		for(String str : ingredientMap.keySet()) {
			char character = str.charAt(0);
			recipe.setIngredient(character, Material.valueOf(ingredientMap.get(str).toString()));
		}
		
		return recipe;
	}
	
	
	private void toConsole(String str) {
		this.getServer().getConsoleSender().sendMessage(str);
	}
}
