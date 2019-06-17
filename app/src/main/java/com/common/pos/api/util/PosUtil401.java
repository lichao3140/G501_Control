package com.common.pos.api.util;

import android.util.Log;

public class PosUtil401 {
	
	public static final int WG26 = 26;
	public static final int WG32 = 32;
	public static final int WG34 = 34;
	public static final int WGBD = 35;
	
	private static String wgCheckCardNum(String cardBindaryString,int type){
		String wgPackage = cardBindaryString;
		int evenParity = 0;
		int paritCheck = 0;
		int cardNumLength = 0;
		if(type == WG26){
			cardNumLength = 24;
		}else if(type == WG32){
			cardNumLength = 30;
		}else if(type == WG34 || type == WGBD){
			cardNumLength = 32;
		}
		while(wgPackage.length()<cardNumLength){
			wgPackage = "0"+wgPackage;
		}
		for(int i=0;i<cardNumLength;i++){
			if(i < cardNumLength/2 && wgPackage.charAt(i) == '1'){
				evenParity++;
			}else if(i > (cardNumLength/2-1) && wgPackage.charAt(i) == '1'){
				paritCheck++;
			}
		}
		if(evenParity%2==0){
			evenParity = 0;
		}else{
			evenParity = 1;
		}
		if(paritCheck%2==0){
			paritCheck = 1;
		}else{
			paritCheck = 0;
		}
		wgPackage = evenParity + wgPackage + paritCheck;
		
		return wgPackage;
	}
	
	private static long[] getWgPackage(long cardNum,int type){
		
		String wgPackage = null;
		long wgPackageData[] = new long[]{0,0};
		
		wgPackage = Long.toBinaryString(cardNum);
		
		if(type == WGBD){
			while(wgPackage.length()<30){
				wgPackage = "0"+wgPackage;
			}
			wgPackage = "11"+wgPackage;
			type = type -1;
		}/*else{
			wgPackage = wgCheckCardNum(wgPackage,type);
		}*/
		wgPackage = wgCheckCardNum(wgPackage,type);
		//Log.d("wgpackage", "type:"+type+" wgPackage:"+wgPackage+"\n");
		if(wgPackage.length() <= 32){
			wgPackageData[0] = Long.parseLong(wgPackage, 2);
			wgPackageData[1] = type;
		}else if(wgPackage.length()>32){
			String tempLow = wgPackage.substring(0, 32);
			//Log.d("wgpackage", "lowB:"+tempLow);
			String typeBinString = Integer.toBinaryString(type);
			while(typeBinString.length()<8){
				typeBinString = "0" + typeBinString;
			}
			String tempHigh = wgPackage.substring(32, wgPackage.length())/* +"0000000000000000000000"+ typeBinString*/;
			while(tempHigh.length()<24){
				tempHigh = tempHigh+"0";
			}
			tempHigh = tempHigh + typeBinString;
			//Log.d("wgpackage", "highB:"+tempHigh);
			wgPackageData[0] = Long.parseLong(tempLow, 2);
			wgPackageData[1] = Long.parseLong(tempHigh, 2);
		}
		
		//Log.d("wgpackage", "wgPackageData[0]:"+wgPackageData[0]+";wgPackageData[1]:"+wgPackageData[1]);
		
		return wgPackageData;
	}
	
	public synchronized static int sendWgPackage(long cardNum,int type){
		
		long[] temp = getWgPackage(cardNum, type);
		return getWgStatus(temp[0], temp[1]);
		
	}

	public synchronized static native int setFlushLedPower(int powerStatus);

	public synchronized static native int setLedPower(int powerStatus);
	
	public synchronized static native int setLedBright(int powerStatus);
	
	public synchronized static native int setCameraPower(int powerStatus);
	
	public synchronized static native int setIRLed(int powerStatus);
	
	public synchronized static native int setLanPower(int powerStatus);
	
	public synchronized static native int setOTGPower(int powerStatus);
	
	public synchronized static native int setSimPower(int powerStatus);
	
	public synchronized static native int setSimPowerKey(int powerStatus);
	
	public synchronized static native int setSimRest(int powerStatus);
	
	public synchronized static native int setLedPower400b(int powerStatus,int selectNum);

	public synchronized static native int setJiaJiPower(int powerStatus);

	public synchronized static native int getPriximitySensorStatus();
	
	public synchronized static native int getPriximitySensorStatus400b(int selectNum);

	public synchronized static native int setRelayPower(int powerStatus);

	public synchronized static native int setRs485Status(int powerStatus);
	
	public synchronized static native int getWg26Status(long powerStatus);
	
	public synchronized static native int getWg32Status(long powerStatus);
	
	public synchronized static native int getWg34Status(long powerStatus);
	
	private synchronized static native int getWgStatus(long dataLow, long dataHigh);
	
	public synchronized static native int setEMC(int arg);
	
	public synchronized static native int setColorLed(int status, int led);
	
	public synchronized static native int setMsrPower(int status);
	
	public synchronized static native int getMsrPower2();

	static {
		System.loadLibrary("posutil");
	}

}