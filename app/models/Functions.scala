package models

import scala.collection.mutable.{ HashMap, MultiMap, Set }
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import com.typesafe.config.ConfigFactory
import java.io.FileNotFoundException


object Functions {
	var funcMap: FuncMap = _

	def load = {
     funcMap = FuncMap.load 
	}
	
	def add(fqn: String, f: Func) = {
		funcMap.addBinding(fqn, f)
	}

	def holds(conds: List[(String, String)]) = {
		!conds.exists{case(f, l) => !funcMap.entryExists(f, x => (l.toInt >= x.start) && (l.toInt <= x.end))}
	}

	def findFunc(f: String, l: Int) = {
		funcMap(f).filter(x => (l.toInt >= x.start) && (l.toInt <= x.end)).toList
	}

	def getFunc(f: String, jarName: String) = {
		funcMap.get(f).map(x => x.filter(_.jarName == jarName).head)
	}

	def store {
		funcMap.store
	}
}

class FuncMap extends HashMap[String, Set[Func]] with MultiMap[String, Func] with Serializable {
	
	def enMap = {
		val m = new HashMap[String, Set[List[String]]] with MultiMap[String, List[String]] with Serializable
		this foreach { x => 
			x._2 foreach (y => m.addBinding(x._1, y.toList))
		}
		m
	}

	def store = {
		val m = enMap
		try {
		  val fos = new FileOutputStream(ConfigFactory.load.getString("mapBackup"))
		  val oos = new ObjectOutputStream(fos)
		  oos.writeObject(m)
		  oos.close()
		  fos.close
		} catch {
		  case e: FileNotFoundException => 
		    val f = new java.io.File(ConfigFactory.load.getString("mapBackup"))
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

	def load: FuncMap = {
		try {
		  val fis     = new FileInputStream(ConfigFactory.load.getString("mapBackup"))
		  val ois     = new ObjectInputStream(fis)
		  val m       = ois.readObject.asInstanceOf[HashMap[String, Set[List[String]]] with MultiMap[String, List[String]]]
		  ois.close
		  fis.close
		  deMap(m)
		} catch {
		  case e: FileNotFoundException => new FuncMap
		  // case e: NoSuchFileException => println("exception thrown")
		} 
	}
}

