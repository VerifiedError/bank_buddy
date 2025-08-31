package com.bankbuddy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;

/**
 * Utility class for organizing and categorizing bank items
 */
public class BankOrganizer
{
	private final ItemManager itemManager;
	
	// Item categories for organization
	private static final Map<String, List<Integer>> ITEM_CATEGORIES = new HashMap<>();
	
	static
	{
		// Combat gear
		ITEM_CATEGORIES.put("Combat", Arrays.asList(
			ItemID.ABYSSAL_WHIP, ItemID.DRAGON_SCIMITAR, ItemID.RUNE_SCIMITAR,
			ItemID.RUNE_PLATEBODY, ItemID.DRAGON_PLATEBODY, ItemID.BARROWS_GLOVES,
			ItemID.DRAGON_BOOTS, ItemID.RUNE_BOOTS, ItemID.COMBAT_BRACELET
		));
		
		// Skilling items
		ITEM_CATEGORIES.put("Skilling", Arrays.asList(
			ItemID.DRAGON_PICKAXE, ItemID.RUNE_PICKAXE, ItemID.DRAGON_AXE,
			ItemID.RUNE_AXE, ItemID.DRAGON_HARPOON, ItemID.LOBSTER_POT,
			ItemID.SMALL_FISHING_NET, ItemID.FLY_FISHING_ROD
		));
		
		// Potions and consumables
		ITEM_CATEGORIES.put("Potions", Arrays.asList(
			ItemID.SUPER_COMBAT_POTION4, ItemID.SUPER_ATTACK4, ItemID.SUPER_STRENGTH4,
			ItemID.SUPER_DEFENCE4, ItemID.RANGING_POTION4, ItemID.MAGIC_POTION4,
			ItemID.PRAYER_POTION4, ItemID.SUPER_RESTORE4, ItemID.ANTIFIRE_POTION4
		));
		
		// Food items
		ITEM_CATEGORIES.put("Food", Arrays.asList(
			ItemID.SHARK, ItemID.LOBSTER, ItemID.SWORDFISH, ItemID.TUNA,
			ItemID.SALMON, ItemID.TROUT, ItemID.MONKFISH, ItemID.KARAMBWAN
		));
		
		// Runes and magic
		ITEM_CATEGORIES.put("Magic", Arrays.asList(
			ItemID.NATURE_RUNE, ItemID.LAW_RUNE, ItemID.DEATH_RUNE,
			ItemID.BLOOD_RUNE, ItemID.SOUL_RUNE, ItemID.COSMIC_RUNE,
			ItemID.CHAOS_RUNE, ItemID.FIRE_RUNE, ItemID.WATER_RUNE,
			ItemID.AIR_RUNE, ItemID.EARTH_RUNE, ItemID.MIND_RUNE
		));
		
		// Ores and bars
		ITEM_CATEGORIES.put("Smithing", Arrays.asList(
			ItemID.IRON_ORE, ItemID.COAL, ItemID.GOLD_ORE, ItemID.MITHRIL_ORE,
			ItemID.ADAMANTITE_ORE, ItemID.RUNITE_ORE, ItemID.IRON_BAR,
			ItemID.STEEL_BAR, ItemID.GOLD_BAR, ItemID.MITHRIL_BAR,
			ItemID.ADAMANTITE_BAR, ItemID.RUNITE_BAR
		));
		
		// Logs and woodcutting
		ITEM_CATEGORIES.put("Woodcutting", Arrays.asList(
			ItemID.LOGS, ItemID.OAK_LOGS, ItemID.WILLOW_LOGS, ItemID.MAPLE_LOGS,
			ItemID.YEW_LOGS, ItemID.MAGIC_LOGS, ItemID.REDWOOD_LOGS
		));
		
		// Seeds and farming
		ITEM_CATEGORIES.put("Farming", Arrays.asList(
			ItemID.POTATO_SEED, ItemID.ONION_SEED, ItemID.CABBAGE_SEED,
			ItemID.TOMATO_SEED, ItemID.SWEETCORN_SEED, ItemID.STRAWBERRY_SEED,
			ItemID.WATERMELON_SEED, ItemID.RANARR_SEED, ItemID.SNAPDRAGON_SEED,
			ItemID.TORSTOL_SEED, ItemID.TREE_SEED, ItemID.WILLOW_SEED
		));
	}
	
	public BankOrganizer(ItemManager itemManager)
	{
		this.itemManager = itemManager;
	}
	
	/**
	 * Categorizes an item based on its ID
	 */
	public String categorizeItem(int itemId)
	{
		for (Map.Entry<String, List<Integer>> category : ITEM_CATEGORIES.entrySet())
		{
			if (category.getValue().contains(itemId))
			{
				return category.getKey();
			}
		}
		
		// Default categorization based on item name/properties
		String itemName = itemManager.getItemComposition(itemId).getName().toLowerCase();
		
		if (itemName.contains("potion"))
		{
			return "Potions";
		}
		else if (itemName.contains("rune"))
		{
			return "Magic";
		}
		else if (itemName.contains("ore") || itemName.contains("bar"))
		{
			return "Smithing";
		}
		else if (itemName.contains("log"))
		{
			return "Woodcutting";
		}
		else if (itemName.contains("seed"))
		{
			return "Farming";
		}
		else if (itemName.contains("fish") || itemName.contains("food"))
		{
			return "Food";
		}
		
		return "Miscellaneous";
	}
	
	/**
	 * Sorts items by value (highest to lowest)
	 */
	public Item[] sortByValue(Item[] items, boolean ascending)
	{
		return Arrays.stream(items)
			.filter(item -> item.getId() != -1)
			.sorted(ascending ? 
				Comparator.comparingLong(item -> itemManager.getItemPrice(item.getId())) :
				Comparator.comparingLong((Item item) -> itemManager.getItemPrice(item.getId())).reversed())
			.toArray(Item[]::new);
	}
	
	/**
	 * Sorts items alphabetically by name
	 */
	public Item[] sortAlphabetically(Item[] items)
	{
		return Arrays.stream(items)
			.filter(item -> item.getId() != -1)
			.sorted(Comparator.comparing(item -> 
				itemManager.getItemComposition(item.getId()).getName()))
			.toArray(Item[]::new);
	}
	
	/**
	 * Finds items worth more than a specified threshold
	 */
	public Map<Integer, Long> findValuableItems(Item[] items, long threshold)
	{
		Map<Integer, Long> valuableItems = new HashMap<>();
		
		for (Item item : items)
		{
			if (item.getId() == -1)
			{
				continue;
			}
			
			long itemValue = itemManager.getItemPrice(item.getId()) * item.getQuantity();
			if (itemValue >= threshold)
			{
				valuableItems.put(item.getId(), itemValue);
			}
		}
		
		return valuableItems;
	}
	
	/**
	 * Gets the total value of items in a specific category
	 */
	public long getCategoryValue(Item[] items, String category)
	{
		long totalValue = 0;
		
		for (Item item : items)
		{
			if (item.getId() == -1)
			{
				continue;
			}
			
			if (categorizeItem(item.getId()).equals(category))
			{
				totalValue += itemManager.getItemPrice(item.getId()) * item.getQuantity();
			}
		}
		
		return totalValue;
	}
	
	/**
	 * Gets all available categories
	 */
	public static String[] getCategories()
	{
		return ITEM_CATEGORIES.keySet().toArray(new String[0]);
	}
}