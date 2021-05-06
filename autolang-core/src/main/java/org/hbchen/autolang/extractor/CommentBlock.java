package org.hbchen.autolang.extractor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CommentBlock {

    private final int number;
    private final SourceType type;
    private final LinkedList<CommentElement> javadoc;
    private final LinkedList<CodeLine> codes;

    public CommentBlock(int number, SourceType type) {
        this.number = number;
        this.type = type;
        this.javadoc = new LinkedList<>();
        this.codes = new LinkedList<>();
    }

    public SourceType getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public LinkedList<CodeLine> code() {
        return codes;
    }

    public LinkedList<CommentElement> javadoc() {
        return javadoc;
    }
}
