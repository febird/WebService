package net.febird.webservice;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView) findViewById(R.id.TextView02);
		tv.setText(getLocalIpAddress());
		if(isServiceRun(this)){
			((ToggleButton)findViewById(R.id.toggleButton1)).setChecked(true);
		}
		findViewById(R.id.toggleButton1).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
					String ipv4;
					if (!inetAddress.isLoopbackAddress()
	                		&& InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {
	                    return ipv4;
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e("WebServer", ex.toString());
	    }
	    return null;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.toggleButton1){
			ToggleButton tb = (ToggleButton)v;
			Intent intent=new Intent(this,WebService.class);  
			if(tb.isChecked()){
			    this.startService(intent);				
			}else{
			    this.stopService(intent);				
			}
		}
		
	}
	public boolean isServiceRun(Context context){
		  ActivityManager am = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
		  List<RunningServiceInfo> list = am.getRunningServices(30);
		  for(RunningServiceInfo info : list){
			  if(info.service.getClassName().equals("net.febird.webservice.WebService")){
				  return true;
			  }
		  }
		  return false;
		}
}
