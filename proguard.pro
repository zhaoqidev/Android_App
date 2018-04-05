-injars D:\jar\LeCloudPlayer.jar
-injars D:\jar\LeCloudSDK.jar
-injars D:\jar\LeCloudUI.jar
-outjars D:\jar\LeCloudSDK_V3.0.jar

-libraryjars 'E:\dev_tools\adt-bundle-windows-x86_64-20140321\adt-bundle-windows-x86_64-20140321\sdk\platforms\android-19\android.jar'
-libraryjars libs\cde-sdk-0.9.53.jar
-libraryjars libs\gson-2.1.jar
-libraryjars libs\javabase64-1.3.1.jar

-dontoptimize
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose
-keepattributes SourceFile,LineNumberTable
-ignorewarnings


-keep public class * extends android.app.Activity
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.app.Service
-keep class * extends android.content.ServiceConnection

-keep class com.guagua.player.** { *; }

-keep public class com.letvcloud.sdk.play.control.PlayController
-keep public class com.letvcloud.sdk.play.listener.ControlListener
-keep public class com.letvcloud.sdk.base.util.Logger
-keep public class com.letvcloud.sdk.play.entity.Config
-keep public class com.letvcloud.sdk.LeCloud

-keep public class com.lecloud.skin.live.LivePlayCenter
-keep public class com.letvcloud.sdk.log.FetchLogLoader
-keep public class com.letvcloud.sdk.play.util.LogUtils
-keep public class com.lecloud.skin.PlayerStateCallback
-keep public class com.lecloud.skin.vod.VODPlayCenter
-keep public class com.lecloud.skin.BasePlayCenter

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}


-keepclassmembers class com.letvcloud.sdk.play.control.PlayController {
    public <fields>;
    protected <fields>;
    public <methods>;
    protected <methods>;
}

-keepclassmembers class com.letvcloud.sdk.play.listener.ControlListener {
    public <methods>;
}

-keepclassmembers class com.letvcloud.sdk.base.util.Logger {
    public <fields>;
    protected <fields>;
    public <methods>;
    protected <methods>;
}

-keepclassmembers class com.letvcloud.sdk.play.entity.Config {
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.letvcloud.sdk.LeCloud {
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.lecloud.skin.BasePlayCenter {
    public <fields>;
    protected <fields>;
    public <methods>;
    protected <methods>;
}

-keepclassmembers class com.lecloud.skin.vod.VODPlayCenter {
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.lecloud.skin.PlayerStateCallback {
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.letvcloud.sdk.play.util.LogUtils {
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.letvcloud.sdk.log.FetchLogLoader {
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.lecloud.skin.live.LivePlayCenter {
    public <fields>;
    public <methods>;
}

# We should not proguard any data model class used gson
-keep class com.letvcloud.sdk.bi.entity.** {
    <fields>;
    <methods>;
}

-keep class com.letvcloud.sdk.play.entity.** {
    <fields>;
    <methods>;
}

-keep class com.letvcloud.sdk.base.entity.** {
    <fields>;
    <methods>;
}

# Gson specific classes 
-keep class sun.misc.Unsafe {
    <fields>;
    <methods>;
}

# -keep class com.google.gson.stream.** { *; } 
-keep class com.google.gson.** {*;} 
-keepattributes Signature


-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
