package ml

import org.apache.log4j._
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.DecisionTreeRegressor
import org.apache.spark.sql._

object RealEstateWithDecisionTree {

  case class RealEstate(No: Int, TransactionDate: Double, HouseAge: Double,
                        DistanceToMRT: Double, NumberConvenienceStores: Int,
                        Latitude: Double, Longitude: Double, PriceOfUnitArea: Double)

  /** Our main function where the action happens */
  def main(args: Array[String]) {
    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("RealEstateWithDecisionTree")
      .master("local[*]")
      .getOrCreate()

    // Load up our page speed / amount spent data in the format required by MLLib
    // (which is label, vector of  features)

    import spark.implicits._
    val dsRaw = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/realestate.csv")
      .as[RealEstate]

    val assembler = new VectorAssembler().
      setInputCols(Array("HouseAge", "DistanceToMRT", "NumberConvenienceStores", "Latitude", "Longitude")).
      setOutputCol("features")

    val df = assembler.transform(dsRaw)
      .select("features", "PriceOfUnitArea")
      .withColumnRenamed("PriceOfUnitArea", "label")

    df.show()

    // Let's split our data into training data and testing data
    val Array(trainingDF, testDF) = df.randomSplit(Array(0.7, 0.3))

    // Now create our linear regression model
    val lir = new DecisionTreeRegressor()
      .setFeaturesCol("features")
      .setLabelCol("label")

    // Train the model using our training data
    val model = lir.fit(trainingDF)

    // test dataframe:
    val predictions = model.transform(testDF).cache()
    predictions.show()

    // Select (prediction, true label) and compute test error.
    val evaluator = new RegressionEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("rmse")
    val rmse = evaluator.evaluate(predictions)
    println(s"Root Mean Squared Error (RMSE) on test data = $rmse")
    println(s"Learned regression tree model:\n ${model.toDebugString}")

    // Stop the session
    spark.stop()

  }
}