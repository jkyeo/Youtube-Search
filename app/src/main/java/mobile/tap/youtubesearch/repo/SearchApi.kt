package mobile.tap.youtubesearch.repo

import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * @desc Tips: Api for search
 * @author Tim Yeo
 * @date 2021/10/17 20:23
 */
interface SearchApi {
//    @Headers("User-Agent: Mozilla/5.0 (Linux; Android 11; Android SDK built for x86 Build/RSR1.210210.001.A1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/83.0.4103.106 Mobile Safari/537.36")
    @GET("/results")
    fun search(@Query("search_query") keyword: String): Maybe<Response<ResponseBody>>
}