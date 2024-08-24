package kr.tgwing.tech.project.domain.Enum;

public enum Part {
    LEADER("LEADER"),
    PM("PM"),
    FRONT("FRONT"),
    BACK("BACK"),
    DESIGNER("DESIGNER");

    private String value;

    Part(String value) {
        this.value = value;
    }
}
