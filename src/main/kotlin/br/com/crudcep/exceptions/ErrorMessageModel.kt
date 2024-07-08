package br.com.crudcep.exceptions

import io.swagger.v3.oas.annotations.media.Schema

class ErrorMessageModel(
    @Schema(example = "404")
    var statusCode: Int,

    @Schema(example = "Erro em questão")
    var message: String?,

    @Schema(example = "1531546894")
    var timestamp: Long
)