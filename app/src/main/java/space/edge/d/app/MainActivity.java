package space.edge.d.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.bitcoinj.core.ECKey;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CODE = 0x01;
    private static final int REQUEST_CODE_SIGN = 0x02;

    private TextView tv;

    private String msg = "test_scry_start";
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv_address);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_sign).setOnClickListener(this);
    }

    private void refresh(String text) {
        tv.setText(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    final String address = data.getStringExtra("address");
                    this.address = address;
                    refresh(address);
                    break;
                case 1000:
                    Toast.makeText(this, "start error", Toast.LENGTH_SHORT).show();
                case 1001:
                    Toast.makeText(this, "请先创建钱包", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == REQUEST_CODE_SIGN) {
            switch (resultCode){
                case Activity.RESULT_OK:
                    String signedMsg = data.getStringExtra("signed_msg");
                    if(TextUtils.isEmpty(signedMsg)){
                        Toast.makeText(this,"签名失败",Toast.LENGTH_SHORT).show();
                    }else{
                        verifySignature(signedMsg);
                    }
                    break;
                case 1001:
                    Toast.makeText(this, "请先创建钱包", Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(this, "签名消息为空", Toast.LENGTH_SHORT).show();
                    break;

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                getAddress();
                break;
            case R.id.btn_sign:
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(this, "请先获取地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                signMsg();
                break;
        }
    }

    private void getAddress() {
        String uri = Uri.decode("wallet://info.scry.wallet:1/address?id=" + getPackageName());
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_INFO);
        intent.setComponent(new ComponentName("info.scry.wallet", "info.scry.wallet.ExportAddressActivity"));
        intent.setData(Uri.parse(uri));
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void signMsg() {
        String uri = Uri.decode("wallet://info.scry.wallet:1/sign?id=" + getPackageName() + "&sign_msg=" + msg);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_INFO);
        intent.setComponent(new ComponentName("info.scry.wallet", "info.scry.wallet.ExportAddressActivity"));
        intent.setData(Uri.parse(uri));
        startActivityForResult(intent, REQUEST_CODE_SIGN);
    }

    private void verifySignature(String signed) {
        try {
            ECKey ecKey = ECKey.signedMessageToKey(msg, signed);
            byte[] pubKey = ecKey.getPubKeyPoint().getEncoded(false);
            BigInteger pubKey_ = new BigInteger(1, Arrays.copyOfRange(pubKey, 1, pubKey.length));
            String address = Numeric.prependHexPrefix(Keys.getAddress(pubKey_));
            if (this.address.equals(address)) {
                Toast.makeText(this,"签名验证通过",Toast.LENGTH_SHORT).show();
                tv.setText(tv.getText()+"\n 签名验证通过");
            }
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }
}