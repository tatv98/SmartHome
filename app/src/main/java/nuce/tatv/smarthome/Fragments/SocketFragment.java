package nuce.tatv.smarthome.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

import nuce.tatv.smarthome.Activities.MainActivity;
import nuce.tatv.smarthome.Adapters.SocketAdapter;
import nuce.tatv.smarthome.Model.Data;
import nuce.tatv.smarthome.R;

import static nuce.tatv.smarthome.Activities.MainActivity.checkConnect;
import static nuce.tatv.smarthome.Activities.MainActivity.publish;
import static nuce.tatv.smarthome.Activities.MainActivity.subcribe;

public class SocketFragment extends Fragment implements View.OnClickListener{
    View vSocket;
    private EditText edtTopic, edtMessage;
    private Button btnSend;
    public static List<Data> listData;
    public static SocketAdapter socketAdapter;
    public static ListView lvData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vSocket = inflater.inflate(R.layout.socket_fragment, container, false);
        init();
        return vSocket;
    }

    private void init() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Socket UI");
        listData = new ArrayList<>();
        socketAdapter = new SocketAdapter(getActivity(), R.layout.socket_custom_item, listData);

        lvData = vSocket.findViewById(R.id.lvReceived);
        lvData.setAdapter(socketAdapter);
        edtTopic = vSocket.findViewById(R.id.edtTopic);
        edtMessage = vSocket.findViewById(R.id.edtMessage);
        btnSend = vSocket.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        if (checkConnect){
            edtTopic.setText("CAMBIENMUA");
            subcribe(edtTopic.getText().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
                if (checkConnect){
                    subcribe(edtTopic.getText().toString());
                    publish(edtTopic.getText().toString() ,edtMessage.getText().toString());
                }
                break;
        }
    }
}
