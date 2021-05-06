package org.hbchen.autolang.translator;

import org.hbchen.autolang.Language;

public abstract class Tokenizer {

    public static Tokenizer create(Language language) {
        switch (language) {
            case ZH_CN:
                return new ZhCnTokenizer();
            case JA_JP:
                return new JaJpTokenizer();
            case US_EN:
                return new UsEnTokenizer();
            default:
                throw new Error("Unsupported language: " + language + ".");
        }
    }

    public abstract String[] tokenize(String text);
}
