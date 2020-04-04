package services

import javax.inject.Inject
import models.{Car, CarDto}
import repositiories.{CarRepository, ColorRepository, ModelRepository}
import utils.errors.{DatabaseTimeoutError, NotFoundError, ServiceError}

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

  def read = {
    try{
      val cars = Await.result(repository.listAll(), 5.seconds)
      Right(cars)
    }
    catch{
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }

  def delete(id: Int) = {
    try{
      var deletionResult = Await.result(repository.delete(id), 5.seconds)
      if (deletionResult == 0) Left(NotFoundError(s"Car with id=${id} was not found"))
      else Right(id)
    }
    catch{
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }
}
