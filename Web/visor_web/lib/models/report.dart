class Report {
  final int id;
  final String texto;
  final String tipo;
  
  Report({
    required this.id,
    required this.texto,
    required this.tipo
  });

  factory Report.fromJson(Map<String, dynamic> json){
    return Report(
      id: int.parse(json['id'] ?? 0),
      texto: (json['content'] ?? '') as String,
      tipo: (json['type'] ?? '') as String,
    );
  }

  @override
  String toString() {
    return "{$id - $tipo: $texto}";
  }

  @override
  bool operator ==(Object other) => other is Report && (other.id == id && other.texto == texto && other.tipo == tipo);
  
}