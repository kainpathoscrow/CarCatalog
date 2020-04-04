package utils

sealed trait ServiceError {}

object DatabaseTimeoutError extends ServiceError
case class NotFoundError(description: String) extends ServiceError