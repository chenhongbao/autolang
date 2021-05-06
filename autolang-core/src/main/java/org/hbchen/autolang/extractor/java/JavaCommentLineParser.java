package org.hbchen.autolang.extractor.java;

import org.hbchen.autolang.extractor.CommentBlock;
import org.hbchen.autolang.extractor.CommentElement;
import org.hbchen.autolang.extractor.CommentLine;

public class JavaCommentLineParser extends JavaCommentParser {
    private final String lineBegin;
    private final CommentBlock block;

    public JavaCommentLineParser(String lineBegin, CommentBlock block) {
        this.lineBegin = lineBegin;
        this.block = block;
    }

    public void parse() {
        if (block.code().isEmpty()) {
            throw new Error("No code line.");
        }
        var c = new CommentElement();
        c.setType("");
        block.javadoc().add(c);
        for (var line : block.code()) {
            var cl = new CommentLine(line);
            var offset = line.getOrigin().indexOf(lineBegin) + lineBegin.length();
            cl.setStartInclusive(skipChar(offset, ' ', line));
            cl.setEndExclusive(line.getOrigin().length());
            block.javadoc().getLast().comments().add(cl);
        }
    }
}
