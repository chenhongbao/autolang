package org.hbchen.autolang;

import org.hbchen.autolang.engine.AutoLanguageEngine;

import java.io.IOException;

public abstract class AutoLanguage {
    public static AutoLanguage create() {
        return new AutoLanguageEngine();
    }

    public abstract String translate(ProgramLanguage program, Language targetLanguage, String code) throws IOException;
}
