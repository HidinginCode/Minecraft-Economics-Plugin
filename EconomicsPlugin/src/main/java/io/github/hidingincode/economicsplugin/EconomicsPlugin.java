package io.github.hidingincode.economicsplugin;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.hidingincode.economicsplugin.commands.GetMoney;
import io.github.hidingincode.economicsplugin.commands.GiveMoney;
import io.github.hidingincode.economicsplugin.commands.SetMoney;
import io.github.hidingincode.economicsplugin.events.Events;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class EconomicsPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        GetMoney getMoney = new GetMoney();
        SetMoney setMoney = new SetMoney();
        GiveMoney giveMoney = new GiveMoney();
        getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("getmoney").setExecutor(getMoney);
        getCommand("setmoney").setExecutor(setMoney);
        getCommand("givemoney").setExecutor(giveMoney);
        getLogger().info("Economics plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Economics plugin disabled!");
    }





}
