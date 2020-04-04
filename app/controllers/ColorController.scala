package controllers

import javax.inject.Inject
import services.ColorService
import play.api.libs.json._
import play.api.mvc._

class ColorController @Inject()(service: ColorService, val controllerComponents: ControllerComponents) extends BaseController {
  def read = Action { implicit request =>
    val json = Json.toJson(service.listAllColorNames())
    Ok(json)
  }
}
