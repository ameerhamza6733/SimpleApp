package com.example.myapplication

import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.*

private val TAG="networkBoundResource"
fun <ResultType, RequestType> networkBoundResource(
    query: () -> ResultType?,
    getFromNetwork: suspend () -> RequestType,
    saveToLocalDb: suspend (ResultType) -> Unit,
    shouldUpdateCach: (ResultType?) -> Boolean = { true }

) = flow {
    emit(Resource.Loading("Loading from disk",null))
    val dataFromLocal=query()
   if (shouldUpdateCach(dataFromLocal)){
        try {
            emit(Resource.Loading("Loading from network",null))
           val dataFromNetwork= getFromNetwork()
            saveToLocalDb(dataFromNetwork as ResultType)
           emit( Resource.Success(dataFromNetwork) )
        }catch (throwable : Throwable){
            emit( Resource.Error(throwable,dataFromLocal,throwable.message.toString()) )
        }
    }else{
      emit(  Resource.Success(dataFromLocal) )
    }


}