package org.hbchen.autolang.engine;

import org.hbchen.autolang.AutoLanguage;
import org.hbchen.autolang.Language;
import org.hbchen.autolang.ProgramLanguage;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AutoLanguageEngineTest {

    @Test
    void translate() {
        translate(Language.JA_JP);
        translate(Language.US_EN);
        translate(Language.ZH_CN);
    }

    private void translate(Language lang) {
        var engine = AutoLanguage.create();
        try {
            var src = engine.translate(ProgramLanguage.Java, lang, readSample());
            System.out.println(src);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private String readSample() {
        File f = new File("Sample.java");
        try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
            var code = "";
            var line = br.readLine();
            while (line != null) {
                code += line + "\n";
                line = br.readLine();
            }
            return code;
        } catch (IOException e) {
            fail(e.getMessage());
            return "";
        }
    }
}