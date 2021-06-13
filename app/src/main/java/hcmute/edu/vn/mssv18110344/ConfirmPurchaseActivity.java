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

                Order order = new Order(id, cart.getCartId(), "Đã tiếp nhận", "Chưa thanh toán", totalCost, dtf.format(now), "", address.getId());
                db.addOrder(order);

                User user = db.getUserById(cart.getUserId());
                String email = user.getEmail();
                String subject = "Đơn hàng của bạn đã được tiếp nhận";
                String msg = "Đơn hàng của bạn đã được chúng tôi tiếp nhận và đang chờ xử lý.\n"
                        + "Mã đơn hàng của bạn là: " + order.getOrderId() + ".\n"
                        + "Bạn có thể xem chi tiết đơn hàng tại mục Đơn hàng trong ứng dụng Ministop. "
                        + "Chúng tôi sẽ gửi email cập nhật trạng thái đơn hàng cho bạn ngay khi đơn hàng của bạn được xử lý.\n"
                        + "Xin chân thành cảm ơn bạn đã đồng hành cùng Ministop!\n"
                        + "Trân trọng,\n"
                        + "Đội ngũ hỗ trợ Ministop";

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
        return new StringBuilder(result).reverse().toString() + "đ";
    }

    private int totalCost() {
        int result = 0;
        for (int i = 0; i < purchases.size(); i++) {
            result += purchases.get(i).getPrice() * purchases.get(i).getAmount();
        }
        return result;
    }
}