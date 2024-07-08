package br.com.crudcep.client

import br.com.crudcep.client.response.AddressResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@FeignClient(name = "viacep", url = "\${viacep.url}")
interface ViaCepApi {

    @GetMapping("/{cep}/json/")
    fun getAddress(@PathVariable cep: String): AddressResponse
}