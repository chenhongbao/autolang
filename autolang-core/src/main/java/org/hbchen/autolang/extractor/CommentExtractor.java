package org.hbchen.autolang.extractor;

import org.hbchen.autolang.ProgramLanguage;
import org.hbchen.autolang.extractor.java.JavaCommentExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public abstract class CommentExtractor {

    private final String blockBegin;
    private final String blockEnd;
    private final String lineBegin;
    private SourceType type;

    protected CommentExtractor(String blockBegin, String blockEnd, String lineBegin) {
        this.blockBegin = blockBegin;
        this.blockEnd = blockEnd;
        this.lineBegin = lineBegin;
    }

    public static CommentExtractor create(ProgramLanguage language) {
        switch (language) {
            case Java:
                return new JavaCommentExtractor();
            default:
                throw new UnsupportedOperationException("Language " + language.name() + " not supported.");
        }
    }

    protected SourceInfo extract(String code) throws IOException {
        clear();
        return extractInfo(parseCode(code));
    }

    private void clear() {
        type = SourceType.CODE;
    }

    private SourceInfo extractInfo(Code code) {
        SourceInfo info = new SourceInfo(code);
        for (var i = 0; i < code.size(); ++i) {
            var line = code.get(i);
            parseLine(info, line);
        }
        return info;
    }

    private void parseLine(SourceInfo info, CodeLine line) {
        switch (type) {
            case CODE:
                parseCodeLine(info, line);
                break;
            case BLOCK_BEGIN:
                parseCommentBlock(info, line);
                break;
            case LINE_BEGIN:
                parseCommentLine(info, line);
                break;
        }
    }

    private void parseCommentBlock(SourceInfo info, CodeLine line) {
        char c0, c1 = '\0';
        for (var at = 0; at < line.getOrigin().length(); ++at) {
            c0 = c1;
            c1 = line.getOrigin().charAt(at);
            var prefix = build(c0, c1);
            if (prefix.equals(blockEnd)) {
                type = SourceType.CODE;
                break;
            }
        }
        info.blocks().getLast().code().add(line);
    }

    private void parseCommentLine(SourceInfo info, CodeLine line) {
        char c0, c1 = '\0';
        for (var at = 0; at < line.getOrigin().length(); ++at) {
            c0 = c1;
            c1 = line.getOrigin().charAt(at);
            var prefix = build(c0, c1);
            if (prefix.equals(lineBegin)) {
                info.blocks().getLast().code().add(line);
                return;
            }
        }
        type = SourceType.CODE;
    }

    private void parseCodeLine(SourceInfo info, CodeLine line) {
        char c0, c1 = '\0';
        for (var at = 0; at < line.getOrigin().length(); ++at) {
            c0 = c1;
            c1 = line.getOrigin().charAt(at);
            var prefix = build(c0, c1);
            if (prefix.equals(blockBegin)) {
                var block = new CommentBlock(line.getNumber(), SourceType.BLOCK_BEGIN);
                block.code().add(line);
                info.blocks().add(block);
                if (line.getOrigin().indexOf(blockEnd, at + 1) == -1) {
                    /* If block ends in following lines, set state to block.
                    * If block ends in this line, next line is still code. */
                    type = SourceType.BLOCK_BEGIN;
                }
                break;
            } else if (prefix.equals(lineBegin)) {
                var block = new CommentBlock(line.getNumber(), SourceType.LINE_BEGIN);
                block.code().add(line);
                info.blocks().add(block);
                type = SourceType.LINE_BEGIN;
                break;
            }
        }
    }

    private String build(char c0, char c1) {
        return c0 + "" + c1;
    }

    private Code parseCode(String code) throws IOException {
        var r = new Code();
        int number = 0;
        try (BufferedReader br = new BufferedReader(new StringReader(code))) {
            String line = br.readLine();
            while (line != null) {
                r.add(new CodeLine(++number, line));
                line = br.readLine();
            }
        }
        return r;
    }

    public abstract SourceInfo parse(String code) throws IOException;
}
