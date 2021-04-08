package com.e.simplegrocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.e.simplegrocery.Adapter.ItemHolder;
import com.e.simplegrocery.R;
import com.e.simplegrocery.databinding.ActivityItems2Binding;
import com.e.simplegrocery.model.Items;
import com.e.simplegrocery.utilities.Utility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ItemsActivity2 extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Items, ItemHolder> adapter;
    private FirebaseRecyclerOptions<Items> options;
    ActivityItems2Binding items2Binding;
    private String cName , catId;

    private ShimmerFrameLayout ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items2Binding = ActivityItems2Binding.inflate(getLayoutInflater());
        setContentView(items2Binding.getRoot());

        catId= getIntent().getStringExtra("CID");
        cName = getIntent().getStringExtra("NAME");


        initialize();
        setupWidgets();
    }

    private void initialize() {
        databaseReference = FirebaseDatabase.getInstance().getReference("categories").child(catId);
        loadAllitems("");
        items2Binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    loadAllitems("");
                }else{
                    loadAllitems(editable.toString());
                }

            }
        });
        items2Binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemsActivity2.this,CartActivity.class));
            }
        });
    }

    private void setupWidgets() {
        items2Binding.welcomeText.setText(Utility.capitalize(cName));


        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        items2Binding.recyclerview.setLayoutManager(llmPlace);
    }

    private void loadAllitems(String s) {
        items2Binding.shimmerll.startShimmer();
        items2Binding.shimmerll.showShimmer(true);
        items2Binding.shimmerll.setVisibility(View.VISIBLE);

        Query query =databaseReference.orderByChild("fname").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Items>().setQuery(query,Items.class).build();
        adapter=new FirebaseRecyclerAdapter<Items, ItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Items model) {
                holder.textView.setText(Utility.capitalize(model.getFname()));
                holder.textView1.setText("Rs "+model.getFprice());
                Glide.with(ItemsActivity2.this).load(model.getFimage()).into(holder.imageView);
                //    holder.textView2.setText("Quantity "+model.getFquantity());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = getRef(holder.getAdapterPosition()).getKey();
                        Intent intent = new Intent(ItemsActivity2.this,ItemDetailsActivity.class);
                        intent.putExtra("ID",id);
                        intent.putExtra("NAME",model.getFname());
                        intent.putExtra("DESC",model.getFdesc());
                        intent.putExtra("PRICE",model.getFprice());
                        intent.putExtra("IMAGE",model.getFimage());
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                        contextMenu.setHeaderTitle("Select Action");
                        MenuItem menuItem =contextMenu.add(Menu.NONE,1,1,"Delete");
                        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                String itemId = getRef(holder.getAdapterPosition()).getKey();
                                assert itemId != null;
                                databaseReference.child(itemId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Item deleted successfully",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(ItemsActivity2.this).inflate(R.layout.item_card,parent,false);
                return new ItemHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();


                // using facebook library
                if(items2Binding.shimmerll.isShimmerVisible()){
                    items2Binding.shimmerll.stopShimmer();
                    items2Binding.shimmerll.setVisibility(View.GONE);
                }

                if(getItemCount()!=0){
                    items2Binding.imageNot.setVisibility(View.GONE);
                }
                else{
                    items2Binding.imageNot.setVisibility(View.VISIBLE);
                }
            }
        };
        adapter.startListening();
        items2Binding.recyclerview.setAdapter(adapter);
    }

}