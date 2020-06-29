package com.example.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    /*Atualmente as mensages SMS utilizam o formato PDU(protocol Description Unit), que permite a utilização de vários encondings
    * Por isso ao receber uma mensagem lemos a informação extra da intent chamada "pdus". Percorremos um array de bytes e passamos
    * cada array para o método createFromPdu da classe SmsMessage, podemos obter as informações da messagem como o remetente e
    * o texto da menssagem*/
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage sms = getMessagesFromIntent(intent)[0];
        String telefone = sms.getOriginatingAddress();
        String mensagem = sms.getMessageBody();

        Toast.makeText(context, "Mensagem recebida de "+ telefone+ " - "+ mensagem, Toast.LENGTH_LONG).show();
    }
    public SmsMessage[] getMessagesFromIntent(Intent intent){
        Object[] pdusExtras = (Object[])intent.getSerializableExtra("pdus");
        SmsMessage[] messages = new SmsMessage[pdusExtras.length];
        for (int i =0; i< pdusExtras.length;i++){
            messages[i] = SmsMessage.createFromPdu((byte[]) pdusExtras[i]);
        }
        return messages;
    }
}
