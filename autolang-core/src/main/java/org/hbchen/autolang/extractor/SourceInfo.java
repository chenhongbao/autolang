package org.hbchen.autolang.extractor;

import java.util.LinkedList;


public class SourceInfo {
    private final LinkedList<CommentBlock> blocks;
    private Code code;

    public SourceInfo(Code code) {
        this.blocks = new LinkedList<>();
        this.code = code;
    }

    public LinkedList<CommentBlock> blocks() {
        return blocks;
    }

    public Code code() {
        return code;
    }
}
