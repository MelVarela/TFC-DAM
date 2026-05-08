import 'package:flutter/material.dart';
import 'package:visor_web/data/api.dart';
import 'package:visor_web/models/report.dart';

class ReportViewModel extends ChangeNotifier {

  final List<Report> _reportes = [];
  List<Report> get reportes => _reportes;
  
  Api api;

  ReportViewModel({required this.api});

  void obtenerReportes() {
    api.getPostsJsonInformation().then((List<dynamic> result) {
      for (var i = 0; i < result.length; i++) {
        if(!_reportes.contains(Report.fromJson(result[i]))){
          _reportes.add(Report.fromJson(result[i]));
        }
      }
      notifyListeners();
    });
    
  }

  Report? obtenerPorId(int id){
    try{
      return _reportes.firstWhere((report) => report.id == id) as Report?;
    }catch(e){
      return null;
    }
  }

  String obtenerTexto(int id){
    try{
      return _reportes.firstWhere((report) => report.id == id).toString();
    }catch(e){
      return "Cargando...";
    }
  }

  void manejar(int id){

  }

}