package mix

import org.apache.log4j._
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

/** Find the superhero with the most co-appearances. */
object MostObscureSuperhero {

  // Function to extract the hero ID and number of connections from each line
  def countCoOccurrences(line: String): (Int, Int) = {
    val elements = line.split("\\s+")
    (elements(0).toInt, elements.length - 1)
  }

  case class SuperHero(id: Int, name: String)

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkSession using every core of the local machine
    val spark = SparkSession
      .builder
      .appName("MostPopularSuperhero")
      .master("local[*]")
      .getOrCreate()

    val superHeroSchema = new StructType()
      .add("id", IntegerType, nullable = false)
      .add("name", StringType, nullable = false)

    import spark.implicits._
    val names = spark.read
      .schema(superHeroSchema)
      .option("sep", " ")
      .csv("data/Marvel-names.txt")
      .as[SuperHero]

    // Load up the superhero co-appearance data
    val linesRDD = spark.sparkContext.textFile("data/Marvel-graph.txt")

    // Convert to (heroID, number of connections) Dataset and sum by Id
    val connections = linesRDD.map(countCoOccurrences).toDF("id", "connections")
      .groupBy("id").agg(sum("connections").alias("connections"))

    // Compute the actual smallest number of connections in the dataset
    val minConnections = connections.agg(min("connections")).first().getLong(0)

    // List the names of all superheroes with only one connection
    connections
      .filter($"connections" === minConnections)
      .join(names, "id")
      .select("name")
      .show()

    spark.stop()

  }
}
