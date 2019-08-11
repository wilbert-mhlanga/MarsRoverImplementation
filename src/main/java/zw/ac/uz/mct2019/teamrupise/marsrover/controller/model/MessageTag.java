package zw.ac.uz.mct2019.teamrupise.marsrover.controller.model;

public enum  MessageTag {

    TELEMETRY("T"),
    INITIALIZATION("I"),
    SUCCESS("S"),
    END_OF_LIFE("E"),
    KILLED_BY_MARTIAN("K"),
    HIT_BOULDER_OR_MAP_EDGE("B"),
    FELL_INTO_A_CRATER("C");
    private String value;

    MessageTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
