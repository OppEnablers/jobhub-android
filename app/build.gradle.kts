import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { stream ->
        localProperties.load(stream)
    }
}

android {
    namespace = "com.oppenablers.jobhub"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.oppenablers.jobhub"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "COGNITO_POOL_ID", "\"${localProperties["COGNITO_POOL_ID"]}\"")
        buildConfigField("String", "AWS_REGION", "\"${localProperties["AWS_REGION"]}\"")
        buildConfigField("String", "BUCKET_NAME", "\"${localProperties["BUCKET_NAME"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(project(":mariatoggle"))
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(project(":cardstackview"))
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.core.splashscreen)
    implementation(libs.preference)
    implementation(libs.firebase.bom)
    implementation("com.amazonaws:aws-android-sdk-core:2.81.0")
    implementation("com.amazonaws:aws-android-sdk-s3:2.81.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}