package ml

import org.apache.log4j._
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql._
import org.apache.spark.sql.types._

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
    // (which is label, vector of features)

    import spark.implicits._
    val dsRaw = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/realestate.csv")
      .as[RealEstate]

    val assembler = new VectorAssembler().
      setInputCols(Array("TransactionDate", "HouseAge", "DistanceToMRT", "NumberConvenienceStores", "Latitude", "Longitude")).
      setOutputCol("features")

    val df = assembler.transform(dsRaw)
      .select("PriceOfUnitArea", "features")

    df.show()

    // Let's split our data into training data and testing data
    val Array(trainingDF, testDF) = df.randomSplit(Array(0.8, 0.2))

    // Now create our linear regression model
    val lir = new LinearRegression()
      .setLabelCol("PriceOfUnitArea")

    // Train the model using our training data
    val model = lir.fit(trainingDF)

    // Now see if we can predict values in our test data.
    // Generate predictions using our linear regression model for all features in our 
    // test dataframe:
    val fullPredictions = model.transform(testDF).cache()
    fullPredictions.show()

    // Extract the predictions and the "known" correct labels.
    val predictionAndLabel = fullPredictions.select("prediction", "PriceOfUnitArea").collect()

    // Print out the predicted and actual values for each point
    for (prediction <- predictionAndLabel) {
      println(prediction)
    }

    // Stop the session
    spark.stop()

  }
}