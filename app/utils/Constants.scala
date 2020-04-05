package utils

import java.util.Calendar

object Constants {
  val minManufactureYear = 1885
  val maxManufactureYear = Calendar.getInstance.get(Calendar.YEAR) + 5 // + 5 for upcoming models
}
