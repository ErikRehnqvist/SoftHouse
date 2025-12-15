package model;

public enum XMLType {
    PERSON("P"),
    FAMILY("F"),
    ADDRESS("A"),
    PHONE("T"),
    UNKNOWN("-1");
    private final String code;

    XMLType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static XMLType fromCode(String code) {
        for (XMLType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
