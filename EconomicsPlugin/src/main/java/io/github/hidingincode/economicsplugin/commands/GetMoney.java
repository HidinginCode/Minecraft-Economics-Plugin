package io.github.hidingincode.economicsplugin.commands;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class GetMoney implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("getmoney")) {
                Player player = (Player) sender;
                String UUID = player.getUniqueId().toString();

                MongoClient mongoClient = MongoClients.create("<Your MongoDB uri goes here>");
                MongoDatabase database = mongoClient.getDatabase("players");  //Change this to right db
                MongoCollection<Document> col = database.getCollection("economics"); //Change this to right collection

                Document filter = new Document("UUID", UUID);

                col.find(filter).forEach((Consumer<Document>) document -> {
                    player.sendMessage("You have " + document.getInteger("money") + "$");
                });
            }
        }
        return true;
    }

}
