package com.example.nmedia.exceptions

import java.lang.RuntimeException
import kotlin.math.cos

sealed class AppError(var code:String):RuntimeException() {
}

class ApiError(val status:Int , code: String):AppError(code)
class NetworkError():AppError("error_network")
class UnknownError():AppError("error_unknown")