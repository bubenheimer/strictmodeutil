/*
 * Copyright (c) 2015-2016 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.os;

import android.os.StrictMode;

public final class StrictModeUtils {
    public static void setInstanceLimit(final Class cls, final int limit) {
        final StrictMode.VmPolicy.Builder builder =
                new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy());
        StrictMode.setVmPolicy(builder.setClassInstanceLimit(cls, limit).build());
    }

    public static void setSingletonLimit(final Class cls) {
        setInstanceLimit(cls, 1);
    }

    private StrictModeUtils() {
        throw new UnsupportedOperationException();
    }
}
