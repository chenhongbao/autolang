package org.hbchen.autolang.translator;

public class ZhCnTokenizer extends Tokenizer {
    @Override
    public String[] tokenize(String text) {
        var r = new String[text.length()];
        for (int i = 0; i < text.length(); ++i) {
            r[i] = text.substring(i, i + 1);
        }
        return r;
    }
}
