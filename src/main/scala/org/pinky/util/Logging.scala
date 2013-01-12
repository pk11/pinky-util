package org.pinky.util

import org.apache.log4j.Logger

trait Log {
  def logger: Logger
  def debug(message: String) { logger.debug(message) }
  def warn(message: String) { logger.warn(message) }
  def error(message: String) { logger.error(message) }
}

private[util] object SingletonLogger {
  var cache = collection.mutable.Map[Class[_], Logger]()
  def logger(c : Class[_]) = cache.getOrElseUpdate(c, Logger.getLogger(c))
}

trait CachedLogging extends Log {
  def logger = SingletonLogger.logger(this.getClass)
}

trait Logging extends Log {
  def logger = Logger.getLogger(this.getClass)
}

