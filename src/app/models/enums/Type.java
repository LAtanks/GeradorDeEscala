package app.models.enums;

public enum Type {
    FRONT_LINE("Linha de Frente"),
    OPERATOR("Operador"),
    ORIENTADOR_DE_ESTACIONAMENTO("Orientador de estacionamento");

    private final String name;
    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Type getByName(String name){
        return name.contains(FRONT_LINE.name()) ? FRONT_LINE : OPERATOR;
    }

}
