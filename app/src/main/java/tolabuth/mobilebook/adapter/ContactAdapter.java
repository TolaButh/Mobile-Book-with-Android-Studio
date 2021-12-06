package tolabuth.mobilebook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tolabuth.mobilebook.MainActivity;
import tolabuth.mobilebook.R;
import tolabuth.mobilebook.UpdateAndDeleteActivity;
import tolabuth.mobilebook.model.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{
    private Context context;
    private List<Contact> contacts;


    private RecyclerView mRecyclerV;
    public ContactAdapter(Context context, List<Contact>contacts){
        this.context = context;
        this.contacts = contacts;

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Contact contact = contacts.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvMobile.setText(contact.getMobile());
        String image = contact.getImage();
        holder.img_contect.setImageURI(Uri.parse(image));

        holder.layoutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateAndDeleteActivity.class);
                Contact contact = contacts.get(position);
                intent.putExtra("id", contact.getId());
                intent.putExtra("name",contact.getName());
                intent.putExtra("mobile",contact.getMobile());
                intent.putExtra("image",contact.getImage());
                Log.d("Image ",contact.getImage());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMobile;
        ConstraintLayout layoutContact;
        CircleImageView img_contect;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMobile = itemView.findViewById(R.id.tv_mobile);
            layoutContact = itemView.findViewById(R.id.layout_contact);
            img_contect = itemView.findViewById(R.id.imgv_contact);



        }
    }
}