package models

class Func(val name: String, val start: Int, val end: Int, val body: Option[String], val jarName: String) extends Serializable {

	def toList = {
		List(name, start.toString, end.toString, body.map(x => x).getOrElse(""), jarName)
	}
}

object Func {
	def fromList(l: List[String]) = {
		var li = l
		val name  = li.head
		li = li drop 1
		val start = li.head.toInt
		li = li drop 1
		val end   = li.head.toInt
		li = li drop 1
		val body = li.head match {
			case "empty" => None
			case x       => Some(x)
		}
		li = li drop 1
		val jarName = li.head
		new Func(name, start, end, body, jarName)
	}
}
