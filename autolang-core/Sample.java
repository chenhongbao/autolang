/*
 * Copyright (c) 2020-2021. Hongbao Chen <chenhongbao@outlook.com>
 *
 * Licensed under the  GNU Affero General Public License v3.0 and you may not use
 * this file except in compliance with the  License. You may obtain a copy of the
 * License at
 *
 *                    https://www.gnu.org/licenses/agpl-3.0.txt
 */
package org.hbchen.autolang;

/*
 * 一个中国人写了这段注释。
 * 这是一个测试用例。
 *
 * @author 陈宏葆
 * @since 1.0.0
 */
public class Sample {
    /**
     * An American coder wrote this comment.
     *
     * @param args command line arguments.
     *             Second doc line.
     */
    public static void main(String[] args/* Command line argument. */) {
        // ある日本人がこのメモを書きました。
        // フィボナッチ数を印刷します。
        fibonacci();
    }

    private static void fibonacci() {
        // TODO 20 フィボナッチ数を印刷します。
    }
}
