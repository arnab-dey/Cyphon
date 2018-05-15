package cy.app.bt.hfpclient.cyphon;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadsetClientCall;
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
    private TextView numberOnCallView;
    private TextView onCallTextView;
    private Button rejectWaitingButton;
    private Button acceptWaitingReleaseActiveButton;
    private Button acceptWaitingHoldActiveButton;

    private BluetoothDevice device1;
    private boolean hasWaiting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hfp_client_main_mod);
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
        hasWaiting = false;

        String number = getIntent().getStringExtra("EXTRA_NUMBER_HOME_ACTIVITY");
        device1 = (BluetoothDevice) getIntent().getParcelableExtra("EXTRA_DEVICE_HOME_ACTIVITY");
        numberView = (TextView)findViewById(R.id.mod_number);
        if(null != number) {
            numberView.setText(number);
        } else {
            numberView.setText("N/A");
        }

        answerButton = (Button)findViewById(R.id.mod_answer_button);
        rejectButton = (Button)findViewById(R.id.mod_reject_button);
        numberOnCallView = (TextView)findViewById(R.id.mod_number_on_call);
        onCallTextView = (TextView)findViewById(R.id.mod_on_call_text);
        rejectWaitingButton = (Button)findViewById(R.id.mod_reject_waiting_button);
        acceptWaitingReleaseActiveButton = (Button)findViewById(R.id.mod_accept_waiting_release_active_button);
        acceptWaitingHoldActiveButton = (Button)findViewById(R.id.mod_accept_waiting_hold_active_button);

        rejectWaitingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "rejectWaiting";
                Intent intent = new Intent();
                intent.putExtra("ACTION", message);
                intent.putExtra("EXTRA_DEVICE_INCOMING_ACTIVITY", device1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        acceptWaitingReleaseActiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "acceptWaitingReleaseActive";
                Intent intent = new Intent();
                intent.putExtra("ACTION", message);
                intent.putExtra("EXTRA_DEVICE_INCOMING_ACTIVITY", device1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        acceptWaitingHoldActiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "acceptWaitingHoldActive";
                Intent intent = new Intent();
                intent.putExtra("ACTION", message);
                intent.putExtra("EXTRA_DEVICE_INCOMING_ACTIVITY", device1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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

        postProcess(getIntent().getBooleanExtra("SHOW_WAITING_UI", false));
    }

    private void postProcess(boolean isWaitingUI) {
        if(isWaitingUI) {
            hasWaiting = true;
            answerButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            numberOnCallView.setVisibility(View.VISIBLE);
            onCallTextView.setVisibility(View.VISIBLE);
            rejectWaitingButton.setVisibility(View.VISIBLE);
            acceptWaitingReleaseActiveButton.setVisibility(View.VISIBLE);
            acceptWaitingHoldActiveButton.setVisibility(View.VISIBLE);
            String numberOnCall = getIntent().getStringExtra("EXTRA_ACTIVE_NUMBER_HOME_ACTIVITY");
            if(null != numberOnCall) {
                numberOnCallView.setText(numberOnCall);
            } else {
                numberOnCallView.setText("N/A");
            }
        } else {
            hasWaiting = false;
            numberOnCallView.setVisibility(View.GONE);
            onCallTextView.setVisibility(View.GONE);
            rejectWaitingButton.setVisibility(View.GONE);
            acceptWaitingReleaseActiveButton.setVisibility(View.GONE);
            acceptWaitingHoldActiveButton.setVisibility(View.GONE);
            answerButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
        }
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
        postProcess(intent.getBooleanExtra("SHOW_WAITING_UI", false));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("finish_activity")) {
                BluetoothDevice device = intent.getParcelableExtra("EXTRA_DEVICE_HOME_ACTIVITY");
                if((null != device) && (device.equals(device1))) {
                    if(intent.hasExtra("EXTRA_CALL_HOME_ACTIVITY")) {
                        BluetoothHeadsetClientCall callInfo = (BluetoothHeadsetClientCall) intent.getParcelableExtra("EXTRA_CALL_HOME_ACTIVITY");
                        if(null != callInfo) {
                            if(hasWaiting) {
                                if(numberView.getText().equals(callInfo.getNumber())) {
                                    finish();
                                }
                            } else {
                                finish();
                            }
                        }
                    }
                }
            }
        }
    };
}
