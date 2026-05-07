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
      id: (json['id'] ?? 0).toInt(),
      texto: (json['content'] ?? '') as String,
      tipo: (json['type'] ?? '') as String,
    );
  }
}