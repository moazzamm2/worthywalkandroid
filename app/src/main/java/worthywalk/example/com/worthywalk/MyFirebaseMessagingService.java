package worthywalk.example.com.worthywalk;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import androidx.core.app.NotificationCompat;
import worthywalk.example.com.worthywalk.Models.User;

import static worthywalk.example.com.worthywalk.App.CHANNEL_ID;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private static final String TAG = "MyAndroidFCMService";
    Gson gson;
    User user;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {




        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification
        Uri img= remoteMessage.getNotification().getImageUrl();
        if(getBitmapfromUrl(img)!=null){
            createNotification(remoteMessage,getBitmapfromUrl(img));

        }else {
            createNotification(remoteMessage);

        }



        //check for the data/notification entry from the payload
    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
        prefsEditor.putString("Token",token);
        prefsEditor.commit();



        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }

    private void createNotification(RemoteMessage remoteMessage, Bitmap bitmapfromUrl) {


        Intent intent = new Intent( this , splash.class );
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent, 0);
        // Check if message contains a data payload.

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this,CHANNEL_ID);
        mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.getNotification().getTitle())
                 .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmapfromUrl))/*Notification with Image*/
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(remoteMessage.getNotification().getBody()));


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
    private void createNotification(RemoteMessage remoteMessage) {


        Intent intent = new Intent( this , splash.class );
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent, 0);
        // Check if message contains a data payload.

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this,CHANNEL_ID);
        mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(remoteMessage.getNotification().getBody()));


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
    public Bitmap getBitmapfromUrl(Uri imageUrl) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUrl);

            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
