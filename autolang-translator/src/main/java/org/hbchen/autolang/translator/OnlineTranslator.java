package org.hbchen.autolang.translator;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import org.hbchen.autolang.Language;

import java.util.Objects;

public class OnlineTranslator extends Translator {

    private final String secretId = "AKIDtYwCfuzEXbNtPgrRxMVR7JEuMf5FPmAI";
    private final String secretKey = "1lEpENGs2waySpdGdx22ssvrdFZdosTs";

    @Override
    public String[] translate(Language to, String text, int lines) {
        var tokens = Tokenizer.create(to).tokenize(translate(LanguageMapper.getName(to), text));
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

            return client.TextTranslate(req).getTargetText();
        } catch (TencentCloudSDKException e) {
            throw new Error("Fail translating text.", e);
        }
    }
}
