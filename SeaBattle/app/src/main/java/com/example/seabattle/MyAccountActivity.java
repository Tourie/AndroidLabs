package com.example.seabattle;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seabattle.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MyAccountActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView img;
    Button choose, upload, save;
    EditText nickname;
    ProgressBar progressBar;
    TextView winsTextView;
    TextView lossesTextView;
    TextView winrateTextView;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        img = findViewById(R.id.user_image);
        choose = findViewById(R.id.choose_file);
        upload = findViewById(R.id.upload);
        save = findViewById(R.id.save);
        nickname = findViewById(R.id.name);
        progressBar = findViewById(R.id.progress_upload);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("profiles");
        mAuth = FirebaseAuth.getInstance();

        winsTextView = findViewById(R.id.wins);
        lossesTextView =  findViewById(R.id.losses);
        winrateTextView = findViewById(R.id.winrate);

        int wins,total;
        String str = mAuth.getCurrentUser().getUid();
        mDatabaseRef.child(str).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.total == null){
                    user.total = 0;
                }
                if(user.wins == null){
                    user.wins = 0;
                }
                if(user != null){
                    int losses = user.total - user.wins;
                    lossesTextView.setText(String.valueOf(losses));
                    winsTextView.setText(String.valueOf(user.wins));
                    winrateTextView.setText(Double.toString(Math.floor((double)user.wins / (double)user.total * 10000)/100).concat("%"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

    upload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(MyAccountActivity.this, "Загрузка в процессе", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }
        }
    });

    save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = nickname.getText().toString().trim();
            if (name.equals("")){
                Toast.makeText(MyAccountActivity.this, "Введите имя", Toast.LENGTH_SHORT).show();
            }
            else{
                mDatabaseRef.child(mAuth.getUid()).child("nickname").setValue(name);
                Toast.makeText(MyAccountActivity.this, "Данные изменены", Toast.LENGTH_SHORT).show();
            }
        }
    });

    mDatabaseRef.child(mAuth.getUid()).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (snapshot.getKey().equals("nickname")){
                nickname.setText(snapshot.getValue().toString());
            }

            if (snapshot.getKey().equals("ImagePath")){
                Picasso.get().load(snapshot.getValue().toString()).into(img);
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}

private void openFileChooser() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(intent, PICK_IMAGE_REQUEST);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null) {
        mImageUri = data.getData();
        Picasso.get().load(mImageUri).into(img);
    }
}

private String getFileExtension(Uri uri) {
    ContentResolver cR = getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cR.getType(uri));
}

private void uploadFile(){
    if (mImageUri != null) {
        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

        mUploadTask = fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        }, 500);

                        Toast.makeText(MyAccountActivity.this, "Загружено успешно", Toast.LENGTH_LONG).show();


                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String upload = task.getResult().toString();
                                mDatabaseRef.child(mAuth.getUid()).child("ImagePath").setValue(upload);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    }
                });
    } else {
        Toast.makeText(this, "Файл не выбран", Toast.LENGTH_SHORT).show();
    }
}

}

