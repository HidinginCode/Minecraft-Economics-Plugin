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

public class SetMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            if(cmd.getName().equalsIgnoreCase("setMoney")){
                if(args.length == 2){
                    if(sender.isOp()){
                        if(isInteger(args[1])) {
                            Player reciever = Bukkit.getPlayer(args[0]);
                            String recieverUUID = reciever.getUniqueId().toString();

                            MongoClient mongoClient = MongoClients.create("<Your MongoDB uri goes here>");
                            MongoDatabase database = mongoClient.getDatabase("players"); //Change this to your database
                            MongoCollection<Document> col = database.getCollection("economics"); //Change this to your collection
                            Document playerDoc = (Document) col.find(new Document("UUID", recieverUUID)).first();

                            if (playerDoc != null) {
                                Bson updateValue = new Document("money", Integer.parseInt(args[1]));
                                Bson updateOperation = new Document("$set", updateValue);
                                col.updateOne(playerDoc, updateOperation);
                                reciever.sendMessage("Your money has been set to "+Integer.parseInt(args[1])+"$ by an Operator");
                            }
                        }
                        else{
                            sender.sendMessage("The second argument has to be an Integer");
                        }
                    }
                    else {
                        sender.sendMessage("You dont have permissions to use this command");
                    }
                }
                else {
                    sender.sendMessage("/setMoney <Player> <Amount>");
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
