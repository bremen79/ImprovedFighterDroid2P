# ImprovedFighterDroid2P

This Android app allows to use the second joystick (and its buttons) on the Arcade1up Yoga Flame cabinet.
It works by *replacing* the native driver and associating the second joystick and its buttons to the keys of a virtual keyboard.

This is an improved version of my app FighterDroid2P: Instead of adding an app to query the state of the joystick, I directly rewrote the native driver so that they also encode the second joystick. This has a technical advantage: before the native driver and my app were both reading from the same read-once input, causing possible problems. Now instead only one read of the serial port is used for both the native driver and the second joystick. Overall, this should be faster and safer.

Note that this app is intended for expert users: You must use adb to install it.

# Disclaimer

This software is provided "as-is," without any express or implied warranty. The author(s) of this software shall not be held liable for any direct, indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this software, even if advised of the possibility of such damage.

# Installation

After installing the app, you must use adb and type the following commands:

```
adb shell pm disable-user --user 0 com.fjtech.ComAssistant
adb pm disable-user --user 0 com.bjw.ComAssistant
```
The first two commands will disable the native drivers (but not remove them!), then just start the app. Notice that the joysticks will stop working when you disable the native drivers and they will work again once you start ImprovedFighterDroid2P.

The new driver will automatically starts itself after each boot.

# Acknowledgments

This app is based on the same idea used by The Code Always Wins (https://www.youtube.com/c/thecodealwayswins) to replace the native driver with a faster one. Here, I use the same approach to poll the ports, but I use the same ``adaptive sleep'' I used in my FighterDroid2P app to be sure that the app runs every 17ms.

The serial port code is from the Team Encoder code at https://github.com/Team-Encoder/A1AndroidControlFix.

