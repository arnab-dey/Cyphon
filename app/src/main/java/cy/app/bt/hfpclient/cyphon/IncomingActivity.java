package cy.app.bt.hfpclient.cyphon;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by user on 10/5/18.
 */

public class IncomingActivity extends Activity {

    private static final String TAG = "IncomingActivity";

    private Button answerButton;
    private Button rejectButton;
    private TextView numberView;

    private BluetoothDevice device1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hfp_client_main_mod);
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

        String number = getIntent().getStringExtra("EXTRA_NUMBER_HOME_ACTIVITY");
        device1 = (BluetoothDevice) getIntent().getParcelableExtra("EXTRA_DEVICE_HOME_ACTIVITY");
        numberView = (TextView)findViewById(R.id.mod_number);
        if(null != number) {
            numberView.setText(number);
        } else {
            numberView.setText("N/A");
        }

        answerButton = (Button)findViewById(R.id.mod_answer_button);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "answer";
                Intent intent = new Intent();
                intent.putExtra("ACTION", message);
                intent.putExtra("EXTRA_DEVICE_INCOMING_ACTIVITY", device1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        rejectButton = (Button)findViewById(R.id.mod_reject_button);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "reject";
                Intent intent = new Intent();
                intent.putExtra("ACTION", message);
                intent.putExtra("EXTRA_DEVICE_INCOMING_ACTIVITY", device1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*Nothing should be here to prevent back press*/
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        String number = intent.getStringExtra("EXTRA_NUMBER_HOME_ACTIVITY");
        numberView = (TextView)findViewById(R.id.mod_number);
        if(null != number) {
            numberView.setText(number);
        } else {
            numberView.setText("N/A");
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("finish_activity")) {
                BluetoothDevice device = intent.getParcelableExtra("EXTRA_DEVICE_HOME_ACTIVITY");
                if((null != device) && (device.equals(device1))) {
                    finish();
                }
            }
        }
    };
}
