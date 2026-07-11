package nl.arjan.sqills.api

data class SimpleResponse(
    private val statusCode: Int,
    private val body: Any
) : Response {

    override fun getStatusCode(): Int = statusCode

    override fun getBody(): Any = body
}