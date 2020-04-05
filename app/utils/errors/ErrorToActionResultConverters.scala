package utils.errors

import play.api.mvc.Results.{NotFound, InternalServerError, BadRequest}

// TODO create default error handler for Await.result https://www.playframework.com/documentation/2.8.x/ScalaErrorHandling
object ErrorToActionResultConverters {
  def serviceErrorToActionResult(error: ServiceError) = error match {
    case DatabaseTimeoutError => InternalServerError(AppError("Database Connection Timeout").asJson)
    case NotFoundError(description) => NotFound(AppError(description).asJson)
    case AlreadyExistsError(description) => BadRequest(AppError(description).asJson)
    case ValidationError(description) => BadRequest(AppError(description).asJson)
    case _ => InternalServerError(AppError("Unknown error").asJson)
  }
}
