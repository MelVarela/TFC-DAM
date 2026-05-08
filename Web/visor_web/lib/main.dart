import 'package:flutter/material.dart';
import 'package:visor_web/views/inicio.dart';

void main() {
  runApp(MainApp());
}

class MainApp extends StatelessWidget {
  MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Inicio(),
      ),
    );
  }
}
