package me.tabizzz.aureliumbonus;

import com.archyx.aureliumskills.ability.Ability;
import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.api.event.XpGainEvent;
import com.archyx.aureliumskills.data.PlayerData;
import com.archyx.aureliumskills.skills.Skills;
import me.tabizzz.klib.KAPI;
import me.tabizzz.klib.evalex.EvaluationException;
import me.tabizzz.klib.evalex.parser.ParseException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class AureliumBonus extends JavaPlugin implements Listener {

	PluginConfig config;

	@Override
	public void onEnable() {
		// Plugin startup logic
		config = new PluginConfig(this);
		config.reload();

		KAPI.RegisterPlayerData(new BonusDataSource(this));

		new BukkitRunnable() {
			@Override
			public void run() {
				for (var player : getServer().getOnlinePlayers()) {
					var bonus = KAPI.getData(player.getUniqueId(), BonusDataSource.class).bonus;
					for (var skill : Skills.values()) {
						var level = config.usePlayerLevel ? AureliumAPI.getTotalLevel(player) / 15 : AureliumAPI.getSkillLevel(player, skill);
						try {
							var maxBonus = config.maxBonus.with("level", level).evaluate().getNumberValue().intValue();

							var currentBonus = bonus.get(skill);
							if (currentBonus != null) {
								// user already has bonus on this skill
								var newBonus = Math.min(currentBonus + 1, maxBonus);
								bonus.put(skill, newBonus);
							} else {
								// user gain bonus for the first time on this skill
								bonus.put(skill, 1);
							}
						} catch (EvaluationException | ParseException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}.runTaskTimer(this, 20L * 60, config.bonusInterval * 20L * 60);

	}

	@EventHandler
	public void OnXpGain(XpGainEvent event) {
		var bonus = KAPI.getData(event.getPlayer().getUniqueId(), BonusDataSource.class).bonus;
		var player = event.getPlayer();
		var skill = event.getSkill();
		if (skill instanceof Skills sk && bonus.containsKey(sk)) {
			var bonusCount = bonus.get(sk);
			if(bonusCount == 0) {
				return;
			}
			bonus.put(sk, bonusCount - 1);
			var xp = event.getAmount();
			try {
				var level = config.usePlayerLevel ? AureliumAPI.getTotalLevel(player) / 15 : AureliumAPI.getSkillLevel(player, skill);
				var bonusXp = config.xpPerBonus.with("level", level).evaluate().getNumberValue().doubleValue();
				var multiplier = AureliumAPI.getPlugin().getLeveler().getMultiplier(player, skill);
				event.setAmount(xp + getAbilityXp(player, bonusXp * multiplier, getAbility(sk)));
			} catch (EvaluationException | ParseException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private Ability getAbility(Skills skill) {
		return switch (skill) {
			case FARMING -> Ability.FARMER;
			case FORAGING -> Ability.FORAGER;
			case MINING -> Ability.MINER;
			case FISHING -> Ability.FISHER;
			case EXCAVATION -> Ability.EXCAVATOR;
			case ARCHERY -> Ability.ARCHER;
			case DEFENSE -> Ability.DEFENDER;
			case FIGHTING -> Ability.FIGHTER;
			case ENDURANCE -> Ability.RUNNER;
			case AGILITY -> Ability.JUMPER;
			case ALCHEMY -> Ability.ALCHEMIST;
			case ENCHANTING -> Ability.ENCHANTER;
			case HEALING -> Ability.HEALER;
			case FORGING -> Ability.FORGER;
			case SORCERY -> null;
		};
	}

	public double getAbilityXp(Player player, double input, Ability ability) {
		PlayerData playerData = AureliumAPI.getPlugin().getPlayerManager().getPlayerData(player);
		if (playerData != null) {
			double output = input;
			if (ability != null) {
				if (AureliumAPI.getPlugin().getAbilityManager().isEnabled(ability) && playerData.getAbilityLevel(ability) > 0) {
					double modifier = 1;
					modifier += AureliumAPI.getPlugin().getAbilityManager().getValue(ability, playerData.getAbilityLevel(ability)) / 100;
					output *= modifier;
				}
			}
			return output;
		}
		return input;
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
