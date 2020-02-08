package org.firstinspires.ftc.clockworks.helpers;

import java.util.function.Predicate;

public class WaitableInteger {
    private final Object integerLock = new Object();
    private final Object waitersLock = new Object();
    private volatile int integer = 0;

    public void inc() {
        synchronized (integerLock) {
            integer++;
            updateWaiters();
        }
    }

    public void dec() {
        synchronized (integerLock) {
            integer--;
            updateWaiters();
        }
    }

    public void set(int val) {
        synchronized (integerLock) {
            int last = integer;
            integer = val;
            if (last != val) updateWaiters();
        }
    }

    public int get() {
        int val = 0;
        synchronized (integerLock) {
            val = integer;
        }
        return val;
    }

    public void waitFor(int val) {
        if (val == integer) return;
        boolean intExec = false;
        while (val != integer) {
            try {
                waitersLock.wait();
            } catch (InterruptedException iex) {
                intExec = true;
            }
        }
        if (intExec) Thread.currentThread().interrupt();
    }

    public void waitUnless(int val) {
        if (val != integer) return;
        boolean intExec = false;
        while (val == integer) {
            try {
                waitersLock.wait();
            } catch (InterruptedException iex) {
                intExec = true;
            }
        }
        if (intExec) Thread.currentThread().interrupt();
    }

    private void updateWaiters() {
        waitersLock.notifyAll();
    }
}
