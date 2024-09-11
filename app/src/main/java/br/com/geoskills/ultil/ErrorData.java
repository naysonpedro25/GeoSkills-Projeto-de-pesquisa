package br.com.geoskills.ultil;

public class ErrorData {
    private final String title;
    private final String message;

    public ErrorData(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
