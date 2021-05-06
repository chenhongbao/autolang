package org.hbchen.autolang.translator;

import org.hbchen.autolang.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OnlineTranslatorTest {

    private final String ja_jp = "このシステムは技術的なデモであり、翻訳コンポーネントは含まれていません。 デフォルトの実装は、Java 言語のロード・サービス実装ツール、またはこのプログラムのデフォルト実装の置き換えです。";
    private final String zh_cn = "本系统是技术演示，不包括翻译组件，如需请自行实现。默认实现方式为Java语言的加载服务实现工具，或者替换本程序的默认实现。";
    private final String us_en = "This system is a technical demonstration and does not include translation components. If necessary, please implement their own. The default implementation is the Java language's load service implementation tool, or replace the default implementation of the program.";


    @Test
    void translate() {
        // JA_JP
        testLanguage(Language.ZH_CN, ja_jp, 5);
        testLanguage(Language.US_EN, ja_jp, 5);
        // ZH_CN
        testLanguage(Language.JA_JP, zh_cn, 5);
        testLanguage(Language.US_EN, zh_cn, 5);
        // US_EN
        testLanguage(Language.JA_JP, us_en, 5);
        testLanguage(Language.ZH_CN, us_en, 5);
    }

    private void testLanguage(Language to, String text, int lines) {
        var translator = Translator.create();
        System.out.println("[" + to + "]");
        for (var str: translator.translate(to, text, lines)) {
            System.out.println(str);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }
}