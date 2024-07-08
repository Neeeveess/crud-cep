package br.com.crudcep.integration

import br.com.crudcep.service.AddressService
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.junit.jupiter.api.*
import org.junit.jupiter.api.ClassOrderer.OrderAnnotation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AddressControllerIT(@Autowired val mockMvc: MockMvc) {

    private lateinit var wireMockServer: WireMockServer


    @BeforeEach
    fun setup() {
        wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(8989))
        wireMockServer.start()
        configureFor("localhost", wireMockServer.port())
        stubFor(
            get(urlEqualTo("/37640000/json/"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"cep\": \"37640-000\",\"logradouro\": \"\",\"complemento\": \"\",\"unidade\": \"\",\"bairro\": \"\",\"localidade\": \"Extrema\",\"uf\": \"MG\",\"ibge\": \"3125101\",\"gia\": \"\",\"ddd\": \"35\",\"siafi\": \"4501\"}")
                        .withStatus(200)
                )
        )
        System.setProperty("viacep.url", "http://localhost:${wireMockServer.port()}")
    }


    @Test
    @Order(1)
    @DisplayName("Listar todos os endereços e retornar vazio (nenhum endereço cadastrado ainda)")
    fun listAllAddressReturnEmpty(){
        mockMvc.perform(MockMvcRequestBuilders
            .get("/address/")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isEmpty)
    }



    @Test
    @Order(2)
    @DisplayName("Inserir endereço e retornar sucesso")
    fun insertAddressReturnSuccess() {
        mockMvc.perform(MockMvcRequestBuilders.post("/address/37640000")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            )

    }

    @Test
    @Order(3)
    @DisplayName("Inserir endereço e retornar excessão Cep existente")
    fun insertAddressThrowExceptionCepExists() {
        mockMvc.perform(MockMvcRequestBuilders.post("/address/37640000")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Cep já existe no banco de dados"))

    }

    @Test
    @Order(4)
    @DisplayName("Inserir endereço e retornar excessão Cep não existe")
    fun insertAddressThrowExceptionCepNotFound() {
        mockMvc.perform(MockMvcRequestBuilders.post("/address/37640111")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Cep não encontrado"))

    }

    @Test
    @Order(5)
    @DisplayName("Listar todos os endereços e retornar algum endereço cadastrado")
    fun listAllAddressReturnNotEmpty(){

        mockMvc.perform(MockMvcRequestBuilders
            .get("/address/")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].cep").isNotEmpty)
    }

    @Test
    @Order(6)
    @DisplayName("Atualizar endereço e retornar sucesso")
    fun updateAddressReturnSuccess() {
        val requestBody = """
        {
            "logradouro": null,
            "complemento": null,
            "unidade": null,
            "bairro": null,
            "localidade": "Nenhum lugar",
            "uf": "Não se sabe",
            "ibge": null,
            "gia": null,
            "ddd": null,
            "siafi": null
        }
    """.trimIndent()

        mockMvc.perform(MockMvcRequestBuilders.put("/address/37640000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.cep").value("37640-000"))
            .andExpect(jsonPath("$.localidade").value("Nenhum lugar"))
            .andExpect(jsonPath("$.uf").value("Não se sabe"))
    }

    @Test
    @Order(7)
    @DisplayName("Atualizar endereço e retornar excessão Cep não encontrado")
    fun updateAddressThrowCepNotFound() {
        val requestBody = """
        {
            "logradouro": null,
            "complemento": null,
            "unidade": null,
            "bairro": null,
            "localidade": "Nenhum lugar",
            "uf": "Não se sabe",
            "ibge": null,
            "gia": null,
            "ddd": null,
            "siafi": null
        }
    """.trimIndent()

        mockMvc.perform(MockMvcRequestBuilders.put("/address/37640111")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isNotFound)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Cep não encontrado"))
    }

    @Test
    @Order(8)
    @DisplayName("Deletar endereço e retornar sucesso")
    fun deleteAddressReturnSuccess() {

        mockMvc.perform(MockMvcRequestBuilders.delete("/address/37640000")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
    }

    @Test
    @Order(9)
    @DisplayName("Deletar endereço e retornar excessão Cep não encontrado")
    fun deleteAddressThrowCepNotFound() {

        mockMvc.perform(MockMvcRequestBuilders.delete("/address/37640111")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Cep não encontrado"))
    }

    @AfterEach
    fun tearDown(){
        wireMockServer.stop()
    }
}