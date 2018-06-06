package space.edge.d.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import space.edge.d.app.ui.AddressLayout;

/**
 * Created by gsl on 2018/6/4.
 */

public class TestActivity extends Activity {

    private AddressLayout al;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        al = findViewById(R.id.address_layout);

        List<String> addresses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            addresses.add("aaaaaaaa" + i);
        }

        al.setAddresses(addresses);
    }
}
