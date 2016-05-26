package io.jackspera.privateitem;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config extends JavaPlugin{
	/*Funzione SAVE
	 * Salva il valore value
	 * nella posizione uuid
	 * usato principalmente per i player(per uuid)
	 * ma si puo usare anche per altri valori
	 * Funziona come una hash map
	 */
	public static void save(String uuid,int value){
		File fl=new File("plugins/PrivateItem/MaxPrivate.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(fl);
		cfg.set(uuid,value);
		cfg.options().copyDefaults(true);
		try {cfg.save(fl);} catch (IOException e) {e.printStackTrace();}
	}
	/*Funzione LOAD
	 * Ritorna il valore
	 * dell'uuid inserito
	 */
	public static int load(String uuid){
		File fl=new File("plugins/PrivateItem/MaxPrivate.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(fl);
		int max=cfg.getInt("MaxPrivate");
		if(cfg.contains(uuid)==false){//Se è il primo controllo dell'utente
			save(uuid,max);
		}
		return cfg.getInt(uuid);
	}
	/*Funazione CHECK
	 * Controlla se il valore MaxPrivate è stato modificato
	 * e ne ritorna il risultato
	 * 
	 * TRUE modificato
	 * FALSE non modificato
	 */
	public static Boolean check(){
		File fl=new File("plugins/PrivateItem/MaxPrivate.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(fl);
		if(cfg.getInt("MaxPrivate")!=0){
			return true;
		}else{
			return false;
		}
	}
}
