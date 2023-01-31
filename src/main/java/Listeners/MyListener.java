package Listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.utils.AttachedFile;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.sql.Array;
import java.util.*;


public class MyListener extends ListenerAdapter {
    private List<String> members;
    private List<Member> channelPeople;

    private int players;

    private StringSelectInteractionEvent event;
    public MyListener() {
        members = new ArrayList<>();
        players = 0;
        channelPeople = new ArrayList<>();
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getAuthor().isBot()) {
            return;
        }
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.equals("!aram")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong!").queue();
        }
    }

    // when the user types a slash command
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        channelPeople = event.getMember().getVoiceState().getChannel().getMembers().stream().toList();
        if (event.getName().equals("aram-builder")) {
            event.reply("Pick game size").setEphemeral(true)
                    .addActionRow(
                            StringSelectMenu.create("chose-game-size")
                                    .addOption("1 vs 1", "1 vs 1")
                                    .addOption("2 vs 2", "2 vs 2")
                                    .addOption("3 vs 3", "3 vs 3")
                                    .addOption("4 vs 4", "4 vs 4")
                                    .addOption("5 vs 5", "5 vs 5")
                                    .build())
                    .queue();
        }
    }

    // when user selects a string option on a drop-down menu
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        this.event = event;
        // if user choose team size
        if (event.getComponentId().equals("chose-game-size")) {
            switch (event.getValues().get(0)) {
                case "1 vs 1":
                    players = 2;
                    break;
                case "2 vs 2":
                    players = 4;
                    break;
                case "3 vs 3":
                    players = 6;
                    break;
                case "4 vs 4":
                    players = 8;
                    break;
                case "5 vs 5":
                    players = 10;
                    break;
            }
            System.out.print(players);
            selectName(event);
        }

        // if name was picked to remove
        if (event.getComponentId().equals("lol")) {
            List<Member> newList = new ArrayList<Member>();
            for (Member person : channelPeople) {
                if (!person.getUser().getName().equals(event.getValues().get(0))) {
                    newList.add(person);
                }
            }
            channelPeople = newList;
            System.out.println(channelPeople);
            if (channelPeople.size() >= players) {
                selectName(event);
            } else {
                Collections.shuffle(channelPeople);
                String team1 = "";
                String team2 = "";
                for (int i = 0; i < channelPeople.size() / 2; i++) {
                    team1 += channelPeople.get(i).getUser().getName() + " ";
                    team2 += channelPeople.get(channelPeople.size() - 1 - i).getUser().getName() + " ";

                }
                MessageChannel channel = event.getChannel();
                channel.sendMessage("Team 1: " + team1).queue();
                channel.sendMessage("Team 2: " + team2).queue();
                event.reply("Setting teams").setEphemeral(true).queue();
            }
        }
    }

    // creates a drop-down menu with dynamic options based on who is in the voice channel
    // or who has been excluded by the user
    public void selectName(StringSelectInteractionEvent event) {
        StringSelectMenu.Builder builder = StringSelectMenu.create("lol");
        for (Member loser : channelPeople) {
            builder.addOption(loser.getUser().getName(), loser.getUser().getName());
        }
        event.reply("Chose who is not playing").setEphemeral(true).addActionRow(builder.build()).queue();
    }
}

   /* @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (players != 1) {
            members.add(event.getMentions().getUsers().get(0).getName());
            System.out.println(members);
            event.reply("Chose person who is not playing")
                    .addActionRow(
                            EntitySelectMenu.create("choose-user", EntitySelectMenu.SelectTarget.USER)
                                    .build()).setEphemeral(true)
                    .queue();
        } else {
            Collections.shuffle(members);
            String team1 = "";
            String team2 = "";
            for (int i = 0; i < members.size(); i++) {
            }
            MessageChannel channel = event.getChannel();
            channel.sendMessage(team1).queue();
        }
    }*/


