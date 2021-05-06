package org.hbchen.autolang.translator;

public class JaJpTokenizer extends Tokenizer {
    @Override
    public String[] tokenize(String text) {
        var tokens = org.atilika.kuromoji.Tokenizer.builder().build().tokenize(text);
        var ss = new String[tokens.size()];
        for (var i = 0; i < tokens.size(); ++i) {
            ss[i] = tokens.get(i).getSurfaceForm();
        }
        return ss;
    }
}
