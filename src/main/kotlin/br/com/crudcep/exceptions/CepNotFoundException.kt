package br.com.crudcep.exceptions

import io.swagger.v3.oas.annotations.media.Schema

class CepNotFoundException(
    message: String = "Cep não encontrado"
) : RuntimeException(message)