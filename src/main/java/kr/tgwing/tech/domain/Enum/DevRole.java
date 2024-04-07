package kr.tgwing.tech.domain.Enum;

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
