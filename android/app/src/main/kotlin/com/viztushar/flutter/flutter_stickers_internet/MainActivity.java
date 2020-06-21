package com.viztushar.flutter.flutter_stickers_internet;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.fxn.stash.Stash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;


public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "com.viztushar.flutter.flutter_stickers_internet/sharedata";
    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    public static final int ADD_PACK = 200;

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<StickerPack> stickerPacks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        Log.d(TAG, "onCreate: " + getApplicationInfo().dataDir);
        stickerPacks =  Stash.getArrayList("sticker_pack",StickerPack.class);
        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                if (call.method.equals("addTOJson")) {
                    Log.d(TAG, "onMethodCall: " + call.argument("identiFier"));
                    Log.d(TAG,
                            call.argument("identiFier") + " " + call.argument("name") + " " +
                                    call.argument("publisher") + " " + call.argument("trayimagefile") + " " +
                                    call.argument("publisheremail") + " " + call.argument("publisherwebsite") + " " +
                                    call.argument("privacypolicywebsite") + " " + call.argument("licenseagreementwebsite") + " " +
                                    call.argument("sticker_image")
                    );

                    addToJson(call.argument("identiFier"),call.argument("name"),
                            call.argument("publisher"),call.argument("trayimagefile"),
                            call.argument("publisheremail"),call.argument("publisherwebsite"),
                            call.argument("privacypolicywebsite"), call.argument("licenseagreementwebsite"),
                            Objects.requireNonNull(call.argument("sticker_image")));
                } else if(call.method.equals("addStickerPackToWhatsApp")){
                    addStickerPackToWhatsApp(call.argument("identifier"),call.argument("name"));
                }
            }
        });
    }

    public void addToJson(String identifier, String name, String publisher,
                          String tray_image_file, String publisher_email, String publisher_website, String privacy_policy_website,
                          String license_agreement_website,
                          ArrayList sticker) {
        Log.d(TAG, "addToJson: " + tray_image_file);
        ArrayList<Sticker> stickers = new ArrayList<>();
        for (int i = 0; i < sticker.size(); i++) {
            stickers.add(new Sticker(sticker.get(i).toString(), Arrays.asList("🙂,🙂".split(","))));
        }


        StickerPack stickerPack = new StickerPack(
                identifier,
                name,
                publisher,
                tray_image_file,
                publisher_email,
                publisher_website,
                privacy_policy_website,
                license_agreement_website,"1", true);

        stickerPack.setAndroidPlayStoreLink("https://play.google.com/store/apps/details?id=" + getPackageName().toString());
        stickerPack.setIosAppStoreLink("");

        stickerPack.setStickers(stickers);
        stickerPacks.add(stickerPack);

        //Log.d(TAG, "addToJson: " + stickerPacks.get(0).stickers  + " " + sticker.size());

        Stash.put("sticker_pack", stickerPacks);

        if ((sticker.size() == 3) || (sticker.size() > 3)) {
            Intent intent = new Intent();
            intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
            intent.putExtra(EXTRA_STICKER_PACK_ID, identifier);
            intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
            intent.putExtra(EXTRA_STICKER_PACK_NAME, name);
            try {
                startActivityForResult(intent, ADD_PACK);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast toast = Toast.makeText(MainActivity.this,"You need 3 or more sticker per pack", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PACK) {
            if (resultCode == Activity.RESULT_CANCELED && data != null) {
                final String validationError = data.getStringExtra("validation_error");
                if (validationError != null) {
                    if (BuildConfig.DEBUG) {
                        //validation error should be shown to developer only, not users.
                        //MessageDialogFragment.newInstance(R.string.title_validation_error, validationError).show(getSupportFragmentManager(), "validation error");
                    }
                    Log.e(TAG, "Validation failed:" + validationError);
                }
            }
        }
    }

    protected void addStickerPackToWhatsApp(String identifier, String stickerPackName) {
        try {
            //if neither WhatsApp Consumer or WhatsApp Business is installed, then tell user to install the apps.
            if (!WhitelistCheck.isWhatsAppConsumerAppInstalled(getPackageManager()) && !WhitelistCheck.isWhatsAppSmbAppInstalled(getPackageManager())) {
                Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
                return;
            }
            final boolean stickerPackWhitelistedInWhatsAppConsumer = WhitelistCheck.isStickerPackWhitelistedInWhatsAppConsumer(this, identifier);
            final boolean stickerPackWhitelistedInWhatsAppSmb = WhitelistCheck.isStickerPackWhitelistedInWhatsAppSmb(this, identifier);
            if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
                //ask users which app to add the pack to.
                launchIntentToAddPackToChooser(identifier, stickerPackName);
            } else if (!stickerPackWhitelistedInWhatsAppConsumer) {
                launchIntentToAddPackToSpecificPackage(identifier, stickerPackName, WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME);
            } else if (!stickerPackWhitelistedInWhatsAppSmb) {
                launchIntentToAddPackToSpecificPackage(identifier, stickerPackName, WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME);
            } else {
                Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "error adding sticker pack to WhatsApp",  e);
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
        }

    }

    private void launchIntentToAddPackToSpecificPackage(String identifier, String stickerPackName, String whatsappPackageName) {
        Intent intent = createIntentToAddStickerPack(identifier, stickerPackName);
        intent.setPackage(whatsappPackageName);
        try {
            startActivityForResult(intent, ADD_PACK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
        }
    }

    //Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
    private void launchIntentToAddPackToChooser(String identifier, String stickerPackName) {
        Intent intent = createIntentToAddStickerPack(identifier, stickerPackName);
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.add_to_whatsapp)), ADD_PACK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show();
        }
    }

    private Intent createIntentToAddStickerPack(String identifier, String stickerPackName) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra(EXTRA_STICKER_PACK_ID, identifier);
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra(EXTRA_STICKER_PACK_NAME, stickerPackName);
        return intent;
    }
}
