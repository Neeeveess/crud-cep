package br.com.crudcep.utils

object RegexUtils {

    fun convertCepFormated(cep: String): String {
        return cep.replaceFirst(Regex("(\\d{5})(\\d{3})"), "$1-$2")
    }

    fun convertCepUnformated(cep: String): String {
        return cep.replace("-", "")
    }
}