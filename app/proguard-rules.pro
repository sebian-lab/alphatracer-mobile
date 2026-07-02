# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Keep line number and source file details in stack traces for crash reporting/debugging
-keepattributes SourceFile,LineNumberTable

# Rename the source file to obfuscate the original source name
-renamesourcefileattribute SourceFile

# Gson rules to preserve SerializedName annotations and serialized fields
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep the data models package intact to allow proper Gson serialization/deserialization
-keep class com.main.alphatracer.model.** { *; }

# Retrofit rules
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes RuntimeVisibleDeclarations,RuntimeVisibleParameterDeclarations
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}