package org.hbchen.autolang.translator;

public class UsEnTokenizer extends Tokenizer {
    @Override
    public String[] tokenize(String text) {
        var r = text.split(" ");
        for (var i = 0; i < r.length; ++i) {
            r[i] += " ";
        }
        return r;
    }
}
