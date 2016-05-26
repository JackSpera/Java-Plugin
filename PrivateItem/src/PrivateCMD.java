package io.jackspera.privateitem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class PrivateCMD extends JavaPlugin{
	public static boolean privateCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){//Console
			Bukkit.getLogger().info("This command can't run by Console");
			return true;
		}else if(sender instanceof Player){
			Boolean maxPr=Config.check();
			Player p=(Player) sender;
			String uuid=p.getUniqueId().toString();
			ItemStack item=null;//Ogetto in mano
			if(args.length==0){//Private normale
				item = p.getItemInHand();
				return apply(item,sender);
			}else if(args.length>1&&args[0].equals("slot")){//Slot private
				int slot=0;
				try{
					slot=Integer.parseInt(args[1]);
				}catch(Exception e){return false;}
				item=p.getInventory().getItem(slot);
				return apply(item,sender);
			}else if(args.length>0&&args[0].equals("all")){
				for(ItemStack it:p.getInventory()){
					apply(it,sender);
				}
				return true;
			}
			List<String> lore=null;
			if(args.length>0&&args[0].equals("remove")){
				//
				//Remove 
				//
				item=p.getItemInHand();
				ItemMeta meta=item.getItemMeta();
				
				String string=ChatColor.RESET+""+ChatColor.RED+""+ChatColor.UNDERLINE+"PrivateItem:"+p.getUniqueId();
				if(p.hasPermission("js.privateitem.user.removePrivate")&&item.getType()!=Material.AIR&&meta.hasLore()){
					lore=meta.getLore();
					for(int i=0;i<lore.size();i++){
						if(lore.get(i).equals(string)){
							lore.remove(i);
							if(maxPr){
								int n=Config.load(uuid);
								if(n!=-1){
									n=n+item.getAmount();
								}
								Config.save(uuid,n);
							}
						}
					}
					meta.setLore(lore);
					item.setItemMeta(meta);
					return true;
				}
			}
			//Fine Remove
			//
			else if(args.length==0&&sender.hasPermission("js.privateitem.user.handPrivate")){

				
			//Errori
			}else if(!(sender.hasPermission("js.privateitem.user.handPrivate"))){//Non ha il permesso
				sender.sendMessage(ChatColor.RED+"You don't have permission.");
				return true;
			}
		}
		return false;
	}
	private static Boolean apply(ItemStack item,CommandSender sender){
		String string=ChatColor.RESET+""+ChatColor.RED+""+ChatColor.UNDERLINE+"PrivateItem:";
		ItemMeta meta=null;
		try{
			meta=item.getItemMeta();
		}catch(Exception e){
			return true;
		}
		List<String> lore=null;
		Boolean maxPr=Config.check();
		Player p=(Player) sender;
		String uuid=p.getUniqueId().toString();

		
		if(item.getType()!=Material.AIR){//Se c'è qualcosa in mano
			if(meta.hasLore()==false){//La lore non c'è quindi posso bloccare senza problemi
				lore = new ArrayList<String>();
			}else{//Devo controlli
				lore=meta.getLore();//Prendo la lore
				if(lore.get(0).length()>=18){
					String sub=lore.get(0).substring(0, 18);//Prendo la prima parte della lore per anallizzarla
					if(sub.equals(string)){
						p.sendMessage(ChatColor.RED+"This item is alredy claimed!");
						return true;
					}
				}
			}
			//PrivateItem:
			if(maxPr){
				int n=Config.load(uuid);
				//Controllo quanti valori scalare
				if(n!=-1&&n<item.getAmount()&&sender.isOp()==false){
					p.sendMessage(ChatColor.RED+"You don't have more privatizable items.");
					return true;
				}else if(n!=-1&&sender.isOp()==false){
					n=n-item.getAmount();
					Config.save(uuid,n);
				}
			}
			lore.add(0,string+uuid);//Aggiungo alla lore
			meta.setLore(lore);//Salvo i metadata
			item.setItemMeta(meta);//Salvo l'item
			return true;
		}else{//Non c'è nulla in mano
			return false;
		}
	}
	
	
	
	/* OP COMANDS
	 * Modificano i config in game
	 * Pensato per gli Admin
	 */
	 //oprivate set JackSpera2001 99
	 //oprivate 0   1             2
	
	@SuppressWarnings("deprecation")
	public static boolean oprivateCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String uuidP="";
		int value=0;
		int n=0;
		if(args.length>=2&&args[0].equals("setMax")==false&&sender.hasPermission("js.privateitem.op.setMax")){//setMax è l'unico che non vuole un utente
			try{
				uuidP=Bukkit.getPlayer(args[1]).getUniqueId().toString();
				n=Config.load(uuidP);
			}catch(Exception e){
				return false;
			}
		}
		if(args.length>=3){
			try{
			value=Integer.parseInt(args[2]);
			}catch(Exception e){
				return false;
			}
		}
		if(args.length>0){
			if(args[0].equals("set")&&args.length>=3&&sender.hasPermission("js.privateitem.op.set")){ 
				Config.save(uuidP,value);	
			
			}else if(args[0].equals("add")&&args.length>=3&&sender.hasPermission("js.privateitem.op.add")){
				Config.save(uuidP,n+value);
			}else if(args[0].equals("remove")&&args.length>=3&&sender.hasPermission("js.privateitem.op.remove")){
				Config.save(uuidP,n-value);
			}else if(args[0].equals("infinite")&&args.length>=2&&sender.hasPermission("js.privateitem.op.infinite")){
				Config.save(uuidP,-1);
			}else if(args[0].equals("help")&&sender.hasPermission("js.privateitem.op.help")){
				return false;
			}else if(args[0].equals("setMax")&&args.length>=1&&sender.hasPermission("js.privateitem.op.setMax")){
				Config.save("MaxPrivate",Integer.parseInt(args[1]));
			}
		}else{//Non ci sono argomenti
			if(sender.hasPermission("js.privateitem.op.*")) return false;
			else return true;
		}
		return true;
	}
}
