package com.example.database;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private static final String TAG = "EmployeeAdapter";
    private Context mContext;
    private Cursor mCursor;
    private AdapterInterface mClickListener;

    public EmployeeAdapter(Context mContext, Cursor mCursor, AdapterInterface clickListener) {
        this.mContext = mContext;
        this.mCursor = mCursor;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i(TAG, "onCreateViewHolder: " + i);
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item, viewGroup, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder employeeViewHolder, int i) {
        if (!mCursor.moveToPosition(i)) {
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(EmployeeDBHelper.COLUMN_NAME));
        String address = mCursor.getString(mCursor.getColumnIndex(EmployeeDBHelper.COLUMN_ADDRESS));
        String phone = mCursor.getString(mCursor.getColumnIndex(EmployeeDBHelper.COLUMN_PHONE));

        employeeViewHolder.tvName.setText(name);
        employeeViewHolder.tvAddress.setText(address);
        employeeViewHolder.tvPhone.setText(phone);

        Log.i(TAG, "onBindViewHolder: " + i);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + mCursor.getCount());
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
        Log.i(TAG, "swapCursor: ");

    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAddress, tvPhone;
        Button btnEdit, btnDelete;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_Name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvPhone = itemView.findViewById(R.id.tv_Phone);
            btnEdit = itemView.findViewById(R.id.btn_Edit);
            btnDelete = itemView.findViewById(R.id.btn_Delete);
            Log.i(TAG, "EmployeeViewHolder: ");

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.editOnClick(v, getAdapterPosition());
                    Log.i(TAG, "onClick: Edit Button");
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.deleteOnClick(v, getAdapterPosition());
                    Log.i(TAG, "onClick: Delete button");
                }
            });
        }
    }

    public interface AdapterInterface {

        void editOnClick(View v, int position);

        void deleteOnClick(View v, int position);
    }
}
