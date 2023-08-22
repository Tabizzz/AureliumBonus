package me.tabizzz.aureliumbonus;

import com.archyx.aureliumskills.skills.Skills;
import me.tabizzz.klib.Data.PlayerDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BonusDataSource implements PlayerDataSource {
	private final AureliumBonus plugin;

	Map<Skills, Integer> bonus;

	public BonusDataSource(AureliumBonus aureliumBonus) {
		this.plugin = aureliumBonus;
		bonus = new HashMap<>();
	}

	@Override
	public @NotNull JavaPlugin getPlugin() {
		return plugin;
	}

	@Override
	public void save(ConfigurationSection configurationSection) {
		for (var skill : Skills.values()) {
			configurationSection.set(skill.toString(), bonus.get(skill));
		}
	}

	@Override
	public void load(ConfigurationSection configurationSection) {
		for (var skill : Skills.values()) {
			bonus.put(skill, configurationSection.getInt(skill.toString(), 0));
		}
	}

	@Override
	public @NotNull PlayerDataSource create() {
		return new BonusDataSource(plugin);
	}

	@Override
	public void setPlayer(Player player, boolean b) {
	}
}
