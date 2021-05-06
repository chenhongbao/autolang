package org.hbchen.autolang.translator;

import org.hbchen.autolang.Language;
import org.junit.jupiter.api.Test;

class DefaultTranslatorTest {
    @Test
    public void translate() {
        translate(Language.JA_JP);
        translate(Language.US_EN);
        translate(Language.ZH_CN);
    }

    private void translate(Language to) {
        var translated = Translator.create().translate(to, "", 6);
        for (int c = 0; c < translated.length; ++c) {
            System.out.println(c + ":\t" + translated[c]);
        }
    }
}