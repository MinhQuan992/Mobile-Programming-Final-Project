package hcmute.edu.vn.mssv18110344.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.AddAndEditAddressActivity;
import hcmute.edu.vn.mssv18110344.ConfirmPurchaseActivity;
import hcmute.edu.vn.mssv18110344.MainActivity;
import hcmute.edu.vn.mssv18110344.R;
import hcmute.edu.vn.mssv18110344.bean.Address;
import hcmute.edu.vn.mssv18110344.bean.Cart;
import hcmute.edu.vn.mssv18110344.bean.User;
import hcmute.edu.vn.mssv18110344.utility.DatabaseHandler;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private List<Address> mAddresses;
    private User mUser;
    private Cart mCart;
    private ImageView mImageView;
    private TextView mTextView;
    private DatabaseHandler db;

    public AddressAdapter(Context context, Activity activity, List<Address> addresses, User user, Cart cart, ImageView imageView, TextView textView) {
        mContext = context;
        mActivity = activity;
        mAddresses = addresses;
        mUser = user;
        mCart = cart;
        mImageView = imageView;
        mTextView = textView;
        db = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.address_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address = mAddresses.get(position);
        holder.txtName.setText(address.getName());
        holder.txtPhone.setText(address.getPhone());
        holder.txtStreet.setText(address.getStreet());
        holder.txtWard.setText(db.getWardName(address.getWardId()));
        holder.txtDistrict.setText(db.getDistrictName(address.getDistrictId()));
        holder.txtProvince.setText(db.getProvinceName(address.getProvinceId()));
    }

    @Override
    public int getItemCount() {
        int count;
        try {
            count = mAddresses.size();
        } catch (Exception e) {
            count = 0;
            e.printStackTrace();
        }
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
        public LinearLayout layout;
        public TextView txtName, txtPhone, txtStreet, txtWard, txtDistrict, txtProvince;
        public ImageButton btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtStreet = itemView.findViewById(R.id.txtStreet);
            txtWard = itemView.findViewById(R.id.txtWard);
            txtDistrict = itemView.findViewById(R.id.txtDistrict);
            txtProvince = itemView.findViewById(R.id.txtProvince);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            layout.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (v.getId() == R.id.layout) {
                    if (mCart == null)
                        return;
                    Intent intent = new Intent(mContext, ConfirmPurchaseActivity.class);
                    intent.putExtra("cart", mCart);
                    intent.putExtra("address", mAddresses.get(position));
                    mContext.startActivity(intent);
                } else {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.inflate(R.menu.pop_up_menu);
                    popupMenu.setOnMenuItemClickListener(this);
                    popupMenu.show();
                }
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        Intent intent = new Intent(mContext, AddAndEditAddressActivity.class);
                        intent.putExtra("address", mAddresses.get(position));
                        intent.putExtra("user", mUser);
                        mContext.startActivity(intent);
                        return true;
                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Bạn chắc chắn muốn xóa địa chỉ này?")
                                .setCancelable(false)
                                .setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.deleteAddress(mAddresses.get(position).getId());
                                        mAddresses = db.getAddresses(mUser.getId());
                                        notifyDataSetChanged();
                                        Toast.makeText(mContext, "Xóa địa chỉ thành công!", Toast.LENGTH_LONG).show();
                                        if (mAddresses == null) {
                                            mImageView.setVisibility(View.VISIBLE);
                                            mTextView.setVisibility(View.VISIBLE);
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
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        }
    }
}
