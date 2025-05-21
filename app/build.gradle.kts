plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "www.minet.staffwellness"
    compileSdk = 35

    defaultConfig {
        applicationId = "www.minet.staffwellness"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    //progress bar
    implementation (libs.circularprogressbar)

    //line graph
    implementation (libs.mpandroidchart)

    //lottie animations
    implementation (libs.lottie)

    //OTP Pin View Design
    implementation ("io.github.chaosleung:pinview:1.4.4")

}