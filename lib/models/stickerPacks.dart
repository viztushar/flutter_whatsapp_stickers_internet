import 'stickers.dart';
import 'package:json_annotation/json_annotation.dart';
part 'stickerPacks.g.dart';

@JsonSerializable()
class StickerPacks {
  String identifier;
  String name;
  String publisher;
  @JsonKey(name: 'tray_image_file')
  String trayimagefile;
  @JsonKey(name: 'publisher_email')
  String publisheremail;
  @JsonKey(name: 'publisher_website')
  String publisherwebsite;
  @JsonKey(name: 'privacy_policy_website')
  String privacypolicywebsite;
  @JsonKey(name: 'license_agreement_website')
  String licenseagreementwebsite;
  @JsonKey(name: 'image_data_version')
  String imagedataversion;
  @JsonKey(name: 'avoid_cache')
  bool avoidcache;
  @JsonKey(name: 'animated_sticker_pack')
  bool animatedstickerpack;
  @JsonKey(name: 'stickers')
  List<Stickers> stickers;

  StickerPacks(
      {this.identifier,
      this.name,
      this.publisher,
      this.trayimagefile,
      this.publisheremail,
      this.publisherwebsite,
      this.privacypolicywebsite,
      this.licenseagreementwebsite,
      this.imagedataversion,
      this.avoidcache,
      this.animatedstickerpack,
      this.stickers});

  factory StickerPacks.fromJson(Map<String, dynamic> json) =>
      _$StickerPacksFromJson(json);

  Map<String, dynamic> toJson() => _$StickerPacksToJson(this);

  String get identiFier => identifier;
  String get names => name;
  String get publishers => publisher;
  String get trayImageFile => trayimagefile;
  String get publisherEmail => publisheremail;
  String get publisherWebsite => publisherwebsite;
  String get privacyPolicyWebsite => privacypolicywebsite;
  String get licenseAgreementWebsite => licenseagreementwebsite;
  String get imageDataVersion => imagedataversion;
  bool get avoidcaches => avoidcache;
  bool get animatedstickerpacks => animatedstickerpack;
  List<Stickers> get sticker => stickers;

  set identiFier(String identifier) {
    this.identifier = identifier;
  }

  set names(String name) {
    this.name = name;
  }

  set publishers(String publisher) {
    this.publisher = publisher;
  }

  set trayImageFile(String trayimagefile) {
    this.trayimagefile = trayimagefile;
  }

  set publisherEmail(String publisheremail) {
    this.publisheremail = publisheremail;
  }

  set publisherWebsite(String publisherwebsite) {
    this.publisherwebsite = publisherwebsite;
  }

  set privacyPolicyWebsite(String privacypolicywebsite) {
    this.privacypolicywebsite = privacypolicywebsite;
  }

  set licenseAgreementWebsite(String licenseagreementwebsite) {
    this.licenseagreementwebsite = licenseagreementwebsite;
  }

  set imageDataVersion(String imagedataversion) {
    this.imagedataversion = imagedataversion;
  }

  set avoidCache(bool avoidcache) {
    this.avoidcache = avoidcache;
  }

  set animatedStickerPack(bool animatedstickerpack) {
    this.animatedstickerpack = animatedstickerpack;
  }

  set sticker(List<Stickers> stickers) {
    this.stickers = stickers;
  }
}
