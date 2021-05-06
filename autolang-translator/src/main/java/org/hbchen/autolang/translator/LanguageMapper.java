package org.hbchen.autolang.translator;

import org.hbchen.autolang.Language;

public class LanguageMapper {

    public static String getName(Language lang) {
        switch (lang) {
            case ZH_CN:
                return "zh";
            case JA_JP:
                return "ja";
            case US_EN:
                return "en";
            default:
                throw new Error("Unsupported language: " + lang + ".");
        }
    }
}
