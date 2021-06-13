package hcmute.edu.vn.mssv18110344;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.adapter.PurchaseAdapterType2;
import hcmute.edu.vn.mssv18110344.bean.Address;
import hcmute.edu.vn.mssv18110344.bean.Cart;
import hcmute.edu.vn.mssv18110344.bean.Order;
import hcmute.edu.vn.mssv18110344.bean.ViewPurchase;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class SeeOrderDetailsActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView txtOrderId, txtNameAndPhone, txtAddress, txtOrderStatus, txtPaymentStatus, txtOrderDate, txtCost;
    RecyclerView recyclerView;
    List<ViewPurchase> purchases;
    PurchaseAdapterType2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_order_details);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnBack);
        txtOrderId = findViewById(R.id.txtOrderId);
        txtNameAndPhone = findViewById(R.id.txtNameAndPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);
        txtPaymentStatus = findViewById(R.id.txtPaymentStatus);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtCost = findViewById(R.id.txtCost);
        recyclerView = findViewById(R.id.recyclerView);

        Order order = (Order) getIntent().getSerializableExtra("order");
        DatabaseHandler db = new DatabaseHandler(this);

        Address address = db.getAddressById(order.getAddressId());
        Cart cart = db.getCartById(order.getCartId());

        String orderId = "Đơn hàng " + order.getOrderId();
        String stringNameAndPhone = address.getName() + " | " + address.getPhone();
        String stringAddress = address.getStreet() + ", "
                + db.getWardName(address.getWardId()) + ", "
                + db.getDistrictName(address.getDistrictId()) + ", "
                + db.getProvinceName(address.getProvinceId());
        String orderStatus = "Trạng thái đơn hàng: " + order.getOrderStatus();
        String paymentStatus = "Trạng thái thanh toán: " + order.getPaymentStatus();
        String orderDate = "Ngày đặt hàng: " + order.getOrderDate();

        purchases = db.getProductsInCart(cart);
        adapter = new PurchaseAdapterType2(this, purchases);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        txtOrderId.setText(orderId);
        txtNameAndPhone.setText(stringNameAndPhone);
        txtAddress.setText(stringAddress);
        txtOrderStatus.setText(orderStatus);
        txtPaymentStatus.setText(paymentStatus);
        txtOrderDate.setText(orderDate);
        txtCost.setText(standardizePrice(totalCost()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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