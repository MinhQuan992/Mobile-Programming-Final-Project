package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hcmute.edu.vn.mssv18110344.bean.Address;
import hcmute.edu.vn.mssv18110344.bean.AdministrativeUnit;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class AddAndEditAddressActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText txtName, txtPhone, txtStreet;
    TextView txtProvince, txtDistrict, txtWard;
    AppCompatButton btnSave;
    String provinceId = "", districtId = "", wardId = "";

    private static final String TEXT_NOT_CHOOSE_PROVINCE = "Chọn tỉnh/thành";
    private static final String TEXT_NOT_CHOOSE_DISTRICT = "Chọn huyện/quận";
    private static final String TEXT_NOT_CHOOSE_WARD = "Chọn xã/phường";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit_address);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtProvince = findViewById(R.id.txtProvince);
        txtDistrict = findViewById(R.id.txtDistrict);
        txtWard = findViewById(R.id.txtWard);
        txtStreet = findViewById(R.id.txtStreet);
        btnSave = findViewById(R.id.btnSave);

        Address address = (Address) getIntent().getSerializableExtra("address");
        DatabaseHandler db = new DatabaseHandler(this);

        if (address == null) {
            txtProvince.setText(TEXT_NOT_CHOOSE_PROVINCE);
            txtDistrict.setText(TEXT_NOT_CHOOSE_DISTRICT);
            txtWard.setText(TEXT_NOT_CHOOSE_WARD);
        } else {
            provinceId = address.getProvinceId();
            districtId = address.getDistrictId();
            wardId = address.getWardId();

            txtName.setText(address.getName());
            txtPhone.setText(address.getPhone());
            txtProvince.setText(db.getProvinceName(provinceId));
            txtDistrict.setText(db.getDistrictName(districtId));
            txtWard.setText(db.getWardName(wardId));
            txtStreet.setText(address.getStreet());
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseAdministrativeUnitActivity.class);
                intent.putExtra("type", "province");
                startActivityForResult(intent, 0);
            }
        });

        txtDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtProvince.getText().toString().equals(TEXT_NOT_CHOOSE_PROVINCE)) {
                    Toast.makeText(getApplicationContext(), "Bạn vui lòng chọn tỉnh/thành!", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), ChooseAdministrativeUnitActivity.class);
                intent.putExtra("type", "district");
                intent.putExtra("id", provinceId);
                startActivityForResult(intent, 0);
            }
        });

        txtWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtProvince.getText().toString().equals(TEXT_NOT_CHOOSE_PROVINCE)) {
                    Toast.makeText(getApplicationContext(), "Bạn vui lòng chọn tỉnh/thành!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (txtDistrict.getText().toString().equals(TEXT_NOT_CHOOSE_DISTRICT)) {
                    Toast.makeText(getApplicationContext(), "Bạn vui lòng chọn huyện/quận!", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), ChooseAdministrativeUnitActivity.class);
                intent.putExtra("type", "ward");
                intent.putExtra("id", districtId);
                startActivityForResult(intent, 0);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String street = txtStreet.getText().toString().trim();
                User user = (User) getIntent().getSerializableExtra("user");

                if (name.isEmpty() || phone.isEmpty() || provinceId.isEmpty() || districtId.isEmpty() || wardId.isEmpty() || street.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Bạn phải nhập tất cả thông tin!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isPhoneValid(phone)) {
                    Toast.makeText(getApplicationContext(), "Bạn phải nhập số điện thoại hợp lệ", Toast.LENGTH_LONG).show();
                    txtPhone.requestFocus();
                    return;
                }

                if (address == null) {
                    Address newAddress = new Address(db.generateAddressId(), name, phone, provinceId, districtId, wardId, street, user.getId());
                    db.addAddress(newAddress);
                    Toast.makeText(getApplicationContext(), "Thêm địa chỉ thành công!", Toast.LENGTH_LONG).show();
                } else {
                    address.setName(name);
                    address.setPhone(phone);
                    address.setProvinceId(provinceId);
                    address.setDistrictId(districtId);
                    address.setWardId(wardId);
                    address.setStreet(street);
                    db.updateAddress(address);
                    Toast.makeText(getApplicationContext(), "Cập nhật địa chỉ thành công!", Toast.LENGTH_LONG).show();
                }

                List<Address> addressList = db.getAddresses(user.getId());
                Intent intent = new Intent();

                Bundle args = new Bundle();
                args.putSerializable("addressList", (Serializable) addressList);
                intent.putExtra("bundle", args);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String type = data.getStringExtra("type");
                AdministrativeUnit unit = (AdministrativeUnit) data.getSerializableExtra("unit");

                switch (type) {
                    case "province":
                        if (!txtProvince.getText().toString().equals(unit.getName())) {
                            txtProvince.setText(unit.getName());
                            txtDistrict.setText(TEXT_NOT_CHOOSE_DISTRICT);
                            txtWard.setText(TEXT_NOT_CHOOSE_WARD);
                            provinceId = unit.getId();
                        }
                        break;
                    case "district":
                        if (!txtDistrict.getText().toString().equals(unit.getName())) {
                            txtDistrict.setText(unit.getName());
                            txtWard.setText(TEXT_NOT_CHOOSE_WARD);
                            districtId = unit.getId();
                        }
                        break;
                    default:
                        txtWard.setText(unit.getName());
                        wardId = unit.getId();
                }
            }
        }
    }

    private boolean isPhoneValid(String phone) {
        String expression = "^\\d{10}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}