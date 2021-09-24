package io.github.hidingincode.economicsplugin.events;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Events implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://HidingInCode:fernseh1@economicsplugin.i8bgv.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("players");
        MongoCollection<Document> col = database.getCollection("economics");

        Player player = event.getPlayer();
        String name = player.getDisplayName();
        String playerId = player.getUniqueId().toString();
        AtomicBoolean newPlayer = new AtomicBoolean(true);

        Document filter =  new Document("UUID", playerId);
        col.find(filter).forEach((Consumer<Document>) document -> {
            newPlayer.set(false);
        });
        if(newPlayer.get()) {
            Document data = new Document("UUID", playerId)
                    .append("name", name)
                    .append("money", 500);

            col.insertOne(data);
        }
    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent deathEvent){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://HidingInCode:fernseh1@economicsplugin.i8bgv.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("players");
        MongoCollection<Document> col = database.getCollection("economics");

        Player player = deathEvent.getEntity();
        String _UUID = player.getUniqueId().toString();


        Document playerDoc = (Document) col.find(new Document("UUID", _UUID)).first();

        if(playerDoc != null){
            int newMoney = playerDoc.getInteger("money")-10;
            if(newMoney <= 0){newMoney = 0;}
            Bson updateValue = new Document("money", newMoney);
            Bson updateOperation  = new Document("$set", updateValue);
            col.updateOne(playerDoc, updateOperation);
            player.sendMessage("You have lost 10$, now you have "+newMoney+"$");
        }



    }
}
