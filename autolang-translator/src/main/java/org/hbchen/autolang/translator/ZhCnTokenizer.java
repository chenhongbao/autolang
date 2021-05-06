package org.hbchen.autolang.translator;

import java.util.LinkedList;
import java.util.List;

public class ZhCnTokenizer extends Tokenizer {
    @Override
    public String[] tokenize(String text) {
        List<String> cs = new LinkedList<>();
        int at = 0;
        String x = "";
        for (; at < text.length(); ++at) {
            var c = text.charAt(at);
            if (isAscii(c)) {
                x += c;
            } else {
                if (!x.isBlank()) {
                    cs.add(x);
                    x = "";
                }
                cs.add("" + c);
            }
        }
        return cs.toArray(new String[1]);
    }

    private boolean isAscii(char c) {
        return 0 <= c && c < 128;
    }
}
