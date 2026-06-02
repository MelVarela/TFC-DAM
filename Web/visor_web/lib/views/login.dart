import 'package:flutter/material.dart';

class Login extends StatefulWidget {
  const Login({super.key});

  @override
  State<Login> createState() => _Login();
}

class _Login extends State<Login> {
  String nombre = "";
  String contrasinal = "";
  bool failedLogin = false;

  _Login();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: SizedBox(
          height: MediaQuery.of(context).size.height * 0.6,
          width: MediaQuery.of(context).size.width * 0.5,
          child: Container(
            decoration: BoxDecoration(
              color: Color.fromARGB(255, 255, 235, 211),
              border: BoxBorder.all(
                color: Colors.black,
                width: 1,
                style: BorderStyle.solid,
              ),
              borderRadius: BorderRadius.circular(16),
            ),

            child: Padding(
              padding: EdgeInsetsGeometry.all(32),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [

                  Text(
                    style: TextStyle(
                      fontSize: 32,
                      fontWeight: FontWeight.bold,
                    ),
                    "Login"
                    ),

                  Padding(
                    padding: EdgeInsetsGeometry.all(16),
                    child: TextField(
                      decoration: InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: "Correo:",
                        fillColor: Color.fromARGB(255, 212, 241, 255),
                      ),
                      onChanged:  (correo) => {setState(() {
                        nombre = correo;
                      })},
                      onSubmitted: (correo) => {setState(() {
                        nombre = correo;
                      })},
                    ),
                  ),

                  Padding(
                    padding: EdgeInsetsGeometry.all(16),
                    child: TextField(
                      decoration: InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: "Contraseña:",
                      ),
                      obscureText: true,
                      onChanged: (contra) => {setState(() {
                        contrasinal = contra;
                      })},
                      onSubmitted: (contra) => {setState(() {
                        contrasinal = contra;
                      })},
                    ),
                  ),
                  (failedLogin) ? Text("Las credenciales no son correctas.") : Text(""),
                  ElevatedButton(
                    onPressed: () {
                      /*
                En un despliegue real, aquí haríamos la conexión con la API para comprobar que las credenciales son correctas
                Como no disponemos de la conexion con la API, simplemente asumiremos que lo son
                */
                      if(nombre != "" && contrasinal != ""){
                        Navigator.pushNamed(context, '/reportes');
                      }else{
                        setState(() {
                          failedLogin = true;
                        });
                      }
                    },
                    style: ButtonStyle(
                      backgroundColor: WidgetStateProperty.all(Colors.black), //Color.fromRGBO(108, 150, 170, 255)
                      foregroundColor: WidgetStateProperty.all(Colors.white), //Color.fromRGBO(212, 241, 255, 255)
                      textStyle: WidgetStateProperty.all(TextStyle(
                        color: Colors.black
                      ))
                    ),
                    child: Text("Login"),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
