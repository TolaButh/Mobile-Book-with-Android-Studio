package tolabuth.mobilebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import tolabuth.mobilebook.adapter.ContactAdapter;
import tolabuth.mobilebook.dao.SQLiteHelper;
import tolabuth.mobilebook.model.Contact;

public class SearchActivity extends AppCompatActivity {
    private MaterialButton btnSearch;
    private AutoCompleteTextView edtSearch;
    private RecyclerView rcvSearch;
    private SQLiteHelper db;
    private ContactAdapter adapter;
    private List<Contact> contacts;
    // use with auto complete
    private String[] list;
    private ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = new SQLiteHelper(SearchActivity.this);

        matchView();
        loadContactsToArray();
        arrayAdapter = new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_dropdown_item_1line,list
                );

        edtSearch.setAdapter(arrayAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = edtSearch.getText().toString();
                contacts = db.selectByName(search);

                if (contacts.size()>0){
                    adapter = new ContactAdapter(SearchActivity.this, contacts);
                    rcvSearch.setAdapter(adapter);
                    rcvSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                }else {
                    Toast.makeText(SearchActivity.this, "No Result",Toast.LENGTH_SHORT).show();

                }
                edtSearch.setText("");
                hideSoftkeyboard(SearchActivity.this);


            }
        });
    }

    private void loadContactsToArray() {
        contacts = db.select();
        int size = contacts.size();
        list = new String[size];
        for (int i =0 ; i<size; i++){
            list[i] = contacts.get(i).getName();
        }
    }

    public static void hideSoftkeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view =activity.getCurrentFocus();
        if(view == null){
            view = new View(activity);

        }
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

    }

    private void matchView() {
        btnSearch= findViewById(R.id.btn_search);
        edtSearch = findViewById(R.id.edt_search);
        rcvSearch = findViewById(R.id.rcv_search);
    }
}