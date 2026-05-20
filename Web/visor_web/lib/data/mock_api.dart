import 'package:visor_web/data/api.dart';

class MockApi extends Api {
  
  @override
  Future<List<dynamic>> getPostsJsonInformation() {
    return Future.value(
      [
        {
          "content":"Tendo pectus abundans compello suspendo consectetur in balbus. Carpo adsum volutabrum. Caput bestia sodalitas tenax ago aliqua tutamen comedo.",
          "type":"S",
          "id":"1"
        },
        {
          "content":"Terminatio teneo substantia corrupti deduco curvo admitto cauda. Valeo aestus adinventitias delibero animi volutabrum tenus abundans debeo totam. Nulla blandior approbo ars expedita damno.",
          "type":"S",
          "id":"5"
        }
      ]
    );
  }

  @override
  Future<void> manejar(int id) {
    return Future.value();
  }

}