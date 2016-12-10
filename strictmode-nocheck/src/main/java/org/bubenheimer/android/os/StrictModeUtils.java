/*
 * Copyright (c) 2015-2016 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.os;

public final class StrictModeUtils {
    public static void setInstanceLimit(final Class cls, final int limit) {
    }

    public static void setSingletonLimit(final Class cls) {
    }

    private StrictModeUtils() {
        throw new UnsupportedOperationException();
    }
}
