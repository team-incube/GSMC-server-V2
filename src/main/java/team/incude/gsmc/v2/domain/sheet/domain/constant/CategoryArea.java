package team.incude.gsmc.v2.domain.sheet.domain.constant;

import lombok.Getter;

@Getter
public enum CategoryArea {
    MAJOR("전공 영역"), HUMANITIES("인문·인성 영역"), FOREIGN_LANG("외국어 영역");
    private final String displayName;

    CategoryArea(String d) {
        this.displayName = d;
    }

    public String getDisplayName() {
        return displayName;
    }
}