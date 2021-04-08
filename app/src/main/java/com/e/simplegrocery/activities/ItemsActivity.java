package com.e.simplegrocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.e.simplegrocery.Adapter.CategoryAdapter;
import com.e.simplegrocery.Adapter.ItemHolder;
import com.e.simplegrocery.R;
import com.e.simplegrocery.model.Category;
import com.e.simplegrocery.model.Items;
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

public class ItemsActivity extends AppCompatActivity {
    private FloatingActionButton fb;
    private RecyclerView cRecyclerview;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Items, ItemHolder> adapter;
    private FirebaseRecyclerOptions<Items> options;
    public AlertDialog dialog;
    private ImageView imageView;
    private ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;

    private EditText searcgText;
    private TextView textView;
    private String cName , catId;

    private ShimmerFrameLayout ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        catId= getIntent().getStringExtra("CID");
        cName = getIntent().getStringExtra("NAME");

        initialize();
        setupWidgets();
    }

    private void setupWidgets() {
        textView=(TextView) findViewById(R.id.cText);
        textView.setText(cName);

        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cRecyclerview.setLayoutManager(llmPlace);
    }

    private void initialize() {
        cRecyclerview=(RecyclerView) findViewById(R.id.recyclerview) ;
        dialog=new SpotsDialog.Builder().setContext(this).build();
        dialog.setMessage("Loading...");
        imageView=(ImageView) findViewById(R.id.imageNot);
        progressBar=(ProgressBar) findViewById(R.id.progressBar1);

        ss=(ShimmerFrameLayout) findViewById(R.id.shimmerll) ;

        databaseReference = FirebaseDatabase.getInstance().getReference("categories").child(catId);
        searcgText=(EditText) findViewById(R.id.search);

        loadAllitems("");
        searcgText.addTextChangedListener(new TextWatcher() {
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

        fb=(FloatingActionButton) findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog= new BottomSheetDialog(ItemsActivity.this,R.style.BottomSheetDialogTheme);
                View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.additems,(LinearLayout) findViewById(R.id.container_dialog));

                final EditText name = view1.findViewById(R.id.name);
                final EditText price = view1.findViewById(R.id.price);
                final EditText qt = view1.findViewById(R.id.qt);
                final Button add = view1.findViewById(R.id.add);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name1 =name.getText().toString().trim();
                        String price1 =price.getText().toString().trim();
                        String qt1 =qt.getText().toString().trim();

                        if(name1.isEmpty()){
                            name.setError("Name is required");
                            name.requestFocus();
                            return;
                        }
                        if(price1.isEmpty()){
                            price.setError("Name is required");
                            price.requestFocus();
                            return;
                        }
                        if(qt1.isEmpty()){
                            qt.setError("Name is required");
                            qt.requestFocus();
                            return;
                        }
                        addFood(name1,price1,qt1);
                    }
                });
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });
    }

    private void loadAllitems(String toString) {
       // dialog.show();
        ss.startShimmer();
        ss.showShimmer(true);
        ss.setVisibility(View.VISIBLE);

        Query query =databaseReference.orderByChild("fname").startAt(toString).endAt(toString+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Items>().setQuery(query,Items.class).build();
        adapter=new FirebaseRecyclerAdapter<Items, ItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Items model) {
                holder.textView.setText(model.getFname());
                holder.textView1.setText("Rs "+model.getFprice());
            //    holder.textView2.setText("Quantity "+model.getFquantity());

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
                View view=LayoutInflater.from(ItemsActivity.this).inflate(R.layout.item_card,parent,false);
                return new ItemHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
         /*       if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
                if (getItemCount() != 0) {
                    // dialog.dismiss();
                    imageView.setVisibility(View.GONE);
                } else {
                    //  dialog.dismiss();
                    imageView.setVisibility(View.VISIBLE);
                } */

                // using facebook library
                if(ss.isShimmerVisible()){
                    ss.stopShimmer();
                    ss.setShimmer(null);
                    ss.setVisibility(View.GONE);
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

    private void addFood(String name1, String price1, String qt1) {
//        bottomSheetDialog.dismiss();
//        progressBar.setVisibility(View.VISIBLE);
//        Items items =new Items(name1,price1,qt1);
//        String itemId = databaseReference.push().getKey();
//        assert itemId != null;
//        databaseReference.child(itemId).setValue(items).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),"New Item added successfully",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}