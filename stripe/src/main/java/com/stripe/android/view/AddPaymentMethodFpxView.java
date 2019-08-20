package com.stripe.android.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stripe.android.R;
import com.stripe.android.model.PaymentMethodCreateParams;

public class AddPaymentMethodFpxView extends AddPaymentMethodView {
    @NonNull private final Adapter mAdapter;

    @NonNull
    public static AddPaymentMethodFpxView create(@NonNull Context context) {
        return new AddPaymentMethodFpxView(context);
    }

    private AddPaymentMethodFpxView(@NonNull Context context) {
        super(context);
        inflate(getContext(), R.layout.add_payment_method_fpx_layout, this);
        mAdapter = new Adapter();
        final RecyclerView recyclerView = findViewById(R.id.fpx_list);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(null);
    }

    @Nullable
    @Override
    PaymentMethodCreateParams getCreateParams() {
        final FpxBank fpxBank = mAdapter.getSelectedBank();
        if (fpxBank == null) {
            return null;
        }

        return PaymentMethodCreateParams.create(
                new PaymentMethodCreateParams.Fpx.Builder()
                        .setBank(fpxBank.code)
                        .build()
        );
    }

    private static final class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private int mSelectedPosition = -1;

        private Adapter() {
            super();
            setHasStableIds(true);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fpx_bank, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
            viewHolder.update(FpxBank.values()[i].displayName);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO(mshafrir): update UI for selected bank
                    mSelectedPosition = viewHolder.getAdapterPosition();
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return FpxBank.values()[position].hashCode();
        }

        @Override
        public int getItemCount() {
            return FpxBank.values().length;
        }

        @Nullable
        FpxBank getSelectedBank() {
            if (mSelectedPosition == -1) {
                return null;
            } else {
                return FpxBank.values()[mSelectedPosition];
            }
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
            @NonNull private final TextView mName;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);

                mName = itemView.findViewById(R.id.name);
            }

            void update(@NonNull String bankName) {
                mName.setText(bankName);
            }
        }
    }

    private enum FpxBank {
        // TODO(mshafrir): add complete bank list

        AffinBank("affin_bank", "Affin Bank"),
        AllianceBankBusiness("alliance_bank", "Alliance Bank (Business)"),
        AmBank("ambank", "AmBank"),
        BankMuamalat("bank_muamalat", "Bank Muamalat"),
        DeutscheBank("deutsche_bank", "Deutsche Bank"),
        Maybank2e("maybank2e", "Maybank2E"),
        PublicBankEnterprise("pb_enterprise", "PB Enterprise"),
        StandardChartered("standard_chartered", "Standard Chartered"),
        UobBank("uob", "UOB Bank"),
        UobRegional("uob_regional", "UOB Regional");

        @NonNull private final String code;
        @NonNull private final String displayName;

        FpxBank(@NonNull String code, @NonNull String displayName) {
            this.code = code;
            this.displayName = displayName;
        }
    }
}
