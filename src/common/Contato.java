package common;

public class Contato extends Usuario {
    private String name;

    public Contato setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}