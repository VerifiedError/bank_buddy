package com.bankbuddy;

import com.google.inject.Provides;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Bank Buddy",
	description = "A comprehensive banking assistant plugin that helps organize and manage your bank efficiently",
	tags = {"bank", "banking", "organization", "inventory", "management", "utility"}
)
public class BankBuddyPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private BankBuddyConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private BankBuddyOverlay bankOverlay;

	private BankOrganizer bankOrganizer;
	private boolean bankOpen = false;
	private long totalBankValue = 0;
	private long previousBankValue = 0;
	private int totalItems = 0;
	private int uniqueItems = 0;
	private Map<Integer, Integer> itemQuantities = new HashMap<>();
	private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
	private boolean bankFullWarningShown = false;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Bank Buddy started!");
		overlayManager.add(bankOverlay);
		bankOrganizer = new BankOrganizer(itemManager);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Bank Buddy stopped!");
		overlayManager.remove(bankOverlay);
		bankOpen = false;
		resetBankData();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			if (config.enableWelcomeMessage())
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", 
					"Bank Buddy is ready to help organize your bank!", null);
			}
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() == WidgetInfo.BANK_CONTAINER.getGroupId())
		{
			bankOpen = true;
			bankFullWarningShown = false; // Reset warning when bank reopens
			
			if (config.autoCalculateValue())
			{
				calculateBankValue();
			}
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getInventoryID() == InventoryID.BANK.getId() && bankOpen)
		{
			calculateBankValue();
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (!config.enableSearchEnhancements() || !bankOpen)
		{
			return;
		}

		// Add bank organization menu entries
		if (event.getOption().equals("Examine") && event.getTarget().contains("Bank"))
		{
			MenuEntry[] entries = client.getMenuEntries();
			MenuEntry[] newEntries = new MenuEntry[entries.length + 4];
			System.arraycopy(entries, 0, newEntries, 0, entries.length);

			// Add "Show Bank Stats" option
			MenuEntry statsEntry = client.createMenuEntry(-1);
			statsEntry.setOption("Bank Stats");
			statsEntry.setTarget(event.getTarget());
			statsEntry.setType(MenuAction.RUNELITE);
			newEntries[entries.length] = statsEntry;

			// Add "Calculate Total Value" option
			MenuEntry valueEntry = client.createMenuEntry(-2);
			valueEntry.setOption("Calculate Value");
			valueEntry.setTarget(event.getTarget());
			valueEntry.setType(MenuAction.RUNELITE);
			newEntries[entries.length + 1] = valueEntry;

			// Add "Show Valuable Items" option
			MenuEntry valuableEntry = client.createMenuEntry(-3);
			valuableEntry.setOption("Valuable Items");
			valuableEntry.setTarget(event.getTarget());
			valuableEntry.setType(MenuAction.RUNELITE);
			newEntries[entries.length + 2] = valuableEntry;

			// Add "Category Values" option
			MenuEntry categoryEntry = client.createMenuEntry(-4);
			categoryEntry.setOption("Category Values");
			categoryEntry.setTarget(event.getTarget());
			categoryEntry.setType(MenuAction.RUNELITE);
			newEntries[entries.length + 3] = categoryEntry;

			client.setMenuEntries(newEntries);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!bankOpen || event.getMenuAction() != MenuAction.RUNELITE)
		{
			return;
		}

		switch (event.getId())
		{
			case -1: // Bank Stats
				displayDetailedBankStats();
				break;
			case -2: // Calculate Value
				calculateBankValue();
				displayBankStats();
				break;
			case -3: // Valuable Items
				showValuableItems();
				break;
			case -4: // Category Values
				showCategoryValues();
				break;
		}
	}

	private void displayDetailedBankStats()
	{
		String message = String.format("=== Bank Buddy Stats === | Value: %s gp | Total: %d items | Unique: %d items | Space: %.1f%% used",
			getFormattedBankValue(), totalItems, uniqueItems, (double) uniqueItems / 816 * 100);
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
	}

	private void showValuableItems()
	{
		ItemContainer bankContainer = client.getItemContainer(InventoryID.BANK);
		if (bankContainer == null)
		{
			return;
		}

		Map<Integer, Long> valuableItems = bankOrganizer.findValuableItems(
			bankContainer.getItems(), config.valuableItemThreshold());

		if (valuableItems.isEmpty())
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", 
				"No items found above " + numberFormat.format(config.valuableItemThreshold()) + " gp threshold.", null);
			return;
		}

		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", 
			"=== Valuable Items (>" + numberFormat.format(config.valuableItemThreshold()) + " gp) ===", null);

		int count = 0;
		for (Map.Entry<Integer, Long> entry : valuableItems.entrySet())
		{
			if (count >= 5) // Limit to prevent spam
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", 
					"... and " + (valuableItems.size() - 5) + " more valuable items", null);
				break;
			}

			String itemName = itemManager.getItemComposition(entry.getKey()).getName();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", 
				itemName + ": " + numberFormat.format(entry.getValue()) + " gp", null);
			count++;
		}
	}

	private void showCategoryValues()
	{
		ItemContainer bankContainer = client.getItemContainer(InventoryID.BANK);
		if (bankContainer == null)
		{
			return;
		}

		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "=== Bank Category Values ===", null);

		String[] categories = BankOrganizer.getCategories();
		for (String category : categories)
		{
			long categoryValue = bankOrganizer.getCategoryValue(bankContainer.getItems(), category);
			if (categoryValue > 0)
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", 
					category + ": " + numberFormat.format(categoryValue) + " gp", null);
			}
		}
	}

	private void calculateBankValue()
	{
		ItemContainer bankContainer = client.getItemContainer(InventoryID.BANK);
		if (bankContainer == null)
		{
			resetBankData();
			return;
		}

		// Store previous value for change tracking
		previousBankValue = totalBankValue;
		totalBankValue = 0;
		totalItems = 0;
		uniqueItems = 0;
		itemQuantities.clear();

		Item[] items = bankContainer.getItems();
		for (Item item : items)
		{
			if (item.getId() == -1)
			{
				continue;
			}

			int itemId = item.getId();
			int quantity = item.getQuantity();
			
			// Track item quantities
			itemQuantities.put(itemId, itemQuantities.getOrDefault(itemId, 0) + quantity);
			
			// Calculate value
			long itemPrice = itemManager.getItemPrice(itemId);
			totalBankValue += (long) itemPrice * quantity;
			totalItems += quantity;
		}

		uniqueItems = itemQuantities.size();

		// Check for significant value changes
		checkValueChanges();
		
		// Check for bank space warnings
		checkBankSpace();

		if (config.enableItemValues() && config.autoCalculateValue())
		{
			displayBankStats();
		}
	}

	private void checkValueChanges()
	{
		if (!config.showValueChanges() || previousBankValue == 0)
		{
			return;
		}

		long valueDifference = Math.abs(totalBankValue - previousBankValue);
		if (valueDifference >= config.valueChangeThreshold())
		{
			String changeText = totalBankValue > previousBankValue ? "increased" : "decreased";
			String message = String.format("Bank value %s by %s gp!", 
				changeText, numberFormat.format(valueDifference));
			
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
		}
	}

	private void checkBankSpace()
	{
		if (!config.notifyBankFull() || bankFullWarningShown)
		{
			return;
		}

		int bankSpace = 816; // Max bank slots
		double utilization = (double) uniqueItems / bankSpace * 100;

		if (utilization >= config.bankFullThreshold())
		{
			String message = String.format("Warning: Bank is %.1f%% full! Consider organizing items.", utilization);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
			bankFullWarningShown = true;
		}
	}

	private void displayBankStats()
	{
		if (!bankOpen || !config.enableWelcomeMessage())
		{
			return;
		}

		String valueText = numberFormat.format(totalBankValue);
		String message = String.format("Bank Value: %s gp | Items: %d (%d unique)", 
			valueText, totalItems, uniqueItems);
		
		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
	}

	private void resetBankData()
	{
		previousBankValue = totalBankValue;
		totalBankValue = 0;
		totalItems = 0;
		uniqueItems = 0;
		itemQuantities.clear();
		bankFullWarningShown = false;
	}

	// Getters for overlay
	public boolean isBankOpen()
	{
		return bankOpen;
	}

	public long getTotalBankValue()
	{
		return totalBankValue;
	}

	public int getTotalItems()
	{
		return totalItems;
	}

	public int getUniqueItems()
	{
		return uniqueItems;
	}

	public String getFormattedBankValue()
	{
		return numberFormat.format(totalBankValue);
	}

	@Provides
	BankBuddyConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BankBuddyConfig.class);
	}
}