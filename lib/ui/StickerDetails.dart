import 'dart:io';

import 'package:dio/dio.dart' as di;
import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';
import '../models/stickerPacks.dart';
import 'package:path/path.dart';

class MyStickerDetails extends StatefulWidget {
  final StickerPacks stickerPacks;
  MyStickerDetails({this.stickerPacks}) : super();
  @override
  _MyStickerDetailsState createState() => _MyStickerDetailsState();
}

class _MyStickerDetailsState extends State<MyStickerDetails> {
  bool isLoading, isDownloading = true;
  List<String> downloadList = List<String>();
  List<String> stickerImageList = List<String>();
  static const MethodChannel stickerMethodChannel = const MethodChannel(
      'com.viztushar.flutter.flutter_stickers_internet/sharedata');

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.stickerPacks.name),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.info_outline),
            onPressed: () {},
          ),
        ],
      ),
      body: Container(
        padding: EdgeInsets.all(15.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            Flexible(
              flex: 1,
              fit: FlexFit.loose,
              child: Container(
                child: Row(
                  children: <Widget>[
                    Image.network(
                      widget.stickerPacks.trayImageFile,
                      height: 50,
                      width: 50,
                    ),
                    Padding(
                      padding: EdgeInsets.fromLTRB(15.0, 0.0, 0.0, 15.0),
                    ),
                    Text(
                      widget.stickerPacks.name,
                      style: TextStyle(
                        fontSize: 18.0,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Flexible(
              flex: 1,
              fit: FlexFit.loose,
              child: Container(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Padding(
                      padding: EdgeInsets.fromLTRB(50.0, 2.0, 10.0, 0.0),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 60.0),
                      child: Text(
                        widget.stickerPacks.publisher,
                        style: TextStyle(
                          fontSize: 18.0,
                          fontWeight: FontWeight.normal,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Flexible(
              flex: 8,
              fit: FlexFit.tight,
              child: Container(
                child: GridView.builder(
                  shrinkWrap: false,
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 4,
                    childAspectRatio: 8.0 / 9.0,
                    crossAxisSpacing: 10.0,
                    mainAxisSpacing: 10.0,
                  ),
                  itemCount: widget.stickerPacks.sticker.length,
                  itemBuilder: (context, i) {
                    return Image.network(
                      widget.stickerPacks.sticker[i].imageFile,
                      height: 50.0,
                      width: 50.0,
                    );
                  },
                ),
              ),
            ),
            Flexible(
              flex: 1,
              fit: FlexFit.tight,
              child: Container(
                child: Align(
                    alignment: AlignmentDirectional.bottomCenter,
                    child: SizedBox.expand(
                      child: RaisedButton(
                        padding: EdgeInsets.all(15.0),
                        onPressed: () {
                          if (!downloadList
                              .contains(widget.stickerPacks.identiFier)) {
                            isDownloading = false;
                            print(isLoading);
                            downloadSticker(widget.stickerPacks,context);
                          } else if (downloadList
                              .contains(widget.stickerPacks.identiFier)) {
                                            addToWhatsapp(widget.stickerPacks);
                              }
                        },
                        color: Colors.black,
                        child: Text(
                          downloadList
                              .contains(widget.stickerPacks.identiFier) ? 'Add To WhatsApp' : "Download",
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 18.0,
                              fontWeight: FontWeight.bold),
                        ),
                      ),
                    )),
              ),
            ),
          ],
        ),
      ),
    );
  }

Future<void> addToWhatsapp(StickerPacks s) async {
    try {
      stickerMethodChannel.invokeMapMethod("addStickerPackToWhatsApp",
          {"identifier": s.identiFier, "name": s.name});
    } on PlatformException catch (e) {
      print(e.details);
    }
  }

  Future<void> downloadSticker(StickerPacks s,context) async {
    showDialogs(context);
    if (s.publisherEmail == null) s.publisherEmail = "0";
    print((s.publisherEmail == null).toString() +
        s.identiFier +
        " " +
        s.name +
        " " +
        s.publisher +
        " " +
        s.trayImageFile +
        " " +
        s.publisherEmail +
        " " +
        s.publisherWebsite +
        " " +
        s.privacyPolicyWebsite +
        " " +
        s.licenseAgreementWebsite.contains("").toString() +
        " ");

    stickerImageList.clear();
    if (!downloadList.contains(s.identiFier)) {
      await Permission.storage.request();
      di.Dio dio = di.Dio();
      var dirToSave = await getApplicationDocumentsDirectory();
      var path = await Directory(dirToSave.path +
              "/" +
              "stickers_asset" +
              "/" +
              s.identiFier +
              "/")
          .create(recursive: true);
      var trypath = await Directory(dirToSave.path +
              "/" +
              "stickers_asset" +
              "/" +
              s.identiFier +
              "/try/")
          .create(recursive: true);
      print(path.path + "\n" + trypath.path);

      String tryFilePath = trypath.path + basename(s.trayImageFile);
      print(tryFilePath);
      await dio.download(s.trayImageFile, tryFilePath,
          onReceiveProgress: (rec, total) {
        print((rec / total) * 100);
        print("try image downloaded");
      });

      for (int i = 0; i < s.sticker.length; i++) {
        String imageFilePath = path.path + basename(s.sticker[i].imageFile);
        stickerImageList.add(basename(s.sticker[i].imageFile));
        await dio.download(s.sticker[i].imageFile, imageFilePath,
            onReceiveProgress: (rec, total) {
          print((rec / total) * 100);
        });
      }

      try {
         stickerMethodChannel.invokeMapMethod("addTOJson", {
          "identiFier": s.identiFier,
          "name": s.name,
          "publisher": s.publisher,
          "trayimagefile": basename(s.trayImageFile),
          "publisheremail": s.publisherEmail,
          "publisherwebsite": s.publisherWebsite,
          "privacypolicywebsite": s.privacyPolicyWebsite,
          "licenseagreementwebsite": s.licenseAgreementWebsite,
          "sticker_image": stickerImageList,
        });
      } on PlatformException catch (e) {
        print(e.details);
      }
      Navigator.of(context, rootNavigator: true).pop();
      setState(() {
        isDownloading = true;
        if (!downloadList.contains(s.identiFier)) {
          downloadList.add(s.identiFier);
        }
      });
    } else {
      print("not");
    }
  }

Future<void> showDialogs(context) {
    CupertinoAlertDialog s = CupertinoAlertDialog(
      content: Row(
        children: <Widget>[
          CircularProgressIndicator(),
          SizedBox(
            width: 10,
          ),
          Text("Downloading..."),
        ],
      ),
    );

    AlertDialog a = AlertDialog(
      content: Row(
        children: <Widget>[
          CircularProgressIndicator(),
          SizedBox(
            width: 10,
          ),
          Text("Downloading..."),
        ],
      ),
    );

    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) {
        if(Platform.isAndroid)
        return a;
        else
        return s;
      },
    );
  }
}
