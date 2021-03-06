package com.example.myapplication;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private Long count;
    private Long bpcU;
    private Long gpbU;
    private Long prestige;
    private Long level;
    private Long intBcpPrice;
    private Long intGpbPrice;
    private Long intLvlPrice;
    private Long intPrestigePrice;
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

//        Long count = SecondFragmentArgs.fromBundle(getArguments()).getMyNum();
        SQLiteDatabase my_db = requireActivity().openOrCreateDatabase("banana.db", 0, null);
        Cursor query = my_db.rawQuery("SELECT * FROM user;", null);
        if (query.moveToFirst()) {
            count = Long.parseLong(query.getString(1));
            bpcU = query.getLong(2);
            gpbU = query.getLong(3);
            level = query.getLong(4);
            prestige = query.getLong(5);

        }
        query.close();
        my_db.close();
        updateScreen();



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


                if (count >= intBcpPrice) {
                    count -= intBcpPrice;
                    bpcU++;
                    updateScreen();


                } else {
                    Toast myToast = Toast.makeText(getActivity(), "Insufficient Funds :( CLICK MOAR", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }

        });
        binding.goldperbanana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count >= intGpbPrice){
                    count -= intGpbPrice;
                    gpbU++;
                    updateScreen();
                } else {
                    Toast myToast = Toast.makeText(getActivity(), "Insufficient Funds :( CLICK MOAR", Toast.LENGTH_SHORT);
                    myToast.show();
                }

            }
        });
        binding.levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count >= intLvlPrice){
                    count -= intLvlPrice;
                    level++;
                    updateScreen();

                } else {
                    Toast myToast = Toast.makeText(getActivity(), "Insufficient Funds :( CLICK MOAR", Toast.LENGTH_SHORT);
                    myToast.show();
                }

            }
        });
        binding.prestigeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count >= intPrestigePrice) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setCancelable(true);
                    builder.setTitle("Confirm Prestige");
                    builder.setMessage("Prestiging will clear all current progress in exchange for a new permanent multiplier");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    prestige++;
                                    gpbU = 0L;
                                    bpcU = 0L;
                                    level = 1L;
                                    count = 0L;
                                    updateScreen();

                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast myToast = Toast.makeText(getActivity(), "Insufficient Funds :( CLICK MOAR", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }
        });
        binding.bpcMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while(count >= intBcpPrice) {
                    count -= intBcpPrice;
                    bpcU++;
                    updateScreen();
                }
            }
        });

        binding.gpbMaxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (count >= intGpbPrice) {
                    count -= intGpbPrice;
                    gpbU++;
                    updateScreen();
                }
            }

        });

    }

    private void updateScreen() {
        String countText = getString(R.string.random_text, count);
        TextView bpcPriceView = binding.bpcPrice;
        TextView headerView = binding.textView;
        TextView bpcUpgrades = binding.bpcView;
        TextView gpbUpgrades = binding.gpbUText;
        TextView gpbPriceView = binding.gpbCostText;
        TextView levelUpgrades = binding.levelUTV;
        TextView levelCost = binding.levelCostTV;
        TextView prestigeCostView = binding.prestigeCostTV;

        headerView.setText(countText);

        double bpcPrice = 10 * Math.pow(1.15, bpcU);
        intBcpPrice = (long) Math.floor(bpcPrice);
        String bcpPriceText = getString(R.string.bpcPrice, intBcpPrice);
        bpcPriceView.setText(bcpPriceText);
        String bpcText = getString(R.string.bpcView, (bpcU + 1));
        bpcUpgrades.setText(bpcText);

        double gpbPrice = 10 * Math.pow(1.15, gpbU);
        intGpbPrice = (long) Math.floor(gpbPrice);
        String gpbPriceText = getString(R.string.gpbCostText, intGpbPrice);
        gpbPriceView.setText(gpbPriceText);
        String gpbUpgradesText = getString(R.string.gpbUText, (gpbU +1));
        gpbUpgrades.setText(gpbUpgradesText);

        double levelPrice = 1000000 * Math.pow(3.15, (level-1));
        intLvlPrice = (long) Math.floor(levelPrice);
        String lvlCostText = getString(R.string.levelCostTV, intLvlPrice);
        levelCost.setText(lvlCostText);
        String lvlUpgradesText = getString(R.string.levelUTVstring, level);
        levelUpgrades.setText(lvlUpgradesText);

        double prestigePrice = 20000000 * Math.pow(3.15, (prestige-1));
        intPrestigePrice = (long) Math.floor(prestigePrice);
        String prestigeCostText = getString(R.string.prestigeCostString, intPrestigePrice);
        prestigeCostView.setText(prestigeCostText);

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