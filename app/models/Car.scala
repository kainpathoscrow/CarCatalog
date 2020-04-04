package models

import java.sql.Timestamp

case class Car(id: Int, model: String, color: String, number: String, manufactureYear: Int, createdAt: Timestamp)
case class CarDto(model: String, color: String, number: String, manufactureYear: Int)