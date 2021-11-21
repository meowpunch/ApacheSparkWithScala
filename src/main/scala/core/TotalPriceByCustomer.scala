package core

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object TotalPriceByCustomer {
  val DELIMITER = ","

  /**
   * @param line row in csv
   * @return (customerId, price)
   */
  def parseLine(line: String): (Int, Double) = {
    val field = line.split(DELIMITER)
    (field(0).toInt, field(2).toFloat)
  }


  def main(args: Array[String]): Unit = {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "FriendsByAge")

    // Load each line of the source data into an RDD
    val res = sc.textFile("data/customer-orders.csv")
      // (customerId, price)
      .map(parseLine)
      // sum price by customerId(Key)
      .reduceByKey((l, r) => l + r)
      .sortBy(_._2) // .map(x => (x._2, x._1)).sortByKey()
      .collect()

    // Print the results
    res.foreach(println)
  }

}
