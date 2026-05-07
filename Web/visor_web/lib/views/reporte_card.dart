import 'package:flutter/material.dart';
import 'package:visor_web/models/report.dart';
import 'package:visor_web/viewmodels/report_view_model.dart';

class ReporteCard extends StatelessWidget {
  final Report reporte;
  final ReportViewModel viewModel;

  const ReporteCard({
    super.key,
    required this.reporte,
    required this.viewModel
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Padding(
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
          child: Text("${reporte.tipo}: ${reporte.texto}"),
        ),
      ),
    );
  }
}