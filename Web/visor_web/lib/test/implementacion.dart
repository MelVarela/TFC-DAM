import 'package:flutter_test/flutter_test.dart';
import 'package:visor_web/data/mock_api.dart';
import 'package:visor_web/models/report.dart';
import 'package:visor_web/viewmodels/report_view_model.dart';

void main(){

  MockApi datosFalsos = MockApi();

  ReportViewModel viewModel = ReportViewModel(api: datosFalsos);

  Report reporteFalso = Report(id: 1, texto: "Tendo pectus abundans compello suspendo consectetur in balbus. Carpo adsum volutabrum. Caput bestia sodalitas tenax ago aliqua tutamen comedo.", tipo: "S");

  viewModel.obtenerReportes();

  group('viewmodel', () {
    
    test('Obtener datos', () {
      expect(viewModel.reportes.isNotEmpty, true);
    });

    test('Obtener un reporte', () {
      expect(viewModel.obtenerPorId(1), reporteFalso);
    });
    
  });

}