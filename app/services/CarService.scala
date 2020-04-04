package services

import java.util.concurrent.Executors

import javax.inject.Inject
import models.{Car, CarDto, CarStatistics, CarsRequestParams}
import org.postgresql.util.PSQLException
import repositiories.{CarRepository, ColorRepository, ModelRepository}
import utils.errors.{AlreadyExistsError, DatabaseTimeoutError, NotFoundError, ServiceError}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, TimeoutException}

class CarService @Inject()(repository: CarRepository, colorRepository: ColorRepository, modelRepository: ModelRepository) {
  def create(car: CarDto): Either[ServiceError, Car] = {
    try {
      val colorAndModel = for {
        color <- Await.result(colorRepository.findByName(car.color), 5.seconds)
        model <- Await.result(modelRepository.findByName(car.model), 5.seconds)
      } yield (color, model)

      colorAndModel match {
        case None => Left(NotFoundError(s"""Color "${car.color}" or model "${car.model}" not found"""))
        case Some((color, model)) => Right(Await.result(repository.create(car.copy(color = color.name, model = model.name)), 10.seconds))
      }
    }
    catch {
      case _: TimeoutException => Left(DatabaseTimeoutError)
      case e: PSQLException if(e.getSQLState == "23505") => Left(AlreadyExistsError)
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
