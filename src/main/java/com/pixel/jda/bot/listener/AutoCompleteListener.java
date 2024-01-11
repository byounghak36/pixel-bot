package com.pixel.jda.bot.listener;

import com.pixel.jda.bot.dto.CharacterDTO;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.stream.Collectors;

import static com.pixel.jda.bot.listener.CommandList.*;

public class AutoCompleteListener extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        JDA jda = event.getJDA();
        Guild guild = event.getGuild();
        Member member = event.getMember();

        String discordId = member.getId();

        // 사용된 커멘드
        String cmd = event.getName();

        // 사용된 옵션
        String option = event.getFocusedOption().getName();

        if (DELETE_CHARACTER.equals(cmd) && option.equals(CHARACTER_NAME)) {
            event.replyChoices(getCharacterNames(discordId)).queue();
        }
    }

    /**
     * 사용자의 캐릭터 목록을 가져온다.
     * @param discordId
     * @return
     */
    private List<Command.Choice> getCharacterNames(String discordId) {

        List<CharacterDTO> characters = CharacterDTO.getCharacterByDiscordId(discordId);

        if (characters == null) {
            return null;
        }

        return characters.stream()
                .filter(CharacterDTO::isSubYn)
                .map(character -> new Command.Choice(character.getCharacterName(), character.getCharacterName()))
                .collect(Collectors.toList());
    }
}
