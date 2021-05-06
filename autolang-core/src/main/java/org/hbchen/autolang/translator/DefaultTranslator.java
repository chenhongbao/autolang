package org.hbchen.autolang.translator;

import org.hbchen.autolang.Language;

import java.util.Objects;

public class DefaultTranslator extends Translator {

    private final String ja_jp = "このシステムは技術的なデモであり、翻訳コンポーネントは含まれていません。 デフォルトの実装は、Java 言語のロード・サービス実装ツール、またはこのプログラムのデフォルト実装の置き換えです。";
    private final String zh_cn = "本系统是技术演示，不包括翻译组件，如需请自行实现。默认实现方式为Java语言的加载服务实现工具，或者替换本程序的默认实现。";
    private final String us_en = "This system is a technical demonstration and does not include translation components. If necessary, please implement their own. The default implementation is the Java language's load service implementation tool, or replace the default implementation of the program.";

    @Override
    public String[] translate(Language to, String text, int lines) {
        if (lines <= 0) {
            throw new IllegalArgumentException("Invalid lines: " + lines + ".");
        }
        switch (to) {
            case ZH_CN:
                return splitLines(tokenizeZH_CN(zh_cn), lines);
            case US_EN:
                return splitLines(tokenizeUS_EN(us_en), lines);
            case JA_JP:
                return splitLines(tokenizeJA_JP(ja_jp), lines);
            default:
                throw new UnsupportedOperationException("Language " + to + " not supported.");
        }
    }

    private String[] splitLines(String[] tokens, int lines) {
        if (lines <= 0) {
            throw new Error("Invalid lines: " + lines + ".");
        }
        if (lines == 1) {
            return new String[]{concat(tokens, 0, tokens.length)};
        } else {
            int s1 = tokens.length / lines;
            var r = new String[lines];
            int i = 0;
            for (; i < lines - 1; ++i) {
                r[i] = concat(tokens, i * s1, (i + 1) * s1);
            }
            r[i] = concat(tokens, i * s1, tokens.length);
            return r;
        }
    }

    private String concat(String[] tokens, int startInclusive, int endExclusive) {
        Objects.checkFromToIndex(startInclusive, endExclusive, tokens.length);
        String r = "";
        for (int i = startInclusive; i < endExclusive; ++i) {
            r += tokens[i];
        }
        return r;
    }

    private String[] tokenizeJA_JP(String text) {
        return tokenizeZH_CN(text);
    }

    private String[] tokenizeZH_CN(String text) {
        var r = new String[text.length()];
        for (int i = 0; i < text.length(); ++i) {
            r[i] = text.substring(i, i + 1);
        }
        return r;
    }

    private String[] tokenizeUS_EN(String text) {
        var r = text.split(" ");
        for (var i = 0; i < r.length; ++i) {
            r[i] += " ";
        }
        return r;
    }
}
