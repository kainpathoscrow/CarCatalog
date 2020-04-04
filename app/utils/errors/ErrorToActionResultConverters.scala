package utils.errors

import play.api.libs.json.Json
import play.api.mvc.Results.InternalServerError
import utils.errors.ErrorConstants.{databaseTimeoutError, unknownError, alreadyExistsError}

// TODO create default error handler for Await.result https://www.playframework.com/documentation/2.8.x/ScalaErrorHandling
object ErrorToActionResultConverters {
  def serviceErrorToActionResult(error: ServiceError) = error match {
    case DatabaseTimeoutError => InternalServerError(AppError(databaseTimeoutError).asJson)
    case NotFoundError(description) => InternalServerError(AppError(description).asJson)
    case AlreadyExistsError => InternalServerError(AppError(alreadyExistsError).asJson)
    case _ => InternalServerError(AppError(unknownError).asJson)
  }
}
