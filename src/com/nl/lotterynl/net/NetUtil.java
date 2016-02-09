package com.nl.lotterynl.net;

import com.nl.lotterynl.domain.GlobalParams;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class NetUtil {

	public static boolean checkNet(Context context) {
		boolean isWifi = isWIFIConnection(context);
		boolean isMobile = isMOBILEConnection(context);
		if (isMobile) {
			readAPN(context);
		}
		if (isWifi || isMobile) {
			return true;
		}
		return false;
	}

	private static void readAPN(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://telephony/carriers/preferapn");
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			GlobalParams.PROXY = cursor.getString(cursor.getColumnIndex("Proxy"));
			GlobalParams.PORT = cursor.getInt(cursor.getColumnIndex("Port"));
		}
		cursor.close();
	}

	private static boolean isMOBILEConnection(Context context) {
		boolean result = isNetConnection(context,
				ConnectivityManager.TYPE_MOBILE);
		return result;
	}

	private static boolean isWIFIConnection(Context context) {
		boolean result = isNetConnection(context, ConnectivityManager.TYPE_WIFI);
		return result;
	}

	private static boolean isNetConnection(Context context, int type) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(type);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}
}
