package com.scp.adminforquiz.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.vk.sdk.util.VKUtil;

import java.security.MessageDigest;

import timber.log.Timber;

public class SystemUtils {

    private static String[] getCertificateFingerprints(final Context context) {
        return VKUtil.getCertificateFingerprint(context, context.getPackageName());
    }

    public static void printCertificateFingerprints(Context context) {
        final String[] fingerprints = getCertificateFingerprints(context);
        Timber.d("sha fingerprints");
        if (fingerprints != null) {
            for (final String sha1 : fingerprints) {
                Timber.d("sha1: %s", sha1);
            }
        } else {
            Timber.e(new NullPointerException(), "fingerprints arr is null!");
        }
        try {
            @SuppressLint("PackageManagerGetSignatures") final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (final Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String hashKey = new String(Base64.encode(md.digest(), 0));
                Timber.i("printHashKey() Hash Key: %s", hashKey);
            }
        } catch (final Exception e) {
            Timber.e(e);
        }
    }
}
