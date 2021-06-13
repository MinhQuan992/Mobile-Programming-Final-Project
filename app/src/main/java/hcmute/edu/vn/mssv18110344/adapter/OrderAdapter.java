package hcmute.edu.vn.mssv18110344.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.R;
import hcmute.edu.vn.mssv18110344.SeeOrderDetailsActivity;
import hcmute.edu.vn.mssv18110344.bean.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private Context mContext;
    private List<Order> mOrders;

    public OrderAdapter(Context context, List<Order> orders) {
        mContext = context;
        mOrders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = mOrders.get(position);
        String id = "Đơn hàng " + order.getOrderId();
        String orderDate = "Ngày đặt hàng: " + order.getOrderDate();
        holder.txtOrderId.setText(id);
        holder.txtOrderDate.setText(orderDate);
        holder.txtCost.setText(standardizePrice(order.getTotalCost()));
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrderId, txtOrderDate, txtCost;
        public LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            txtOrderId = view.findViewById(R.id.txtOrderId);
            txtOrderDate = view.findViewById(R.id.txtOrderDate);
            txtCost = view.findViewById(R.id.txtCost);
            layout = view.findViewById(R.id.layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mContext, SeeOrderDetailsActivity.class);
                        intent.putExtra("order", mOrders.get(position));
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
