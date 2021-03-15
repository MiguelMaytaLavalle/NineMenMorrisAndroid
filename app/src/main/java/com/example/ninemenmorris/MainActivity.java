package com.example.ninemenmorris;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ninemenmorris.view.MVCView;

public class MainActivity extends AppCompatActivity{
    private static boolean NEW_GAME = false;
    private MVCView mvcView;
    private MVCView restoredView;


    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Create");
        mvcView = new MVCView(this,null);
        this.view = mvcView.getRootView();
        setContentView(view);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.item_new_game:
                NEW_GAME = true;
                recreate();
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("ONSTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("ONSTOP");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(restoredView != null){
            this.mvcView = new MVCView(restoredView, this, null);
            setContentView(mvcView.getRootView());
        }
        System.out.println("ONRESUME");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Bruh");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("ONSAVEINSTANCE");
        outState.putParcelable("MVCView", mvcView);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("ONRESTORE");
        if(!NEW_GAME){
            if(savedInstanceState != null){
                if(view.getParent() != null){
                    restoredView = savedInstanceState.getParcelable("MVCView");
                }
            }
        }
        NEW_GAME = false;
    }

}