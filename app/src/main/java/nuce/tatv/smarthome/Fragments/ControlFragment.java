package nuce.tatv.smarthome.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import nuce.tatv.smarthome.Activities.AddModule;
import nuce.tatv.smarthome.Activities.MainActivity;
import nuce.tatv.smarthome.R;

import static nuce.tatv.smarthome.Activities.MainActivity.checkBlue;
import static nuce.tatv.smarthome.Activities.MainActivity.checkConnect;
import static nuce.tatv.smarthome.Activities.MainActivity.checkOut;
import static nuce.tatv.smarthome.Activities.MainActivity.checkRed;
import static nuce.tatv.smarthome.Activities.MainActivity.checkSpeaker;
import static nuce.tatv.smarthome.Activities.MainActivity.checkWarning;
import static nuce.tatv.smarthome.Activities.MainActivity.checkYellow;
import static nuce.tatv.smarthome.Activities.MainActivity.publish;
import static nuce.tatv.smarthome.Activities.MainActivity.subcribe;

public class ControlFragment extends Fragment implements ToggleButton.OnCheckedChangeListener {
    public static final String TOPPIC = "CAMBIENMUA";
    private static final int REQUEST_CODE_ADD = 111;
    private View vControl;
    private ToggleButton tgbLampInYellow, tgbLampInBlue, tgbLampInRed, tgbLampOut, tgbLempWarning, tgbSpeaker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vControl = inflater.inflate(R.layout.control_fragment, container, false);
        init();
        return vControl;
    }

    private void init() {
//        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Control");

        tgbLampInYellow = vControl.findViewById(R.id.tgbLampInYellow);
        tgbLampInBlue = vControl.findViewById(R.id.tgbLampInBlue);
        tgbLampInRed = vControl.findViewById(R.id.tgbLampInRed);
        tgbLampOut = vControl.findViewById(R.id.tgbLampOut);
        tgbLempWarning = vControl.findViewById(R.id.tgbLampWarning);
        tgbSpeaker = vControl.findViewById(R.id.tgbSpeaker);


        if (checkYellow){
            tgbLampInYellow.setChecked(true);
        }else {
            tgbLampInYellow.setChecked(false);
        }
        if (checkBlue){
            tgbLampInBlue.setChecked(true);
        }else {
            tgbLampInBlue.setChecked(false);
        }
        if (checkRed){
            tgbLampInRed.setChecked(true);
        }else {
            tgbLampInRed.setChecked(false);
        }
        if (checkOut){
            tgbLampOut.setChecked(true);
        }else {
            tgbLampOut.setChecked(false);
        }

        tgbLampInYellow.setOnCheckedChangeListener(this);
        tgbLampInBlue.setOnCheckedChangeListener(this);
        tgbLampInRed.setOnCheckedChangeListener(this);
        tgbLampOut.setOnCheckedChangeListener(this);
        tgbLempWarning.setOnCheckedChangeListener(this);
        tgbSpeaker.setOnCheckedChangeListener(this);


        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Control");
        if (checkConnect){
            subcribe(TOPPIC);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.tgbLampOut:
                if (isChecked){
                    checkOut = true;
                    publish(TOPPIC, "D11");
                }else {
                    checkOut = false;
                    publish(TOPPIC, "D10");
                }
                break;
            case R.id.tgbLampInYellow:
                if (isChecked){
                    checkYellow = true;
                    publish(TOPPIC, "D01");
                }else {
                    checkYellow = false;
                    publish(TOPPIC, "D00");
                }
                break;
            case R.id.tgbLampInBlue:
                if (isChecked){
                    checkBlue = true;
                    publish(TOPPIC, "D61");
                }else {
                    checkBlue = false;
                    publish(TOPPIC, "D60");
                }
                break;
            case R.id.tgbLampInRed:
                if (isChecked){
                    checkRed = true;
                    publish(TOPPIC, "D71");
                }else {
                    checkRed = false;
                    publish(TOPPIC, "D70");
                }
                break;
            case R.id.tgbLampWarning:
                if (isChecked){
                    checkWarning = true;
                    publish(TOPPIC, "D21");
                }else {
                    checkWarning = false;
                    publish(TOPPIC, "D20");
                }
                break;
            case R.id.tgbSpeaker:
                if (isChecked){
                    checkSpeaker = true;
                    publish(TOPPIC, "D81");
                }else {
                    checkSpeaker = false;
                    publish(TOPPIC, "D80");
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itemNote = menu.add(1, R.id.itemAddModule, 1, "Thêm Note");
        itemNote.setIcon(R.drawable.ic_add_module);
        itemNote.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemAddModule:
                startActivityForResult(new Intent(getActivity(), AddModule.class), REQUEST_CODE_ADD);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD){
            if(resultCode == Activity.RESULT_OK){

            }
        }
    }
}
