package kr.tgwing.tech.project.domain.Enum;

public enum DevRole {
    LEADER("LEADER"),
    PM("PM"),
    FRONT("FRONT"),
    BACK("BACK"),
    DESIGNER("DESIGNER");

    private String value;

    DevRole(String value) {
        this.value = value;
    }
}
