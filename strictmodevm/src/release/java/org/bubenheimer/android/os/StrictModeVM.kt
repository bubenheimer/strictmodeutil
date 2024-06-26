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

import android.os.StrictMode.VmPolicy
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Suppress("UnusedReceiverParameter")
public fun Class<*>.instanceLimit(@Suppress("UNUSED_PARAMETER") limit: Int = 1) {}

@OptIn(ExperimentalContracts::class)
public inline fun <T> allowNonSdkApiUse(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

    return block()
}

@OptIn(ExperimentalContracts::class)
public inline fun <T> allowAllVmPolicyViolations(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

    return block()
}

public fun allowLaxVmPolicy(): Nothing = error("")
