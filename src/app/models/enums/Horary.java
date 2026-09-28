package app.models.enums;

public enum Horary {
    MORNING("Manhã"),
    AFTERNOON("Tarde");


    Horary(String description) {
    }

    public static Horary getByName(String name){
        return name.equalsIgnoreCase(MORNING.name()) ? MORNING : MORNING;
    }
}
