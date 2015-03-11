package com.lorentzos.adp.util;

import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dionysis_lorentzos on 25/2/15
 * for package com.lorentzos.adp.util
 * Use with caution dinosaurs might appear!
 */
public class APKUtil {

    public static void writeFile(byte[] bytes) throws IOException {
        String apkDir = Environment.getExternalStorageDirectory() + "/download/" + "app.apk";
        java.io.File file = new java.io.File(apkDir);
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(bytes);
        fop.flush();
        fop.close();
    }


}
