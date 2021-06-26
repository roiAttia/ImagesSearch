package roiattia.com.imagessearch.data.mapper

interface Mapper<T, R> {

    fun map(source: T): R
}