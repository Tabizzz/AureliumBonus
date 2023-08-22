package me.tabizzz.aureliumbonus;

import com.archyx.aureliumskills.skills.Skills;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tabizzz.klib.KAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderSupport extends PlaceholderExpansion {

	private final AureliumBonus plugin;

	public PlaceholderSupport(AureliumBonus aureliumBonus) {
		this.plugin = aureliumBonus;
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "aureliumbonus";
	}

	@Override
	public @NotNull String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
		if (player == null) {
			return "";
		}
		if (identifier.startsWith("lore_")) {
			var skill = identifier.substring(5);
			try {
				var bonus = KAPI.getData(player.getUniqueId(), BonusDataSource.class).bonus;
				var str = "§7Bonos disponibles: §a";
				return str + bonus.get(Skills.valueOf(skill.toUpperCase()));
			} catch (Exception e) {
				return null;
			}
		}


		return null;
	}
}
