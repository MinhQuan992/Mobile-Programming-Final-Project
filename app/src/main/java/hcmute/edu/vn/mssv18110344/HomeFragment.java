package hcmute.edu.vn.mssv18110344;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hcmute.edu.vn.mssv18110344.bean.Cart;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    View view;
    EditText txtFind;
    ImageButton btnCart, btnRice, btnNoodle, btnWater, btnSalad, btnSandwich, btnFastFood;
    User user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");
        DatabaseHandler db = new DatabaseHandler(getContext());
        Cart cart = db.getCart(user);

        txtFind = view.findViewById(R.id.txtFind);
        btnCart = view.findViewById(R.id.btnCart);
        btnRice = view.findViewById(R.id.btnRice);
        btnNoodle = view.findViewById(R.id.btnNoodle);
        btnWater = view.findViewById(R.id.btnWater);
        btnSalad = view.findViewById(R.id.btnSalad);
        btnSandwich = view.findViewById(R.id.btnSandwich);
        btnFastFood = view.findViewById(R.id.btnFastFood);

        btnRice.setOnClickListener(this::onClick);
        btnNoodle.setOnClickListener(this::onClick);
        btnWater.setOnClickListener(this::onClick);
        btnSalad.setOnClickListener(this::onClick);
        btnSandwich.setOnClickListener(this::onClick);
        btnFastFood.setOnClickListener(this::onClick);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SeeCartActivity.class);
                intent.putExtra("cart", cart);
                startActivity(intent);
            }
        });

        txtFind.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(v.getContext(), SeeProductsActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("type", "from text");
                    intent.putExtra("text", txtFind.getText().toString().trim());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), SeeProductsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("type", "from category");
        switch (v.getId()) {
            case R.id.btnRice:
                intent.putExtra("category", 1);
                break;
            case R.id.btnNoodle:
                intent.putExtra("category", 2);
                break;
            case R.id.btnWater:
                intent.putExtra("category", 3);
                break;
            case R.id.btnSandwich:
                intent.putExtra("category", 4);
                break;
            case R.id.btnFastFood:
                intent.putExtra("category", 5);
                break;
            case R.id.btnSalad:
                intent.putExtra("category", 6);
                break;
        }
        startActivity(intent);
    }
}