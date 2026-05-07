import 'package:flutter/material.dart';
import 'package:visor_web/models/report.dart';

class ReportViewModel extends ChangeNotifier {

  List _reportes = [
    Report(id: 1, texto: "La app crashea todo el tiempo", tipo: "R"),
    Report(id: 2, texto: "La app se ve muy fea", tipo: "S")
  ];
  int _viendo = 0;
  //Api api;

  void obtenerReportes() {

  }

  //BORRAR
  Report obtenerPorId(int id){
    return _reportes[id];
  }

  /*
  Report obtenerPorId(int id){

  }

  List<Report> obtenerTodos(){
  
  }
  */

  void cambiarViendo(int id){

  }

  void manejar(int id){

  }

}