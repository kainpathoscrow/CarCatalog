package api

import controllers.CarController

import javax.inject.Inject

import play.api.routing.{Router, SimpleRouter}
import play.api.routing.sird._

class CarRouter @Inject()(controller: CarController) extends SimpleRouter {
  override def routes: Router.Routes = {
    case GET(p"/") => controller.read

    case POST(p"/") => controller.create

    case DELETE(p"/${int(id)}") => controller.delete(id)
  }
}
