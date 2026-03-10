package sc.example.firebasestart;

import static sc.example.firebasestart.FBref.refPlayers;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 101;
    private static final long TWENTY_FOUR_HOURS = 24L * 60 * 60 * 1000; // 24 שעות במילישניות

    private RecyclerView recyclerViewPlayers;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;
    private Button btnMyProfile;
    private Button btnAI;
    private Button btnFilter;
    private SwitchCompat mySwitch;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initViews();
        setupRecyclerView();
        loadPlayers();
        setupButtons();
        setupSwitch();

        NotificationHelper.createNotificationChannel(this);
        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    private void initViews() {
        recyclerViewPlayers = findViewById(R.id.recyclerViewPlayers);
        btnMyProfile = findViewById(R.id.btnMyProfile);
        btnAI = findViewById(R.id.btnAI);
        btnFilter = findViewById(R.id.btnFilter);
        mySwitch = findViewById(R.id.mySwitch);
    }

    private void setupSwitch() {
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startRepeating();
                    Toast.makeText(MainActivity2.this, "התראות יומיות הופעלו", Toast.LENGTH_SHORT).show();
                } else {
                    stopRepeating();
                    Toast.makeText(MainActivity2.this, "התראות הופסקו", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView() {
        playerList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(this, playerList);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlayers.setAdapter(playerAdapter);
    }

    private void loadPlayers() {
        refPlayers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Player> tempList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Player player = data.getValue(Player.class);
                    tempList.add(player);
                }
                playerAdapter.updateFullList(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void startRepeating() {
        runnable = new Runnable() {
            @Override
            public void run() {
                NotificationHelper.sendNotification(
                        MainActivity2.this,
                        "תזכורת יומית",
                        "עברו 24 שעות! אל תשכח לבדוק את השחקנים החדשים."
                );
                handler.postDelayed(this, TWENTY_FOUR_HOURS);
            }
        };
        // מפעיל את ההתראה הראשונה מיד (או אחרי 24 שעות אם תשנה את הערך פה)
        handler.postDelayed(runnable, TWENTY_FOUR_HOURS);
    }

    private void stopRepeating() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeating(); // עצירת הטיימר כשהמסך נסגר
    }

    private void setupButtons() {
        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MyPro.class);
                startActivity(intent);
            }
        });

        btnAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment filterFragment = new FilterFragment();
                filterFragment.setFilterListener(new FilterFragment.FilterListener() {
                    @Override
                    public void onFilterApplied(String position, String team, int minAge, int maxAge) {
                        playerAdapter.filter(position, team, minAge, maxAge);
                    }
                });
                filterFragment.show(getSupportFragmentManager(), "filter");
            }
        });
    }
}
