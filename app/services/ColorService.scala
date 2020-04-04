package services

import javax.inject.Inject
import repositiories.ColorRepository
import utils.errors.{DatabaseTimeoutError, ServiceError}

import scala.concurrent.duration._
import scala.concurrent.{Await, TimeoutException}

class ColorService @Inject()(repository: ColorRepository){
  def listAllColorNames: Either[ServiceError, Seq[String]] = {
    try{
      Right(Await.result(repository.listAll, 5.seconds).map(_.name))
    }
    catch{
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }
}
