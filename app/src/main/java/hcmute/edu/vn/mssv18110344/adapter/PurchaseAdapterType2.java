package hcmute.edu.vn.mssv18110344.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.R;
import hcmute.edu.vn.mssv18110344.bean.ViewPurchase;

public class PurchaseAdapterType2 extends RecyclerView.Adapter<PurchaseAdapterType2.ViewHolder>{
    private Context mContext;
    private List<ViewPurchase> mPurchases;

    public PurchaseAdapterType2(Context context, List<ViewPurchase> purchases) {
        mContext = context;
        mPurchases = purchases;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.purchase_item_type_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewPurchase purchase = mPurchases.get(position);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), purchase.getPicture());
        holder.img.setImageBitmap(bitmap);
        holder.txtProductName.setText(standardizeProductName(purchase.getName()));
        holder.txtPrice.setText(standardizePrice(purchase.getPrice()));
        holder.txtAmount.setText(String.valueOf(purchase.getAmount()));
    }

    @Override
    public int getItemCount() {
        return mPurchases.size();
    }

    private String standardizeProductName(String name) {
        if (name.length() <= 29)
            return name;
        return name.substring(0, 28) + "...";
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
        public ImageView img;
        public TextView txtProductName, txtPrice, txtAmount;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);
            txtProductName = view.findViewById(R.id.txtProductName);
            txtPrice = view.findViewById(R.id.txtPrice);
            txtAmount = view.findViewById(R.id.txtAmount);
        }
    }
}
