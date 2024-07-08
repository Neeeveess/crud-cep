package br.com.crudcep.domain

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Pattern

@Entity
@Table(name = "address")
class Address (
    @Id
    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "CEP inválido")
    val cep: String,
    var logradouro: String,
    var complemento: String,
    var unidade: String,
    var bairro: String,
    var localidade: String,
    var uf: String,
    var ibge: String,
    var gia: String,
    var ddd: String,
    var siafi: String
)