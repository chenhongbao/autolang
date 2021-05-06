package org.hbchen.autolang.extractor.java;

import org.hbchen.autolang.extractor.CodeLine;
import org.hbchen.autolang.extractor.CommentBlock;
import org.hbchen.autolang.extractor.CommentElement;
import org.hbchen.autolang.extractor.CommentLine;

public class JavaCommentBlockParser extends JavaCommentParser {
    private final String blockBegin;
    private final String blockEnd;
    private final CommentBlock block;
    private CommentState state;

    public JavaCommentBlockParser(String blockBegin, String blockEnd, CommentBlock block) {
        this.blockBegin = blockBegin;
        this.blockEnd = blockEnd;
        this.block = block;
    }

    void parse() {
        state = CommentState.WAITING;
        if (block.code().size() == 1) {
            parseSingleLineBlock();
        } else if (block.code().size() == 2) {
            parseFirstBlockLine();
            parseLastBlockLine();
        } else {
            parseFirstBlockLine();
            for (int i = 1; i < block.code().size() - 1; ++i) {
                parseMiddleBlockLine(i);
            }
            parseLastBlockLine();
        }
        processAttrSuffix();
    }

    private void processAttrSuffix() {
        for (var  elem : block.javadoc()) {
            if (!elem.getType().isBlank()) {
                if (elem.comments().isEmpty()) {
                    throw new Error("No comment line for attribute: " + elem.getType() + ".");
                }
                for (var line : elem.comments()) {
                    if (docComment(elem.getType(), line)) {
                        break;
                    }
                }
            }
        }
    }

    private boolean docComment(String type, CommentLine line) {
        if (type.equals("author") | type.equals("deprecated") | type.equals("return") |
            type.equals("serial") | type.equals("serialData")) {
            line.setStartInclusive(skipChar(line.getStartInclusive(), ' ', line.getCode()));
            return true;
        } else if (type.equals("param") | type.equals("exception") | type.equals("throws")) {
            boolean foundName = false;
            for (var s = line.getStartInclusive(); s < line.getEndExclusive(); ++s) {
                char c = line.getCode().getOrigin().charAt(s);
                if (!foundName) {
                    if (!Character.isWhitespace(c)) {
                        foundName = true;
                    }
                } else {
                    if (Character.isWhitespace(c)) {
                        line.setStartInclusive(skipChar(s, ' ', line.getCode()));
                        return true;
                    }
                }
            }
            return false;
        } else if (type.equals("since") | type.equals("see") | type.equals("version")) {
            line.setEndExclusive(line.getStartInclusive());
            return true;
        } else {
            throw new IllegalArgumentException("Unknown type @" + type + ".");
        }
    }

    private void parseMiddleBlockLine(int i) {
        var line = block.code().get(i);
        var at = skipChar(0, '*', line);
        parseLine(at, line.getOrigin().length(), line);
    }

    private void parseLine(int begIndex, int endIndex, CodeLine line) {
        if (begIndex >= endIndex) {
            // Empty comment.
            return;
        }
        String type = "";
        int contentBeg = begIndex;
        int contentEnd = -1;
        for (var at = begIndex; at < endIndex; ++at) {
            char c = line.getOrigin().charAt(at);
            switch (state) {
                case WAITING:
                    if (c == '@' && contentEnd == -1) {
                        state = CommentState.AT_PREFIX;
                    } else {
                        state = CommentState.DESCRIPTION;
                        contentEnd = contentBeg = at;
                    }
                    block.javadoc().add(new CommentElement());
                    break;
                case DESCRIPTION:
                case AT_SUFFIX:
                    if (c == '@' && contentEnd == -1) {
                        state = CommentState.AT_PREFIX;
                        block.javadoc().add(new CommentElement());
                    } else {
                        contentEnd = at;
                    }
                    break;
                case AT_PREFIX:
                    if (c == '@') {
                        throw new Error("Invalid Javadoc attribute: " + type + ".");
                    } else if (Character.isWhitespace(c)) {
                        contentEnd = contentBeg = at;
                        state = CommentState.AT_SUFFIX;
                    } else {
                        type += c;
                    }
                    break;
            }
        }
        addCommentLine(type, contentBeg, contentEnd + 1, line);
    }

    private void addCommentLine(String type, int start, int end, CodeLine line) {
        var e = block.javadoc().getLast();
        if (e == null) {
            throw new Error("Comment element is null.");
        }
        if (e.getType() == null && type != null) {
            e.setType(type);
        }
        var l = new CommentLine(line);
        l.setStartInclusive(start);
        l.setEndExclusive(Math.max(end, 0));
        e.comments().add(l);
    }

    private void parseLastBlockLine() {
        var f = block.code().getLast();
        var x = f.getOrigin().indexOf(blockEnd);
        parseLine(skipChar(0, '*', f), x, f);
    }

    private void parseSingleLineBlock() {
        var f = block.code().getFirst();
        var x = f.getOrigin().indexOf(blockBegin) + blockBegin.length();
        var y = f.getOrigin().indexOf(blockEnd);
        parseLine(skipChar(x, '*', f), y, f);
    }

    private void parseFirstBlockLine() {
        var f = block.code().getFirst();
        var x = f.getOrigin().indexOf(blockBegin) + blockBegin.length();
        parseLine(skipChar(x, '*', f), f.getOrigin().length(), f);
    }

}
