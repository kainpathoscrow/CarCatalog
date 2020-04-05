package controllers

import play.api.cache.Cached
import javax.inject._
import play.api.mvc._

import scala.concurrent.duration._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val cached: Cached, val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = cached.apply(_ => "homePage", 1.hour.toSeconds.toInt) {
    Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())
    }
  }
}
