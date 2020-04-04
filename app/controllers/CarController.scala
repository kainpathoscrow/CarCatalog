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

  private def processCreate(carDto: CarDto) = {
    val creationResult = service.create(carDto)
    creationResult match {
      case Left(error) => serviceErrorToActionResult(error)
      case Right(car) => Ok(Json.toJson(car))
    }
  }

  def read = Action { implicit request =>
    val readResult = service.read
    readResult match {
      case Left(error) => serviceErrorToActionResult(error)
      case Right(cars) => Ok(Json.toJson(cars))
    }
  }

  def statistics = Action { implicit request =>
    val statisticsResult = service.statistics
    statisticsResult match {
      case Left(error) => serviceErrorToActionResult(error)
      case Right(statistics) => Ok(Json.toJson(statistics))
    }
  }

  def delete(id: Int) = Action { implicit request =>
    val deletionResult = service.delete(id)
    deletionResult match {
      case Left(error) => serviceErrorToActionResult(error)
      case Right(id) => Ok(Json.obj("deletedId" -> id))
    }
  }
}
