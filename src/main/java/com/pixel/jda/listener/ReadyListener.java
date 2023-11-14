package com.pixel.jda.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ReadyListener extends ListenerAdapter {

    // 봇이 준비되었을 때 호출되는 메서드(함수)
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Discord Bot is ready!");

        /*  커맨드 예시
        event.getJDA().updateCommands().addCommands(
                Commands.slash("echo", "Repeats messages back to you.")
                        .addOption(OptionType.STRING, "message", "The message to repeat.")
                        .addOption(OptionType.INTEGER, "times", "The number of times to repeat the message.")
                        .addOption(OptionType.BOOLEAN, "ephemeral", "Whether or not the message should be sent as an ephemeral message."),
                Commands.slash("animal", "Finds a random animal")
                        .addOptions(
                                new OptionData(OptionType.STRING, "type", "The type of animal to find")
                                        .addChoice("Bird", "bird")
                                        .addChoice("Big Cat", "bigcat")
                                        .addChoice("Canine", "canine")
                                        .addChoice("Fish", "fish")
                        )
        ).queue(); */
    }
}
