plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.devtools.ksp")
}
ksp {
    arg("AROUTER_MODULE_NAME", project.getName())
}
android.sourceSets.all { java.srcDir("build/generated/ksp/${name}/kotlin/") }

android {
    namespace = "com.ruma.routeksp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ruma.routeksp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

//    sourceSets {
//        named("main") {
//            java.srcDir("build/generated/ksp/main/kotlin/")
//        }
//        // 如果你有其他的 sourceSets，可以继续添加类似的配置
//    }
}

tasks.withType<JavaCompile> {
    // 将 compileJava 任务的 JVM 目标设置为 Java 17
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(project(mapOf("path" to ":annotations")))
    ksp(project(mapOf("path" to ":compiler")))

}