package nuce.tatv.smarthome.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nuce.tatv.smarthome.Activities.MainActivity;
import nuce.tatv.smarthome.R;

import static nuce.tatv.smarthome.Activities.MainActivity.checkConnect;
import static nuce.tatv.smarthome.Activities.MainActivity.disconnect;

public class SettingFragment extends Fragment implements View.OnClickListener{
    private Button btnSave, btnClear;
    private EditText edtServer, edtPort, edtUser, edtPass;
    private SharedPreferences sharedPreferences;
    View vSetting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vSetting = inflater.inflate(R.layout.setting_fragment, container, false);
        init();
        return vSetting;
    }

    public void init() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Setting");

        edtServer = vSetting.findViewById(R.id.edtServer);
        edtPort = vSetting.findViewById(R.id.edtPort);
        edtUser = vSetting.findViewById(R.id.edtUser);
        edtPass = vSetting.findViewById(R.id.edtPass);
        btnSave = vSetting.findViewById(R.id.btnSave);
        btnClear = vSetting.findViewById(R.id.btnClear);

        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);


        sharedPreferences = this.getActivity().getSharedPreferences("dataMQTT", Context.MODE_PRIVATE);
        String mqttServer = sharedPreferences.getString("mqttServer", "");
        String mqttPort = sharedPreferences.getString("mqttPort", "");
        String mqttUser = sharedPreferences.getString("mqttUser", "");
        String mqttPass = sharedPreferences.getString("mqttPass", "");
        if (mqttServer.equals("") || mqttPort.equals("") || mqttUser.equals("") || mqttPass.equals("")){
            edtServer.setText("tailor.cloudmqtt.com");
            edtPort.setText("15321");
            edtUser.setText("hkiaooev");
            edtPass.setText("_VOy1bLzPLJt");
        }else {
            edtServer.setText(mqttServer);
            edtPort.setText(mqttPort);
            edtUser.setText(mqttUser);
            edtPass.setText(mqttPass);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                save();
                break;
            case R.id.btnClear:
                clear();
                break;
        }
    }
    private void  save(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mqttServer", edtServer.getText().toString());
        editor.putString("mqttPort", edtPort.getText().toString());
        editor.putString("mqttUser", edtUser.getText().toString());
        editor.putString("mqttPass", edtPass.getText().toString());
        editor.commit();
        Toast.makeText(getActivity(), "Đã cài đặt tài khoản MQTT thành công, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT);
        delay(5);
    }
    private void clear() {
        edtServer.setText("");
        edtPort.setText("");
        edtUser.setText("");
        edtPass.setText("");
    }

    public void delay(int seconds){
        final int milliseconds = seconds * 1000;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                }, milliseconds);
            }
        });
    }

}
