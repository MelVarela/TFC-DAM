import 'package:flutter/material.dart';
import 'package:visor_web/models/report.dart';
import 'package:visor_web/viewmodels/report_view_model.dart';

class ViewReport extends StatelessWidget {
  Report reporte;
  ReportViewModel viewModel;

  ViewReport({required this.reporte, required this.viewModel});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsetsGeometry.all(8),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          SizedBox(
            height: MediaQuery.of(context).size.height * 0.7,
            width: MediaQuery.of(context).size.width * 0.6,
            child: Container(
              decoration: BoxDecoration(
                color: Color.fromARGB(255, 108, 150, 170),
                borderRadius: BorderRadius.circular(16),
                border: BoxBorder.all(
                  color: Colors.black,
                  width: 1,
                  style: BorderStyle.solid,
                ),
              ),
              child: Padding(
                padding: EdgeInsetsGeometry.all(16),
                child: Text("${reporte.tipo}: ${reporte.texto}"),
              ),
            ),
          ),
          Column(
            children: [
              Padding(
                padding: EdgeInsetsGeometry.all(8),
                child: ElevatedButton(
                  onPressed: () {
                    viewModel.manejar(reporte.id);
                  },
                  style: ButtonStyle(
                    backgroundColor: WidgetStateProperty.all(
                      Colors.black,
                    ), //Color.fromRGBO(108, 150, 170, 255)
                    foregroundColor: WidgetStateProperty.all(
                      Colors.white,
                    ), //Color.fromRGBO(212, 241, 255, 255)
                    textStyle: WidgetStateProperty.all(
                      TextStyle(color: Colors.black),
                    ),
                  ),
                  child: Text("Marcar como completado"),
                ),
              ),
              Padding(
                padding: EdgeInsetsGeometry.all(8),
                child: ElevatedButton(
                  onPressed: () {
                    viewModel.manejar(reporte.id);
                  },
                  style: ButtonStyle(
                    backgroundColor: WidgetStateProperty.all(
                      Colors.black,
                    ), //Color.fromRGBO(108, 150, 170, 255)
                    foregroundColor: WidgetStateProperty.all(
                      Colors.white,
                    ), //Color.fromRGBO(212, 241, 255, 255)
                    textStyle: WidgetStateProperty.all(
                      TextStyle(color: Colors.black),
                    ),
                  ),
                  child: Text("Ignorar"),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
