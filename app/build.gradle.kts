import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
//    id ("com.android.application")

}

android {
    namespace = "tech.ayodele.gravity"
    compileSdk = 34

    defaultConfig {
        applicationId = "tech.ayodele.gravity"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.location)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
// Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))



    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

//    for bottom nav
    implementation (libs.material)
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("com.google.android.material:material:1.4.0")


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

//    implementation ("com.sun.mail:android-mail:1.6.0")
//    implementation ("com.sun.mail:android-activation:1.6.0")
//send mail
    implementation ("com.github.1902shubh:SendMail:1.0.0")

    implementation ("androidx.work:work-runtime:2.9.0")

//    chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //chart
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")

    implementation  ("androidx.activity:activity-ktx:1.9.0")

    implementation ("com.google.android.gms:play-services-location:18.0.0") // Google Play Services
    implementation ("com.google.android.libraries.places:places:2.5.0") // Google Places

}