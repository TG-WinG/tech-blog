package kr.tgwing.tech.project.domain.Enum;

public enum DevType {
    WEB("WEB"),
    APP("APP"),
    WEBAPP("WABAPP");

    private String value;

    DevType(String value) {
        this.value = value;
    }
}
