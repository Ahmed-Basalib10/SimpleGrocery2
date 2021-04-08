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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.e.simplegrocery.Adapter.CartAdapter;
import com.e.simplegrocery.Adapter.ItemHolder;
import com.e.simplegrocery.R;
import com.e.simplegrocery.databinding.ActivityCartBinding;
import com.e.simplegrocery.model.Cart;
import com.e.simplegrocery.model.Items;
import com.e.simplegrocery.model.Order;
import com.e.simplegrocery.utilities.Utility;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding cartBinding;
    private DatabaseReference databaseReference,databaseReference1;
    private FirebaseRecyclerAdapter<Cart, CartAdapter> adapter;
    private FirebaseRecyclerOptions<Cart> options;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String currentUser;
    private int total=0;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(cartBinding.getRoot());

        initialize();
        setupWidgets();
    }

    private void initialize() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            currentUser = user.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(currentUser);
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Orders");

        cartBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(total != 0){
                    bottomSheetDialog= new BottomSheetDialog(CartActivity.this,R.style.BottomSheetDialogTheme);
                    View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.addlocation,(LinearLayout) findViewById(R.id.container_dialog));

                    final EditText name = view1.findViewById(R.id.name);
                    final Button add = view1.findViewById(R.id.add);
                    final ProgressBar progressBar = view1.findViewById(R.id.progress);

                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name1 =name.getText().toString().toLowerCase();
                            if(name1.isEmpty()){
                                name.setError("Name is required");
                                name.requestFocus();
                                return;
                            }
                            String orderId = databaseReference1.push().getKey();
                            assert orderId != null;
                            Order order = new Order(currentUser, name1, ServerValue.TIMESTAMP, "Placed",String.valueOf(total),orderId);
                            progressBar.setVisibility(View.VISIBLE);

                            databaseReference1.child(orderId).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference1.child(orderId).child("orderlist").setValue(snapshot.getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    databaseReference.setValue(null);
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(),"Your Order Placed Successfully",Toast.LENGTH_SHORT).show();
                                                    total = 0;
                                                    cartBinding.total.setText("Total : Rs"+ total);
                                                    bottomSheetDialog.dismiss();

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    bottomSheetDialog.setContentView(view1);
                    bottomSheetDialog.show();
                }else {
                    Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void loaditems() {
        cartBinding.shimmerll.startShimmer();
        cartBinding.shimmerll.showShimmer(true);
        cartBinding.shimmerll.setVisibility(View.VISIBLE);

        options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(databaseReference,Cart.class).build();
        adapter = new FirebaseRecyclerAdapter<Cart, CartAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartAdapter holder, int position, @NonNull Cart model) {
                holder.textView.setText(Utility.capitalize(model.getFoodName()));
                holder.textView1.setText("Rs "+model.getFoodPrice());
                Glide.with(CartActivity.this).load(model.getFoodImage()).into(holder.imageView);
                holder.textView2.setText("x"+model.getFoodQuantity());

                holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                        menu.setHeaderTitle("Select action");
                        MenuItem edit =menu.add(Menu.NONE,1,1,"Edit");
                        MenuItem delete =menu.add(Menu.NONE,2,2,"Delete");

                        edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String id = getRef(holder.getAdapterPosition()).getKey();
                                Intent intent = new Intent(CartActivity.this,ItemDetailsActivity.class);
                                intent.putExtra("ID",id);
                                intent.putExtra("NAME",model.foodName);
                                intent.putExtra("DESC",model.getFoodDesc());
                                intent.putExtra("PRICE",model.getFoodPrice());
                                intent.putExtra("IMAGE",model.getFoodImage());
                                intent.putExtra("QT",model.getFoodQuantity());
                                startActivity(intent);
                            //    finish();
                                return false;
                            }
                        });

                        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String id = getRef(holder.getAdapterPosition()).getKey();
                                assert id != null;
                                databaseReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Item removed successfully",Toast.LENGTH_SHORT).show();
                                        onDataChanged();
                                        int  priceForEach = Integer.parseInt(model.getFoodPrice()) * Integer.parseInt(model.getFoodQuantity());
                                        total=total-priceForEach;
                                        cartBinding.total.setText("Total : Rs"+ total);
                                    }
                                });
                                return false;
                            }
                        });
                    }
                });

                int  priceForEach = Integer.parseInt(model.getFoodPrice()) * Integer.parseInt(model.getFoodQuantity());
                total = total + priceForEach;
                cartBinding.total.setText("Total : Rs"+ total);
            }

            @NonNull
            @Override
            public CartAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card,parent,false);
                return new CartAdapter(view);
            }
            @Override
            public void onDataChanged() {
                super.onDataChanged();

                // using facebook library
                if(cartBinding.shimmerll.isShimmerVisible()){
                    cartBinding.shimmerll.stopShimmer();
                    cartBinding.shimmerll.setVisibility(View.GONE);
                }

                if(getItemCount()!=0){
                    cartBinding.imageNot.setVisibility(View.GONE);
                }
                else{
                    cartBinding.imageNot.setVisibility(View.VISIBLE);
                }
            }
        };
        adapter.startListening();
        cartBinding.recyclerview.setAdapter(adapter);
    }

    private void setupWidgets() {
        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartBinding.recyclerview.setLayoutManager(llmPlace);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loaditems();
    }

    @Override
    protected void onStop() {
        super.onStop();
        total = 0;
        adapter.stopListening();
    }
}