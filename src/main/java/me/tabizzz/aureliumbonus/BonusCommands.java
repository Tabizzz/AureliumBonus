package me.tabizzz.aureliumbonus;

import com.archyx.aureliumskills.skills.Skills;
import me.tabizzz.klib.KAPI;
import me.tabizzz.klib.acf.BaseCommand;
import me.tabizzz.klib.acf.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("skb|skbonus")
public class BonusCommands extends BaseCommand {

	@Subcommand("all")
	@CommandCompletion("@players")
	@CommandPermission("sk.bonus")
	public void bonusAll(CommandSender sender, @Flags("other") Player player, int amount){
		var bonus = KAPI.getData(player.getUniqueId(), BonusDataSource.class).bonus;
		for (var skill : Skills.values()) {
			bonus.merge(skill, amount, Integer::sum);
		}
	}

	@Subcommand("add")
	@CommandCompletion("@players @skills")
	@CommandPermission("sk.bonus")
	public void bonusAdd(CommandSender sender, @Flags("other") Player player, Skills skill, int amount){
		var bonus = KAPI.getData(player.getUniqueId(), BonusDataSource.class).bonus;
		bonus.merge(skill, amount, Integer::sum);
	}
}
