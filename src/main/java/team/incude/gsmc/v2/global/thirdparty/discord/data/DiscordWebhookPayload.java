package team.incude.gsmc.v2.global.thirdparty.discord.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DiscordWebhookPayload(@JsonProperty("embeds") List<DiscordEmbed> embeds) {
}