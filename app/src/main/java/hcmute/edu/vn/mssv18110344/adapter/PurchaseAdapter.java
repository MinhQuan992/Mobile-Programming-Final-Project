package hcmute.edu.vn.mssv18110344.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.R;
import hcmute.edu.vn.mssv18110344.bean.Cart;
import hcmute.edu.vn.mssv18110344.bean.Purchase;
import hcmute.edu.vn.mssv18110344.bean.ViewPurchase;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder>{

    private Context mContext;
    private Cart mCart;
    private List<ViewPurchase> mViewPurchases;
    private DatabaseHandler mDb;
    private ImageView mImageView;
    private TextView mTextView;
    private TextView mTxtPrice;
    private ConstraintLayout mLayout;

    public PurchaseAdapter(Context context, Cart cart, List<ViewPurchase> viewPurchases, DatabaseHandler db, ImageView imageView, TextView textView, TextView txtPrice, ConstraintLayout layout) {
        mContext = context;
        mCart = cart;
        mViewPurchases = viewPurchases;
        mDb = db;
        mImageView = imageView;
        mTextView = textView;
        mTxtPrice = txtPrice;
        mLayout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.purchase_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewPurchase purchase = mViewPurchases.get(position);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), purchase.getPicture());
        holder.img.setImageBitmap(bitmap);
        holder.txtProductName.setText(standardizeProductName(purchase.getName()));
        holder.txtPrice.setText(standardizePrice(purchase.getPrice()));
        holder.txtAmount.setText(String.valueOf(purchase.getAmount()));
        mTxtPrice.setText(standardizePrice(totalCost()));
    }

    @Override
    public int getItemCount() {
        int count;
        try {
            count = mViewPurchases.size();
        } catch (Exception e) {
            count = 0;
            e.printStackTrace();
        }
        return count;
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

    private int totalCost() {
        int result = 0;
        for (int i = 0; i < mViewPurchases.size(); i++) {
            result += mViewPurchases.get(i).getPrice() * mViewPurchases.get(i).getAmount();
        }
        return result;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView txtProductName, txtPrice, txtAmount;
        public ImageButton btnSubtract, btnAdd, btnDelete;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);
            txtProductName = view.findViewById(R.id.txtProductName);
            txtPrice = view.findViewById(R.id.txtPrice);
            txtAmount = view.findViewById(R.id.txtAmount);
            btnSubtract = view.findViewById(R.id.btnSubtract);
            btnAdd = view.findViewById(R.id.btnAdd);
            btnDelete = view.findViewById(R.id.btnDelete);
            btnSubtract.setOnClickListener(this);
            btnAdd.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ViewPurchase viewPurchase = mViewPurchases.get(position);
                Purchase purchase = new Purchase(viewPurchase.getCartId(), viewPurchase.getProductId(), viewPurchase.getAmount(), viewPurchase.getPrice());
                switch (v.getId()) {
                    case R.id.btnSubtract:
                        if (purchase.getAmount() == 1)
                            return;
                        purchase.setAmount(purchase.getAmount() - 1);
                        mDb.updatePurchase(purchase);
                        mViewPurchases = mDb.getProductsInCart(mCart);
                        notifyDataSetChanged();
                        mTxtPrice.setText(standardizePrice(totalCost()));
                        break;
                    case R.id.btnAdd:
                        purchase.setAmount(purchase.getAmount() + 1);
                        mDb.updatePurchase(purchase);
                        mViewPurchases = mDb.getProductsInCart(mCart);
                        notifyDataSetChanged();
                        mTxtPrice.setText(standardizePrice(totalCost()));
                        break;
                    case R.id.btnDelete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Bạn chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")
                                .setCancelable(false)
                                .setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDb.deletePurchase(purchase);
                                        mViewPurchases = mDb.getProductsInCart(mCart);
                                        notifyDataSetChanged();
                                        Toast.makeText(mContext, "Xóa sản phẩm thành công!", Toast.LENGTH_LONG).show();
                                        if (mViewPurchases.size() == 0) {
                                            mImageView.setVisibility(View.VISIBLE);
                                            mTextView.setVisibility(View.VISIBLE);
                                            mLayout.setVisibility(View.INVISIBLE);
                                        }
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
                        break;
                }
            }
        }
    }
}
