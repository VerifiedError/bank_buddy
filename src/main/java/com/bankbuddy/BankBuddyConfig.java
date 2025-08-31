package com.bankbuddy;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("bankbuddy")
public interface BankBuddyConfig extends Config
{
	@ConfigSection(
		name = "General Settings",
		description = "General Bank Buddy settings",
		position = 0
	)
	String generalSettings = "generalSettings";

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
}