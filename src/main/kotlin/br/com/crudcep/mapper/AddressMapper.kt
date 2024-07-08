package br.com.crudcep.mapper

import br.com.crudcep.client.request.AddressRequest
import br.com.crudcep.client.response.AddressResponse
import br.com.crudcep.domain.Address
import br.com.crudcep.dto.AddressDTO

fun AddressResponse.toDTO(): AddressDTO {
    return AddressDTO(
        cep = this.cep!!,
        logradouro = this.logradouro!!,
        complemento = this.complemento!!,
        bairro = this.bairro!!,
        unidade = this.unidade!!,
        localidade = this.localidade!!,
        uf = this.uf!!,
        ibge = this.ibge!!,
        gia = this.gia!!,
        ddd = this.ddd!!,
        siafi = this.siafi!!,
        erro = this.erro!!
    )
}

fun AddressDTO.toEntity(): Address {
    return Address(
        cep = this.cep!!,
        logradouro = this.logradouro!!,
        complemento = this.complemento!!,
        bairro = this.bairro!!,
        unidade = this.unidade!!,
        localidade = this.localidade!!,
        uf = this.uf!!,
        ibge = this.ibge!!,
        gia = this.gia!!,
        ddd = this.ddd!!,
        siafi = this.siafi!!
    )
}

fun AddressDTO.toResponse(): AddressResponse {
    return AddressResponse(
        cep = this.cep!!,
        logradouro = this.logradouro!!,
        complemento = this.complemento!!,
        bairro = this.bairro!!,
        unidade = this.unidade!!,
        localidade = this.localidade!!,
        uf = this.uf!!,
        ibge = this.ibge!!,
        gia = this.gia!!,
        ddd = this.ddd!!,
        siafi = this.siafi!!
    )
}

fun AddressRequest.toEntity(): Address {
    return Address(
        cep = this.cep!!,
        logradouro = this.logradouro!!,
        complemento = this.complemento!!,
        bairro = this.bairro!!,
        unidade = this.unidade!!,
        localidade = this.localidade!!,
        uf = this.uf!!,
        ibge = this.ibge!!,
        gia = this.gia!!,
        ddd = this.ddd!!,
        siafi = this.siafi!!
    )
}