package mestrado.ipg.condomastercrud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText userTV, passTV;
    Button loginBTN;
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userTV = findViewById(R.id.user);
        passTV = findViewById(R.id.password);
        loginBTN = findViewById(R.id.login_btn);

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(String.valueOf(userTV.getText()), String.valueOf(passTV.getText()));
            }
        });

        registerReceiver();

    }

    private void login(String username, String password) {

        HashMap<String, String> params = new HashMap<>();
        params.put("urlStr", "https://bd.ipg.pt:5500/ords/bda_1701887/user/login");
        params.put("_uri", "/user/login");
        params.put("username", username);
        params.put("password", password);
        params.put("wherefrom", "login");

        Intent intent = new Intent(LoginActivity.this, BackgroundPostServiceAuth.class);
        intent.putExtra("ParamsMAP", params);
        startService(intent);
    }

    private void registerReceiver() {

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String data = intent.getStringExtra("data");
                HashMap<String, String> hashParams = (HashMap<String, String>) intent.getSerializableExtra("hashParams");
                String username = null;
                String password = null;

                for (Map.Entry<String, String> entry : hashParams.entrySet()) {
                    if (entry.getKey().equals("username")) {
                        username = entry.getValue();
                    } else if (entry.getKey().equals("password")) {
                        password = entry.getValue();
                    }
                }

                loginTerminado(data, username, password);
                intent.getBundleExtra("Location");
            }
        };

        LocalBroadcastManager.getInstance(LoginActivity.this).registerReceiver(
                mMessageReceiver, new IntentFilter("ServiceLogin"));

    }

    private void loginTerminado(String data, String username, String password) {

        JSONObject json;
        String api_key;
        String email;
        String userID;

        try {
            json = new JSONObject(data);
            api_key = json.getString("api-key");

            email = json.getString("email");
            userID = json.getString("user_id");

            user.setApi_key(api_key);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setUser_id(userID);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
