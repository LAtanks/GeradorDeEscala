package app.models;

import app.models.enums.Gender;
import app.models.enums.Horary;
import app.models.enums.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Collaborator {
    private String name;
    public Horary horary;

    //Mês e os dias de folga
    private Map<String, Set<LocalDate>> daysOffInMonth;
    private Type collaboratorType;

    public Collaborator(String name, Horary horary, Map<String, Set<LocalDate>> daysOffInMonth, Type collaboratorType) {
        this.name = name;
        this.horary = horary;
        this.daysOffInMonth = daysOffInMonth;
        this.collaboratorType = collaboratorType;
    }

    public Collaborator() {
    }

    @Override
    public String toString() {
        return name + '\'' +
                ", Horario=" + horary +
                ", Dias da folga=" + daysOffInMonth.keySet() + " " + daysOffInMonth.values().stream().map(String::valueOf).toList() +
                ", Cargo=" + collaboratorType.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Collaborator that)) return false;
        return Objects.equals(getName(), that.getName()) && getHorary() == that.getHorary() && Objects.equals(getDaysOffInMonth(), that.getDaysOffInMonth()) && getCollaboratorType() == that.getCollaboratorType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getHorary(), getDaysOffInMonth(), getCollaboratorType());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Horary getHorary() {
        return horary;
    }

    public void setHorary(Horary horary) {
        this.horary = horary;
    }

    public Map<String, Set<LocalDate>> getDaysOffInMonth() {
        return daysOffInMonth;
    }

    public void setDaysOffInMonth(Map<String, Set<LocalDate>> daysOffInMonth) {
        this.daysOffInMonth = daysOffInMonth;
    }

    public Type getCollaboratorType() {
        return collaboratorType;
    }

    public void setCollaboratorType(Type collaboratorType) {
        this.collaboratorType = collaboratorType;
    }
}
