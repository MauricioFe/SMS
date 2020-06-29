package com.example.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class EnvioSmsReceiver extends BroadcastReceiver {
    private static  String ACAO_ENVIADO = "";
    private static  String ACAO_ENTREGUE = "";

    public EnvioSmsReceiver(String enviado, String entregue) {
        ACAO_ENTREGUE = enviado;
        ACAO_ENTREGUE = entregue;
    }

    /*Aqui verificamos se as ações  foram bem sucedidas checando se o retorno do método getResultCode é igual a result_ok.
    * Em caso de positivo, checamos qual foi a ação e mandamos a mensagem apropiada.
    * A intent do segundo parâmetro serve para indicar o caminho de um servidor para encaminhar o SMS. Esse parâmetro é muito útil para testes,
    * pois algumas operadoras cobram pelo envio excedente de mensagens de texo. Então podemos definir um servidor que interceptará a mensagem e encaminhará
    * a mensagem para o destino sem necessáriamente passar pela operadora.*/
    @Override
    public void onReceive(Context context, Intent intent) {
        String mensagem = null;
        String acao = intent.getAction();
        int resultado = getResultCode();
        if (resultado == Activity.RESULT_OK){
            if (ACAO_ENVIADO.equals(acao)){
                mensagem = "Enviado com sucesso";
            }else if (ACAO_ENTREGUE.equals(acao)){
                mensagem ="entregue com sucesso";
            }
        }else{
            if (ACAO_ENVIADO.equals(acao)){
                mensagem = "Falha ao enviar "+ resultado;
            }else if (ACAO_ENTREGUE.equals(acao)) {
                mensagem = "Falha ao entregar " + resultado;
            }
        }
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }
}
