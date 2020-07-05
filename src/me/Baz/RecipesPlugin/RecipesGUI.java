package me.Baz.RecipesPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipesGUI implements CommandExecutor, Listener {

	private static final ItemStack EXIT = createExitItem();
	private static final ItemStack FILLER = createFillerItem();

	public final String USAGE = Util.RED + "Usage: /recipe <recipe>";
	public static Map<MinecraftRecipe, Inventory> inventories = new HashMap<>();
	public static Map<Inventory, MinecraftRecipe> invToRecipes = new HashMap<>();
	public static Map<ItemStack, MinecraftRecipe> itemToRecipe = new HashMap<>();
	public static List<Inventory> mainInventories = createMainInventories();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Util.DARK_RED + "You may not use this command!");
			return true;
		}
		Player player = (Player) sender;

		if(label.equalsIgnoreCase("recipes")) {
			
			if(RecipeManager.recipeList.size() == 0) {
				player.sendMessage(Util.RED + "There are no custom recipes in this server!"); return true;
			}
			
			player.openInventory(mainInventories.get(0));
			return true;
		}

		if(label.equalsIgnoreCase("recipe")) {
			if(args.length < 1) {
				player.sendMessage(USAGE);
				return true;
			}

			MinecraftRecipe recipe = RecipeManager.recipes.get(args[0]);
			if(recipe == null) {
				player.sendMessage(Util.RED + "Name of recipe not found! Make sure you spelled it correctly!");
				return true;
			}

			Inventory inv = inventories.get(recipe);
			if(inv == null) {
				inv = createInventory(recipe);
			}
			
			player.openInventory(inv);
			return true;
		}

		return false;
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {

	}

	private static ItemStack createExitItem() {
		ItemStack exit = new ItemStack(Material.BARRIER);
		ItemMeta meta = exit.getItemMeta();
		List<String> lore = new ArrayList<>();
		
		lore.add("");
		lore.add(Util.GRAY + "Click here to exit the menu");
		
		meta.setLore(lore);
		meta.setDisplayName(Util.RED + Util.BOLD + "CLOSE");
		
		exit.setItemMeta(meta);
		return exit;
	}
	
	
	//Creates the filler items in between the actual crafting to disallow players to lose items in the inventories
	private static ItemStack createFillerItem() {
		ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = filler.getItemMeta();
		
		meta.setDisplayName("");
		
		filler.setItemMeta(meta);
		return filler;
	}

	private Inventory createInventory(MinecraftRecipe recipe) {
		ShapedRecipe rec = recipe.getRecipe();
		Inventory inv = Bukkit.createInventory(null, 27, Util.BOLD + Util.GREEN 
				+ rec.getResult().getItemMeta().getDisplayName() + " Recipe");
		
		//Sets the entire inventory to be filled with the filler item, then later overrides the slots needed to be used
		for(int x = 0; x < inv.getSize(); x++) {
			inv.setItem(x, FILLER);
		}
		
		//The starting point, (aka the third space in the inventory)
		int index = 2;
		//For each row in the shape
		for(String str : rec.getShape()) {
			//For each Material in the row
			for(char character : str.toCharArray()) {
				//Saving the itemstack from the Material
				ItemStack item = rec.getIngredientMap().get(character);
				
				//Setting the index in the inventory to the item, then increasing the index by one
				inv.setItem(index, item);
				index++;
			}
			
			//Once the row runs out of materials, it then jumps to the next row's index in the inventory
			index += 6;
		}
		
		//Sets the result to the result position of the inventory
		inv.setItem(15, rec.getResult());
		
		RecipesGUI.inventories.put(recipe, inv);
		RecipesGUI.invToRecipes.put(inv, recipe);
		return inv;
	}

	private static List<Inventory> createMainInventories() {
		List<Inventory> inventories = new ArrayList<>();
		int size = RecipeManager.recipeList.size();
		Inventory inv;

		if(size < 10) {
			inv = Bukkit.createInventory(null, 18, Util.GREEN + Util.BOLD + "Recipe Menu");

			for(int x = 0; x < RecipeManager.recipeList.size(); x++) {

				MinecraftRecipe recipe = RecipeManager.recipeList.get(x);
				ItemStack item = recipe.getRecipe().getResult();
				inv.setItem(9, item);
				
			}
			
			inv.setItem(17, EXIT);
//			MinecraftRecipe recipe = RecipeManager.recipeList.get(0);
//			ItemStack item = recipe.getRecipe().getResult();
//			inv.setItem(9, item);
			inventories.add(inv);
		} else if(size < 19) {
			inv = Bukkit.createInventory(null, 27, Util.GREEN + Util.BOLD + "Recipe Menu");

			for(int x = 0; x < RecipeManager.recipeList.size(); x++) {
				MinecraftRecipe recipe = RecipeManager.recipeList.get(x);
				ItemStack item = recipe.getRecipe().getResult();
				
				inv.setItem(x, item);
			}
		}
		return inventories;
	}

}
