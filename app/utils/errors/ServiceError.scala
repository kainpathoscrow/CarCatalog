package utils.errors

sealed trait ServiceError {}

object DatabaseTimeoutError extends ServiceError
case class AlreadyExistsError(description: String) extends ServiceError
case class NotFoundError(description: String) extends ServiceError
case class ValidationError(description: String) extends ServiceError