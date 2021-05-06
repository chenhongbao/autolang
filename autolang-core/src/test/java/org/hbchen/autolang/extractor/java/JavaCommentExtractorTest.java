package org.hbchen.autolang.extractor.java;

import org.hbchen.autolang.ProgramLanguage;
import org.hbchen.autolang.extractor.CommentExtractor;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JavaCommentExtractorTest {

    @Test
    public void testParse() {
        var extractor = CommentExtractor.create(ProgramLanguage.Java);
        try {
            var info = extractor.parse(readSample());
            for (var c : info.blocks()) {
                for (var x : c.javadoc()) {
                    System.out.println("[" + x.getType() + "]");
                    for (var y : x.comments()) {
                        System.out.println("[" + y.getCode().getNumber() +"]" + y.getCode().getOrigin().substring(y.getStartInclusive(), y.getEndExclusive()));
                    }
                }
            }
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