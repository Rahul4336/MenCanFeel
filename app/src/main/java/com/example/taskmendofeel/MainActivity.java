package com.example.taskmendofeel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId())
            {
                case R.id.home:
                    fragmentManager=getSupportFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container,new Home_Fragment());
                    fragmentTransaction.commit();
                    break;
//                case R.id.home:
//                    fragmentManager=getSupportFragmentManager();
//                    fragmentTransaction=fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container,new Home_Fragment());
//                    fragmentTransaction.commit();
//                    break;
//                case R.id.home:
//                    fragmentManager=getSupportFragmentManager();
//                    fragmentTransaction=fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container,new Home_Fragment());
//                    fragmentTransaction.commit();
//                    break;
//                case R.id.home:
//                    fragmentManager=getSupportFragmentManager();
//                    fragmentTransaction=fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container,new Home_Fragment());
//                    fragmentTransaction.commit();
//                    break;
            }

            return true;
        });

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,new Home_Fragment());
        fragmentTransaction.commit();
    }

    private static int backButtonCount=1;
    @Override
    public void onBackPressed() {

        if (backButtonCount > 1) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press once again to exit.", Toast.LENGTH_LONG).show();
            backButtonCount++;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backButtonCount = 1;
                }
            }, 2000);
        }
    }
}