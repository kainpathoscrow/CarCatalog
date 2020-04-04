package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.ModelService
import utils.DatabaseTimeoutError

class ModelController @Inject()(service: ModelService, val controllerComponents: ControllerComponents) extends BaseController {
  def read = Action { implicit request =>
    val modelNames = service.listAllModelNames()
    modelNames match {
      case Left(error) => error match {
        case DatabaseTimeoutError => InternalServerError("Database Connection Timeout")
        case _ => InternalServerError("Unknown error")
      }
      case Right(res) =>
        val json = Json.toJson(res)
        Ok(json)
    }
  }
}
