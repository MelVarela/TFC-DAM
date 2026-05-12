import 'package:flutter/material.dart';
import 'package:visor_web/views/inicio.dart';
import 'package:visor_web/views/login.dart';

void main() {
  runApp(MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      initialRoute: "/",
      routes: {
        '/': (context) => Login(),
        '/reportes': (context) => Inicio()
      },
    );
  }
}
