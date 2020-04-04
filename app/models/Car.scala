package models

import java.sql.Timestamp
import play.api.libs.json._

case class Car(id: Int, model: String, color: String, number: String, manufactureYear: Int, createdAt: Timestamp)
object Car {
  implicit val format = Json.writes[Car]
}
case class CarDto(model: String, color: String, number: String, manufactureYear: Int)
object CarDto {
  implicit val writes = Json.reads[CarDto]
}
