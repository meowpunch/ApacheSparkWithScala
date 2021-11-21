package sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/** Compute the average number of friends by age in a social network. */
object FriendsByAgeSQL {
  case class Person(id: Int, name: String, age: Int, friends: Int)

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Use SparkSession interface
    val spark = SparkSession
      .builder
      .appName("SparkSQL")
      .master("local[*]")
      .getOrCreate()

    // Load each line of the source data into an Dataset
    import spark.implicits._
    val schemaPeople = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/fakefriends.csv")
      .as[Person]

    schemaPeople.printSchema()

    schemaPeople.createOrReplaceTempView("people")

    val averageByAge = spark.sql("SELECT age, ROUND(AVG(friends), 2) FROM people GROUP BY age ORDER BY age")

    val results = averageByAge.collect()

    results.foreach(println)

    spark.stop()
  }

}
  