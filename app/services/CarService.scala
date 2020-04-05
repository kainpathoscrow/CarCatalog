package services

import java.util.concurrent.Executors

import javax.inject.Inject
import models.{Car, CarDto, CarStatistics, CarsRequestParams}
import repositiories.{CarRepository, ColorRepository, ModelRepository}
import utils.errors.{AlreadyExistsError, DatabaseTimeoutError, NotFoundError, ServiceError}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, TimeoutException}

class CarService @Inject()(repository: CarRepository, colorRepository: ColorRepository, modelRepository: ModelRepository) {
  def create(car: CarDto): Either[ServiceError, Car] = {
    try {
      val colorFindFuture = colorRepository.findByName(car.color)
      val modelFindFuture = modelRepository.findByName(car.model)
      val carWithSameNumberFuture = repository.findCarByNumber(car.number)
      implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3))

      val colorModelAndCarFuture = for {
        color <- colorFindFuture
        model <- modelFindFuture
        carWithSameNumber <- carWithSameNumberFuture
      } yield (color, model, carWithSameNumber)
      val colorModelAndCar = Await.result(colorModelAndCarFuture, 8.seconds)

      colorModelAndCar match {
        case (_, _, Some(_)) => Left(AlreadyExistsError(s"""Car with number "${car.number}" already exists"""))
        case (None, None, _) => Left(NotFoundError(s"""Color "${car.color}" and model "${car.model}" not found"""))
        case (None, _, _) => Left(NotFoundError(s"""Color "${car.color}" not found"""))
        case (_, None, _) => Left(NotFoundError(s"""Model "${car.model}" not found"""))
        case (Some(color), Some(model), _) =>
          val creationFuture = repository.create(car.copy(color = color.name, model = model.name))
          val creationResult = Await.result(creationFuture, 10.seconds)
          Right(creationResult)
      }
    }
    catch {
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }

  def read(carsRequestParams: Option[CarsRequestParams]): Either[ServiceError, Seq[Car]] = {
    try{
      val cars = Await.result(repository.list(carsRequestParams), 5.seconds)
      Right(cars)
    }
    catch{
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }

    def statistics: Either[ServiceError, CarStatistics] = {
    try{
      val total = Await.result(repository.total, 3.seconds)
      if (total == 0) {
        Right(CarStatistics(total, None, None))
      } else {
        implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))
        val carsFuture = for {
          firstCar <- repository.firstCar
          lastCar <- repository.lastCar
        } yield (firstCar, lastCar)
        val (firstCar, lastCar) = Await.result(carsFuture, 5.seconds)
        Right(CarStatistics(total, firstCar.map(_.createdAt), lastCar.map(_.createdAt)))
      }
    }
    catch{
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }

  def delete(id: Int): Either[ServiceError, Int] = {
    try{
      val deletionResult = Await.result(repository.delete(id), 5.seconds)
      if (deletionResult == 0) Left(NotFoundError(s"Car with id=${id} was not found"))
      else Right(id)
    }
    catch{
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }
}
