package sc.example.firebasestart;


import static sc.example.firebasestart.FBref.refScouters;
import static sc.example.firebasestart.FBref.refPlayers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText passwordEditText;
    private TextView welcomeTextView;
    private FirebaseAuth refAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);

        Button login=findViewById(R.id.btnLogin);
        Button signup=findViewById(R.id.ButtonSignUp);
        Log.d("HOSAM_ERROR","12345");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(view);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this,SignupActivity.class);
               startActivity(intent);
            }
        });
    }


    public void loginUser(View view){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
            return;
        }

        refAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User Logged in successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = refAuth.getCurrentUser();
                    String name = (user != null) ? user.getDisplayName() : "";
                    Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }
                else{
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(MainActivity.this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (refAuth != null) {
            refAuth.signOut();
        }
        super.onDestroy();
    }
}
