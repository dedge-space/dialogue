package space.edge.d.app.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import space.edge.d.app.R;

/**
 * Created by gsl on 2018/6/4.
 */

public class AddressLayout extends LinearLayout {

    private List<String> addresses;

    public AddressLayout(Context context) {
        super(context);
        init(context);
    }

    public AddressLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddressLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_address_list, this, true);
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
        addOrRmChild();
        for (int i = 0; i < getChildCount(); i++) {
            TextView tv = (TextView) getChildAt(i);
            String address = addresses.get(i);
            tv.setText(address);
        }
    }

    private void addOrRmChild() {
        int count = getChildCount();
        int dataSize = addresses.size();
        if (count > dataSize) {
            removeViews(dataSize, count);
        } else if (count < dataSize) {
            for (int i = count; i < dataSize; i++) {
                LayoutInflater.from(getContext()).inflate(R.layout.item_address_list, this, true);
            }
        }
    }

}
