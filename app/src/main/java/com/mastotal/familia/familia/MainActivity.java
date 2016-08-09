package com.mastotal.familia.familia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mButtonEnviar;
    EditText mEditText;
    TextView resultado;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonEnviar = (Button) findViewById(R.id.btn_enviar);
        mEditText = (EditText) findViewById(R.id.caja_nombre);
        resultado = (TextView) findViewById(R.id.resultado);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);



        mButtonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()){
                    pedirDatos("http://webservice.mastotal.com/webservice_prueba.php");
                }else{
                    Toast.makeText(getApplicationContext(), "Sin conexión", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    //verificar coneccion a internet
    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }//fin verificar conexion

    public void pedirDatos(String uri){
        TareaAsync tareaAsync = new TareaAsync();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri(uri);
        requestPackage.setParam("nombre", mEditText.getText().toString());
        //requestPackage.setParam("animal", "Valor2");
        //requestPackage.setParam("id", "Valor3");
        tareaAsync.execute(requestPackage);
    }

    public void cargarDatos(String datos){
        //resultado.append(datos + "\n");
        resultado.setText("");
        resultado.append(datos);
    }


    private class TareaAsync extends AsyncTask<RequestPackage, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            // Algo para hacer antes de ejecutar
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);

            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null){
                Toast.makeText(MainActivity.this, "No se pudo conectar al webservice", Toast.LENGTH_SHORT).show();
            }

            cargarDatos(result);
            mProgressBar.setVisibility(View.GONE);
            mEditText.setText("");

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            // para modificar la atividad principal mientras ocurre el proceso
        }
    }

}
