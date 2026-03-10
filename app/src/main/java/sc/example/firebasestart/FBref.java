package sc.example.firebasestart;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBref {

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static FirebaseFirestore FBFS = FirebaseFirestore.getInstance();
    
    // הגדרה מפורשת של ה-Bucket כדי למנוע שגיאות מיקום
    public static FirebaseStorage FBST = FirebaseStorage.getInstance("gs://scout-me-30591.firebasestorage.app");
    
    public static CollectionReference refImages = FBFS.collection("Images");
    public static CollectionReference refVideos = FBFS.collection("Videos");
    public static DatabaseReference refScouters = FBDB.getReference("Scouter");
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();
    public static DatabaseReference refPlayers = FBDB.getReference("Player");
    
    public static StorageReference refStorage = FBST.getReference();
    public static StorageReference refVideosStorage = refStorage.child("Videos");
    public static StorageReference refPhotos = refStorage.child("Photos");

    public static FirebaseStorage storage = FBST;
}
