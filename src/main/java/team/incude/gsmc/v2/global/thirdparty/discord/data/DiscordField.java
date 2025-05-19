package team.incude.gsmc.v2.global.thirdparty.discord.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscordField(
        @JsonProperty("name") String name,
        @JsonProperty("value") String value,
        @JsonProperty("inline") boolean inline
) {}