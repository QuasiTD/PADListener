PADListener
==========

TL;DR description
----------
PADListener is an Android application aimed at synchronizing your PAD monster box to [PADherder](https://www.padherder.com).
It's similar to [padherder_sync](https://github.com/madcowfred/padherder-sync) or [padproxy.py](https://bitbucket.org/mywaifu/padproxy/), but doesn't require a computer.

You can watch a [demonstration on youtube](http://youtu.be/tKrHvC7k03Y).

To sum up, this application starts a local proxy on your Android device and captures the data sent by GungHo servers to PAD. It then syncs your monster box using PADherder's APIs.

Thanks
----------
I'd like to thank [Freddie](https://github.com/madcowfred) for his great work on [PADherder](https://www.padherder.com) and its API.
I use the following libraries :
* [SandroProxyLib](http://code.google.com/p/sandrop/) : local proxy on an Android device
* [ckChangeLog](https://github.com/cketti/ckChangeLog) : changelog library
* [libsuperuser](https://github.com/Chainfire/libsuperuser)
* [Picasso](https://github.com/square/picasso)
* [Showcaseview](https://github.com/amlcurran/ShowcaseView)
* [Timber](https://github.com/JakeWharton/timber)
* [Butter Knife](https://github.com/JakeWharton/butterknife)


Warning
----------
First of all, intercepting PAD requests to GungHo servers could potentially get you banned. As this application does not tamper with the data, I don't think GungHo could be aware of the capture. But you never know ...

Depending on the chosen settings, this application will modify your WiFi settings. If something goes wrong, the proxy can crash. You would have to manually edit your WiFi settings to unset the proxy.

And finally, this application could mess up a sync and corrupt your PADherder's account. I suggest you back it up first.


Longer description
----------
This application can :
* store monster information (stats and images) and expose it through ContentProviders for other apps
* refresh the monster information to keep it up-to-date without having to update the app itself
* start a local proxy to capture the monster box from PAD. You have to start the listener from PADListener, and then start PAD. When you pass the title screen, you should see a Toast at the bottom of your screen saying "PAD data captured for [your account name]"
* list the captured monster box
* sync the captured monster box (materials and monsters) with a PADherder account



Building the application
----------
### Prerequisites
* Java SDK
* [Gradle](http://gradle.org/)
  * Follow instructions on their [website](https://docs.gradle.org/current/userguide/installation.html)
* [Android SDK](http://developer.android.com/sdk/index.html) with the following packages:
  * Tools
    * Android SDK Tools
    * Android SDK Platform-tools
    * Android SDK Build-tools
  * Latest SDK Platform
  * Extras
    * Android Support Repository

### Notes
If you want to build the application yourself, you can use gradle.

To set up the build, you need to set up the android sdk home with one of the following methods : 
* create a file ROOT/local.properties containing a single key sdk.dir
* set up an ANDROID_HOME environment variable

If you want a properly sign apk, you should create a properties file containing your keystore information (cf the example signing.properties.example).
You need to reference this properties file with one of the following methods : 
* create a ROOT/gradle.properties containing a single key signingProperties.path
* set up an SIGNING_PROPERTIES_PATH environment variable

To build a debug APK : 
* check that gradle works by issuing a "gradle" command in ROOT
* build a debug apk with "gradle build" in ROOT
* fetch the APK from ROOT/PADListener/build/apk/

