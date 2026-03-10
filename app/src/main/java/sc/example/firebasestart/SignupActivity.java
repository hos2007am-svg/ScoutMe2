package sc.example.firebasestart;

import static android.content.ContentValues.TAG;
import static sc.example.firebasestart.FBref.refAuth;
import static sc.example.firebasestart.FBref.refPlayers;
import static sc.example.firebasestart.FBref.refScouters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
EditText name,email,password,team,age,position;
Boolean flag=false;
    Switch aSwitch;
    Button buttonSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);
        name=findViewById(R.id.etName);
        age=findViewById(R.id.etAge);
        team=findViewById(R.id.etTeam);
        email=findViewById(R.id.etEmail);
        password=findViewById(R.id.etPassword);
        buttonSignUp=findViewById(R.id.btnRegister);
        position=findViewById(R.id.etPosition);
        aSwitch=findViewById(R.id.switchActive);
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Toast.makeText(this, "אתה נכנס כסקאוטר, אין צורך להכניס גיל, עמדה, קבוצה", Toast.LENGTH_SHORT).show();
                aSwitch.setText("סקאוטר");
                flag=true;
            }
            else {
                Toast.makeText(this, "אתה נכנס כשחקן פעיל", Toast.LENGTH_SHORT).show();
                aSwitch.setText("שחקן פעיל");
                flag=false;
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser(v);
            }
        });
    }
    private void CreateUser(View v){
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        String team1 = team.getText().toString();
        String age1 = age.getText().toString();
        String position1 = position.getText().toString();
        String name1 = name.getText().toString();
        if(flag==true) {

            if (email1.isEmpty() || password1.isEmpty()||name1.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        if(flag==false) {

            if (email1.isEmpty() || password1.isEmpty()||name1.isEmpty()||position1.isEmpty()||team1.isEmpty()||age1.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        refAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "המשתמש נרשם בהצלחה", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = refAuth.getCurrentUser();
                    String name = (user != null) ? user.getDisplayName() : "";

                    if (flag) {
                        Scouter scouter = new Scouter(name1, user.getUid());
                        refScouters.child(scouter.getUid()).setValue(scouter);
                    } else {
                        Player player = new Player(name1, user.getUid(), team1, position1, age1);
                        refPlayers.child(user.getUid()).setValue(player);
                    }

                    Intent intent = new Intent(SignupActivity.this, MainActivity2.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                } else {

                    Exception e = task.getException();
                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        Toast.makeText(SignupActivity.this, "הסיסמה חלשה מדי", Toast.LENGTH_SHORT).show();

                    } else if (e instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(SignupActivity.this, "האימייל כבר קיים במערכת", Toast.LENGTH_SHORT).show();

                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(SignupActivity.this, "אימייל לא תקין", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SignupActivity.this, "הרשמה נכשלה: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
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