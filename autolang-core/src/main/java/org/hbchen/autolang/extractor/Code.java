package org.hbchen.autolang.extractor;

import java.util.LinkedList;

public class Code extends LinkedList<CodeLine> {

    @Override
    public CodeLine get(int index) {
        var c = super.get(index);
        if (c.getNumber() != index + 1) {
            throw new IllegalStateException("Line number not matched. (" + (index + 1) + c.getNumber() + ")");
        }
        return c;
    }
}
