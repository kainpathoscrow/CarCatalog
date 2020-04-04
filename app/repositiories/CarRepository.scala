package repositiories

import java.sql.Timestamp

import javax.inject.Inject
import models.{Car, CarDto, CarsRequestParams}
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

    def * = (id, model, color, number, manufactureYear, createdAt) <> ((Car.apply _).tupled, Car.unapply)
  }

  private val cars = TableQuery[CarTable]

  def create(car: CarDto) : Future[Car] = db.run {
    (cars.map(c => (c.model, c.color, c.number, c.manufactureYear))
      returning cars.map(car => (car.id, car.createdAt))
      into ((params, returns) => Car(returns._1, params._1, params._2, params._3, params._4, returns._2))
      ) += (car.model, car.color, car.number, car.manufactureYear)
  }

  def delete(id: Int) = db.run {
    cars.filter(_.id === id).delete
  }

  def list(carsRequestParams: Option[CarsRequestParams]): Future[Seq[Car]] = db.run {
    cars.result
  }

  def total: Future[Int] = db.run {
    cars.length.result
  }

  // TODO firstCar can be expressed through list function + headOption
  def firstCar: Future[Option[Car]] = db.run {
    cars.sortBy(_.createdAt.asc).take(1).result.headOption
  }

  // TODO lastCar can be expressed through list function + headOption
  def lastCar: Future[Option[Car]] = db.run {
    cars.sortBy(_.createdAt.desc).take(1).result.headOption
  }
}
