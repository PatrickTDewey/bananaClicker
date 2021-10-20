package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private TextView showCountTextView;
//    private Integer count;
    private Integer bpcU;
    private Integer gpbU;
    private Integer prestige;
    private Integer level;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);


        return binding.getRoot();

    }

    @SuppressLint("SetTextI18n")
    private void countMe(View view) {
        // Get val of text view
        String countString = showCountTextView.getText().toString();
        // Convert to number and increment
        Integer count = Integer.parseInt(countString);
        count += bpcU;
        // Display the new value in the text view
        showCountTextView.setText(Integer.toString(count));
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCountTextView = binding.textviewFirst;
        SQLiteDatabase my_db = requireActivity().openOrCreateDatabase("banana.db", 0, null);
        Cursor query = my_db.rawQuery("SELECT * FROM user;", null);
        if (query.moveToFirst()) {
            showCountTextView.setText(query.getString(1));
            bpcU = query.getInt(2);
            gpbU = query.getInt(3);
            level = query.getInt(4);
            prestige = query.getInt(5);
            System.out.println(bpcU);
        }
        query.close();
        my_db.close();
        binding.randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt(showCountTextView.getText().toString());

                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(action);
            }
        });
        binding.toastButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast myToast = Toast.makeText(getActivity(), "Hello toast!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
        binding.countButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                countMe(view);
            }

        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putString("currentCount", showCountTextView.getText().toString());
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onViewStateRestored( Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null){
//            String savedString = savedInstanceState.getString("currentCount");
//            showCountTextView.setText(savedString);
//        }
    }

    @Override
    public void onPause() {
        SQLiteDatabase my_db = requireActivity().openOrCreateDatabase("banana.db", 0, null);
        String sql = "INSERT OR REPLACE INTO user (id, count, bpcU, gpbU, level, prestige) VALUES(1,"+showCountTextView.getText().toString()+","+bpcU+","+gpbU+","+level+","+prestige+");" ;
        my_db.execSQL(sql);
        my_db.close();
        super.onPause();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

}