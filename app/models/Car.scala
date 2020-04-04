package models

import java.sql.Timestamp
import play.api.libs.json._

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
