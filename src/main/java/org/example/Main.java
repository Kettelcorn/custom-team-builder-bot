package org.example;

import Listeners.MyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.util.*;

public class Main{
    public static void main(String[] args) throws Exception {
         JDA jda = JDABuilder
                .createDefault("MTA2NjUxNTM4NTI2MTc3MjgwMA.GuXdWg.f7LIoTPWEwiKdcPO09Fak72OTUxPkFlyxIwsGs").enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        jda.addEventListener(new MyListener());

        jda.upsertCommand("custom-builder", "creates ARAM team").setGuildOnly(true).queue();
    }
}