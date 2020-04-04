package controllers

import javax.inject.Inject
import services.ColorService
import play.api.libs.json._
import play.api.mvc._
import utils.DatabaseTimeoutError
import utils.ErrorConstants._

class ColorController @Inject()(service: ColorService, val controllerComponents: ControllerComponents) extends BaseController {
  def read = Action { implicit request =>
    val colorNames = service.listAllColorNames
    colorNames match {
      case Left(error) => error match {
        case DatabaseTimeoutError => InternalServerError(databaseTimeoutError)
        case _ => InternalServerError(unknownError)
      }
      case Right(res) =>
        val json = Json.toJson(res)
        Ok(json)
    }
  }
}
