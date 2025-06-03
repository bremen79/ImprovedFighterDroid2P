# ImprovedFighterDroid2P

This Android app does three things:

- It allows to use the second joystick (and its buttons) on the Arcade1up Yoga Flame cabinet.

- It reduces the delay of both joysticks.

- It maps the long press (3 seconds) of the Live button to the button HOME.

It works by *replacing* the native driver and associating the second joystick and its buttons to the keys of a virtual keyboard.

# Why a New App?

This is an improved version of my app [FighterDroid2P app](https://github.com/bremen79/FighterDroid2P): Instead of adding an app to query the state of the joystick, I directly rewrote the native driver so that it also encodes the second joystick. This has a couple of advantages. First, in my other app the native driver and my app were both reading from the same read-once input, causing possible problems. So, now only one read of the serial port is used for both the native driver and the second joystick. Second, the native driver is poorly coded and my implementation reduces the delays even for player 1. Finally, it maps the long press of the Live button to HOME, so you do not need to install yet another app to do it.

# Disclaimer

This software is provided "as-is," without any express or implied warranty. The author(s) of this software shall not be held liable for any direct, indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this software, even if advised of the possibility of such damage.

# Installation and Uninstallation

First of all, this app is not compatible with the input lag fix by [The Code Always Wins](https://www.youtube.com/c/thecodealwayswins). If you have it, first you must disinstall it. This app includes the input lag fix, so you won't need other apps for that.

After installing the app, run it once. It will prompt you to *Install* or *Uninstall*.

*Install* will disable the native drivers (but not remove them!), and it will enable the new driver. The new driver will automatically starts itself after each boot.

*Uninstall* will re-enable the native drivers and stop the app. Then, you must disinstall the app and reboot the cabinet, and the native driver will work as before.

# History

1.0.0: First public release  
1.1.0: Removed the need to use adb to install and unistall, added mapping of long press Live button to HOME

# Acknowledgments
This app is based on the same idea used by [The Code Always Wins](https://www.youtube.com/c/thecodealwayswins) to replace the native driver with a faster one. Here, I use the same approach to poll the ports, but I use the same ``adaptive sleep'' I used in my [FighterDroid2P app](https://github.com/bremen79/FighterDroid2P) to be sure that the app runs every 16ms.

The serial port code is from the Team Encoder code at https://github.com/Team-Encoder/A1AndroidControlFix.

