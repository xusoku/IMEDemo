package com.android.broadcast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.android.softkeyboard.SoftKeyboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

public class ReceiveBroadCast extends BroadcastReceiver
{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            //得到广播中得到的数据，并显示出来
        	
          
        	Message msg=new Message();
        	msg.obj=intent.getStringExtra("input");
        	
        	SoftKeyboard.handler.sendMessage(msg);
        	
        }
        
}