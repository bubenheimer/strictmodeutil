/*
 * Copyright (c) 2015-2016 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.os;

import org.bubenheimer.util.Uninstantiable;
import org.bubenheimer.util.function.Supplier;

public final class StrictModeCore extends Uninstantiable {
    public static void allowThreadDiskReads(final Runnable runnable) {
        runnable.run();
    }
    public static void allowThreadDiskWrites(final Runnable runnable) {
        runnable.run();
    }
    public static <T> T allowThreadDiskReads(final Supplier<T> supplier) {
        return supplier.get();
    }
    public static <T> T allowThreadDiskWrites(final Supplier<T> supplier) {
        return supplier.get();
    }
}
