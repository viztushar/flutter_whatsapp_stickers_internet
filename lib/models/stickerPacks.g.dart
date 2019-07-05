// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'stickerPacks.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

StickerPacks _$StickerPacksFromJson(Map<String, dynamic> json) {
  return StickerPacks(
      identifier: json['identifier'] as String,
      name: json['name'] as String,
      publisher: json['publisher'] as String,
      trayimagefile: json['tray_image_file'] as String,
      publisheremail: json['publisher_email'] as String,
      publisherwebsite: json['publisher_website'] as String,
      privacypolicywebsite: json['privacy_policy_website'] as String,
      licenseagreementwebsite: json['license_agreement_website'] as String,
      stickers: (json['stickers'] as List)
          ?.map((e) =>
              e == null ? null : Stickers.fromJson(e as Map<String, dynamic>))
          ?.toList())
    ..identiFier = json['identiFier'] as String
    ..names = json['names'] as String
    ..publishers = json['publishers'] as String
    ..trayImageFile = json['trayImageFile'] as String
    ..publisherEmail = json['publisherEmail'] as String
    ..publisherWebsite = json['publisherWebsite'] as String
    ..privacyPolicyWebsite = json['privacyPolicyWebsite'] as String
    ..licenseAgreementWebsite = json['licenseAgreementWebsite'] as String
    ..sticker = (json['sticker'] as List)
        ?.map((e) =>
            e == null ? null : Stickers.fromJson(e as Map<String, dynamic>))
        ?.toList();
}

Map<String, dynamic> _$StickerPacksToJson(StickerPacks instance) =>
    <String, dynamic>{
      'identifier': instance.identifier,
      'name': instance.name,
      'publisher': instance.publisher,
      'tray_image_file': instance.trayimagefile,
      'publisher_email': instance.publisheremail,
      'publisher_website': instance.publisherwebsite,
      'privacy_policy_website': instance.privacypolicywebsite,
      'license_agreement_website': instance.licenseagreementwebsite,
      'stickers': instance.stickers,
      'identiFier': instance.identiFier,
      'names': instance.names,
      'publishers': instance.publishers,
      'trayImageFile': instance.trayImageFile,
      'publisherEmail': instance.publisherEmail,
      'publisherWebsite': instance.publisherWebsite,
      'privacyPolicyWebsite': instance.privacyPolicyWebsite,
      'licenseAgreementWebsite': instance.licenseAgreementWebsite,
      'sticker': instance.sticker
    };
