package methode.methodeelectronics_newdb;


import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.mtp.MtpConstants;
import android.mtp.MtpDevice;
import android.mtp.MtpObjectInfo;
import android.util.Log;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyBackgroundService extends IntentService {
    public int i=0;
    public int[] StorageIds;
    public int associationtype;
    public String m;
    public int count = 0;
    public int flag =0;
    public int flag1 = 0;
    public int count_mp3 =0;
    public int count_video =0;
    public int count_photo =0;

    public String searchquery;
    public ArrayList<String> mp3songnames = new ArrayList<String>();
    public ArrayList<String> videofilenames = new ArrayList<String>();
    public ArrayList<String> photofilenames = new ArrayList<String>();
    private static final String ACTION_USB_PERMISSION = "com.ashishgupta.media_feb24.USB_PERMISSION";
    private MyDatabaseHandler db = new MyDatabaseHandler(this);
    TabHost th;

    public MyBackgroundService() {
        super("MyBackgroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            Log.d("callingfn", "Reached inside function");
            while (deviceIterator.hasNext()) {
                Log.d("callingfn","Reached inside device iterator");
                UsbDevice device = deviceIterator.next();
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                registerReceiver(mUsbReceiver, filter);
                Log.d("callingfn", "Sending intent for USB permissions");
                manager.requestPermission(device, mPermissionIntent);
                Log.d("devicecomm", "Inside function devicecomm");
                //  Log.d("devicecomm", "Manufacturer name is : " + device.getManufacturerName());
                UsbDeviceConnection usbDeviceConnection = manager.openDevice(device);
                MtpDevice mtpDevice = new MtpDevice(device);
                if (!mtpDevice.open(usbDeviceConnection)) {
                    Log.d("devicecomm", "Mtp device could not be opened");
                } else {
                    Log.d("devicecomm", "Successfully opened mtp device");
                    StorageIds = mtpDevice.getStorageIds();
                    if (StorageIds != null) {
                        Log.d("devicecomm", "Storage ids is : " + StorageIds);
                        for (int storage : StorageIds)
                        {

                            int[] objecthandles = mtpDevice.getObjectHandles(storage, 0,0);
                            Log.d("scan","Inside scan object function");
                            if (objecthandles == null) {
                                Log.d("scan","No object handles found");
                            } else {
                                Log.d("scan","object handles are : "+objecthandles);
                                Log.d("scan", "no of object handles = " + objecthandles.length);
                                for (int objecthandle : objecthandles) {
                                    Log.d("scan", "Looping from object handles");
                                    MtpObjectInfo mtpobjectinfo = mtpDevice.getObjectInfo(objecthandle);
                                    if (mtpobjectinfo == null) {
                                        Log.d("scan", "Mtpdevice object info is null");
                                        continue;
                                    }
                                    associationtype = mtpobjectinfo.getFormat();
                                    if (associationtype == MtpConstants.ASSOCIATION_TYPE_GENERIC_FOLDER) {
                                        flag++;
                                    } else if (associationtype == MtpConstants.FORMAT_ASSOCIATION){
                                        flag1++;
                                        Log.d("scan", "File is a folder");}
                                    else if (associationtype == MtpConstants.FORMAT_MP3) {
                                        db.addmp3file(new MP3(mtpobjectinfo.getName()));
                                        Log.d("scan", "Mp3 file added : " + mtpobjectinfo.getName());
                                        count_mp3++;
                                    }
                                    else if (associationtype == MtpConstants.FORMAT_EXIF_JPEG) {
                                        db.addphotofile(new PHOTO(mtpobjectinfo.getName()));
                                        Log.d("scan", "Image file added : " + mtpobjectinfo.getName());
                                        count_photo++;
                                    }
                                    else if (associationtype == MtpConstants.FORMAT_3GP_CONTAINER ||associationtype==MtpConstants.FORMAT_MPEG) {
                                        db.addvideofile(new VIDEO(mtpobjectinfo.getName()));
                                        Log.d("scan", "Video file added : " + mtpobjectinfo.getName());
                                        count_video++;
                                    }
                                    count++;
                                }
                                Log.d("mp3", "no of mp3 files = " + count_mp3);
                                Log.d("video", "no of video files = " + count_video);
                            }
                        }
                        Log.d("devicecomm", "Loop ran for : " + count);
                    } else
                        Log.d("devicecomm", "Storage ids = null");
                }

            }

        }
    }



    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive", "Inside Broadcast receiver");
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    Log.d("onReceive", "Made usb device to give permissions");
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            Log.d("onReceive", "Calling devicecomm method to handle communication");
                            //call method to set up device communication
                            // devicecomm(device);
                            //   Toast.makeText(context, "Permission granted and inside loop", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("onReceive", "permission denied for device " + device);
                    }
                }
            }
        };
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
    }
}
