package io.github.hidingincode.economicsplugin.commands;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("giveMoney")) {
                if (args.length == 2) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        if ((Player) sender != Bukkit.getPlayer(args[0])) {
                            if (isInteger(args[1])) {
                                MongoClient mongoClient = MongoClients.create("<Your MongoDB uri goes here>");
                                MongoDatabase database = mongoClient.getDatabase("players"); //Change this to your database
                                MongoCollection<Document> col = database.getCollection("economics"); //change this to your collection

                                Player player = (Player) sender;
                                Player reciever = Bukkit.getPlayer(args[0]);

                                Document playerDoc1 = (Document) col.find(new Document("UUID", player.getUniqueId().toString())).first();
                                Document playerDoc2 = (Document) col.find(new Document("UUID", reciever.getUniqueId().toString())).first();

                                if (playerDoc1.getInteger("money") < Integer.parseInt(args[1])) {
                                    sender.sendMessage("You dont have enough money!");
                                    return false;
                                }

                                if (playerDoc1 != null && playerDoc2 != null) {
                                    int newMoney1 = playerDoc1.getInteger("money") - Integer.parseInt(args[1]);
                                    int newMoney2 = playerDoc2.getInteger("money") + Integer.parseInt(args[1]);

                                    Bson updateValue1 = new Document("money", newMoney1);
                                    Bson updateValue2 = new Document("money", newMoney2);
                                    Bson updateOperation1 = new Document("$set", updateValue1);
                                    Bson updateOperation2 = new Document("$set", updateValue2);
                                    col.findOneAndUpdate(playerDoc1, updateOperation1);
                                    col.findOneAndUpdate(playerDoc2, updateOperation2);

                                    player.sendMessage("You have given " + args[1] + "$ to " + args[0]);
                                    reciever.sendMessage("You have recieved " + args[1] + "$ from " + player.getDisplayName());
                                } else {
                                    player.sendMessage("Please use a valid player name");
                                }
                            }
                            else {
                                sender.sendMessage("You need to use an integer value as the second argument");
                            }
                        } else{
                            sender.sendMessage("You cant send money to yourself");
                        }
                    }
                    else {
                        sender.sendMessage("This isnt a valid player");
                    }
                } else {
                    sender.sendMessage("Please use the command as follows /give <Player> <Amount>");
                }
            }
        }
        return true;
    }



    public boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException ex){
            return false;
        }
    }

}
