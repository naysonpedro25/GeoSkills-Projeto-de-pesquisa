package br.com.geoskills.enums;

public enum AuthEnum {
    COLLECTION_USERS("users"),
    NAME("name"),
    EMAIL("email"),
    POINTS("points"),
    PROFILE_SELECTED("profileSelected");

    private final String value;

    AuthEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
