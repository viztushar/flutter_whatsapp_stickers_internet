package com.viztushar.flutter.flutter_stickers_internet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class StickerPack implements Parcelable {
   public final String identifier;
    public  final String name;
    public final String publisher;
    public String publisherEmail;
    public final String trayImageFile;
    public final String publisherWebsite;
    public final String privacyPolicyWebsite;
    public final String licenseAgreementWebsite;
    public final String imageDataVersion;
    public final boolean avoidCache;
    public final boolean animatedStickerPack;

    public String iosAppStoreLink;
    public  List<Sticker> stickers;
    public  long totalSize;
    public  String androidPlayStoreLink;
    private boolean isWhitelisted;

    StickerPack(String identifier, String name, String publisher, String trayImageFile, String publisherEmail, String publisherWebsite, String privacyPolicyWebsite, String licenseAgreementWebsite, String imageDataVersion, boolean avoidCache, boolean animatedStickerPack) {
        this.identifier = identifier;
        this.name = name;
        this.publisher = publisher;
        this.trayImageFile = trayImageFile;
        this.publisherEmail = publisherEmail;
        this.publisherWebsite = publisherWebsite;
        this.privacyPolicyWebsite = privacyPolicyWebsite;
        this.licenseAgreementWebsite = licenseAgreementWebsite;
        this.imageDataVersion = imageDataVersion;
        this.avoidCache = avoidCache;
        this.animatedStickerPack = animatedStickerPack;
    }

    void setIsWhitelisted(boolean isWhitelisted) {
        this.isWhitelisted = isWhitelisted;
    }

    boolean getIsWhitelisted() {
        return isWhitelisted;
    }

    private StickerPack(Parcel in) {
        identifier = in.readString();
        name = in.readString();
        publisher = in.readString();
        trayImageFile = in.readString();
        publisherEmail = in.readString();
        publisherWebsite = in.readString();
        privacyPolicyWebsite = in.readString();
        licenseAgreementWebsite = in.readString();
        iosAppStoreLink = in.readString();
        stickers = in.createTypedArrayList(Sticker.CREATOR);
        totalSize = in.readLong();
        androidPlayStoreLink = in.readString();
        isWhitelisted = in.readByte() != 0;
        imageDataVersion = in.readString();
        avoidCache = in.readByte() != 0;
        animatedStickerPack = in.readByte() != 0;
    }

    public static final Creator<StickerPack> CREATOR = new Creator<StickerPack>() {
        @Override
        public StickerPack createFromParcel(Parcel in) {
            return new StickerPack(in);
        }

        @Override
        public StickerPack[] newArray(int size) {
            return new StickerPack[size];
        }
    };

    void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
        totalSize = 0;
        for (Sticker sticker : stickers) {
            totalSize += sticker.size;
        }
    }

    void setAndroidPlayStoreLink(String androidPlayStoreLink) {
        this.androidPlayStoreLink = androidPlayStoreLink;
    }

    void setIosAppStoreLink(String iosAppStoreLink) {
        this.iosAppStoreLink = iosAppStoreLink;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    long getTotalSize() {
        return totalSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        dest.writeString(name);
        dest.writeString(publisher);
        dest.writeString(trayImageFile);
        dest.writeString(publisherEmail);
        dest.writeString(publisherWebsite);
        dest.writeString(privacyPolicyWebsite);
        dest.writeString(licenseAgreementWebsite);
        dest.writeString(iosAppStoreLink);
        dest.writeTypedList(stickers);
        dest.writeLong(totalSize);
        dest.writeString(androidPlayStoreLink);
        dest.writeByte((byte) (isWhitelisted ? 1 : 0));
        dest.writeString(imageDataVersion);
        dest.writeByte((byte) (avoidCache ? 1 : 0));
        dest.writeByte((byte) (animatedStickerPack ? 1 : 0));
    }
}