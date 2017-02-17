package com.fonfon.ffloatingbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingFloatingButton ffb = (FloatingFloatingButton) findViewById(R.id.fview);
        ffb.setListener(new FloatingFloatingButton.onAboveViewListener() {
            @Override
            public void onAboveView(View view) {
                if (view != null) {
                    if (view.getTag() != null)
                        switch (view.getTag().toString()) {
                            case "layout1":
                                state = 1;
                                ffb.setImageResource(R.drawable.ic_phone_call);
                                break;
                            case "layout2":
                                state = 2;
                                ffb.setImageResource(R.drawable.ic_torch);
                                break;
                            case "layout3":
                                state = 3;
                                ffb.setImageResource(R.drawable.ic_ghost);
                                break;
                            default:
                                state = 0;
                                ffb.setImageResource(R.mipmap.ic_launcher);
                                break;
                        }
                }
            }
        });

        ffb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (state) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "wow", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "lol", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "work", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "none", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }
}
