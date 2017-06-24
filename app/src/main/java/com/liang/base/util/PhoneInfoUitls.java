package com.liang.base.util;

import android.app.Activity;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * @ClassName: PhoneInfoUitl
 * @Description: 获取设备信息
 * @author liang_xs
 * @date 2014-7-7
 */
public class PhoneInfoUitls{
	public static String TAG = "PhoneInfoUitl";
	
	/**
	 * @Title: getPhoneInfo 
	 * @Description: 获取手机信息
	 * @return Map<String,String>
	 * @author liang_xs
	 */
	public static Map<String, String> getPhoneInfo(Context mContext) {
		Map<String, String> mapInfos = new HashMap<String, String>();
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(mContext.TELEPHONY_SERVICE);
		String strmobleBrand = android.os.Build.BRAND;// 手机品牌
		String strmobleType = android.os.Build.MODEL; // 手机型号
		String strImei = tm.getDeviceId();
		String strImsi = tm.getSubscriberId();
		String strProvidersName = null;
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (!TextUtils.isEmpty(strImsi)) {
			if (strImsi.startsWith("46000") || strImsi.startsWith("46002")) {
				strProvidersName = "中国移动";
			} else if (strImsi.startsWith("46001")) {
				strProvidersName = "中国联通";
			} else if (strImsi.startsWith("46003")) {
				strProvidersName = "中国电信";
			}
		}
		String strMobleNumber = tm.getLine1Number(); // 手机号码
		String strExternalMemory = formatSize(getAvailableExternalMemorySize());
		String strInternalMemory = formatSize(getAvailableInternalMemorySize());
//		String strRootMemory = formatSize(getAvailableRootMemorySize());
		String strCpu = getCpuInfo();
		int intCoresCount = getCoresCount();
		DisplayMetrics dm = new DisplayMetrics();
		int width = dm.widthPixels;// 宽度
		int height = dm.heightPixels ;//高度
		String strRom = getRomversion();
		String strVersionName = CommonUtils.getAppVersionName();
		mapInfos.put("手机ROM", strRom);
		mapInfos.put("品牌", strmobleBrand);
		mapInfos.put("型号", strmobleType);
		mapInfos.put("版本 Android", android.os.Build.VERSION.RELEASE);
		mapInfos.put("手机IMEI", strImei);
		mapInfos.put("手机IMSI", strImsi);
		mapInfos.put("手机号码", strMobleNumber);
		mapInfos.put("运营商", strProvidersName);
		mapInfos.put("手机SD卡可用空间", strExternalMemory);
		mapInfos.put("手机可用空间", strInternalMemory);
		mapInfos.put("手机CPU型号", strCpu);
		mapInfos.put("屏幕尺寸", width+"*"+height);
		mapInfos.put("时间", getDate());
		mapInfos.put("apk版本名称", strVersionName);
		return mapInfos;
	}
	
	public static TimeZone getTimeZone(){
		TimeZone tz = TimeZone.getDefault();
		String s = "TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID();
		return tz;
	}
	
	/** 获取Rom版本 */
	public static String getRomversion() {
		String rom = "";
		try {
			String modversion = getSystemProperty("ro.modversion");
			String displayId = getSystemProperty("ro.build.display.id");
			if (modversion != null && !modversion.equals("")) {
				rom = modversion;
			}
			if (displayId != null && !displayId.equals("")) {
				rom = displayId;
			}
		} catch (Exception e) {
		}
		return rom;
	}
	
	/** 获取系统配置参数 */
	public static String getSystemProperty(String key) {
		String pValue = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method m = c.getMethod("get", String.class);
			pValue = m.invoke(null, key).toString();
		} catch (Exception e) {
//			LogUtils.e(e);
		}
		return pValue;
	}
	
	private String getAvailMemory(Activity mActivity) {
		// 获取android当前可用内存大小                
		android.app.ActivityManager am = (android.app.ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);            
		//当前系统的可用内存
		long availMem = mi.availMem;                 
		return Formatter.formatFileSize(mActivity.getBaseContext(), mi.availMem);// 将获取的内存大小规格化        }
	}
	/**
	 * @Title: getAvailableExternalMemorySize 
	 * @Description: 获取sd卡存储空间大小
	 * @return long
	 * @author liang_xs
	 */
	public static long getAvailableExternalMemorySize() {
    	String strState = Environment.getExternalStorageState();
    	if(Environment.MEDIA_MOUNTED.equals(strState)) {
    		File sdcardDir = Environment.getExternalStorageDirectory();
    		StatFs sf = new StatFs(sdcardDir.getPath());
    		long blockSize = sf.getBlockSize();
    		long availableBlocks = sf.getAvailableBlocks();
    		long unUsed = availableBlocks * blockSize;
    		return unUsed;
    	}
    	return -1;
    }
	
	/**
	 * @Title: getAvailableInternalMemorySize 
	 * @Description: 获取手机内部存储空间大小
	 * @return long
	 * @author liang_xs
	 */
	public static long getAvailableInternalMemorySize() {
		File filePath = Environment.getDataDirectory();
		StatFs stat = new StatFs(filePath.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long unUsed = availableBlocks * blockSize;
		return unUsed;
	}

	/**
	 * @Title: getAvailableInternalMemorySize 
	 * @Description: 获取手机内部存储空间大小
	 * @return long
	 * @author liang_xs
	 */
    public static long getAvailableRootMemorySize() {
        File filePath = Environment.getRootDirectory();
        StatFs stat = new StatFs(filePath.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long unUsed = availableBlocks * blockSize;
        return unUsed;
    }
	
    /**
     * @Title: formatSize 
     * @Description: 转换大小
     * @return String
     * @author liang_xs
     */
    public static String formatSize(long size) {
		String strSuffix = null;
		float fSize = 0;

		if (size >= 1024) {
			strSuffix = "KB";
			fSize = size / 1024;
			if (fSize >= 1024) {
				strSuffix = "MB";
				fSize /= 1024;
			}
			if (fSize >= 1024) {
				strSuffix = "GB";
				fSize /= 1024;
			}
		} else {
			fSize = size;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
		StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
		if (strSuffix != null)
			resultBuffer.append(strSuffix);
		return resultBuffer.toString();
	}


	/**
	 * @Title: getCpuInfo 
	 * @Description: 获得CPU信息
	 * @return String
	 * @author liang_xs
	 */
	public static String getCpuInfo() {
		String strPath = "/proc/cpuinfo";
		String strRead = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(strPath);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			strRead = localBufferedReader.readLine();
			arrayOfString = strRead.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			strRead = localBufferedReader.readLine();
			arrayOfString = strRead.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		String strResult = cpuInfo[0];
//		String strResult = "CPU型号 " + cpuInfo[0] + "\n" + "CPU频率: " + cpuInfo[1];
		return strResult;
	}

	/**
	 * @Title: getCoresCount 
	 * @Description: 获得CPU核数
	 * @return int
	 * @author liang_xs
	 */
	public static int getCoresCount() {
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			// Default to return 1 core
			return 1;
		}
	}
	
	/**
	 * @Title: getDate 
	 * @Description: 获取系统时间
	 * @return String
	 * @author liang_xs
	 */
	public static String getDate() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String time = formatter.format(new Date());
		return time;
	}

}
