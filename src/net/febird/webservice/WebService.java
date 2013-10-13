package net.febird.webservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.util.Log;
import fi.iki.elonen.SimpleWebServer;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.widget.Toast;

import java.io.*;

public class WebService extends Service {
	public static final String TAG = "WebService";
	private NotificationManager mNM;
	private int NOTIFICATION = R.string.service_started;
	
	public WebService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return null;
	}
	@Override
	public void onCreate(){
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		//copyAssetsToLocalData();
		copyFolder("www");
	}

	@Override
	public int onStartCommand(Intent intent, int flag, int startId){
		//TODO start webserver;
		String[] args = new String[]{"-h","0.0.0.0","-p","8080","-d",getFilesDir().getPath()+"/www"};
		SimpleWebServer.main(args);
		Toast.makeText(this,"Server started.",Toast.LENGTH_SHORT).show();
		showNotification(mNM);
		
		return START_STICKY;
	}
	@Override
	public void onDestroy(){
        //mNM.cancel(NOTIFICATION);
		// Tell the user we stopped.
		SimpleWebServer.mainStop();
        Toast.makeText(this,"Server stopped.",Toast.LENGTH_SHORT).show();
	}
	
	private void showNotification(NotificationManager nm){
		Notification notification = new Notification(android.R.drawable.ic_media_play, getText(NOTIFICATION),
		        System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(this, getText(R.string.app_name),
		        getText(NOTIFICATION), pendingIntent);
		startForeground(NOTIFICATION, notification);
		
		/* when use notify, will be clear with cancel when destroyed.*/
		//nm.notify(NOTIFICATION, notification);
	}
	private void copyAssetsToLocalData(){
		File wwwfolder = new File( getFilesDir().getPath() + "/www");
		if (!wwwfolder.exists()){
			copyFolder("www");
		}
	}	
	private void copyFolder(String name) {
        // "Name" is the name of your folder!
		AssetManager assetManager = getAssets();
		String[] files = null;
		String BasePath = getFilesDir().getPath();

	    try {
	        files = assetManager.list(name);
	    } catch (IOException e) {
	        Log.e(TAG, "Failed to get asset file list.", e);
	    }
	    // Analyzing all file on assets subfolder
	    for(String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        // First: checking if there is already a target folder
	        File folder = new File( BasePath + "/" + name);
	        boolean success = true;
	        if (!folder.exists()) {
	            success = folder.mkdir();
	        }
	        if (success) {
	            // Moving all the files on external SD
	            try {
	                in = assetManager.open(name + "/" +filename);
	                out = new FileOutputStream(BasePath + "/" + name + "/" + filename);
	                Log.i(TAG, BasePath + "/" + name + "/" + filename);
	                copyFile(in, out);
	                in.close();
	                in = null;
	                out.flush();
	                out.close();
	                out = null;
	            } catch(IOException e) {
	                Log.e(TAG, "Failed to copy asset file: " + filename, e);
	            }
	        }
	        else {
	            // Do something else on failure
	        }       
	    }
}

	//Method used by copyAssets() on purpose to copy a file.
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1) {
		    out.write(buffer, 0, read);
		}
	}
}
