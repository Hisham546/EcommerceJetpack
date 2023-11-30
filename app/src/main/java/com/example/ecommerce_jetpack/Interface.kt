import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun fetchData():  List<DataModel>
}