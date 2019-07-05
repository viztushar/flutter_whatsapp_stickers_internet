// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'stickers.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Stickers _$StickersFromJson(Map<String, dynamic> json) {
  return Stickers(
      imagefile: json['imagefile'] as String, emojis: json['emojis'] as List)
    ..imageFile = json['imageFile'] as String
    ..emoji = json['emoji'] as List;
}

Map<String, dynamic> _$StickersToJson(Stickers instance) => <String, dynamic>{
      'imagefile': instance.imagefile,
      'emojis': instance.emojis,
      'imageFile': instance.imageFile,
      'emoji': instance.emoji
    };
