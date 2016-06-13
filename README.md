# IMEDemo

----------
键盘输入简单demo

------------
```java
static Random rand = new Random();
	public static boolean humanTypeString_zidong(Activity activity,String title) {
		
		while (!setVPN.getSharedPreferencesBoolean(activity, "keyboardState")) {
			baseCmd.Wait(2000);
			Log.e("wodelog", "继续等待键盘");
			Nexus5Touch.Tap(322, 685);// 点击用户名
		}
		
		int randNumfor = rand.nextInt(5)+1;
		
		for (int i = 0; i < randNumfor; i++) {
			int randNum = rand.nextInt(SoftKeyboard.mLetterList1111.length-1);
			int jcode = SoftKeyboard.mLetterList1111[randNum];
			Log.i("CMD", "人工输入字符串:" + (char)jcode);
			SoftKeyboard.touchKeyByKeyCode(jcode);
					}
		
		 Intent intent = new Intent("com.ime.input");
		 intent.putExtra("input", title);
		 intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		 activity.sendBroadcast(intent);
		return true;
	}
```
