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

import android.os.Build.VERSION_CODES.P
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.annotation.RequiresApi
import org.bubenheimer.android.sdkAtLeast
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public fun Class<*>.instanceLimit(limit: Int = 1): Unit = StrictMode.setVmPolicy(
    VmPolicy.Builder(StrictMode.getVmPolicy())
        .setClassInstanceLimit(this, limit)
        .build()
)

@OptIn(ExperimentalContracts::class)
public inline fun <T> allowNonSdkApiUse(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }

    return if (sdkAtLeast(P)) {
        runWith(block) { allowNonSdkApiUse() }
    } else block()
}

@OptIn(ExperimentalContracts::class)
@PublishedApi
internal inline fun <T> runWith(block: () -> T, policyProcessor: () -> VmPolicy): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        callsInPlace(policyProcessor, InvocationKind.EXACTLY_ONCE)
    }

    val oldPolicy: VmPolicy = policyProcessor()
    return try {
        block()
    } finally {
        StrictMode.setVmPolicy(oldPolicy)
    }
}

@PublishedApi
@RequiresApi(P)
internal fun allowNonSdkApiUse(): VmPolicy = StrictMode.getVmPolicy().also {
    StrictMode.setVmPolicy(
        VmPolicy.Builder(it)
            .permitNonSdkApiUsage()
            .build()
    )
}
