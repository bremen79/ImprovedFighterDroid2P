package com.android.improvedfighterdroid2p;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android_serialport_api.SerialPort;

public class Uart2PService extends Service {
    private long clock = SystemClock.uptimeMillis();
    //private long previousClock = SystemClock.uptimeMillis();
    private SerialPort serialPort = null;
    private FileOutputStream fileOutputStreamUmidokey = null;
    private FileOutputStream fileOutputStreamSerial = null;
    private FileInputStream fileInputStreamSerial = null;

    public static byte[] bufferUmidokey = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static final byte[][] byte2char = {new byte[]{48, 48}, new byte[]{48, 49}, new byte[]{48, 50}, new byte[]{48, 51}, new byte[]{48, 52}, new byte[]{48, 53}, new byte[]{48, 54}, new byte[]{48, 55}, new byte[]{48, 56}, new byte[]{48, 57}, new byte[]{48, 65}, new byte[]{48, 66}, new byte[]{48, 67}, new byte[]{48, 68}, new byte[]{48, 69}, new byte[]{48, 70}, new byte[]{49, 48}, new byte[]{49, 49}, new byte[]{49, 50}, new byte[]{49, 51}, new byte[]{49, 52}, new byte[]{49, 53}, new byte[]{49, 54}, new byte[]{49, 55}, new byte[]{49, 56}, new byte[]{49, 57}, new byte[]{49, 65}, new byte[]{49, 66}, new byte[]{49, 67}, new byte[]{49, 68}, new byte[]{49, 69}, new byte[]{49, 70}, new byte[]{50, 48}, new byte[]{50, 49}, new byte[]{50, 50}, new byte[]{50, 51}, new byte[]{50, 52}, new byte[]{50, 53}, new byte[]{50, 54}, new byte[]{50, 55}, new byte[]{50, 56}, new byte[]{50, 57}, new byte[]{50, 65}, new byte[]{50, 66}, new byte[]{50, 67}, new byte[]{50, 68}, new byte[]{50, 69}, new byte[]{50, 70}, new byte[]{51, 48}, new byte[]{51, 49}, new byte[]{51, 50}, new byte[]{51, 51}, new byte[]{51, 52}, new byte[]{51, 53}, new byte[]{51, 54}, new byte[]{51, 55}, new byte[]{51, 56}, new byte[]{51, 57}, new byte[]{51, 65}, new byte[]{51, 66}, new byte[]{51, 67}, new byte[]{51, 68}, new byte[]{51, 69}, new byte[]{51, 70}, new byte[]{52, 48}, new byte[]{52, 49}, new byte[]{52, 50}, new byte[]{52, 51}, new byte[]{52, 52}, new byte[]{52, 53}, new byte[]{52, 54}, new byte[]{52, 55}, new byte[]{52, 56}, new byte[]{52, 57}, new byte[]{52, 65}, new byte[]{52, 66}, new byte[]{52, 67}, new byte[]{52, 68}, new byte[]{52, 69}, new byte[]{52, 70}, new byte[]{53, 48}, new byte[]{53, 49}, new byte[]{53, 50}, new byte[]{53, 51}, new byte[]{53, 52}, new byte[]{53, 53}, new byte[]{53, 54}, new byte[]{53, 55}, new byte[]{53, 56}, new byte[]{53, 57}, new byte[]{53, 65}, new byte[]{53, 66}, new byte[]{53, 67}, new byte[]{53, 68}, new byte[]{53, 69}, new byte[]{53, 70}, new byte[]{54, 48}, new byte[]{54, 49}, new byte[]{54, 50}, new byte[]{54, 51}, new byte[]{54, 52}, new byte[]{54, 53}, new byte[]{54, 54}, new byte[]{54, 55}, new byte[]{54, 56}, new byte[]{54, 57}, new byte[]{54, 65}, new byte[]{54, 66}, new byte[]{54, 67}, new byte[]{54, 68}, new byte[]{54, 69}, new byte[]{54, 70}, new byte[]{55, 48}, new byte[]{55, 49}, new byte[]{55, 50}, new byte[]{55, 51}, new byte[]{55, 52}, new byte[]{55, 53}, new byte[]{55, 54}, new byte[]{55, 55}, new byte[]{55, 56}, new byte[]{55, 57}, new byte[]{55, 65}, new byte[]{55, 66}, new byte[]{55, 67}, new byte[]{55, 68}, new byte[]{55, 69}, new byte[]{55, 70}, new byte[]{56, 48}, new byte[]{56, 49}, new byte[]{56, 50}, new byte[]{56, 51}, new byte[]{56, 52}, new byte[]{56, 53}, new byte[]{56, 54}, new byte[]{56, 55}, new byte[]{56, 56}, new byte[]{56, 57}, new byte[]{56, 65}, new byte[]{56, 66}, new byte[]{56, 67}, new byte[]{56, 68}, new byte[]{56, 69}, new byte[]{56, 70}, new byte[]{57, 48}, new byte[]{57, 49}, new byte[]{57, 50}, new byte[]{57, 51}, new byte[]{57, 52}, new byte[]{57, 53}, new byte[]{57, 54}, new byte[]{57, 55}, new byte[]{57, 56}, new byte[]{57, 57}, new byte[]{57, 65}, new byte[]{57, 66}, new byte[]{57, 67}, new byte[]{57, 68}, new byte[]{57, 69}, new byte[]{57, 70}, new byte[]{65, 48}, new byte[]{65, 49}, new byte[]{65, 50}, new byte[]{65, 51}, new byte[]{65, 52}, new byte[]{65, 53}, new byte[]{65, 54}, new byte[]{65, 55}, new byte[]{65, 56}, new byte[]{65, 57}, new byte[]{65, 65}, new byte[]{65, 66}, new byte[]{65, 67}, new byte[]{65, 68}, new byte[]{65, 69}, new byte[]{65, 70}, new byte[]{66, 48}, new byte[]{66, 49}, new byte[]{66, 50}, new byte[]{66, 51}, new byte[]{66, 52}, new byte[]{66, 53}, new byte[]{66, 54}, new byte[]{66, 55}, new byte[]{66, 56}, new byte[]{66, 57}, new byte[]{66, 65}, new byte[]{66, 66}, new byte[]{66, 67}, new byte[]{66, 68}, new byte[]{66, 69}, new byte[]{66, 70}, new byte[]{67, 48}, new byte[]{67, 49}, new byte[]{67, 50}, new byte[]{67, 51}, new byte[]{67, 52}, new byte[]{67, 53}, new byte[]{67, 54}, new byte[]{67, 55}, new byte[]{67, 56}, new byte[]{67, 57}, new byte[]{67, 65}, new byte[]{67, 66}, new byte[]{67, 67}, new byte[]{67, 68}, new byte[]{67, 69}, new byte[]{67, 70}, new byte[]{68, 48}, new byte[]{68, 49}, new byte[]{68, 50}, new byte[]{68, 51}, new byte[]{68, 52}, new byte[]{68, 53}, new byte[]{68, 54}, new byte[]{68, 55}, new byte[]{68, 56}, new byte[]{68, 57}, new byte[]{68, 65}, new byte[]{68, 66}, new byte[]{68, 67}, new byte[]{68, 68}, new byte[]{68, 69}, new byte[]{68, 70}, new byte[]{69, 48}, new byte[]{69, 49}, new byte[]{69, 50}, new byte[]{69, 51}, new byte[]{69, 52}, new byte[]{69, 53}, new byte[]{69, 54}, new byte[]{69, 55}, new byte[]{69, 56}, new byte[]{69, 57}, new byte[]{69, 65}, new byte[]{69, 66}, new byte[]{69, 67}, new byte[]{69, 68}, new byte[]{69, 69}, new byte[]{69, 70}, new byte[]{70, 48}, new byte[]{70, 49}, new byte[]{70, 50}, new byte[]{70, 51}, new byte[]{70, 52}, new byte[]{70, 53}, new byte[]{70, 54}, new byte[]{70, 55}, new byte[]{70, 56}, new byte[]{70, 57}, new byte[]{70, 65}, new byte[]{70, 66}, new byte[]{70, 67}, new byte[]{70, 68}, new byte[]{70, 69}, new byte[]{70, 70}};

