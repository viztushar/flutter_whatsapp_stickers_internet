import 'package:json_annotation/json_annotation.dart';
part 'stickers.g.dart';

@JsonSerializable()
class Stickers {

  String imagefile;
  List emojis;

  Stickers({this.imagefile,this.emojis});

  factory Stickers.fromJson(Map<String, dynamic> json) =>
      _$StickersFromJson(json);

  Map<String, dynamic> toJson() => _$StickersToJson(this);

  String get imageFile => imagefile;
  List get emoji => emojis;

  set imageFile(String imagefile) {
    this.imagefile = imagefile;
  }
  
  set emoji(List emojis) {
    this.emojis = emojis;
  }

}