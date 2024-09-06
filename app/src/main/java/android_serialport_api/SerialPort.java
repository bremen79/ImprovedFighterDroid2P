package android_serialport_api;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";
    public FileDescriptor mFd;
    public FileInputStream mFileInputStream;
    public FileOutputStream mFileOutputStream;

    public SerialPort(File file) throws SecurityException, IOException {
        mFd = open(file.getAbsolutePath(), 115200, 0);
        FileDescriptor fileDescriptor = mFd;
        if (fileDescriptor == null) {
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(fileDescriptor);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    public native void close();
    private native static FileDescriptor open(String path, int baudrate, int flags);
    static {
        System.loadLibrary("serial_port");
    }
}