package roiattia.com.imagessearch.data.mapper

// Non-nullable to Non-nullable
interface ListMapper<I, O> : Mapper<List<I>, List<O>>

class ListMapperImpl<I, O>(
    private val mapper: Mapper<I, O>
) : ListMapper<I, O> {
    override fun map(source: List<I>): List<O> {
        return source.map { mapper.map(it) }
    }
}
