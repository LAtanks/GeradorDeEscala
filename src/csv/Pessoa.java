package csv;

import java.time.LocalDate;
import java.util.Set;

public class Pessoa {

    private String nome;
    private boolean linhaDeFrente;
    private Set<LocalDate> diasDeFolga;

    public Pessoa(String nome, boolean linhaDeFrente, Set<LocalDate> diasDeFolga) {
        this.nome = nome;
        this.linhaDeFrente = linhaDeFrente;
        this.diasDeFolga = diasDeFolga;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isLinhaDeFrente() {
        return linhaDeFrente;
    }

    public void setLinhaDeFrente(boolean linhaDeFrente) {
        this.linhaDeFrente = linhaDeFrente;
    }

    public Set<LocalDate> getDiasDeFolga() {
        return diasDeFolga;
    }

    public void setDiasDeFolga(Set<LocalDate> diasDeFolga) {
        this.diasDeFolga = diasDeFolga;
    }
}
