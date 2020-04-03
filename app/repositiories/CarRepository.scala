package repositiories

import javax.inject.Inject
import models.Car
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class CarRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)  {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class CarTable(tag: Tag) extends Table[Car](tag, "cars") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def model = column[String]("model")
    def color = column[String]("color")
    def number = column[String]("number")
    def manufactureYear = column[Int]("manufacture_year")

    def * = (id, model, color, number, manufactureYear) <> (Car.tupled, Car.unapply)
  }

  private val cars = TableQuery[CarTable]
}
