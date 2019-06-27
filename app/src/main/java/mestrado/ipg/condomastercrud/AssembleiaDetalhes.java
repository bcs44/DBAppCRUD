package mestrado.ipg.condomastercrud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class AssembleiaDetalhes extends Activity {

    int idAssembleia;
    DBAdapter datasource;
    Assembleia assembleia;
    EditText txtuser, txtDate, txtPlaceID, txtTitulo, txtDesc;
    Button btVoltar, btEliminar, btEditar, btEnviarBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_assembleia);

        txtuser = findViewById(R.id.txtuser);
        txtDate = findViewById(R.id.txtDate);
        txtPlaceID = findViewById(R.id.txtPlaceID);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDesc = findViewById(R.id.txtDesc);

        btEliminar = findViewById(R.id.btEliminar);
        btVoltar = findViewById(R.id.btmenu);
        btEditar = findViewById(R.id.btEditar);
        btEnviarBD = findViewById(R.id.btEnviarBD);

        carregaDetalhesAssembleia();
        registerReceiver();

        btVoltar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder dialogo = new AlertDialog.Builder(AssembleiaDetalhes.this);
                dialogo.setTitle("Aviso");
                dialogo.setMessage("Eliminar Contacot?");
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                dialogo.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        datasource.open();
                        datasource.EliminaAssembleia(idAssembleia);
                        datasource.close();
                        finish();
                    }
                });
                dialogo.show();
            }
        });

        btEditar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (btEditar.getText().equals("Editar")) {

                    txtuser.setFocusable(true);
                    txtuser.setFocusableInTouchMode(true);
                    txtuser.requestFocus();
                    txtDate.setFocusable(true);
                    txtDate.setFocusableInTouchMode(true);
                    txtDate.requestFocus();
                    txtPlaceID.setFocusable(true);
                    txtPlaceID.setFocusableInTouchMode(true);
                    txtPlaceID.requestFocus();
                    txtTitulo.setFocusable(true);
                    txtTitulo.setFocusableInTouchMode(true);
                    txtTitulo.requestFocus();
                    txtDesc.setFocusable(true);
                    txtDesc.setFocusableInTouchMode(true);
                    txtDesc.requestFocus();

                    btEditar.setText("Gravar");
                } else if (btEditar.getText().equals("Gravar")) {

                    btEditar.setText("Editar");
                    datasource.open();

                    String USER_ID = String.valueOf(txtuser.getText());
                    String MEETING_DATE = String.valueOf(txtDate.getText());
                    String PLACE_ID = String.valueOf(txtPlaceID.getText());
                    String DESCRIPTION = String.valueOf(txtDesc.getText());
                    String TITLE = String.valueOf(txtTitulo.getText());

                    datasource.EditaAssembleia(USER_ID, MEETING_DATE, PLACE_ID, DESCRIPTION, TITLE, idAssembleia);
                    datasource.close();

                    AlertDialog.Builder dialogo = new
                            AlertDialog.Builder(AssembleiaDetalhes.this);
                    dialogo.setTitle("Aviso");
                    dialogo.setMessage("Assembleia: " + assembleia.getTITLE() + " Editada");
                    dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    dialogo.show();
                }
            }
        });


        btEnviarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senDataBD();
            }
        });
    }

    private void carregaDetalhesAssembleia() {
        idAssembleia = getIntent().getIntExtra("idAssembleia", 0);

        datasource = new DBAdapter(this);
        datasource.open();
        assembleia = datasource.getAssembleia(idAssembleia);
        datasource.close();

        txtuser.setText(assembleia.getUSER_ID());
        txtDate.setText(assembleia.getMEETING_DATE());
        txtPlaceID.setText(assembleia.getPLACE_ID());
        txtTitulo.setText(assembleia.getTITLE());
        txtDesc.setText(assembleia.getDESCRIPTION());
    }

    private void registerReceiver() {

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String data = intent.getStringExtra("data");
                String wherefrom = intent.getStringExtra("wherefrom");

                switch (wherefrom) {
                    case "getPlacesToMarcAssembleia":
                        context.stopService(new Intent(context, BackgroundGetServiceAuth.class));
                        //    dealWithSpinners(data);
                        //TODO
                        break;
                    case "postAssembleia":
                        context.stopService(new Intent(context, BackgroundGetServiceAuth.class));

                        AlertDialog.Builder dialogo = new
                                AlertDialog.Builder(AssembleiaDetalhes.this);
                        dialogo.setTitle("Aviso");
                        dialogo.setMessage("Assembleia Guardada: " + assembleia.getTITLE());
                        dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                        dialogo.show();

                        break;
                }
                intent.getBundleExtra("Location");
            }
        };

        LocalBroadcastManager.getInstance(AssembleiaDetalhes.this).registerReceiver(
                mMessageReceiver, new IntentFilter("ServiceMarcAssembleia"));
    }


    private void senDataBD() {

        HashMap<String, String> params = new HashMap<>();
        String url = "https://bd.ipg.pt:5500/ords/bda_1701887/meeting/insert";
        String _uri = "/meeting/insert";
        params.put("urlStr", url);
        params.put("_uri", _uri);

        params.put("meeting_date", String.valueOf(txtDate.getText()));
        params.put("place_id", String.valueOf(txtPlaceID.getText()));
        params.put("description", String.valueOf(txtDesc.getText()));
        params.put("title", String.valueOf(txtTitulo.getText()));
        params.put("user_id", String.valueOf(txtuser.getText()));

        params.put("wherefrom", "postAssembleia");

        new sendPost().execute(params);

    }

    private class sendPost extends AsyncTask<HashMap, HashMap, String> {

        @Override
        protected String doInBackground(HashMap... args) {

            HashMap<String, String> hashMap = args[0];

            Intent intent = new Intent(AssembleiaDetalhes.this, BackgroundPostServiceAuth.class);
            intent.putExtra("ParamsMAP", hashMap);
            startService(intent);

            return "done";
        }
    }

}
