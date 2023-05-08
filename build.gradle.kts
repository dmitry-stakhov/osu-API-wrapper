plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.0.1").apply(false)
    id("com.android.library").version("8.0.1").apply(false)
    kotlin("android").version("1.8.21").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs + "-Xcontext-receivers"
}
