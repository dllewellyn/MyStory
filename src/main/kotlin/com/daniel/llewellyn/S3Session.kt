package com.daniel.llewellyn

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

class S3Session(val sessionId : String) {

    private val s3client: AmazonS3

    init {
        val credentials = BasicAWSCredentials(
                System.getenv("S3_ACCESS_ID"),
                System.getenv("S3_ACCESS_PASSWORD")
        )

        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build()
    }

    fun retrieveSession(): Session {
        return try {
            val array = s3client.getObject(BUCKET_NAME, this.sessionId).objectContent.readBytes()
            ObjectMapper().readValue(array, Session::class.java)
        } catch (ex : Exception) {
            Session(mutableMapOf())
        }
    }

    @Throws(JsonProcessingException::class)
    fun updateSession( results: Session) {
        val mapper = ObjectMapper()
        s3client.putObject(BUCKET_NAME, this.sessionId, mapper.writeValueAsString(results))
    }

    fun clear() {
        s3client.deleteObject(DeleteObjectRequest(BUCKET_NAME, this.sessionId))
    }

    companion object {
        val BUCKET_NAME = "alexastore"
    }
}