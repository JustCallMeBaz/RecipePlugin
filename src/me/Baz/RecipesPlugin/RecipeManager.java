package me.Baz.RecipesPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeManager {
	public static Map<String, MinecraftRecipe> recipes = new HashMap<>();
	public static List<MinecraftRecipe> recipeList = new ArrayList<>();
	
	public static MinecraftRecipe addRecipe(ShapedRecipe recipe) {
		return new MinecraftRecipe(recipe);
	}
	public static MinecraftRecipe addRecipe(NamespacedKey key, ItemStack result, String[] shape, Map<Character, ItemStack> ingredients) {
		ShapedRecipe recipe = new ShapedRecipe(key, result);
		recipe.shape(shape);
		for(Character character : ingredients.keySet()) {
			recipe.setIngredient(character, ingredients.get(character).getType());
		}
		return new MinecraftRecipe(recipe);
	}
	
	public static void addAlias(String aliasName, MinecraftRecipe recipe) {
		recipes.put(aliasName, recipe);
	}
	
	public static MinecraftRecipe getRecipe(String alias) {
		return recipes.get(alias);
	}
	
}
