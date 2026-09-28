package app.models.enums;


public enum Gender {
    FEMININE('F'),
    MASCULINE('M');

    Gender(char character) {
    }

    public static Gender of(String character) {
        if (!character.equalsIgnoreCase("M") || !character.equalsIgnoreCase("F")) {
            throw new RuntimeException("Invalid value for character: " + character);
        }
        return Gender.valueOf(character);
    }
}
