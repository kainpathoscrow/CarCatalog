package controllers

import javax.inject.Inject
import services.ColorService
import play.api.libs.json._
import play.api.mvc._
import utils.errors.ErrorToActionResultConverters.serviceErrorToActionResult

class ColorController @Inject()(service: ColorService, val controllerComponents: ControllerComponents) extends BaseController {
  def read = Action { implicit request =>
    val colorNames = service.listAllColorNames
    colorNames match {
      case Left(error) => serviceErrorToActionResult(error)
      case Right(res) => Ok(Json.toJson(res))
    }
  }
}
