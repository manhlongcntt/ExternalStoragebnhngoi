package com.example.externalstorage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnluu, btndoc;
    private TextView tvData;
    private final String fileName = "NguyenTheLong.com";
    private final String conten   = "NguyenTheLong.com blog lập trình";
    private final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndRequestPermissions();
        setContentView(R.layout.activity_main);
        btndoc = findViewById(R.id.btn_docData);
        btnluu = findViewById(R.id.btn_luu);
        tvData = findViewById(R.id.tv_data);
        btndoc.setOnClickListener(this);
        btnluu.setOnClickListener(this);
    }
    private void checkAndRequestPermissions() {
        // check quyền lưu đọc file
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_docData:
                readData();
                break;
            case R.id.btn_luu:
                //saveData();
                saveData2();
                break;
            default:
                break;
        }
    }
    public void saveData(){
        if (isExternalStorageReadable()){
            FileOutputStream fileOutputStream = null;
            File file;
            try {
                file = new File(Environment.getExternalStorageDirectory(),fileName);
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(conten.getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Can not save file", Toast.LENGTH_SHORT).show();
        }

    }
    public void saveData2(){
        FileOutputStream fileOutputStream = null;
        File file;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(conten.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readData(){
        BufferedReader bf = null;
        File file = null;

        try {
            file = new File(Environment.getExternalStorageDirectory(),fileName);
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ( (line = bf.readLine()) != null){
                buffer.append(line);
            }
            tvData.setText(buffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isExternalStorageReadable() {
        //kiểm tra xem dung lượng bộ nhớ có đủ để lưu file vào ko
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
