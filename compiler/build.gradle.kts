plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_1_8
//    targetCompatibility = JavaVersion.VERSION_1_8
//}
tasks.withType<JavaCompile> {
    // 将 compileJava 任务的 JVM 目标设置为 Java 17
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}


//tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).configureEach {
//    kotlinOptions {
//        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
//    }
//}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.23-1.0.19")
    implementation("com.google.devtools.ksp:symbol-processing:1.7.20-1.0.8")
    implementation("com.squareup:kotlinpoet:1.11.0")
    implementation("com.squareup:kotlinpoet-ksp:1.11.0")
    implementation("com.squareup:kotlinpoet-metadata:1.11.0")
    implementation(project(mapOf("path" to ":annotations")))

}