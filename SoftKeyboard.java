package cmd.normal.DeviceTouch;

import android.util.Log;

import cmd.normal.baseCmd;
import cmd.normal.model.zlFrame;
import cmd.normal.model.zlKey;

/**
 * Created by zhongliang on 16-1-19.
 */
public class SoftKeyboard {
    public static zlKey[]   keyMap;
    public static String    currentKeyboard = "";
    public static boolean   isShift = false;
    public static int[]     mSymbolsList = {49,50,51,52,53,54,55,56,57,48,64,35,36,37,38,42,45,61,40,41,-1,33,34,39,58,59,47,63,-5,-3,-2,-101,32,44,10};
    public static int[]     mSymbolsShiftList = {126,177,215,247,8226,176,96,180,123,125,169,163,8364,94,174,165,95,43,91,93,-1,161,60,62,162,124,92,191,-5,-3,-2,-101,32,8230,10};
    public static int[]     mLetterList = 	  {113,119,101,114,116,121,117,105,111,112,97,115,100,102,103,104,106,107,108,-1,122,120,99,118,98,110,109,-5,-3,-2,-101,32,46,10};
    public static int[]     mLetterList1111 = {113,119,101,114,116,121,117,105,111,112,97,115,100,102,103,104,106,107,108,-1,122,120,99,118,98,110,109,-3,-2,-101,32,46};
    

    public static int[]     tapKeyList = new int[1000];
    public static int       tapKeyIndex = -1;

    public static void  currentKeyPress(int code){
        tapKeyIndex = tapKeyIndex + 1;
        tapKeyList[tapKeyIndex] = code;
    }

    public static void  cleanKeyList(){
        for (int i=0;i<1000;i++){
            tapKeyList[i] = -1;
        }
        tapKeyIndex = -1;
    }

    //重置
    public static void resetKeyMap(String aMsg){
        try {
//            Log.e("CMD", aMsg);
            String[] keyArray = aMsg.split("\n");
            keyMap = new zlKey[keyArray.length-1];
            int index = 0;
            for (String line:keyArray) {
                String[] keyCode = line.split(",");
                if (keyCode.length>=5){
                    zlFrame tempFrame = new zlFrame();
                    tempFrame.x = Integer.parseInt(keyCode[0]);
                    tempFrame.y = Integer.parseInt(keyCode[1]);
                    tempFrame.w = Integer.parseInt(keyCode[2]);
                    tempFrame.h = Integer.parseInt(keyCode[3]);
                    int codes[] = new int[keyCode.length-4];
                    for (int i = 4 ; i <keyCode.length;i++){
                        codes[i-4] = Integer.parseInt(keyCode[i]);
                    }
                    zlKey tempKey = new zlKey(tempFrame,codes);
                    keyMap[index]=tempKey;
                    index = index + 1;
                }
                if (keyCode.length==2){
                    currentKeyboard = keyCode[0];
                    isShift = Boolean.parseBoolean(keyCode[1]);
                }
            }
        }catch (Exception e){
            Log.i("CMD",e.toString());
            e.printStackTrace();
        }
    }

    public static  void touchLetter(int aKeyCode){
        if (aKeyCode<0){
            return;
        }

        try {
            if (aKeyCode>65&&aKeyCode<90){
                ChangeToLetterKeyBoard(true);
                touchKeyByKeyCode(aKeyCode + 32);
                return;
            }

            boolean isInmSymbolsList = isInList(aKeyCode,mSymbolsList);
            boolean isInmSymbolsShiftList = isInList(aKeyCode,mSymbolsShiftList);
            boolean inInmLetterList = isInList(aKeyCode, mLetterList);
            if (isInmSymbolsList){
                ChangeToSymbolsKeyboard();
                touchKeyByKeyCode(aKeyCode);
                return;
            }
            if (isInmSymbolsShiftList){
                ChangeToSymbolsShiftKeyboard();
                touchKeyByKeyCode(aKeyCode);
                return;
            }
            if (inInmLetterList){
                ChangeToLetterKeyBoard(false);
                touchKeyByKeyCode(aKeyCode);
                return;
            }
        }catch (Exception e){
            Log.e("CMD", e.toString());
        }

    }

    public static  boolean isInList(int aKeyCode,int[] list){
        for (int i = 0; i < list.length; i++){
            if (list[i]==aKeyCode){
                return true;
            }
        }
        return false;
    }

    public static void touchShift(){
       touchKeyByKeyCode(-1);
    }

    public static void touchChangeKeyboard(){
        touchKeyByKeyCode(-2);
    }

    public static void touchDel(int aCount){
        for (int i=0;i<aCount;i++){
            touchDel();
        }
    }

    public static void touchDel(){
        touchKeyByKeyCode(-5);
    }

    public static void ChangeToLetterKeyBoard(boolean upper){

        if (!currentKeyboard.equalsIgnoreCase("mQwertyKeyboard")){
            touchChangeKeyboard();
            baseCmd.randomWait(500,500);
        }
        if (upper!=isShift){
            Log.e("CMD", "upper:"+upper+" Shift:"+isShift);
            touchShift();
        }
    }

    public static void ChangeToSymbolsKeyboard(){
        if (currentKeyboard.equalsIgnoreCase("mQwertyKeyboard")){
            touchChangeKeyboard();
            baseCmd.randomWait(500, 500);
        }
        if (currentKeyboard.equalsIgnoreCase("mSymbolsShiftedKeyboard")){
            touchShift();
        }
    }

    public static void ChangeToSymbolsShiftKeyboard(){
        if (currentKeyboard.equalsIgnoreCase("mQwertyKeyboard")){
            touchChangeKeyboard();
            baseCmd.randomWait(1000, 1000);
        }
        if (currentKeyboard.equalsIgnoreCase("mSymbolsKeyboard")){
            touchShift();
        }
    }

    public static boolean CheckInput(String aText){
        char chs[]=aText.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            int code = chs[i];
            if (code!=tapKeyList[i]){
                touchDel(chs.length+2);
                return false;
            }
        }
        if (tapKeyList[chs.length]!=-1){
            touchDel(chs.length+2);
            return false;
        }
        return true;
    }


    public static void touchKeyByKeyCode(int aKeyCode){
        int xPos = 0;
        int yPos = 0;
        for (zlKey akey:keyMap){
            if (akey.isInTheKey(aKeyCode)){
                xPos = akey.m_frame.getRanTouchX();
                yPos = akey.m_frame.getRanTouchY();
            }
        }
        Nexus5Touch.Tap(xPos,yPos);
        baseCmd.randomWait(250, 500);
    }

    public static boolean isEqualKeylist(int[] aList,int[] bList){
        int len = Math.min(aList.length,bList.length);
        for (int i=0;i<len;i++){
            if (aList.length!=bList.length){
                return false;
            }
        }
        return true;
    }
}
