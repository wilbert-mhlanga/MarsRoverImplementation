package zw.ac.uz.mct2019.teamrupise.marsrover.controller.model;

public enum EnemyTag {

    CRATER("c"),
    BOULDER("b"),
    MARTIAN("m");
    private String value;

    EnemyTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
