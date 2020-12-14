package nuce.tatv.smarthome.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nuce.tatv.smarthome.R;

public class AddModule extends AppCompatActivity implements View.OnClickListener {
    EditText edtName, edtOn, edtOff;
    Button btnAdd, btnCancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_add_layout);
        init();
    }

    private void init() {
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        edtName = findViewById(R.id.edtName);
        edtOn = findViewById(R.id.edtOn);
        edtOff = findViewById(R.id.edtOff);

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:
//                Toast.makeText(AddModule.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }
}
