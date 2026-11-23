plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.example.listanddetails.core.data"
    compileSdk = 37

    defaultConfig {
        minSdk = 29
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        getByName("main") {
            java.srcDirs(
                file("build/generated/source/proto/debug/java"),
                file("build/generated/source/proto/debug/kotlin")
            )
        }
    }
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)
    implementation(libs.protobuf.javalite)
    implementation(libs.protobuf.kotlin.lite)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    testImplementation(libs.junit)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.29.3"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
