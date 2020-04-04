package utils.errors

import play.api.libs.json.{JsValue, Json}

case class AppError(error: String){
  def asJson: JsValue = Json.toJson(this)(AppError.writes)
}
object AppError{
  implicit val writes = Json.writes[AppError]
}