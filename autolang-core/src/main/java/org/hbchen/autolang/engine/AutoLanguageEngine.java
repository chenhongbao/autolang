package org.hbchen.autolang.engine;

import org.hbchen.autolang.AutoLanguage;
import org.hbchen.autolang.Language;
import org.hbchen.autolang.ProgramLanguage;
import org.hbchen.autolang.extractor.Code;
import org.hbchen.autolang.extractor.CommentElement;
import org.hbchen.autolang.extractor.CommentExtractor;
import org.hbchen.autolang.extractor.SourceInfo;
import org.hbchen.autolang.translator.Translator;

import java.io.IOException;

public class AutoLanguageEngine extends AutoLanguage {

    private final Translator t;

    public AutoLanguageEngine() {
        t = Translator.create();
    }

    @Override
    public String translate(ProgramLanguage program, Language targetLanguage, String code) throws IOException {
        var source = CommentExtractor.create(program).parse(code);
        translate(source, targetLanguage);
        return formSource(source.code());
    }

    private String formSource(Code code) {
        final StringBuilder b = new StringBuilder();
        code.forEach(line -> {
            b.append(line.getTranslated()).append('\n');
        });
        return b.toString();
    }

    private void translate(SourceInfo info, Language target) {
        for (var block : info.blocks()) {
            block.javadoc().forEach(element -> {
                var comment = formComment(element);
                if (comment.isBlank()) {
                    return;
                }
                comment = processQuotes(comment, element);
                var trans = t.translate(target, comment, element.comments().size());
                if (trans.length != element.comments().size()) {
                    throw new Error("Original and translated lines not matched.");
                }
                processTranslated(element, trans);
            });
        }
    }

    private void processTranslated(CommentElement element, String[] trans) {
        var rec = recoverQuotes(element, trans);
        for (var i = 0; i < element.comments().size(); ++i) {
            var l  = element.comments().get(i);
            l.getCode().setTranslated(l.getStartInclusive(), l.getEndExclusive(), rec[i]);
        }
    }

    private String[] recoverQuotes(CommentElement element, String[] trans) {
        var r = new String[trans.length];
        final int[] i = new int[] {0};
        for (; i[0] < trans.length; ++i[0]) {
            r[i[0]] = trans[i[0]];
            element.quotes().entrySet().forEach(entry -> {
                var index = i[0];
                r[index] = r[index].replace(entry.getKey(), entry.getValue());
            });
        }
        return r;
    }

    private String processQuotes(String comment, CommentElement element) {
        int count = 0;
        int start = comment.indexOf("{@");
        while (start != -1) {
            int end = comment.indexOf("}", start);
            if (end == -1) {
                throw new Error("Invalid Javadoc {@..}.");
            }
            var rep = "{" + (count++) + "}";
            var old = comment.substring(start, end + 1);
            element.quotes().put(rep, old);
            comment.replace(old, " " + rep + " ");
            start = comment.indexOf("{@");
        }
        return comment;
    }

    private String formComment(CommentElement doc) {
        StringBuilder b = new StringBuilder();
        doc.comments().forEach(line -> {
            var comment = line.getCode().getOrigin().substring(line.getStartInclusive(), line.getEndExclusive());
            b.append(comment).append(" ");
        });
        return b.toString();
    }
}
