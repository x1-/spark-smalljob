package com.inkenkun.x1.spark.smalljob

import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkContext
import org.apache.spark.sql.{SaveMode, SQLContext}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object Job {

  val identifier  = this.getClass.getCanonicalName
  val config      = ConfigFactory.load.getConfig( "smalljob" )
  val sparkConfig = ConfigFactory.load.getConfig( "spark" ).toSparkConf.setAppName( s"$identifier" )

  def main( args: Array[String] ): Unit = {

    if ( args.length < 2 ) {
      usage
      System.exit(1)
    }

    val input  = args(0)
    val output = args(1)

    val sc = new SparkContext( sparkConfig )
    implicit val sqlContext = new SQLContext( sc )
    import sqlContext.implicits._

    val df = sqlContext.read.format( "com.databricks.spark.csv" ).option( "header", "true" ).load( input )
    val aggregate = df.select(
      'REG_DATE,
      'SEX_ID,
      'AGE cast IntegerType as "AGE",
      regexp_replace( 'WITHDRAW_DATE, "^NA", "" ),
      'PREF_NAME,
      'USER_ID_hash )

    df.write.format( "json" ).mode( SaveMode.Overwrite ).save( output )

  }
  def usage = {
    println( "Usage: Job <input> <output>" )
  }
}
