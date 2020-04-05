package controllers

import javax.inject.Inject
import play.api.cache.Cached
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.ModelService
import utils.errors.ErrorToActionResultConverters.serviceErrorToActionResult

import scala.concurrent.duration._

class ModelController @Inject()(val cached: Cached, service: ModelService, val controllerComponents: ControllerComponents) extends BaseController {
  def read = cached.apply(_ => "allModels", 5.minutes.toSeconds.toInt) {
    Action { implicit request =>
      val modelNames = service.listAllModelNames
      modelNames match {
        case Left(error) => serviceErrorToActionResult(error)
        case Right(res) => Ok(Json.toJson(res))
      }
    }
  }
}
