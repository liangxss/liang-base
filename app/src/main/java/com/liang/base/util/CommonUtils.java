package com.liang.base.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.liang.base.MyApplication;
import com.liang.base.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liang_xs
 * @date 2016-03-27
 */
public class CommonUtils {
	private static String getRandomString(int length, String base) {
		Random random = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getRandomNumberString(int length) {
		return getRandomString(length, "0123456789");
	}

	public static boolean isMobileNO(String mobiles) {
		if (mobiles == null || "".equals(mobiles)) {
			return false;
		}
		
		String regexMOBILE = "^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$";
		String regexCM = "^1(34[0-8]|(3[5-9]|5[017-9]|8[278])\\d)\\d{7}$";
		String regexCU = "^1(3[0-2]|5[256]|8[56])\\d{8}$";
		String regexCT = "^1((33|53|8[09])[0-9]|349)\\d{7}$";
		String regexPHS = "^0(10|2[0-5789]|\\d{3})\\d{7,8}$";
//		String regexAll = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|7[0-9])\\d{8}$";
		String regexAll = "^1+[34578]+\\d{9}";
		return mobiles.matches(regexMOBILE) ||
				mobiles.matches(regexCM) || 
				mobiles.matches(regexCU) || 
				mobiles.matches(regexCT) || 
				mobiles.matches(regexAll) || 
				mobiles.matches(regexPHS);
	}

	public static boolean isExpressNum(String expressNum){
		if (TextUtils.isEmpty(expressNum)) {
			return false;
		}
		String regex = "^[a-z0-9A-Z]*$";
		return expressNum.matches(regex);
		
	}
	public static boolean isEmailAddress(String email) {
		if (email == null || "".equals(email)) {
			return false;
		}

		String regex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
				+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

		return email.matches(regex);
	}
	public static boolean isNickName(String name) {
		if (name == null || "".equals(name)) {
			return false;
		}
		
//		String regex = "[\u4e00-\u9fa5\\w]+";
//		String regex = "http(s)?:\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- .\\/?%&=]*)?";
		String regex = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
		
		return name.matches(regex);
	}

	/**
	 * @Title: emailFormat 
	 * @Description: 验证邮箱格式
	 * @return boolean
	 * @author liang_xs
	 */
	public static boolean emailFormat(String email) {
		boolean tag = true;
		String pattern1 = "[_a-z\\d\\-\\./]+@[_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)*(\\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}
	
	public static boolean isNetAvaliable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 返回当前程序版本号
	 */
	public static String getAppVersionName() {
		String versionName = "";
		try {

			PackageManager pm = MyApplication.getApplication().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(MyApplication.getApplication().getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}

		} catch (Exception e) {

		}
		return versionName;
	}


	public static int getVersionCode(Context context) {
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * @Description: 获取随机数
	 * @param count
	 * @return
	 */
	public static int getIndex(int count){
		int index = 0;
		index = (int) Math.round(Math.random() * count);
		if(index == 0){
			index = count;
		}
		return index;
	}

	public static void showKey(final EditText editText){
		editText.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if(inputManager != null) {
					inputManager.showSoftInput(editText, 0);
				}
			}
		}, 500);
	}

