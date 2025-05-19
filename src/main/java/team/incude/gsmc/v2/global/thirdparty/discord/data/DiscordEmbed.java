package team.incude.gsmc.v2.global.thirdparty.discord.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DiscordEmbed(
        @JsonProperty("title") String title,
        @JsonProperty("color") int color,
        @JsonProperty("fields") List<DiscordField> fields,
        @JsonProperty("timestamp") String timestamp
) {}