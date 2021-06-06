package hcmute.edu.vn.mssv18110344;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;
import hcmute.edu.vn.mssv18110344.utility.DbBitmapUtility;
import hcmute.edu.vn.mssv18110344.utility.ImagePickerUtility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    View view;
    ImageView imgAvatar;
    TextView txtFullName, showInfo, showAddress, changeAvt, changePassword;
    AppCompatButton btnSignOut;
    AlertDialog.Builder builder;
    Bitmap avt;
    User user;
    DatabaseHandler db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        view = inflater.inflate(R.layout.fragment_account, container, false);

        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtFullName = view.findViewById(R.id.txtFullName);
        showInfo = view.findViewById(R.id.showInfo);
        showAddress = view.findViewById(R.id.showAddress);
        changeAvt = view.findViewById(R.id.changeAvt);
        changePassword = view.findViewById(R.id.changePassword);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        user = (User) getActivity().getIntent().getSerializableExtra("user");
        db = new DatabaseHandler(getContext());
        db.openDataBase();

        Bitmap avt = DbBitmapUtility.getImage(user.getAvatar());
        Bitmap circleBitmap = Bitmap.createBitmap(avt.getWidth(), avt.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (avt,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(avt.getWidth()/2, avt.getHeight()/2, avt.getWidth()/2, paint);

        imgAvatar.setImageBitmap(circleBitmap);
        txtFullName.setText(user.getFullName());

        showInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SeeInfoActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        showAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SeeAddressesActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        changeAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePickerUtility.getPickImageIntent(getContext());
                startActivityForResult(chooseImageIntent, 0);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn chắc chắn muốn đăng xuất?")
                        .setCancelable(false)
                        .setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                getActivity().finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("KHÔNG", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Thông báo");
                alertDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0:
                avt = ImagePickerUtility.getImageFromResult(getContext(), resultCode, data);
                if (avt == null)
                    return;

                Bitmap circleBitmap = Bitmap.createBitmap(avt.getWidth(), avt.getHeight(), Bitmap.Config.ARGB_8888);

                BitmapShader shader = new BitmapShader (avt,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(avt.getWidth()/2, avt.getHeight()/2, avt.getWidth()/2, paint);

                imgAvatar.setImageBitmap(circleBitmap);

                user.setAvatar(DbBitmapUtility.getString(avt));
                db.updateUser(user);
                Toast.makeText(getContext(), "Đổi ảnh đại diện thành công!", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}