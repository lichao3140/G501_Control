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
        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                    mReception.append(new String(buffer, 0, size));
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
}
