# GmailAccessTokenRetriever
In order to install GmailAccessTokenRetriever library in your project do the following:

Add "maven { url "https://jitpack.io" }" to project bulid.gradle in allprojects -> repositories block.
Like this:
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```


Add "compile 'com.github.sentsent:GmailAccessTokenRetriever:1.0.2'" to app build.gradle in dependencies block.
Like this:
```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:24.2.1'
    compile 'com.github.sentsent:GmailAccessTokenRetriever:1.0.2'
    testCompile 'junit:junit:4.12'
}
```

and dataBinding in android block, like this:
```
android {
    compileSdkVersion 24
    buildToolsVersion "24.0.3"

    dataBinding {
        enabled = true
    }
    .
    .
    .
}
```

In your project's main activity call the code:
```
Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
intent.setComponent(new ComponentName("pl.sentia.gmailaccesstokenretriever", "pl.sentia.gmailaccesstokenretriever.MainActivity"));
startActivity(intent);
```
