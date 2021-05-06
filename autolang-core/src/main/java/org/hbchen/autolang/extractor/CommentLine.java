package org.hbchen.autolang.extractor;

public class CommentLine {
    private final CodeLine code;
    private Integer startInclusive;
    private Integer endExclusive;

    public CommentLine( CodeLine line) {
        this.code = line;
    }

    public CodeLine getCode() {
        return code;
    }

    public Integer getStartInclusive() {
        return startInclusive;
    }

    public void setStartInclusive(int startInclusive) {
        this.startInclusive = startInclusive;
    }

    public Integer getEndExclusive() {
        return endExclusive;
    }

    public void setEndExclusive(int endExclusive) {
        this.endExclusive = endExclusive;
    }
}
