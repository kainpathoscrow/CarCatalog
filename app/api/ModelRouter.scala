package api

import controllers.{ModelController}
import javax.inject.Inject
import play.api.routing.{Router, SimpleRouter}
import play.api.routing.sird._

class ModelRouter @Inject()(controller: ModelController) extends SimpleRouter {
  override def routes: Router.Routes = {
    case GET(p"/") =>
      controller.read
  }
}
