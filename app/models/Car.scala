package models

import java.sql.Timestamp
import play.api.libs.json._

// TODO split file to one file per class+object
case class Car(id: Int, model: String, color: String, number: String, manufactureYear: Int, createdAt: Timestamp)
object Car {
  implicit val writes = Json.writes[Car]
}
case class CarDto(model: String, color: String, number: String, manufactureYear: Int)
object CarDto {
  implicit val reads = Json.reads[CarDto]
}

case class CarStatistics(totalCars: Int, firstCarCreationUtc: Option[Timestamp], lastCarCreationUtc: Option[Timestamp])
object CarStatistics {
  implicit val writes = Json.writes[CarStatistics]
}

// TODO paging (page, perPage params)
case class CarsRequestParams(model: Option[List[String]], color: Option[List[String]], number: Option[String],
                             yearMin: Option[Int], yearMax: Option[Int],
                             sortedBy: Option[String], sortedAsc: Option[Boolean])
object CarsRequestParams {
  implicit val reads = Json.reads[CarsRequestParams]
}
