package repositiories

import javax.inject.Inject
import models.{Color, Model}
import play.api.cache.AsyncCacheApi
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class ModelRepository @Inject() (cache: AsyncCacheApi, dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ModelTable(tag: Tag) extends Table[Model](tag, "Model") {
    def name = column[String]("name")

    def * = (name) <> (Model.apply, Model.unapply)
  }

  private val models = TableQuery[ModelTable]

  def listAll: Future[Seq[Model]] = db.run {
    models.result
  }

  def findByName(name: String): Future[Option[Model]] = cache.getOrElseUpdate[Option[Model]]("model." + name, 5.minutes){
    db.run {
      models.filter(_.name.toUpperCase === name.toUpperCase).result.headOption
    }
  }
}
