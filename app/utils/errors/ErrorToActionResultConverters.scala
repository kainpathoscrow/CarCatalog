package utils.errors

import play.api.libs.json.Json
import play.api.mvc.Results.{NotFound, InternalServerError, BadRequest}
import utils.errors.ErrorConstants.{databaseTimeoutError, unknownError, alreadyExistsError}

// TODO create default error handler for Await.result https://www.playframework.com/documentation/2.8.x/ScalaErrorHandling
object ErrorToActionResultConverters {
  def serviceErrorToActionResult(error: ServiceError) = error match {
    case DatabaseTimeoutError => InternalServerError(AppError(databaseTimeoutError).asJson)
    case NotFoundError(description) => NotFound(AppError(description).asJson)
    case AlreadyExistsError(description) => BadRequest(AppError(description).asJson)
    case _ => InternalServerError(AppError(unknownError).asJson)
  }
}
