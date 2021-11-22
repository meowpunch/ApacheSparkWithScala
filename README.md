# Apache Spark with Scala
- hands on exercises of [an udemy course](https://www.udemy.com/course/apache-spark-with-scala-hands-on-with-big-data/)
    - [spark core, Resilient Distributed Dataset](https://spark.apache.org/docs/latest/rdd-programming-guide.html#rdd-persistence)
    - [spark SQL, Datasets and Dataframes](https://spark.apache.org/docs/latest/sql-programming-guide.html)
      - [item based collaborative filtering](https://en.wikipedia.org/wiki/Item-item_collaborative_filtering), recommendation system
    - [spark ML, machine learning](https://spark.apache.org/docs/latest/ml-guide.html)
  
## Data
- social network
  - fakefriends.csv: id, name, age, # of friends
  
- e-commerce 
  - customer-orders.csv: customerId, productId, priceId
  
- superhero
  - Marvel-names.txt: heroId, heroName
  - Marvel-graph.txt: heroId, connectionId...
  

## Exercise
### Predict Real Estate Values with Decision Tree
predict the price per unit area base on several features
- features(X): HouseAge, DistanceToMRT, NumberConvenienceStores, Latitude, Longitude
- label(y): PriceOfUnitArea






## Reference
- [Decision Tree with Spark](https://spark.apache.org/docs/latest/ml-classification-regression.html#decision-tree-regression)
