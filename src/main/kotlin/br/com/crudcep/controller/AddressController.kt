package br.com.crudcep.controller

import br.com.crudcep.client.request.AddressRequest
import br.com.crudcep.client.response.AddressResponse
import br.com.crudcep.domain.Address
import br.com.crudcep.dto.AddressDTO
import br.com.crudcep.exceptions.CepNotFoundException
import br.com.crudcep.exceptions.ErrorMessageModel
import br.com.crudcep.service.AddressService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/address")
@Tag(name = "Endereços", description = "Informações sobre os endereços com base no CEP fornecido")
class AddressController(val addressService: AddressService) {


    @GetMapping("/")
    @Operation(summary = "Listar endereços", description = "Essa função é responsável por listar todos os endereços")
    fun getAll(): ResponseEntity<List<Address>> {
        return ResponseEntity.ok(addressService.getAll())
    }

    @PostMapping("/{cep}")
    @Operation(summary = "Inserir endereço", description = "Essa função é responsável por inserir endereços")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", content = arrayOf(
            Content(schema = Schema(implementation = Address::class))
        )),
        ApiResponse(responseCode = "409", content = arrayOf(
            Content(schema = Schema(implementation = ErrorMessageModel::class))
        ))
    ])
    fun insert(@PathVariable("cep") cep: String): ResponseEntity<Address> {
            return ResponseEntity.status(HttpStatus.CREATED).body(addressService.insert(cep))
    }

    @PutMapping("/{cep}")
    @Operation(summary = "Atualizar endereço", description = "Essa função é responsável por atualizar o endereço especificado")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", content = arrayOf(
            Content(schema = Schema(implementation = Address::class))
        )),
        ApiResponse(responseCode = "404", content = arrayOf(
            Content(schema = Schema(implementation = ErrorMessageModel::class))
        ))
    ])
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados do endereço para atualização",
        content = [Content(schema = Schema(implementation = Address::class))]
    )
    fun update(@PathVariable("cep") cep: String, @RequestBody address: AddressRequest): ResponseEntity<Address> {
        return ResponseEntity.ok(addressService.update(cep, address))
    }

    @DeleteMapping("/{cep}")
    @Operation(summary = "Deletar endereço", description = "Essa função é responsável por deletar o endereço especificado")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "No content"),
        ApiResponse(responseCode = "404", content = arrayOf(
            Content(schema = Schema(implementation = ErrorMessageModel::class))
        ))
    ])
    fun delete(@PathVariable("cep") cep: String): ResponseEntity<Unit> {
        addressService.delete(cep)
        return ResponseEntity.noContent().build()
    }
}