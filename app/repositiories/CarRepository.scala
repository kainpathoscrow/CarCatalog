package repositiories

import java.sql.Timestamp

import javax.inject.Inject
import models.Car
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

import scala.concurrent.{ExecutionContext, Future}

class CarRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)  {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class CarTable(tag: Tag) extends Table[Car](tag, "Car") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def model = column[String]("model")
    def color = column[String]("color")
    def number = column[String]("number")
    def manufactureYear = column[Int]("manufacture_year")
    def createdAt = column[Timestamp]("created_at", SqlType("TIMESTAMP NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc')"))

    def * = (id, model, color, number, manufactureYear, createdAt) <> (Car.tupled, Car.unapply)
  }

  private val cars = TableQuery[CarTable]

  def create(model: String, color: String, number: String, manufactureYear: Int) : Future[Car] = db.run {
    (cars.map(c => (c.model, c.color, c.number, c.manufactureYear))
      returning cars.map(car => (car.id, car.createdAt))
      into ((params, returns) => Car(returns._1, params._1, params._2, params._3, params._4, returns._2))
      ) += (model, color, number, manufactureYear)
  }

  def delete(id: Int) = db.run {
    cars.filter(_.id === id).delete
  }

  def listAll(): Future[Seq[Car]] = db.run {
    cars.result
  }
}
