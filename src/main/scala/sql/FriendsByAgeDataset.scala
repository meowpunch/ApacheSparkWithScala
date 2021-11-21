package sql


import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.{avg, round}


/** Compute the average number of friends by age in a social network. */
object FriendsByAgeDataset {
  case class Person(id: Int, name: String, age: Int, friends: Int)

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Use new SparkSession interface in Spark 2.0
    val spark = SparkSession
      .builder
      .appName("SparkSQL")
      .master("local[*]")
      .getOrCreate()

    // Convert our csv file to a DataSet, using our Person case
    // class to infer the schema.
    import spark.implicits._
    val people: Dataset[Person] = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/fakefriends.csv")
      .as[Person]

    // There are lots of other ways to make a DataFrame.
    // For example, spark.read.json("json file path")
    // or sqlContext.table("Hive table name")

    println("Here is our inferred schema:")
    people.printSchema()

    println("Results:")
    people
      .select("age", "friends")
      .groupBy("age")
      .avg("friends")
      .sort("age")
      .show()

    println("Well Formatted Results :")
    people
      .select("age", "friends")
      .groupBy("age")
      .agg(round(avg("friends"), 2).alias("friends_avg"))
      .sort("age")
      .show()

    spark.stop()
  }

}
  