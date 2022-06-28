package com.example.emprestaai.DAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImagemDAO {
        public Map adicionarImagemDB(byte[] imagem, String nome){
            final String[] caminho = {""};
            String idImagem = UUID.randomUUID().toString();
            String path = "objetos/"+ idImagem + ".png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference sr = storage.getReference(path);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("Nome:",nome).build();

            UploadTask uploadTask = sr.putBytes(imagem,metadata);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri url = taskSnapshot.getUploadSessionUri();
                    caminho[0] = url.toString();
                }
            });
            Map valores = new HashMap<>();
            valores.put("caminho",caminho);
            valores.put("idImagem",idImagem);
            return valores;
        }

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
