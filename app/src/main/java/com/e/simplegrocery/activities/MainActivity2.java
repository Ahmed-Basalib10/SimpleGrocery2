package com.e.simplegrocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.e.simplegrocery.Adapter.CategoryAdapter;
import com.e.simplegrocery.R;
import com.e.simplegrocery.databinding.ActivityMain2Binding;
import com.e.simplegrocery.model.Category;
import com.e.simplegrocery.utilities.Utility;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.regex.Matcher;

public class MainActivity2 extends AppCompatActivity {
    private FirebaseRecyclerAdapter<Category, CategoryAdapter> adapter;
    private FirebaseRecyclerOptions<Category> options;
    private DatabaseReference databaseReference;
    ActivityMain2Binding main2Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main2Binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(main2Binding.getRoot());

        setSupportActionBar(main2Binding.toolbar);
        initialize();
        setupWidgets();
        setUpNavigiation();
    }

    private void initialize() {
        databaseReference = FirebaseDatabase.getInstance().getReference("categories");

        loadAllcategories("");

        main2Binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    loadAllcategories("");
                }else{
                    loadAllcategories(s.toString().toLowerCase());
                }

            }
        });

        main2Binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this,CartActivity.class));
            }
        });
    }

    private void loadAllcategories(String s) {
        main2Binding.shimmerRecyclerView.showShimmerAdapter();
        Query query = databaseReference.orderByChild("name").startAt(s).endAt(s+"\uf8ff");
        options= new FirebaseRecyclerOptions.Builder<Category>().setQuery(query,Category.class).build();
        adapter=new FirebaseRecyclerAdapter<Category, CategoryAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryAdapter holder, int position, @NonNull Category model) {
             //   String name = model.getName();
             //   String Name = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
                holder.cName.setText(Utility.capitalize(model.getName()));
                Glide.with(MainActivity2.this).load(model.getImageurl()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String catId = getRef(holder.getAdapterPosition()).getKey();
                        Intent intent = new Intent(MainActivity2.this,ItemsActivity2.class);
                        intent.putExtra("CID",catId);
                        intent.putExtra("NAME",model.getName());
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
                                String catId = getRef(holder.getAdapterPosition()).getKey();
                                assert catId != null;
                                databaseReference.child(catId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Removed successfully",Toast.LENGTH_SHORT).show();
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
            public CategoryAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card,parent,false);
                return new CategoryAdapter(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                if(main2Binding.shimmerRecyclerView.isShown()){
                    main2Binding.shimmerRecyclerView.hideShimmerAdapter();
                }
                if(getItemCount()!=0){
                    main2Binding.imageNot.setVisibility(View.GONE);
                }
                else{
                    main2Binding.imageNot.setVisibility(View.VISIBLE);
                }
            }
        };
        adapter.startListening();
        main2Binding.recyclerview.setAdapter(adapter);
    }

    private void setupWidgets() {
        GridLayoutManager lnlGrid = new GridLayoutManager(this, 2);
        main2Binding.recyclerview.setLayoutManager(lnlGrid);
    }
    private void setUpNavigiation() {
        BottomNavigationView bottomNavigationView =findViewById(R.id.nav_view);
        Utility.enableNavigaton(MainActivity2.this,this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem =menu.getItem(0);
        menuItem.setChecked(true);
    }
}