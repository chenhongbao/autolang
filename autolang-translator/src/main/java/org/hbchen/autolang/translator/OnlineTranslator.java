package org.hbchen.autolang.translator;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import org.hbchen.autolang.Language;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class OnlineTranslator extends Translator {

    private final String secretId = "AKIDtYwCfuzEXbNtPgrRxMVR7JEuMf5FPmAI";
    private final String secretKey = "1lEpENGs2waySpdGdx22ssvrdFZdosTs";

    private final BlockingQueue<TranslateInfo> que;
    private final Thread th;

    public OnlineTranslator() {
        que = new LinkedTransferQueue<>();
        th = new Thread(new TranslateWorker(5));
        th.setDaemon(true);
        th.start();
    }

    @Override
    public String[] translate(Language to, String text, int lines) {
        var info = new TranslateInfo(to, text);
        que.offer(info);
        info.join(15, TimeUnit.SECONDS);
        if (info.getError() != null) {
            throw info.getError();
        }
        if (info.getTargetText() == null) {
            throw new Error("Fail translating text: target text null. Text is \' " + info.getText() + "\'");
        }
        var tokens = Tokenizer.create(to).tokenize(info.getTargetText());
        return splitLines(tokens, lines);
    }

    private String[] splitLines(String[] tokens, int lines) {
        if (lines <= 0) {
            throw new Error("Invalid lines: " + lines + ".");
        }
        int s1 = tokens.length / lines;
        var r = new String[lines];
        int i = 0;
        for (; i < lines - 1; ++i) {
            r[i] = concat(tokens, i * s1, (i + 1) * s1);
        }
        r[i] = concat(tokens, i * s1, tokens.length);
        return r;
    }

    private String concat(String[] tokens, int startInclusive, int endExclusive) {
        Objects.checkFromToIndex(startInclusive, endExclusive, tokens.length);
        String r = "";
        for (int i = startInclusive; i < endExclusive; ++i) {
            r += tokens[i];
        }
        return r;
    }

    private String translate(String lang, String text) {
        try {
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tmt.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            TmtClient client = new TmtClient(cred, "ap-guangzhou", clientProfile);

            TextTranslateRequest req = new TextTranslateRequest();
            req.setSourceText(text);
            req.setSource("auto");
            req.setTarget(lang);
            req.setProjectId(0L);

            var str = client.TextTranslate(req).getTargetText();
            if (str == null) {
                throw new Error("Translator returns null.");
            } else {
                return str;
            }
        } catch (TencentCloudSDKException e) {
            throw new Error("Fail translating text.", e);
        }
    }

    private class TranslateWorker implements Runnable {
        private final int traffic;

        private int ctrl;
        private LocalDateTime last;
        private LocalDateTime ts;

        TranslateWorker(int traffic) {
            this.ctrl = 0;
            this.ts = LocalDateTime.now();
            this.last = LocalDateTime.now();
            this.traffic = traffic;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                var ms = Duration.between(last, ts).toMillis();
                if (++ctrl > traffic && ms < 1000) {
                    try {
                        // Sleep one second for traffic ctrl.
                        Thread.sleep(1000);
                        last = LocalDateTime.now();
                        ctrl = 0;
                    } catch (InterruptedException ignored) {
                    }
                }
                TranslateInfo info = null;
                try {
                    info = que.take();
                    var str = translate(LanguageMapper.getName(info.getTarget()), info.getText());
                    info.setTargetText(str);
                } catch (Throwable th) {
                    info.setTargetText(null);
                    info.setError(new Error("Fail translating text: " + info.getText() + ".", th));
                } finally {
                    ts = LocalDateTime.now();
                }
            }
        }
    }
}
