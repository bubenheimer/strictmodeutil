/*
 * Copyright (c) 2015-2016 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.os;

import android.os.StrictMode;

import org.bubenheimer.util.Uninstantiable;
import org.bubenheimer.util.function.Supplier;

public final class StrictModeCore extends Uninstantiable {
    private static void allow(
            final Runnable runnable,
            final Supplier<StrictMode.ThreadPolicy> policyProcessor
    ) {
        final StrictMode.ThreadPolicy oldPolicy = policyProcessor.get();
        try {
            runnable.run();
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private static <T> T allow(
            final Supplier<T> supplier,
            final Supplier<StrictMode.ThreadPolicy> policyProcessor
    ) {
        final StrictMode.ThreadPolicy oldPolicy = policyProcessor.get();
        try {
            return supplier.get();
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public static void allowThreadDiskReads(
            final Runnable runnable
    ) {
        allow(runnable, StrictMode::allowThreadDiskReads);
    }

    public static void allowThreadDiskWrites(
            final Runnable runnable
    ) {
        allow(runnable, StrictMode::allowThreadDiskWrites);
    }

    public static <T> T allowThreadDiskReads(
            final Supplier<T> supplier
    ) {
        return allow(supplier, StrictMode::allowThreadDiskReads);
    }

    public static <T> T allowThreadDiskWrites(
            final Supplier<T> supplier
    ) {
        return allow(supplier, StrictMode::allowThreadDiskWrites);
    }

    public static void allowSlowCalls(
            final Runnable runnable
    ) {
        allow(runnable, StrictModeCore::allowSlowCalls);
    }

    public static <T> T allowSlowCalls(
            final Supplier<T> supplier
    ) {
        return allow(supplier, StrictModeCore::allowSlowCalls);
    }

    private static StrictMode.ThreadPolicy allowSlowCalls() {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(oldPolicy)
                .permitCustomSlowCalls()
                .build()
        );
        return oldPolicy;
    }
}
