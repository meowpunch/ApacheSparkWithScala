package sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.types.{FloatType, IntegerType, StructType}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.{round, sum}

object TotalPriceByCustomerDataset {
  val DELIMITER = ","

  case class Order(customerId: Int, productId: Int, amountSpent: Float)

  def main(args: Array[String]): Unit = {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Use new SparkSession interface in Spark 2.0
    val spark = SparkSession
      .builder
      .appName("SparkSQL")
      .master("local[*]")
      .getOrCreate()

    val orderSchema = new StructType()
      .add("customerId", IntegerType, nullable = false)
      .add("productId", IntegerType, nullable = false)
      .add("amountSpent", FloatType, nullable = false)

    // Convert our csv file to a DataSet, using our Person case
    // class to infer the schema.
    import spark.implicits._
    val orders: Dataset[Order] = spark.read
      .schema(orderSchema)
      .csv("data/customer-orders.csv")
      .as[Order]

    orders.drop("productId")
      .groupBy("customerId")
      .agg(round(sum("amountSpent"), 2).alias("totalSpent"))
      .sort("totalSpent")
      .show()
  }

}
