package org.example;

import Listeners.MyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class Main{
    public static void main(String[] args) throws Exception {
        
        // create bot with given token, and add event listeners
         JDA jda = JDABuilder
                .createDefault(System.getenv("DISC_KEY")).enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        jda.addEventListener(new MyListener());

        // slash command for bot
        jda.upsertCommand("custom-builder", "creates ARAM team").setGuildOnly(true).queue();
    }
}
