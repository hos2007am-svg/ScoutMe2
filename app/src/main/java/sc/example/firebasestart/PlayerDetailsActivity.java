package sc.example.firebasestart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDetailsActivity extends AppCompatActivity {

    private TextView tvName, tvAge, tvPosition, tvTeam;
    private RecyclerView recyclerMedia;
    private List<Map<String, Object>> mediaList = new ArrayList<>();
    private MediaAdapter mediaAdapter;
    private String playerUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        tvName     = findViewById(R.id.tvDetailName);
        tvAge      = findViewById(R.id.tvDetailAge);
        tvPosition = findViewById(R.id.tvDetailPosition);
        tvTeam     = findViewById(R.id.tvDetailTeam);
        recyclerMedia = findViewById(R.id.recyclerMedia);

        String name     = getIntent().getStringExtra("name");
        String age      = getIntent().getStringExtra("age");
        String position = getIntent().getStringExtra("position");
        String team     = getIntent().getStringExtra("team");
        playerUid       = getIntent().getStringExtra("uid");

        tvName.setText(name);
        tvAge.setText("גיל: " + age);
        tvPosition.setText("עמדה: " + position);
        tvTeam.setText("קבוצה: " + team);

        recyclerMedia.setLayoutManager(new GridLayoutManager(this, 3));
        mediaAdapter = new MediaAdapter(this, mediaList);
        recyclerMedia.setAdapter(mediaAdapter);

        loadMedia();
    }

    private void loadMedia() {
        FBref.refImages.whereEqualTo("uid", playerUid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        for (DocumentSnapshot doc : snapshots) {
                            Blob blob = doc.getBlob("imageData");
                            if (blob != null) {
                                Map<String, Object> item = new HashMap<>();
                                item.put("type", "image");
                                item.put("data", blob.toBytes());
                                mediaList.add(item);
                            }
                        }
                        FBref.refVideos.whereEqualTo("uid", playerUid).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot snapshots2) {
                                        for (DocumentSnapshot doc : snapshots2) {
                                            Blob blob = doc.getBlob("videoData");
                                            if (blob != null) {
                                                Map<String, Object> item = new HashMap<>();
                                                item.put("type", "video");
                                                item.put("data", blob.toBytes());
                                                mediaList.add(item);
                                            }
                                        }
                                        mediaAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }
}