package controllers

import javax.inject.Inject
import play.api.cache.Cached
import services.ColorService
import play.api.libs.json._
import play.api.mvc._
import utils.errors.ErrorToActionResultConverters.serviceErrorToActionResult

import scala.concurrent.duration._

class ColorController @Inject()(val cached: Cached, service: ColorService, val controllerComponents: ControllerComponents) extends BaseController {
  def read = cached.apply(_ => "allColors", 5.minutes.toSeconds.toInt) {
    Action { implicit request =>
      val colorNames = service.listAllColorNames
      colorNames match {
        case Left(error) => serviceErrorToActionResult(error)
        case Right(res) => Ok(Json.toJson(res))
      }
    }
  }
}
