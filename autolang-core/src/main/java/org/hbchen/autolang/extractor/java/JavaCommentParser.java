package org.hbchen.autolang.extractor.java;

import org.hbchen.autolang.extractor.CodeLine;

public class JavaCommentParser {
    protected int skipChar(int offsetInclusive, char ch, CodeLine line) {
        var at = offsetInclusive;
        for (; at < line.getOrigin().length(); ++at) {
            char c = line.getOrigin().charAt(at);
            if (c != ch && !Character.isWhitespace(c)) {
                break;
            }
        }
        return at;
    }
}
