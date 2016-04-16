package io.jackspera.TelegramChat;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	private final String USER_AGENT = "Mozilla/5.0"; //User-Agent
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "TelegramHelp.yml"));//Carico i config
	Boolean enabled=true;
	public void onEnable(){
		if (cfg.getBoolean("exist")==false){//controllo se i config sono stati modificati
			enabled=false;
			cfg.set("Help","bot=id you receive when making bot");//Scrivo i dati di default nel file di config
			cfg.set("Help2","chat_id=list chat ids of the helper,you can get it with bot API");
			cfg.set("Help3","exist=set to true");
			cfg.set("exist",false);
			cfg.set("bot","12345-CodeOfYourBot");
			cfg.set("chatId",Arrays.asList("12345","00000"));
			try {
		        File file = new File(getDataFolder(), "TelegramHelp.yml");
				cfg.save(file);//Salvo i config
			} catch (IOException e) {
				e.printStackTrace();
			}
			getLogger().warning("Please set the config TelegramHelp.yml.");//Avverto di modificare i config			
		}
        getLogger().info("Activation");//Avverto che l'attivazione finita
	}
	public void onDisable(){}
	
	@Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] arg){
		String text;
		if (cmd.getName().equalsIgnoreCase("helpme") && arg.length>0 &&enabled==true){//Se il comando è helpme abbiamo almeno un parametro e i config esistono
			reloadConfig();//ricarico i config in modo che siano sempre aggiornati
			List<String> user = (List<String>) cfg.getList("chatId");//Lista deigli utenti da notificare
			String bot=cfg.getString("bot");
			HttpURLConnectionExample http = new HttpURLConnectionExample();
			if (sender instanceof Player){
				text=sender.getName().toString()+":";//Indico il nome del player che manda la notifica
			}else{
				text="Console:";//In caso non fosse un player scrivo Console
			}
			for(int i=0;i<arg.length;i++){
				text=text+arg[i]+" ";//Forma la stringa del messaggio
			}
			try {
				http.sendGet(text,user,bot);//Mando la richiesta GET al server
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}else if(enabled!=true){
			getLogger().warning("Config not set.");//Avverto che i config non sono stati modificati
			sender.sendMessage("Config not set.");
			return true;
		}
		return false;
	}
}
class HttpURLConnectionExample{//Classe che manda richiesta GET
	private final String USER_AGENT = "Mozilla/5.0";
	void sendGet(String text,List<String> user,String bot) throws Exception {//funzione GET
		for (int i=0;i<user.size();i++){
			String id=user.get(i);
			String url = "https://api.telegram.org/bot"+bot+"/sendMessage?chat_id="+id+"&text="+text;//mando la richiesta GET al server di Telegram
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.getResponseCode();
		}
	}
}
