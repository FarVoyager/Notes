package com.example.notes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class DataSourceFireBaseImpl extends BaseDataSource {

    private final static String COLLECTION_NOTES = "CollectionNotes";
    private final static String TAG = "DataSourceFireBaseImpl";

    private final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private final CollectionReference mCollection = mStore.collection(COLLECTION_NOTES);

    //singleTon
    private volatile static DataSourceFireBaseImpl sInstance;

    public static DataSourceFireBaseImpl getInstance() {
        DataSourceFireBaseImpl instance = sInstance;
        if (instance == null) {
            synchronized (DataSourceFireBaseImpl.class) {
                if (sInstance == null) {

                    instance = new DataSourceFireBaseImpl();
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    private DataSourceFireBaseImpl() {
        mCollection.orderBy("Date", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(this::onFetchComplete)
                .addOnFailureListener(this::onFetchFailed);
    }

    private void onFetchComplete(Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            LinkedList<Note> data = new LinkedList<>();
            System.out.println(task.getResult() + " HUI B");
            System.out.println(data + "NICE HUI");

            for (QueryDocumentSnapshot document : task.getResult()) {
                System.out.println(document + " HUI V");
                data.add(new DataFromFirestore(document.getId(), document.getData()));

                document.getId();
                document.getData();
            }
            mData.clear();
            mData.addAll(data);
            System.out.println(data + "NICE HUI");

            data.clear();
            notifyDataSetChanged();

        }
    }

    private void onFetchFailed(Exception e) {
        Log.e(TAG, "Fetch failed", e);
    }

    @Override
    public void add(@NonNull Note data) {
        final DataFromFirestore note;
        if (data instanceof DataFromFirestore) {
            note = (DataFromFirestore) data;
        } else {
            note = new DataFromFirestore(data);
        }
        mData.add(note);
        mCollection.add(note.getFields()).addOnSuccessListener(documentReference -> {
            note.setId(documentReference.getId());
        });
    }

    @Override
    public void remove(int position) {
        String id = mData.get(position).getId();
        mCollection.document(id).delete();
        super.remove(position);

    }
}
