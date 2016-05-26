package io.jackspera.privateitem;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Event extends JavaPlugin{
	public static void pickUpGround(PlayerPickupItemEvent e){
		ItemStack stack=e.getItem().getItemStack();
		if(stack.hasItemMeta()&&stack.getItemMeta().hasLore()){
			List<String> lore = stack.getItemMeta().getLore();
			String string=ChatColor.RESET+""+ChatColor.RED+""+ChatColor.UNDERLINE+"PrivateItem:";
			String stringl=string+e.getPlayer().getUniqueId();
			for(String line:lore){
				if(line.length()>=18&&line.substring(0,18).equals(string)){//Controlla se l'ogetto è privatizato
					if(line.equals(stringl)==false){//Se privatizato non da lui
						e.setCancelled(true);//Cancella l'evento
						e.getPlayer().sendMessage(ChatColor.RED+"This item was protected with /private from another player.");
					}
				}
			}//END FOR
		}//END IF META
	}//END PICKUPGROUND
	
	public static void damage(EntityDamageEvent e){
		if(e.getEntity() instanceof Item){
			ItemStack item=((Item)e.getEntity()).getItemStack();
			if(item.hasItemMeta()&&item.getItemMeta().hasLore()){
				List<String> lore = item.getItemMeta().getLore();
				String string=ChatColor.RESET+""+ChatColor.RED+""+ChatColor.UNDERLINE+"PrivateItem:";
				for(String line:lore){
					if(line.substring(0,18).equals(string)){
						e.setCancelled(true);
					}//END IF
				}//END FOR
			}//END IF META
		}//END IF INSTANCE
	}//END DAMAGE
}
