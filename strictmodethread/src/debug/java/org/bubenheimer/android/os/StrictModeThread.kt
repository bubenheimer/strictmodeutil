/*
 * Copyright (c) 2023 Uli Bubenheimer
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
import android.os.StrictMode.ThreadPolicy
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public inline fun <T> allowThreadDiskReads(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

    return runWith(block, StrictMode::allowThreadDiskReads)
}

@OptIn(ExperimentalContracts::class)
public inline fun <T> allowThreadDiskWrites(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

    return runWith(block, StrictMode::allowThreadDiskWrites)
}

@OptIn(ExperimentalContracts::class)
public inline fun <T> allowSlowCalls(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

    return runWith(block) { allowSlowCalls() }
}

@OptIn(ExperimentalContracts::class)
public inline fun <T> allowAllThreadPolicyViolations(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }

    return runWith(block, ::allowLaxThreadPolicy)
}

@OptIn(ExperimentalContracts::class)
@PublishedApi
internal inline fun <T> runWith(block: () -> T, policyProcessor: () -> ThreadPolicy): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        callsInPlace(policyProcessor, InvocationKind.EXACTLY_ONCE)
    }

    val oldPolicy: ThreadPolicy = policyProcessor()
    return try {
        block()
    } finally {
        StrictMode.setThreadPolicy(oldPolicy)
    }
}

@PublishedApi
internal fun allowSlowCalls(): ThreadPolicy = StrictMode.getThreadPolicy().also {
    StrictMode.setThreadPolicy(
        ThreadPolicy.Builder(it)
            .permitCustomSlowCalls()
            .build()
    )
}

public fun allowLaxThreadPolicy(): ThreadPolicy =
    StrictMode.getThreadPolicy().also { StrictMode.setThreadPolicy(ThreadPolicy.LAX) }
