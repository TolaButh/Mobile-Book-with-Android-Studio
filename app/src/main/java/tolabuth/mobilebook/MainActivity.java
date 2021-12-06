package tolabuth.mobilebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import tolabuth.mobilebook.adapter.ContactAdapter;
import tolabuth.mobilebook.dao.SQLiteHelper;
import tolabuth.mobilebook.model.Contact;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private RecyclerView rcvContact;
    private ImageView imgvData;
    private TextView tvData;
    private SQLiteHelper db;
    private List<Contact> contacts;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLiteHelper(MainActivity.this);
        contacts = db.select();

        mactView();
        displayContact();
        contacts = db.select();
        adapter = new ContactAdapter(MainActivity.this, contacts);
        rcvContact.setAdapter(adapter);
        rcvContact.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayContact() {
        contacts = db.select();
        if (contacts.size()>0){
            imgvData.setVisibility(View.GONE);
            tvData.setVisibility(View.GONE);
        }else {
            imgvData.setVisibility(View.VISIBLE);
            tvData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void mactView() {
        fabAdd = findViewById(R.id.fab_add);
        rcvContact = findViewById(R.id.rcv_contact);
        imgvData = findViewById(R.id.imgv_data);
        tvData = findViewById(R.id.tv_data);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_all){
            ConfirmDeleteContact();

        }else if(item.getItemId() == R.id.menu_search){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    private void ConfirmDeleteContact() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Confirmed Delete Dialog");
        alert.setMessage("Do you want to delete");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db = new SQLiteHelper(MainActivity.this);
                db.delete();
                Toast.makeText(MainActivity.this, "Delete all data contact Successfull", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.create().show();
    }
}