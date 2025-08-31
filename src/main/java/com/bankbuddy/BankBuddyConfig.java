package com.bankbuddy;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup("bankbuddy")
public interface BankBuddyConfig extends Config
{
	@ConfigSection(
		name = "General Settings",
		description = "General Bank Buddy settings",
		position = 0
	)
	String generalSettings = "generalSettings";

	@ConfigSection(
		name = "Display Settings",
		description = "Customize display options",
		position = 1
	)
	String displaySettings = "displaySettings";

	@ConfigSection(
		name = "Bank Features",
		description = "Bank organization and utility features",
		position = 2
	)
	String bankFeatures = "bankFeatures";

	@ConfigSection(
		name = "Notifications",
		description = "Notification and alert settings",
		position = 3
	)
	String notifications = "notifications";

	// General Settings
	@ConfigItem(
		keyName = "enableWelcomeMessage",
		name = "Enable Welcome Message",
		description = "Show a welcome message when logging in",
		section = generalSettings,
		position = 1
	)
	default boolean enableWelcomeMessage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableBankOrganization",
		name = "Enable Bank Organization",
		description = "Enable automatic bank organization features",
		section = generalSettings,
		position = 2
	)
	default boolean enableBankOrganization()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableItemValues",
		name = "Enable Item Value Display",
		description = "Show item values in the bank interface",
		section = generalSettings,
		position = 3
	)
	default boolean enableItemValues()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableSearchEnhancements",
		name = "Enable Search Enhancements",
		description = "Enhance bank search functionality",
		section = generalSettings,
		position = 4
	)
	default boolean enableSearchEnhancements()
	{
		return true;
	}

	// Display Settings
	@Alpha
	@ConfigItem(
		keyName = "overlayColor",
		name = "Overlay Color",
		description = "Color of the bank buddy overlay",
		section = displaySettings,
		position = 1
	)
	default Color overlayColor()
	{
		return Color.CYAN;
	}

	@ConfigItem(
		keyName = "showBankSpace",
		name = "Show Bank Space Usage",
		description = "Display bank space utilization percentage",
		section = displaySettings,
		position = 2
	)
	default boolean showBankSpace()
	{
		return true;
	}

	@ConfigItem(
		keyName = "compactMode",
		name = "Compact Display Mode",
		description = "Use a more compact overlay display",
		section = displaySettings,
		position = 3
	)
	default boolean compactMode()
	{
		return false;
	}

	// Bank Features
	@ConfigItem(
		keyName = "highlightValuableItems",
		name = "Highlight Valuable Items",
		description = "Highlight items above a certain value threshold",
		section = bankFeatures,
		position = 1
	)
	default boolean highlightValuableItems()
	{
		return true;
	}

	@Range(min = 1000, max = 100000000)
	@Units(Units.GP)
	@ConfigItem(
		keyName = "valuableItemThreshold",
		name = "Valuable Item Threshold",
		description = "Minimum value to consider an item valuable (in GP)",
		section = bankFeatures,
		position = 2
	)
	default int valuableItemThreshold()
	{
		return 100000;
	}

	@ConfigItem(
		keyName = "enableBankTabs",
		name = "Enhanced Bank Tabs",
		description = "Show enhanced information for bank tabs",
		section = bankFeatures,
		position = 3
	)
	default boolean enableBankTabs()
	{
		return true;
	}

	@ConfigItem(
		keyName = "autoCalculateValue",
		name = "Auto Calculate Bank Value",
		description = "Automatically calculate bank value when opening bank",
		section = bankFeatures,
		position = 4
	)
	default boolean autoCalculateValue()
	{
		return true;
	}

	// Notifications
	@ConfigItem(
		keyName = "notifyBankFull",
		name = "Notify When Bank Nearly Full",
		description = "Show notification when bank space is running low",
		section = notifications,
		position = 1
	)
	default boolean notifyBankFull()
	{
		return true;
	}

	@Range(min = 75, max = 99)
	@ConfigItem(
		keyName = "bankFullThreshold",
		name = "Bank Full Threshold",
		description = "Bank space percentage to trigger full warning",
		section = notifications,
		position = 2
	)
	default int bankFullThreshold()
	{
		return 90;
	}

	@ConfigItem(
		keyName = "showValueChanges",
		name = "Show Value Changes",
		description = "Display notifications when bank value changes significantly",
		section = notifications,
		position = 3
	)
	default boolean showValueChanges()
	{
		return false;
	}

	@Range(min = 100000, max = 50000000)
	@Units(Units.GP)
	@ConfigItem(
		keyName = "valueChangeThreshold",
		name = "Value Change Threshold",
		description = "Minimum change in bank value to trigger notification",
		section = notifications,
		position = 4
	)
	default int valueChangeThreshold()
	{
		return 1000000;
	}
}