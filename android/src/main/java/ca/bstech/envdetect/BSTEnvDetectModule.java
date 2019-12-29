package ca.bstech.envdetect;

import com.facebook.react.bridge.Promise;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;

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
    public void getEnvAsync(String matchingFingerprint, Promise promise) {
        try {
            List<String> fingerprints = getCertificateSignatures();
            for ( String f: fingerprints) {
                if ( f.equalsIgnoreCase(matchingFingerprint)) {
                    promise.resolve("TESTING");
                }
            }
            promise.resolve("PRODUCTION");
        } catch (NameNotFoundException e) {
            promise.reject(E_CERT_ERROR, e);
        } catch (NoSuchAlgorithmException e1) {
            promise.reject(E_CERT_ERROR, e1);
        }
    }

    private List<String> getCertificateSignatures() throws NameNotFoundException, NoSuchAlgorithmException {

        PackageManager pm = this.getReactApplicationContext().getPackageManager();
        String packageName = this.getReactApplicationContext().getPackageName();
        int flags = PackageManager.GET_SIGNATURES;

        PackageInfo packageInfo = pm.getPackageInfo(packageName, flags);
        Signature[] signatures = packageInfo.signatures;

        List<String> array = new ArrayList<>();

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
            array.add(ret);
            Log.d("Example", "Cer: "+ ret);
        }
        return array;
    }

}
