package worthywalk.example.com.worthywalk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static worthywalk.example.com.worthywalk.App.CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private static final String TAG = "MyAndroidFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification
        createNotification(remoteMessage.getNotification().getBody());


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

    private void createNotification( String messageBody) {
        Intent intent = new Intent( this , MainActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this,CHANNEL_ID);
        mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Android Tutorial Point FCM Tutorial")
                 .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
