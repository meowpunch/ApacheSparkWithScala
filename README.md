# Apache Spark with Scala
- hands on exercises of an [udemy course](https://www.udemy.com/course/apache-spark-with-scala-hands-on-with-big-data/)
    - [spark core, Resilient Distributed Dataset](https://spark.apache.org/docs/latest/rdd-programming-guide.html#rdd-persistence)
    - [spark SQL, Datasets and Dataframes](https://spark.apache.org/docs/latest/sql-programming-guide.html)
      - [item based collaborative filtering](https://en.wikipedia.org/wiki/Item-item_collaborative_filtering), recommendation system
    - [spark ML, machine learning](https://spark.apache.org/docs/latest/ml-guide.html)
    - [spark streaming, DStreams](https://spark.apache.org/docs/latest/streaming-programming-guide.html)
    - [Structured Streaming](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)

## Data
- social network
  - fakefriends.csv: id, name, age, # of friends
  
- e-commerce 
  - customer-orders.csv: customerId, productId, priceId
  
- superhero
  - Marvel-names.txt: heroId, heroName
  - Marvel-graph.txt: heroId, connectionId...
  

## Exercise
### Predict Real Estate Values with Decision Tree Model
predict real estate values per unit area base on several features
- features(X): HouseAge, DistanceToMRT, NumberConvenienceStores, Latitude, Longitude
- label(y): PriceOfUnitArea

### Realtime processing with Twitter Streaming data
#### Twitter API
[`CHANGELOG, 15 Nov 2021`](https://developer.twitter.com/en/updates/changelog) : Today, we are announcing that Twitter API v2 is now the primary version of the Twitter API. We have launched enough endpoints and functionality into Twitter API v2 to satisfy the needs of 90% of all existing Apps built on the Twitter API.
- sign up Twitter Developer site and get Keys
  - consumerKey: API Key 
  - consumerSecret: API Secret
```txt
consumerKey 12345
consumerSecret 12345
accessToken 12345
accessTokenSecret 12345 
```





## Reference
- [Decision Tree with Spark](https://spark.apache.org/docs/latest/ml-classification-regression.html#decision-tree-regression)
