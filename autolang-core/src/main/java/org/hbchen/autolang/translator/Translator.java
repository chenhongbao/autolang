package org.hbchen.autolang.translator;

import org.hbchen.autolang.Language;

import java.util.ServiceLoader;

public abstract class Translator {
    public static Translator create() {
        var it = ServiceLoader.load(Translator.class).iterator();
        if (it.hasNext()) {
            return it.next();
        } else {
            return new DefaultTranslator();
        }
    }

    public abstract String[] translate(Language to, String text, int lines);
}
