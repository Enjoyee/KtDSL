package com.glimmer.lib;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.glimmer.lib.databinding.ActivityMainBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class SecondActivity extends BaseAc {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<String> arrayList = new ArrayList<>();
        arrayList.add(null);

//        Intent intent = new Intent();
//        intent.addCategory("com.glimmer.test");
//        intent.setAction("www.fuckyou.com");
//        startActivity(intent);

        Class<?> cls = Student.class;
        try {
//            Constructor constructor = cls.getConstructor(String.class);
            Constructor constructor = cls.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            Object obj = constructor.newInstance("123");
            Method declaredMethods = cls.getDeclaredMethod("getName");
            declaredMethods.setAccessible(true);
            Log.e("yyyyy", declaredMethods.invoke(obj).toString());
//            for (Method declaredMethod : declaredMethods) {
//
//                Log.e("yyyyy", declaredMethod);
//            }
            Student student = (Student) obj;
            binding.tvTest.setText(declaredMethods.invoke(obj) + "");
        } catch (Exception e) {
            binding.tvTest.setText(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(1000);
    }
}
