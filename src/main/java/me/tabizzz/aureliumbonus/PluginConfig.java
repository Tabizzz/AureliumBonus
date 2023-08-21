package me.tabizzz.aureliumbonus;

import me.tabizzz.klib.Config.KConfig;
import me.tabizzz.klib.evalex.Expression;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginConfig extends KConfig {

	boolean usePlayerLevel;
	int bonusInterval;
	Expression xpPerBonus;
	Expression maxBonus;

	public PluginConfig(JavaPlugin plugin) {
		super(plugin, "config.yml");
	}

	@Override
	protected void loadFromConfig(FileConfiguration config) {
		usePlayerLevel = config.getBoolean("usePlayerLevel");
		bonusInterval = config.getInt("bonusInterval");
		xpPerBonus = new Expression(config.getString("xpPerBonus"));
		maxBonus = new Expression(config.getString("maxBonus"));
	}
}