	public static void hideKey(Context mContext, EditText editText){
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
	public static void showKeyBoard(Context context) {
		InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputManager.isActive()) {
			inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}
	
	public static void hideKeyBoard(Context context) {
		InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	
	public static void hideInput(Activity activity) {
		View view = activity.getWindow().peekDecorView();
		if (view != null && view.getWindowToken() != null) {
			/* 隐藏软键盘 */
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
     * unicode解码（unicode编码转中文）
     *
     * @param theString
     * @return
     */
    public static String unicodeDecode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
 
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public static boolean isActivityRunning(Activity activity) {
    	ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> info = activityManager.getRunningTasks(1);
	    if(info != null && info.size() > 0){  
	        for (RunningTaskInfo componentName : info) {
                ComponentName component = componentName.topActivity;
                if (activity.getClass().getName().equals(component.getClassName())) {
                	return true;
                }
	        }
	    }
	    return false;
    }
	private static DisplayMetrics displayMetrics = null;
    
	public static float getScreenDensity() {
		if (displayMetrics == null) {
			setDisplayMetrics(MyApplication.getApplication().getResources().getDisplayMetrics());
		}
		return displayMetrics.density;
	}

	public static int getScreenHeight() {
		if (displayMetrics == null) {
			setDisplayMetrics(MyApplication.getApplication().getResources().getDisplayMetrics());
		}
		return displayMetrics.heightPixels;
	}

	public static int getScreenWidth() {
		if (displayMetrics == null) {
			setDisplayMetrics(MyApplication.getApplication().getResources().getDisplayMetrics());
		}
		return displayMetrics.widthPixels;
	}
	
	public static int getMyHeight() {
		return (int) (13.0F * getScreenWidth() / 32.0F);
	}
	public static int getHomeBnnerHeight() {
		return (int) (432.0F * getScreenWidth() / 750.0F);
	}

	public static int getMinHeight() {
		return (int) (24.0F * getScreenWidth() / 75.0F);
	}

	public static void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
		displayMetrics = DisplayMetrics;
	}

	public static int dp2px(float f) {
		return (int) (0.5F + f * getScreenDensity());
	}

	public static int px2dp(float pxValue) {
		return (int) (pxValue / getScreenDensity() + 0.5f);
	}


	
	protected static final String PREFS_FILE = "gank_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "gank_device_id";
    protected static String uuid;
    public static String getUDID(Context context) {
    	final SharedPreferences prefs = context.getSharedPreferences( PREFS_FILE, 0);
        final String id = prefs.getString(PREFS_DEVICE_ID, null );
        if (id != null) {  
            // Use the ids previously computed and stored in the prefs file  
            uuid = id;  
        } else {  
            final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            // Use the Android ID unless it's broken, in which case fallback on deviceId,  
            // unless it's not available, then fallback on a random number which we store  
            // to a prefs file  
            try {  
                if (!"9774d56d682e549c".equals(androidId)) {  
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                } else {  
                    final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                    uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                }  
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }  

            // Write the value out to the prefs file  
            prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();  
        }
    	return uuid;
    }


	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
	 * @param s 需要得到长度的字符串
	 * @return int 得到的字符串长度
	 */
	public static int getLength(String s) {
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < s.length(); i++) {
			// 获取一个字符
			String temp = s.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				valueLength += 1;
			} else {
				// 其他字符长度为0.5
				valueLength += 0.5;
			}
		}
		//进位取整
		return  (int) Math.ceil(valueLength);
	}


	public static String getImei(Context mContext){
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		LogUtil.i("liang", "imei:" + imei);
		return imei;
	}

	public static String getImsi(Context mContext){
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		LogUtil.i("liang", "imsi:" + imsi);
		return imsi;
	}
	public static String getMac() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			macSerial = "02:00:00:00:00:00";
		}
		LogUtil.i("liang", "macSerial:" + macSerial);
		return macSerial;
	}


	public static String SHA1(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			byte[] cert = info.signatures[0].toByteArray();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] publicKey = md.digest(cert);
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < publicKey.length; i++) {
				String appendString = Integer.toHexString(0xFF & publicKey[i])
						.toUpperCase(Locale.US);
				if (appendString.length() == 1)
					hexString.append("0");
				hexString.append(appendString);
				hexString.append(":");
			}
			String result = hexString.toString();
			return result.substring(0, result.length()-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取application中指定的meta-data
	 *
	 * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
	 */
	public static String getAppMetaData(Context ctx, String key) {
		if (ctx == null || TextUtils.isEmpty(key)) {
			return null;
		}
		String resultData = null;
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						resultData = applicationInfo.metaData.getString(key);
					}
				}

			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return resultData;
	}

	public static boolean isAppRunning(Context context){
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(MyApplication.getApplication().getApplicationInfo().packageName) &&
					info.baseActivity.getPackageName().equals(MyApplication.getApplication().getApplicationInfo().packageName)) {
				isAppRunning = true;
				//find it, break
				break;
			}
		}
		return isAppRunning;
	}

	public static boolean isExistActivity(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
		boolean flag = false;
		if (cmpName != null) { // 说明系统中存在这个activity
			ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
			for (RunningTaskInfo taskInfo : taskInfoList) {
				if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
					flag = true;
					break;  //跳出循环，优化效率
				}
			}
		}
		return flag;

	}

	/***
	 * 判断Network具体类型（联通移动wap，电信wap，其他net）
	 *
	 * */
	public static int getNetState(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				// 注意一：
				// NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
				// 但是有些电信机器，仍可以正常联网，
				// 所以当成net网络处理依然尝试连接网络。
				// （然后在socket中捕捉异常，进行二次判断与用户提示）。
				return TYPE_NET_WORK_DISABLED;
			} else {
				// NetworkInfo不为null开始判断是网络类型
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					// wifi net处理
					return TYPE_WIFI;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					// 注意二：
					// 判断是否电信wap:
					// 不要通过getExtraInfo获取接入点名称来判断类型，
					// 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
					// 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
					// 所以可以通过这个进行判断！

					boolean is3G = isFastMobileNetwork(mContext);

					final Cursor c = mContext.getContentResolver().query(
							PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c
								.getColumnIndex("user"));
						if (!TextUtils.isEmpty(user)) {
							if (user.startsWith(CTWAP)) {
								return is3G ? TYPE_CT_WAP : TYPE_CT_WAP_2G;
							} else if (user.startsWith(CTNET)) {
								return is3G ? TYPE_CT_NET : TYPE_CT_NET_2G;
							}
						}
					}
					c.close();

					// 注意三：
					// 判断是移动联通wap:
					// 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
					// 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
					// 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
					// 所以采用getExtraInfo获取接入点名字进行判断

					String netMode = mobNetInfoActivity.getExtraInfo();
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase();

						if (netMode.equals(CMWAP)) {
							return is3G ? TYPE_CM_WAP : TYPE_CM_WAP_2G;
						} else if (netMode.equals(CMNET)) {
							return is3G ? TYPE_CM_NET : TYPE_CM_NET_2G;
						} else if (netMode.equals(NET_3G)
								|| netMode.equals(UNINET)) {
							return is3G ? TYPE_CU_NET : TYPE_CU_NET_2G;
						} else if (netMode.equals(WAP_3G)
								|| netMode.equals(UNIWAP)) {
							return is3G ? TYPE_CU_WAP : TYPE_CU_WAP_2G;
						}
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return TYPE_OTHER;
		}

		return TYPE_OTHER;

	}

	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false; // ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return true; // ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return true; // ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return true; // ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true; // ~ 400-7000 kbps
			case TelephonyManager.NETWORK_TYPE_EHRPD:
				return true; // ~ 1-2 Mbps
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				return true; // ~ 5 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return true; // ~ 10-20 Mbps
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return false; // ~25 kbps
			case TelephonyManager.NETWORK_TYPE_LTE:
				return true; // ~ 10+ Mbps
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;
			default:
				return false;

		}
	}

	public static final String CTWAP = "ctwap";
	public static final String CTNET = "ctnet";
	public static final String CMWAP = "cmwap";
	public static final String CMNET = "cmnet";
	public static final String NET_3G = "3gnet";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	public static final String UNINET = "uninet";

	public static final int TYPE_CT_WAP = 5;
	public static final int TYPE_CT_NET = 6;
	public static final int TYPE_CT_WAP_2G = 7;
	public static final int TYPE_CT_NET_2G = 8;

	public static final int TYPE_CM_WAP = 9;
	public static final int TYPE_CM_NET = 10;
	public static final int TYPE_CM_WAP_2G = 11;
	public static final int TYPE_CM_NET_2G = 12;

	public static final int TYPE_CU_WAP = 13;
	public static final int TYPE_CU_NET = 14;
	public static final int TYPE_CU_WAP_2G = 15;
	public static final int TYPE_CU_NET_2G = 16;

	public static final int TYPE_OTHER = 17;

	/** 没有网络 */
	public static final int TYPE_NET_WORK_DISABLED = 0;

	/** wifi网络 */
	public static final int TYPE_WIFI = 4;

	public static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");


	public static void toast(Context context, String msg) {
		if(context != null) {
			Toast toast = new Toast(context);
			View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
			TextView tv = (TextView)view.findViewById(R.id.tvToast);
			tv.setText(msg);
			toast.setView(view);
			toast.setGravity(Gravity.CENTER, 0, 100);
			toast.show();
		}
	}

	public static void toast(Context context, int resId) {
		toast(context, context.getResources().getString(resId));
	}
	/**
	 * @Title: installApp
	 * @Description: 安装
	 * @return void
	 * @author liang_xs
	 */
	public static void installApp(Context context, String strFilePath) {
		FileUtils.chmod(strFilePath, "704");
		File file = new File(strFilePath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	private final static int kSystemRootStateUnknow = -1;
	private final static int kSystemRootStateDisable = 0;
	private final static int kSystemRootStateEnable = 1;
	private static int systemRootState = kSystemRootStateUnknow;

	/** 判断手机是否root权限 */
	public static boolean isRootSystem() {
		if (systemRootState == kSystemRootStateEnable) {
			return true;
		} else if (systemRootState == kSystemRootStateDisable) {
			return false;
		}
		File f = null;
		final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/",
				"/system/sbin/", "/sbin/", "/vendor/bin/" };
		try {
			for (int i = 0; i < kSuSearchPaths.length; i++) {
				f = new File(kSuSearchPaths[i] + "su");
				if (f != null && f.exists()) {
					systemRootState = kSystemRootStateEnable;
					return true;
				}
			}
		} catch (Exception e) {
		}
		systemRootState = kSystemRootStateDisable;
		return false;
	}

}

