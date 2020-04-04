package services

import javax.inject.Inject
import models.{Car, CarDto}
import repositiories.{CarRepository, ColorRepository, ModelRepository}
import utils.{DatabaseTimeoutError, NotFoundError, ServiceError}

import scala.concurrent.duration._
import scala.concurrent.{Await, TimeoutException}

class CarService @Inject()(repository: CarRepository, colorRepository: ColorRepository, modelRepository: ModelRepository) {
  def create(car: CarDto): Either[ServiceError, Car] = {
    try{
      val colorAndModel = for {
        color <- Await.result(colorRepository.findByName(car.color), 5.seconds)
        model <- Await.result(modelRepository.findByName(car.model), 5.seconds)
      } yield (color, model)

      colorAndModel match {
        case None => Left(NotFoundError(s"""Color "${car.color}" or model "${car.model}" not found"""))
        case Some((color, model)) => Right(Await.result(repository.create(car.copy(color = color.name, model = model.name)), 10.seconds))
      }
    }
    catch{
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }
}
