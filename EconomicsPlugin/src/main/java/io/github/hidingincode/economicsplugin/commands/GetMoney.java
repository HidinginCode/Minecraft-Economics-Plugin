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

                MongoClient mongoClient = MongoClients.create("mongodb+srv://HidingInCode:fernseh1@economicsplugin.i8bgv.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
                MongoDatabase database = mongoClient.getDatabase("players");
                MongoCollection<Document> col = database.getCollection("economics");

                Document filter = new Document("UUID", UUID);

                col.find(filter).forEach((Consumer<Document>) document -> {
                    player.sendMessage("You have " + document.getInteger("money") + "$");
                });
            }
        }
        return true;
    }

}
