import 'package:flutter/material.dart';
import 'package:visor_web/data/api.dart';
import 'package:visor_web/models/report.dart';
import 'package:visor_web/viewmodels/report_view_model.dart';
import 'package:visor_web/views/reporte_card.dart';
import 'package:visor_web/views/view_report.dart';

class Inicio extends StatefulWidget {
  const Inicio({super.key});

  @override
  State<Inicio> createState() => _Inicio();
}

class _Inicio extends State<Inicio> {
  int mostrando = 0;

  final ReportViewModel viewModel = ReportViewModel(api: Api());

  _Inicio();

  void _cambiarMostrando(int id) {
    setState(() {
      mostrando = id;
    });
  }

  @override
  Widget build(BuildContext context) {
    viewModel.obtenerReportes();

    return AnimatedBuilder(
      animation: viewModel,
      builder: (context, _) {
        return Center(
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
                  child: Column(
                    children: [
                      SingleChildScrollView(
                        child: SizedBox(
                          height: MediaQuery.of(context).size.height - 20,
                          child: ListView.builder(
                            itemCount: viewModel.reportes.length,
                            itemBuilder: (context, index) {
                              return ReporteCard(
                                reporte: viewModel.reportes[index],
                                viewModel: viewModel,
                                alTocar: (value) {
                                  _cambiarMostrando(value);
                                },
                              );
                            },
                          ),
                        ),
                      ),
                    ],
                  ),
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
                  child: Column(
                    children: [
                      ViewReport(
                        reporte: viewModel.obtenerPorId(mostrando),
                        viewModel: viewModel
                      )
                    ],
                  ),
                ),
              ),
            ],
          ),
        );
      },
    );
    
  }
}
