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

  set sticker(List<Stickers> stickers) {
    this.stickers = stickers;
  }
}
