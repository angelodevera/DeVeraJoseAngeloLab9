package devera.angelo.jose.deverajoseangelolab9;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference points;
    EditText etFname, etage, etgender;
    TextView tvFname,tvage,tvgender;
    ArrayList<String> keyList;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        points = db.getReference("points");
        etFname = findViewById(R.id.etFname);
        etage = findViewById(R.id.etage);
        etgender = findViewById(R.id.etgender);
        tvFname = findViewById(R.id.tvFname);
        tvage = findViewById(R.id.tvage);
        tvgender = findViewById(R.id.tvgender);

        keyList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        points.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ss : dataSnapshot.getChildren()){
                    keyList.add(ss.getKey());
                }
                Toast.makeText(MainActivity.this,dataSnapshot.child( keyList.get(0)).child("fullName").getValue().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addRecord (View v){
        String fname = etFname.getText().toString().trim();
        String gender = etgender.getText().toString().trim();
        Integer age = Integer.parseInt(etage.getText().toString().trim());
        String key = points.push().getKey();
        Person person = new Person(fname, gender, age);
        points.child(key).setValue(person);
        keyList.add(key);
//        Toast.makeText(this, "Record Inserted...", Toast.LENGTH_LONG).show();

    }


    public void search(View v){
        String fname = etFname.getText().toString().trim();
        points.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (String ss : keyList) {
//                    Person user = dataSnapshot.child(ss).child().getValue(Person.class);
                        String fname = dataSnapshot.child(ss).child("fullName").getValue().toString();

                    Toast.makeText(MainActivity.this, fname, Toast.LENGTH_LONG).show();
                    if(fname.equals(etFname.getText().toString().trim())){
                        tvFname.setText(dataSnapshot.child(ss).child("fullName").getValue().toString());
                        tvgender.setText(dataSnapshot.child(ss).child("gender").getValue().toString());
                        tvage.setText(dataSnapshot.child(ss).child("age").getValue().toString());
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}