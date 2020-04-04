package controllers

import javax.inject.Inject
import models.CarDto
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.CarService
import utils.DatabaseTimeoutError
import utils.ErrorConstants._

class CarController @Inject()(service: CarService, val controllerComponents: ControllerComponents) extends BaseController {
  def create = Action(parse.json) { implicit request =>
    request.body.validate[CarDto].fold(
      errors => BadRequest(errors.mkString),
      carDto => processCreate(carDto)
    )
  }

  def read = ???
  def delete(id: Int) = ???

  private def processCreate(carDto: CarDto) = {
    val createResult = service.create(carDto)
    createResult match {
      case Left(error) => error match { // TODO "ServiceError to ActionResult" default function
        case DatabaseTimeoutError => InternalServerError(databaseTimeoutError)
        case _ => InternalServerError(unknownError)
      }
      case Right(car) => Ok(Json.toJson(car))
    }
  }
}
