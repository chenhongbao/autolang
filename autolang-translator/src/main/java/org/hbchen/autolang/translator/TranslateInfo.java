package org.hbchen.autolang.translator;

import org.hbchen.autolang.Language;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TranslateInfo {
    private final String text;
    private final Language target;
    private final Lock lck;
    private final Condition cnd;
    private Error error;
    private String targetText;

    public TranslateInfo(Language target, String text) {
        this.text = text;
        this.target = target;
        this.lck = new ReentrantLock();
        this.cnd = lck.newCondition();
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getText() {
        return text;
    }

    public Language getTarget() {
        return target;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
        wake();
    }

    private void wake() {
        lck.lock();
        try {
            cnd.signal();
        } finally {
            lck.unlock();
        }
    }

    void join(int timeout, TimeUnit unit) {
        lck.lock();
        try {
            if (!cnd.await(timeout, unit)) {
                throw new Error("Translation timeout.");
            }
        } catch (InterruptedException e) {
            throw new Error("Translation interrupted.", e);
        } finally {
            lck.unlock();
        }
    }
}
