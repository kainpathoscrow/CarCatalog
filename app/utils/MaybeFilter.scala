package utils

// https://gist.github.com/cvogt/9193220#gistcomment-2121674
case class MaybeFilter[X, Y, C[_]](val query: slick.lifted.Query[X, Y, C]) {
  def filter[T,R:slick.lifted.CanBeQueryCondition](data: Option[T])(f: T => X => R) = {
    data.map(v => MaybeFilter(query.withFilter(f(v)))).getOrElse(this)
  }
}