package ca.bstech.envdetect;

import com.facebook.react.bridge.Promise;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Log;

public class BSTEnvDetectModule extends ReactContextBaseJavaModule {

    private static final String E_CERT_ERROR = "E_CERT_ERROR";

    public BSTEnvDetectModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.getPackageManager();
    }

    @Override
    public String getName() {
        return "BSTEnvDetect";
    }

    @ReactMethod
    public void getCertificateSignature(Promise promise) {

        PackageManager pm = this.getReactApplicationContext().getPackageManager();
        String packageName = this.getReactApplicationContext().getPackageName();
        int flags = PackageManager.GET_SIGNATURES;

        PackageInfo packageInfo = null;

        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (NameNotFoundException e) {
            promise.reject(E_CERT_ERROR, e);
        }
        Signature[] signatures = packageInfo.signatures;

        WritableArray array = Arguments.createArray();
        try {
            for ( Signature signature: signatures ) {

                byte[] cert = signature.toByteArray();

                MessageDigest md = MessageDigest.getInstance("SHA1");
                byte[] digest = md.digest(cert);

                StringBuilder hexString = new StringBuilder();
                for (int i=0;i<digest.length;i++) {
                    if ( i > 0 ) hexString.append(':');
                    String appendString = Integer.toHexString(0xFF & digest[i]);
                    if(appendString.length()==1)hexString.append("0");
                    hexString.append(appendString);
                }
                String ret = hexString.toString();
                array.pushString(ret);
                Log.d("Example", "Cer: "+ ret);
            }
            promise.resolve(array);
        } catch (NoSuchAlgorithmException e1) {
            promise.reject(E_CERT_ERROR, e1);
        }
    }

}
