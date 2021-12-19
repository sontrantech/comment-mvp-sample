package sontran.geocomply.homeassignment.comment;

public class Constants {
    // Pattern
    public static final String MENTION_PATTERN = "(?:^|.*\\s+)[@]((?=.*[A-Za-z0-9])[\\w-_]+).*";
    public static final String SPECIAL_CHAR_PATTERN = "[^a-zA-Z0-9]";
}
