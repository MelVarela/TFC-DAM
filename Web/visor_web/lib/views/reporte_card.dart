import 'package:flutter/material.dart';
import 'package:visor_web/models/report.dart';
import 'package:visor_web/viewmodels/report_view_model.dart';

class ReporteCard extends StatelessWidget {
  final Report reporte;
  final ReportViewModel viewModel;
  final Function(int value) alTocar;

  const ReporteCard({
    super.key,
    required this.reporte,
    required this.viewModel,
    required this.alTocar
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
        onTap: () {
          alTocar(reporte.id);
        },
        child: Padding(
          padding: EdgeInsetsGeometry.all(8),
          child: Container(
            decoration: BoxDecoration(
              color: Color.fromARGB(255, 212, 241, 255),
              borderRadius: BorderRadius.circular(8),
              border: BoxBorder.all(
                color: Colors.black,
                width: 1,
                style: BorderStyle.solid,
              ),
            ),
            child: Padding(padding: EdgeInsetsGeometry.all(8), child: Text("${reporte.tipo}: ${reporte.texto}"),),
          ),
        ),
      );
  }
}
