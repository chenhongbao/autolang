package org.hbchen.autolang.extractor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommentElement {

    private final LinkedList<CommentLine> comments;
    private final Map<String, String> quotes;
    private String type;

    public CommentElement() {
        this.comments = new LinkedList<>();
        this.quotes = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LinkedList<CommentLine> comments() {
        return comments;
    }

    public Map<String, String> quotes() {
        return quotes;
    }
}
