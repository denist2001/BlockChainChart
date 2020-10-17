package com.codingchallenge.blockchainchart.domain

class RepositoryException(val error: Error) : RuntimeException() {
    enum class Error(val message: String) {
        NETWORK_ERROR("Network error"),
        CONNECTION_ERROR("Connection error"),
        PARSING_ERROR("Data can't be parsed"),
        EMPTY_DATA("No data received"),
        UNKNOWN_ERROR("Unknown error")
    }
}