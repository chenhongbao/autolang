package org.hbchen.autolang.extractor;

public class CodeLine {

    private final int number;
    private final String origin;
    private String translated;

    public CodeLine(int number, String origin) {
        this.number = number;
        this.origin = origin;
        this.translated = origin;
    }

    public int getNumber() {
        return number;
    }

    public String getOrigin() {
        return origin;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(int startInclusive, int endExclusive, String translated) {
        this.translated = this.translated.replace(origin.substring(startInclusive, endExclusive), translated);
    }
}
