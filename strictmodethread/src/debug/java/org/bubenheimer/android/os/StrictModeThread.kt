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
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@ExperimentalContracts
public inline fun allowThreadDiskReads(runnable: () -> Unit) {
    contract { callsInPlace(runnable, InvocationKind.EXACTLY_ONCE) }

    runnable.runWith(StrictMode::allowThreadDiskReads)
}

@ExperimentalContracts
public inline fun allowThreadDiskWrites(runnable: () -> Unit) {
    contract { callsInPlace(runnable, InvocationKind.EXACTLY_ONCE) }

    runnable.runWith(StrictMode::allowThreadDiskWrites)
}

@ExperimentalContracts
public inline fun <T> allowThreadDiskReads(supplier: () -> T): T {
    contract { callsInPlace(supplier, InvocationKind.EXACTLY_ONCE) }

    return supplier.runWith(StrictMode::allowThreadDiskReads)
}

@ExperimentalContracts
public inline fun <T> allowThreadDiskWrites(supplier: () -> T): T {
    contract { callsInPlace(supplier, InvocationKind.EXACTLY_ONCE) }

    return supplier.runWith(StrictMode::allowThreadDiskWrites)
}

@ExperimentalContracts
public inline fun allowSlowCalls(runnable: () -> Unit) {
    contract { callsInPlace(runnable, InvocationKind.EXACTLY_ONCE) }

    runnable.runWith { allowSlowCalls() }
}

@ExperimentalContracts
public inline fun <T> allowSlowCalls(supplier: () -> T): T {
    contract { callsInPlace(supplier, InvocationKind.EXACTLY_ONCE) }

    return supplier.runWith { allowSlowCalls() }
}

@ExperimentalContracts
@PublishedApi
internal inline fun (() -> Unit).runWith(policyProcessor: () -> StrictMode.ThreadPolicy) {
    contract {
        callsInPlace(this@runWith, InvocationKind.EXACTLY_ONCE)
        callsInPlace(policyProcessor, InvocationKind.EXACTLY_ONCE)
    }

    val oldPolicy = policyProcessor()
    try {
        this()
    } finally {
        StrictMode.setThreadPolicy(oldPolicy)
    }
}

@ExperimentalContracts
@PublishedApi
internal inline fun <T> (() -> T).runWith(policyProcessor: () -> StrictMode.ThreadPolicy): T {
    contract {
        callsInPlace(this@runWith, InvocationKind.EXACTLY_ONCE)
        callsInPlace(policyProcessor, InvocationKind.EXACTLY_ONCE)
    }

    val oldPolicy = policyProcessor()
    return try {
        this()
    } finally {
        StrictMode.setThreadPolicy(oldPolicy)
    }
}

@PublishedApi
internal fun allowSlowCalls(): StrictMode.ThreadPolicy {
    return StrictMode.getThreadPolicy().also {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder(it)
                .permitCustomSlowCalls()
                .build()
        )
    }
}
