package Listeners;


import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import java.util.*;


public class MyListener extends ListenerAdapter {
    private List<String> members;
    private List<Member> channelPeople;
    private SlashCommandInteractionEvent events;
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
        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.reply("You must be in a voice channel to use this bot").setEphemeral(true).queue();
        } else {
            events = event;
            channelPeople = event.getMember().getVoiceState().getChannel().getMembers();
            if (channelPeople.size() < 4) {
                event.reply("Not enough people in voice channel to make game").setEphemeral(true).queue();
            }
            if (event.getName().equals("custom-builder")) {
                selectGameSize("Select the game size");
            }
        }
    }

    // when user selects a string option on a drop-down menu
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        this.event = event;
        // if user choose team size
        if (event.getComponentId().equals("chose-game-size")) {
            switch (event.getValues().get(0)) {
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
            if (players > channelPeople.size()) {
                event.reply("Invalid team size").setEphemeral(true).queue();
            } else {
                chooseOption(event);
            }
        }

        if (event.getComponentId().equals("select-members")) {
            List<Member> newList = new ArrayList<Member>();
            for (Member person : channelPeople) {
                if (!person.getUser().getName().equals(event.getValues().get(0))) {
                    newList.add(person);
                }
            }
            channelPeople = newList;
            chooseOption(event);
        }


    }

    public void chooseOption(StringSelectInteractionEvent event) {
        if (channelPeople.size() > players) {
            selectName(event);
        } else {
            createTeams();
        }
    }

    public void selectGameSize(String message) {
        events.reply(message).setEphemeral(true)
                .addActionRow(
                        StringSelectMenu.create("chose-game-size")
                                .addOption("2 vs 2", "2 vs 2")
                                .addOption("3 vs 3", "3 vs 3")
                                .addOption("4 vs 4", "4 vs 4")
                                .addOption("5 vs 5", "5 vs 5")
                                .build())
                .queue();
    }
    // creates a drop-down menu with dynamic options based on who is in the voice channel
    // or who has been excluded by the user
    public void selectName(StringSelectInteractionEvent event) {
        StringSelectMenu.Builder builder = StringSelectMenu.create("select-members");
        for (Member loser : channelPeople) {
            builder.addOption(loser.getUser().getName(), loser.getUser().getName());
        }

        event.reply("Chose who is not playing").setEphemeral(true).addActionRow(builder.build()).queue();
    }

    public void createTeams() {
        List<Member> temp = new ArrayList<>(channelPeople);
        Collections.shuffle(temp);
        String team1 = "";
        String team2 = "";
        for (int i = 0; i < temp.size() / 2; i++) {
            team1 += temp.get(i).getUser().getName() + "\t";
            team2 += temp.get(temp.size() - 1 - i).getUser().getName() + "\t";

        }
        MessageChannel channel = event.getChannel();
        event.reply("Setting teams").setEphemeral(true).queue();
        channel.sendMessage("Team 1:\t" + team1).queue();
        channel.sendMessage("Team 2:\t" + team2).queue();
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


