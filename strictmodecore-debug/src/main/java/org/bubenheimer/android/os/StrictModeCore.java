/*
 * Copyright (c) 2015-2016 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.os;

import android.os.StrictMode;

import org.bubenheimer.util.Uninstantiable;
import org.bubenheimer.util.function.Supplier;

public final class StrictModeCore extends Uninstantiable {
    public static void allowThreadDiskReads(final Runnable runnable) {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
        try {
            runnable.run();
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public static void allowThreadDiskWrites(final Runnable runnable) {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            runnable.run();
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public static <T> T allowThreadDiskReads(final Supplier<T> supplier) {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
        try {
            return supplier.get();
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public static <T> T allowThreadDiskWrites(final Supplier<T> supplier) {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            return supplier.get();
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }
}
