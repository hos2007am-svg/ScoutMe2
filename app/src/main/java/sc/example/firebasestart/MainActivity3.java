package sc.example.firebasestart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3 extends AppCompatActivity {
    Button button;
    EditText editText;
    TextView textView;
    GeminiManager geminiManager=GeminiManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Question1 = editText.getText().toString();
                if (Question1.isEmpty()) {
                    textView.setText("Please enter a question");
                    return;
                }

                textView.setText("טוען....");

                String prompt = " answer this question in football with simple words and less than 50 words: how to improve in " + Question1;
                geminiManager.sentTextPrompt(prompt, new GeminiCallback() {
                    @Override
                    public void onSuccess(String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("HOSAM_ERROR",result);
                                textView.setText(result);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("HOSAM_ERROR",error.getMessage());
                                textView.setText("Error: " + error.getMessage());
                            }
                        });
                    }
                });
            }
        });
    }
}
