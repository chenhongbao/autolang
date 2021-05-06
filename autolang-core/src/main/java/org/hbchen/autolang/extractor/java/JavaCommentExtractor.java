package org.hbchen.autolang.extractor.java;

import org.hbchen.autolang.extractor.CommentBlock;
import org.hbchen.autolang.extractor.CommentExtractor;
import org.hbchen.autolang.extractor.SourceInfo;

import java.io.IOException;

public class JavaCommentExtractor extends CommentExtractor {

    private final static String blockBegin = "/*";
    private final static String blockEnd = "*/";
    private final static String lineBegin = "//";
    private final String[] todos = new String[]{"TODO", "todo"};

    public JavaCommentExtractor() {
        super(blockBegin, blockEnd, lineBegin);
    }

    @Override
    public SourceInfo parse(String code) throws IOException {
        var info = extract(code);
        parseJavadoc(info);
        return info;
    }

    private void parseJavadoc(SourceInfo info) {
        for (var block : info.blocks()) {
            parseJavaBlock(block);
        }
    }

    private void parseJavaBlock(CommentBlock block) {
        switch (block.getType()) {
            case BLOCK_BEGIN:
                new JavaCommentBlockParser(blockBegin, blockEnd, block).parse();
                break;
            case LINE_BEGIN:
                new JavaCommentLineParser(lineBegin, block).parse();
                break;
            default:
                throw new IllegalStateException("Not a comment block.");
        }
    }
}
