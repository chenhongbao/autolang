package org.hbchen.autolang;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.fail;

public class DesktopAutolangTest {
    @Test
    public void test() {
        var al = AutoLanguage.create();
        try {
            var code = al.translate(ProgramLanguage.Java, Language.ZH_CN, readSample("../autolang-core/Sample.java"));
            System.out.println(code);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private String readSample(String file) {
        File f = new File(file);
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
