package org.pinky.util

import java.io._

object Serializer {
  def serializeToFile(obj: AnyRef, filePath: String) {
     val out = new FileOutputStream(filePath)
     val oos = new ObjectOutputStream(out)
     oos.writeObject(obj)
     oos.close()
     println("just saved "+obj.toString+" file:"+filePath)
  }

  def deserializeFrom[T](fileName: String): T = {
    val file = new java.io.File(fileName)
    val classLoader = this.getClass.getClassLoader
    if (file.exists) {
      val fin = new FileInputStream(file)
      val ois = new ObjectInputStream(fin) {
        override protected def resolveClass(objectStreamClass: ObjectStreamClass): Class[_] = {
          Class.forName(objectStreamClass.getName(), true, classLoader)
        }
      }
      val errors = ois.readObject().asInstanceOf[T]
      ois.close()
      errors
    }else throw new RuntimeException ("could not desarialize from "+ fileName +" because it did not exist")
  }
}