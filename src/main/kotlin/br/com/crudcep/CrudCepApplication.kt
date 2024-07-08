package br.com.crudcep

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class CrudCepApplication

fun main(args: Array<String>) {
    runApplication<CrudCepApplication>(*args)
}
