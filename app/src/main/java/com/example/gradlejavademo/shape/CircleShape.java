package com.example.gradlejavademo.shape;

import android.util.Log;

import com.example.annotation.InjectFactory;

/**
 * Author: fushuang
 * Date: 2020/4/30 下午9:18
 * Description:
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */

@InjectFactory(id = "CircleShape",superClass = IShape.class)
public class CircleShape implements IShape {
    @Override
    public void onDraw() {
        Log.d("fs666888", "onDraw: CircleShape");
    }
}
