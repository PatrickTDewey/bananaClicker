package com.example.myapplication;

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

import com.example.myapplication.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private Integer count;
    private Integer bpcU;
    private Integer gpbU;
    private Integer prestige;
    private Integer level;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Integer count = SecondFragmentArgs.fromBundle(getArguments()).getMyNum();
        SQLiteDatabase my_db = requireActivity().openOrCreateDatabase("banana.db", 0, null);
        Cursor query = my_db.rawQuery("SELECT * FROM user;", null);
        if (query.moveToFirst()) {
            count = Integer.parseInt(query.getString(1));
            bpcU = query.getInt(2);
            gpbU = query.getInt(3);
            level = query.getInt(4);
            prestige = query.getInt(5);

        }
        query.close();
        my_db.close();


        String countText = getString(R.string.random_text, count);
        TextView bpcPriceView = binding.bpcPrice;
        TextView headerView = binding.textView;
        TextView bpcUpgrades = binding.bpcView;

        headerView.setText(countText);

        double bpcPrice = 10 * Math.pow(1.15, bpcU);
        Integer intBcpPrice = (int) Math.floor(bpcPrice);
        String bcpPriceText = getString(R.string.bpcPrice, intBcpPrice);
        bpcPriceView.setText(bcpPriceText);
        String bpcText = getString(R.string.bpcView, (bpcU + 1));
        bpcUpgrades.setText(bpcText);


        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        binding.bananaperclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (count > intBcpPrice) {
                    count -= intBcpPrice;
                    bpcU++;
                    String countText = getString(R.string.random_text, count);
                    headerView.setText(countText);
                    double bpcPrice = 10 * Math.pow(1.15, bpcU);
                    Integer intBcpPrice = (int) Math.floor(bpcPrice);
                    String priceText = getString(R.string.bpcPrice, intBcpPrice);
                    bpcPriceView.setText(priceText);
                    String bpcText = getString(R.string.bpcView, (bpcU + 1));
                    bpcUpgrades.setText(bpcText);
                    System.out.println("Integer Price: " + intBcpPrice);

                } else {
                    Toast myToast = Toast.makeText(getActivity(), "Insufficient Funds :( CLICK MOAR", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        SQLiteDatabase my_db = requireActivity().openOrCreateDatabase("banana.db", 0, null);
        String sql = "INSERT OR REPLACE INTO user (id, count, bpcU, gpbU, level, prestige) VALUES(1,"+count+","+bpcU+","+gpbU+","+level+","+prestige+");" ;
        my_db.execSQL(sql);
        my_db.close();
        super.onPause();


    }

}