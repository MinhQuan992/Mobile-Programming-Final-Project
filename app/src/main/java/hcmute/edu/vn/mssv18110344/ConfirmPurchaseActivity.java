package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hcmute.edu.vn.mssv18110344.adapter.PurchaseAdapterType2;
import hcmute.edu.vn.mssv18110344.bean.Address;
import hcmute.edu.vn.mssv18110344.bean.Cart;
import hcmute.edu.vn.mssv18110344.bean.Order;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.bean.ViewPurchase;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;
import hcmute.edu.vn.mssv18110344.utility.JavaMailAPI;

public class ConfirmPurchaseActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView txtNameAndPhone, txtAddress, txtCost;
    RecyclerView recyclerView;
    AppCompatButton btnConfirm;
    List<ViewPurchase> purchases;
    PurchaseAdapterType2 adapter;
    int totalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_purchase);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        txtNameAndPhone = findViewById(R.id.txtNameAndPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtCost = findViewById(R.id.txtCost);
        recyclerView = findViewById(R.id.recyclerView);
        btnConfirm = findViewById(R.id.btnConfirm);

        Cart cart = (Cart) getIntent().getSerializableExtra("cart");
        Address address = (Address) getIntent().getSerializableExtra("address");

        DatabaseHandler db = new DatabaseHandler(this);

        String stringNameAndPhone = address.getName() + " | " + address.getPhone();
        String stringAddress = address.getStreet() + ", "
                + db.getWardName(address.getWardId()) + ", "
                + db.getDistrictName(address.getDistrictId()) + ", "
                + db.getProvinceName(address.getProvinceId());

        txtNameAndPhone.setText(stringNameAndPhone);
        txtAddress.setText(stringAddress);

        purchases = db.getProductsInCart(cart);
        adapter = new PurchaseAdapterType2(this, purchases);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalCost = totalCost();
        txtCost.setText(standardizePrice(totalCost));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = db.generateOrderId();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime now = LocalDateTime.now();

                Order order = new Order(id, cart.getCartId(), "???? ti???p nh???n", "Ch??a thanh to??n", totalCost, dtf.format(now), "", address.getId());
                db.addOrder(order);

                User user = db.getUserById(cart.getUserId());
                String email = user.getEmail();
                String subject = "????n h??ng c???a b???n ???? ???????c ti???p nh???n";
                String msg = "????n h??ng c???a b???n ???? ???????c ch??ng t??i ti???p nh???n v?? ??ang ch??? x??? l??.\n"
                        + "M?? ????n h??ng c???a b???n l??: " + order.getOrderId() + ".\n"
                        + "B???n c?? th??? xem chi ti???t ????n h??ng t???i m???c ????n h??ng trong ???ng d???ng Ministop. "
                        + "Ch??ng t??i s??? g???i email c???p nh???t tr???ng th??i ????n h??ng cho b???n ngay khi ????n h??ng c???a b???n ???????c x??? l??.\n"
                        + "Xin ch??n th??nh c???m ??n b???n ???? ?????ng h??nh c??ng Ministop!\n"
                        + "Tr??n tr???ng,\n"
                        + "?????i ng?? h??? tr??? Ministop";

                JavaMailAPI javaMailAPI = new JavaMailAPI(getApplicationContext(), email, subject, msg);
                javaMailAPI.execute();

                Cart newCart = new Cart(db.generateCartId(), user.getId());
                db.addCart(newCart);

                Intent intent = new Intent(getApplicationContext(), NotifyActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private String standardizePrice(int price) {
        String priceInString = String.valueOf(price);
        String result = "";
        int i = priceInString.length() - 1, count = 0;
        while (i >= 0) {
            result += priceInString.substring(i, i + 1);
            count++;
            if (count == 3 && i != 0) {
                result += ".";
                count = 0;
            }
            i--;
        }
        return new StringBuilder(result).reverse().toString() + "??";
    }

    private int totalCost() {
        int result = 0;
        for (int i = 0; i < purchases.size(); i++) {
            result += purchases.get(i).getPrice() * purchases.get(i).getAmount();
        }
        return result;
    }
}