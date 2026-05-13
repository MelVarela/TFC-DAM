import 'package:flutter/material.dart';

class Login extends StatefulWidget {
  const Login({super.key});

  @override
  State<Login> createState() => _Login();
}

class _Login extends State<Login> {
  String nombre = "";
  String contrasinal = "";

  _Login();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          children: [
            TextField(
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                hintText: "Correo:",
              ),
              onSubmitted: (correo) => {nombre = correo},
            ),
            TextField(
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                hintText: "Contraseña:",
              ),
              obscureText: true,
              onSubmitted: (contra) => {contrasinal = contra},
            ),
            ElevatedButton(
              onPressed: () {
                /*
                En un despliegue real, aquí haríamos la conexión con la API para comprobar que las credenciales son correctas
                Como no disponemos de la conexion con la API, simplemente asumiremos que lo son
                */
                Navigator.pushNamed(context, '/reportes');
              },
              child: Text("Login"),
            ),
          ],
        ),
      ),
    );
  }
}
