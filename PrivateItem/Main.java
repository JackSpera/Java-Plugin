package io.jackspera.privateitem;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		//Config
		File fl=new File(this.getDataFolder(), "MaxPrivate.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(fl);
		cfg.addDefault("HELP|MaxPrivate","Number of privatizable items.0=Infinite");
		cfg.addDefault("MaxPrivate",0);
		cfg.options().copyDefaults(true);
		try {cfg.save(fl);} catch (IOException e) {e.printStackTrace();}
		//END Config
	}
	@Override
	public void onDisable(){}
	
	
	//Comandi
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equals("private")){
			return PrivateCMD.privateCommand(sender,cmd,label,args);
		}else if(cmd.getName().equals("oprivate")){
			return PrivateCMD.oprivateCommand(sender,cmd,label,args);
		}
		return true;
	}
	
	//Eventi
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent event){
		//getLogger().info("PickUpItem Event");
		Event.pickUpGround(event);
	}
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		//getLogger().info("Damage Event");
		Event.damage(event);
	}
	//THIS IS USED FOR DEBUG @EventHandler
	public void debug(PlayerJoinEvent event){
		Player p=event.getPlayer();
		getLogger().info(event.getPlayer().getDisplayName());
		getLogger().info("js.privateitem.handPrivate "+p.hasPermission("js.privateitem.user.handPrivate"));
		getLogger().info("js.privateitem.allPrivate "+p.hasPermission("js.privateitem.user.allPrivate"));
		getLogger().info("js.privateitem.slotPrivate "+p.hasPermission("js.privateitem.user.slotPrivate"));
		getLogger().info("js.privateitem.removePrivate "+p.hasPermission("js.privateitem.user.removePrivate"));

	}
}





