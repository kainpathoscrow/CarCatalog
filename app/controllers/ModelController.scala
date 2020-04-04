package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.ModelService

class ModelController @Inject()(service: ModelService, val controllerComponents: ControllerComponents) extends BaseController {
  def read = Action { implicit request =>
    val json = Json.toJson(service.listAllModelNames())
    Ok(json)
  }
}
