package br.com.crudcep.dto

data class AddressDTO(
    val cep: String?,
    val logradouro: String?,
    val complemento: String?,
    val bairro: String?,
    val unidade: String?,
    val localidade: String?,
    val uf: String?,
    val ibge: String?,
    val gia: String?,
    val ddd: String?,
    val siafi: String?,
    val erro: Boolean?
)
