package br.com.crudcep.service

import br.com.crudcep.client.ViaCepApi
import br.com.crudcep.client.request.AddressRequest
import br.com.crudcep.domain.Address
import br.com.crudcep.exceptions.CepExistsException
import br.com.crudcep.exceptions.CepNotFoundException
import br.com.crudcep.mapper.toDTO
import br.com.crudcep.mapper.toEntity
import br.com.crudcep.repository.AddressRepository
import br.com.crudcep.utils.RegexUtils
import org.springframework.stereotype.Service

@Service
class AddressService(
    val addressRepository: AddressRepository,
    val viaCepApi: ViaCepApi
) {


    fun getAll(): List<Address> {
        return addressRepository.findAll()
    }

    fun insert(cep: String): Address {
        val cepFormated = RegexUtils.convertCepFormated(cep)
        if (addressRepository.findById(cepFormated).isPresent) {
            throw CepExistsException()
        }

        val newAddress = viaCepApi.getAddress(cep)


        newAddress.erro?.let {
            if (it) {
                throw CepNotFoundException()
            }
        }
        return addressRepository.save(newAddress.toDTO().toEntity())


    }

    fun update(cep: String, addressToUpdate: AddressRequest): Address{
        val cepFormated = RegexUtils.convertCepFormated(cep)

        val addressExist =
            addressRepository.findById(cepFormated).orElseThrow { throw CepNotFoundException() }

        addressExist.apply {
            this.logradouro = addressToUpdate.logradouro?: this.logradouro
            this.complemento = addressToUpdate.complemento?: this.complemento
            this.bairro = addressToUpdate.bairro?: this.bairro
            this.localidade = addressToUpdate.localidade?: this.localidade
            this.uf = addressToUpdate.uf?: this.uf
            this.ibge = addressToUpdate.ibge?: this.ibge
            this.gia = addressToUpdate.gia?: this.gia
            this.ddd = addressToUpdate.ddd?: this.ddd
            this.siafi = addressToUpdate.siafi?: this.siafi
        }

        return addressRepository.save(addressExist)

    }

    fun delete(cep: String) {
        val cepFormated = RegexUtils.convertCepFormated(cep)
        addressRepository.findById(cepFormated).orElseThrow { throw CepNotFoundException() }

        addressRepository.deleteById(cepFormated)
    }

}
