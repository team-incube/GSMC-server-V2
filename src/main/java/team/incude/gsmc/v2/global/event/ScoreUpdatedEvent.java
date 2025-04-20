package team.incude.gsmc.v2.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ScoreUpdatedEvent {
    private final String studentCode;
}