package me.Baz.RecipesPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class MinecraftRecipe {
	private ShapedRecipe recipe;
	private List<String> aliases = new ArrayList<>();
	
	public MinecraftRecipe(ShapedRecipe recipe) { 
		this.recipe = recipe; 
		RecipeManager.recipes.put(recipe.getKey().getKey(), this);
		RecipeManager.recipeList.add(this);
	}
	public MinecraftRecipe(NamespacedKey key, ItemStack result, String[] shape, Map<Character, ItemStack> ingredients) {
		ShapedRecipe recipe = new ShapedRecipe(key, result);
		recipe.shape(shape);
		for(Character character : ingredients.keySet()) {
			recipe.setIngredient(character, ingredients.get(character).getType());
		}
		this.recipe = recipe;
		RecipeManager.recipes.put(recipe.getKey().getKey(), this);
		RecipeManager.recipeList.add(this);
	}
	
	public void editRecipe(ShapedRecipe recipe) { this.recipe = recipe; }
	public void setAliases(List<String> aliases) { this.aliases = aliases; }
	
	public ShapedRecipe getRecipe() { return recipe; }
	public List<String> getAliases() { return aliases; }
	public String getKey() { return this.recipe.getKey().getKey(); }
	
}
