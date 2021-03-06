package com.example.gradlejavademo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.annotation.InjectFactory;
import com.example.gradlejavademo.shape.IShape;
import com.example.gradlejavademo.shape.IShapeFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        IShape circleShape = new IShapeFactory().create("CircleShape");

        circleShape.onDraw();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("fs666", "onStop: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("fs666", "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("fs666", "onPause: ");
    }
}
