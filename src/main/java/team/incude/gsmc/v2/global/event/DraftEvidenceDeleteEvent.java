package team.incude.gsmc.v2.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class DraftEvidenceDeleteEvent {
    private final UUID draftId;
}
