import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:visor_web/models/report.dart';

class Api {

  static const String apiUrl = "https://69b989d7e69653ffe6a80299.mockapi.io/Suggestion";

  Future<List<dynamic>> getPostsJsonInformation() async {
    final url = Uri.parse(apiUrl);

    final res = await http.get(url);

    if (res.statusCode != 200) {
      throw Exception('HTTP ${res.statusCode}');
    }

    final decoded = jsonDecode(res.body);

    return decoded;
  }

}