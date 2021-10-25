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
    private TextView gpcTextView;
    private TextView bpcTextView;
    private TextView gpbTextView;
    private TextView levelTextView;
    private TextView prestigeTextView;
    //  private Long count;
    private Long bpcU;
    private Long gpbU;
    private Long prestige =1L;
    private Long level =1L;
    private Long bpcM =1L;
    private Long gpbM =1L;

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
        Long count = Long.parseLong(countString);
        if (bpcU == null){
            count++;
        } else {
            bpcM = (bpcU +1) * level * prestige;
            gpbM = (gpbU +1) * level * prestige;
            count += (bpcM * gpbM);
        }
        // Display the new value in the text view
        showCountTextView.setText(Long.toString(count));
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCountTextView = binding.textviewFirst;
        gpbTextView = binding.gpbTextView;
        gpcTextView = binding.gpcTextView;
        bpcTextView = binding.bpcTextView;
        levelTextView = binding.levelTV;
        prestigeTextView = binding.prestigeTV;
        SQLiteDatabase my_db = requireActivity().openOrCreateDatabase("banana.db", 0, null);
        Cursor query = my_db.rawQuery("SELECT * FROM user;", null);
        if (query.moveToFirst()) {
            showCountTextView.setText(query.getString(1));
            bpcU = query.getLong(2);
            gpbU = query.getLong(3);
            level = query.getLong(4);
            prestige = query.getLong(5);
            System.out.println(bpcU);
            System.out.println("prestige: " + prestige);
            System.out.println("level: " + level);
            bpcM = (bpcU +1) * level * prestige;
            gpbM = (gpbU +1) * level * prestige;
            gpcTextView.setText(getString(R.string.gpcTVstring, (bpcM * gpbM)));
            gpbTextView.setText(getString(R.string.gpbTVstring, gpbM));
            bpcTextView.setText(getString(R.string.bpcTVstring, bpcM));
            levelTextView.setText(getString(R.string.levelTVstring, level));
            prestigeTextView.setText(getString(R.string.prestigeTVstring, prestige));
        }
        query.close();
        my_db.close();
        binding.randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long currentCount = Long.parseLong(showCountTextView.getText().toString());

                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(action);
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