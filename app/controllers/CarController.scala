package controllers

import javax.inject.Inject
import models.{CarDto, CarsRequestParams}
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

  def read = Action { implicit request => // TODO better create QueryStringBindable[CarsRequestParams]
    val model = request.queryString.get("model").map(_.toList)
    val color = request.queryString.get("color").map(_.toList)
    val number = request.queryString.get("number").flatMap(_.headOption)
    val manufactureYearMin = request.queryString.get("manufactureYearMin").flatMap(_.headOption).flatMap(_.toIntOption)
    val manufactureYearMax = request.queryString.get("manufactureYearMax").flatMap(_.headOption).flatMap(_.toIntOption)
    val sortedBy = request.queryString.get("sortedBy").flatMap(_.headOption)
    val sortedAsc = request.queryString.get("sortingDirection").flatMap(_.headOption).map(_ != "-1")

    processRead(CarsRequestParams(model, color, number, manufactureYearMin, manufactureYearMax, sortedBy, sortedAsc))
  }
  private def processRead(carsRequestParams: CarsRequestParams) = {
    val readResult = service.read(Some(carsRequestParams))
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
