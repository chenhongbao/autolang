<%@ page import="java.io.IOException" %>
<%@ page import="org.hbchen.autolang.ProgramLanguage" %>
<%@ page import="org.hbchen.autolang.Language" %>
<%@ page import="org.hbchen.autolang.AutoLanguage" %>
<%@ page import="org.hbchen.autolang.translator.LanguageMapper" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="zh-cn">
<head>
    <title>功能展示 | 自动翻译注释到相同母语</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/default.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/highlight.min.js"></script>
    <link rel="stylesheet" href="index.css">
    <script>hljs.highlightAll();</script>
</head>

<body style="margin: 0 10%; padding: 20px 0; color: rgb(16, 54, 23)">
<h1>功能展示 | 自动翻译注释到相同母语</h1>
<textarea name="code" cols="120" rows="30" form="TranslateCode"
          style="resize: none;border-color: rgb(16, 54, 23); padding: 5px">
<%
    /* Don't leave blanks here cause it adds blanks into textarea. */
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    String code = request.getParameter("code");
    String lang = request.getParameter("lang");
    String prog = request.getParameter("program");
    writeWithDefault(out, code, sampleCode());
%>
</textarea>
<br><br>
<form action="index.jsp" method="post" id="TranslateCode"
      style="display: inline; margin-top: 50px">
    <span>请选择计算机程序语言：</span>
    <input type="radio" name="program"
           value="java" <% checkRadio(out, prog, "java");%>>Java
    <br>
    <span>请选择您的母语：</span>
    <input type="radio" name="lang" value="zh" <% checkRadio(out, lang, "zh");%>>中文
    <input type="radio" name="lang" value="en" <% checkRadio(out, lang, "en");%>>英语
    <input type="radio" name="lang" value="ja" <% checkRadio(out, lang, "ja");%>>日本语
    <p>
        <input type="submit" value="提交"/>
    </p>
</form>
<div style="position: relative;left: 900px; top: -600px;">
    <pre><code class="language-java">
<%
    writeWithDefault(out, translate(code, lang, prog), "[发生内部错误]");
%>
</code></pre>
</div>
</body>
</html>
<%!
    private void checkRadio(JspWriter out, String param, String against) throws IOException {
        if (param == null && (against.compareToIgnoreCase("zh") == 0 || against.compareToIgnoreCase("java") == 0)) {
            // Default behavior.
            out.write("checked");
            return;
        }
        if (param != null && param.compareToIgnoreCase(against) == 0) {
            out.write("checked");
        }
    }

    private String translate(String code, String lang, String prog) throws IOException {
        if (code == null || code.isBlank()) {
            code = sampleCode();
        }
        if (prog == null || prog.isBlank()) {
            prog = "java";
        }
        ProgramLanguage program = program(prog);
        if (program == null) {
            return "[不支持" + prog + "程序设计语言]";
        }
        if (lang == null || lang.isBlank()) {
            lang = "zh";
        }
        Language language = language(lang);
        if (language == null) {
            return "[不支持" + lang + "母语语种]";
        }
        AutoLanguage autolang = AutoLanguage.create();
        return autolang.translate(program, language, code);
    }

    private ProgramLanguage program(String prog) {
        for (ProgramLanguage n : ProgramLanguage.values()) {
            if (n.name().compareToIgnoreCase(prog) == 0) {
                return n;
            }
        }
        return null;
    }

    private Language language(String lang) {
        for (Language n : Language.values()) {
            String name = LanguageMapper.getName(n);
            if (name.compareToIgnoreCase(lang) == 0) {
                return n;
            }
        }
        return null;
    }

    private void writeWithDefault(JspWriter out, String code, String defaultCode) throws IOException {
        if (code == null || code.isBlank()) {
            out.write(defaultCode);
        } else {
            out.write(code);
        }
    }

    private String sampleCode() {
        return "/*\n" +
               " * Copyright (c) 2020-2021. Hongbao Chen <chenhongbao@outlook.com>\n" +
               " *\n" +
               " * Licensed under the  GNU Affero General Public License v3.0 and you may not use\n" +
               " * this file except in compliance with the  License. You may obtain a copy of the\n" +
               " * License at https://www.gnu.org/licenses/agpl-3.0.txt\n" +
               " */\n" +
               "package org.hbchen.autolang;\n" +
               "\n" +
               "/*\n" +
               " * 一个中国人写了这段注释。\n" +
               " * 这是一个测试用例。\n" +
               " *\n" +
               " * @author 陈宏葆\n" +
               " * @since 1.0.0\n" +
               " */\n" +
               "public class Sample {\n" +
               "    /**\n" +
               "     * An American coder wrote this comment.\n" +
               "     *\n" +
               "     * @param args command line arguments.\n" +
               "     *             Second document line.\n" +
               "     */\n" +
               "    public static void main(String[] args/* Command line argument. */) {\n" +
               "        // ある日本人がこのメモを書きました。\n" +
               "        // フィボナッチ数を印刷します。\n" +
               "        fibonacci();\n" +
               "    }\n" +
               "\n" +
               "    private static void fibonacci() {\n" +
               "        // TODO 20 フィボナッチ数を印刷します。\n" +
               "    }\n" +
               "}\n";
    }
%>