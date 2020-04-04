package services

import javax.inject.Inject
import repositiories.ColorRepository
import scala.concurrent.duration._

import scala.concurrent.Await

class ColorService @Inject()(repository: ColorRepository){
  def listAllColorNames(): Seq[String] = Await.result(repository.listAll(), 15.seconds).map(_.name)
}
