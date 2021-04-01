package com.example.notes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataSourceFireBaseImpl extends BaseDataSource {

    private final static String COLLECTION_NOTES = "CollectionNotes";
    private final static String TAG = "DataSourceFireBaseImpl";

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DATE = "date";

    private final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private final CollectionReference mCollection = mStore.collection(COLLECTION_NOTES);
    private LinkedList<Note> data = new LinkedList<Note>();

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
        mCollection.orderBy("name", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(this::onFetchComplete)
                .addOnFailureListener(this::onFetchFailed);
    }

    private void onFetchComplete(Task<QuerySnapshot> task) {
        data = new LinkedList<>();
        if (task.isSuccessful()) {

            for (QueryDocumentSnapshot document : task.getResult()) {
                data.add(new DataFromFirestore(document.getId(), document.getData()));
                document.getId();
                document.getData();
            }
            mData.clear();
            mData.addAll(data);
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

    @Override
    public void update(@NonNull Note data) {
        String id = data.getId();

        if (id != null) {
            int idx = 0;
            for (Note note : mData) {
                if (id.equals(note.getId())) {

                    note.setName(data.getName());
                    note.setDescription(data.getDescription());
                    note.setDate(data.getDate());
                    notifyUpdated(idx);

                    mCollection.document(id).update(FIELD_NAME, note.getName(), FIELD_DESCRIPTION, note.getDescription(),
                            FIELD_DATE, note.getDate());
                    super.update(note);
                    return;
                }
                idx++;
            }
        }
    }

}
