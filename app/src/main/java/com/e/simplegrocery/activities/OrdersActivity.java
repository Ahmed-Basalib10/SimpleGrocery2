package com.e.simplegrocery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.e.simplegrocery.Adapter.ItemHolder;
import com.e.simplegrocery.Adapter.OrderAdapter;
import com.e.simplegrocery.R;
import com.e.simplegrocery.databinding.ActivityOrdersBinding;
import com.e.simplegrocery.model.Items;
import com.e.simplegrocery.model.Order;
import com.e.simplegrocery.utilities.Utility;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrdersActivity extends AppCompatActivity {
    ActivityOrdersBinding ordersBinding;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Order, OrderAdapter> adapter;
    private FirebaseRecyclerOptions<Order> options;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersBinding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(ordersBinding.getRoot());


        setUpNavigiation();
        initialize();
        setupWidgets();

    }

    private void initialize() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            currentUser = user.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        loadorders();
    }

    private void loadorders() {
        ordersBinding.shimmerll.startShimmer();
        ordersBinding.shimmerll.showShimmer(true);
        ordersBinding.shimmerll.setVisibility(View.VISIBLE);

        Query query = databaseReference.orderByChild("userId").equalTo(currentUser);
        options = new FirebaseRecyclerOptions.Builder<Order>().setQuery(query,Order.class).build();
        adapter = new FirebaseRecyclerAdapter<Order, OrderAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderAdapter holder, int position, @NonNull Order model) {
             holder.id.setText(model.getOrderId());
                Date date1 = new Date((Long) model.getTinmdate());
                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy hh:mm a",
                        Locale.getDefault());
                String text = sfd.format(date1);
             holder.date.setText(text);
             holder.state.setText(model.getState());
             holder.total.setText("Rs."+model.getTotal());
            }

            @NonNull
            @Override
            public OrderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card,parent,false);
                return new OrderAdapter(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(ordersBinding.shimmerll.isShimmerVisible()){
                    ordersBinding.shimmerll.stopShimmer();
                    ordersBinding.shimmerll.setVisibility(View.GONE);
                }

                if(getItemCount()!=0){
                    ordersBinding.imageNot.setVisibility(View.GONE);
                }
                else{
                    ordersBinding.imageNot.setVisibility(View.VISIBLE);
                }
            }
        };
        adapter.startListening();
        ordersBinding.recyclerview.setAdapter(adapter);
    }

    private void setupWidgets() {
        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        ordersBinding.recyclerview.setLayoutManager(llmPlace);
    }

    private void setUpNavigiation() {
        BottomNavigationView bottomNavigationView =findViewById(R.id.nav_view);
        Utility.enableNavigaton(OrdersActivity.this,this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem =menu.getItem(1);
        menuItem.setChecked(true);
    }
}