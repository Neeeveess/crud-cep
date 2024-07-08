package br.com.crudcep.exceptions

class CepExistsException(message: String = "Cep já existe no banco de dados") : RuntimeException(message)