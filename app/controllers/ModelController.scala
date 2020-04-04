package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.ModelService
import utils.errors.ErrorToActionResultConverters.serviceErrorToActionResult

class ModelController @Inject()(service: ModelService, val controllerComponents: ControllerComponents) extends BaseController {
  def read = Action { implicit request =>
    val modelNames = service.listAllModelNames
    modelNames match {
      case Left(error) => serviceErrorToActionResult(error)
      case Right(res) => Ok(Json.toJson(res))
    }
  }
}
