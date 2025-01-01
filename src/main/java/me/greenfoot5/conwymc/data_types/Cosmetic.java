package me.greenfoot5.conwymc.data_types;

public class Cosmetic {

    private final CosmeticType type;
    private final String value;
    private final String name;

    public Cosmetic(CosmeticType type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public CosmeticType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public enum CosmeticType {
        TITLE,
        CHAT_COLOUR,
        JOIN_COLOUR,
        LEAVE_COLOUR
    }
}
