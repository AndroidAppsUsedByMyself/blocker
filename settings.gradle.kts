/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "Blocker"
include(":app")
include(":core:common")
include(":core:data")
include(":core:data-test")
include(":core:model")
include(":core:domain")
include(":core:testing")
include(":core:network")
include(":core:database")
include(":core:component-controller")
include(":core:ifw-api")
include(":core:datastore")
include(":core:datastore-test")
include(":core:ui")
include(":core:designsystem")
include(":lint")
include(":feature:applist")
include(":feature:appdetail")
include(":feature:search")
include(":feature:settings")