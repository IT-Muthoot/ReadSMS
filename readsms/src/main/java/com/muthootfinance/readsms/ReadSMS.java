package com.muthootfinance.readsms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ReadSMS {
    final static int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static List<String> getSMS(Context context){
        List<String> sms = new ArrayList<String>();
        if(ContextCompat.checkSelfPermission(context, "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            Uri uriSMSURI = Uri.parse("content://sms/inbox");
            Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);
            while (cur.moveToNext()) {
                @SuppressLint("Range") String address = cur.getString(cur.getColumnIndex("address"));
                String body = cur.getString(cur.getColumnIndexOrThrow("body"));
                sms.add("Number: " + address + " .Message: " + body);
            }
        }else{
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
            getSMS(context);
        }
        return sms;
    }
}
