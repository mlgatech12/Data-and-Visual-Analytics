/*
Referenced - https://docs.databricks.com/spark/latest/dataframes-datasets/introduction-to-dataframes-scala.html#create-dataframes
for output file : https://community.hortonworks.com/questions/44894/how-do-you-write-a-rdd-as-a-tab-delimited-file-in.html
*/
package edu.gatech.cse6242

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._

object Q2 {
	case class LargeGraph(edge: Int,  weight: Int)
	def main(args: Array[String]) {
    	val sc = new SparkContext(new SparkConf().setAppName("Q2"))
		val sqlContext = new SQLContext(sc)
		import sqlContext.implicits._

    	// read the file
    	val file = sc.textFile("hdfs://localhost:8020" + args(0))
		
		val incoming = file.map(_.split("\t"))
                    .map(p => LargeGraph(p(1).trim.toInt, p(2).trim.toInt))
                    .toDF("edgei","weight")
                    .filter($"weight" >= 5)
        
        val outgoing = file.map(_.split("\t"))
                    .map(p => LargeGraph(p(0).trim.toInt, p(2).trim.toInt))
                    .toDF("edgeo","weight")
                    .filter($"weight" >= 5)
        
        val aggIncoming = incoming.groupBy($"edgei").agg(sum($"weight")).toDF("edgei", "incoming_weight")
        val aggOutgoing = outgoing.groupBy($"edgeo").agg(sum($"weight")).toDF("edgeo", "outgoing_weight")
        
        /* combined df */
        val combinedDf = aggIncoming.join(aggOutgoing, aggIncoming.col("edgei") === aggOutgoing.col("edgeo"), "outer")
        
        /* replace null values */
        val combinedDfNoNull = combinedDf.na.fill(0, Seq("incoming_weight", "outgoing_weight")).withColumn("edge", coalesce($"edgei", $"edgeo"))
        
        /* subtract the weights*/
        val output = combinedDfNoNull.select($"edge",  $"incoming_weight" -  $"outgoing_weight")
        
       	// store output on given HDFS path.
    	output.map(_.mkString("\t")).saveAsTextFile("hdfs://localhost:8020" + args(1))
    	
    	//file.saveAsTextFile("hdfs://localhost:8020" + args(1))
  	}
}
