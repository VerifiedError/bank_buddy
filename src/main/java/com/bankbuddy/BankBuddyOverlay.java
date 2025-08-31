package com.bankbuddy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class BankBuddyOverlay extends OverlayPanel
{
	private final BankBuddyPlugin plugin;
	private final BankBuddyConfig config;

	@Inject
	private BankBuddyOverlay(BankBuddyPlugin plugin, BankBuddyConfig config)
	{
		super(plugin);
		this.plugin = plugin;
		this.config = config;
		
		setPosition(OverlayPosition.TOP_LEFT);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setResizable(true);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.enableBankOrganization() || !plugin.isBankOpen())
		{
			return null;
		}

		// Build the panel with configurable width
		int panelWidth = config.compactMode() ? 150 : 200;
		panelComponent.setPreferredSize(new Dimension(panelWidth, 0));
		
		// Title with configurable color
		panelComponent.getChildren().add(TitleComponent.builder()
			.text("Bank Buddy")
			.color(config.overlayColor())
			.build());

		// Display total bank value
		if (config.enableItemValues())
		{
			String valueLabel = config.compactMode() ? "Value:" : "Total Value:";
			String valueText = config.compactMode() ? 
				plugin.getFormattedBankValue() : 
				plugin.getFormattedBankValue() + " gp";
				
			panelComponent.getChildren().add(LineComponent.builder()
				.left(valueLabel)
				.right(valueText)
				.rightColor(Color.GREEN)
				.build());
		}

		// Display item counts
		if (!config.compactMode())
		{
			panelComponent.getChildren().add(LineComponent.builder()
				.left("Total Items:")
				.right(String.valueOf(plugin.getTotalItems()))
				.rightColor(Color.WHITE)
				.build());
		}

		String uniqueLabel = config.compactMode() ? "Items:" : "Unique Items:";
		panelComponent.getChildren().add(LineComponent.builder()
			.left(uniqueLabel)
			.right(String.valueOf(plugin.getUniqueItems()))
			.rightColor(Color.YELLOW)
			.build());

		// Calculate and display bank space utilization
		if (config.showBankSpace())
		{
			int bankSpace = 816; // Max bank slots
			int usedSlots = plugin.getUniqueItems();
			double utilization = (double) usedSlots / bankSpace * 100;

			String spaceLabel = config.compactMode() ? "Space:" : "Bank Space:";
			String spaceText = config.compactMode() ? 
				String.format("%.1f%%", utilization) :
				String.format("%.1f%% (%d/%d)", utilization, usedSlots, bankSpace);

			Color spaceColor = utilization >= config.bankFullThreshold() ? Color.RED : 
							  utilization > 75 ? Color.ORANGE : Color.GREEN;

			panelComponent.getChildren().add(LineComponent.builder()
				.left(spaceLabel)
				.right(spaceText)
				.rightColor(spaceColor)
				.build());
		}

		return super.render(graphics);
	}
}