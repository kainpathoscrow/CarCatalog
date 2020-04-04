package controllers

import javax.inject.Inject
import models.CarDto
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.CarService
import utils.errors.ErrorToActionResultConverters.serviceErrorToActionResult

class CarController @Inject()(service: CarService, val controllerComponents: ControllerComponents) extends BaseController {
  def create = Action(parse.json) { implicit request =>
    request.body.validate[CarDto].fold(
      errors => BadRequest(errors.mkString), // TODO human-readable error list
      carDto => processCreate(carDto)
    )
  }

  def read = ???

  def delete(id: Int) = Action { implicit request =>
    val deletionResult = service.delete(id)
    deletionResult match {
      case Left(error) => serviceErrorToActionResult(error)
      case Right(id) => Ok(Json.obj("deletedId" -> id))
    }
  }

  private def processCreate(carDto: CarDto) = {
    val createResult = service.create(carDto)
    createResult match {
      case Left(error) => serviceErrorToActionResult(error)
      case Right(car) => Ok(Json.toJson(car))
    }
  }
}
