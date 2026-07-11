package nl.arjan.sqills.api

interface Response {
    fun getStatusCode(): Int
    fun getBody(): Any
}