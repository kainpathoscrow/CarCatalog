package services

import javax.inject.Inject
import repositiories.ModelRepository
import scala.concurrent.duration._

import scala.concurrent.Await

class ModelService @Inject()(repository: ModelRepository){
  def listAllModelNames(): Seq[String] = Await.result(repository.listAll(), 15.seconds).map(_.name)
}
