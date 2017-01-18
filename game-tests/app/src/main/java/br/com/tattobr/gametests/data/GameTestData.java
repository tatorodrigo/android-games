package br.com.tattobr.gametests.data;

public class GameTestData {
    private String name;
    private Class<?> clazz;

    public GameTestData(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