    private int idDevice = -1;

    private Class<?> inputManagerClass;
    private Method getInstanceMethod;
    private Object inputManager;
    private Method injectInputEventMethod;

    private static final int UP = 8;
    private static final int DOWN = 2;
    private static final int LEFT = 4;
    private static final int RIGHT = 1;
    private static final int P1 = 16;
    private static final int P2 = 2;
    private static final int P3 = 4;
    private static final int P4 = 8;
    private static final int P5 = 1;
    private static final int P6 = 32;
    private static final int START = 32;

    private boolean holdUP = false;
    private boolean holdDOWN = false;
    private boolean holdLEFT = false;
    private boolean holdRIGHT = false;
    private boolean holdP1 = false;
    private boolean holdP2 = false;
    private boolean holdP3 = false;
    private boolean holdP4 = false;
    private boolean holdP5 = false;
    private boolean holdP6 = false;
    private boolean holdSTART = false;

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    
    public static final byte[] queryCommand = { (byte) 0xA6, (byte) 0x01, (byte) 0x00 };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();

        // The injectInputEvent cannot be accessed normally, so we use Reflection to do it
        // see https://www.pocketmagic.net/injecting-events-programatically-on-android/
        try {
            // Get the InputManager class
            inputManagerClass = Class.forName("android.hardware.input.InputManager");
            // Get the getInstance method of InputManager
            getInstanceMethod = inputManagerClass.getMethod("getInstance");
            // Invoke the getInstance method to get an instance of InputManager
            inputManager = getInstanceMethod.invoke(null);
            // Get the injectInputEvent method
            injectInputEventMethod = inputManagerClass.getMethod("injectInputEvent", InputEvent.class, int.class);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        Log.d("Bremen79", "Service created");
        startMyOwnForeground();
    }


