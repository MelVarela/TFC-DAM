import 'package:flutter/material.dart';
import 'package:visor_web/models/report.dart';
import 'package:visor_web/viewmodels/report_view_model.dart';

class ViewReport extends StatelessWidget{
  Report reporte;
  ReportViewModel viewModel;

  ViewReport({
    required this.reporte,
    required this.viewModel
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsetsGeometry.all(8),
      child: Container(
        decoration: BoxDecoration(
          color: Color.fromARGB(255, 57, 123, 167),
          border: BoxBorder.all(
            color: Colors.black,
            width: 1,
            style: BorderStyle.solid,
          ),
        ),
        child: Column(
          children: [
            Text("${reporte.tipo}: ${reporte.texto}"),
            ElevatedButton(onPressed: (){viewModel.manejar(reporte.id);}, child: Text("Marcar como completado")),
            ElevatedButton(onPressed: (){viewModel.manejar(reporte.id);}, child: Text("Ignorar")),
          ],
        ),
      ),
    );
  }
}