package br.com.geoskills.enums;

public enum QuestionEnum {
    COLLECTION_QUESTION("questions"),
    STATEMENT("statement"),
    ANSWER("answer"),
    CHOICES("choices"),
    ID("id");

    private final String value;

    QuestionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