    // https://androidwave.com/foreground-service-android-example/
    private void startMyOwnForeground() {
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ImprovedFighterDroid2P "+BuildConfig.VERSION_NAME+" is running in background")
                .setPriority(1)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_NONE
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private int findInputDeviceIdByName(String deviceName) {
        InputManager inputManager = (InputManager) getSystemService(Context.INPUT_SERVICE);

        int[] deviceIds = inputManager.getInputDeviceIds();

        for (int deviceId : deviceIds) {
            String name = inputManager.getInputDevice(deviceId).getName();

            if (name.equals(deviceName)) {
                return deviceId;
            }
        }

        // If the device with the given name is not found, return -1 or handle as needed
        return -1;
    }

    private void readSerialPort(){
        Log.d("Bremen79", "Trying to open serial port");
        try {
            serialPort = new SerialPort(new File("/dev/ttyS1"));
            Log.d("Bremen79", "Serial port opened");
            Log.d("Bremen79", "Trying to open umidokey device");
            fileOutputStreamUmidokey = new FileOutputStream("/dev/umidokey");
            Log.d("Bremen79", "umidokey device opened");
        } catch (IOException unused) {
        } catch (SecurityException e2) {
            e2.printStackTrace();
        }

        fileOutputStreamSerial = serialPort.mFileOutputStream;
        fileInputStreamSerial = serialPort.mFileInputStream;

        idDevice = findInputDeviceIdByName(BuildConfig.DEVICE_NAME);
        Log.d("Bremen79", String.valueOf(idDevice));

        try {
            while (true) {
                clock = SystemClock.uptimeMillis();
                fileOutputStreamSerial.write(queryCommand);
                if (fileInputStreamSerial != null) {
                    byte[] bArr = new byte[128];
                    int read = fileInputStreamSerial.available() != 0 ? fileInputStreamSerial.read(bArr) : 0;
                    if (read > 0) {
                        //Log.d("Bremen79", String.format("Read %d bytes from serial port", read));
                        writeUmidokey(bArr);
                    }
                }
                // We are aiming at polling the state of the joystick every 16ms, that is at 62.5Hz
                // So, if the processing took more than 16ms, we skip the sleep.
                long timeSleep = 16L-(SystemClock.uptimeMillis() - clock);
                //Log.d("Bremen79", String.format("Sleep time: %d", timeSleep));
                if (timeSleep>0)
                    Thread.sleep(timeSleep);
            }
        } catch (IOException | InterruptedException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public final void writeUmidokey(byte[] serialBytes) {
        // the first 2 bytes should be 'A710'
        if (serialBytes[0] != (byte) 0xA7 || serialBytes[1] != (byte) 0x10) return;

        bufferUmidokey[0]  = byte2char[serialBytes[4] & 255][0];
        bufferUmidokey[1]  = byte2char[serialBytes[4] & 255][1];

        bufferUmidokey[2]  = byte2char[serialBytes[5] & 255][0];
        bufferUmidokey[3]  = byte2char[serialBytes[5] & 255][1];

        bufferUmidokey[4]  = byte2char[serialBytes[6] & 255][0];
        bufferUmidokey[5]  = byte2char[serialBytes[6] & 255][1];

        bufferUmidokey[6]  = byte2char[serialBytes[8] & 255][0];
        bufferUmidokey[7]  = byte2char[serialBytes[8] & 255][1];

        bufferUmidokey[8]  = byte2char[serialBytes[9] & 255][0];
        bufferUmidokey[9]  = byte2char[serialBytes[9] & 255][1];

        bufferUmidokey[10] = byte2char[serialBytes[10] & 255][0];
        bufferUmidokey[11] = byte2char[serialBytes[10] & 255][1];

        bufferUmidokey[12] = byte2char[serialBytes[2] & 255][0];
        bufferUmidokey[13] = byte2char[serialBytes[2] & 255][1];

        try {
            fileOutputStreamUmidokey.write(bufferUmidokey, 0, 14);
            emulateP2(serialBytes[8], serialBytes[9]);
        } catch (Exception e2) {
            e2.toString();
        }
    }


    private void emulateP2(byte i, byte i2) {
        //Log.d("Bremen79", String.format("%d %d %d %d %d %d %d %d %d %d", bArr[0], bArr[1], bArr[2], bArr[3], bArr[4],bArr[5], bArr[6], bArr[7], bArr[8], bArr[9]));
        //Log.d("Bremen79", String.format("%d %d", i, i2));
        //previousClock=clock;
        if ((i & UP) != 0) {
            if (!holdUP) {
                pressKeyEvent(BuildConfig.UP_KEY);
                holdUP = true;
            }
        } else {
            if (holdUP) {
                releaseKeyEvent(BuildConfig.UP_KEY);
                holdUP = false;
            }
            if ((i & DOWN) != 0) {
                if (!holdDOWN) {
                    pressKeyEvent(BuildConfig.DOWN_KEY);
                    holdDOWN = true;
                }
            } else if (holdDOWN) {
                releaseKeyEvent(BuildConfig.DOWN_KEY);
                holdDOWN = false;
            }
        }
        if ((i & LEFT) != 0) {
            if (!holdLEFT) {
                pressKeyEvent(BuildConfig.LEFT_KEY);
                holdLEFT = true;
            }
        } else {
            if (holdLEFT) {
                releaseKeyEvent(BuildConfig.LEFT_KEY);
                holdLEFT = false;
            }
            if ((i & RIGHT) != 0) {
                if (!holdRIGHT) {
                    pressKeyEvent(BuildConfig.RIGHT_KEY);
                    holdRIGHT = true;
                }
            } else if (holdRIGHT) {
                releaseKeyEvent(BuildConfig.RIGHT_KEY);
                holdRIGHT = false;
            }
        }
        if ((i2 & P1) != 0) {
            if (!holdP1) {
                pressKeyEvent(BuildConfig.P1_KEY);
                holdP1 = true;
            }
        } else if (holdP1) {
            releaseKeyEvent(BuildConfig.P1_KEY);
            holdP1 = false;
        }
        if ((i2 & P2) != 0) {
            if (!holdP2) {
                pressKeyEvent(BuildConfig.P2_KEY);
                holdP2 = true;
            }
        } else if (holdP2) {
            releaseKeyEvent(BuildConfig.P2_KEY);
            holdP2 = false;
        }
        if ((i2 & P3) != 0) {
            if (!holdP3) {
                pressKeyEvent(BuildConfig.P3_KEY);
                holdP3 = true;
            }
        } else if (holdP3) {
            releaseKeyEvent(BuildConfig.P3_KEY);
            holdP3 = false;
        }
        if ((i2 & P4) != 0) {
            if (!holdP4) {
                pressKeyEvent(BuildConfig.P4_KEY);
                holdP4 = true;
            }
        } else if (holdP4) {
            releaseKeyEvent(BuildConfig.P4_KEY);
            holdP4 = false;
        }
        if ((i2 & P5) != 0) {
            if (!holdP5) {
                pressKeyEvent(BuildConfig.P5_KEY);
                holdP5 = true;
            }
        } else if (holdP5) {
            releaseKeyEvent(BuildConfig.P5_KEY);
            holdP5 = false;
        }
        if ((i2 & P6) != 0) {
            if (!holdP6) {
                pressKeyEvent(BuildConfig.P6_KEY);
                holdP6 = true;
            }
        } else if (holdP6) {
            releaseKeyEvent(BuildConfig.P6_KEY);
            holdP6 = false;
        }
        if ((i & START) != 0) {
            if (!holdSTART) {
                pressKeyEvent(BuildConfig.START_KEY);
                holdSTART = true;
            }
        } else if (holdSTART) {
            releaseKeyEvent(BuildConfig.START_KEY);
            holdSTART = false;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("Bremen79", "Start command received");
        new Thread(this::readSerialPort).start();
        return START_STICKY;
    }

    @Override // android.app.Service
    public final void onDestroy() {
        Log.d("Bremen79", "Service onDestroy");
        try {
            if (fileInputStreamSerial != null) {
                fileInputStreamSerial.close();
                fileInputStreamSerial = null;
                Log.d("Bremen79", "Closed input serial");
            }
            if (fileOutputStreamSerial!= null) {
                fileOutputStreamSerial.close();
                fileOutputStreamSerial = null;
                Log.d("Bremen79", "Closed output serial");
            }
            if (fileOutputStreamUmidokey!= null) {
                fileOutputStreamUmidokey.close();
                fileOutputStreamUmidokey = null;
                Log.d("Bremen79", "Closed output umidokey");
            }
        } catch (Exception ignored) {
        }
        stopForeground(true);
        super.onDestroy();
    }

    // Should I implement an on task removed method?
    // see https://robertohuertas.com/2019/06/29/android_foreground_services/

    private void pressKeyEvent(int keyCode) {
        try {
            injectInputEventMethod.invoke(inputManager, new KeyEvent(clock, clock, KeyEvent.ACTION_DOWN, keyCode, 0, 0,
                            idDevice, 0, 0, 1),
                    0);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //Log.d("Bremen79", String.format("Keypress down sent, response time: %d ms", SystemClock.uptimeMillis() - clock));
    }

    private void releaseKeyEvent(int keyCode) {
        try {
            injectInputEventMethod.invoke(inputManager,new KeyEvent(clock, clock, KeyEvent.ACTION_UP, keyCode, 0, 0,
                            idDevice, 0, 0, 1),
                    0);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //Log.d("Bremen79", String.format("Keypress up sent, response time: %d ms", SystemClock.uptimeMillis() - clock));
    }
}