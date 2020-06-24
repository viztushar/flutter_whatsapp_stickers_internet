package com.viztushar.flutter.flutter_stickers_internet

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Messenger
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.fxn.stash.Stash
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugins.GeneratedPluginRegistrant
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : FlutterActivity(), MethodChannel.MethodCallHandler {
    private var stickerPacks = ArrayList<StickerPack>()
//    override fun onCreate(savedInstanceState: Bundle) {
//        super.onCreate(savedInstanceState)
//        GeneratedPluginRegistrant.registerWith(this)
//        Log.d(TAG, "onCreate: " + applicationInfo.dataDir)
//        stickerPacks = Stash.getArrayList("sticker_pack", StickerPack::class.java)
//        MethodChannel(flutterView, CHANNEL).setMethodCallHandler { call, result ->
//
//        }
//    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        methodChannel.setMethodCallHandler(this)
    }

    fun addToJson(identifier: String?, name: String?, publisher: String?,
                  tray_image_file: String?, publisher_email: String?, publisher_website: String?, privacy_policy_website: String?,
                  license_agreement_website: String?,
                  sticker: ArrayList<*>) {
        Log.d(TAG, "addToJson: $tray_image_file")
        val stickers = ArrayList<Sticker>()
        for (i in sticker.indices) {
            stickers.add(Sticker(sticker[i].toString(), Arrays.asList(*"🙂,🙂".split(",".toRegex()).toTypedArray())))
        }
        val stickerPack = StickerPack(
                identifier,
                name,
                publisher,
                tray_image_file,
                publisher_email,
                publisher_website,
                privacy_policy_website,
                license_agreement_website, "1", true)
        stickerPack.setAndroidPlayStoreLink("https://play.google.com/store/apps/details?id=$packageName")
        stickerPack.setIosAppStoreLink("")
        stickerPack.setStickers(stickers)
        stickerPacks.add(stickerPack)

        //Log.d(TAG, "addToJson: " + stickerPacks.get(0).stickers  + " " + sticker.size());
        Stash.put("sticker_pack", stickerPacks)
        if (sticker.size == 3 || sticker.size > 3) {
            val intent = Intent()
            intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
            intent.putExtra(EXTRA_STICKER_PACK_ID, identifier)
            intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
            intent.putExtra(EXTRA_STICKER_PACK_NAME, name)
            try {
                startActivityForResult(intent, ADD_PACK)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this@MainActivity, "error", Toast.LENGTH_LONG).show()
            }
        } else {
            val toast = Toast.makeText(this@MainActivity, "You need 3 or more sticker per pack", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PACK) {
            if (resultCode == Activity.RESULT_CANCELED && data != null) {
                val validationError = data.getStringExtra("validation_error")
                if (validationError != null) {
                    if (BuildConfig.DEBUG) {
                        //validation error should be shown to developer only, not users.
                        //MessageDialogFragment.newInstance(R.string.title_validation_error, validationError).show(getSupportFragmentManager(), "validation error");
                    }
                    Log.e(TAG, "Validation failed:$validationError")
                }
            }
        }
    }

    fun addStickerPackToWhatsApp(identifier: String?, stickerPackName: String?) {
        try {
            //if neither WhatsApp Consumer or WhatsApp Business is installed, then tell user to install the apps.
            if (!WhitelistCheck.isWhatsAppConsumerAppInstalled(packageManager) && !WhitelistCheck.isWhatsAppSmbAppInstalled(packageManager)) {
                Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show()
                return
            }
            val stickerPackWhitelistedInWhatsAppConsumer = WhitelistCheck.isStickerPackWhitelistedInWhatsAppConsumer(this, identifier!!)
            val stickerPackWhitelistedInWhatsAppSmb = WhitelistCheck.isStickerPackWhitelistedInWhatsAppSmb(this, identifier)
            if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
                //ask users which app to add the pack to.
                launchIntentToAddPackToChooser(identifier, stickerPackName)
            } else if (!stickerPackWhitelistedInWhatsAppConsumer) {
                launchIntentToAddPackToSpecificPackage(identifier, stickerPackName, WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME)
            } else if (!stickerPackWhitelistedInWhatsAppSmb) {
                launchIntentToAddPackToSpecificPackage(identifier, stickerPackName, WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME)
            } else {
                Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "error adding sticker pack to WhatsApp", e)
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show()
        }
    }

    private fun launchIntentToAddPackToSpecificPackage(identifier: String?, stickerPackName: String?, whatsappPackageName: String) {
        val intent = createIntentToAddStickerPack(identifier, stickerPackName)
        intent.setPackage(whatsappPackageName)
        try {
            startActivityForResult(intent, ADD_PACK)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show()
        }
    }

    //Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
    private fun launchIntentToAddPackToChooser(identifier: String?, stickerPackName: String?) {
        val intent = createIntentToAddStickerPack(identifier, stickerPackName)
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.add_to_whatsapp)), ADD_PACK)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.add_pack_fail_prompt_update_whatsapp, Toast.LENGTH_LONG).show()
        }
    }

    private fun createIntentToAddStickerPack(identifier: String?, stickerPackName: String?): Intent {
        val intent = Intent()
        intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        intent.putExtra(EXTRA_STICKER_PACK_ID, identifier)
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
        intent.putExtra(EXTRA_STICKER_PACK_NAME, stickerPackName)
        return intent
    }

    companion object {
        private const val CHANNEL = "com.viztushar.flutter.flutter_stickers_internet/sharedata"
        const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
        const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
        const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"
        const val ADD_PACK = 200
        private val TAG = MainActivity::class.java.simpleName
        lateinit var methodChannel: MethodChannel
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "addTOJson") {
            Log.d(TAG, "onMethodCall: " + call.argument("identiFier"))
            Log.d(TAG,
                    call.argument<Any>("identiFier").toString() + " " + call.argument("name") + " " +
                            call.argument("publisher") + " " + call.argument("trayimagefile") + " " +
                            call.argument("publisheremail") + " " + call.argument("publisherwebsite") + " " +
                            call.argument("privacypolicywebsite") + " " + call.argument("licenseagreementwebsite") + " " +
                            call.argument("sticker_image")
            )
            addToJson(call.argument("identiFier"), call.argument("name"),
                    call.argument("publisher"), call.argument("trayimagefile"),
                    call.argument("publisheremail"), call.argument("publisherwebsite"),
                    call.argument("privacypolicywebsite"), call.argument("licenseagreementwebsite"),
                    Objects.requireNonNull(call.argument("sticker_image")))
        } else if (call.method == "addStickerPackToWhatsApp") {
            addStickerPackToWhatsApp(call.argument("identifier"), call.argument("name"))
        }
    }
}