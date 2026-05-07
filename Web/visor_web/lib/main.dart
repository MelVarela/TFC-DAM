import 'package:flutter/material.dart';
import 'package:visor_web/viewmodels/report_view_model.dart';
import 'package:visor_web/views/reporte_card.dart';

void main() {
  runApp(MainApp());
}

class MainApp extends StatelessWidget {
  MainApp({super.key});

  final ReportViewModel viewModel = ReportViewModel();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Center(
          child: Row(
            children: [

              Expanded(
                flex: 2,
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.greenAccent,
                    border: BoxBorder.all(
                      color: Colors.black,
                      width: 1,
                      style: BorderStyle.solid,
                    ),
                  ),
                  child: Column(children: [

                    ReporteCard(reporte: viewModel.obtenerPorId(0), viewModel: viewModel),
                    ReporteCard(reporte: viewModel.obtenerPorId(1), viewModel: viewModel)

                  ]),
                ),
              ),

              Expanded(
                flex: 7,
                child: Container(
                  decoration: BoxDecoration(
                    color: Color.fromARGB(255, 132, 216, 94),
                    border: BoxBorder.all(
                      color: Colors.black,
                      width: 1,
                      style: BorderStyle.solid,
                    ),
                  ),
                  child: Column(children: [Text("On the right")]),
                ),
              ),

            ],
          ),
        ),
      ),
    );
  }
}
