#1 未混淆，未压缩
#2 混淆，未压缩
#3 未混淆， 压缩(不通过)
#4 混淆压缩
#v4、v7混淆设置
#=========== begin android defaul config =================
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#android 优化迭代次数 5次 有风险
-optimizationpasses 5
#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
#指定不对处理后的类文件进行预校验，android平台没必要进行预校验
-dontpreverify
#混淆时不会产生大小写混合的类名
-dontusemixedcaseclassnames
#不忽略非公共库类
-dontskipnonpubliclibraryclasses

#Gneral Option
#指定处理期间打印更多相关信息
-verbose
-ignorewarnings
-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
#Keep Option
#保留原生的方法
-keepclasseswithmembernames class * {
    native <methods>;
}
#保留View的setter和getter方法让view的动画能正常执行
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
# 保留activity中定义的用于响应xml中view点击时间的方法
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保留枚举成员，因为系统需要处理枚举的固定方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保留android序列化类成员
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}
#不混淆R文件
-keepclassmembers class **.R$* {
    public static <fields>;
}
#android.support.**里面的警告忽略
-dontwarn android.support.**
#=========== end android defaul config =================
#防止因使用泛型导致的类型转换错误发生
-keepattributes Signature

#保留Serializable 类及成员
-keep class * implements java.io.Serializable {*;}
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {*;}
-keep class android.support.v4.** {*;}
-keep public class * extends android.support.v4.**
#忽略警告
-dontwarn android.support.**

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#ShareSDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-dontwarn cn.sharesdk.**


#AsyncHttpClient
-keep class com.lcshidai.lc.http.** {*;}
-keep class com.loopj.android.http.**
-keep class com.lcshidai.lc.model.** {*;}


-ignorewarnings
#注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }

-keepattributes Signature

#talking data
-keep class com.apptalkingdata.** {*;}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}

#photoView
-keep class com.bm.library.** {*;}

##widget 包内控件保持
-keep class com.lcshidai.lc.widget.** {*;}

#faster json
-keep class com.fasterxml.jackson.**{*;}
-dontwarn  com.fasterxml.jackson.**

#pdf viewer
-keep class com.github.barteksc.pdfviewer.** {*;}

#charting
-keep class com.github.mikephil.charting.** {*;}


#tendcloud
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class ** {
     public void *(***);
 }


#百度推送
-keep class com.baidu.**{*;}
-dontwarn com.baidu.**

#个推推送
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }

#http
-keep class com.loopj.android.http.** {*;}

# 资源文件
-keep class **.R$* {*;}
-keep class **.R{*;}
#Gson
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}

#udesk
-keep class udesk.** {*;}
-keep class cn.udesk.**{*; }
#七牛
-keep class com.qiniu.android.dns.** {*; }
-keep class okhttp3.** {*;}
-keep class okio.** {*;}
-keep class com.qiniu.android.** {*;}
#smack
-keep class org.jxmpp.** {*;}
-keep class de.measite.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.xmlpull.** {*;}
#Android M 权限
-keep class rx.** {*;}
-keep class com.tbruyelle.rxpermissions.** {*;}

#其它
-keep class com.tencent.bugly.** {*; }
-keep class com.nostra13.universalimageloader.** {*;}
-keep class de.hdodenhof.circleimageview.** {*;}
#MultiDex
-keep class android.support.multidex.MultiDex.** {*; }
#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
