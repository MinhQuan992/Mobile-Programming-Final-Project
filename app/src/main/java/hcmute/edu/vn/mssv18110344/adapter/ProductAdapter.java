package hcmute.edu.vn.mssv18110344.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.R;
import hcmute.edu.vn.mssv18110344.SeeProductDetailsActivity;
import hcmute.edu.vn.mssv18110344.bean.Product;
import hcmute.edu.vn.mssv18110344.utility.DbBitmapUtility;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context mContext;
    private List<Product> mProducts;

    public ProductAdapter(Context context, List<Product> mProducts) {
        this.mContext = context;
        this.mProducts = mProducts;
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.product_item, parent, false);
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mProducts.get(position);
        Bitmap picture = BitmapFactory.decodeResource(mContext.getResources(), product.getPicture());
        holder.picture.setImageBitmap(picture);
        holder.txtName.setText(standardizeProductName(product.getName()));
        holder.txtPrice.setText(standardizeProductPrice(product.getPrice()));
    }

    private String standardizeProductName(String name) {
        if (name.length() <= 15)
            return name;
        return name.substring(0, 14) + "...";
    }

    private String standardizeProductPrice(int price) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView picture;
        public TextView txtName;
        public TextView txtPrice;


        public ViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picture);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) {
                Product product = mProducts.get(position);
                Intent intent = new Intent(mContext, SeeProductDetailsActivity.class);
                intent.putExtra("product", product);
                mContext.startActivity(intent);
            }
        }
    }
}
