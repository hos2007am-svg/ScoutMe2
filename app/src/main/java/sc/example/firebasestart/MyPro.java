package sc.example.firebasestart;
import com.google.firebase.firestore.FirebaseFirestore;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.VideoView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import com.google.firebase.firestore.Blob;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MyPro extends AppCompatActivity {

    VideoView videoView;
    Button btnRecordVideo;

    static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final int REQUEST_PICK_IMAGE = 301;
    private static final int REQUEST_PICK_VIDEO = 302;
    RecyclerView recyclerView;
    MediaAdapter mediaAdapter;
    List<Map<String, Object>> mediaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pro);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mediaAdapter = new MediaAdapter(this, mediaList);
        recyclerView.setAdapter(mediaAdapter);
        loadMedia();
        videoView = findViewById(R.id.videoView);
        btnRecordVideo = findViewById(R.id.btnVideo);
    }

    private void loadMedia() {
        mediaList.clear();
        String currentUid = FBref.refAuth.getCurrentUser().getUid();

        FBref.refImages.whereEqualTo("uid", currentUid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Blob blob = doc.getBlob("imageData");
                            if (blob != null) {
                                Map<String, Object> item = new HashMap<>();
                                item.put("type", "image");
                                item.put("data", blob.toBytes());
                                mediaList.add(item);
                            }
                        }
                        FBref.refVideos.whereEqualTo("uid", currentUid).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots2) {
                                        for (DocumentSnapshot doc : queryDocumentSnapshots2) {
                                            Blob videoBlob = doc.getBlob("videoData");
                                            if (videoBlob != null) {
                                                Map<String, Object> item = new HashMap<>();
                                                item.put("type", "video");
                                                item.put("data", videoBlob.toBytes());
                                                mediaList.add(item);
                                            }
                                        }
                                        mediaAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }

    public void pickVideo(View view) {
        Intent si = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(si, REQUEST_PICK_VIDEO);
    }

    public void gallery(View view) {
        Intent si = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(si, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_PICK_IMAGE) uploadImage(data.getData());
            if (requestCode == REQUEST_PICK_VIDEO) uploadVideo(data.getData());
        }
    }

    private void uploadVideo(Uri videoUri) {
        if (videoUri != null) {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading Video to Firestore...");
            pd.show();

            try {
                InputStream inputStream = getContentResolver().openInputStream(videoUri);
                byte[] videoBytes = getBytes(inputStream);

                if (videoBytes.length > 1000000) { // מגבלת 1MB של Firestore
                    pd.dismiss();
                    Toast.makeText(this, "Video too large for Firestore! (Max 1MB)", Toast.LENGTH_LONG).show();
                    return;
                }

                String fileName = UUID.randomUUID().toString() + ".mp4";
                String currentUid = FBref.refAuth.getCurrentUser().getUid();
                Map<String, Object> videoMap = new HashMap<>();
                videoMap.put("videoName", fileName);
                videoMap.put("videoData", Blob.fromBytes(videoBytes));
                videoMap.put("uid", currentUid);

                FBref.refVideos.document(fileName).set(videoMap)
                        .addOnSuccessListener(aVoid -> {
                            pd.dismiss();
                            loadMedia();
                        });
            } catch (IOException e) {
                pd.dismiss();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(Uri imageUri) {
        if (imageUri != null) {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading Image...");
            pd.show();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] imageBytes = baos.toByteArray();

                String currentUid = FBref.refAuth.getCurrentUser().getUid();
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("imageData", Blob.fromBytes(imageBytes));
                imageMap.put("uid", currentUid);
                FBref.refImages.document(UUID.randomUUID().toString()).set(imageMap)
                        .addOnSuccessListener(aVoid -> {
                            pd.dismiss();
                            loadMedia();
                        });
            } catch (IOException e) {
                pd.dismiss();
            }
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
