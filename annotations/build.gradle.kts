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