/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.lichao.g501_control;

import java.io.IOException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ConsoleActivity extends SerialPortActivity implements View.OnClickListener {

    EditText mReception, emission;
    Button btn_send;
    String json_str = null;
    String json_length = null;
    String temp_str = null;
    int value_ten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.console);

        mReception = findViewById(R.id.EditTextReception);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(this);

        emission = findViewById(R.id.EditTextEmission);

        emission.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("lichao", "按:" + actionId);
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//                    int i;
//                    CharSequence t = v.getText();
//                    char[] text = new char[t.length()];
//                    for (i = 0; i < t.length(); i++) {
//                        text[i] = t.charAt(i);
//                    }
//                    try {
//                        mOutputStream.write(new String(text).getBytes());
//                        mOutputStream.write('\n');
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                return false;
            }
        });
    }

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {

        String data = new String(buffer, 0, size);

        json_str = json_str + data.replace(" ", "");
        temp_str = json_str.substring(4, json_str.length());
        json_length = temp_str.substring(2, 6);
        value_ten = Integer.parseInt(json_length,16);

        if (temp_str.length() >= value_ten * 2) {
            //要处理的十六进制数据
            LogUtil.i("zfr_data", "temp_str:" + temp_str);
        }

        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                    mReception.append(new String(buffer, 0, size));
//                    String data = "7B22736E223A22373138383032363131222C22757365724944223A223638222C2273746166664944223A223132222C226175746854797065223A2230222C22757365724E616D65223A22E4BDA0222C226175746854696D65223A22323031392D30362D32362031393A33383A3339222C2249444E4F223A22343330343032313938303131323231353358222C2264757479223A22E9A1B9E79BAEE8B49FE8B4A3E4BABA222C2263657274696669636174696F6E223A22222C2249434E4F223A22222C2270686F746F223A22227D";
                    //data = data.substring(data.indexOf("7B22"), data.length());//截取字符串

//                    data = toStringHex(data);//十六进制转字符串 //{"addInfo":{"result_code":"-1","err_code":"NEED_REVERSAL","err_msg":"exchange cups error"}}
//                    Log.d("zfr_data1:", data);

//                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);//GSON解析
//                    Log.d("zfr_addInfo:", jsonObject.get("addInfo").getAsJsonObject().get("err_code").getAsString());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                int i;
                CharSequence t = emission.getText().toString().trim();
                Log.e("lichao", "emission=" + emission.getText().toString().trim());
                char[] text = new char[t.length()];
                for (i = 0; i < t.length(); i++) {
                    text[i] = t.charAt(i);
                }
                try {
                    mOutputStream.write(new String(text).getBytes());
                    mOutputStream.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 十六进制字符串转换字符串
     * 35353637 ==> 5567
     *
     * @return String
     */
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
