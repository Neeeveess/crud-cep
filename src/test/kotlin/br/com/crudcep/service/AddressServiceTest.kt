package br.com.crudcep.service

import br.com.crudcep.client.ViaCepApi
import br.com.crudcep.client.request.AddressRequest
import br.com.crudcep.client.response.AddressResponse
import br.com.crudcep.domain.Address
import br.com.crudcep.dto.AddressDTO
import br.com.crudcep.exceptions.CepExistsException
import br.com.crudcep.exceptions.CepNotFoundException
import br.com.crudcep.mapper.toDTO
import br.com.crudcep.mapper.toEntity
import br.com.crudcep.repository.AddressRepository
import br.com.crudcep.utils.RegexUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class AddressServiceTest {

    @Mock
    private lateinit var addressRepository: AddressRepository

    @Mock
    private lateinit var viaCepApi: ViaCepApi

    @InjectMocks
    private lateinit var addressService: AddressService


    @Test
    @DisplayName("Listar todos os endereços")
    fun listAllAddresses() {
        val mockAddresses = listOf(
            Address(
                cep = "12345-678",
                logradouro = "Rua Exemplo 1",
                complemento = "Apto 101",
                unidade = "",
                bairro = "Bairro Exemplo",
                localidade = "Cidade Exemplo",
                uf = "EX",
                ibge = "1234567",
                gia = "",
                ddd = "11",
                siafi = "1234"
            ),
            Address(
                cep = "23456-789",
                logradouro = "Avenida Exemplo 2",
                complemento = "Sala 202",
                unidade = "",
                bairro = "Bairro Exemplo 2",
                localidade = "Cidade Exemplo 2",
                uf = "EX",
                ibge = "2345678",
                gia = "",
                ddd = "22",
                siafi = "2345"
            ),
            Address(
                cep = "34567-890",
                logradouro = "Praça Exemplo 3",
                complemento = "Casa 3",
                unidade = "",
                bairro = "Bairro Exemplo 3",
                localidade = "Cidade Exemplo 3",
                uf = "EX",
                ibge = "3456789",
                gia = "",
                ddd = "33",
                siafi = "3456"
            )
        )

        Mockito.`when`(addressRepository.findAll()).thenReturn(mockAddresses)

        val addresses = addressService.getAll()

        assertEquals(mockAddresses, addresses)
    }

    @Test
    @DisplayName("Inserir endereço com base no CEP")
    fun insertAddress() {
        val cepTest = "12345678"
        val cepFormated = RegexUtils.convertCepFormated(cepTest)

        val addressMock = AddressResponse(
            cep = cepFormated,
            logradouro = "Rua Exemplo 1",
            complemento = "Apto 101",
            unidade = "",
            bairro = "Bairro Exemplo",
            localidade = "Cidade Exemplo",
            uf = "EX",
            ibge = "1234567",
            gia = "",
            ddd = "11",
            siafi = "1234"
        )

        Mockito.`when`(viaCepApi.getAddress(cepTest)).thenReturn(addressMock)
        Mockito.`when`(addressRepository.findById(cepFormated)).thenReturn(Optional.empty())
        Mockito.`when`(addressRepository.save(ArgumentMatchers.any(Address::class.java)))
            .thenReturn(addressMock.toDTO().toEntity())

        val result = addressService.insert(cepTest)

        assertEquals(addressMock.toDTO().toEntity().cep, result.cep)
    }

    @Test
    @DisplayName("Inserir endereço e lançar excessão de CEP já existe se já existir um CEP já cadastrado")
    fun insertAddressThrowCepExistsException() {
        val cepTest = "12345678"
        val cepFormated = RegexUtils.convertCepFormated(cepTest)

        val addressInDb = Address(
            cep = cepFormated,
            logradouro = "Rua Exemplo 1",
            complemento = "Apto 101",
            unidade = "",
            bairro = "Bairro Exemplo",
            localidade = "Cidade Exemplo",
            uf = "EX",
            ibge = "1234567",
            gia = "",
            ddd = "11",
            siafi = "1234"
        )

        Mockito.`when`(addressRepository.findById(cepFormated)).thenReturn(Optional.of(addressInDb))


        assertThrows<CepExistsException> {
            addressService.insert(cepTest)
        }
    }

    @Test
    @DisplayName("Inserir endereço e lançar excessão de CEP não encontrado se não encontrar esse CEP na API")
    fun insertAddressThrowCCepNotFoundException() {
        val cepTest = "12345678"
        val cepFormated = RegexUtils.convertCepFormated(cepTest)

        val errorMock = AddressResponse(
            cep = null,
            logradouro = null,
            complemento = null,
            unidade = null,
            bairro = null,
            localidade = null,
            uf = null,
            ibge = null,
            gia = null,
            ddd = null,
            siafi = null,
            erro = true
        )

        Mockito.`when`(viaCepApi.getAddress(cepTest)).thenReturn(errorMock)


        assertThrows<CepNotFoundException> {
            addressService.insert(cepTest)
        }
    }

    @Test
    @DisplayName("Atualizar endereço com base no CEP e retornar sucesso")
    fun updateAddress() {
        val cepTest = "12345678"
        val cepFormated = RegexUtils.convertCepFormated(cepTest)

        val addressInDb = Address(
            cep = cepFormated,
            logradouro = "Rua Exemplo 1",
            complemento = "Apto 101",
            unidade = "",
            bairro = "Bairro Exemplo",
            localidade = "Cidade Exemplo",
            uf = "EX",
            ibge = "1234567",
            gia = "",
            ddd = "11",
            siafi = "1234"
        )

        val addressRequest = AddressRequest(
            cep = cepFormated,
            logradouro = "Rua Exemplo 2",
            complemento = "Apto 102",
            unidade = "",
            bairro = "Bairro Exemplo",
            localidade = "Cidade Exemplo",
            uf = "EX",
            ibge = "1234567",
            gia = "",
            ddd = "11",
            siafi = "1234"
        )

        Mockito.`when`(addressRepository.findById(cepFormated)).thenReturn(Optional.of(addressInDb))
        Mockito.`when`(addressRepository.save(ArgumentMatchers.any(Address::class.java)))
            .thenReturn(addressRequest.toEntity())

        val result = addressService.update(cepTest, addressRequest)

        assertEquals(addressRequest.logradouro, result.logradouro)
        assertEquals(addressRequest.complemento, result.complemento)
    }

    @Test
    @DisplayName("Atualizar endereço com base no CEP e o CEP não existir no Banco lançar excessão CepNotFound")
    fun updateAddressThrowCepNotFoundException() {
        val cepTest = "12345678"
        val cepFormated = RegexUtils.convertCepFormated(cepTest)

        val addressRequest = AddressRequest(
            cep = cepFormated,
            logradouro = "Rua Exemplo 2",
            complemento = "Apto 102",
            unidade = "",
            bairro = "Bairro Exemplo",
            localidade = "Cidade Exemplo",
            uf = "EX",
            ibge = "1234567",
            gia = "",
            ddd = "11",
            siafi = "1234"
        )

        Mockito.`when`(addressRepository.findById(cepFormated)).thenReturn(Optional.empty())


        assertThrows<CepNotFoundException> {
            addressService.update(cepTest, addressRequest)
        }

    }

    @Test
    @DisplayName("Deletar endereço com base no CEP")
    fun deleteAddress() {
        val cepTest = "12345678"
        val cepFormated = RegexUtils.convertCepFormated(cepTest)

        val addressInDb = Address(
            cep = cepFormated,
            logradouro = "Rua Exemplo 1",
            complemento = "Apto 101",
            unidade = "",
            bairro = "Bairro Exemplo",
            localidade = "Cidade Exemplo",
            uf = "EX",
            ibge = "1234567",
            gia = "",
            ddd = "11",
            siafi = "1234"
        )

        Mockito.`when`(addressRepository.findById(cepFormated)).thenReturn(Optional.of(addressInDb))

        addressService.delete(cepTest)

        Mockito.verify(addressRepository).deleteById(cepFormated)
    }

    @Test
    @DisplayName("Deletar endereço com base no CEP e não encontrar, lançar excessão CepNotFound")
    fun deleteAddressThrowCepNotFoundException() {
        val cepTest = "12345678"
        val cepFormated = RegexUtils.convertCepFormated(cepTest)

        val addressInDb = Address(
            cep = cepFormated,
            logradouro = "Rua Exemplo 1",
            complemento = "Apto 101",
            unidade = "",
            bairro = "Bairro Exemplo",
            localidade = "Cidade Exemplo",
            uf = "EX",
            ibge = "1234567",
            gia = "",
            ddd = "11",
            siafi = "1234"
        )

        Mockito.`when`(addressRepository.findById(cepFormated)).thenReturn(Optional.empty())

        assertThrows<CepNotFoundException> {
            addressService.delete(cepTest)
        }
    }
}