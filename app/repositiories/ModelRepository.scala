package repositiories

import javax.inject.{Inject, Singleton}
import models.Model
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ModelRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ModelTable(tag: Tag) extends Table[Model](tag, "Model") {
    def name = column[String]("name")

    def * = (name) <> (Model.apply, Model.unapply)
  }

  private val models = TableQuery[ModelTable]

  def listAll(): Future[Seq[Model]] = db.run {
    models.result
  }
}
