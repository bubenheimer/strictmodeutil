/*
 * Copyright (c) 2015-2019 Uli Bubenheimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.bubenheimer.android.os

import android.os.StrictMode

inline fun allowThreadDiskReads(runnable: () -> Unit) =
        runnable.runWith(StrictMode::allowThreadDiskReads)

inline fun allowThreadDiskWrites(runnable: () -> Unit) =
        runnable.runWith(StrictMode::allowThreadDiskWrites)

inline fun <T> allowThreadDiskReads(supplier: () -> T): T =
        supplier.runWith(StrictMode::allowThreadDiskReads)

inline fun <T> allowThreadDiskWrites(supplier: () -> T): T =
        supplier.runWith(StrictMode::allowThreadDiskWrites)

inline fun allowSlowCalls(runnable: () -> Unit) = runnable.runWith() { allowSlowCalls() }

inline fun <T> allowSlowCalls(supplier: () -> T): T = supplier.runWith() { allowSlowCalls() }

@PublishedApi
internal inline fun (() -> Unit).runWith(policyProcessor: () -> StrictMode.ThreadPolicy) {
    val oldPolicy = policyProcessor()
    try {
        this()
    } finally {
        StrictMode.setThreadPolicy(oldPolicy)
    }
}

@PublishedApi
internal inline fun <T> (() -> T).runWith(policyProcessor: () -> StrictMode.ThreadPolicy): T {
    val oldPolicy = policyProcessor()
    return try {
        this()
    } finally {
        StrictMode.setThreadPolicy(oldPolicy)
    }
}

@PublishedApi
internal fun allowSlowCalls(): StrictMode.ThreadPolicy {
    return StrictMode.getThreadPolicy().also { StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder(it)
                    .permitCustomSlowCalls()
                    .build()
    )}
}
