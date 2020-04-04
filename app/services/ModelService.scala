package services

import javax.inject.Inject
import repositiories.ModelRepository
import utils.{DatabaseTimeoutError, ServiceError}

import scala.concurrent.duration._
import scala.concurrent.{Await, TimeoutException}

class ModelService @Inject()(repository: ModelRepository){
  def listAllModelNames(): Either[ServiceError, Seq[String]] = {
    try{
      Right(Await.result(repository.listAll(), 15.seconds).map(_.name))
    }
    catch{
      case _: TimeoutException => Left(DatabaseTimeoutError)
    }
  }
}
