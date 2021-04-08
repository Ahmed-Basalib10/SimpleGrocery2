package com.e.simplegrocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.e.simplegrocery.Adapter.CategoryAdapter;
import com.e.simplegrocery.R;
import com.e.simplegrocery.model.Category;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fb;
    private RecyclerView cRecyclerview;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Category, CategoryAdapter> adapter;
    private FirebaseRecyclerOptions<Category> options;
    public AlertDialog dialog;
    private ImageView imageView;
    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;
    private EditText searcgText;
    ShimmerRecyclerView shimmerRecycler;

    private ShimmerFrameLayout sss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        setupWidgets();

    }

    private void setupWidgets() {
        GridLayoutManager lnlGrid = new GridLayoutManager(this, 3);
        cRecyclerview.setLayoutManager(lnlGrid);
    }

    private void initialize() {

        cRecyclerview=(RecyclerView) findViewById(R.id.recyclerview) ;
        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);
        progressBar=(ProgressBar) findViewById(R.id.progressBar1);
        dialog=new SpotsDialog.Builder().setContext(this).build();
        dialog.setMessage("Loading...");
        imageView=(ImageView) findViewById(R.id.imageNot);
        sss=(ShimmerFrameLayout) findViewById(R.id.shimmerll);

        databaseReference = FirebaseDatabase.getInstance().getReference("categories");
        searcgText=(EditText) findViewById(R.id.search);

        fb=(FloatingActionButton) findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog= new BottomSheetDialog(MainActivity.this,R.style.BottomSheetDialogTheme);
                View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.addcategory,(LinearLayout) findViewById(R.id.container_dialog));

                final EditText name = view1.findViewById(R.id.name);
                final Button add = view1.findViewById(R.id.add);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name1 =name.getText().toString().toLowerCase();
                        if(name1.isEmpty()){
                            name.setError("Name is required");
                            name.requestFocus();
                            return;
                        }

                        addCategory(name1);
                      //  bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });

        loadAllcategories("");

        searcgText.addTextChangedListener(new TextWatcher() {
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
    }

    private void addCategory(String name1) {
//        bottomSheetDialog.dismiss();
//        progressBar.setVisibility(View.VISIBLE);
//        Category category =new Category(name1);
//        String catId = databaseReference.push().getKey();
//        assert catId != null;
//        databaseReference.child(catId).setValue(category).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),"New category has been added successfully",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void loadAllcategories(String s) {
       // dialog.show();
     /*   sss.startShimmer();
        sss.showShimmer(true);
        sss.setVisibility(View.VISIBLE); */
        shimmerRecycler.showShimmerAdapter();
        Query query = databaseReference.orderByChild("name").startAt(s).endAt(s+"\uf8ff");
        options= new FirebaseRecyclerOptions.Builder<Category>().setQuery(query,Category.class).build();
        adapter=new FirebaseRecyclerAdapter<Category, CategoryAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryAdapter holder, int position, @NonNull Category model) {
                String name = model.getName();
                String Name = name.substring(0,1).toUpperCase()+name.substring(1).toLowerCase();
                holder.cName.setText(Name);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String catId = getRef(holder.getAdapterPosition()).getKey();
                        Intent intent = new Intent(MainActivity.this,ItemsActivity.class);
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
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card,parent,false);
                return new CategoryAdapter(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
             /*   if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
                if (getItemCount() != 0) {
                    // dialog.dismiss();
                    imageView.setVisibility(View.GONE);
                } else {
                    //  dialog.dismiss();
                    imageView.setVisibility(View.VISIBLE);
                }*/
                // using facebook library
             /*   if(sss.isShimmerVisible()){
                    sss.stopShimmer();
                    sss.setShimmer(null);
                    sss.setVisibility(View.GONE);
                }

                if(getItemCount()!=0){
                    imageView.setVisibility(View.GONE);
                }
                else{
                    imageView.setVisibility(View.VISIBLE);
                }*/
                if(shimmerRecycler.isShown()){
                    shimmerRecycler.hideShimmerAdapter();
                }
                if(getItemCount()!=0){
                    imageView.setVisibility(View.GONE);
                }
                else{
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        };
        adapter.startListening();
        cRecyclerview.setAdapter(adapter);
    }
}