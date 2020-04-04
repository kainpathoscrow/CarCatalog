package api

import controllers.ColorController
import javax.inject.Inject
import play.api.routing.{Router, SimpleRouter}
import play.api.routing.sird._

class ColorRouter @Inject()(controller: ColorController) extends SimpleRouter {
  override def routes: Router.Routes = {
    case GET(p"/") =>
      controller.read
  }
}
