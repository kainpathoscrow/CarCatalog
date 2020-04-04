package utils

sealed trait ServiceError {}

object DatabaseTimeoutError extends ServiceError
object NotFoundError extends ServiceError