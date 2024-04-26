-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

-dontnote okhttp3.**, okio.**, retrofit2.**
-dontwarn retrofit2.**
-keep class okhttp3.** { *; }
-keepattributes Signature
-dontwarn okhttp3.**
-keep class retrofit2.** { *; }
-dontwarn com.google.errorprone.annotations.**
-keep class com.google.errorprone.annotations.** { *; }
-keep class * implements java.io.Serializable { *; }
-keep class com.br.b2b.data.model.** { *; }
-keep class com.br.b2b.data.remote.** { *; }
-keep class com.br.b2b.data.domain.repository.** { *; }
-keep class com.br.b2b.data.domain.viewmodels.** { *; }
-keep class com.br.b2b.data.R.** { *; }
-keep class kotlin.collections.* { *; }
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type

# Remover logs de depuração
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# Remover métodos de logging do Kotlin
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static *** checkParameterIsNotNull(...);
}

-keep class com.br.b2b.data.** { *; }

-keep class com.br.b2b.domain.** { *; }

-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Remover referências não utilizadas
-whyareyoukeeping class *
-whyareyoukeeping interface *
-whyareyoukeeping enum *


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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Adicione as regras de manutenção geradas pelo R8