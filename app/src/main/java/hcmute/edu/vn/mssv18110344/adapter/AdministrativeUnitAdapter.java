package hcmute.edu.vn.mssv18110344.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110344.R;
import hcmute.edu.vn.mssv18110344.bean.AdministrativeUnit;

public class AdministrativeUnitAdapter extends RecyclerView.Adapter<AdministrativeUnitAdapter.ViewHolder> {
    private Context mContext;
    private List<AdministrativeUnit> mUnits;
    private String mType;
    private Activity mActivity;

    public AdministrativeUnitAdapter(Context context, List<AdministrativeUnit> units, String type, Activity activity) {
        mContext = context;
        mUnits = units;
        mType = type;
        mActivity = activity;
    }

    @NonNull
    @Override
    public AdministrativeUnitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.administrative_unit_item, parent, false);
        return new ViewHolder(view, mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull AdministrativeUnitAdapter.ViewHolder holder, int position) {
        AdministrativeUnit unit = mUnits.get(position);
        holder.txtName.setText(unit.getName());
    }

    @Override
    public int getItemCount() {
        return mUnits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtName;
        private Activity mActivity;

        public ViewHolder(View itemView, Activity activity) {
            super(itemView);
            mActivity = activity;
            txtName = itemView.findViewById(R.id.txtName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent();
                intent.putExtra("unit", mUnits.get(position));
                intent.putExtra("type", mType);
                mActivity.setResult(Activity.RESULT_OK, intent);
                mActivity.finish();
            }
        }
    }
}
