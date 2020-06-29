package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SMS = 1;
    private static String EXTRA_PERMISSAO = "pediu";
    private static final String ACAO_ENVIADO = "sms_enviado";
    private static final String ACAO_ENTREGUE = "sms_entregue";

    private EditText mEdtNumero;
    private EditText mEdtMensagem;
    private EnvioSmsReceiver mReceiver;
    private boolean mPediuPermissao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdtNumero = findViewById(R.id.edtNumeroTelefone);
        mEdtMensagem = findViewById(R.id.edtMenssagem);

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            Toast.makeText(this, "Dispositivo não suporta SMS", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (savedInstanceState != null) {
            mPediuPermissao = savedInstanceState.getBoolean(EXTRA_PERMISSAO);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(EXTRA_PERMISSAO, mPediuPermissao);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
            mPediuPermissao = true;
        }
        mReceiver = new EnvioSmsReceiver(ACAO_ENVIADO, ACAO_ENTREGUE);
        registerReceiver(mReceiver, new IntentFilter(ACAO_ENVIADO));
        registerReceiver(mReceiver, new IntentFilter(ACAO_ENTREGUE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean success = true;
        for (int i =0; i< permissions.length; i++){
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                success = false;
                break;
            }
        }
        mPediuPermissao = false;
        if (!success){
            Toast.makeText(this, "Você precisa aceitar as permissões", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void enviarSmsClick(View view) {
        PendingIntent pitEnviado = PendingIntent.getBroadcast(this, 0, new Intent(ACAO_ENVIADO), 0);
        PendingIntent pitEntregue = PendingIntent.getBroadcast(this, 0, new Intent(ACAO_ENTREGUE), 0);

        SmsManager smsManager = SmsManager.getDefault();
        /*Os dois últimos parâmetros desse método são um objeto PendingIntent para saber quando a mensagen foi enviada e quando foi recebida pelo destinatário*/
        smsManager.sendTextMessage(mEdtNumero.getText().toString(), null, mEdtMensagem.getText().toString(), pitEnviado, pitEntregue);
    }
}