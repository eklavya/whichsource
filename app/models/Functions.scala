package models

import scala.collection.mutable.{ HashMap, MultiMap, Set }
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import com.typesafe.config.ConfigFactory
import java.io.FileNotFoundException


trait FunctionStore {
	var funcMap: FuncMap = _

  def getPath: String

	def load = {
     funcMap = FuncMap.load(getPath)
	}
	
	def add(fqn: String, f: Func) = {
		funcMap.addBinding(fqn, f)
	}

	def holds(conds: List[(String, String)]): Boolean = {
		!conds.exists{case(f, l) => !funcMap.entryExists(f, x => (l.toInt >= x.start) && (l.toInt <= x.end))}
	}

	def findFunc(f: String, l: Int): List[Func] = {
		funcMap(f).filter(x => (l.toInt >= x.start) && (l.toInt <= x.end)).toList
	}

	def getFunc(f: String, jarName: String): Option[List[Func]] = {
    funcMap.get(f).map(x => x.filter(_.jarName == jarName).toList)
	}

	def store {
		funcMap.store(getPath)
	}
}

object Functions extends FunctionStore {
  def getPath = ConfigFactory.load.getString("mapBackup")
}

class FuncMap extends HashMap[String, Set[Func]] with MultiMap[String, Func] with Serializable {

	def enMap = {
		val m = new HashMap[String, Set[List[String]]] with MultiMap[String, List[String]] with Serializable
		this foreach { x =>
			x._2 foreach (y => m.addBinding(x._1, y.toList))
		}
		m
	}

	def store(toPath: String) = {
		val m = enMap
    try {
		  val fos = new FileOutputStream(toPath)
		  val oos = new ObjectOutputStream(fos)
		  oos.writeObject(m)
		  oos.close()
		  fos.close
		} catch {
		  case e: FileNotFoundException =>
		    val f = new java.io.File(toPath)
		    val fos = new FileOutputStream(f)
		    val oos = new ObjectOutputStream(fos)
		    oos.writeObject(m)
		    oos.close
		    fos.close
		}
	}
}

object FuncMap {

	def deMap(m: HashMap[String, Set[List[String]]] with MultiMap[String, List[String]] with Serializable) = {
		val fm = new FuncMap
		m foreach { x =>
			x._2 foreach (y => fm.addBinding(x._1, Func.fromList(y)))
		}
		fm
	}

	def load(fromPath: String): FuncMap = {
		try {
      val fis     = new FileInputStream(fromPath)
		  val ois     = new ObjectInputStream(fis)
		  val m       = ois.readObject.asInstanceOf[HashMap[String, Set[List[String]]] with MultiMap[String, List[String]]]
		  ois.close
		  fis.close
		  deMap(m)
		} catch {
		  case e: FileNotFoundException => new FuncMap
		}
	}
}

