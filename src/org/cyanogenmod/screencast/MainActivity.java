/*
 * Copyright (C) 2013 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyanogenmod.screencast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button mStartScreencastButton;
    Button mStopScreencastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mStartScreencastButton = (Button) findViewById(R.id.start_screencast);
        mStartScreencastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(ScreencastService.PREFS, 0).edit().putBoolean(ScreencastService.KEY_RECORDING, true).apply();
                startService(new Intent("org.cyanogenmod.ACTION_START_SCREENCAST")
                        .setClass(MainActivity.this, ScreencastService.class));
                finish();
            }
        });

        mStopScreencastButton = (Button) findViewById(R.id.stop_screencast);
        mStopScreencastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(ScreencastService.PREFS, 0).edit().putBoolean(ScreencastService.KEY_RECORDING, false).apply();
                startService(new Intent("org.cyanogenmod.ACTION_STOP_SCREENCAST")
                        .setClass(MainActivity.this, ScreencastService.class));
                refreshState();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshState();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        refreshState();
    }

    private void refreshState() {
        final boolean recording = getSharedPreferences(ScreencastService.PREFS, 0).getBoolean(ScreencastService.KEY_RECORDING, false);
        if (mStartScreencastButton != null) {
            mStartScreencastButton.setEnabled(!recording);
        }
        if (mStopScreencastButton != null) {
            mStopScreencastButton.setEnabled(recording);
        }
    }
}
